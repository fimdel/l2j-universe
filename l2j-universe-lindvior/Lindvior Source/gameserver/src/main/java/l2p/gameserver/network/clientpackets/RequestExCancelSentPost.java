package l2p.gameserver.network.clientpackets;

import l2p.commons.math.SafeMath;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.dao.MailDAO;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.mail.Mail;
import l2p.gameserver.network.serverpackets.ExReplySentPost;
import l2p.gameserver.network.serverpackets.ExShowSentPostList;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.utils.Log;

import java.util.Set;

/**
 * Запрос на удаление письма с приложениями. Возвращает приложения отправителю на личный склад и удаляет письмо. Ответ на кнопку Cancel в {@link ExReplySentPost}.
 */
public class RequestExCancelSentPost extends L2GameClientPacket {
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
            activeChar.sendPacket(Msg.YOU_CANNOT_CANCEL_BECAUSE_THE_PRIVATE_SHOP_OR_WORKSHOP_IS_IN_PROGRESS);
            return;
        }

        if (activeChar.isInTrade()) {
            activeChar.sendPacket(Msg.YOU_CANNOT_CANCEL_DURING_AN_EXCHANGE);
            return;
        }

        if (activeChar.getEnchantScroll() != null) {
            activeChar.sendPacket(Msg.YOU_CANNOT_CANCEL_DURING_AN_ITEM_ENHANCEMENT_OR_ATTRIBUTE_ENHANCEMENT);
            return;
        }

        if (!activeChar.isInPeaceZone()) {
            activeChar.sendPacket(Msg.YOU_CANNOT_CANCEL_IN_A_NON_PEACE_ZONE_LOCATION);
            return;
        }

        if (activeChar.isFishing()) {
            activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
            return;
        }

        Mail mail = MailDAO.getInstance().getSentMailByMailId(activeChar.getObjectId(), postId);
        if (mail != null) {
            if (mail.getAttachments().isEmpty()) {
                activeChar.sendActionFailed();
                return;
            }
            activeChar.getInventory().writeLock();
            try {
                int slots = 0;
                long weight = 0;
                for (ItemInstance item : mail.getAttachments()) {
                    weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(item.getCount(), item.getTemplate().getWeight()));
                    if (!item.getTemplate().isStackable() || activeChar.getInventory().getItemByItemId(item.getItemId()) == null)
                        slots++;
                }

                if (!activeChar.getInventory().validateWeight(weight)) {
                    sendPacket(Msg.YOU_COULD_NOT_CANCEL_RECEIPT_BECAUSE_YOUR_INVENTORY_IS_FULL);
                    return;
                }

                if (!activeChar.getInventory().validateCapacity(slots)) {
                    sendPacket(Msg.YOU_COULD_NOT_CANCEL_RECEIPT_BECAUSE_YOUR_INVENTORY_IS_FULL);
                    return;
                }

                ItemInstance[] items;
                Set<ItemInstance> attachments = mail.getAttachments();

                synchronized (attachments) {
                    items = mail.getAttachments().toArray(new ItemInstance[attachments.size()]);
                    attachments.clear();
                }

                for (ItemInstance item : items) {
                    activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_ACQUIRED_S2_S1).addItemName(item.getItemId()).addNumber(item.getCount()));
                    Log.LogItem(activeChar, Log.PostCancel, item);
                    activeChar.getInventory().addItem(item);
                }

                mail.delete();

                activeChar.sendPacket(Msg.MAIL_SUCCESSFULLY_CANCELLED);
            } catch (ArithmeticException ae) {
                //TODO audit
            } finally {
                activeChar.getInventory().writeUnlock();
            }
        }
        activeChar.sendPacket(new ExShowSentPostList(activeChar));
    }
}