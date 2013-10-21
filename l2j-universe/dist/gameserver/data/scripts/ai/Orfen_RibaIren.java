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
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Orfen_RibaIren extends Fighter
{
	/**
	 * Field Orfen_id. (value is 29014)
	 */
	private static final int Orfen_id = 29014;
	
	/**
	 * Constructor for Orfen_RibaIren.
	 * @param actor NpcInstance
	 */
	public Orfen_RibaIren(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method createNewTask.
	 * @return boolean
	 */
	@Override
	protected boolean createNewTask()
	{
		return defaultNewTask();
	}
	
	/**
	 * Method onEvtClanAttacked.
	 * @param attacked_member Creature
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtClanAttacked(Creature attacked_member, Creature attacker, int damage)
	{
		super.onEvtClanAttacked(attacked_member, attacker, damage);
		final NpcInstance actor = getActor();
		if (_healSkills.length == 0)
		{
			return;
		}
		if (attacked_member.isDead() || actor.isDead() || (attacked_member.getCurrentHpPercents() > 50))
		{
			return;
		}
		int heal_chance = 0;
		if (attacked_member.getNpcId() == actor.getNpcId())
		{
			heal_chance = (attacked_member.getObjectId() == actor.getObjectId()) ? 100 : 0;
		}
		else
		{
			heal_chance = (attacked_member.getNpcId() == Orfen_id) ? 90 : 10;
		}
		if (Rnd.chance(heal_chance) && canUseSkill(_healSkills[0], attacked_member, -1))
		{
			addTaskAttack(attacked_member, _healSkills[0], 1000000);
		}
	}
}
