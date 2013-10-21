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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.ExMentorList;
import lineage2.gameserver.utils.Mentoring;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestMentorCancel extends L2GameClientPacket
{
	/**
	 * Field _mtype.
	 */
	@SuppressWarnings("unused")
	private int _mtype;
	/**
	 * Field _charName.
	 */
	private String _charName;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_mtype = readD();
		_charName = readS();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		int mentorId = activeChar.getMenteeMentorList().getMentor();
		if (mentorId != 0)
		{
			String mentorName = activeChar.getMenteeMentorList().getList().get(mentorId).getName();
			Player mentorChar = World.getPlayer(mentorName);
			mentorChar.getMenteeMentorList().remove(activeChar.getName(), true, true);
			mentorChar.sendPacket(new ExMentorList(mentorChar));
			if ((activeChar != null) && activeChar.isOnline())
			{
				activeChar.getMenteeMentorList().remove(mentorChar.getName(), false, false);
				activeChar.sendPacket(new ExMentorList(activeChar));
			}
			Mentoring.cancelMentorBuffs(mentorChar);
			Mentoring.cancelMenteeBuffs(activeChar);
			Mentoring.setTimePenalty(mentorChar.getObjectId(), System.currentTimeMillis() + (2 * 24 * 3600 * 1000L), -1);
		}
		else
		{
			Player menteeChar = World.getPlayer(_charName);
			activeChar.getMenteeMentorList().remove(_charName, true, true);
			activeChar.sendPacket(new ExMentorList(activeChar));
			if ((menteeChar != null) && menteeChar.isOnline())
			{
				menteeChar.getMenteeMentorList().remove(activeChar.getName(), false, false);
				menteeChar.sendPacket(new ExMentorList(menteeChar));
			}
			Mentoring.cancelMentorBuffs(activeChar);
			Mentoring.cancelMenteeBuffs(menteeChar);
			Mentoring.setTimePenalty(activeChar.getObjectId(), System.currentTimeMillis() + (2 * 24 * 3600 * 1000L), -1);
		}
	}
}
