/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.clientpackets;

import java.util.List;

import lineage2.gameserver.dao.MailDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.serverpackets.ExShowReceivedPostList;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExDeleteReceivedPost extends L2GameClientPacket
{
	/**
	 * Field _count.
	 */
	private int _count;
	/**
	 * Field _list.
	 */
	private int[] _list;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_count = readD();
		if (((_count * 4) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
		{
			_count = 0;
			return;
		}
		_list = new int[_count];
		for (int i = 0; i < _count; i++)
		{
			_list[i] = readD();
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if ((activeChar == null) || (_count == 0))
		{
			return;
		}
		List<Mail> mails = MailDAO.getInstance().getReceivedMailByOwnerId(activeChar.getObjectId());
		if (!mails.isEmpty())
		{
			for (Mail mail : mails)
			{
				if (ArrayUtils.contains(_list, mail.getMessageId()))
				{
					if (mail.getAttachments().isEmpty())
					{
						MailDAO.getInstance().deleteReceivedMailByMailId(activeChar.getObjectId(), mail.getMessageId());
					}
				}
			}
		}
		activeChar.sendPacket(new ExShowReceivedPostList(activeChar));
	}
}
