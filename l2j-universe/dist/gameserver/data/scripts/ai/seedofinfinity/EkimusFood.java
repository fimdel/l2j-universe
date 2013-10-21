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
package ai.seedofinfinity;

import instances.HeartInfinityDefence;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EkimusFood extends DefaultAI
{
	/**
	 * Field _route1.
	 */
	private static final Location[] _route1 =
	{
		new Location(-179544, 207400, -15496),
		new Location(-178856, 207464, -15496),
		new Location(-178168, 207864, -15496),
		new Location(-177512, 208728, -15496),
		new Location(-177336, 209528, -15496),
		new Location(-177448, 210328, -15496),
		new Location(-177864, 211048, -15496),
		new Location(-178584, 211608, -15496),
		new Location(-179304, 211848, -15496),
		new Location(-179512, 211864, -15496),
		new Location(-179528, 211448, -15472)
	};
	/**
	 * Field _route2.
	 */
	private static final Location[] _route2 =
	{
		new Location(-179576, 207352, -15496),
		new Location(-180440, 207544, -15496),
		new Location(-181256, 208152, -15496),
		new Location(-181752, 209112, -15496),
		new Location(-181720, 210264, -15496),
		new Location(-181096, 211224, -15496),
		new Location(-180264, 211720, -15496),
		new Location(-179528, 211848, -15496),
		new Location(-179528, 211400, -15472)
	};
	/**
	 * Field _points.
	 */
	private final Location[] _points;
	/**
	 * Field _lastPoint.
	 */
	private int _lastPoint = 0;
	
	/**
	 * Constructor for EkimusFood.
	 * @param actor NpcInstance
	 */
	public EkimusFood(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = Integer.MAX_VALUE - 10;
		_points = Rnd.chance(50) ? _route1 : _route2;
		actor.startDebuffImmunity();
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		return false;
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		startMoveTask();
		super.onEvtArrived();
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if (!_def_think)
		{
			startMoveTask();
		}
		return true;
	}
	
	/**
	 * Method startMoveTask.
	 */
	private void startMoveTask()
	{
		final NpcInstance npc = getActor();
		_lastPoint++;
		if (_lastPoint >= _points.length)
		{
			if (!npc.getReflection().isDefault())
			{
				((HeartInfinityDefence) npc.getReflection()).notifyWagonArrived();
				npc.deleteMe();
				return;
			}
		}
		addTaskMove(Location.findPointToStay(_points[_lastPoint], 250, npc.getGeoIndex()), true);
		doTask();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		// empty method
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	/**
	 * Method maybeMoveToHome.
	 * @return boolean
	 */
	@Override
	protected boolean maybeMoveToHome()
	{
		return false;
	}
	
	/**
	 * Method teleportHome.
	 */
	@Override
	protected void teleportHome()
	{
		// empty method
	}
}
