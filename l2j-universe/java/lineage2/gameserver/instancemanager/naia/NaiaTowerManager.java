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
package lineage2.gameserver.instancemanager.naia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class NaiaTowerManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(NaiaTowerManager.class);
	/**
	 * Field _groupList.
	 */
	static Map<Integer, List<Player>> _groupList = new HashMap<>();
	/**
	 * Field _roomsDone.
	 */
	private static Map<Integer, List<Player>> _roomsDone = new HashMap<>();
	/**
	 * Field _groupTimer.
	 */
	static Map<Integer, Long> _groupTimer = new HashMap<>();
	/**
	 * Field _roomMobs.
	 */
	private static Map<Integer, List<NpcInstance>> _roomMobs;
	/**
	 * Field _roomMobList.
	 */
	private static List<NpcInstance> _roomMobList;
	/**
	 * Field _towerAccessible.
	 */
	private static long _towerAccessible = 0;
	/**
	 * Field _index.
	 */
	private static int _index = 0;
	/**
	 * Field lockedRooms.
	 */
	public static HashMap<Integer, Boolean> lockedRooms;
	/**
	 * Field _instance.
	 */
	private static final NaiaTowerManager _instance = new NaiaTowerManager();
	
	/**
	 * Method getInstance.
	 * @return NaiaTowerManager
	 */
	public static final NaiaTowerManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for NaiaTowerManager.
	 */
	private NaiaTowerManager()
	{
		if (lockedRooms == null)
		{
			lockedRooms = new HashMap<>();
			for (int i = 18494; i <= 18505; i++)
			{
				lockedRooms.put(i, false);
			}
			_roomMobs = new HashMap<>();
			for (int i = 18494; i <= 18505; i++)
			{
				_roomMobList = new ArrayList<>();
				_roomMobs.put(i, _roomMobList);
			}
			_log.info("Naia Tower Manager: Loaded 12 rooms");
		}
		ThreadPoolManager.getInstance().schedule(new GroupTowerTimer(), 30 * 1000L);
	}
	
	/**
	 * Method startNaiaTower.
	 * @param leader Player
	 */
	public static void startNaiaTower(Player leader)
	{
		if (leader == null)
		{
			return;
		}
		if (_towerAccessible > System.currentTimeMillis())
		{
			return;
		}
		for (Player member : leader.getParty().getPartyMembers())
		{
			member.teleToLocation(new Location(-47271, 246098, -9120));
		}
		addGroupToTower(leader);
		_towerAccessible += 20 * 60 * 1000L;
		ReflectionUtils.getDoor(18250001).openMe();
	}
	
	/**
	 * Method addGroupToTower.
	 * @param leader Player
	 */
	private static void addGroupToTower(Player leader)
	{
		_index = _groupList.keySet().size() + 1;
		_groupList.put(_index, leader.getParty().getPartyMembers());
		_groupTimer.put(_index, System.currentTimeMillis() + (5 * 60 * 1000L));
		leader.sendMessage("The Tower of Naia countdown has begun. You have only 5 minutes to pass each room.");
	}
	
	/**
	 * Method updateGroupTimer.
	 * @param player Player
	 */
	public static void updateGroupTimer(Player player)
	{
		for (int i : _groupList.keySet())
		{
			if (_groupList.get(i).contains(player))
			{
				_groupTimer.put(i, System.currentTimeMillis() + (5 * 60 * 1000L));
				player.sendMessage("Group timer has been updated");
				break;
			}
		}
	}
	
	/**
	 * Method removeGroupTimer.
	 * @param player Player
	 */
	public static void removeGroupTimer(Player player)
	{
		for (int i : _groupList.keySet())
		{
			if (_groupList.get(i).contains(player))
			{
				_groupList.remove(i);
				_groupTimer.remove(i);
			}
		}
	}
	
	/**
	 * Method isLegalGroup.
	 * @param player Player
	 * @return boolean
	 */
	public static boolean isLegalGroup(Player player)
	{
		if ((_groupList == null) || _groupList.isEmpty())
		{
			return false;
		}
		for (int i : _groupList.keySet())
		{
			if (_groupList.get(i).contains(player))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method lockRoom.
	 * @param npcId int
	 */
	public static void lockRoom(int npcId)
	{
		lockedRooms.put(npcId, true);
	}
	
	/**
	 * Method unlockRoom.
	 * @param npcId int
	 */
	public static void unlockRoom(int npcId)
	{
		lockedRooms.put(npcId, false);
	}
	
	/**
	 * Method isLockedRoom.
	 * @param npcId int
	 * @return boolean
	 */
	public static boolean isLockedRoom(int npcId)
	{
		return lockedRooms.get(npcId);
	}
	
	/**
	 * Method addRoomDone.
	 * @param roomId int
	 * @param player Player
	 */
	public static void addRoomDone(int roomId, Player player)
	{
		if (player.getParty() != null)
		{
			_roomsDone.put(roomId, player.getParty().getPartyMembers());
		}
	}
	
	/**
	 * Method isRoomDone.
	 * @param roomId int
	 * @param player Player
	 * @return boolean
	 */
	public static boolean isRoomDone(int roomId, Player player)
	{
		if ((_roomsDone == null) || _roomsDone.isEmpty())
		{
			return false;
		}
		if ((_roomsDone.get(roomId) == null) || _roomsDone.get(roomId).isEmpty())
		{
			return false;
		}
		if (_roomsDone.get(roomId).contains(player))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method addMobsToRoom.
	 * @param roomId int
	 * @param mob List<NpcInstance>
	 */
	public static void addMobsToRoom(int roomId, List<NpcInstance> mob)
	{
		_roomMobs.put(roomId, mob);
	}
	
	/**
	 * Method getRoomMobs.
	 * @param roomId int
	 * @return List<NpcInstance>
	 */
	public static List<NpcInstance> getRoomMobs(int roomId)
	{
		return _roomMobs.get(roomId);
	}
	
	/**
	 * Method removeRoomMobs.
	 * @param roomId int
	 */
	public static void removeRoomMobs(int roomId)
	{
		_roomMobs.get(roomId).clear();
	}
	
	/**
	 * @author Mobius
	 */
	private class GroupTowerTimer extends RunnableImpl
	{
		/**
		 * Constructor for GroupTowerTimer.
		 */
		public GroupTowerTimer()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			ThreadPoolManager.getInstance().schedule(new GroupTowerTimer(), 30 * 1000L);
			if (!_groupList.isEmpty() && !_groupTimer.isEmpty())
			{
				for (int i : _groupTimer.keySet())
				{
					if (_groupTimer.get(i) < System.currentTimeMillis())
					{
						for (Player kicked : _groupList.get(i))
						{
							kicked.teleToLocation(new Location(17656, 244328, 11595));
							kicked.sendMessage("The time has expired. You cannot stay in Tower of Naia any longer");
						}
						_groupList.remove(i);
						_groupTimer.remove(i);
					}
				}
			}
		}
	}
}
