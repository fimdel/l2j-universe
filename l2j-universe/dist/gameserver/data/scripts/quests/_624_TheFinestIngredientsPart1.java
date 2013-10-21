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

import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.ScriptFile;

public class _624_TheFinestIngredientsPart1 extends Quest implements ScriptFile
{
	private static int JEREMY = 31521;
	private static int HOT_SPRINGS_ATROX = 21321;
	private static int HOT_SPRINGS_NEPENTHES = 21319;
	private static int HOT_SPRINGS_ATROXSPAWN = 21317;
	private static int HOT_SPRINGS_BANDERSNATCHLING = 21314;
	private static int SECRET_SPICE = 7204;
	private static int TRUNK_OF_NEPENTHES = 7202;
	private static int FOOT_OF_BANDERSNATCHLING = 7203;
	private static int CRYOLITE = 7080;
	private static int SAUCE = 7205;
	
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
	
	public _624_TheFinestIngredientsPart1()
	{
		super(true);
		addStartNpc(JEREMY);
		addKillId(HOT_SPRINGS_ATROX);
		addKillId(HOT_SPRINGS_NEPENTHES);
		addKillId(HOT_SPRINGS_ATROXSPAWN);
		addKillId(HOT_SPRINGS_BANDERSNATCHLING);
		addQuestItem(TRUNK_OF_NEPENTHES);
		addQuestItem(FOOT_OF_BANDERSNATCHLING);
		addQuestItem(SECRET_SPICE);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("jeremy_q0624_0104.htm"))
		{
			if (st.getPlayer().getLevel() >= 73)
			{
				st.setState(STARTED);
				st.setCond(1);
				st.playSound(SOUND_ACCEPT);
			}
			else
			{
				htmltext = "jeremy_q0624_0103.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if (event.equalsIgnoreCase("jeremy_q0624_0201.htm"))
		{
			if ((st.getQuestItemsCount(TRUNK_OF_NEPENTHES) == 50) && (st.getQuestItemsCount(FOOT_OF_BANDERSNATCHLING) == 50) && (st.getQuestItemsCount(SECRET_SPICE) == 50))
			{
				st.takeItems(TRUNK_OF_NEPENTHES, -1);
				st.takeItems(FOOT_OF_BANDERSNATCHLING, -1);
				st.takeItems(SECRET_SPICE, -1);
				st.playSound(SOUND_FINISH);
				st.giveItems(SAUCE, 1);
				st.giveItems(CRYOLITE, 1);
				htmltext = "jeremy_q0624_0201.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "jeremy_q0624_0202.htm";
				st.setCond(1);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getCond();
		if (cond == 0)
		{
			htmltext = "jeremy_q0624_0101.htm";
		}
		else if (cond != 3)
		{
			htmltext = "jeremy_q0624_0106.htm";
		}
		else
		{
			htmltext = "jeremy_q0624_0105.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getState() != STARTED)
		{
			return null;
		}
		int npcId = npc.getNpcId();
		if (st.getCond() == 1)
		{
			if ((npcId == HOT_SPRINGS_NEPENTHES) && (st.getQuestItemsCount(TRUNK_OF_NEPENTHES) < 50))
			{
				st.rollAndGive(TRUNK_OF_NEPENTHES, 1, 1, 50, 100);
			}
			else if ((npcId == HOT_SPRINGS_BANDERSNATCHLING) && (st.getQuestItemsCount(FOOT_OF_BANDERSNATCHLING) < 50))
			{
				st.rollAndGive(FOOT_OF_BANDERSNATCHLING, 1, 1, 50, 100);
			}
			else if (((npcId == HOT_SPRINGS_ATROX) || (npcId == HOT_SPRINGS_ATROXSPAWN)) && (st.getQuestItemsCount(SECRET_SPICE) < 50))
			{
				st.rollAndGive(SECRET_SPICE, 1, 1, 50, 100);
			}
			onKillCheck(st);
		}
		return null;
	}
	
	private void onKillCheck(QuestState st)
	{
		if ((st.getQuestItemsCount(TRUNK_OF_NEPENTHES) == 50) && (st.getQuestItemsCount(FOOT_OF_BANDERSNATCHLING) == 50) && (st.getQuestItemsCount(SECRET_SPICE) == 50))
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(3);
		}
		else
		{
			st.playSound(SOUND_ITEMGET);
		}
	}
}
