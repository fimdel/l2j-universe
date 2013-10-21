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
package npc.model;

import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager;
import lineage2.gameserver.instancemanager.games.HandysBlockCheckerManager.ArenaParticipantsHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExCubeGameChangeTimeToStart;
import lineage2.gameserver.network.serverpackets.ExCubeGameRequestReady;
import lineage2.gameserver.network.serverpackets.ExCubeGameTeamList;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HandysBlockCheckerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for HandysBlockCheckerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public HandysBlockCheckerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		if (!Config.ALT_ENABLE_BLOCK_CHECKER_EVENT)
		{
			return;
		}
		HandysBlockCheckerManager.getInstance().startUpParticipantsQueue();
	}
	
	/**
	 * Field A_MANAGER_1. (value is 32521)
	 */
	private static final int A_MANAGER_1 = 32521;
	/**
	 * Field A_MANAGER_2. (value is 32522)
	 */
	private static final int A_MANAGER_2 = 32522;
	/**
	 * Field A_MANAGER_3. (value is 32523)
	 */
	private static final int A_MANAGER_3 = 32523;
	/**
	 * Field A_MANAGER_4. (value is 32524)
	 */
	private static final int A_MANAGER_4 = 32524;
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		if ((player == null) || !Config.ALT_ENABLE_BLOCK_CHECKER_EVENT)
		{
			return;
		}
		int npcId = getNpcId();
		int arena = -1;
		switch (npcId)
		{
			case A_MANAGER_1:
				arena = 0;
				break;
			case A_MANAGER_2:
				arena = 1;
				break;
			case A_MANAGER_3:
				arena = 2;
				break;
			case A_MANAGER_4:
				arena = 3;
				break;
		}
		if (arena != -1)
		{
			if (eventIsFull(arena))
			{
				player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_REGISTER_BECAUSE_CAPACITY_HAS_BEEN_EXCEEDED));
				return;
			}
			if (HandysBlockCheckerManager.getInstance().arenaIsBeingUsed(arena))
			{
				player.sendPacket(new SystemMessage(SystemMessage.THE_MATCH_IS_BEING_PREPARED_PLEASE_TRY_AGAIN_LATER));
				return;
			}
			if (HandysBlockCheckerManager.getInstance().addPlayerToArena(player, arena))
			{
				ArenaParticipantsHolder holder = HandysBlockCheckerManager.getInstance().getHolder(arena);
				final ExCubeGameTeamList tl = new ExCubeGameTeamList(holder.getRedPlayers(), holder.getBluePlayers(), arena);
				player.sendPacket(tl);
				int countBlue = holder.getBlueTeamSize();
				int countRed = holder.getRedTeamSize();
				int minMembers = Config.ALT_MIN_BLOCK_CHECKER_TEAM_MEMBERS;
				if ((countBlue >= minMembers) && (countRed >= minMembers))
				{
					holder.updateEvent();
					holder.broadCastPacketToTeam(new ExCubeGameRequestReady());
					holder.broadCastPacketToTeam(new ExCubeGameChangeTimeToStart(10));
					ThreadPoolManager.getInstance().schedule(holder.getEvent().new StartEvent(), 10100L);
				}
			}
		}
	}
	
	/**
	 * Method eventIsFull.
	 * @param arena int
	 * @return boolean
	 */
	private boolean eventIsFull(int arena)
	{
		if (HandysBlockCheckerManager.getInstance().getHolder(arena).getAllPlayers().size() == 12)
		{
			return true;
		}
		return false;
	}
}
