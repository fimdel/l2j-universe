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
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PrisonGuard extends Fighter
{
	/**
	 * Field RACE_STAMP. (value is 10013)
	 */
	private static final int RACE_STAMP = 10013;
	
	/**
	 * Constructor for PrisonGuard.
	 * @param actor NpcInstance
	 */
	public PrisonGuard(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		final NpcInstance actor = getActor();
		if (actor.isDead() || (actor.getNpcId() == 18367))
		{
			return false;
		}
		if (target.getEffectList().getEffectsCountForSkill(Skill.SKILL_EVENT_TIMER) == 0)
		{
			return false;
		}
		return super.checkAggression(target);
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
		if (actor.isDead())
		{
			return;
		}
		if (attacker.isServitor() || attacker.isPet())
		{
			attacker = attacker.getPlayer();
		}
		if (attacker.getEffectList().getEffectsCountForSkill(Skill.SKILL_EVENT_TIMER) == 0)
		{
			if (actor.getNpcId() == 18367)
			{
				Functions.npcSay(actor, "It's not easy to obtain.");
			}
			else if (actor.getNpcId() == 18368)
			{
				Functions.npcSay(actor, "You're out of mind comming here...");
			}
			final Skill petrification = SkillTable.getInstance().getInfo(4578, 1);
			actor.doCast(petrification, attacker, true);
			for (Summon summon : attacker.getPlayer().getSummonList())
			{
				actor.doCast(petrification, summon, true);
			}
			return;
		}
		if (actor.getNpcId() == 18367)
		{
			notifyFriends(attacker, damage);
			return;
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		if (actor == null)
		{
			return;
		}
		if ((actor.getNpcId() == 18367) && (killer.getPlayer().getEffectList().getEffectsBySkillId(Skill.SKILL_EVENT_TIMER) != null))
		{
			Functions.addItem(killer.getPlayer(), RACE_STAMP, 1);
		}
		super.onEvtDead(killer);
	}
}
