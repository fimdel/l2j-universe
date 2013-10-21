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
package ai.seedofinfinity;

import instances.ErosionHallAttack;
import instances.ErosionHallDefence;
import instances.HeartInfinityAttack;
import instances.HeartInfinityDefence;
import instances.SufferingHallDefence;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SoulCoffin extends DefaultAI
{
	/**
	 * Field checkTimer.
	 */
	private long checkTimer = 0;
	
	/**
	 * Constructor for SoulCoffin.
	 * @param actor NpcInstance
	 */
	public SoulCoffin(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		final Reflection r = actor.getReflection();
		if (!r.isDefault())
		{
			if (actor.getNpcId() == 18711)
			{
				if (r.getInstancedZoneId() == 119)
				{
					((ErosionHallAttack) r).notifyCoffinDeath();
				}
				else if (r.getInstancedZoneId() == 121)
				{
					((HeartInfinityAttack) r).notifyCoffinDeath();
				}
				else if (r.getInstancedZoneId() == 120)
				{
					((ErosionHallDefence) r).notifyCoffinDeath();
				}
				else if (r.getInstancedZoneId() == 122)
				{
					((HeartInfinityDefence) r).notifyCoffinDeath();
				}
			}
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if ((actor.getNpcId() == 18706) && (actor.getReflection().getInstancedZoneId() == 116) && ((checkTimer + 10000) < System.currentTimeMillis()))
		{
			checkTimer = System.currentTimeMillis();
			((SufferingHallDefence) actor.getReflection()).notifyCoffinActivity();
		}
		return super.thinkActive();
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
