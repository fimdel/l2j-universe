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
public class GhostOfVonHellmannsPage extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(51462, -54539, -3176),
		new Location(51870, -54398, -3176),
		new Location(52164, -53964, -3176),
		new Location(52390, -53282, -3176),
		new Location(52058, -52071, -3104),
		new Location(52237, -51483, -3112),
		new Location(52024, -51262, -3096)
	};
	/**
	 * Field NPCtext.
	 */
	static final String[] NPCtext = new String[]
	{
		"Follow me...",
		"This where that here...",
		"I want to speak to you..."
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
	 * Constructor for GhostOfVonHellmannsPage.
	 * @param actor NpcInstance
	 */
	public GhostOfVonHellmannsPage(NpcInstance actor)
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
					case 6:
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
				actor.deleteMe();
				return false;
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
