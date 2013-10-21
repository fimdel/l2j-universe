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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Typhoon extends Fighter
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(-16696, 250232, -2956),
		new Location(-17944, 251000, -3213),
		new Location(-19000, 252312, -3385),
		new Location(-20840, 253000, -3343),
		new Location(-20792, 255320, -3267),
		new Location(-19368, 256936, -3175),
		new Location(-16968, 255656, -3207),
		new Location(-17160, 253208, -3461),
		new Location(-15240, 253576, -3476),
		new Location(-13128, 254792, -3424),
		new Location(-10776, 256120, -3340),
		new Location(-8600, 256712, -3234),
		new Location(-4792, 254344, -3143),
		new Location(-4024, 252360, -3325),
		new Location(-5320, 251224, -3240),
		new Location(-8424, 251432, -2950),
		new Location(-11240, 252856, -3114),
		new Location(-12616, 254168, -3150),
		new Location(-14120, 254280, -3463),
		new Location(-17128, 251896, -3388),
		new Location(-16712, 250520, -3029),
		new Location(-15864, 250872, -3013)
	};
	/**
	 * Field current_point.
	 */
	private int current_point = -1;
	/**
	 * Field wait_timeout.
	 */
	private long wait_timeout = 0;
	/**
	 * Field wait.
	 */
	private boolean wait = false;
	
	/**
	 * Constructor for Typhoon.
	 * @param actor NpcInstance
	 */
	public Typhoon(NpcInstance actor)
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
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return false;
		}
		if ((getIntention() != CtrlIntention.AI_INTENTION_ACTIVE) && (current_point > -1))
		{
			current_point--;
		}
		actor.getAggroList().addDamageHate(target, 0, 1);
		setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		return true;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	public boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return true;
		}
		if (_def_think)
		{
			if (doTask())
			{
				clearTasks();
			}
			return true;
		}
		if (super.thinkActive())
		{
			return true;
		}
		if ((System.currentTimeMillis() > wait_timeout) && ((current_point > -1) || Rnd.chance(5)))
		{
			if (!wait && (current_point == 31))
			{
				wait_timeout = System.currentTimeMillis() + 30000;
				wait = true;
				return true;
			}
			wait_timeout = 0;
			wait = false;
			current_point++;
			if (current_point >= points.length)
			{
				current_point = 0;
			}
			actor.setWalking();
			addTaskMove(points[current_point], true);
			doTask();
			return true;
		}
		if (randomAnimation())
		{
			return false;
		}
		return false;
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
}
