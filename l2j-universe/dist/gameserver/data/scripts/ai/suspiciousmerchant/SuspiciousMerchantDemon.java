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
public class SuspiciousMerchantDemon extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(104150, -57163, -848),
		new Location(106218, -59401, -1344),
		new Location(106898, -59553, -1664),
		new Location(107352, -60168, -2000),
		new Location(107651, -61177, -2400),
		new Location(109094, -62678, -3248),
		new Location(108266, -62657, -3104),
		new Location(105169, -61226, -2616),
		new Location(102968, -59982, -2384),
		new Location(100070, -60173, -2792),
		new Location(98764, -61095, -2768),
		new Location(94946, -60039, -2432),
		new Location(96103, -59078, -1992),
		new Location(96884, -59043, -1656),
		new Location(97064, -57884, -1256),
		new Location(96884, -59043, -1656),
		new Location(96103, -59078, -1992),
		new Location(94946, -60039, -2432),
		new Location(98764, -61095, -2768),
		new Location(100070, -60173, -2792),
		new Location(102968, -59982, -2384),
		new Location(105169, -61226, -2616),
		new Location(108266, -62657, -3104),
		new Location(109094, -62678, -3248),
		new Location(107651, -61177, -2400),
		new Location(107352, -60168, -2000),
		new Location(106898, -59553, -1664),
		new Location(106218, -59401, -1344),
		new Location(104150, -57163, -848)
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
	 * Constructor for SuspiciousMerchantDemon.
	 * @param actor NpcInstance
	 */
	public SuspiciousMerchantDemon(NpcInstance actor)
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
					case 2:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 14:
						wait_timeout = System.currentTimeMillis() + 60000;
						wait = true;
						return true;
					case 26:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 28:
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
