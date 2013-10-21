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
public class Gordon extends Fighter
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(146268, -64651, -3412),
		new Location(143678, -64045, -3434),
		new Location(141620, -62316, -3210),
		new Location(139466, -60839, -2994),
		new Location(138429, -57679, -3548),
		new Location(139402, -55879, -3334),
		new Location(139660, -52780, -2908),
		new Location(139516, -50343, -2591),
		new Location(140059, -48657, -2271),
		new Location(140319, -46063, -2408),
		new Location(142462, -45540, -2432),
		new Location(144290, -43543, -2380),
		new Location(146494, -43234, -2325),
		new Location(148416, -43186, -2329),
		new Location(151135, -44084, -2746),
		new Location(153040, -42240, -2920),
		new Location(154871, -39193, -3294),
		new Location(156725, -41827, -3569),
		new Location(157788, -45071, -3598),
		new Location(159433, -45943, -3547),
		new Location(160327, -47404, -3681),
		new Location(159106, -48215, -3691),
		new Location(159541, -50908, -3563),
		new Location(159576, -53782, -3226),
		new Location(160918, -56899, -2790),
		new Location(160785, -59505, -2662),
		new Location(158252, -60098, -2680),
		new Location(155962, -59751, -2656),
		new Location(154649, -60214, -2701),
		new Location(153121, -63319, -2969),
		new Location(151511, -64366, -3174),
		new Location(149161, -64576, -3316)
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
	 * Constructor for Gordon.
	 * @param actor NpcInstance
	 */
	public Gordon(NpcInstance actor)
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
		if (!target.isCursedWeaponEquipped())
		{
			return false;
		}
		if ((getIntention() != CtrlIntention.AI_INTENTION_ACTIVE) && (current_point > -1))
		{
			current_point--;
		}
		return super.checkAggression(target);
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
				wait_timeout = System.currentTimeMillis() + 60000;
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
