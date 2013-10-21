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

import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _149_PrimalMotherIstina extends Quest implements ScriptFile
{
	private static final int _Rumiese = 33293;
	private static final int _Istina = 29195;
	private static final int _ShilensMark = 17589;
	private static final int _IstinasBracelet = 19455;
	
	public _149_PrimalMotherIstina()
	{
		super(false);
		addStartNpc(_Rumiese);
		addTalkId(_Rumiese);
		addKillId(_Istina);
		addQuestItem(_ShilensMark);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("33293-06.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		String htmlText = NO_QUEST_DIALOG;
		int cond = st.getCond();
		if (cond == 0)
		{
			if (player.getLevel() < 90)
			{
				htmlText = "33293-02.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmlText = "33293-01.htm";
			}
		}
		if (cond == 1)
		{
			htmlText = "33293-07.htm";
		}
		else if ((cond == 2) || (st.getQuestItemsCount(_ShilensMark) >= 1))
		{
			htmlText = "33293-08.htm";
			st.addExpAndSp(833065000, 368800464);
			st.giveItems(_IstinasBracelet, 1);
			st.setState(COMPLETED);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmlText;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		Party party = st.getPlayer().getParty();
		if (cond == 1)
		{
			if (npc.getNpcId() == _Istina)
			{
				if (party == null)
				{
					st.setCond(2);
					st.giveItems(_ShilensMark, 1);
					st.playSound(SOUND_MIDDLE);
				}
				else
				{
					for (Player pmember : party.getPartyMembers())
					{
						QuestState pst = pmember.getQuestState(_149_PrimalMotherIstina.class);
						if ((pst != null) && (pst.getCond() == 1))
						{
							pst.setCond(2);
							pst.giveItems(_ShilensMark, 1);
							pst.playSound("SOUND_MIDDLE");
						}
					}
				}
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
