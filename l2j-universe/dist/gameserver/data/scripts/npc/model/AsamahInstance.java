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
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import quests._111_ElrokianHuntersProof;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AsamahInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field ElrokianTrap. (value is 8763)
	 */
	private static final int ElrokianTrap = 8763;
	/**
	 * Field TrapStone. (value is 8764)
	 */
	private static final int TrapStone = 8764;
	
	/**
	 * Constructor for AsamahInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public AsamahInstance(int objectId, NpcTemplate template)
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
		if (command.equals("buyTrap"))
		{
			String htmltext = null;
			QuestState ElrokianHuntersProof = player.getQuestState(_111_ElrokianHuntersProof.class);
			if ((player.getLevel() >= 75) && (ElrokianHuntersProof != null) && ElrokianHuntersProof.isCompleted() && (Functions.getItemCount(player, 57) > 1000000))
			{
				if (Functions.getItemCount(player, ElrokianTrap) > 0)
				{
					htmltext = getNpcId() + "-alreadyhave.htm";
				}
				else
				{
					Functions.removeItem(player, 57, 1000000);
					Functions.addItem(player, ElrokianTrap, 1);
					htmltext = getNpcId() + "-given.htm";
				}
			}
			else
			{
				htmltext = getNpcId() + "-cant.htm";
			}
			showChatWindow(player, "default/" + htmltext);
		}
		else if (command.equals("buyStones"))
		{
			String htmltext = null;
			QuestState ElrokianHuntersProof = player.getQuestState(_111_ElrokianHuntersProof.class);
			if ((player.getLevel() >= 75) && (ElrokianHuntersProof != null) && ElrokianHuntersProof.isCompleted() && (Functions.getItemCount(player, 57) > 1000000))
			{
				Functions.removeItem(player, 57, 1000000);
				Functions.addItem(player, TrapStone, 100);
				htmltext = getNpcId() + "-given.htm";
			}
			else
			{
				htmltext = getNpcId() + "-cant.htm";
			}
			showChatWindow(player, "default/" + htmltext);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
