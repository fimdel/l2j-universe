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

public class _10375_SuccubusDisciples extends Quest implements ScriptFile
{
	private static final int NPC_ZENYA = 32140;
	private static final int NightmareDeath = 23191;
	private static final int NightmareofDarkness = 23192;
	private static final int NightmareofMadness = 23197;
	private static final int NightmareofSilence = 23198;
	public static final String Nightmare_Death = "NightmareDeath";
	public static final String Nightmare_of_Darkness = "NightmareofDarkness";
	public static final String Nightmare_of_Madness = "NightmareofMadness";
	public static final String Nightmare_of_Silence = "NightmareofSilence";
	
	public _10375_SuccubusDisciples()
	{
		super(false);
		addStartNpc(NPC_ZENYA);
		addKillNpcWithLog(1, Nightmare_Death, 5, NightmareDeath);
		addKillNpcWithLog(1, Nightmare_of_Darkness, 5, NightmareofDarkness);
		addKillNpcWithLog(3, Nightmare_of_Madness, 5, NightmareofMadness);
		addKillNpcWithLog(3, Nightmare_of_Silence, 5, NightmareofSilence);
		addLevelCheck(80, 99);
		addQuestCompletedCheck(_10374_ThatPlaceSuccubus.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("32140-06.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		if (event.equalsIgnoreCase("32140-09.htm"))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == NPC_ZENYA)
		{
			switch (st.getState())
			{
				case COMPLETED:
					htmltext = "32140-05.htm";
					break;
				case CREATED:
					if (st.getPlayer().getLevel() >= 80)
					{
						QuestState qs = st.getPlayer().getQuestState(_10374_ThatPlaceSuccubus.class);
						if ((st.getPlayer().getClassId().level() == 4) && (qs != null) && qs.isCompleted())
						{
							htmltext = "32140-01.htm";
						}
						else
						{
							htmltext = "32140-03.htm";
							st.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "32140-04.htm";
					}
					break;
				case STARTED:
					if (st.getCond() == 1)
					{
						htmltext = "32140-07.htm";
					}
					else if (st.getCond() == 2)
					{
						htmltext = "32140-08.htm";
					}
					else if (st.getCond() == 3)
					{
						htmltext = "32140-10.htm";
					}
					else if (st.getCond() == 4)
					{
						htmltext = "32140-11.htm";
						st.giveItems(57, 498700L);
						st.addExpAndSp(24782300, 28102300);
						st.exitCurrentQuest(false);
						st.playSound(SOUND_FINISH);
					}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (!((st.getCond() == 1) || (st.getCond() == 3)))
		{
			return null;
		}
		if (updateKill(npc, st))
		{
			if (st.getCond() == 1)
			{
				st.unset(Nightmare_Death);
				st.unset(Nightmare_of_Darkness);
				st.setCond(2);
				st.playSound(SOUND_MIDDLE);
			}
			else if (st.getCond() == 3)
			{
				st.unset(Nightmare_of_Madness);
				st.unset(Nightmare_of_Silence);
				st.setCond(4);
				st.playSound(SOUND_MIDDLE);
			}
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
