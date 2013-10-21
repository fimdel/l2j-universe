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
package quests;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _690_JudesRequest extends Quest implements ScriptFile
{
	private static int JUDE = 32356;
	private static int EVIL_WEAPON = 10327;
	private static int Evil = 22399;
	private static int EVIL_WEAPON_CHANCE = 30;
	private static int ISawsword = 10373;
	private static int IDisperser = 10374;
	private static int ISpirit = 10375;
	private static int IHeavyArms = 10376;
	private static int ITrident = 10377;
	private static int IHammer = 10378;
	private static int IHand = 10379;
	private static int IHall = 10380;
	private static int ISpitter = 10381;
	private static int ISawswordP = 10397;
	private static int IDisperserP = 10398;
	private static int ISpiritP = 10399;
	private static int IHeavyArmsP = 10400;
	private static int ITridentP = 10401;
	private static int IHammerP = 10402;
	private static int IHandP = 10403;
	private static int IHallP = 10404;
	private static int ISpitterP = 10405;
	
	@Override
	public void onLoad()
	{
	}
	
	@Override
	public void onReload()
	{
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	public _690_JudesRequest()
	{
		super(true);
		addStartNpc(JUDE);
		addTalkId(JUDE);
		addKillId(Evil);
		addQuestItem(EVIL_WEAPON);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("jude_q0690_03.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return htmltext;
	}
	
	private void giveReward(QuestState st, int item_id, long count)
	{
		st.giveItems(item_id, count);
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if (cond == 0)
		{
			if (st.getPlayer().getLevel() >= 78)
			{
				htmltext = "jude_q0690_01.htm";
			}
			else
			{
				htmltext = "jude_q0690_02.htm";
			}
			st.exitCurrentQuest(true);
		}
		else if ((cond == 1) && (st.getQuestItemsCount(EVIL_WEAPON) >= 5))
		{
			int reward = Rnd.get(8);
			if (st.getQuestItemsCount(EVIL_WEAPON) >= 100)
			{
				if (reward == 0)
				{
					giveReward(st, ISawsword, 1);
				}
				else if (reward == 1)
				{
					giveReward(st, IDisperser, 1);
				}
				else if (reward == 2)
				{
					giveReward(st, ISpirit, 1);
				}
				else if (reward == 3)
				{
					giveReward(st, IHeavyArms, 1);
				}
				else if (reward == 4)
				{
					giveReward(st, ITrident, 1);
				}
				else if (reward == 5)
				{
					giveReward(st, IHammer, 1);
				}
				else if (reward == 6)
				{
					giveReward(st, IHand, 1);
				}
				else if (reward == 7)
				{
					giveReward(st, IHall, 1);
				}
				else if (reward == 8)
				{
					giveReward(st, ISpitter, 1);
				}
				st.playSound(SOUND_FINISH);
				st.takeItems(EVIL_WEAPON, 100);
				htmltext = "jude_q0690_07.htm";
			}
			else if ((st.getQuestItemsCount(EVIL_WEAPON) > 0) && (st.getQuestItemsCount(EVIL_WEAPON) < 100))
			{
				if (reward == 0)
				{
					st.giveItems(ISawswordP, 1);
				}
				else if (reward == 1)
				{
					st.giveItems(IDisperserP, 1);
				}
				else if (reward == 2)
				{
					st.giveItems(ISpiritP, 1);
				}
				else if (reward == 3)
				{
					st.giveItems(IHeavyArmsP, 1);
				}
				else if (reward == 4)
				{
					st.giveItems(ITridentP, 1);
				}
				else if (reward == 5)
				{
					st.giveItems(IHammerP, 1);
				}
				else if (reward == 6)
				{
					st.giveItems(IHandP, 1);
				}
				else if (reward == 7)
				{
					st.giveItems(IHallP, 1);
				}
				else if (reward == 8)
				{
					st.giveItems(ISpitterP, 1);
				}
				st.playSound(SOUND_FINISH);
				st.takeItems(EVIL_WEAPON, 5);
				htmltext = "jude_q0690_09.htm";
			}
		}
		else
		{
			htmltext = "jude_q0690_10.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		Player player = st.getRandomPartyMember(STARTED, Config.ALT_PARTY_DISTRIBUTION_RANGE);
		if (st.getState() != STARTED)
		{
			return null;
		}
		if (player != null)
		{
			QuestState sts = player.getQuestState(st.getQuest().getName());
			if ((sts != null) && Rnd.chance(EVIL_WEAPON_CHANCE))
			{
				st.giveItems(EVIL_WEAPON, 1);
				st.playSound(SOUND_ITEMGET);
			}
		}
		return null;
	}
}
