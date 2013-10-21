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

public class _616_MagicalPowerofFire2 extends Quest implements ScriptFile
{
	private static final int KETRAS_HOLY_ALTAR = 31558;
	private static final int UDAN = 31379;
	private static final int FIRE_HEART_OF_NASTRON = 7244;
	private static final int RED_TOTEM = 7243;
	private static int Reward_First = 4589;
	private static int Reward_Last = 4594;
	private static final int SoulOfFireNastron = 25306;
	private NpcInstance SoulOfFireNastronSpawn = null;
	
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
	
	public _616_MagicalPowerofFire2()
	{
		super(true);
		addStartNpc(UDAN);
		addTalkId(KETRAS_HOLY_ALTAR);
		addKillId(SoulOfFireNastron);
		addQuestItem(FIRE_HEART_OF_NASTRON);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		NpcInstance isQuest = GameObjectsStorage.getByNpcId(SoulOfFireNastron);
		String htmltext = event;
		if (event.equalsIgnoreCase("quest_accept"))
		{
			htmltext = "shaman_udan_q0616_0104.htm";
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("616_1"))
		{
			if ((ServerVariables.getLong(_616_MagicalPowerofFire2.class.getSimpleName(), 0) + (3 * 60 * 60 * 1000)) > System.currentTimeMillis())
			{
				htmltext = "totem_of_ketra_q0616_0204.htm";
			}
			else if ((st.getQuestItemsCount(RED_TOTEM) >= 1) && (isQuest == null))
			{
				st.takeItems(RED_TOTEM, 1);
				SoulOfFireNastronSpawn = st.addSpawn(SoulOfFireNastron, 142528, -82528, -6496);
				SoulOfFireNastronSpawn.addListener(new DeathListener());
				st.playSound(SOUND_MIDDLE);
			}
			else
			{
				htmltext = "totem_of_ketra_q0616_0203.htm";
			}
		}
		else if (event.equalsIgnoreCase("616_3"))
		{
			if (st.getQuestItemsCount(FIRE_HEART_OF_NASTRON) >= 1)
			{
				st.takeItems(FIRE_HEART_OF_NASTRON, -1);
				st.giveItems(Rnd.get(Reward_First, Reward_Last), 5, true);
				st.playSound(SOUND_FINISH);
				htmltext = "shaman_udan_q0616_0301.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "shaman_udan_q0616_0302.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		NpcInstance isQuest = GameObjectsStorage.getByNpcId(SoulOfFireNastron);
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		switch (npcId)
		{
			case UDAN:
				if (cond == 0)
				{
					if (st.getPlayer().getLevel() >= 75)
					{
						if (st.getQuestItemsCount(RED_TOTEM) >= 1)
						{
							htmltext = "shaman_udan_q0616_0101.htm";
						}
						else
						{
							htmltext = "shaman_udan_q0616_0102.htm";
							st.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "shaman_udan_q0616_0103.htm";
						st.exitCurrentQuest(true);
					}
				}
				else if (cond == 1)
				{
					htmltext = "shaman_udan_q0616_0105.htm";
				}
				else if (cond == 2)
				{
					htmltext = "shaman_udan_q0616_0202.htm";
				}
				else if ((cond == 3) && (st.getQuestItemsCount(FIRE_HEART_OF_NASTRON) >= 1))
				{
					htmltext = "shaman_udan_q0616_0201.htm";
				}
				break;
			case KETRAS_HOLY_ALTAR:
				if ((ServerVariables.getLong(_616_MagicalPowerofFire2.class.getSimpleName(), 0) + (3 * 60 * 60 * 1000)) > System.currentTimeMillis())
				{
					htmltext = "totem_of_ketra_q0616_0204.htm";
				}
				else if (npc.isBusy())
				{
					htmltext = "totem_of_ketra_q0616_0202.htm";
				}
				else if (cond == 1)
				{
					htmltext = "totem_of_ketra_q0616_0101.htm";
				}
				else if (cond == 2)
				{
					if (isQuest == null)
					{
						SoulOfFireNastronSpawn = st.addSpawn(SoulOfFireNastron, 142528, -82528, -6496);
						SoulOfFireNastronSpawn.addListener(new DeathListener());
						htmltext = "totem_of_ketra_q0616_0204.htm";
					}
					else
					{
						htmltext = "<html><body>Already in spawn.</body></html>";
					}
				}
				break;
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
			ServerVariables.set(_616_MagicalPowerofFire2.class.getSimpleName(), String.valueOf(System.currentTimeMillis()));
		}
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getQuestItemsCount(FIRE_HEART_OF_NASTRON) == 0)
		{
			st.giveItems(FIRE_HEART_OF_NASTRON, 1);
			st.setCond(3);
			if (SoulOfFireNastronSpawn != null)
			{
				SoulOfFireNastronSpawn.deleteMe();
			}
			SoulOfFireNastronSpawn = null;
		}
		return null;
	}
}
