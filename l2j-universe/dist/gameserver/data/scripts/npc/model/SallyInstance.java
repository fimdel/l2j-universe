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
package npc.model;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.templates.npc.NpcTemplate;
import quests._250_WatchWhatYouEat;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SallyInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for SallyInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public SallyInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		if (command.equals("ask_about_rare_plants"))
		{
			QuestState qs = player.getQuestState(_250_WatchWhatYouEat.class);
			if ((qs != null) && qs.isCompleted())
			{
				showChatWindow(player, 3);
			}
			else
			{
				showChatWindow(player, 2);
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
