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
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DaimonTheWhiteEyed extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(191276, -49556, -2960),
		new Location(193537, -47182, -2984),
		new Location(194317, -43736, -2872),
		new Location(193336, -42510, -2888),
		new Location(194633, -40843, -2872),
		new Location(194498, -39516, -2912),
		new Location(191985, -35868, -2904),
		new Location(190083, -35015, -2912),
		new Location(187815, -36733, -3146),
		new Location(186256, -35136, -3072),
		new Location(184477, -36749, -3080),
		new Location(180834, -37288, -3104),
		new Location(179653, -38946, -3176),
		new Location(179854, -42412, -3248),
		new Location(177627, -43341, -3336),
		new Location(177842, -45723, -3456),
		new Location(180459, -47145, -3256),
		new Location(175858, -51288, -3496),
		new Location(173028, -49337, -3520),
		new Location(171936, -46364, -3472),
		new Location(173074, -44264, -3488),
		new Location(172575, -42937, -3464),
		new Location(170964, -41753, -3464),
		new Location(170428, -39132, -3432)
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
	 * Constructor for DaimonTheWhiteEyed.
	 * @param actor NpcInstance
	 */
	public DaimonTheWhiteEyed(NpcInstance actor)
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
		if ((System.currentTimeMillis() > wait_timeout) && ((current_point > -1) || Rnd.chance(5)))
		{
			if (!wait && (current_point == 23))
			{
				wait_timeout = System.currentTimeMillis() + 5000;
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
			addTaskMove(points[current_point], true);
			doTask();
			return true;
		}
		if (randomAnimation())
		{
			return true;
		}
		return false;
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
