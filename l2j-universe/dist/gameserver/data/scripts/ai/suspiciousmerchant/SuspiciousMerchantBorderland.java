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
public class SuspiciousMerchantBorderland extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(161876, -73407, -2984),
		new Location(161795, -75288, -3088),
		new Location(159678, -77671, -3584),
		new Location(158917, -78117, -3760),
		new Location(158989, -77130, -3720),
		new Location(158757, -75951, -3720),
		new Location(158157, -74161, -3592),
		new Location(157547, -73326, -3400),
		new Location(153815, -71497, -3392),
		new Location(153086, -70701, -3488),
		new Location(152262, -70352, -3568),
		new Location(155193, -69617, -3008),
		new Location(152262, -70352, -3568),
		new Location(153086, -70701, -3488),
		new Location(153815, -71497, -3392),
		new Location(157547, -73326, -3400),
		new Location(158157, -74161, -3592),
		new Location(158757, -75951, -3720),
		new Location(158989, -77130, -3720),
		new Location(158917, -78117, -3760),
		new Location(159678, -77671, -3584),
		new Location(161795, -75288, -3088),
		new Location(161876, -73407, -2984)
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
	 * Constructor for SuspiciousMerchantBorderland.
	 * @param actor NpcInstance
	 */
	public SuspiciousMerchantBorderland(NpcInstance actor)
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
					case 6:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 11:
						wait_timeout = System.currentTimeMillis() + 60000;
						wait = true;
						return true;
					case 16:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 22:
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
