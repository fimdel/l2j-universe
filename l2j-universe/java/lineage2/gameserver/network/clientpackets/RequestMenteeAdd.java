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

import java.util.Map;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.actor.instances.player.MenteeMentor;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.serverpackets.ExMentorAdd;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestMenteeAdd extends L2GameClientPacket
{
	/**
	 * Field _newMentee.
	 */
	private String _newMentee;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_newMentee = readS();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		GameClient client = getClient();
		Player activeChar = client.getActiveChar();
		Player newMentee = World.getPlayer(_newMentee);
		if (newMentee == null)
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.THAT_PLAYER_IS_NOT_ONLINE));
			return;
		}
		if (activeChar.getClassId().getId() < 139)
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_MUST_AWAKEN_IN_ORDER_TO_BECOME_A_MENTOR));
			return;
		}
		if (activeChar.getMenteeMentorList().getList().size() == 3)
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.A_MENTOR_CAN_HAVE_UP_TO_3_MENTEES_AT_THE_SAME_TIME));
			return;
		}
		if (newMentee.getMenteeMentorList().getMentor() != 0)
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_ALREADY_HAS_A_MENTOR).addName(newMentee));
			return;
		}
		for (Map.Entry<Integer, MenteeMentor> entry : activeChar.getMenteeMentorList().getList().entrySet())
		{
			if (entry.getValue().getName().equals(_newMentee))
			{
				return;
			}
		}
		if (activeChar.getName().equals(_newMentee))
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_CANNOT_BECOME_YOUR_OWN_MENTEE));
			return;
		}
		if (newMentee.getLevel() > 85)
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_IS_ABOVE_LEVEL_86_AND_CANNOT_BECOME_A_MENTEE).addName(newMentee));
			return;
		}
		if (!newMentee.getInventory().validateCapacity(33800, 1))
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_DOES_NOT_HAVE_THE_ITEM_NEDEED_TO_BECOME_A_MENTEE).addName(newMentee));
			return;
		}
		long mentorPenalty = activeChar.getVarLong("mentorPenalty", 0L);
		if (mentorPenalty > System.currentTimeMillis())
		{
			long milisPenalty = mentorPenalty - System.currentTimeMillis();
			double numSecs = (milisPenalty / 1000) % 60;
			double countDown = ((milisPenalty / 1000) - numSecs) / 60;
			int numMins = (int) Math.floor(countDown % 60);
			countDown = (countDown - numMins) / 60;
			int numHours = (int) Math.floor(countDown % 24);
			int numDays = (int) Math.floor((countDown - numHours) / 24);
			activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_CAN_BOND_WITH_A_NEW_MENTEE_IN_S1_DAYS_S2_HOUR_S3_MINUTE).addInteger(numDays).addInteger(numHours).addInteger(numMins));
			return;
		}
		new Request(L2RequestType.MENTEE, activeChar, newMentee).setTimeout(10000L);
		activeChar.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_OFFERED_TO_BECOME_S1_MENTOR).addName(newMentee));
		newMentee.sendPacket(new ExMentorAdd(activeChar));
	}
}
