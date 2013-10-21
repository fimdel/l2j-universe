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

import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.ScriptFile;

public class _061_LawEnforcement extends Quest implements ScriptFile
{
	private static final int COND1 = 1;
	private static final int COND2 = 2;
	private static final int Liane = 32222;
	private static final int Kekropus = 32138;
	private static final int Eindburgh = 32469;
	
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
	
	public _061_LawEnforcement()
	{
		super(false);
		addStartNpc(Liane);
		addTalkId(Kekropus, Eindburgh);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equals("ask"))
		{
			if (st.getPlayer().getRace() != Race.kamael)
			{
				htmltext = "grandmaste_piane_q0061_03.htm";
				st.exitCurrentQuest(true);
			}
			else if ((st.getPlayer().getClassId() != ClassId.INSPECTOR) || (st.getPlayer().getLevel() < 76))
			{
				htmltext = "grandmaste_piane_q0061_02.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "grandmaste_piane_q0061_04.htm";
			}
		}
		else if (event.equals("accept"))
		{
			st.setState(STARTED);
			st.setCond(COND1);
			st.playSound(SOUND_ACCEPT);
			htmltext = "grandmaste_piane_q0061_05.htm";
		}
		else if (event.equals("kekrops_q0061_09.htm"))
		{
			st.setCond(COND2);
		}
		else if (event.equals("subelder_aientburg_q0061_08.htm") || event.equals("subelder_aientburg_q0061_09.htm"))
		{
			st.giveItems(ADENA_ID, 26000);
			st.getPlayer().setClassId(ClassId.JUDICATOR.ordinal(), false, false);
			st.getPlayer().broadcastCharInfo();
			st.getPlayer().broadcastPacket(new MagicSkillUse(st.getPlayer(), 4339, 1, 6000, 1));
			st.getPlayer().broadcastPacket(new MagicSkillUse(npc, 4339, 1, 6000, 1));
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Liane)
		{
			if (st.getState() == CREATED)
			{
				htmltext = "grandmaste_piane_q0061_01.htm";
			}
			else
			{
				htmltext = "grandmaste_piane_q0061_06.htm";
			}
		}
		else if (npcId == Kekropus)
		{
			if (cond == COND1)
			{
				htmltext = "kekrops_q0061_01.htm";
			}
			else if (cond == COND2)
			{
				htmltext = "kekrops_q0061_10.htm";
			}
		}
		else if ((npcId == Eindburgh) && (cond == COND2))
		{
			htmltext = "subelder_aientburg_q0061_01.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		return null;
	}
}
