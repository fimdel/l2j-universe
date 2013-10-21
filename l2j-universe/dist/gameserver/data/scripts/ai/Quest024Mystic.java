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

import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.instancemanager.QuestManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import quests._024_InhabitantsOfTheForestOfTheDead;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Quest024Mystic extends Mystic
{
	/**
	 * Constructor for Quest024Mystic.
	 * @param actor NpcInstance
	 */
	public Quest024Mystic(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final Quest q = QuestManager.getQuest(_024_InhabitantsOfTheForestOfTheDead.class);
		if (q != null)
		{
			for (Player player : World.getAroundPlayers(getActor(), 300, 200))
			{
				QuestState questState = player.getQuestState(_024_InhabitantsOfTheForestOfTheDead.class);
				if ((questState != null) && (questState.getCond() == 3))
				{
					q.notifyEvent("seePlayer", questState, getActor());
				}
			}
		}
		return super.thinkActive();
	}
}
