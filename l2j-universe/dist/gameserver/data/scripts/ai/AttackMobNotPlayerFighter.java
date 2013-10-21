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

import java.util.List;

import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.QuestEventType;
import lineage2.gameserver.model.quest.QuestState;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AttackMobNotPlayerFighter extends Fighter
{
	/**
	 * Constructor for AttackMobNotPlayerFighter.
	 * @param actor NpcInstance
	 */
	public AttackMobNotPlayerFighter(NpcInstance actor)
	{
		super(actor);
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
		if (attacker == null)
		{
			return;
		}
		final Player player = attacker.getPlayer();
		if (player != null)
		{
			final List<QuestState> quests = player.getQuestsForEvent(actor, QuestEventType.ATTACKED_WITH_QUEST, false);
			if (quests != null)
			{
				for (QuestState qs : quests)
				{
					qs.getQuest().notifyAttack(actor, qs);
				}
			}
		}
		onEvtAggression(attacker, damage);
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
		if (attacker == null)
		{
			return;
		}
		if (!actor.isRunning())
		{
			startRunningTask(AI_TASK_ATTACK_DELAY);
		}
		if (getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
		{
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
		}
	}
}
