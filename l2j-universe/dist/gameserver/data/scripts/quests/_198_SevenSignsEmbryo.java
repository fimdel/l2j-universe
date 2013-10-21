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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

public class _198_SevenSignsEmbryo extends Quest implements ScriptFile
{
	private static int Wood = 32593;
	private static int Franz = 32597;
	private static int Jaina = 32582;
	private static int ShilensEvilThoughtsCapt = 27346;
	private static int PieceOfDoubt = 14355;
	private static int DawnsBracelet = 15312;
	private static int Adena = 57;
	private static final int izId = 113;
	Location setcloc = new Location(-23734, -9184, -5384, 0);
	
	public _198_SevenSignsEmbryo()
	{
		super(false);
		addStartNpc(Wood);
		addTalkId(Wood, Franz, Jaina);
		addKillId(ShilensEvilThoughtsCapt);
		addQuestItem(PieceOfDoubt);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		String htmltext = event;
		if (event.equalsIgnoreCase("wood_q198_2.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("wood_q198_3.htm"))
		{
			enterInstance(player);
			if (st.get("embryo") != null)
			{
				st.unset("embryo");
			}
		}
		else if (event.equalsIgnoreCase("franz_q198_3.htm"))
		{
			NpcInstance embryo = player.getReflection().addSpawnWithoutRespawn(ShilensEvilThoughtsCapt, setcloc, 0);
			st.set("embryo", 1);
			Functions.npcSay(npc, player.getName() + "! You should kill this monster! I'll try to help!");
			Functions.npcSay(embryo, "This is not yours.");
			embryo.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, player, 500);
		}
		else if (event.equalsIgnoreCase("wood_q198_8.htm"))
		{
			enterInstance(player);
		}
		else if (event.equalsIgnoreCase("franz_q198_5.htm"))
		{
			Functions.npcSay(npc, "We will be with you always...");
			st.takeItems(PieceOfDoubt, -1);
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("jaina_q198_2.htm"))
		{
			player.getReflection().collapse();
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		Player player = st.getPlayer();
		String htmltext = "noquest";
		if (npcId == Wood)
		{
			QuestState qs = player.getQuestState(_197_SevenSignsTheSacredBookofSeal.class);
			if (cond == 0)
			{
				if ((player.getLevel() >= 79) && (qs != null) && qs.isCompleted())
				{
					htmltext = "wood_q198_1.htm";
				}
				else
				{
					htmltext = "wood_q198_0.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if ((cond == 1) || (cond == 2))
			{
				htmltext = "wood_q198_2a.htm";
			}
			else if (cond == 3)
			{
				if (player.getBaseClassId() == player.getActiveClassId())
				{
					st.addExpAndSp(67500000, 15000000);
					st.giveItems(DawnsBracelet, 1);
					st.giveItems(Adena, 1500000);
					st.setState(COMPLETED);
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(false);
					htmltext = "wood_q198_4.htm";
				}
				else
				{
					htmltext = "subclass_forbidden.htm";
				}
			}
		}
		else if (npcId == Franz)
		{
			if (cond == 1)
			{
				if ((st.get("embryo") == null) || (Integer.parseInt(st.get("embryo")) != 1))
				{
					htmltext = "franz_q198_1.htm";
				}
				else
				{
					htmltext = "franz_q198_3a.htm";
				}
			}
			else if (cond == 2)
			{
				htmltext = "franz_q198_4.htm";
			}
			else
			{
				htmltext = "franz_q198_6.htm";
			}
		}
		else if (npcId == Jaina)
		{
			htmltext = "jaina_q198_1.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		Player player = st.getPlayer();
		if (player == null)
		{
			return null;
		}
		if ((npcId == ShilensEvilThoughtsCapt) && (cond == 1))
		{
			Functions.npcSay(npc, player.getName() + ", I'm leaving now. But we shall meet again!");
			st.set("embryo", 2);
			st.setCond(2);
			st.giveItems(PieceOfDoubt, 1);
			player.showQuestMovie(ExStartScenePlayer.SCENE_SSQ_EMBRYO);
		}
		return null;
	}
	
	private void enterInstance(Player player)
	{
		Reflection r = player.getActiveReflection();
		if (r != null)
		{
			if (player.canReenterInstance(izId))
			{
				player.teleToLocation(r.getTeleportLoc(), r);
			}
		}
		else if (player.canEnterInstance(izId))
		{
			ReflectionUtils.enterReflection(player, izId);
		}
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
