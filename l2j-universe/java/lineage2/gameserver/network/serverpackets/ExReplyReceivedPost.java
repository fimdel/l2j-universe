package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.clientpackets.RequestExReceivePost;
import lineage2.gameserver.network.clientpackets.RequestExRejectPost;
import lineage2.gameserver.network.clientpackets.RequestExRequestReceivedPost;

/**
 * Просмотр полученного письма. Шлется в ответ на {@link RequestExRequestReceivedPost}. При попытке забрать приложенные вещи клиент шлет
 * {@link RequestExReceivePost}. При возврате письма клиент шлет {@link RequestExRejectPost}.
 * 
 * @see ExReplySentPost
 */
public class ExReplyReceivedPost extends L2GameServerPacket
{
	private final Mail mail;

	public ExReplyReceivedPost(Mail mail)
	{
		this.mail = mail;
	}

	// dddSSS dx[hddQdddhhhhhhhhhh] Qdd
	@Override
	protected void writeImpl()
	{
		writeEx(0xAC);

		writeD(mail.getType().ordinal());

		if (mail.getType() == Mail.SenderType.SYSTEM)
		{
			writeD(0x00);// unknown1
			writeD(0x00);// unknown2
			writeD(0x00);// unknown3
			writeD(0x00);// unknown4
			writeD(0x00);// unknown5
			writeD(0x00);// unknown6
			writeD(0x00);// unknown7
			writeD(0x00);// unknown8
			writeD(mail.getSystemMsg1());
			writeD(mail.getSystemMsg2());
		}

		// Type = Normal

		writeD(mail.getMessageId());

		writeD(0x00);// unknown2
		writeD(0x00);// unknown3

		writeS(mail.getSenderName()); // от кого
		writeS(mail.getTopic()); // топик
		writeS(mail.getBody()); // тело

		writeD(mail.getAttachments().size()); // количество приложенных вещей
		for (ItemInstance item : mail.getAttachments())
		{
			writeItemInfo(item);
			writeD(item.getObjectId());
		}

		writeQ(mail.getPrice()); // для писем с оплатой - цена
		writeD(0x00); // unk
		writeD(mail.isReturnable());
		writeD(mail.getReceiverId());
	}
}