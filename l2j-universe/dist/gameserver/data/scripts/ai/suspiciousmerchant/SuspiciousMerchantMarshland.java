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
public class SuspiciousMerchantMarshland extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(71436, -58182, -2904),
		new Location(71731, -56949, -3080),
		new Location(72715, -56729, -3104),
		new Location(73277, -56055, -3104),
		new Location(73369, -55636, -3104),
		new Location(74136, -54646, -3104),
		new Location(73408, -54422, -3104),
		new Location(72998, -53404, -3136),
		new Location(71661, -52937, -3104),
		new Location(71127, -52304, -3104),
		new Location(70225, -52304, -3064),
		new Location(69668, -52780, -3064),
		new Location(68422, -52407, -3240),
		new Location(67702, -52940, -3208),
		new Location(67798, -52940, -3232),
		new Location(66667, -55841, -2840),
		new Location(67798, -52940, -3232),
		new Location(67702, -52940, -3208),
		new Location(68422, -52407, -3240),
		new Location(69668, -52780, -3064),
		new Location(70225, -52304, -3064),
		new Location(71127, -52304, -3104),
		new Location(71661, -52937, -3104),
		new Location(72998, -53404, -3136),
		new Location(73408, -54422, -3104),
		new Location(74136, -54646, -3104),
		new Location(73369, -55636, -3104),
		new Location(73277, -56055, -3104),
		new Location(72715, -56729, -3104),
		new Location(71731, -56949, -3080),
		new Location(71436, -58182, -2904)
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
	 * Constructor for SuspiciousMerchantMarshland.
	 * @param actor NpcInstance
	 */
	public SuspiciousMerchantMarshland(NpcInstance actor)
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
					case 3:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 15:
						wait_timeout = System.currentTimeMillis() + 60000;
						wait = true;
						return true;
					case 27:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 30:
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
