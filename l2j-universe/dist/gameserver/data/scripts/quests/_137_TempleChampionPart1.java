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

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _137_TempleChampionPart1 extends Quest implements ScriptFile
{
	private static final int SYLVAIN = 30070;
	private static final int FRAGMENT = 10340;
	private static final int BadgeTempleExecutor = 10334;
	private static final int BadgeTempleMissionary = 10339;
	private final static int GraniteGolem = 20083;
	private final static int HangmanTree = 20144;
	private final static int AmberBasilisk = 20199;
	private final static int Strain = 20200;
	private final static int Ghoul = 20201;
	private final static int DeadSeeker = 20202;
	
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
	
	public _137_TempleChampionPart1()
	{
		super(false);
		addStartNpc(SYLVAIN);
		addKillId(GraniteGolem, HangmanTree, AmberBasilisk, Strain, Ghoul, DeadSeeker);
		addQuestItem(FRAGMENT);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("sylvain_q0137_04.htm"))
		{
			st.takeItems(BadgeTempleExecutor, -1);
			st.takeItems(BadgeTempleMissionary, -1);
			st.setCond(1);
			st.setState(STARTED);
			st.set("talk", "0");
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("sylvain_q0137_08.htm"))
		{
			st.set("talk", "1");
		}
		else if (event.equalsIgnoreCase("sylvain_q0137_10.htm"))
		{
			st.set("talk", "2");
		}
		else if (event.equalsIgnoreCase("sylvain_q0137_13.htm"))
		{
			st.unset("talk");
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("sylvain_q0137_24.htm"))
		{
			st.giveItems(ADENA_ID, 69146);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == SYLVAIN)
		{
			if (cond == 0)
			{
				if ((st.getPlayer().getLevel() >= 35) && (st.getQuestItemsCount(BadgeTempleExecutor) > 0) && (st.getQuestItemsCount(BadgeTempleMissionary) > 0))
				{
					htmltext = "sylvain_q0137_01.htm";
				}
				else
				{
					htmltext = "sylvain_q0137_03.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				if (st.getInt("talk") == 0)
				{
					htmltext = "sylvain_q0137_05.htm";
				}
				else if (st.getInt("talk") == 1)
				{
					htmltext = "sylvain_q0137_08.htm";
				}
				else if (st.getInt("talk") == 2)
				{
					htmltext = "sylvain_q0137_10.htm";
				}
			}
			else if (cond == 2)
			{
				htmltext = "sylvain_q0137_13.htm";
			}
			else if ((cond == 3) && (st.getQuestItemsCount(FRAGMENT) >= 30))
			{
				htmltext = "sylvain_q0137_15.htm";
				st.set("talk", "1");
				st.takeItems(FRAGMENT, -1);
			}
			else if ((cond == 3) && (st.getInt("talk") == 1))
			{
				htmltext = "sylvain_q0137_16.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getCond() == 2)
		{
			if (st.getQuestItemsCount(FRAGMENT) < 30)
			{
				st.giveItems(FRAGMENT, 1);
				if (st.getQuestItemsCount(FRAGMENT) >= 30)
				{
					st.setCond(3);
					st.playSound(SOUND_MIDDLE);
				}
				else
				{
					st.playSound(SOUND_ITEMGET);
				}
			}
		}
		return null;
	}
}
