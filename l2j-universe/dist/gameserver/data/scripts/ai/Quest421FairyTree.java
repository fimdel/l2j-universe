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

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Quest421FairyTree extends Fighter
{
	/**
	 * Constructor for Quest421FairyTree.
	 * @param actor NpcInstance
	 */
	public Quest421FairyTree(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		if ((attacker != null) && attacker.isPlayer())
		{
			final Skill skill = SkillTable.getInstance().getInfo(5423, 12);
			skill.getEffects(actor, attacker, false, false);
			return;
		}
		if (attacker.isPet())
		{
			super.onEvtAttacked(attacker, damage);
			return;
		}
	}
	
	/**
	 * Method onEvtAggression.
	 * @param attacker Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature attacker, int aggro)
	{
		final NpcInstance actor = getActor();
		if ((attacker != null) && attacker.isPlayer())
		{
			final Skill skill = SkillTable.getInstance().getInfo(5423, 12);
			skill.getEffects(actor, attacker, false, false);
			return;
		}
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
