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
public class Remy extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(-81926, 243894, -3712),
		new Location(-82134, 243600, -3728),
		new Location(-83165, 243987, -3728),
		new Location(-84501, 243245, -3728),
		new Location(-85100, 243285, -3728),
		new Location(-86152, 242898, -3728),
		new Location(-86288, 242962, -3720),
		new Location(-86348, 243223, -3720),
		new Location(-86522, 242762, -3720),
		new Location(-86500, 242615, -3728),
		new Location(-86123, 241606, -3728),
		new Location(-85167, 240589, -3728),
		new Location(-84323, 241245, -3728),
		new Location(-83215, 241170, -3728),
		new Location(-82364, 242944, -3728),
		new Location(-81674, 243391, -3712),
		new Location(-81926, 243894, -3712)
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
	 * Constructor for Remy.
	 * @param actor NpcInstance
	 */
	public Remy(NpcInstance actor)
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
					case 0:
						wait_timeout = System.currentTimeMillis() + 15000;
						Functions.npcSay(actor, "A delivery for Mr. Lector? Very good!");
						wait = true;
						return true;
					case 3:
						wait_timeout = System.currentTimeMillis() + 15000;
						Functions.npcSay(actor, "I need a break!");
						wait = true;
						return true;
					case 7:
						wait_timeout = System.currentTimeMillis() + 15000;
						Functions.npcSay(actor, "Hello, Mr. Lector! Long time no see, Mr. Jackson!");
						wait = true;
						return true;
					case 12:
						wait_timeout = System.currentTimeMillis() + 15000;
						Functions.npcSay(actor, "Lulu!");
						wait = true;
						return true;
					case 15:
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
			actor.setRunning();
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
