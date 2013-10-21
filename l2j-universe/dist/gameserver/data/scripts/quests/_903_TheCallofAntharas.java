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

public class _903_TheCallofAntharas extends Quest implements ScriptFile
{
	private static final int Theodric = 30755;
	private static final int BehemothDragonLeather = 21992;
	private static final int TaraskDragonsLeatherFragment = 21991;
	private static final int TaraskDragon = 29190;
	private static final int BehemothDragon = 29069;
	
	public _903_TheCallofAntharas()
	{
		super(PARTY_ALL);
		addStartNpc(Theodric);
		addKillId(TaraskDragon, BehemothDragon);
		addQuestItem(BehemothDragonLeather, TaraskDragonsLeatherFragment);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("theodric_q903_03.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("theodric_q903_06.htm"))
		{
			st.takeAllItems(BehemothDragonLeather);
			st.takeAllItems(TaraskDragonsLeatherFragment);
			st.giveItems(21897, 1);
			st.setState(COMPLETED);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(this);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if (npc.getNpcId() == Theodric)
		{
			switch (st.getState())
			{
				case CREATED:
					if (st.isNowAvailableByTime())
					{
						if (st.getPlayer().getLevel() >= 83)
						{
							if (st.getQuestItemsCount(3865) > 0)
							{
								htmltext = "theodric_q903_01.htm";
							}
							else
							{
								htmltext = "theodric_q903_00b.htm";
							}
						}
						else
						{
							htmltext = "theodric_q903_00.htm";
							st.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "theodric_q903_00a.htm";
					}
					break;
				case STARTED:
					if (cond == 1)
					{
						htmltext = "theodric_q903_04.htm";
					}
					else if (cond == 2)
					{
						htmltext = "theodric_q903_05.htm";
					}
					break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if (cond == 1)
		{
			switch (npc.getNpcId())
			{
				case TaraskDragon:
					if (st.getQuestItemsCount(TaraskDragonsLeatherFragment) < 1)
					{
						st.giveItems(TaraskDragonsLeatherFragment, 1);
					}
					break;
				case BehemothDragon:
					if (st.getQuestItemsCount(BehemothDragonLeather) < 1)
					{
						st.giveItems(BehemothDragonLeather, 1);
					}
					break;
				default:
					break;
			}
			if ((st.getQuestItemsCount(BehemothDragonLeather) > 0) && (st.getQuestItemsCount(TaraskDragonsLeatherFragment) > 0))
			{
				st.setCond(2);
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
