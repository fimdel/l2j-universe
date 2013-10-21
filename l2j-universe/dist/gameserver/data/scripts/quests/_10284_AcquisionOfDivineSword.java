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
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

public class _10284_AcquisionOfDivineSword extends Quest implements ScriptFile
{
	private static final int Rafforty = 32020;
	private static final int Jinia = 32760;
	private static final int Krun = 32653;
	private static final int ColdResistancePotion = 15514;
	private static final int InjKegor = 18846;
	private static final int MithrilMillipede = 22766;
	int _count = 0;
	
	public _10284_AcquisionOfDivineSword()
	{
		super(false);
		addStartNpc(Rafforty);
		addTalkId(Jinia, Krun, InjKegor);
		addKillId(MithrilMillipede);
		addQuestItem(ColdResistancePotion);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("rafforty_q10284_02.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("enterinstance"))
		{
			st.setCond(2);
			enterInstance(st.getPlayer(), 140);
			return null;
		}
		else if (event.equalsIgnoreCase("jinia_q10284_03.htm"))
		{
			if (!st.getPlayer().getReflection().isDefault())
			{
				st.getPlayer().getReflection().startCollapseTimer(60 * 1000L);
				st.getPlayer().sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(1));
			}
			st.setCond(3);
		}
		else if (event.equalsIgnoreCase("leaveinstance"))
		{
			st.getPlayer().getReflection().collapse();
			return null;
		}
		else if (event.equalsIgnoreCase("entermines"))
		{
			st.setCond(4);
			if (st.getQuestItemsCount(ColdResistancePotion) < 1)
			{
				st.giveItems(ColdResistancePotion, 1);
			}
			enterInstance(st.getPlayer(), 138);
			return null;
		}
		else if (event.equalsIgnoreCase("leavemines"))
		{
			st.giveItems(ADENA_ID, 296425);
			st.addExpAndSp(921805, 82230);
			st.playSound(SOUND_FINISH);
			st.setState(COMPLETED);
			st.exitCurrentQuest(false);
			st.getPlayer().getReflection().collapse();
			return null;
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Rafforty)
		{
			if (cond == 0)
			{
				QuestState qs = st.getPlayer().getQuestState(_10283_RequestOfIceMerchant.class);
				if ((st.getPlayer().getLevel() >= 82) && (qs != null) && qs.isCompleted())
				{
					htmltext = "rafforty_q10284_01.htm";
				}
				else
				{
					htmltext = "rafforty_q10284_00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if ((cond == 1) || (cond == 2))
			{
				htmltext = "rafforty_q10284_02.htm";
			}
		}
		else if (npcId == Jinia)
		{
			if (cond == 2)
			{
				htmltext = "jinia_q10284_01.htm";
			}
			else if (cond == 3)
			{
				htmltext = "jinia_q10284_02.htm";
			}
		}
		else if (npcId == Krun)
		{
			if ((cond == 3) || (cond == 4) || (cond == 5))
			{
				htmltext = "krun_q10284_01.htm";
			}
		}
		else if (npcId == InjKegor)
		{
			if (cond == 4)
			{
				st.takeAllItems(ColdResistancePotion);
				st.setCond(5);
				htmltext = "kegor_q10284_01.htm";
				for (int i = 0; i < 4; i++)
				{
					NpcInstance mob = st.getPlayer().getReflection().addSpawnWithoutRespawn(MithrilMillipede, Location.findPointToStay(st.getPlayer(), 50, 100), st.getPlayer().getGeoIndex());
					mob.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, st.getPlayer(), 300);
				}
			}
			else if (cond == 5)
			{
				htmltext = "kegor_q10284_02.htm";
			}
			else if (cond == 6)
			{
				htmltext = "kegor_q10284_03.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if ((cond == 5) && (npcId == MithrilMillipede))
		{
			if (_count < 3)
			{
				_count++;
			}
			else
			{
				st.setCond(6);
				st.getPlayer().getReflection().startCollapseTimer(3 * 60 * 1000L);
				st.getPlayer().sendPacket(new SystemMessage(SystemMessage.THIS_DUNGEON_WILL_EXPIRE_IN_S1_MINUTES).addNumber(3));
			}
		}
		return null;
	}
	
	private void enterInstance(Player player, int izId)
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
