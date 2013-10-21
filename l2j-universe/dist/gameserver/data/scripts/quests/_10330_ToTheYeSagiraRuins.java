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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.ScriptFile;

public class _10330_ToTheYeSagiraRuins extends Quest implements ScriptFile
{
	private static final int atran = 33448;
	private static final int lakis = 32977;
	
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
	
	public _10330_ToTheYeSagiraRuins()
	{
		super(false);
		addStartNpc(atran);
		addTalkId(atran);
		addTalkId(lakis);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(_10329_BackupSeekers.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Player player = st.getPlayer();
		if (event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "0-4.htm";
		}
		if (event.equalsIgnoreCase("qet_rev"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.ARMOR_HAS_BEEN_ADDED_TO_YOUR_INVENTORY, 4500, ScreenMessageAlign.TOP_CENTER));
			htmltext = "1-3.htm";
			st.getPlayer().addExpAndSp(23000, 25000);
			st.giveItems(57, 62000);
			st.giveItems(29, 1);
			st.giveItems(22, 1);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == atran)
		{
			if (st.isCompleted())
			{
				htmltext = "0-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()))
			{
				htmltext = "start.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-4.htm";
			}
			else
			{
				htmltext = "0-nc.htm";
			}
		}
		if (npcId == lakis)
		{
			if (st.isCompleted())
			{
				htmltext = "1-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = "1-nc.htm";
			}
			else if (cond == 1)
			{
				htmltext = "1-1.htm";
			}
		}
		return htmltext;
	}
}
