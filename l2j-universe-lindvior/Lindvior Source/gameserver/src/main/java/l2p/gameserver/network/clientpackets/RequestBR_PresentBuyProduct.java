package l2p.gameserver.network.clientpackets;

import l2p.commons.dao.JdbcEntityState;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.dao.CharacterDAO;
import l2p.gameserver.data.xml.holder.ProductHolder;
import l2p.gameserver.database.mysql;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.ProductItem;
import l2p.gameserver.model.ProductItemComponent;
import l2p.gameserver.model.World;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.mail.Mail;
import l2p.gameserver.network.serverpackets.ExBR_GamePoint;
import l2p.gameserver.network.serverpackets.ExBR_PresentBuyProductPacket;
import l2p.gameserver.network.serverpackets.ExNoticePostArrived;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.utils.ItemFunctions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Darvin
 * @date : 26.01.12  17:26
 */
public class RequestBR_PresentBuyProduct extends L2GameClientPacket {
    private int productId, count;
    private String receiverName, topic, message;

    @Override
    protected void readImpl() throws Exception {
        productId = readD();
        count = readD();
        receiverName = readS();
        topic = readS();
        message = readS();
    }

    @Override
    protected void runImpl() throws Exception {
        Player activeChar = getClient().getActiveChar();

        if (activeChar == null)
            return;

        if (count > 99 || count < 0)
            return;

        ProductItem product = ProductHolder.getInstance().getProduct(productId);
        if (product == null) {
            activeChar.sendPacket(new ExBR_PresentBuyProductPacket(ExBR_PresentBuyProductPacket.RESULT_WRONG_PRODUCT));
            return;
        }

        if (System.currentTimeMillis() < product.getStartTimeSale() || System.currentTimeMillis() > product.getEndTimeSale()) {
            activeChar.sendPacket(new ExBR_PresentBuyProductPacket(ExBR_PresentBuyProductPacket.RESULT_SALE_PERIOD_ENDED));
            return;
        }

        int totalPoints = product.getPoints() * count;

        if (totalPoints < 0) {
            activeChar.sendPacket(new ExBR_PresentBuyProductPacket(ExBR_PresentBuyProductPacket.RESULT_WRONG_PRODUCT));
            return;
        }

        final long gamePointSize = activeChar.getPremiumPoints();

        if (totalPoints > gamePointSize) {
            activeChar.sendPacket(new ExBR_PresentBuyProductPacket(ExBR_PresentBuyProductPacket.RESULT_NOT_ENOUGH_POINTS));
            return;
        }

        int recieverId;

        Player target = World.getPlayer(receiverName);
        if (target != null) {
            recieverId = target.getObjectId();
            receiverName = target.getName();
            if (target.isInBlockList(activeChar)) { // цель заблокировала отправителя
                // TODO correct message
                activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BLOCKED_YOU_YOU_CANNOT_SEND_MAIL_TO_S1_).addString(receiverName));
                return;
            }
        } else {
            recieverId = CharacterDAO.getInstance().getObjectIdByName(receiverName);
            if (recieverId > 0)
                //TODO корректировать receiverName
                if (mysql.simple_get_int("target_Id", "character_blocklist", "obj_Id=" + recieverId + " AND target_Id=" + activeChar.getObjectId()) > 0) { // цель заблокировала отправителя
                    // TODO correct message
                    activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BLOCKED_YOU_YOU_CANNOT_SEND_MAIL_TO_S1_).addString(receiverName));
                    return;
                }
        }

        if (recieverId == 0) { // не нашли цель?
            // TODO: correct message
            activeChar.sendPacket(Msg.WHEN_THE_RECIPIENT_DOESN_T_EXIST_OR_THE_CHARACTER_HAS_BEEN_DELETED_SENDING_MAIL_IS_NOT_POSSIBLE);
            return;
        }

        activeChar.reducePremiumPoints(totalPoints);

        List<ItemInstance> attachments = new ArrayList<ItemInstance>();
        for (ProductItemComponent comp : product.getComponents()) {
            ItemInstance item = ItemFunctions.createItem(comp.getItemId());
            item.setCount(comp.getCount());
            item.setOwnerId(activeChar.getObjectId());
            item.setLocation(ItemInstance.ItemLocation.MAIL);
            if (item.getJdbcState().isSavable())
                item.save();
            else {
                item.setJdbcState(JdbcEntityState.UPDATED);
                item.update();
            }

            attachments.add(item);
        }

        Mail mail = new Mail();
        mail.setSenderId(activeChar.getObjectId());
        mail.setSenderName(activeChar.getName());
        mail.setReceiverId(recieverId);
        mail.setReceiverName(receiverName);
        mail.setTopic(topic);
        mail.setBody(message);
        mail.setPrice(0);
        mail.setUnread(true);
        mail.setType(Mail.SenderType.NORMAL);
        mail.setExpireTime(720 * 3600 + (int) (System.currentTimeMillis() / 1000L));
        for (ItemInstance item : attachments)
            mail.addAttachment(item);
        mail.save();

        activeChar.sendPacket(new ExBR_GamePoint(activeChar));
        activeChar.sendPacket(new ExBR_PresentBuyProductPacket(ExBR_PresentBuyProductPacket.RESULT_OK));
        activeChar.sendChanges();

        if (target != null) {
            target.sendPacket(ExNoticePostArrived.STATIC_TRUE);
            target.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
        }
    }
}
