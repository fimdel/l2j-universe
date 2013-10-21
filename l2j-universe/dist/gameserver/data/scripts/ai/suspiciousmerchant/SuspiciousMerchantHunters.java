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
public class SuspiciousMerchantHunters extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(121072, 93215, -2736),
		new Location(122718, 92355, -2320),
		new Location(126171, 91910, -2216),
		new Location(126353, 90422, -2296),
		new Location(125796, 87720, -2432),
		new Location(124803, 85970, -2464),
		new Location(125036, 83836, -2376),
		new Location(128886, 83331, -1416),
		new Location(129697, 84969, -1256),
		new Location(126291, 86712, -2240),
		new Location(126599, 88950, -2325),
		new Location(126847, 90713, -2264),
		new Location(126599, 88950, -2325),
		new Location(126291, 86712, -2240),
		new Location(129697, 84969, -1256),
		new Location(128886, 83331, -1416),
		new Location(125036, 83836, -2376),
		new Location(124803, 85970, -2464),
		new Location(125796, 87720, -2432),
		new Location(126353, 90422, -2296),
		new Location(126171, 91910, -2216),
		new Location(122718, 92355, -2320),
		new Location(121072, 93215, -2736)
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
	 * Constructor for SuspiciousMerchantHunters.
	 * @param actor NpcInstance
	 */
	public SuspiciousMerchantHunters(NpcInstance actor)
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
