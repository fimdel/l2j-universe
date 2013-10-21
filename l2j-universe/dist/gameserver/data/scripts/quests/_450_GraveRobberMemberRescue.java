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

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;

public class _450_GraveRobberMemberRescue extends Quest implements ScriptFile
{
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
	
	private static int KANEMIKA = 32650;
	private static int WARRIOR_NPC = 32651;
	private static int WARRIOR_MON = 22741;
	private static int EVIDENCE_OF_MIGRATION = 14876;
	
	public _450_GraveRobberMemberRescue()
	{
		super(false);
		addStartNpc(KANEMIKA);
		addTalkId(WARRIOR_NPC);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("32650-05.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		return event;
	}
	
	@Override
	public String onTalk(final NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int id = st.getState();
		int cond = st.getCond();
		Player player = st.getPlayer();
		if (npcId == KANEMIKA)
		{
			if (id == CREATED)
			{
				if (player.getLevel() < 80)
				{
					htmltext = "32650-00.htm";
					st.exitCurrentQuest(true);
				}
				else if (!canEnter(player))
				{
					htmltext = "32650-09.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					htmltext = "32650-01.htm";
				}
			}
			else if (cond == 1)
			{
				if (st.getQuestItemsCount(EVIDENCE_OF_MIGRATION) >= 1)
				{
					htmltext = "32650-07.htm";
				}
				else
				{
					htmltext = "32650-06.htm";
				}
			}
			else if ((cond == 2) && (st.getQuestItemsCount(EVIDENCE_OF_MIGRATION) == 10))
			{
				htmltext = "32650-08.htm";
				st.giveItems(ADENA_ID, 371400);
				st.addExpAndSp(6886980, 8116410);
				st.takeItems(EVIDENCE_OF_MIGRATION, -1);
				st.exitCurrentQuest(true);
				st.playSound(SOUND_FINISH);
				st.getPlayer().setVar(getName(), String.valueOf(System.currentTimeMillis()), -1);
			}
		}
		else if ((cond == 1) && (npcId == WARRIOR_NPC))
		{
			if (Rnd.chance(50))
			{
				htmltext = "32651-01.htm";
				st.giveItems(EVIDENCE_OF_MIGRATION, 1);
				st.playSound(SOUND_ITEMGET);
				npc.moveToLocation(new Location(npc.getX() + 200, npc.getY() + 200, npc.getZ()), 0, false);
				ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						npc.deleteMe();
					}
				}, 2500L);
				if (st.getQuestItemsCount(EVIDENCE_OF_MIGRATION) == 10)
				{
					st.setCond(2);
					st.playSound(SOUND_MIDDLE);
				}
			}
			else
			{
				htmltext = "";
				player.sendPacket(new ExShowScreenMessage("The grave robber warrior has been filled with dark energy and is attacking you!", 4000, ScreenMessageAlign.MIDDLE_CENTER, false));
				NpcInstance warrior = st.addSpawn(WARRIOR_MON, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), 100, 120000);
				warrior.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, Rnd.get(1, 100));
				if (Rnd.chance(50))
				{
					Functions.npcSay(warrior, "...Grunt... oh...");
				}
				else
				{
					Functions.npcSay(warrior, "Grunt... What's... wrong with me...");
				}
				npc.decayMe();
				return null;
			}
		}
		return htmltext;
	}
	
	private boolean canEnter(Player player)
	{
		if (player.isGM())
		{
			return true;
		}
		String var = player.getVar(getName());
		if (var == null)
		{
			return true;
		}
		return (Long.parseLong(var) - System.currentTimeMillis()) > (24 * 60 * 60 * 1000);
	}
}
