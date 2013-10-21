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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unused")
public class AdminMammon implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_find_mammon.
		 */
		admin_find_mammon,
		/**
		 * Field admin_show_mammon.
		 */
		admin_show_mammon,
		/**
		 * Field admin_hide_mammon.
		 */
		admin_hide_mammon,
		/**
		 * Field admin_list_spawns.
		 */
		admin_list_spawns
	}
	
	/**
	 * Field npcIds.
	 */
	List<Integer> npcIds = new ArrayList<Integer>();
	
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
		npcIds.clear();
		if (!activeChar.getPlayerAccess().Menu)
		{
			return false;
		}
		else if (fullString.startsWith("admin_find_mammon"))
		{
			npcIds.add(31113);
			npcIds.add(31126);
			npcIds.add(31092);
			int teleportIndex = -1;
			try
			{
				if (fullString.length() > 16)
				{
					teleportIndex = Integer.parseInt(fullString.substring(18));
				}
			}
			catch (Exception NumberFormatException)
			{
			}
			findAdminNPCs(activeChar, npcIds, teleportIndex, -1);
		}
		else if (fullString.equals("admin_show_mammon"))
		{
			npcIds.add(31113);
			npcIds.add(31126);
			findAdminNPCs(activeChar, npcIds, -1, 1);
		}
		else if (fullString.equals("admin_hide_mammon"))
		{
			npcIds.add(31113);
			npcIds.add(31126);
			findAdminNPCs(activeChar, npcIds, -1, 0);
		}
		else if (fullString.startsWith("admin_list_spawns"))
		{
			int npcId = 0;
			try
			{
				npcId = Integer.parseInt(fullString.substring(18).trim());
			}
			catch (Exception NumberFormatException)
			{
				activeChar.sendMessage("Command format is //list_spawns <NPC_ID>");
			}
			npcIds.add(npcId);
			findAdminNPCs(activeChar, npcIds, -1, -1);
		}
		else if (fullString.startsWith("admin_msg"))
		{
			activeChar.sendPacket(new SystemMessage(Integer.parseInt(fullString.substring(10).trim())));
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
	 * Method findAdminNPCs.
	 * @param activeChar Player
	 * @param npcIdList List<Integer>
	 * @param teleportIndex int
	 * @param makeVisible int
	 */
	public void findAdminNPCs(Player activeChar, List<Integer> npcIdList, int teleportIndex, int makeVisible)
	{
		int index = 0;
		for (NpcInstance npcInst : GameObjectsStorage.getAllNpcsForIterate())
		{
			int npcId = npcInst.getNpcId();
			if (npcIdList.contains(npcId))
			{
				if (makeVisible == 1)
				{
					npcInst.spawnMe();
				}
				else if (makeVisible == 0)
				{
					npcInst.decayMe();
				}
				if (npcInst.isVisible())
				{
					index++;
					if (teleportIndex > -1)
					{
						if (teleportIndex == index)
						{
							activeChar.teleToLocation(npcInst.getLoc());
						}
					}
					else
					{
						activeChar.sendMessage(index + " - " + npcInst.getName() + " (" + npcInst.getObjectId() + "): " + npcInst.getX() + " " + npcInst.getY() + " " + npcInst.getZ());
					}
				}
			}
		}
	}
}
