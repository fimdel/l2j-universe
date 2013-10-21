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
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

public class _10286_ReunionWithSirra extends Quest implements ScriptFile
{
	private static final int Rafforty = 32020;
	private static final int Jinia = 32760;
	private static final int Jinia2 = 32781;
	private static final int Sirra = 32762;
	
	public _10286_ReunionWithSirra()
	{
		super(false);
		addStartNpc(Rafforty);
		addTalkId(Jinia, Jinia2, Sirra);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("rafforty_q10286_02.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("enterinstance"))
		{
			st.setCond(2);
			enterInstance(st.getPlayer(), 141);
			return null;
		}
		else if (event.equalsIgnoreCase("sirraspawn"))
		{
			st.setCond(3);
			NpcInstance sirra = st.getPlayer().getReflection().addSpawnWithoutRespawn(Sirra, new Location(-23848, -8744, -5413, 49152), 0);
			Functions.npcSay(sirra, "Вы с таким энтузиазмом отправились в путь и это все, чего Вы добиись? Хе-хе-хе...");
			return null;
		}
		else if (event.equalsIgnoreCase("sirra_q10286_04.htm"))
		{
			st.giveItems(15470, 5);
			st.setCond(4);
			npc.deleteMe();
		}
		else if (event.equalsIgnoreCase("leaveinstance"))
		{
			st.setCond(5);
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
				QuestState qs = st.getPlayer().getQuestState(_10285_MeetingSirra.class);
				if ((st.getPlayer().getLevel() >= 82) && (qs != null) && qs.isCompleted())
				{
					htmltext = "rafforty_q10286_01.htm";
				}
				else
				{
					htmltext = "rafforty_q10286_00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if ((cond == 1) || (cond == 2) || (cond == 3) || (cond == 4))
			{
				htmltext = "rafforty_q10286_03.htm";
			}
		}
		else if (npcId == Jinia)
		{
			if (cond == 2)
			{
				htmltext = "jinia_q10286_01.htm";
			}
			else if (cond == 3)
			{
				htmltext = "jinia_q10286_01a.htm";
			}
			else if (cond == 4)
			{
				htmltext = "jinia_q10286_05.htm";
			}
		}
		else if (npcId == Sirra)
		{
			if (cond == 3)
			{
				htmltext = "sirra_q10286_01.htm";
			}
		}
		else if (npcId == Jinia2)
		{
			if (cond == 5)
			{
				htmltext = "jinia2_q10286_01.htm";
			}
			else if (cond == 6)
			{
				htmltext = "jinia2_q10286_04.htm";
			}
			else if (cond == 7)
			{
				htmltext = "jinia2_q10286_05.htm";
				st.addExpAndSp(2152200, 181070);
				st.setState(COMPLETED);
				st.exitCurrentQuest(false);
			}
		}
		return htmltext;
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
