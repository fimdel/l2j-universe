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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExQuestNpcLogList;
import lineage2.gameserver.scripts.ScriptFile;

public class _472_ChallengeSteamCorridor extends Quest implements ScriptFile
{
	private static final int CON1 = 33044;
	private static final int CON2 = 25797;
	private static final int CON3 = 30387;
	
	public _472_ChallengeSteamCorridor()
	{
		super(false);
		addStartNpc(CON1);
		addTalkId(CON1);
		addKillId(CON2);
		addLevelCheck(97, 99);
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
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		String str = event;
		int i = -1;
		switch (str.hashCode())
		{
			case -659506668:
				if (!str.equals("33044-04.htm"))
				{
					break;
				}
				i = 0;
				break;
			case -656736105:
				if (!str.equals("33044-07.htm"))
				{
					break;
				}
				i = 1;
		}
		switch (i)
		{
			case 0:
				st.setState(STARTED);
				break;
			case 1:
				st.playSound(SOUND_FINISH);
				st.giveItems(CON3, 10);
				st.exitCurrentQuest(false);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == CON1)
		{
			if (player.getLevel() < 97)
			{
				st.exitCurrentQuest(true);
				return "33044-02.htm";
			}
			if (st.getState() == CREATED)
			{
				htmltext = "33044-01.htm";
			}
			else if (st.getState() == STARTED)
			{
				if (cond == 1)
				{
					htmltext = "33044-05.htm";
				}
				else
				{
					htmltext = "33044-06.htm";
				}
			}
			else if (st.getState() == COMPLETED)
			{
				htmltext = "33044-08.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		npc.getNpcId();
		st.getCond();
		Player player = st.getPlayer();
		if ((npc.getNpcId() == CON2) && (st.getCond() == 1))
		{
			TIntIntHashMap moblist = new TIntIntHashMap();
			moblist.put(CON2, 1);
			if (player.getParty() != null)
			{
				for (Player partyMember : player.getParty().getPartyMembers())
				{
					QuestState pst = partyMember.getQuestState("_472_ChallengeSteamCorridor");
					if ((pst != null) && (pst.isStarted()))
					{
						pst.setCond(2);
						pst.playSound(SOUND_MIDDLE);
						partyMember.sendPacket(new ExQuestNpcLogList(st));
					}
				}
			}
			else
			{
				st.setCond(2);
				st.playSound(SOUND_MIDDLE);
				player.sendPacket(new ExQuestNpcLogList(st));
			}
		}
		return null;
	}
	
	public boolean isDailyQuest()
	{
		return true;
	}
}
