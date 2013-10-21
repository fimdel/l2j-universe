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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.ArrayUtils;

public class _423_TakeYourBestShot extends Quest implements ScriptFile
{
	private static final int Johnny = 32744;
	private static final int Batracos = 32740;
	private static final int TantaGuard = 18862;
	private static final int SeerUgorosPass = 15496;
	private static final int[] TantaClan =
	{
		22768,
		22769,
		22770,
		22771,
		22772,
		22773,
		22774
	};
	
	public _423_TakeYourBestShot()
	{
		super(true);
		addStartNpc(Johnny);
		addTalkId(Batracos);
		addKillId(TantaGuard);
		addKillId(TantaClan);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("johnny_q423_04.htm"))
		{
			st.exitCurrentQuest(true);
		}
		else if (event.equalsIgnoreCase("johnny_q423_05.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Johnny)
		{
			if (cond == 0)
			{
				QuestState qs = st.getPlayer().getQuestState(_249_PoisonedPlainsOfTheLizardmen.class);
				if ((st.getPlayer().getLevel() >= 82) && (qs != null) && qs.isCompleted())
				{
					htmltext = "johnny_q423_01.htm";
				}
				else
				{
					htmltext = "johnny_q423_00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "johnny_q423_06.htm";
			}
			else if (cond == 2)
			{
				htmltext = "johnny_q423_07.htm";
			}
		}
		else if (npcId == Batracos)
		{
			if (cond == 1)
			{
				htmltext = "batracos_q423_01.htm";
			}
			else if (cond == 2)
			{
				htmltext = "batracos_q423_02.htm";
				st.giveItems(SeerUgorosPass, 1);
				st.exitCurrentQuest(true);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (cond == 1)
		{
			if (ArrayUtils.contains(TantaClan, npcId) && Rnd.chance(2))
			{
				Location loc = st.getPlayer().getLoc();
				addSpawn(TantaGuard, loc.x, loc.y, loc.z, 0, 100, 120000);
			}
			else if ((npcId == TantaGuard) && (st.getQuestItemsCount(SeerUgorosPass) < 1))
			{
				st.setCond(2);
			}
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
