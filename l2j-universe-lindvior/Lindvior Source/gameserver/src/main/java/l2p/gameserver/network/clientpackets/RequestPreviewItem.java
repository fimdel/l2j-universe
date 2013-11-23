/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.data.xml.holder.BuyListHolder;
import l2p.gameserver.data.xml.holder.BuyListHolder.NpcTradeList;
import l2p.gameserver.data.xml.holder.ItemHolder;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.base.Race;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.model.items.Inventory;
import l2p.gameserver.network.serverpackets.ShopPreviewInfo;
import l2p.gameserver.network.serverpackets.ShopPreviewList;
import l2p.gameserver.templates.item.ItemTemplate;
import l2p.gameserver.templates.item.type.ArmorType;
import l2p.gameserver.templates.item.type.WeaponType;

import java.util.HashMap;
import java.util.Map;

public class RequestPreviewItem extends L2GameClientPacket {
    @SuppressWarnings("unused")
    private int _unknow;
    private int _listId;
    private int _count;
    private int[] _items;

    @Override
    protected void readImpl() {
        _unknow = readD();
        _listId = readD();
        _count = readD();
        if (_count * 4 > _buf.remaining() || _count > Short.MAX_VALUE || _count < 1) {
            _count = 0;
            return;
        }
        _items = new int[_count];
        for (int i = 0; i < _count; i++)
            _items[i] = readD();
    }

    @SuppressWarnings("unused")
    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null || _count == 0)
            return;

        if (activeChar.isActionsDisabled()) {
            activeChar.sendActionFailed();
            return;
        }

        if (activeChar.isInStoreMode()) {
            activeChar.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
            return;
        }

        if (activeChar.isInTrade()) {
            activeChar.sendActionFailed();
            return;
        }

        if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && activeChar.getKarma() < 0 && !activeChar.isGM()) {
            activeChar.sendActionFailed();
            return;
        }

        NpcInstance merchant = activeChar.getLastNpc();
        boolean isValidMerchant = merchant != null && merchant.isMerchantNpc();
        if (!activeChar.isGM() && (merchant == null || !isValidMerchant || !activeChar.isInRange(merchant, Creature.INTERACTION_DISTANCE))) {
            activeChar.sendActionFailed();
            return;
        }

        NpcTradeList list = BuyListHolder.getInstance().getBuyList(_listId);
        if (list == null) {
            //TODO audit
            activeChar.sendActionFailed();
            return;
        }

        int slots = 0;
        long totalPrice = 0; // Цена на примерку каждого итема 10 Adena.

        Map<Integer, Integer> itemList = new HashMap<Integer, Integer>();
        try {
            for (int i = 0; i < _count; i++) {
                int itemId = _items[i];
                if (list.getItemByItemId(itemId) == null) {
                    activeChar.sendActionFailed();
                    return;
                }

                ItemTemplate template = ItemHolder.getInstance().getTemplate(itemId);
                if (template == null)
                    continue;

                if (!template.isEquipable())
                    continue;

                int paperdoll = Inventory.getPaperdollIndex(template.getBodyPart());
                if (paperdoll < 0)
                    continue;

                if (activeChar.getRace() == Race.kamael) {
                    if (template.getItemType() == ArmorType.HEAVY || template.getItemType() == ArmorType.MAGIC || template.getItemType() == ArmorType.SIGIL || template.getItemType() == WeaponType.NONE)
                        continue;
                } else if (template.getItemType() == WeaponType.CROSSBOW || template.getItemType() == WeaponType.RAPIER || template.getItemType() == WeaponType.ANCIENTSWORD)
                    continue;

                if (itemList.containsKey(paperdoll)) {
                    activeChar.sendPacket(Msg.THOSE_ITEMS_MAY_NOT_BE_TRIED_ON_SIMULTANEOUSLY);
                    return;
                } else
                    itemList.put(paperdoll, itemId);

                totalPrice += ShopPreviewList.getWearPrice(template);
            }

            if (!activeChar.reduceAdena(totalPrice)) {
                activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
                return;
            }
        } catch (ArithmeticException ae) {
            //TODO audit
            sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
            return;
        }

        if (!itemList.isEmpty()) {
            activeChar.sendPacket(new ShopPreviewInfo(itemList));
            // Schedule task
            ThreadPoolManager.getInstance().schedule(new RemoveWearItemsTask(activeChar), Config.WEAR_DELAY * 1000);
        }
    }

    private static class RemoveWearItemsTask extends RunnableImpl {
        private Player _activeChar;

        public RemoveWearItemsTask(Player activeChar) {
            _activeChar = activeChar;
        }

        @Override
        public void runImpl() throws Exception {
            _activeChar.sendPacket(Msg.TRYING_ON_MODE_HAS_ENDED);
            _activeChar.sendUserInfo(true);
        }
    }
}
