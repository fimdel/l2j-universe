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
package lineage2.gameserver.templates.spawn;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WalkerRouteTemplate
{
	/**
	 * @author Mobius
	 */
	public enum RouteType
	{
		/**
		 * Field CYCLE.
		 */
		CYCLE(0),
		/**
		 * Field LINEAR.
		 */
		LINEAR(1),
		/**
		 * Field RANDOM.
		 */
		RANDOM(2),
		/**
		 * Field TELEPORT.
		 */
		TELEPORT(3);
		/**
		 * Field _id.
		 */
		int _id;
		
		/**
		 * Constructor for RouteType.
		 * @param id int
		 */
		RouteType(int id)
		{
			_id = id;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class Route
	{
		/**
		 * Field _h.
		 */
		/**
		 * Field _z.
		 */
		/**
		 * Field _y.
		 */
		/**
		 * Field _x.
		 */
		private final int _x, _y, _z, _h;
		/**
		 * Field _delay.
		 */
		private final long _delay;
		/**
		 * Field _loc.
		 */
		private Location _loc;
		/**
		 * Field _end.
		 */
		private boolean _end = false;
		
		/**
		 * Constructor for Route.
		 * @param x int
		 * @param y int
		 * @param z int
		 * @param h int
		 * @param delay long
		 * @param end boolean
		 */
		public Route(int x, int y, int z, int h, long delay, boolean end)
		{
			_x = x;
			_y = y;
			_z = z;
			_h = h;
			_delay = delay;
			_end = end;
		}
		
		/**
		 * Constructor for Route.
		 * @param x int
		 * @param y int
		 * @param z int
		 */
		public Route(int x, int y, int z)
		{
			this(x, y, z, 0, 0, false);
		}
		
		/**
		 * Constructor for Route.
		 * @param x int
		 * @param y int
		 * @param z int
		 * @param h int
		 */
		public Route(int x, int y, int z, int h)
		{
			this(x, y, z, h, 0, false);
		}
		
		/**
		 * Constructor for Route.
		 * @param x int
		 * @param y int
		 * @param z int
		 * @param delay long
		 */
		public Route(int x, int y, int z, long delay)
		{
			this(x, y, z, 0, delay, false);
		}
		
		/**
		 * Constructor for Route.
		 * @param x int
		 * @param y int
		 * @param z int
		 * @param delay long
		 * @param end boolean
		 */
		public Route(int x, int y, int z, long delay, boolean end)
		{
			this(x, y, z, 0, delay, end);
		}
		
		/**
		 * Method getLoc.
		 * @return Location
		 */
		public Location getLoc()
		{
			if (_loc == null)
			{
				_loc = new Location(_x, _y, _z, _h);
			}
			return _loc;
		}
		
		/**
		 * Method getHeading.
		 * @return int
		 */
		public int getHeading()
		{
			return _h;
		}
		
		/**
		 * Method getDelay.
		 * @return long
		 */
		public long getDelay()
		{
			return _delay;
		}
		
		/**
		 * Method getLastPoint.
		 * @return boolean
		 */
		public boolean getLastPoint()
		{
			return _end;
		}
	}
	
	/**
	 * Field _npcId.
	 */
	private final int _npcId;
	/**
	 * Field _walkRange.
	 */
	private final int _walkRange;
	/**
	 * Field _delay.
	 */
	private final long _delay;
	/**
	 * Field _type.
	 */
	private final RouteType _type;
	/**
	 * Field _routes.
	 */
	private final List<Route> _routes = new ArrayList<>();
	/**
	 * Field _isRunning.
	 */
	private boolean _isRunning = false;
	
	/**
	 * Constructor for WalkerRouteTemplate.
	 * @param npcId int
	 * @param delay long
	 * @param type RouteType
	 * @param isRunning boolean
	 * @param walkRange int
	 */
	public WalkerRouteTemplate(int npcId, long delay, RouteType type, boolean isRunning, int walkRange)
	{
		_npcId = npcId;
		_delay = delay;
		_type = type;
		_isRunning = isRunning;
		_walkRange = walkRange;
	}
	
	/**
	 * Method setRoute.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param h int
	 * @param delay long
	 * @param end boolean
	 */
	public void setRoute(int x, int y, int z, int h, long delay, boolean end)
	{
		_routes.add(new Route(x, y, z, h, delay, end));
	}
	
	/**
	 * Method setRoute.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public void setRoute(int x, int y, int z)
	{
		setRoute(x, y, z, 0, 0, false);
	}
	
	/**
	 * Method setRoute.
	 * @param route Route
	 */
	public void setRoute(Route route)
	{
		setRoute(route);
	}
	
	/**
	 * Method getNpcId.
	 * @return int
	 */
	public int getNpcId()
	{
		return _npcId;
	}
	
	/**
	 * Method getWalkRange.
	 * @return int
	 */
	public int getWalkRange()
	{
		return _walkRange;
	}
	
	/**
	 * Method getDelay.
	 * @return long
	 */
	public long getDelay()
	{
		return _delay;
	}
	
	/**
	 * Method getRouteType.
	 * @return RouteType
	 */
	public RouteType getRouteType()
	{
		return _type;
	}
	
	/**
	 * Method getPoints.
	 * @return List<Route>
	 */
	public List<Route> getPoints()
	{
		return _routes;
	}
	
	/**
	 * Method getPointsCount.
	 * @return int
	 */
	public int getPointsCount()
	{
		return _routes.size();
	}
	
	/**
	 * Method getIsRunning.
	 * @return boolean
	 */
	public boolean getIsRunning()
	{
		return _isRunning;
	}
}
