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
package ai.hellbound;

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GreaterEvil extends Fighter
{
	/**
	 * Field path.
	 */
	static final Location[] path =
	{
		new Location(28448, 243816, -3696),
		new Location(27624, 245256, -3696),
		new Location(27528, 246808, -3656),
		new Location(28296, 247912, -3248),
		new Location(25880, 246184, -3176)
	};
	/**
	 * Field current_point.
	 */
	private int current_point = 0;
	
	/**
	 * Constructor for GreaterEvil.
	 * @param actor NpcInstance
	 */
	public GreaterEvil(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = 6000;
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
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return true;
		}
		if (_def_think)
		{
			doTask();
			return true;
		}
		if (current_point >= path.length)
		{
			actor.doDie(null);
			current_point = 0;
			return true;
		}
		actor.setRunning();
		addTaskMove(path[current_point], false);
		doTask();
		return false;
	}
	
	/**
	 * Method onEvtArrived.
	 */
	@Override
	protected void onEvtArrived()
	{
		current_point++;
		super.onEvtArrived();
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
}
