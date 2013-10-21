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
public class SuspiciousMerchantNarsell extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(159377, 52403, -3312),
		new Location(161177, 54083, -3560),
		new Location(162152, 54365, -3632),
		new Location(162703, 55840, -3696),
		new Location(162370, 58534, -3504),
		new Location(160099, 60034, -3224),
		new Location(158048, 62696, -3464),
		new Location(157220, 63450, -3520),
		new Location(155076, 63731, -3544),
		new Location(153893, 64441, -3656),
		new Location(153085, 62948, -3680),
		new Location(150866, 58737, -3432),
		new Location(153085, 62948, -3680),
		new Location(153893, 64441, -3656),
		new Location(155076, 63731, -3544),
		new Location(157220, 63450, -3520),
		new Location(158048, 62696, -3464),
		new Location(160099, 60034, -3224),
		new Location(162370, 58534, -3504),
		new Location(162703, 55840, -3696),
		new Location(162152, 54365, -3632),
		new Location(161177, 54083, -3560),
		new Location(159377, 52403, -3312)
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
	 * Constructor for SuspiciousMerchantNarsell.
	 * @param actor NpcInstance
	 */
	public SuspiciousMerchantNarsell(NpcInstance actor)
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
					case 12:
						wait_timeout = System.currentTimeMillis() + 60000;
						wait = true;
						return true;
					case 21:
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
