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
package ai;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class OctavisRider extends DefaultAI
{
	/**
	 * Field _points.
	 */
	private final Location[] _points =
	{
		new Location(207992, 120904, -10038, 49151),
		new Location(207544, 121384, -10038),
		new Location(206856, 121384, -10038),
		new Location(206392, 120920, -10038),
		new Location(206392, 120264, -10038),
		new Location(206856, 119768, -10038),
		new Location(207528, 119768, -10038),
		new Location(207992, 120232, -10038)
	};
	/**
	 * Field _lastPoint.
	 */
	private int _lastPoint = 0;
	
	/**
	 * Constructor for OctavisRider.
	 * @param actor NpcInstance
	 */
	public OctavisRider(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method isGlobalAI.
	 * @return boolean
	 */
	@Override
	public boolean isGlobalAI()
	{
		return true;
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
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		startMoveTask();
		super.onEvtArrived();
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
			_lastPoint = 0;
		}
		npc.setRunning();
		addTaskMove(_points[_lastPoint], false);
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
	 * Method onEvtAggression.
	 * @param target Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		// empty method
	}
}
