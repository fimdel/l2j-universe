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
package lineage2.gameserver.model.entity.events.impl;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.collections.MultiValueSet;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.BoatHolder;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.boat.Boat;
import lineage2.gameserver.model.entity.boat.ClanAirShip;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.entity.events.objects.BoatPoint;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.MapUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BoatWayEvent extends GlobalEvent
{
	/**
	 * Field BOAT_POINTS. (value is ""boat_points"")
	 */
	public static final String BOAT_POINTS = "boat_points";
	/**
	 * Field _ticketId.
	 */
	private final int _ticketId;
	/**
	 * Field _returnLoc.
	 */
	private final Location _returnLoc;
	/**
	 * Field _boat.
	 */
	private final Boat _boat;
	
	/**
	 * Constructor for BoatWayEvent.
	 * @param boat ClanAirShip
	 */
	public BoatWayEvent(ClanAirShip boat)
	{
		super(boat.getObjectId(), "ClanAirShip");
		_ticketId = 0;
		_boat = boat;
		_returnLoc = null;
	}
	
	/**
	 * Constructor for BoatWayEvent.
	 * @param set MultiValueSet<String>
	 */
	public BoatWayEvent(MultiValueSet<String> set)
	{
		super(set);
		_ticketId = set.getInteger("ticketId", 0);
		_returnLoc = Location.parseLoc(set.getString("return_point"));
		String className = set.getString("class", null);
		if (className != null)
		{
			_boat = BoatHolder.getInstance().initBoat(getName(), className);
			Location loc = Location.parseLoc(set.getString("spawn_point"));
			_boat.setLoc(loc, true);
			_boat.setHeading(loc.h);
		}
		else
		{
			_boat = BoatHolder.getInstance().getBoat(getName());
		}
		_boat.setWay(className != null ? 1 : 0, this);
	}
	
	/**
	 * Method initEvent.
	 */
	@Override
	public void initEvent()
	{
	}
	
	/**
	 * Method startEvent.
	 */
	@Override
	public void startEvent()
	{
		L2GameServerPacket startPacket = _boat.startPacket();
		for (Player player : _boat.getPlayers())
		{
			if (_ticketId > 0)
			{
				if (player.consumeItem(_ticketId, 1))
				{
					if (startPacket != null)
					{
						player.sendPacket(startPacket);
					}
				}
				else
				{
					player.sendPacket(SystemMsg.YOU_DO_NOT_POSSESS_THE_CORRECT_TICKET_TO_BOARD_THE_BOAT);
					_boat.oustPlayer(player, _returnLoc, true);
				}
			}
			else
			{
				if (startPacket != null)
				{
					player.sendPacket(startPacket);
				}
			}
		}
		moveNext();
	}
	
	/**
	 * Method moveNext.
	 */
	public void moveNext()
	{
		List<BoatPoint> points = getObjects(BOAT_POINTS);
		if (_boat.getRunState() >= points.size())
		{
			_boat.trajetEnded(true);
			return;
		}
		final BoatPoint bp = points.get(_boat.getRunState());
		if (bp.getSpeed1() >= 0)
		{
			_boat.setMoveSpeed(bp.getSpeed1());
		}
		if (bp.getSpeed2() >= 0)
		{
			_boat.setRotationSpeed(bp.getSpeed2());
		}
		if (_boat.getRunState() == 0)
		{
			_boat.broadcastCharInfo();
		}
		_boat.setRunState(_boat.getRunState() + 1);
		if (bp.isTeleport())
		{
			_boat.teleportShip(bp.getX(), bp.getY(), bp.getZ());
		}
		else
		{
			_boat.moveToLocation(bp.getX(), bp.getY(), bp.getZ(), 0, false);
		}
	}
	
	/**
	 * Method reCalcNextTime.
	 * @param onInit boolean
	 */
	@Override
	public void reCalcNextTime(boolean onInit)
	{
		registerActions();
	}
	
	/**
	 * Method startTimeMillis.
	 * @return long
	 */
	@Override
	protected long startTimeMillis()
	{
		return System.currentTimeMillis();
	}
	
	/**
	 * Method broadcastPlayers.
	 * @param range int
	 * @return List<Player>
	 */
	@Override
	public List<Player> broadcastPlayers(int range)
	{
		if (range <= 0)
		{
			List<Player> list = new ArrayList<>();
			int rx = MapUtils.regionX(_boat.getX());
			int ry = MapUtils.regionY(_boat.getY());
			int offset = Config.SHOUT_OFFSET;
			for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			{
				if (player.getReflection() != _boat.getReflection())
				{
					continue;
				}
				int tx = MapUtils.regionX(player);
				int ty = MapUtils.regionY(player);
				if ((tx >= (rx - offset)) && (tx <= (rx + offset)) && (ty >= (ry - offset)) && (ty <= (ry + offset)))
				{
					list.add(player);
				}
			}
			return list;
		}
		return World.getAroundPlayers(_boat, range, Math.max(range / 2, 200));
	}
	
	/**
	 * Method printInfo.
	 */
	@Override
	protected void printInfo()
	{
	}
	
	/**
	 * Method getReturnLoc.
	 * @return Location
	 */
	public Location getReturnLoc()
	{
		return _returnLoc;
	}
}
