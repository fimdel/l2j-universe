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

public class _493_KickingOutUnwelcomeGuests extends Quest implements ScriptFile
{
	private static final int georgio = 33515;
	private static final int Resurrectedc = 23147;
	private static final int Insanec = 23148;
	private static final int Undeadc = 23149;
	private static final int Hellishc = 23150;
	private static final int sMessenger = 23151;
	private static final String Resurrectedc_item = "Resurrectedc";
	private static final String Insanec_item = "Insanec";
	private static final String Undeadc_item = "Undeadc";
	private static final String Hellishc_item = "Hellishc";
	private static final String sMessenger_item = "sMessenger";
	
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
	
	public _493_KickingOutUnwelcomeGuests()
	{
		super(false);
		addStartNpc(georgio);
		addTalkId(georgio);
		addKillNpcWithLog(1, Resurrectedc_item, 20, Resurrectedc);
		addKillNpcWithLog(1, Insanec_item, 20, Insanec);
		addKillNpcWithLog(1, Undeadc_item, 20, Undeadc);
		addKillNpcWithLog(1, Hellishc_item, 20, Hellishc);
		addKillNpcWithLog(1, sMessenger_item, 20, sMessenger);
		addLevelCheck(95, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "0-2.htm";
		}
		else if (event.equalsIgnoreCase("quest_rev"))
		{
			st.getPlayer().addExpAndSp(560000000, 16000000);
			st.exitCurrentQuest(this);
			st.playSound(SOUND_FINISH);
			htmltext = "0-3.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == georgio)
		{
			if (cond == 0)
			{
				if (isAvailableFor(st.getPlayer()))
				{
					if (st.isNowAvailableByTime())
					{
						htmltext = "start.htm";
					}
					else
					{
						htmltext = "0-c.htm";
					}
				}
				else
				{
					htmltext = "0-nc.htm";
				}
			}
			else if (cond == 1)
			{
				htmltext = "0-4.htm";
			}
			else if (cond == 2)
			{
				htmltext = "0-5.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		boolean doneKill = updateKill(npc, st);
		if (doneKill)
		{
			st.unset(Resurrectedc_item);
			st.unset(Insanec_item);
			st.unset(Undeadc_item);
			st.unset(Hellishc_item);
			st.unset(sMessenger_item);
			st.setCond(2);
		}
		return null;
	}
}
