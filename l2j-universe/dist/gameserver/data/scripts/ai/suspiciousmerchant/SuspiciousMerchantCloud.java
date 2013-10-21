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
public class SuspiciousMerchantCloud extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(-56032, 86017, -3259),
		new Location(-57329, 86006, -3640),
		new Location(-57470, 85306, -3664),
		new Location(-58892, 85159, -3768),
		new Location(-59030, 80150, -3632),
		new Location(-57642, 77591, -3512),
		new Location(-53971, 77664, -3224),
		new Location(-53271, 85126, -3552),
		new Location(-53971, 77664, -3224),
		new Location(-57642, 77591, -3512),
		new Location(-59030, 80150, -3632),
		new Location(-58892, 85159, -3768),
		new Location(-57470, 85306, -3664),
		new Location(-57329, 86006, -3640),
		new Location(-56032, 86017, -3259)
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
	 * Constructor for SuspiciousMerchantCloud.
	 * @param actor NpcInstance
	 */
	public SuspiciousMerchantCloud(NpcInstance actor)
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
					case 5:
						wait_timeout = System.currentTimeMillis() + 20000;
						wait = true;
						return true;
					case 6:
						wait_timeout = System.currentTimeMillis() + 40000;
						wait = true;
						return true;
					case 7:
						wait_timeout = System.currentTimeMillis() + 60000;
						wait = true;
						return true;
					case 8:
						wait_timeout = System.currentTimeMillis() + 40000;
						wait = true;
						return true;
					case 9:
						wait_timeout = System.currentTimeMillis() + 20000;
						wait = true;
						return true;
					case 11:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
					case 14:
						wait_timeout = System.currentTimeMillis() + 30000;
						wait = true;
						return true;
				}
			}
			wait_timeout = 0;
			wait = false;
			if (current_point >= (points.length - 1))
			{
				current_point = -1;
			}
			current_point++;
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
