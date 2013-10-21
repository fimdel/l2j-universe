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
public class SuspiciousMerchantShanty extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(-58672, 154703, -2688),
		new Location(-58672, 154703, -2688),
		new Location(-57522, 156523, -2576),
		new Location(-55226, 157117, -2064),
		new Location(-57528, 156515, -2576),
		new Location(-58660, 154706, -2688),
		new Location(-60174, 156182, -2832),
		new Location(-61834, 157703, -3264),
		new Location(-62761, 159101, -3584),
		new Location(-63472, 159672, -3680),
		new Location(-64072, 160631, -3760),
		new Location(-64387, 161877, -3792),
		new Location(-63842, 163092, -3840),
		new Location(-64397, 161831, -3792),
		new Location(-64055, 160587, -3760),
		new Location(-63461, 159656, -3680),
		new Location(-62744, 159095, -3584),
		new Location(-61831, 157693, -3256),
		new Location(-60152, 156167, -2824),
		new Location(-58652, 154707, -2688)
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
	 * Constructor for SuspiciousMerchantShanty.
	 * @param actor NpcInstance
	 */
	public SuspiciousMerchantShanty(NpcInstance actor)
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
					case 12:
						wait_timeout = System.currentTimeMillis() + 15000;
						Functions.npcSay(actor, "�? где дорога?");
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
					case 2:
						Functions.npcSay(actor, "�?ойду прогул�?�?�?�?...");
						break;
					case 12:
						Functions.npcSay(actor, "�?уда мир катит�?�?...");
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
