package lineage2.gameserver.network.serverpackets;

import lineage2.commons.collections.CollectionUtils;
import lineage2.gameserver.dao.MailDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.clientpackets.RequestExDeleteReceivedPost;
import lineage2.gameserver.network.clientpackets.RequestExPostItemList;
import lineage2.gameserver.network.clientpackets.RequestExRequestReceivedPost;
import lineage2.gameserver.network.clientpackets.RequestExRequestReceivedPostList;

import java.util.List;

/**
 * Появляется при нажатии на кнопку "почта" или "received mail", входящие письма <br>
 * Ответ на {@link RequestExRequestReceivedPostList}. <br>
 * При нажатии на письмо в списке шлется {@link RequestExRequestReceivedPost} а в ответ {@link ExReplyReceivedPost}. <br>
 * При попытке удалить письмо шлется {@link RequestExDeleteReceivedPost}. <br>
 * При нажатии кнопки send mail шлется {@link RequestExPostItemList}.
 * 
 * @see ExShowSentPostList аналогичный список отправленной почты
 */
public class ExShowReceivedPostList extends L2GameServerPacket
{
	private final List<Mail> mails;

	public ExShowReceivedPostList(Player cha)
	{
		mails = MailDAO.getInstance().getReceivedMailByOwnerId(cha.getObjectId());
		CollectionUtils.eqSort(mails);
	}

	// d dx[dSSddddddd]
	@Override
	protected void writeImpl()
	{
		writeEx(0xAB);
		writeD((int) (System.currentTimeMillis() / 1000L));
		writeD(mails.size()); // количество писем
		for (Mail mail : mails)
		{
			writeD(mail.getType().ordinal()); // тип письма

			if (mail.getType() == Mail.SenderType.SYSTEM)
			{
				writeD(mail.getSystemMsg1());
				writeD(mail.getMessageId());
			}
			else
			{
				writeD(mail.getMessageId());
			}

			writeS(mail.getTopic());
			writeS(mail.getSenderName());
			writeD(mail.isPayOnDelivery() ? 1 : 0); // если тут 1 то письмо
			                                        // требует оплаты
			writeD(mail.getExpireTime()); // время действительности письма
			writeD(mail.isUnread() ? 1 : 0); // письмо не прочитано - его нельзя
			                                 // удалить и оно выделяется ярким
			                                 // цветом
			writeD(mail.isReturnable()); // returnable
			writeD(mail.getAttachments().isEmpty() ? 0 : 1); // 1 - письмо с
			                                                 // приложением, 0 -
			                                                 // просто письмо
			writeD(0x00); // unknown
			writeD(mail.getReceiverId());
		}
		writeD(100);
		writeD(1000);
	}
}