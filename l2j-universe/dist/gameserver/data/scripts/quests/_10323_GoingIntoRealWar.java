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
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.TutorialShowHtml;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;
import lineage2.gameserver.utils.ReflectionUtils;

public class _10323_GoingIntoRealWar extends Quest implements ScriptFile
{
	private final static int key = 17574;
	private NpcInstance solderg = null;
	private final static int shenon = 32974;
	private final static int evain = 33464;
	private final static int holden = 33194;
	private final static int guard = 33021;
	private final static int husk = 23113;
	private final static int solder = 33006;
	private static final int[] SOLDER_START_POINT =
	{
		-110808,
		253896,
		-1817
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
	
	public _10323_GoingIntoRealWar()
	{
		super(false);
		addStartNpc(evain);
		addTalkId(shenon);
		addTalkId(holden);
		addFirstTalkId(guard);
		addKillId(husk);
		addLevelCheck(1, 20);
		addQuestCompletedCheck(_10322_SearchingfortheMysteriousPower.class);
	}
	
	private void spawnsolder(QuestState st)
	{
		solderg = NpcUtils.spawnSingle(solder, Location.findPointToStay(SOLDER_START_POINT[0], SOLDER_START_POINT[1], SOLDER_START_POINT[2], 50, 100, st.getPlayer().getGeoIndex()));
		solderg.setFollowTarget(st.getPlayer());
		Functions.npcSay(solderg, NpcString.S1_COME_WITH_ME_I_WILL_LEAD_YOU_TO_HOLDEN, ChatType.NPC_SAY, 800, st.getPlayer().getName());
	}
	
	private void despawnsolder()
	{
		if (solderg != null)
		{
			solderg.deleteMe();
		}
	}
	
	private void enterInstance(Player player, int instancedZoneId)
	{
		Reflection r = player.getActiveReflection();
		if (r != null)
		{
			if (player.canReenterInstance(instancedZoneId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(instancedZoneId))
		{
			ReflectionUtils.enterReflection(player, instancedZoneId);
		}
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Player player = st.getPlayer();
		if (event.equalsIgnoreCase("quest_ac"))
		{
			st.giveItems(17574, 1);
			st.setState(STARTED);
			st.setCond(1);
			spawnsolder(st);
			st.playSound(SOUND_ACCEPT);
			htmltext = "0-3.htm";
		}
		if (event.equalsIgnoreCase("qet_rev"))
		{
			htmltext = "3-2.htm";
			st.getPlayer().addExpAndSp(300, 1500);
			st.giveItems(57, 9000);
			st.takeAllItems(17574);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		if (event.equalsIgnoreCase("enter_instance"))
		{
			if (st.getQuestItemsCount(key) >= 1)
			{
				despawnsolder();
				st.setCond(2);
				st.playSound(SOUND_MIDDLE);
				enterInstance(player, 176);
				return null;
			}
			htmltext = "1-1.htm";
		}
		if (event.equalsIgnoreCase("getshots"))
		{
			st.playSound(SOUND_MIDDLE);
			st.showTutorialHTML(TutorialShowHtml.QT_003, TutorialShowHtml.TYPE_WINDOW);
			if (!player.isMageClass() || (player.getTemplate().getRace() == Race.orc))
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.GOING_INTO_REAL_WAR_SOULSHOTS_ADDED, 4500, ScreenMessageAlign.TOP_CENTER));
				st.startQuestTimer("soul_timer", 4000);
				st.giveItems(5789, 500);
				st.setCond(5);
			}
			else
			{
				player.sendPacket(new ExShowScreenMessage(NpcString.GOING_INTO_REAL_WAR_SPIRITSHOTS_ADDED, 4500, ScreenMessageAlign.TOP_CENTER));
				st.startQuestTimer("spirit_timer", 4000);
				st.giveItems(5790, 500);
				st.setCond(4);
			}
			return null;
		}
		if (event.equalsIgnoreCase("soul_timer"))
		{
			htmltext = "2-3.htm";
			player.sendPacket(new ExShowScreenMessage(NpcString.GOING_INTO_REAL_WAR_AUTOMATE_SOULSHOTS, 4500, ScreenMessageAlign.TOP_CENTER));
		}
		if (event.equalsIgnoreCase("spirit_timer"))
		{
			htmltext = "2-3m.htm";
			player.sendPacket(new ExShowScreenMessage(NpcString.GOING_INTO_REAL_WAR_AUTOMATE_SPIRITSHOTS, 4500, ScreenMessageAlign.TOP_CENTER));
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == evain)
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
			else if ((cond >= 1) && (st.getQuestItemsCount(key) < 1))
			{
				st.giveItems(17574, 1);
				htmltext = "0-5.htm";
			}
			else if (cond == 8)
			{
				htmltext = "0-6.htm";
			}
			else
			{
				htmltext = "noqu.htm";
			}
		}
		else if (npcId == shenon)
		{
			if (st.isCompleted())
			{
				htmltext = "3-c.htm";
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 8)
			{
				htmltext = "3-1.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(NpcInstance npc, Player player)
	{
		QuestState st = player.getQuestState(getClass());
		String htmltext = "";
		if (npc.getNpcId() == guard)
		{
			if (st.getCond() == 3)
			{
				if (!player.isMageClass() || (player.getTemplate().getRace() == Race.orc))
				{
					htmltext = "2-2.htm";
				}
				else
				{
					htmltext = "2-2m.htm";
				}
			}
			else if ((st.getCond() == 4) || (st.getCond() == 5))
			{
				st.setCond(7);
				st.playSound(SOUND_MIDDLE);
				st.getPlayer().getReflection().addSpawnWithoutRespawn(husk, new Location(-115029, 247884, -7872, 0), 0);
				st.getPlayer().getReflection().addSpawnWithoutRespawn(husk, new Location(-114921, 248281, -7872, 0), 0);
				st.getPlayer().getReflection().addSpawnWithoutRespawn(husk, new Location(-114559, 248661, -7872, 0), 0);
				st.getPlayer().getReflection().addSpawnWithoutRespawn(husk, new Location(-114148, 248416, -7872, 0), 0);
				if (!player.isMageClass() || (player.getTemplate().getRace() == Race.orc))
				{
					htmltext = "2-4.htm";
				}
				else
				{
					htmltext = "2-4m.htm";
				}
			}
			else if (st.getCond() == 8)
			{
				htmltext = "2-5.htm";
			}
			else if (st.isCompleted())
			{
				htmltext = TODO_FIND_HTML;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int killedhusk = st.getInt("killedhusk");
		if ((npcId == husk) && ((st.getCond() == 2) || (st.getCond() == 7)))
		{
			if (killedhusk >= 3)
			{
				st.setCond(st.getCond() + 1);
				st.unset("killedhusk");
				st.playSound(SOUND_MIDDLE);
			}
			else
			{
				st.set("killedhusk", ++killedhusk);
			}
		}
		return null;
	}
}
