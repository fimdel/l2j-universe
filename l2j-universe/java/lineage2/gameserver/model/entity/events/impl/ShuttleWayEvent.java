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

import gnu.trove.list.array.TIntArrayList;
import lineage2.commons.collections.MultiValueSet;
import lineage2.gameserver.data.BoatHolder;
import lineage2.gameserver.model.entity.boat.Shuttle;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ShuttleWayEvent extends GlobalEvent
{
	/**
	 * Field _shuttle.
	 */
	private final Shuttle _shuttle;
	/**
	 * Field _nextFloorLoc.
	 */
	private final Location _nextFloorLoc;
	/**
	 * Field _floorDoorsId.
	 */
	private final TIntArrayList _floorDoorsId = new TIntArrayList();
	/**
	 * Field _speed.
	 */
	private final int _speed;
	/**
	 * Field _returnLoc.
	 */
	private final Location _returnLoc;
	
	/**
	 * Constructor for ShuttleWayEvent.
	 * @param set MultiValueSet<String>
	 */
	public ShuttleWayEvent(MultiValueSet<String> set)
	{
		super(set);
		int shuttleId = set.getInteger("shuttle_id", -1);
		if (shuttleId > 0)
		{
			_shuttle = BoatHolder.getInstance().initShuttle(getName(), shuttleId);
			Location loc = Location.parseLoc(set.getString("spawn_point"));
			_shuttle.setLoc(loc, true);
			_shuttle.setHeading(loc.h);
		}
		else
		{
			_shuttle = (Shuttle) BoatHolder.getInstance().getBoat(getName());
		}
		_nextFloorLoc = Location.parseLoc(set.getString("next_floor_loc"));
		_floorDoorsId.add(set.getIntegerArray("floor_doors_id"));
		_speed = set.getInteger("speed");
		_returnLoc = Location.parseLoc(set.getString("return_point"));
		_shuttle.addFloor(this);
	}
	
	/**
	 * Method startEvent.
	 */
	@Override
	public void startEvent()
	{
		super.startEvent();
		_shuttle.setMoveSpeed(_speed);
		_shuttle.setRunState(1);
		_shuttle.broadcastCharInfo();
		_shuttle.moveToLocation(_nextFloorLoc.getX(), _nextFloorLoc.getY(), _nextFloorLoc.getZ(), 0, false);
	}
	
	/**
	 * Method stopEvent.
	 */
	@Override
	public void stopEvent()
	{
		super.stopEvent();
		_shuttle.setRunState(0);
		_shuttle.broadcastCharInfo();
	}
	
	/**
	 * Method reCalcNextTime.
	 * @param onInit boolean
	 */
	@Override
	public void reCalcNextTime(boolean onInit)
	{
		if (onInit)
		{
			return;
		}
		clearActions();
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
	 * Method printInfo.
	 */
	@Override
	protected void printInfo()
	{
	}
	
	/**
	 * Method isThisFloorDoor.
	 * @param doorId int
	 * @return boolean
	 */
	public boolean isThisFloorDoor(int doorId)
	{
		return _floorDoorsId.contains(doorId);
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
