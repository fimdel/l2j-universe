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
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _610_MagicalPowerofWater2 extends Quest implements ScriptFile
{
	private static final int ASEFA = 31372;
	private static final int VARKAS_HOLY_ALTAR = 31560;
	private static final int GREEN_TOTEM = 7238;
	int ICE_HEART_OF_ASHUTAR = 7239;
	private static final int Reward_First = 4589;
	private static final int Reward_Last = 4594;
	private static final int SoulOfWaterAshutar = 25316;
	private NpcInstance SoulOfWaterAshutarSpawn = null;
	
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
	
	public _610_MagicalPowerofWater2()
	{
		super(true);
		addStartNpc(ASEFA);
		addTalkId(VARKAS_HOLY_ALTAR);
		addKillId(SoulOfWaterAshutar);
		addQuestItem(ICE_HEART_OF_ASHUTAR);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		NpcInstance isQuest = GameObjectsStorage.getByNpcId(SoulOfWaterAshutar);
		String htmltext = event;
		if (event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "shaman_asefa_q0610_0104.htm";
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("610_1"))
		{
			if ((ServerVariables.getLong(_610_MagicalPowerofWater2.class.getSimpleName(), 0) + (3 * 60 * 60 * 1000)) > System.currentTimeMillis())
			{
				htmltext = "totem_of_barka_q0610_0204.htm";
			}
			else if ((st.getQuestItemsCount(GREEN_TOTEM) >= 1) && (isQuest == null))
			{
				st.takeItems(GREEN_TOTEM, 1);
				SoulOfWaterAshutarSpawn = st.addSpawn(SoulOfWaterAshutar, 104825, -36926, -1136);
				SoulOfWaterAshutarSpawn.addListener(new DeathListener());
				st.playSound(SOUND_MIDDLE);
			}
			else
			{
				htmltext = "totem_of_barka_q0610_0203.htm";
			}
		}
		else if (event.equalsIgnoreCase("610_3"))
		{
			if (st.getQuestItemsCount(ICE_HEART_OF_ASHUTAR) >= 1)
			{
				st.takeItems(ICE_HEART_OF_ASHUTAR, -1);
				st.giveItems(Rnd.get(Reward_First, Reward_Last), 5, true);
				st.playSound(SOUND_FINISH);
				htmltext = "shaman_asefa_q0610_0301.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "shaman_asefa_q0610_0302.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		NpcInstance isQuest = GameObjectsStorage.getByNpcId(SoulOfWaterAshutar);
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == ASEFA)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 75)
				{
					if (st.getQuestItemsCount(GREEN_TOTEM) >= 1)
					{
						htmltext = "shaman_asefa_q0610_0101.htm";
					}
					else
					{
						htmltext = "shaman_asefa_q0610_0102.htm";
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "shaman_asefa_q0610_0103.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "shaman_asefa_q0610_0105.htm";
			}
			else if (cond == 2)
			{
				htmltext = "shaman_asefa_q0610_0202.htm";
			}
			else if ((cond == 3) && (st.getQuestItemsCount(ICE_HEART_OF_ASHUTAR) >= 1))
			{
				htmltext = "shaman_asefa_q0610_0201.htm";
			}
		}
		else if (npcId == VARKAS_HOLY_ALTAR)
		{
			if (!npc.isBusy())
			{
				if ((ServerVariables.getLong(_610_MagicalPowerofWater2.class.getSimpleName(), 0) + (3 * 60 * 60 * 1000)) > System.currentTimeMillis())
				{
					htmltext = "totem_of_barka_q0610_0204.htm";
				}
				else if (cond == 1)
				{
					htmltext = "totem_of_barka_q0610_0101.htm";
				}
				else if ((cond == 2) && (isQuest == null))
				{
					SoulOfWaterAshutarSpawn = st.addSpawn(SoulOfWaterAshutar, 104825, -36926, -1136);
					SoulOfWaterAshutarSpawn.addListener(new DeathListener());
					htmltext = "totem_of_barka_q0610_0204.htm";
				}
			}
			else
			{
				htmltext = "totem_of_barka_q0610_0202.htm";
			}
		}
		return htmltext;
	}
	
	private static class DeathListener implements OnDeathListener
	{
		public DeathListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onDeath(Creature actor, Creature killer)
		{
			ServerVariables.set(_610_MagicalPowerofWater2.class.getSimpleName(), String.valueOf(System.currentTimeMillis()));
		}
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if ((st.getQuestItemsCount(ICE_HEART_OF_ASHUTAR) == 0) && (npc.getNpcId() == SoulOfWaterAshutar))
		{
			st.giveItems(ICE_HEART_OF_ASHUTAR, 1);
			st.setCond(3);
			if (SoulOfWaterAshutarSpawn != null)
			{
				SoulOfWaterAshutarSpawn.deleteMe();
			}
			SoulOfWaterAshutarSpawn = null;
		}
		return null;
	}
}
