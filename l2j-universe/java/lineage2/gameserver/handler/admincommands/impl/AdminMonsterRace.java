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
package lineage2.gameserver.handler.admincommands.impl;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.MonsterRace;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.DeleteObject;
import lineage2.gameserver.network.serverpackets.MonRaceInfo;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unused")
public class AdminMonsterRace implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_mons.
		 */
		admin_mons
	}
	
	/**
	 * Field state.
	 */
	protected static int state = -1;
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;
		if (fullString.equalsIgnoreCase("admin_mons"))
		{
			if (!activeChar.getPlayerAccess().MonsterRace)
			{
				return false;
			}
			handleSendPacket(activeChar);
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[] * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
	
	/**
	 * Method handleSendPacket.
	 * @param activeChar Player
	 */
	private void handleSendPacket(Player activeChar)
	{
		int[][] codes =
		{
			{
				-1,
				0
			},
			{
				0,
				15322
			},
			{
				13765,
				-1
			},
			{
				-1,
				0
			}
		};
		MonsterRace race = MonsterRace.getInstance();
		if (state == -1)
		{
			state++;
			race.newRace();
			race.newSpeeds();
			activeChar.broadcastPacket(new MonRaceInfo(codes[state][0], codes[state][1], race.getMonsters(), race.getSpeeds()));
		}
		else if (state == 0)
		{
			state++;
			activeChar.sendPacket(Msg.THEYRE_OFF);
			activeChar.broadcastPacket(new PlaySound("S_Race"));
			activeChar.broadcastPacket(new PlaySound(PlaySound.Type.SOUND, "ItemSound2.race_start", 1, 121209259, new Location(12125, 182487, -3559)));
			activeChar.broadcastPacket(new MonRaceInfo(codes[state][0], codes[state][1], race.getMonsters(), race.getSpeeds()));
			ThreadPoolManager.getInstance().schedule(new RunRace(codes, activeChar), 5000);
		}
	}
	
	/**
	 * @author Mobius
	 */
	class RunRace extends RunnableImpl
	{
		/**
		 * Field codes.
		 */
		private final int[][] codes;
		/**
		 * Field activeChar.
		 */
		private final Player activeChar;
		
		/**
		 * Constructor for RunRace.
		 * @param codes int[][]
		 * @param activeChar Player
		 */
		public RunRace(int[][] codes, Player activeChar)
		{
			this.codes = codes;
			this.activeChar = activeChar;
		}
		
		/**
		 * Method runImpl.
		 * @throws Exception
		 */
		@Override
		public void runImpl() throws Exception
		{
			activeChar.broadcastPacket(new MonRaceInfo(codes[2][0], codes[2][1], MonsterRace.getInstance().getMonsters(), MonsterRace.getInstance().getSpeeds()));
			ThreadPoolManager.getInstance().schedule(new RunEnd(activeChar), 30000);
		}
	}
	
	/**
	 * @author Mobius
	 */
	class RunEnd extends RunnableImpl
	{
		/**
		 * Field activeChar.
		 */
		private final Player activeChar;
		
		/**
		 * Constructor for RunEnd.
		 * @param activeChar Player
		 */
		public RunEnd(Player activeChar)
		{
			this.activeChar = activeChar;
		}
		
		/**
		 * Method runImpl.
		 * @throws Exception
		 */
		@Override
		public void runImpl() throws Exception
		{
			NpcInstance obj;
			for (int i = 0; i < 8; i++)
			{
				obj = MonsterRace.getInstance().getMonsters()[i];
				activeChar.broadcastPacket(new DeleteObject(obj));
			}
			state = -1;
		}
	}
}
