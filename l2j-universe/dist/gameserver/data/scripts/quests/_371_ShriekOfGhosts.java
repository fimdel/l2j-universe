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

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _371_ShriekOfGhosts extends Quest implements ScriptFile
{
	private static int REVA = 30867;
	private static int PATRIN = 30929;
	private static int Hallates_Warrior = 20818;
	private static int Hallates_Knight = 20820;
	private static int Hallates_Commander = 20824;
	private static int Ancient_Porcelain__Excellent = 6003;
	private static int Ancient_Porcelain__High_Quality = 6004;
	private static int Ancient_Porcelain__Low_Quality = 6005;
	private static int Ancient_Porcelain__Lowest_Quality = 6006;
	private static int Ancient_Ash_Urn = 5903;
	private static int Ancient_Porcelain = 6002;
	private static int Urn_Chance = 43;
	private static int Ancient_Porcelain__Excellent_Chance = 1;
	private static int Ancient_Porcelain__High_Quality_Chance = 14;
	private static int Ancient_Porcelain__Low_Quality_Chance = 46;
	private static int Ancient_Porcelain__Lowest_Quality_Chance = 84;
	private final Map<Integer, Integer> common_chances = new HashMap<>();
	
	public _371_ShriekOfGhosts()
	{
		super(true);
		addStartNpc(REVA);
		addTalkId(PATRIN);
		addKillId(Hallates_Warrior);
		addKillId(Hallates_Knight);
		addKillId(Hallates_Commander);
		addQuestItem(Ancient_Ash_Urn);
		common_chances.put(Hallates_Warrior, 71);
		common_chances.put(Hallates_Knight, 74);
		common_chances.put(Hallates_Commander, 82);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int _state = st.getState();
		if (event.equalsIgnoreCase("30867-03.htm") && (_state == CREATED))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30867-10.htm") && (_state == STARTED))
		{
			long Ancient_Ash_Urn_count = st.getQuestItemsCount(Ancient_Ash_Urn);
			if (Ancient_Ash_Urn_count > 0)
			{
				st.takeItems(Ancient_Ash_Urn, -1);
				st.giveItems(ADENA_ID, Ancient_Ash_Urn_count * 1000L);
			}
			st.exitCurrentQuest(true);
		}
		else if (event.equalsIgnoreCase("30867-TRADE") && (_state == STARTED))
		{
			long Ancient_Ash_Urn_count = st.getQuestItemsCount(Ancient_Ash_Urn);
			if (Ancient_Ash_Urn_count > 0)
			{
				htmltext = Ancient_Ash_Urn_count > 100 ? "30867-08.htm" : "30867-07.htm";
				int bonus = Ancient_Ash_Urn_count > 100 ? 17000 : 3000;
				st.takeItems(Ancient_Ash_Urn, -1);
				st.giveItems(ADENA_ID, bonus + (Ancient_Ash_Urn_count * 1000L));
			}
			else
			{
				htmltext = "30867-06.htm";
			}
		}
		else if (event.equalsIgnoreCase("30929-TRADE") && (_state == STARTED))
		{
			long Ancient_Porcelain_count = st.getQuestItemsCount(Ancient_Porcelain);
			if (Ancient_Porcelain_count > 0)
			{
				st.takeItems(Ancient_Porcelain, 1);
				int chance = Rnd.get(100);
				if (chance < Ancient_Porcelain__Excellent_Chance)
				{
					st.giveItems(Ancient_Porcelain__Excellent, 1);
					htmltext = "30929-03.htm";
				}
				else if (chance < Ancient_Porcelain__High_Quality_Chance)
				{
					st.giveItems(Ancient_Porcelain__High_Quality, 1);
					htmltext = "30929-04.htm";
				}
				else if (chance < Ancient_Porcelain__Low_Quality_Chance)
				{
					st.giveItems(Ancient_Porcelain__Low_Quality, 1);
					htmltext = "30929-05.htm";
				}
				else if (chance < Ancient_Porcelain__Lowest_Quality_Chance)
				{
					st.giveItems(Ancient_Porcelain__Lowest_Quality, 1);
					htmltext = "30929-06.htm";
				}
				else
				{
					htmltext = "30929-07.htm";
				}
			}
			else
			{
				htmltext = "30929-02.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int _state = st.getState();
		int npcId = npc.getNpcId();
		if (_state == CREATED)
		{
			if (npcId != REVA)
			{
				return htmltext;
			}
			if (st.getPlayer().getLevel() >= 59)
			{
				htmltext = "30867-02.htm";
				st.setCond(0);
			}
			else
			{
				htmltext = "30867-01.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if ((_state == STARTED) && (npcId == REVA))
		{
			htmltext = st.getQuestItemsCount(Ancient_Porcelain) > 0 ? "30867-05.htm" : "30867-04.htm";
		}
		else if ((_state == STARTED) && (npcId == PATRIN))
		{
			htmltext = "30929-01.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState qs)
	{
		Player player = qs.getRandomPartyMember(STARTED, Config.ALT_PARTY_DISTRIBUTION_RANGE);
		if (player == null)
		{
			return null;
		}
		QuestState st = player.getQuestState(qs.getQuest().getName());
		Integer _chance = common_chances.get(npc.getNpcId());
		if (_chance == null)
		{
			return null;
		}
		if (Rnd.chance(_chance))
		{
			st.giveItems(Rnd.chance(Urn_Chance) ? Ancient_Ash_Urn : Ancient_Porcelain, 1);
			st.playSound(SOUND_ITEMGET);
		}
		return null;
	}
	
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
}
