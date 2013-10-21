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
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SuspiciousMerchantAntharas extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(74810, 90814, -3344),
		new Location(75094, 92951, -3104),
		new Location(75486, 92906, -3072),
		new Location(75765, 91794, -2912),
		new Location(77116, 90455, -2896),
		new Location(77743, 89119, -2896),
		new Location(77118, 90457, -2896),
		new Location(75750, 91811, -2912),
		new Location(75479, 92904, -3072),
		new Location(75094, 92943, -3104),
		new Location(74809, 90794, -3344),
		new Location(76932, 88297, -3296),
		new Location(77882, 87441, -3408),
		new Location(78257, 85859, -3632),
		new Location(80994, 85866, -3472),
		new Location(82676, 87519, -3360),
		new Location(83778, 88414, -3376),
		new Location(83504, 90378, -3120),
		new Location(84431, 90379, -3264),
		new Location(85453, 90117, -3312),
		new Location(85605, 89708, -3296),
		new Location(84894, 88975, -3344),
		new Location(83735, 88382, -3376),
		new Location(82616, 87485, -3360),
		new Location(80971, 85855, -3472),
		new Location(78247, 85853, -3632),
		new Location(77868, 87463, -3408),
		new Location(76916, 88304, -3280),
		new Location(75494, 89865, -3200)
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
	 * Constructor for SuspiciousMerchantAntharas.
	 * @param actor NpcInstance
	 */
	public SuspiciousMerchantAntharas(NpcInstance actor)
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
					case 5:
						wait_timeout = System.currentTimeMillis() + 15000;
						switch (Rnd.get(4))
						{
							case 0:
								Functions.npcSay(actor, "�?ак погода?");
								break;
							case 1:
								Functions.npcSay(actor, "�?ак жизн�??");
								break;
							case 2:
								Functions.npcSay(actor, "�?огода �?егодн�? хоро�?а�?.");
								break;
							case 3:
								Functions.npcSay(actor, "�? у ва�? крепкие ворота?");
								break;
						}
						wait = true;
						return true;
				}
			}
			else
			{
				switch (current_point)
				{
					case 0:
						Functions.npcSay(actor, "�?адо разведат�? об�?тановку...");
						break;
					case 5:
						Functions.npcSay(actor, "�?ойду прогул�?�?�?�?...");
						break;
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
			doTask()/**
			 * Method onEvtAttacked.
			 * @param attacker Creature
			 * @param damage int
			 */
			;
			return true;
		}
		if (randomAnimation())
		{
			return true;
		}
		return /**
		 * Method onEvtAggression.
		 * @param target Creature
		 * @param aggro int
		 */
		false;
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		// empty method
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		// empty method
	}
}
