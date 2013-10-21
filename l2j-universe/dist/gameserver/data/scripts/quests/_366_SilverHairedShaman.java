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

public class _366_SilverHairedShaman extends Quest implements ScriptFile
{
	private static final int DIETER = 30111;
	private static final int SAIRON = 20986;
	private static final int SAIRONS_DOLL = 20987;
	private static final int SAIRONS_PUPPET = 20988;
	private static final int ADENA_PER_ONE = 500;
	private static final int START_ADENA = 12070;
	private static final int SAIRONS_SILVER_HAIR = 5874;
	
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
	
	public _366_SilverHairedShaman()
	{
		super(false);
		addStartNpc(DIETER);
		addKillId(SAIRON);
		addKillId(SAIRONS_DOLL);
		addKillId(SAIRONS_PUPPET);
		addQuestItem(SAIRONS_SILVER_HAIR);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("30111-02.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30111-quit.htm"))
		{
			st.takeItems(SAIRONS_SILVER_HAIR, -1);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int id = st.getState();
		int cond = st.getCond();
		if (id == CREATED)
		{
			st.setCond(0);
		}
		else
		{
			cond = st.getCond();
		}
		if (npcId == 30111)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() >= 48)
				{
					htmltext = "30111-01.htm";
				}
				else
				{
					htmltext = "30111-00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if ((cond == 1) && (st.getQuestItemsCount(SAIRONS_SILVER_HAIR) == 0))
			{
				htmltext = "30111-03.htm";
			}
			else if ((cond == 1) && (st.getQuestItemsCount(SAIRONS_SILVER_HAIR) >= 1))
			{
				st.giveItems(ADENA_ID, ((st.getQuestItemsCount(SAIRONS_SILVER_HAIR) * ADENA_PER_ONE) + START_ADENA));
				st.takeItems(SAIRONS_SILVER_HAIR, -1);
				htmltext = "30111-have.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if ((cond == 1) && Rnd.chance(66))
		{
			st.giveItems(SAIRONS_SILVER_HAIR, 1);
			st.playSound(SOUND_MIDDLE);
		}
		return null;
	}
}
