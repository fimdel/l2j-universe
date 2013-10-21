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
package services;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PaganDoormans extends Functions
{
	/**
	 * Field MainDoorId. (value is 19160001)
	 */
	private static final int MainDoorId = 19160001;
	/**
	 * Field SecondDoor1Id. (value is 19160011)
	 */
	private static final int SecondDoor1Id = 19160011;
	/**
	 * Field SecondDoor2Id. (value is 19160010)
	 */
	private static final int SecondDoor2Id = 19160010;
	
	/**
	 * Method openMainDoor.
	 */
	public void openMainDoor()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (!NpcInstance.canBypassCheck(player, npc))
		{
			return;
		}
		if ((getItemCount(player, 8064) == 0) && (getItemCount(player, 8067) == 0))
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
			return;
		}
		openDoor(MainDoorId);
		show("default/32034-1.htm", player, npc);
	}
	
	/**
	 * Method openSecondDoor.
	 */
	public void openSecondDoor()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (!NpcInstance.canBypassCheck(player, npc))
		{
			return;
		}
		if (getItemCount(player, 8067) == 0)
		{
			show("default/32036-2.htm", player, npc);
			return;
		}
		openDoor(SecondDoor1Id);
		openDoor(SecondDoor2Id);
		show("default/32036-1.htm", player, npc);
	}
	
	/**
	 * Method pressSkull.
	 */
	public void pressSkull()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (!NpcInstance.canBypassCheck(player, npc))
		{
			return;
		}
		openDoor(MainDoorId);
		show("default/32035-1.htm", player, npc);
	}
	
	/**
	 * Method press2ndSkull.
	 */
	public void press2ndSkull()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (!NpcInstance.canBypassCheck(player, npc))
		{
			return;
		}
		openDoor(SecondDoor1Id);
		openDoor(SecondDoor2Id);
		show("default/32037-1.htm", player, npc);
	}
	
	/**
	 * Method openDoor.
	 * @param doorId int
	 */
	private static void openDoor(int doorId)
	{
		DoorInstance door = ReflectionUtils.getDoor(doorId);
		door.openMe();
	}
}
