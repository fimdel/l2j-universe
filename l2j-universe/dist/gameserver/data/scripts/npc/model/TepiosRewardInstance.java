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

import instances.SufferingHallAttack;
import instances.SufferingHallDefence;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import quests._694_BreakThroughTheHallOfSuffering;
import quests._695_DefendtheHallofSuffering;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TepiosRewardInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field MARK_OF_KEUCEREUS_STAGE_1. (value is 13691)
	 */
	private static final int MARK_OF_KEUCEREUS_STAGE_1 = 13691;
	/**
	 * Field MARK_OF_KEUCEREUS_STAGE_2. (value is 13692)
	 */
	private static final int MARK_OF_KEUCEREUS_STAGE_2 = 13692;
	/**
	 * Field SOE. (value is 736)
	 */
	private static final int SOE = 736;
	/**
	 * Field SUPPLIES1. (value is 13777)
	 */
	private static final int SUPPLIES1 = 13777;
	/**
	 * Field SUPPLIES2. (value is 13778)
	 */
	private static final int SUPPLIES2 = 13778;
	/**
	 * Field SUPPLIES3. (value is 13779)
	 */
	private static final int SUPPLIES3 = 13779;
	/**
	 * Field SUPPLIES4. (value is 13780)
	 */
	private static final int SUPPLIES4 = 13780;
	/**
	 * Field SUPPLIES5. (value is 13781)
	 */
	private static final int SUPPLIES5 = 13781;
	/**
	 * Field SUPPLIES6_10.
	 */
	private static final int[] SUPPLIES6_10 =
	{
		13782,
		13783,
		13784,
		13785,
		13786
	};
	/**
	 * Field _gotReward.
	 */
	private boolean _gotReward = false;
	
	/**
	 * Constructor for TepiosRewardInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public TepiosRewardInstance(int objectId, NpcTemplate template)
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
		if (command.equalsIgnoreCase("getreward"))
		{
			if (_gotReward)
			{
				return;
			}
			if (player.isInParty() && (player.getParty().getPartyLeader() != player))
			{
				showChatWindow(player, 1);
				return;
			}
			int time = 0;
			if (getReflection().getInstancedZoneId() == 115)
			{
				time = ((SufferingHallAttack) getReflection()).timeSpent;
			}
			else if (getReflection().getInstancedZoneId() == 116)
			{
				time = ((SufferingHallDefence) getReflection()).timeSpent;
			}
			for (Player p : getReflection().getPlayers())
			{
				if ((ItemFunctions.getItemCount(p, MARK_OF_KEUCEREUS_STAGE_1) < 1) && (ItemFunctions.getItemCount(p, MARK_OF_KEUCEREUS_STAGE_2) < 1))
				{
					ItemFunctions.addItem(p, MARK_OF_KEUCEREUS_STAGE_1, 1, true);
				}
				ItemFunctions.addItem(p, SOE, 1, true);
				if (time > 0)
				{
					if (time <= ((20 * 60) + 59))
					{
						ItemFunctions.addItem(p, SUPPLIES1, 1, true);
					}
					else if ((time > ((20 * 60) + 59)) && (time <= ((22 * 60) + 59)))
					{
						ItemFunctions.addItem(p, SUPPLIES2, 1, true);
					}
					else if ((time > ((22 * 60) + 59)) && (time <= ((24 * 60) + 59)))
					{
						ItemFunctions.addItem(p, SUPPLIES3, 1, true);
					}
					else if ((time > ((24 * 60) + 59)) && (time <= ((26 * 60) + 59)))
					{
						ItemFunctions.addItem(p, SUPPLIES4, 1, true);
					}
					else if ((time > ((26 * 60) + 59)) && (time <= ((28 * 60) + 59)))
					{
						ItemFunctions.addItem(p, SUPPLIES5, 1, true);
					}
					else if (time > ((26 * 60) + 59))
					{
						ItemFunctions.addItem(p, SUPPLIES6_10[Rnd.get(SUPPLIES6_10.length)], 1, true);
					}
				}
				QuestState qs = p.getQuestState(_694_BreakThroughTheHallOfSuffering.class);
				QuestState qs2 = p.getQuestState(_695_DefendtheHallofSuffering.class);
				if ((qs != null) && (getReflection().getInstancedZoneId() == 115))
				{
					qs.exitCurrentQuest(true);
				}
				if ((qs2 != null) && (getReflection().getInstancedZoneId() == 116))
				{
					qs2.exitCurrentQuest(true);
				}
			}
			_gotReward = true;
			showChatWindow(player, 2);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		String htmlpath;
		if (val == 0)
		{
			if (_gotReward)
			{
				htmlpath = "default/32530-3.htm";
			}
			else
			{
				htmlpath = "default/32530.htm";
			}
		}
		else
		{
			return super.getHtmlPath(npcId, val, player);
		}
		return htmlpath;
	}
}
