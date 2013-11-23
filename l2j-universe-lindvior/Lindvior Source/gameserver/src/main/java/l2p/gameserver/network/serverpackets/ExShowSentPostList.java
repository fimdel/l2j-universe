package l2p.gameserver.network.serverpackets;

import l2p.commons.collections.CollectionUtils;
import l2p.gameserver.dao.MailDAO;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.mail.Mail;

import java.util.List;


/**
 * Появляется при нажатии на кнопку "sent mail", исходящие письма
 * Ответ на {@link RequestExRequestSentPostList}
 * При нажатии на письмо в списке шлется {@link RequestExRequestSentPost}, а в ответ {@link ExReplySentPost}.
 * При нажатии на "delete" шлется {@link RequestExDeleteSentPost}.
 *
 * @see ExShowReceivedPostList аналогичный список принятой почты
 */
public class ExShowSentPostList extends L2GameServerPacket {
    private final List<Mail> mails;

    public ExShowSentPostList(Player cha) {
        mails = MailDAO.getInstance().getSentMailByOwnerId(cha.getObjectId());
        CollectionUtils.eqSort(mails);
    }

    // d dx[dSSddddd]
    @Override
    protected void writeImpl() {
        writeEx449(0xAC);
        writeD((int) (System.currentTimeMillis() / 1000L));
        writeD(mails.size()); // количество писем
        for (Mail mail : mails) {
            writeD(mail.getMessageId()); // уникальный id письма
            writeS(mail.getTopic()); // топик
            writeS(mail.getReceiverName()); // получатель
            writeD(mail.isPayOnDelivery() ? 1 : 0); // если тут 1 то письмо требует оплаты
            writeD(mail.getExpireTime()); // время действительности письма
            writeD(mail.isUnread() ? 1 : 0); // ?
            writeD(mail.isReturnable()); // returnable
            writeD(mail.getAttachments().isEmpty() ? 0 : 1); // 1 - письмо с приложением, 0 - просто письмо
            writeD(0x00); // ???
        }
    }
}