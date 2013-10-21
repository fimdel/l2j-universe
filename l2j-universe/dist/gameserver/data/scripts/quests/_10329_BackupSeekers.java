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
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

public class _10329_BackupSeekers extends Quest implements ScriptFile
{
	private static final int atran = 33448;
	private static final int kakai = 30565;
	private static final int solder = 33204;
	private NpcInstance solderg = null;
	private static final int[] SOLDER_START_POINT =
	{
		-117880,
		255864,
		-1352
	};
	
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
	
	public _10329_BackupSeekers()
	{
		super(false);
		addStartNpc(kakai);
		addTalkId(atran);
		addTalkId(kakai);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(_10328_RequestOfSealedEvilFragments.class);
	}
	
	private void spawnsolder(QuestState st)
	{
		solderg = NpcUtils.spawnSingle(solder, Location.findPointToStay(SOLDER_START_POINT[0], SOLDER_START_POINT[1], SOLDER_START_POINT[2], 50, 100, st.getPlayer().getGeoIndex()));
		solderg.setFollowTarget(st.getPlayer());
	}
	
	private void despawnsolder()
	{
		if (solderg != null)
		{
			solderg.deleteMe();
		}
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
			htmltext = "0-3.htm";
			spawnsolder(st);
		}
		if (event.equalsIgnoreCase("qet_rev"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.GOING_INTO_REAL_WAR_SOULSHOTS_ADDED, 4500, ScreenMessageAlign.TOP_CENTER));
			htmltext = "1-2.htm";
			st.getPlayer().addExpAndSp(16900, 5000);
			st.giveItems(57, 25000);
			st.giveItems(875, 2);
			st.giveItems(906, 1);
			despawnsolder();
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
		if (npcId == kakai)
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
		if (npcId == atran)
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
