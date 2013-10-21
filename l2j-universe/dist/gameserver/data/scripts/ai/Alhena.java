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
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Alhena extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(10968, 14620, -4248),
		new Location(11308, 15847, -4584),
		new Location(12119, 16441, -4584),
		new Location(15104, 15661, -4376),
		new Location(15265, 16288, -4376),
		new Location(12292, 16934, -4584),
		new Location(11777, 17669, -4584),
		new Location(11229, 17650, -4576),
		new Location(10641, 17282, -4584),
		new Location(7683, 18034, -4376),
		new Location(10551, 16775, -4584),
		new Location(11004, 15942, -4584),
		new Location(10827, 14757, -4248),
		new Location(10968, 14620, -4248)
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
	 * Constructor for Alhena.
	 * @param actor NpcInstance
	 */
	public Alhena(NpcInstance actor)
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
			if (!wait)
			{
				switch (current_point)
				{
					case 3:
						wait_timeout = System.currentTimeMillis() + 15000;
						wait = true;
						return true;
					case 4:
						wait_timeout = System.currentTimeMillis() + 15000;
						Functions.npcSay(actor, "You're a hard worker, Rayla!");
						wait = true;
						return true;
					case 9:
						wait_timeout = System.currentTimeMillis() + 15000;
						Functions.npcSay(actor, "You're a hard worker!");
						wait = true;
						return true;
					case 12:
						wait_timeout = System.currentTimeMillis() + 60000;
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
