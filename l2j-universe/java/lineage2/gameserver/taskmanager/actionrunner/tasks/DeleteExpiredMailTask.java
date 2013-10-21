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
package lineage2.gameserver.taskmanager.actionrunner.tasks;

import java.util.List;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.dao.MailDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.serverpackets.ExNoticePostArrived;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DeleteExpiredMailTask extends AutomaticTask
{
	/**
	 * Constructor for DeleteExpiredMailTask.
	 */
	public DeleteExpiredMailTask()
	{
		super();
	}
	
	/**
	 * Method doTask.
	 */
	@Override
	public void doTask()
	{
		int expireTime = (int) (System.currentTimeMillis() / 1000L);
		List<Mail> mails = MailDAO.getInstance().getExpiredMail(expireTime);
		for (Mail mail : mails)
		{
			if (!mail.getAttachments().isEmpty())
			{
				if (mail.getType() == Mail.SenderType.NORMAL)
				{
					Player player = World.getPlayer(mail.getSenderId());
					Mail reject = mail.reject();
					mail.delete();
					reject.setExpireTime(expireTime + (360 * 3600));
					reject.save();
					if (player != null)
					{
						player.sendPacket(ExNoticePostArrived.STATIC_TRUE);
						player.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
					}
				}
				else
				{
					mail.setExpireTime(expireTime + 86400);
					mail.setJdbcState(JdbcEntityState.UPDATED);
					mail.update();
				}
			}
			else
			{
				mail.delete();
			}
		}
	}
	
	/**
	 * Method reCalcTime.
	 * @param start boolean
	 * @return long
	 */
	@Override
	public long reCalcTime(boolean start)
	{
		return System.currentTimeMillis() + 600000L;
	}
}
