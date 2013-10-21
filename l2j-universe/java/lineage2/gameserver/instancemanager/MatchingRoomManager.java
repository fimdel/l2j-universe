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
package lineage2.gameserver.instancemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.matching.MatchingRoom;
import lineage2.gameserver.templates.mapregion.RestartArea;
import lineage2.gameserver.templates.mapregion.RestartPoint;

import org.apache.commons.lang3.ArrayUtils;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.CHashIntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MatchingRoomManager
{
	/**
	 * @author Mobius
	 */
	private class RoomsHolder
	{
		/**
		 * Field _id.
		 */
		private int _id = 1;
		
		/**
		 * Field _rooms.
		 */
		final IntObjectMap<MatchingRoom> _rooms = new CHashIntObjectMap<>();
		
		/**
		 * Constructor for RoomsHolder.
		 */
		public RoomsHolder()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method addRoom.
		 * @param r MatchingRoom
		 * @return int
		 */
		public int addRoom(MatchingRoom r)
		{
			int val = _id++;
			_rooms.put(val, r);
			return val;
		}
	}
	
	/**
	 * Field _instance.
	 */
	private static final MatchingRoomManager _instance = new MatchingRoomManager();
	
	/**
	 * Method getInstance.
	 * @return MatchingRoomManager
	 */
	public static MatchingRoomManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field _holder.
	 */
	private final RoomsHolder[] _holder = new RoomsHolder[2];
	/**
	 * Field _players.
	 */
	private final Set<Player> _players = new CopyOnWriteArraySet<>();
	
	/**
	 * Constructor for MatchingRoomManager.
	 */
	public MatchingRoomManager()
	{
		_holder[MatchingRoom.PARTY_MATCHING] = new RoomsHolder();
		_holder[MatchingRoom.CC_MATCHING] = new RoomsHolder();
	}
	
	/**
	 * Method addToWaitingList.
	 * @param player Player
	 */
	public void addToWaitingList(Player player)
	{
		_players.add(player);
	}
	
	/**
	 * Method removeFromWaitingList.
	 * @param player Player
	 */
	public void removeFromWaitingList(Player player)
	{
		_players.remove(player);
	}
	
	/**
	 * Method getWaitingList.
	 * @param minLevel int
	 * @param maxLevel int
	 * @param classes int[]
	 * @return List<Player>
	 */
	public List<Player> getWaitingList(int minLevel, int maxLevel, int[] classes)
	{
		List<Player> res = new ArrayList<>();
		for (Player $member : _players)
		{
			if (($member.getLevel() >= minLevel) && ($member.getLevel() <= maxLevel))
			{
				if ((classes.length == 0) || ArrayUtils.contains(classes, $member.getClassId().getId()))
				{
					res.add($member);
				}
			}
		}
		
		return res;
	}
	
	/**
	 * Method getMatchingRooms.
	 * @param type int
	 * @param region int
	 * @param allLevels boolean
	 * @param activeChar Player
	 * @return List<MatchingRoom>
	 */
	public List<MatchingRoom> getMatchingRooms(int type, int region, boolean allLevels, Player activeChar)
	{
		List<MatchingRoom> res = new ArrayList<>();
		for (MatchingRoom room : _holder[type]._rooms.values())
		{
			if ((region > 0) && (room.getLocationId() != region))
			{
				continue;
			}
			else if ((region == -2) && (room.getLocationId() != MatchingRoomManager.getInstance().getLocation(activeChar)))
			{
				continue;
			}
			if (!allLevels && ((room.getMinLevel() > activeChar.getLevel()) || (room.getMaxLevel() < activeChar.getLevel())))
			{
				continue;
			}
			res.add(room);
		}
		return res;
	}
	
	/**
	 * Method addMatchingRoom.
	 * @param r MatchingRoom
	 * @return int
	 */
	public int addMatchingRoom(MatchingRoom r)
	{
		return _holder[r.getType()].addRoom(r);
	}
	
	/**
	 * Method removeMatchingRoom.
	 * @param r MatchingRoom
	 */
	public void removeMatchingRoom(MatchingRoom r)
	{
		_holder[r.getType()]._rooms.remove(r.getId());
	}
	
	/**
	 * Method getMatchingRoom.
	 * @param type int
	 * @param id int
	 * @return MatchingRoom
	 */
	public MatchingRoom getMatchingRoom(int type, int id)
	{
		return _holder[type]._rooms.get(id);
	}
	
	/**
	 * Method getLocation.
	 * @param player Player
	 * @return int
	 */
	public int getLocation(Player player)
	{
		if (player == null)
		{
			return 0;
		}
		
		RestartArea ra = MapRegionManager.getInstance().getRegionData(RestartArea.class, player);
		if (ra != null)
		{
			RestartPoint rp = ra.getRestartPoint().get(player.getRace());
			return rp.getBbs();
		}
		
		return 0;
	}
}
