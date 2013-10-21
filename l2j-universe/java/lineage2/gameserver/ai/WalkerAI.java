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
package lineage2.gameserver.ai;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.spawn.WalkerRouteTemplate;
import lineage2.gameserver.templates.spawn.WalkerRouteTemplate.Route;
import lineage2.gameserver.templates.spawn.WalkerRouteTemplate.RouteType;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WalkerAI extends DefaultAI
{
	/**
	 * Field _routeIndex.
	 */
	private int _routeIndex = 0;
	/**
	 * Field _direction.
	 */
	private short _direction = 1;
	/**
	 * Field _lastMove.
	 */
	private long _lastMove = 0;
	
	/**
	 * Constructor for WalkerAI.
	 * @param actor NpcInstance
	 */
	public WalkerAI(NpcInstance actor)
	{
		super(actor);
		setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		WalkerRouteTemplate routeTemplate = getActor().getWalkerRouteTemplate();
		if (routeTemplate == null)
		{
			return false;
		}
		boolean LINEAR = (routeTemplate.getRouteType() == RouteType.LINEAR);
		boolean CYCLE = (routeTemplate.getRouteType() == RouteType.CYCLE);
		boolean TELEPORT = (routeTemplate.getRouteType() == RouteType.TELEPORT);
		boolean RANDOM = (routeTemplate.getRouteType() == RouteType.RANDOM);
		if (routeTemplate.getIsRunning())
		{
			getActor().setRunning();
		}
		int pointsCount = routeTemplate.getPointsCount();
		if (pointsCount <= 0)
		{
			return false;
		}
		Route point = null;
		int oldIndex = _routeIndex;
		if (((_routeIndex + _direction) >= pointsCount) || ((_routeIndex + _direction) < 0))
		{
			if (LINEAR)
			{
				_direction *= -1;
				_routeIndex += _direction;
				point = routeTemplate.getPoints().get(_routeIndex);
			}
			else if (CYCLE)
			{
				_direction = 1;
				_routeIndex = 0;
				point = routeTemplate.getPoints().get(_routeIndex);
			}
			else if (TELEPORT)
			{
				_direction = 1;
				_routeIndex = 0;
				point = routeTemplate.getPoints().get(_routeIndex);
			}
			else if (RANDOM)
			{
				randomWalk();
			}
		}
		else
		{
			_routeIndex += _direction;
			point = routeTemplate.getPoints().get(_routeIndex);
		}
		Location nextLoc = point.getLoc();
		long delay = (point.getDelay() <= 0) ? routeTemplate.getDelay() : point.getDelay();
		if (_lastMove == 0)
		{
			_lastMove = System.currentTimeMillis() + delay;
			_routeIndex = oldIndex;
			return false;
		}
		else if (getActor().isMoving)
		{
			_routeIndex = oldIndex;
			return false;
		}
		else if (RANDOM)
		{
			return false;
		}
		else if ((System.currentTimeMillis() - _lastMove) > delay)
		{
			getActor().moveToLocation(nextLoc, 0, true);
			_lastMove = System.currentTimeMillis();
			if (TELEPORT & point.getLastPoint())
			{
				getActor().teleToLocation(nextLoc);
				_lastMove = System.currentTimeMillis();
			}
		}
		return true;
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		WalkerRouteTemplate routeTemplate = getActor().getWalkerRouteTemplate();
		int AI_WALK_RANGE = routeTemplate.getWalkRange();
		if ((AI_WALK_RANGE == 0) | (routeTemplate.getRouteType() == RouteType.RANDOM))
		{
			return false;
		}
		NpcInstance actor = getActor();
		if (actor == null)
		{
			return false;
		}
		Location sloc = actor.getSpawnedLoc();
		int x = (sloc.x + Rnd.get(2 * AI_WALK_RANGE)) - AI_WALK_RANGE;
		int y = (sloc.y + Rnd.get(2 * AI_WALK_RANGE)) - AI_WALK_RANGE;
		int z = GeoEngine.getHeight(x, y, sloc.z, actor.getGeoIndex());
		actor.setRunning();
		actor.moveToLocation(x, y, z, 0, true);
		return true;
	}
}
