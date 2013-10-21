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
package ai.hellbound;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.instancemanager.HellboundManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Chimera extends Fighter
{
	/**
	 * Constructor for Chimera.
	 * @param actor NpcInstance
	 */
	public Chimera(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		if (skill.getId() != 2359)
		{
			return;
		}
		final NpcInstance actor = getActor();
		if (!actor.isDead() && (actor.getCurrentHpPercents() > 10))
		{
			return;
		}
		switch (actor.getNpcId())
		{
			case 22353:
				actor.dropItem(caster.getPlayer(), 9682, 1);
				break;
			case 22349:
			case 22350:
			case 22351:
			case 22352:
				if (Rnd.chance(70))
				{
					if (Rnd.chance(30))
					{
						actor.dropItem(caster.getPlayer(), 9681, 1);
					}
					else
					{
						actor.dropItem(caster.getPlayer(), 9680, 1);
					}
				}
				break;
		}
		actor.doDie(null);
		actor.endDecayTask();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if (HellboundManager.getHellboundLevel() < 7)
		{
			attacker.teleToLocation(-11272, 236464, -3248);
			return;
		}
		super.onEvtAttacked(attacker, damage);
	}
}
