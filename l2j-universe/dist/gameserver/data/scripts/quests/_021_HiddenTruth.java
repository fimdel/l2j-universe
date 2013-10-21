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
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;

public class _021_HiddenTruth extends Quest implements ScriptFile
{
	public static final int DARIN = 30048;
	public static final int ROXXY = 30006;
	public static final int BAULRO = 30033;
	public static final int MysteriousWizard = 31522;
	public static final int Tombstone = 31523;
	public static final int GhostofvonHellmannId = 31524;
	public static final int GhostofvonHellmannsPageId = 31525;
	public static final int BrokenBookshelf = 31526;
	public static final int Agripel = 31348;
	public static final int Dominic = 31350;
	public static final int Benedict = 31349;
	public static final int Innocentin = 31328;
	public static final int CrossofEinhasad = 7140;
	public static final int CrossofEinhasadNextQuest = 7141;
	public NpcInstance GhostofvonHellmannsPage;
	public NpcInstance GhostofvonHellmann;
	
	private void spawnGhostofvonHellmannsPage()
	{
		GhostofvonHellmannsPage = Functions.spawn(new Location(51462, -54539, -3176), GhostofvonHellmannsPageId);
	}
	
	private void despawnGhostofvonHellmannsPage()
	{
		if (GhostofvonHellmannsPage != null)
		{
			GhostofvonHellmannsPage.deleteMe();
		}
		GhostofvonHellmannsPage = null;
	}
	
	private void spawnGhostofvonHellmann()
	{
		GhostofvonHellmann = Functions.spawn(Location.findPointToStay(new Location(51432, -54570, -3136), 50, ReflectionManager.DEFAULT.getGeoIndex()), GhostofvonHellmannId);
	}
	
	private void despawnGhostofvonHellmann()
	{
		if (GhostofvonHellmann != null)
		{
			GhostofvonHellmann.deleteMe();
		}
		GhostofvonHellmann = null;
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
	
	public _021_HiddenTruth()
	{
		super(false);
		addStartNpc(MysteriousWizard);
		addTalkId(Tombstone);
		addTalkId(GhostofvonHellmannId);
		addTalkId(GhostofvonHellmannsPageId);
		addTalkId(BrokenBookshelf);
		addTalkId(Agripel);
		addTalkId(Dominic);
		addTalkId(Benedict);
		addTalkId(Innocentin);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("31522-02.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
		}
		else if (event.equalsIgnoreCase("html"))
		{
			htmltext = "31328-05.htm";
		}
		else if (event.equalsIgnoreCase("31328-05.htm"))
		{
			st.unset("cond");
			st.takeItems(CrossofEinhasad, -1);
			if (st.getQuestItemsCount(CrossofEinhasadNextQuest) == 0)
			{
				st.giveItems(CrossofEinhasadNextQuest, 1);
			}
			st.addExpAndSp(3472500, 3291820);
			st.playSound(SOUND_FINISH);
			st.startQuestTimer("html", 1);
			htmltext = "Congratulations! You are completed this quest!<br>The Quest \"Tragedy In Von Hellmann Forest\" become available.<br>Show Cross of Einhasad to High Priest Tifaren.";
			st.exitCurrentQuest(false);
		}
		else if (event.equalsIgnoreCase("31523-03.htm"))
		{
			st.playSound(SOUND_HORROR2);
			st.setCond(2);
			despawnGhostofvonHellmann();
			spawnGhostofvonHellmann();
		}
		else if (event.equalsIgnoreCase("31524-06.htm"))
		{
			st.setCond(3);
			despawnGhostofvonHellmannsPage();
			spawnGhostofvonHellmannsPage();
		}
		else if (event.equalsIgnoreCase("31526-03.htm"))
		{
			st.playSound(SOUND_ITEM_DROP_EQUIP_ARMOR_CLOTH);
		}
		else if (event.equalsIgnoreCase("31526-08.htm"))
		{
			st.playSound(SOUND_ED_CHIMES05);
			st.setCond(5);
		}
		else if (event.equalsIgnoreCase("31526-14.htm"))
		{
			st.giveItems(CrossofEinhasad, 1);
			st.setCond(6);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "This person inaccessible and does not want with you to talk!<br>Are they please returned later...";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == MysteriousWizard)
		{
			if (cond == 0)
			{
				if (st.getPlayer().getLevel() > 62)
				{
					htmltext = "31522-01.htm";
				}
				else
				{
					htmltext = "31522-03.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if (cond == 1)
			{
				htmltext = "31522-05.htm";
			}
		}
		else if (npcId == Tombstone)
		{
			if (cond == 1)
			{
				htmltext = "31523-01.htm";
			}
			else if ((cond == 2) || (cond == 3))
			{
				htmltext = "31523-04.htm";
				st.playSound(SOUND_HORROR2);
				despawnGhostofvonHellmann();
				spawnGhostofvonHellmann();
			}
		}
		else if (npcId == GhostofvonHellmannId)
		{
			if (cond == 2)
			{
				htmltext = "31524-01.htm";
			}
			else if (cond == 3)
			{
				htmltext = "31524-07b.htm";
			}
			else if (cond == 4)
			{
				htmltext = "31524-07c.htm";
			}
		}
		else if (npcId == GhostofvonHellmannsPageId)
		{
			if ((cond == 3) || (cond == 4))
			{
				htmltext = "31525-01.htm";
				if ((GhostofvonHellmannsPage == null) || !GhostofvonHellmannsPage.isMoving)
				{
					htmltext = "31525-02.htm";
					if (cond == 3)
					{
						st.setCond(4);
					}
					despawnGhostofvonHellmannsPage();
				}
			}
		}
		else if (npcId == BrokenBookshelf)
		{
			if ((cond == 4) || (cond == 3))
			{
				despawnGhostofvonHellmannsPage();
				despawnGhostofvonHellmann();
				st.setCond(5);
				htmltext = "31526-01.htm";
			}
			else if (cond == 5)
			{
				htmltext = "31526-10.htm";
				st.playSound(SOUND_ED_CHIMES05);
			}
			else if (cond == 6)
			{
				htmltext = "31526-15.htm";
			}
		}
		else if ((npcId == Agripel) && (st.getQuestItemsCount(CrossofEinhasad) >= 1))
		{
			if (cond == 6)
			{
				if ((st.getInt("DOMINIC") == 1) && (st.getInt("BENEDICT") == 1))
				{
					htmltext = "31348-02.htm";
					st.setCond(7);
				}
				else
				{
					st.set("AGRIPEL", "1");
					htmltext = "31348-0" + Rnd.get(3) + ".htm";
				}
			}
			else if (cond == 7)
			{
				htmltext = "31348-03.htm";
			}
		}
		else if ((npcId == Dominic) && (st.getQuestItemsCount(CrossofEinhasad) >= 1))
		{
			if (cond == 6)
			{
				if ((st.getInt("AGRIPEL") == 1) && (st.getInt("BENEDICT") == 1))
				{
					htmltext = "31350-02.htm";
					st.setCond(7);
				}
				else
				{
					st.set("DOMINIC", "1");
					htmltext = "31350-0" + Rnd.get(3) + ".htm";
				}
			}
			else if (cond == 7)
			{
				htmltext = "31350-03.htm";
			}
		}
		else if ((npcId == Benedict) && (st.getQuestItemsCount(CrossofEinhasad) >= 1))
		{
			if (cond == 6)
			{
				if ((st.getInt("AGRIPEL") == 1) && (st.getInt("DOMINIC") == 1))
				{
					htmltext = "31349-02.htm";
					st.setCond(7);
				}
				else
				{
					st.set("BENEDICT", "1");
					htmltext = "31349-0" + Rnd.get(3) + ".htm";
				}
			}
			else if (cond == 7)
			{
				htmltext = "31349-03.htm";
			}
		}
		else if (npcId == Innocentin)
		{
			if (cond == 7)
			{
				if (st.getQuestItemsCount(CrossofEinhasad) != 0)
				{
					htmltext = "31328-01.htm";
				}
			}
			else if (cond == 0)
			{
				htmltext = "31328-06.htm";
			}
		}
		return htmltext;
	}
}
