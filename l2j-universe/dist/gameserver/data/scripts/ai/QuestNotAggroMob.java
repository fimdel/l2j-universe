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

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.QuestEventType;
import lineage2.gameserver.model.quest.QuestState;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class QuestNotAggroMob extends DefaultAI
{
	/**
	 * Constructor for QuestNotAggroMob.
	 * @param actor NpcInstance
	 */
	public QuestNotAggroMob(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	public boolean thinkActive()
	{
		return false;
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param dam int
	 */
	@Override
	public void onEvtAttacked(Creature attacker, int dam)
	{
		final NpcInstance actor = getActor();
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
	}
	
	/**
	 * Method onEvtAggression.
	 * @param attacker Creature
	 * @param d int
	 */
	@Override
	public void onEvtAggression(Creature attacker, int d)
	{
		// empty method
	}
}
