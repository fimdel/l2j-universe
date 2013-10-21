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
package ai.dragonvalley;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Patrollers extends Fighter
{
	/**
	 * Field _points.
	 */
	protected Location[] _points;
	/**
	 * Field _teleporters.
	 */
	private final int[] _teleporters =
	{
		22857,
		22833,
		22834
	};
	/**
	 * Field _lastPoint.
	 */
	private int _lastPoint = 0;
	/**
	 * Field _firstThought.
	 */
	private boolean _firstThought = true;
	
	/**
	 * Constructor for Patrollers.
	 * @param actor NpcInstance
	 */
	public Patrollers(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = Integer.MAX_VALUE - 10;
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
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		final NpcInstance actor = getActor();
		if (target.isPlayable() && !target.isDead() && !target.isInvisible())
		{
			actor.getAggroList().addDamageHate(target, 0, 1);
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}
		return true;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if (super.thinkActive())
		{
			return true;
		}
		if (!getActor().isMoving)
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
		if (_firstThought)
		{
			_lastPoint = getIndex(Location.findNearest(npc, _points));
			_firstThought = false;
		}
		else
		{
			_lastPoint++;
		}
		if (_lastPoint >= _points.length)
		{
			_lastPoint = 0;
			if (ArrayUtils.contains(_teleporters, npc.getNpcId()))
			{
				npc.teleToLocation(_points[_lastPoint]);
			}
		}
		npc.setRunning();
		if (Rnd.chance(30))
		{
			npc.altOnMagicUseTimer(npc, SkillTable.getInstance().getInfo(6757, 1));
		}
		try
		{
			addTaskMove(Location.findPointToStay(_points[_lastPoint], 250, npc.getGeoIndex()), true);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			// empty catch clause
		}
		doTask();
	}
	
	/**
	 * Method getIndex.
	 * @param loc Location
	 * @return int
	 */
	private int getIndex(Location loc)
	{
		for (int i = 0; i < _points.length; i++)
		{
			if (_points[i].equals(loc))
			{
				return i;
			}
		}
		return 0;
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
	
	/**
	 * Method returnHome.
	 * @param clearAggro boolean
	 * @param teleport boolean
	 */
	@Override
	protected void returnHome(boolean clearAggro, boolean teleport)
	{
		super.returnHome(clearAggro, teleport);
		clearTasks();
		_firstThought = true;
		startMoveTask();
	}
}
