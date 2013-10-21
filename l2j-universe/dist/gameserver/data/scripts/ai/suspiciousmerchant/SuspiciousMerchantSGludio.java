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
public class SuspiciousMerchantSGludio extends DefaultAI
{
	/**
	 * Field points.
	 */
	static final Location[] points =
	{
		new Location(-28169, 216864, -3544),
		new Location(-29028, 215089, -3672),
		new Location(-30888, 213455, -3656),
		new Location(-31937, 211656, -3656),
		new Location(-30880, 211006, -3552),
		new Location(-27690, 210004, -3272),
		new Location(-25784, 210108, -3272),
		new Location(-21682, 211459, -3272),
		new Location(-18430, 212927, -3704),
		new Location(-16247, 212795, -3664),
		new Location(-16868, 214267, -3648),
		new Location(-17263, 215887, -3552),
		new Location(-18352, 216841, -3504),
		new Location(-17263, 215887, -3552),
		new Location(-16868, 214267, -3648),
		new Location(-16247, 212795, -3664),
		new Location(-18430, 212927, -3704),
		new Location(-21682, 211459, -3272),
		new Location(-25784, 210108, -3272),
		new Location(-27690, 210004, -3272),
		new Location(-30880, 211006, -3552),
		new Location(-31937, 211656, -3656),
		new Location(-30888, 213455, -3656),
		new Location(-29028, 215089, -3672),
		new Location(-28169, 216864, -3544)
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
	 * Constructor for SuspiciousMerchantSGludio.
	 * @param actor NpcInstance
	 */
	public SuspiciousMerchantSGludio(NpcInstance actor)
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
					case 12:
						wait_timeout = System.currentTimeMillis() + 60000;
						wait = true;
						return true;
					case 24:
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
