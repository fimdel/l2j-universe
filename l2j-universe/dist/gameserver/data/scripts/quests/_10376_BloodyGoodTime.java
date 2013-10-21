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

import java.util.HashMap;
import java.util.Map;

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _10376_BloodyGoodTime extends Quest implements ScriptFile
{
	private static final int NPC_ZENYA = 32140;
	private static final int NPC_CASCA = 32139;
	private static final int NPC_AGNES = 31588;
	private static final int NPC_ANDREI = 31292;
	private static final int MOB_BLOODY_VEIN = 27481;
	private static final int REWARD_MAGIC_RUNE_CLIP = 32700;
	public static final String _bloodyVein = "NightmareDeath";
	private static Map<Integer, Integer> spawns = new HashMap<>();
	
	public _10376_BloodyGoodTime()
	{
		super(false);
		addStartNpc(NPC_ZENYA);
		addTalkId(NPC_CASCA, NPC_AGNES, NPC_ANDREI);
		addKillNpcWithLog(3, _bloodyVein, 1, MOB_BLOODY_VEIN);
		addLevelCheck(80, 99);
		addQuestCompletedCheck(_10375_SuccubusDisciples.class);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("32140-06.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		if (event.equalsIgnoreCase("32139-03.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		if (event.equalsIgnoreCase("enterInstance"))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
			NpcInstance BloodyVein = st.addSpawn(MOB_BLOODY_VEIN, st.getPlayer().getX() + 50, st.getPlayer().getY() + 50, st.getPlayer().getZ(), 0, 0, 180000);
			spawns.put(st.getPlayer().getObjectId(), BloodyVein.getObjectId());
			BloodyVein.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, st.getPlayer(), 100000);
			return "";
		}
		if (event.equalsIgnoreCase("32139-08.htm"))
		{
			st.setCond(5);
			st.playSound(SOUND_MIDDLE);
		}
		if (event.equalsIgnoreCase("teleport_goddard"))
		{
			st.getPlayer().teleToLocation(149597, -57249, -2976);
			return "";
		}
		if (event.equalsIgnoreCase("31588-03.htm"))
		{
			st.setCond(6);
			st.playSound(SOUND_MIDDLE);
		}
		if (event.equalsIgnoreCase("31292-03.htm"))
		{
			st.addExpAndSp(121297500, 48433200);
			st.giveItems(REWARD_MAGIC_RUNE_CLIP, 1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == NPC_ZENYA)
		{
			switch (st.getState())
			{
				case COMPLETED:
					htmltext = "32140-05.htm";
					break;
				case CREATED:
					if (st.getPlayer().getLevel() >= 80)
					{
						QuestState qs = st.getPlayer().getQuestState(_10375_SuccubusDisciples.class);
						if ((st.getPlayer().getClassId().level() == 4) && (qs != null) && qs.isCompleted())
						{
							htmltext = "32140-01.htm";
						}
						else
						{
							htmltext = "32140-03.htm";
							st.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "32140-04.htm";
					}
					break;
				case STARTED:
					htmltext = "32140-07.htm";
			}
		}
		else if (npcId == NPC_CASCA)
		{
			if (st.isStarted())
			{
				switch (st.getCond())
				{
					case 1:
						htmltext = "32139-02.htm";
						break;
					case 2:
					case 3:
						htmltext = "32139-03.htm";
						Integer obj_id = spawns.get(st.getPlayer().getObjectId());
						NpcInstance mob = obj_id != null ? GameObjectsStorage.getNpc(obj_id) : null;
						if ((mob == null) || mob.isDead())
						{
							htmltext = "32139-03.htm";
						}
						else
						{
							htmltext = "noquest";
						}
						break;
					case 4:
						htmltext = "32139-04.htm";
						break;
					case 5:
						htmltext = "32139-08.htm";
				}
			}
		}
		else if (npcId == NPC_AGNES)
		{
			if (st.isStarted())
			{
				if (st.getCond() == 5)
				{
					htmltext = "31588-01.htm";
				}
				else if (st.getCond() == 6)
				{
					htmltext = "31588-03.htm";
				}
			}
		}
		else if (npcId == NPC_ANDREI)
		{
			if (st.isStarted())
			{
				if (st.getCond() == 6)
				{
					htmltext = "31292-01.htm";
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getCond() != 3)
		{
			return null;
		}
		if (updateKill(npc, st))
		{
			st.unset(_bloodyVein);
			st.setCond(4);
			st.playSound(SOUND_MIDDLE);
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
