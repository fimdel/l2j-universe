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

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.SubClassType;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExSubjobInfo;
import lineage2.gameserver.scripts.ScriptFile;

public class _177_StartTheNewDestiny extends Quest implements ScriptFile
{
	private static final int chanceGetItem = 30;
	private static final int Hadel = 33344;
	private static final int Ishuma = 32615;
	private static final int questitem_01 = 17718;
	private static final int questitem_02 = 17719;
	private static final int questitem_03 = 17720;
	private static final int questitem_04 = 17721;
	private static final List <Integer> _Mobs1 = new ArrayList<Integer>();
	private static final List <Integer> _Mobs2 = new ArrayList<Integer>();
	
	public _177_StartTheNewDestiny()
	{
		super(false);
		
		_Mobs1.clear();
		_Mobs1.add(21547);
		_Mobs1.add(21548);
		_Mobs1.add(21549);
		_Mobs1.add(21550);
		_Mobs1.add(21587);
		
		_Mobs2.clear();
		_Mobs2.add(22257);
		_Mobs2.add(22258);
		_Mobs2.add(22259);
		_Mobs2.add(22260);
		
		addStartNpc(Hadel);
		addTalkId(Hadel);
		addTalkId(Ishuma);
		addQuestItem(questitem_01, questitem_02, questitem_03, questitem_04);
		addKillId(_Mobs1);
		addKillId(_Mobs2);
		addLevelCheck(80, 99);
		addClassLevelCheck(4);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("33344_03.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.set("subClassId", st.getPlayer().getActiveClassId());
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("33344_07.htm"))
		{
			st.takeAllItems(questitem_03);
			st.setCond(4);
		}
		else if (event.equalsIgnoreCase("33344_10.htm"))
		{
			st.giveItems(questitem_03, 10);
			st.setCond(7);
		}
		else if (event.equalsIgnoreCase("32615_03.htm"))
		{
			st.takeAllItems(questitem_03);
			st.takeAllItems(questitem_04);
			st.setCond(8);
		}
		else if (event.equalsIgnoreCase("33344_13.htm"))
		{
			st.takeAllItems(questitem_01);
			st.takeAllItems(questitem_02);
		}
		else if (event.contains("SoulCrystal"))
		{
			st.giveItems(event.contains("Red") ? 10480 : event.contains("Blue") ? 10481 : 10482, 1);
			st.giveItems(18168, 1);
			st.addExpAndSp(175739575, 2886300);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
			st.getPlayer().getActiveSubClass().setType(SubClassType.DOUBLE_SUBCLASS);
			st.getPlayer().sendPacket(new ExSubjobInfo(st.getPlayer(), true));
			htmltext = "33344_16.htm";
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Hadel)
		{
			if(!(st.getState() == Quest.COMPLETED))
			{		
				if ((player.getActiveSubClass().isSub() && cond == 0 && !player.getSubClassList().haveDualClass()) ||(cond > 0 && player.getActiveSubClass().getClassId() == st.getInt("subClassId")))
				{
					if (cond == 0)
					{
						if (player.getLevel() >= 80 && ClassId.VALUES[player.getSubClassList().getBaseSubClass().getClassId()].isOfLevel(ClassLevel.Awaking) && ClassId.VALUES[player.getActiveSubClass().getClassId()].isOfLevel(ClassLevel.Fourth))
						{
							htmltext = "33344_01.htm";
						}
						else
						{
							htmltext = "33344_nosubclass.htm";
						}
					}
					else if (cond == 1)
					{
						htmltext = "33344_04.htm";
					}
					else if (cond == 2)
					{
						htmltext = "33344_04.htm";
					}
					else if (cond == 3)
					{
						if (st.getQuestItemsCount(questitem_03) >= 10)
						{
							htmltext = "33344_05.htm";
						}
					}
					else if (cond == 4)
					{
						htmltext = "33344_08.htm";
					}
					else if (cond == 5)
					{
						htmltext = "33344_08.htm";
					}
					else if (cond == 6)
					{
						if (st.getQuestItemsCount(questitem_04) >= 10)
						{
							htmltext = "33344_09.htm";
						}
					}
					else if (cond == 7)
					{
						htmltext = "33344_11.htm";
					}
					else if (cond == 9)
					{
						if ((st.getQuestItemsCount(questitem_01) >= 2) && (st.getQuestItemsCount(questitem_02) >= 2))
						{
							htmltext = "33344_12.htm";
						}
						else
						{
							htmltext = "33344_14.htm";
						}
					}
				}
				else
				{
					htmltext = "33344_nosubclass.htm";
				}
			}
			else
			{
				htmltext = "33344_completed.htm";
			}
		}
		else if (npcId == Ishuma)
		{
			if(!(st.getState() == Quest.COMPLETED))
			{
				if (st.getInt("subClassId") == player.getClassId().getId())
				{
					if (cond == 7)
					{
						if ((st.getQuestItemsCount(questitem_03) >= 10) && (st.getQuestItemsCount(questitem_04) >= 10))
						{
							htmltext = "32615_01.htm";
							st.setCond(8);
						}
					}
					else if (cond == 8)
					{
						st.giveItems(questitem_01, 2);
						st.giveItems(questitem_02, 2);
						htmltext = "32615_04.htm";
						st.setCond(9);
					}
					else if (cond == 9)
					{
						htmltext = "32615_05.htm";
					}
				}
				else
				{
					htmltext = "no_subclass.htm";
				}
			}
			else
			{
				htmltext = "32615_completed.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		int subclassId = st.getInt("subClassId");
		if((st.getPlayer().getActiveSubClass().getClassId() == subclassId) && st.getState() == Quest.STARTED)
		{
			if ((cond == 1) && _Mobs1.contains(npcId) && Rnd.chance(chanceGetItem))
			{
				if (st.getQuestItemsCount(questitem_03) < 1)
				{
					st.giveItems(questitem_03, 1);
					st.playSound(SOUND_MIDDLE);
					st.setCond(2);
				}
			}
			else if ((cond == 2) && _Mobs1.contains(npcId) && Rnd.chance(chanceGetItem))
			{
				if (st.getQuestItemsCount(questitem_03) < 10)
				{
					st.giveItems(questitem_03, 1);
					st.playSound(SOUND_ITEMGET);
				}
				if (st.getQuestItemsCount(questitem_03) >= 10)
				{
					st.playSound(SOUND_MIDDLE);
					st.setCond(3);
				}
			}
			else if ((cond == 4) && _Mobs2.contains(npcId) && Rnd.chance(chanceGetItem))
			{
				if (st.getQuestItemsCount(questitem_04) < 1)
				{
					st.giveItems(questitem_04, 1);
					st.playSound(SOUND_MIDDLE);
					st.setCond(5);
				}
			}
			else if ((cond == 5) && _Mobs2.contains(npcId) && Rnd.chance(chanceGetItem))
			{
				if (st.getQuestItemsCount(questitem_04) < 10)
				{
					st.giveItems(questitem_04, 1);
					st.playSound(SOUND_ITEMGET);
				}
				if (st.getQuestItemsCount(questitem_04) >= 10)
				{
					st.playSound(SOUND_MIDDLE);
					st.setCond(6);
				}
			}
		}
		else
		{
			if(cond > 0)
			{
				st.getPlayer().sendMessage("You cannot obtain the quest items if you are in different subclass to which started the quest.");
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
