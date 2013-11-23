package l2p.gameserver.network.clientpackets;

import l2p.commons.dao.JdbcEntityState;
import l2p.commons.math.SafeMath;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.dao.MailDAO;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.ItemInstance.ItemLocation;
import l2p.gameserver.model.mail.Mail;
import l2p.gameserver.network.serverpackets.ExReplyReceivedPost;
import l2p.gameserver.network.serverpackets.ExShowReceivedPostList;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.templates.item.ItemTemplate;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.Log;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * Шлется клиентом при согласии принять письмо в {@link ExReplyReceivedPost}. Если письмо с оплатой то создателю письма шлется запрошенная сумма.
 */
public class RequestExReceivePost extends L2GameClientPacket {
    private int postId;

    /**
     * format: d
     */
    @Override
    protected void readImpl() {
        postId = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        if (activeChar.isActionsDisabled()) {
            activeChar.sendActionFailed();
            return;
        }

        if (activeChar.isInStoreMode()) {
            activeChar.sendPacket(Msg.YOU_CANNOT_RECEIVE_BECAUSE_THE_PRIVATE_SHOP_OR_WORKSHOP_IS_IN_PROGRESS);
            return;
        }

        if (activeChar.isInTrade()) {
            activeChar.sendPacket(Msg.YOU_CANNOT_RECEIVE_DURING_AN_EXCHANGE);
            return;
        }

        if (activeChar.isFishing()) {
            activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
            return;
        }

        if (activeChar.getEnchantScroll() != null) {
            activeChar.sendPacket(Msg.YOU_CANNOT_RECEIVE_DURING_AN_ITEM_ENHANCEMENT_OR_ATTRIBUTE_ENHANCEMENT);
            return;
        }

        Mail mail = MailDAO.getInstance().getReceivedMailByMailId(activeChar.getObjectId(), postId);
        if (mail != null) {
            activeChar.getInventory().writeLock();
            try {
                Set<ItemInstance> attachments = mail.getAttachments();
                ItemInstance[] items;

                if (attachments.size() > 0 && !activeChar.isInPeaceZone()) {
                    activeChar.sendPacket(Msg.YOU_CANNOT_RECEIVE_IN_A_NON_PEACE_ZONE_LOCATION);
                    return;
                }
                synchronized (attachments) {
                    if (mail.getAttachments().isEmpty())
                        return;

                    items = mail.getAttachments().toArray(new ItemInstance[attachments.size()]);

                    int slots = 0;
                    long weight = 0;
                    for (ItemInstance item : items) {
                        weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(item.getCount(), item.getTemplate().getWeight()));
                        if (!item.getTemplate().isStackable() || activeChar.getInventory().getItemByItemId(item.getItemId()) == null)
                            slots++;
                    }

                    if (!activeChar.getInventory().validateWeight(weight)) {
                        sendPacket(Msg.YOU_COULD_NOT_RECEIVE_BECAUSE_YOUR_INVENTORY_IS_FULL);
                        return;
                    }

                    if (!activeChar.getInventory().validateCapacity(slots)) {
                        sendPacket(Msg.YOU_COULD_NOT_RECEIVE_BECAUSE_YOUR_INVENTORY_IS_FULL);
                        return;
                    }

                    if (mail.getPrice() > 0) {
                        if (!activeChar.reduceAdena(mail.getPrice(), true)) {
                            activeChar.sendPacket(Msg.YOU_CANNOT_RECEIVE_BECAUSE_YOU_DON_T_HAVE_ENOUGH_ADENA);
                            return;
                        }

                        Player sender = World.getPlayer(mail.getSenderId());
                        if (sender != null) {
                            sender.addAdena(mail.getPrice(), true);
                            sender.sendPacket(new SystemMessage(SystemMessage.S1_ACQUIRED_THE_ATTACHED_ITEM_TO_YOUR_MAIL).addName(activeChar));
                        } else {
                            int expireTime = 360 * 3600 + (int) (System.currentTimeMillis() / 1000L); //TODO [G1ta0] хардкод времени актуальности почты
                            Mail reply = mail.reply();
                            reply.setExpireTime(expireTime);

                            ItemInstance item = ItemFunctions.createItem(ItemTemplate.ITEM_ID_ADENA);
                            item.setOwnerId(reply.getReceiverId());
                            item.setCount(mail.getPrice());
                            item.setLocation(ItemLocation.MAIL);
                            item.save();

                            Log.LogItem(activeChar, Log.PostSend, item);

                            reply.addAttachment(item);
                            reply.save();
                        }
                    }

                    attachments.clear();
                }

                mail.setJdbcState(JdbcEntityState.UPDATED);
                if (StringUtils.isEmpty(mail.getBody()))
                    mail.delete();
                else
                    mail.update();

                for (ItemInstance item : items) {
                    activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_ACQUIRED_S2_S1).addItemName(item.getItemId()).addNumber(item.getCount()));
                    Log.LogItem(activeChar, Log.PostRecieve, item);
                    activeChar.getInventory().addItem(item);
                }

                activeChar.sendPacket(Msg.MAIL_SUCCESSFULLY_RECEIVED);
            } catch (ArithmeticException ae) {
                //TODO audit
            } finally {
                activeChar.getInventory().writeUnlock();
            }
        }

        activeChar.sendPacket(new ExShowReceivedPostList(activeChar));
    }
}