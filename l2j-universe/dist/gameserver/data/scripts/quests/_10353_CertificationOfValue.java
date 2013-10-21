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

import gnu.trove.map.hash.TIntIntHashMap;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExQuestNpcLogList;
import lineage2.gameserver.scripts.ScriptFile;

public class _10353_CertificationOfValue extends Quest implements ScriptFile
{
	private static final int CON1 = 33155;
	private static final int CON2 = 33406;
	private static final int CON3 = 33358;
	private static final int CON4 = 17624;
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = "noquest";
		if (event.equalsIgnoreCase("33155-08.htm") || event.equalsIgnoreCase("33406-08.htm"))
		{
			st.getQuest();
		}
		else if (event.equalsIgnoreCase("33358-04.htm"))
		{
			st.setCond(2);
			st.playSound("ItemSound.quest_middle");
		}
		return htmltext;
	}
	
	public String onTalk(NpcInstance npc, Player player)
	{
		String htmltext = "noquest";
		QuestState st = player.getQuestState("10353_CertificationOfValue");
		if (st == null)
		{
			return htmltext;
		}
		if (npc.getNpcId() == CON1)
		{
			switch (st.getState())
			{
				case 0:
					if (player.getLevel() >= 48)
					{
						htmltext = "33155-01.htm";
					}
					else
					{
						htmltext = "33155-02.htm";
						st.exitCurrentQuest(true);
					}
					break;
				case 1:
					if (st.getCond() != 1)
					{
						break;
					}
					htmltext = "33155-08.htm";
					break;
				case 2:
					htmltext = "33155-03.htm";
			}
		}
		else if (npc.getNpcId() == CON2)
		{
			switch (st.getState())
			{
				case 0:
					if (player.getLevel() >= 48)
					{
						htmltext = "33406-01.htm";
					}
					else
					{
						htmltext = "33406-02.htm";
						st.exitCurrentQuest(true);
					}
					break;
				case 1:
					if (st.getCond() != 1)
					{
						break;
					}
					htmltext = "33406-08.htm";
					break;
				case 2:
					htmltext = "33406-03.htm";
			}
		}
		else if (npc.getNpcId() == CON3)
		{
			if (st.isStarted())
			{
				if (st.getCond() == 1)
				{
					htmltext = "33358-01.htm";
				}
				else if (st.getCond() == 2)
				{
					htmltext = "33358-05.htm";
				}
				else if (st.getCond() == 3)
				{
					htmltext = "33358-07.htm";
					st.addExpAndSp(3000000, 2500000);
					player.getInventory().getItemByItemId(CON4);
					st.exitCurrentQuest(false);
				}
			}
			else if (st.isCompleted())
			{
				htmltext = "33358-03.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((npc == null) || (st == null))
		{
			return null;
		}
		if (st.getCond() == 2)
		{
			TIntIntHashMap moblist = new TIntIntHashMap();
			int _1 = st.getInt("1");
			if (((npc.getNpcId() >= 23044) && (npc.getNpcId() <= 23068)) || ((npc.getNpcId() >= 23101) && (npc.getNpcId() <= 23112) && (_1 < 10) && Rnd.chance(10)))
			{
				_1++;
				st.set("1", String.valueOf(_1));
			}
			moblist.put(1033349, _1);
			st.getPlayer().sendPacket(new ExQuestNpcLogList(st));
			if (_1 >= 10)
			{
				st.setCond(3);
				st.playSound("ItemSound.quest_middle");
			}
		}
		return null;
	}
	
	public _10353_CertificationOfValue()
	{
		super(false);
		addStartNpc(CON1, CON2);
		addTalkId(33155, 33406, CON3);
		for (int i = 23044; i <= 23068; i++)
		{
			addKillId(i);
		}
		for (int i = 23101; i <= 23112; i++)
		{
			addKillId(i);
		}
		addLevelCheck(48, 99);
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
