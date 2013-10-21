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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _10303_CrossroadsBetweenLightAndDarkness extends Quest implements ScriptFile
{
	private static final int CON1 = 32909;
	private static final int CON2 = 33343;
	private static final int CON3 = 17747;
	private static final int[] CON4 =
	{
		13505,
		16108,
		16102,
		16105
	};
	private static final int[] CON5 =
	{
		16101,
		16100,
		16099,
		16098
	};
	
	public _10303_CrossroadsBetweenLightAndDarkness()
	{
		super(false);
		addTalkId(CON1, CON2);
		addQuestItem(CON3);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (st == null)
		{
			return event;
		}
		if (event.equalsIgnoreCase("32909-05.htm"))
		{
			st.dropItem(npc, CON4[Rnd.get(CON4.length)], Rnd.get(1, 100));
			st.addExpAndSp(6730155, 2847330);
			st.getPlayer().addAdena(465855);
			st.playSound("ItemSound.quest_finish");
			st.exitCurrentQuest(false);
		}
		else if (event.equalsIgnoreCase("33343-05.htm"))
		{
			st.dropItem(npc, CON5[Rnd.get(CON5.length)], Rnd.get(1, 100));
			st.addExpAndSp(6730155, 2847330);
			st.getPlayer().addAdena(465855);
			st.playSound("ItemSound.quest_finish");
			st.exitCurrentQuest(false);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		if (st == null)
		{
			return htmltext;
		}
		if (npc.getNpcId() == CON1)
		{
			switch (st.getState())
			{
				case 2:
					htmltext = "32909-02.htm";
					break;
				case 1:
					switch (st.getCond())
					{
						case 1:
							if (st.getPlayer().getLevel() < 90)
							{
								htmltext = "32909-03.htm";
							}
							else
							{
								htmltext = "32909-01.htm";
							}
					}
			}
		}
		else if (npc.getNpcId() == CON2)
		{
			switch (st.getState())
			{
				case 2:
					htmltext = "33343-02.htm";
					break;
				case 1:
					switch (st.getCond())
					{
						case 1:
							if (st.getPlayer().getLevel() < 90)
							{
								htmltext = "33343-03.htm";
							}
							else
							{
								htmltext = "33343-01.htm";
							}
					}
			}
		}
		return htmltext;
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
