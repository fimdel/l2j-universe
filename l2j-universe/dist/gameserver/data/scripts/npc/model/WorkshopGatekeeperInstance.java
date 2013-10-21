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

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WorkshopGatekeeperInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field doorRecharge.
	 */
	private static long doorRecharge = 0;
	/**
	 * Field doors.
	 */
	private static final IntObjectMap<IntSet> doors = new HashIntObjectMap<>();
	static
	{
		IntSet list;
		list = new HashIntSet();
		list.add(19260001);
		list.add(19260002);
		doors.put(18445, list);
		list = new HashIntSet();
		list.add(19260003);
		doors.put(18446, list);
		list = new HashIntSet();
		list.add(19260003);
		list.add(19260004);
		list.add(19260005);
		doors.put(18447, list);
		list = new HashIntSet();
		list.add(19260006);
		list.add(19260007);
		doors.put(18448, list);
		list = new HashIntSet();
		list.add(19260007);
		list.add(19260008);
		doors.put(18449, list);
		list = new HashIntSet();
		list.add(19260010);
		doors.put(18450, list);
		list = new HashIntSet();
		list.add(19260011);
		list.add(19260012);
		doors.put(18451, list);
		list = new HashIntSet();
		list.add(19260009);
		list.add(19260011);
		doors.put(18452, list);
		list = new HashIntSet();
		list.add(19260014);
		list.add(19260023);
		list.add(19260013);
		doors.put(18453, list);
		list = new HashIntSet();
		list.add(19260015);
		list.add(19260023);
		doors.put(18454, list);
		list = new HashIntSet();
		list.add(19260016);
		list.add(19260019);
		doors.put(18455, list);
		list = new HashIntSet();
		list.add(19260017);
		list.add(19260018);
		doors.put(18456, list);
		list = new HashIntSet();
		list.add(19260021);
		list.add(19260020);
		doors.put(18457, list);
		list = new HashIntSet();
		list.add(19260022);
		doors.put(18458, list);
		list = new HashIntSet();
		list.add(19260018);
		doors.put(18459, list);
		list = new HashIntSet();
		list.add(19260051);
		doors.put(18460, list);
		list = new HashIntSet();
		list.add(19260052);
		doors.put(18461, list);
	}
	
	/**
	 * Constructor for WorkshopGatekeeperInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public WorkshopGatekeeperInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.startsWith("trydoor"))
		{
			int npcId = getNpcId();
			if ((doorRecharge == 0) || (doorRecharge <= System.currentTimeMillis()))
			{
				if (player.getClassId() == ClassId.MAESTRO)
				{
					openDoor(npcId);
					player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Tully Gatekeeper:<br><br>Doors are opened."));
				}
				else if (Rnd.chance(60))
				{
					openDoor(npcId);
					player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Tully Gatekeeper:<br><br>Doors are opened."));
				}
				else
				{
					doorRecharge = System.currentTimeMillis() + (120 * 1000L);
					player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Tully Gatekeeper:<br><br>The attempt has failed. Recharching..."));
				}
			}
			else
			{
				player.sendPacket(new NpcHtmlMessage(player, this).setHtml("Tully Gatekeeper:<br><br>The time needed for the recharge has not elapsed yet"));
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		return "default/18445.htm";
	}
	
	/**
	 * Method openDoor.
	 * @param npcId int
	 */
	private void openDoor(int npcId)
	{
		IntSet set = doors.get(npcId);
		if (set != null)
		{
			for (int i : set.toArray())
			{
				DoorInstance doorToOpen = ReflectionUtils.getDoor(i);
				doorToOpen.openMe();
				ThreadPoolManager.getInstance().schedule(new DoorClose(doorToOpen), 120 * 1000L);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class DoorClose extends RunnableImpl
	{
		/**
		 * Field _door.
		 */
		DoorInstance _door;
		
		/**
		 * Constructor for DoorClose.
		 * @param door DoorInstance
		 */
		public DoorClose(DoorInstance door)
		{
			_door = door;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_door.closeMe();
		}
	}
}
