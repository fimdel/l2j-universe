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
package ai.suspiciousmerchant;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SuspiciousMerchantWhiteSands extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(114436, 202528, -3408),
		new Location(113809, 200514, -3720),
		new Location(116035, 199822, -3664),
		new Location(117017, 199876, -3632),
		new Location(119959, 201032, -3608),
		new Location(121849, 200614, -3384),
		new Location(122868, 200874, -3168),
		new Location(123130, 202427, -3128),
		new Location(122427, 204162, -3488),
		new Location(122661, 204842, -3576),
		new Location(124051, 205402, -3576),
		new Location(124211, 206023, -3504),
		new Location(124948, 206778, -3400),
		new Location(124483, 207777, -3200),
		new Location(124948, 206778, -3400),
		new Location(124211, 206023, -3504),
		new Location(124051, 205402, -3576),
		new Location(122661, 204842, -3576),
		new Location(122427, 204162, -3488),
		new Location(123130, 202427, -3128),
		new Location(122868, 200874, -3168),
		new Location(121849, 200614, -3384),
		new Location(119959, 201032, -3608),
		new Location(117017, 199876, -3632),
		new Location(116035, 199822, -3664),
		new Location(113809, 200514, -3720),
		new Location(114436, 202528, -3408)
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
	 * Constructor for SuspiciousMerchantWhiteSands.
	 * @param actor NpcInstance
	 */
	public SuspiciousMerchantWhiteSands(NpcInstance actor)
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
		if (actor.isMoving)
		{
			return true;
		}
		if ((System.currentTimeMillis() > wait_timeout) && ((current_point > -1) || Rnd.chance(5)))
		{
			if (!wait)
			{
				switch (current_point)
				{
					case 0:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 7:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 13:
						wait_timeout = System.currentTimeMillis() + 60000;
						wait = true;
						return true;
					case 19:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 26:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
				}
			}
			wait_timeout = 0;
			wait = false;
			current_point++;
			if (current_point >= points.length)
			{
				current_point = 0;
			}
			addTaskMove(points[current_point], false);
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
