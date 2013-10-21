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

import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import services.SupportMagic;

public class _10352_LegacyofCrumaTower extends Quest implements ScriptFile
{
	public static final int LILEJ = 33155;
	public static final int LINKENS = 33163;
	public static final int STRANGE_MECHANIC_CR = 33158;
	public static final int MARTES_NPC = 33292;
	public static final int MARTES_RB = 25829;
	public static final int TRESURE_TOOL = 17619;
	public static final int MARTES_CORE = 17728;
	
	public _10352_LegacyofCrumaTower()
	{
		super(true);
		addStartNpc(33155);
		addTalkId(new int[]
		{
			33163
		});
		addTalkId(new int[]
		{
			33292
		});
		addKillId(new int[]
		{
			25829
		});
		addQuestItem(new int[]
		{
			17619
		});
		addQuestItem(new int[]
		{
			17728
		});
		
		addLevelCheck(38, 99);
	}
	
	@Override
	public void onShutdown()
	{
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		if (event.equalsIgnoreCase("33155-9.htm"))
		{
			SupportMagic.doSupportMagic(npc, player, false);
		}
		
		if (event.equalsIgnoreCase("33155-10.htm"))
		{
			SupportMagic.doSupportMagic(npc, player, true);
		}
		
		if (event.equalsIgnoreCase("advanceCond3"))
		{
			if (st.getCond() != 3)
			{
				st.setCond(3);
			}
			return null;
		}
		
		if (event.equalsIgnoreCase("teleportCruma"))
		{
			st.setCond(1);
			st.setState(2);
			st.playSound("ItemSound.quest_accept");
			player.teleToLocation(17192, 114173, -3439);
			return null;
		}
		if (event.equalsIgnoreCase("33163-8.htm"))
		{
			if (st.getQuestItemsCount(17619) == 0L)
			{
				st.giveItems(17619, 30L);
				st.setCond(2);
			}
			else
			{
				return "33163-12.htm";
			}
		}
		if (event.equalsIgnoreCase("EnterInstance"))
		{
			if (player.getParty() == null)
			{
				player.sendMessage("You cannot enter without party!");
				return null;
			}
			
			for (Player member : player.getParty().getPartyMembers())
			{
				QuestState qs = member.getQuestState(_10352_LegacyofCrumaTower.class);
				if ((qs != null) && (qs.getCond() == 3))
				{
					if (qs.getCond() == 3)
					{
						qs.setCond(4);
					}
				}
			}
			ReflectionUtils.enterReflection(player, 198);
			return null;
		}
		
		if (event.equalsIgnoreCase("LeaveInstance"))
		{
			player.teleToLocation(17192, 114173, -3439, ReflectionManager.DEFAULT);
			return null;
		}
		
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int npcId = npc.getNpcId();
		st.getState();
		int cond = st.getCond();
		if (player.getLevel() < 38)
		{
			return "33155-lvl.htm";
		}
		if (npcId == 33155)
		{
			if (cond < 5)
			{
				return "33155.htm";
			}
		}
		if (npcId == 33163)
		{
			if (cond == 1)
			{
				return "33163.htm";
			}
			if (cond == 2)
			{
				return "33163-5.htm";
			}
			if (cond == 5)
			{
				if (st.getQuestItemsCount(17728) == 0L)
				{
					return "33163-14.htm";
				}
				if (st.getQuestItemsCount(17728) != 0L)
				{
					st.takeItems(17728, -1L);
					st.takeItems(17619, -1L);
					st.addExpAndSp(480000L, 312000L);
					st.playSound("ItemSound.quest_finish");
					st.exitCurrentQuest(false);
					return "33163-15.htm";
				}
			}
		}
		
		if (npcId == 33292)
		{
			if (cond == 3)
			{
				return "25829.htm";
			}
			if (cond == 5)
			{
				return "25829-1.htm";
			}
		}
		return "noquest";
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
	public String onKill(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		if (player.getParty() == null)
		{
			st.setCond(5);
		}
		else
		{
			for (Player member : player.getParty().getPartyMembers())
			{
				QuestState qs = member.getQuestState(_10352_LegacyofCrumaTower.class);
				if ((qs != null) && (qs.getCond() == 4))
				{
					qs.setCond(5);
				}
			}
		}
		st.getPlayer().getReflection().addSpawnWithoutRespawn(33292, Location.findPointToStay(st.getPlayer(), 50, 100), st.getPlayer().getGeoIndex());
		return null;
	}
}