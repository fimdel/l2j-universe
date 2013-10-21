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
public class Kasiel extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(43932, 51096, -2992),
		new Location(43304, 50364, -2992),
		new Location(43041, 49312, -2992),
		new Location(43612, 48322, -2992),
		new Location(44009, 47645, -2992),
		new Location(45309, 47341, -2992),
		new Location(46726, 47762, -2992),
		new Location(47509, 49004, -2992),
		new Location(47443, 50456, -2992),
		new Location(47013, 51287, -2992),
		new Location(46380, 51254, -2900),
		new Location(46389, 51584, -2800),
		new Location(46009, 51593, -2800),
		new Location(46027, 52156, -2800),
		new Location(44692, 52141, -2800),
		new Location(44692, 51595, -2800),
		new Location(44346, 51564, -2850),
		new Location(44357, 51259, -2900),
		new Location(44111, 51252, -2992)
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
	 * Constructor for Kasiel.
	 * @param actor NpcInstance
	 */
	public Kasiel(NpcInstance actor)
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
					case 5:
						wait_timeout = System.currentTimeMillis() + 15000;
						Functions.npcSay(actor, "The Mother Tree is always so gorgeous!");
						wait = true;
						return true;
					case 9:
						wait_timeout = System.currentTimeMillis() + 60000;
						Functions.npcSay(actor, "Lady Mirabel, may the peace of the lake be with you!");
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
