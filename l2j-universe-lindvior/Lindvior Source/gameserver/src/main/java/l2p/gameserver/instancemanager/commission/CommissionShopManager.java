/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.instancemanager.commission;

import l2p.commons.math.SafeMath;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.dao.CommissionShopDAO;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.PcInventory;
import l2p.gameserver.model.items.TradeItem;
import l2p.gameserver.model.mail.Mail;
import l2p.gameserver.network.serverpackets.ExNoticePostArrived;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.commission.*;
import l2p.gameserver.network.serverpackets.components.IStaticPacket;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.templates.item.ItemTemplate;
import l2p.gameserver.templates.item.type.ExItemType;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author : Darvin
 */
public class CommissionShopManager {
    private static final Logger _log = LoggerFactory.getLogger(CommissionShopManager.class);

    private static final long MIN_FEE = 10000;
    private static final double REGISTRATION_FEE = 0.01;
    private static final double SALE_FEE = 0.5;
    private static final CommissionItemContainer container = new CommissionItemContainer();

    private static CommissionShopManager ourInstance = new CommissionShopManager();

    public static CommissionShopManager getInstance() {
        return ourInstance;
    }

    private CommissionShopManager() {
        restore();
    }

    public CommissionItemContainer getContainer() {
        return container;
    }

    private void restore() {
        container.restore();
        // CommissionShopDAO.getInstance();
    }

    /**
     * Показывает предметы, которые игрок может продать в комиссионном магазине
     *
     * @param player - игрок
     */
    public void showRegistrableItems(Player player) {
        ItemInstance[] items = player.getInventory().getItems();
        List<TradeItem> registrableItems = new ArrayList<TradeItem>(items.length);
        for (ItemInstance item : items) {
            if (item.canBeSold(player)) {
                registrableItems.add(new TradeItem(item));
            }
        }
        player.sendPacket(new ExResponseCommissionItemList(registrableItems));
    }

    /**
     * Открывает окно комиссионного магазина
     *
     * @param player - игрок, для которого открываем магазин
     */
    public void showCommission(Player player) {
        player.sendPacket(new ExShowCommission());
    }

    /**
     * Добавляет предмет в регистрацию на продажу.
     *
     * @param player    - игрок, для которого открываем окно
     * @param itemObjId - objectId продаваемого предмета
     */
    public void showCommissionInfo(Player player, int itemObjId) {
        ItemInstance item = player.getInventory().getItemByObjectId(itemObjId);
        if ((item != null) && item.canBeSold(player)) {
            player.sendPacket(new ExResponseCommissionInfo(item, 1));
        } else {
            player.sendPacket(new ExResponseCommissionInfo(item, 0));
        }
    }

    /**
     * Отправляет список вещей, которые стоят на продаже у игрока
     *
     * @param player - игрок, для которого показываем веши.
     */
    public void showPlayerRegisteredItems(Player player) {
        List<CommissionItemInfo> items = CommissionShopDAO.getInstance().getRegisteredItemsFor(player);

        if (items.size() == 0) {
            player.sendPacket(new ExResponseCommissionList(ExResponseCommissionList.EMPTY_LIST));
        } else {
            player.sendPacket(new ExResponseCommissionList(ExResponseCommissionList.PLAYER_REGISTERED_ITEMS, 0, items));
        }
    }

    /**
     * Регистрирует вещь в списке продаж
     *
     * @param player    - игрок, чья вещь регистрируется
     * @param objectId  - objectId вещи
     * @param item_name - название вещи (передает клиент)
     * @param price     - регистрируемая цена
     * @param count     - количество
     * @param sale_days - кол-во дней (0 - 1 день, 1 - 3 дня, 2 - 5 дней, 3 - 7 дней.)
     */
    public void registerItem(Player player, int objectId, String item_name, long price, long count, int sale_days) {
        PcInventory inventory = player.getInventory();
        ItemInstance item = inventory.getItemByObjectId(objectId);
        if ((item == null) || (item.getCount() < count) || !item.canBeSold(player)) {
            return;
        }

        int days = (sale_days * 2) + 1;
        if ((days <= 0) || (days > 7)) {
            return;
        }

        inventory.writeLock();
        container.writeLock();
        try {
            long total = SafeMath.mulAndCheck(price, count); // Цена на все предметы.
            long fee = Math.round(SafeMath.mulAndCheck(total, days) * REGISTRATION_FEE);
            fee = Math.max(fee, MIN_FEE);
            if ((fee > player.getAdena()) || !player.reduceAdena(fee, false)) {
                player.sendPacket(new SystemMessage2(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA_TO_REGISTER_THE_ITEM));
                return;
            }

            ItemInstance cItem = inventory.removeItemByObjectId(objectId, count);
            container.addItem(cItem);
            CommissionShopDAO.getInstance().saveNewItem(cItem.getObjectId(), player.getObjectId(), item_name, price, cItem.getTemplate().getExItemType(), sale_days, System.currentTimeMillis() + (days * 86400000), player.getName());
            Log.LogItem(player, Log.CommissionItemRegister, cItem);
        } catch (ArithmeticException ae) {
            _log.info("CommissionShopManager.registerItem: " + ae, ae);
        } finally {
            inventory.writeUnlock();
            container.writeUnlock();
        }

        showRegistrableItems(player);
        showPlayerRegisteredItems(player);
    }

    public void showItems(int listType, int category, int rareType, int grade, String searchName, Player player) {
        Queue<List<CommissionItemInfo>> list = new ArrayDeque<List<CommissionItemInfo>>();
        container.readLock();
        try {
            ExItemType[] types;
            if (listType == 1) { // Категория, оружие, броня...
                types = ExItemType.getTypesForMask(category);
            } else if (listType == 2) {
                types = new ExItemType[]
                        {
                                ExItemType.valueOf(category)
                        };
            } else {
                return;
            }
            list = CommissionShopDAO.getInstance().getRegisteredItems(types, rareType, grade, searchName);
        } catch (Exception e) {
            _log.info("CommissionShopManager.showItems: " + e, e);
        } finally {
            container.readUnlock();
        }
        if ((list.size() == 1) && list.peek().isEmpty()) {
            player.sendPacket(new ExResponseCommissionList(ExResponseCommissionList.EMPTY_LIST));
            return;
        }
        while (list.size() > 0) {
            List<CommissionItemInfo> part = list.poll();
            player.sendPacket(new ExResponseCommissionList(ExResponseCommissionList.ALL_ITEMS, list.size(), part));
        }
    }

    public void showCommissionBuyInfo(Player player, long auctionId, int exItemType) {

        if ((exItemType < 0) || (exItemType > ExItemType.values().length)) {
            return;
        }
        CommissionItemInfo itemInfo = CommissionShopDAO.getInstance().getCommissionItemInfo(auctionId, ExItemType.values()[exItemType]);
        if (itemInfo != null) {
            player.sendPacket(new ExResponseCommissionBuyInfo(itemInfo));
        }
    }

    public void requestBuyItem(Player player, long auctionId, int exItemType) {
        CommissionItemInfo itemInfo = CommissionShopDAO.getInstance().getCommissionItemInfo(auctionId, ExItemType.values()[exItemType]);
        if (itemInfo == null) {
            return;
        }
        PcInventory inventory = player.getInventory();
        container.writeLock();
        inventory.writeLock();
        try {
            if (itemInfo.getItem().getOwnerId() == player.getObjectId()) {
                player.sendPacket(new SystemMessage2(SystemMsg.ITEM_PURCHASE_HAS_FAILED));
                player.sendPacket(ExResponseCommissionBuyItem.FAILED);
                return;
            }

            if (!inventory.validateCapacity(itemInfo.getItem()) || !inventory.validateWeight(itemInfo.getItem())) {
                player.sendPacket(ExResponseCommissionBuyItem.FAILED);
                return;
            }

            long price = itemInfo.getRegisteredPrice() * itemInfo.getItem().getCount();

            if (price > player.getAdena()) {
                player.sendPacket(ExResponseCommissionBuyItem.FAILED);
                return;
            }

            if (!CommissionShopDAO.getInstance().removeItem(auctionId)) {
                player.sendPacket(ExResponseCommissionBuyItem.FAILED);
                return;
            }

            int receiverId = itemInfo.getItem().getOwnerId();

            inventory.reduceAdena(price);
            container.removeItem(itemInfo.getItem());
            inventory.addItem(itemInfo.getItem());

            player.sendPacket(new ExResponseCommissionBuyItem(1, itemInfo.getItem().getItemId(), itemInfo.getItem().getCount()));

            long fee = (long) Math.max(1000, price * SALE_FEE);

            Mail mail = new Mail();
            mail.setSenderId(receiverId);
            mail.setSenderName(itemInfo.getSellerName());
            mail.setReceiverId(receiverId);
            mail.setReceiverName(itemInfo.getSellerName());
            mail.setTopic("CommissionBuyTitle");
            mail.setBody(itemInfo.getItem().getName());
            // TODO: To system message
            mail.setSystemMsg1(3490);
            mail.setSystemMsg2(3491);
            ItemInstance item = ItemFunctions.createItem(ItemTemplate.ITEM_ID_ADENA);
            item.setLocation(ItemInstance.ItemLocation.MAIL);
            item.setCount(price - fee);
            if (item.getCount() > 0) {
                item.save();
                mail.addAttachment(item);
            }
            mail.setType(Mail.SenderType.SYSTEM);
            mail.setUnread(true);
            mail.setReturnable(false);
            mail.setExpireTime((360 * 3600) + (int) (System.currentTimeMillis() / 1000L));
            mail.save();

            Player receiver = World.getPlayer(receiverId);
            if (receiver != null) {
                receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
                receiver.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
            }
            Log.LogItem(player, Log.CommissionItemSold, itemInfo.getItem());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            container.writeUnlock();
            inventory.writeUnlock();
        }
    }

    public IStaticPacket returnExpiredItems() {
        container.writeLock();
        try {
            List<CommissionItemInfo> expiredItems = CommissionShopDAO.getInstance().removeExpiredItems(System.currentTimeMillis());
            for (CommissionItemInfo itemInfo : expiredItems) {
                Mail mail = new Mail();
                mail.setSenderId(itemInfo.getItem().getOwnerId());
                mail.setSenderName("CommissionBuyTitle");
                mail.setReceiverId(itemInfo.getItem().getOwnerId());
                mail.setReceiverName(itemInfo.getSellerName());
                mail.setTopic("CommissionBuyTitle");
                mail.setSystemMsg1(3492);
                mail.setSystemMsg2(3493);
                container.removeItem(itemInfo.getItem());
                itemInfo.getItem().setLocation(ItemInstance.ItemLocation.MAIL);
                itemInfo.getItem().save();
                mail.addAttachment(itemInfo.getItem());
                mail.setType(Mail.SenderType.SYSTEM);
                mail.setUnread(true);
                mail.setReturnable(false);
                mail.setExpireTime((360 * 3600) + (int) (System.currentTimeMillis() / 1000L));
                mail.save();

                Player receiver = World.getPlayer(itemInfo.getItem().getOwnerId());
                if (receiver != null) {
                    receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
                    receiver.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            container.writeUnlock();
        }
        return null;
    }

}
