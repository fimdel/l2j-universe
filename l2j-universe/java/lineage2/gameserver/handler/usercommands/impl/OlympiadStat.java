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
package lineage2.gameserver.handler.usercommands.impl;

import lineage2.gameserver.Config;
import lineage2.gameserver.handler.usercommands.IUserCommandHandler;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OlympiadStat implements IUserCommandHandler
{
	/**
	 * Field COMMAND_IDS.
	 */
	private static final int[] COMMAND_IDS =
	{
		109
	};
	
	/**
	 * Method useUserCommand.
	 * @param id int
	 * @param activeChar Player
	 * @return boolean * @see lineage2.gameserver.handler.usercommands.IUserCommandHandler#useUserCommand(int, Player)
	 */
	@Override
	public boolean useUserCommand(int id, Player activeChar)
	{
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		GameObject objectTarget = Config.OLYMPIAD_OLDSTYLE_STAT ? activeChar : activeChar.getTarget();
		if ((objectTarget == null) || !objectTarget.isPlayer() || !objectTarget.getPlayer().isNoble())
		{
			activeChar.sendPacket(SystemMsg.THIS_COMMAND_CAN_ONLY_BE_USED_BY_A_NOBLESSE);
			return true;
		}
		Player playerTarget = objectTarget.getPlayer();
		SystemMessage2 sm = new SystemMessage2(SystemMsg.FOR_THE_CURRENT_GRAND_OLYMPIAD_YOU_HAVE_PARTICIPATED_IN_S1_MATCHES_S2_WINS_S3_DEFEATS_YOU_CURRENTLY_HAVE_S4_OLYMPIAD_POINTS);
		sm.addInteger(Olympiad.getCompetitionDone(playerTarget.getObjectId()));
		sm.addInteger(Olympiad.getCompetitionWin(playerTarget.getObjectId()));
		sm.addInteger(Olympiad.getCompetitionLoose(playerTarget.getObjectId()));
		sm.addInteger(Olympiad.getNoblePoints(playerTarget.getObjectId()));
		activeChar.sendPacket(sm);
		int[] ar = Olympiad.getWeekGameCounts(playerTarget.getObjectId());
		sm = new SystemMessage2(SystemMsg.YOU_HAVE_S1_MATCHES_REMAINING_THAT_YOU_CAN_PARTICIPATE_IN_THIS_WEEK_S2_1_VS_1_CLASS_MATCHES_S3_1_VS_1_MATCHES__S4_3_VS_3_TEAM_MATCHES);
		sm.addInteger(ar[0]);
		sm.addInteger(ar[1]);
		sm.addInteger(ar[2]);
		sm.addInteger(ar[2]);
		activeChar.sendPacket(sm);
		return true;
	}
	
	/**
	 * Method getUserCommandList.
	 * @return int[] * @see lineage2.gameserver.handler.usercommands.IUserCommandHandler#getUserCommandList()
	 */
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}
