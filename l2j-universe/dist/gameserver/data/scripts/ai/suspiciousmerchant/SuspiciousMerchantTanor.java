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
public class SuspiciousMerchantTanor extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(58314, 136319, -2000),
		new Location(57078, 137124, -2216),
		new Location(54644, 137366, -2600),
		new Location(58696, 134202, -3096),
		new Location(60967, 134154, -3416),
		new Location(62813, 134744, -3592),
		new Location(65158, 135007, -3728),
		new Location(64278, 139384, -3176),
		new Location(63711, 140599, -2720),
		new Location(63187, 141192, -2440),
		new Location(62811, 142466, -2064),
		new Location(63187, 141192, -2440),
		new Location(63711, 140599, -2720),
		new Location(64278, 139384, -3176),
		new Location(65158, 135007, -3728),
		new Location(62813, 134744, -3592),
		new Location(60967, 134154, -3416),
		new Location(58696, 134202, -3096),
		new Location(54644, 137366, -2600),
		new Location(57078, 137124, -2216),
		new Location(58314, 136319, -2000)
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
	 * Constructor for SuspiciousMerchantTanor.
	 * @param actor NpcInstance
	 */
	public SuspiciousMerchantTanor(NpcInstance actor)
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
					case 7:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 10:
						wait_timeout = System.currentTimeMillis() + 60000;
						wait = true;
						return true;
					case 13:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 17:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 20:
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
