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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.ExShowUsmVideo;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.HtmlUtils;

import org.apache.commons.lang3.ArrayUtils;

public class _10360_CertificationOfFate extends Quest implements ScriptFile
{
	private static final int reins = 30288;
	private static final int raimon = 30289;
	private static final int tobias = 30297;
	private static final int Drikus = 30505;
	private static final int mendius = 30504;
	private static final int gershfin = 32196;
	private static final int elinia = 30155;
	private static final int ershandel = 30158;
	private static final int renpard = 33524;
	private static final int joel = 33516;
	private static final int shachen = 33517;
	private static final int shelon = 33518;
	private static final int[] classesav =
	{
		1,
		7,
		4,
		11,
		15,
		19,
		22,
		26,
		29,
		32,
		35,
		39,
		42,
		45,
		47,
		50,
		54,
		56,
		125,
		126,
		108,
		109,
		110,
		111,
		112,
		113,
		114,
		115,
		116,
		117,
		118,
		136,
		135,
		134,
		132,
		133
	};
	private static final int poslov = 27460;
	private static final int kanilov = 27459;
	private static final int sakum = 27453;
	private static final int Stone = 17587;
	private int killedkanilov;
	private int killedposlov;
	private int killedsakum;
	
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
	
	public _10360_CertificationOfFate()
	{
		super(false);
		addStartNpc(reins);
		addStartNpc(raimon);
		addStartNpc(tobias);
		addStartNpc(Drikus);
		addStartNpc(mendius);
		addStartNpc(gershfin);
		addStartNpc(elinia);
		addStartNpc(ershandel);
		addTalkId(renpard);
		addTalkId(joel);
		addTalkId(shelon);
		addTalkId(shachen);
		addKillId(poslov);
		addKillId(kanilov);
		addKillId(sakum);
		addQuestItem(Stone);
		addLevelCheck(38, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Player player = st.getPlayer();
		if (player.getClassLevel() > 2)
		{
			return htmltext;
		}
		if (event.equalsIgnoreCase("1-3.htm"))
		{
			st.setState(STARTED);
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
			htmltext = "1-3.htm";
		}
		else if (event.equalsIgnoreCase("telep"))
		{
			player.teleToLocation(-24776, 188696, -3993);
			htmltext = "";
		}
		else if (event.equalsIgnoreCase("master"))
		{
			if (st.getPlayer().getRace() == Race.human)
			{
				if (st.getPlayer().isMageClass())
				{
					htmltext = "4-5re.htm";
					st.playSound(SOUND_MIDDLE);
					st.setCond(9);
				}
				else
				{
					htmltext = "4-5r.htm";
					st.playSound(SOUND_MIDDLE);
					st.setCond(8);
				}
			}
			else if (st.getPlayer().getRace() == Race.elf)
			{
				if (st.getPlayer().isMageClass())
				{
					htmltext = "4-5e.htm";
					st.playSound(SOUND_MIDDLE);
					st.setCond(11);
				}
				else
				{
					htmltext = "4-5ew.htm";
					st.playSound(SOUND_MIDDLE);
					st.setCond(10);
				}
			}
			else if (st.getPlayer().getRace() == Race.darkelf)
			{
				htmltext = "4-5t.htm";
				st.playSound(SOUND_MIDDLE);
				st.setCond(12);
			}
			else if (st.getPlayer().getRace() == Race.orc)
			{
				htmltext = "4-5d.htm";
				st.playSound(SOUND_MIDDLE);
				st.setCond(13);
			}
			else if (st.getPlayer().getRace() == Race.dwarf)
			{
				htmltext = "4-5m.htm";
				st.playSound(SOUND_MIDDLE);
				st.setCond(14);
			}
			else if (st.getPlayer().getRace() == Race.kamael)
			{
				htmltext = "4-5g.htm";
				st.playSound(SOUND_MIDDLE);
				st.setCond(15);
			}
		}
		else if (event.equalsIgnoreCase("quest_ac"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
			if (st.getPlayer().getRace() == Race.human)
			{
				if (st.getPlayer().isMageClass())
				{
					htmltext = "0-3re.htm";
				}
				else
				{
					htmltext = "0-3r.htm";
				}
			}
			else if (st.getPlayer().getRace() == Race.elf)
			{
				if (st.getPlayer().isMageClass())
				{
					htmltext = "0-3e.htm";
				}
				else
				{
					htmltext = "0-3ew.htm";
				}
			}
			else if (st.getPlayer().getRace() == Race.darkelf)
			{
				htmltext = "0-3t.htm";
			}
			else if (st.getPlayer().getRace() == Race.orc)
			{
				htmltext = "0-3d.htm";
			}
			else if (st.getPlayer().getRace() == Race.dwarf)
			{
				htmltext = "0-3m.htm";
			}
			else if (st.getPlayer().getRace() == Race.kamael)
			{
				htmltext = "0-3g.htm";
			}
		}
		else if (event.equalsIgnoreCase("3-3.htm"))
		{
			st.setCond(6);
			st.playSound(SOUND_MIDDLE);
			player.sendPacket(new ExShowUsmVideo(ExShowUsmVideo.Q003));
		}
		else if (event.equalsIgnoreCase("2-3.htm"))
		{
			htmltext = "2-3.htm";
			st.playSound(SOUND_MIDDLE);
			st.setCond(4);
		}
		if (event.startsWith("changeclass"))
		{
			int newClassId = 0;
			try
			{
				newClassId = Integer.parseInt(event.substring(12, event.length()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			player.sendPacket(Msg.CONGRATULATIONS_YOU_HAVE_TRANSFERRED_TO_A_NEW_CLASS);
			player.setClassId(newClassId, false, false);
			player.broadcastCharInfo();
			MultiSellHolder.getInstance().SeparateAndSend(85556, st.getPlayer(), 0);
			st.getPlayer().addExpAndSp(2700000, 250000);
			st.giveItems(17822, 40);
			st.giveItems(32777, 1);
			st.giveItems(33800, 1);
			st.giveItems(ADENA_ID, 110000);
			st.exitCurrentQuest(false);
			st.takeAllItems(Stone);
			st.playSound(SOUND_FINISH);
			if (st.getPlayer().getRace() == Race.human)
			{
				if (st.getPlayer().isMageClass())
				{
					htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-6re.htm", st.getPlayer());
				}
				else
				{
					htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-6r.htm", st.getPlayer());
				}
			}
			else if (st.getPlayer().getRace() == Race.elf)
			{
				if (st.getPlayer().isMageClass())
				{
					htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-6e.htm", st.getPlayer());
				}
				else
				{
					htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-6ew.htm", st.getPlayer());
				}
			}
			else if (st.getPlayer().getRace() == Race.darkelf)
			{
				htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-6t.htm", st.getPlayer());
			}
			else if (st.getPlayer().getRace() == Race.orc)
			{
				htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-6d.htm", st.getPlayer());
			}
			else if (st.getPlayer().getRace() == Race.dwarf)
			{
				htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-6m.htm", st.getPlayer());
			}
			else if (st.getPlayer().getRace() == Race.kamael)
			{
				htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-6g.htm", st.getPlayer());
			}
			htmltext = htmltext.replace("%showproof%", HtmlUtils.htmlClassName(newClassId));
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		Player player = st.getPlayer();
		int classid = player.getClassId().getId();
		if ((npcId == raimon) && (st.getPlayer().getRace() == Race.human) && st.getPlayer().isMageClass())
		{
			if (st.isCompleted())
			{
				htmltext = "0re-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()) && ArrayUtils.contains(classesav, classid))
			{
				htmltext = "0-1re.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-3re.htm";
			}
			else if (cond == 9)
			{
				htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-5re.htm", st.getPlayer());
				htmltext = htmltext.replace("%classmaster%", makeMessage(st.getPlayer()));
			}
		}
		else if ((npcId == reins) && (st.getPlayer().getRace() == Race.human) && !st.getPlayer().isMageClass())
		{
			if (st.isCompleted())
			{
				htmltext = "0r-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()) && ArrayUtils.contains(classesav, classid))
			{
				htmltext = "0-1r.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-3r.htm";
			}
			else if (cond == 8)
			{
				htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-5r.htm", st.getPlayer());
				htmltext = htmltext.replace("%classmaster%", makeMessage(st.getPlayer()));
			}
		}
		else if ((npcId == tobias) && (st.getPlayer().getRace() == Race.darkelf))
		{
			if (st.isCompleted())
			{
				htmltext = "0t-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()) && ArrayUtils.contains(classesav, classid))
			{
				htmltext = "0-1t.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-3t.htm";
			}
			else if (cond == 12)
			{
				htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-5t.htm", st.getPlayer());
				htmltext = htmltext.replace("%classmaster%", makeMessage(st.getPlayer()));
			}
		}
		else if ((npcId == Drikus) && (st.getPlayer().getRace() == Race.orc))
		{
			if (st.isCompleted())
			{
				htmltext = "0d-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()) && ArrayUtils.contains(classesav, classid))
			{
				htmltext = "0-1d.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-3d.htm";
			}
			else if (cond == 13)
			{
				htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-5d.htm", st.getPlayer());
				htmltext = htmltext.replace("%classmaster%", makeMessage(st.getPlayer()));
			}
		}
		else if ((npcId == gershfin) && (st.getPlayer().getRace() == Race.kamael))
		{
			if (st.isCompleted())
			{
				htmltext = "0g-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()) && ArrayUtils.contains(classesav, classid))
			{
				htmltext = "0-1g.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-3g.htm";
			}
			else if (cond == 15)
			{
				htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-5g.htm", st.getPlayer());
				htmltext = htmltext.replace("%classmaster%", makeMessage(st.getPlayer()));
			}
		}
		else if ((npcId == elinia) && (st.getPlayer().getRace() == Race.elf) && !st.getPlayer().isMageClass())
		{
			if (st.isCompleted())
			{
				htmltext = "0e-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()) && ArrayUtils.contains(classesav, classid))
			{
				htmltext = "0-1e.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-3e.htm";
			}
			else if (cond == 10)
			{
				htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-5e.htm", st.getPlayer());
				htmltext = htmltext.replace("%classmaster%", makeMessage(st.getPlayer()));
			}
		}
		else if ((npcId == ershandel) && (st.getPlayer().getRace() == Race.elf) && st.getPlayer().isMageClass())
		{
			if (st.isCompleted())
			{
				htmltext = "0ew-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()) && ArrayUtils.contains(classesav, classid))
			{
				htmltext = "0-1ew.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-3ew.htm";
			}
			else if (cond == 11)
			{
				htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-5ew.htm", st.getPlayer());
				htmltext = htmltext.replace("%classmaster%", makeMessage(st.getPlayer()));
			}
		}
		else if ((npcId == mendius) && (st.getPlayer().getRace() == Race.dwarf))
		{
			if (st.isCompleted())
			{
				htmltext = "0m-c.htm";
			}
			else if ((cond == 0) && isAvailableFor(st.getPlayer()) && ArrayUtils.contains(classesav, classid))
			{
				htmltext = "0-1m.htm";
			}
			else if (cond == 1)
			{
				htmltext = "0-3m.htm";
			}
			else if (cond == 14)
			{
				htmltext = HtmCache.getInstance().getNotNull("quests/_10360_CertificationOfFate/0-5m.htm", st.getPlayer());
				htmltext = htmltext.replace("%classmaster%", makeMessage(st.getPlayer()));
			}
		}
		else if (npcId == renpard)
		{
			if (st.isCompleted())
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 1)
			{
				htmltext = "1-1.htm";
			}
			else if (cond == 2)
			{
				htmltext = "1-4.htm";
			}
		}
		else if (npcId == joel)
		{
			if (st.isCompleted())
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 2)
			{
				htmltext = "2-1.htm";
			}
			else if (cond == 3)
			{
				htmltext = "2-2.htm";
			}
			else if (cond == 4)
			{
				htmltext = "2-5.htm";
			}
		}
		else if (npcId == shachen)
		{
			if (st.isCompleted())
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 5)
			{
				htmltext = "3-1.htm";
			}
		}
		else if (npcId == shelon)
		{
			if (st.isCompleted())
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 0)
			{
				htmltext = TODO_FIND_HTML;
			}
			else if (cond == 6)
			{
				htmltext = "4-1.htm";
			}
			else if (cond == 7)
			{
				htmltext = "4-2.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		if ((npcId == poslov) && (st.getCond() == 4))
		{
			++killedposlov;
			if (killedposlov >= 1)
			{
				st.setCond(5);
				st.playSound(SOUND_MIDDLE);
				killedposlov = 0;
			}
		}
		if ((npcId == kanilov) && (st.getCond() == 2))
		{
			++killedkanilov;
			if (killedkanilov >= 1)
			{
				st.setCond(3);
				st.playSound(SOUND_MIDDLE);
				killedkanilov = 0;
			}
		}
		if ((npcId == sakum) && (st.getCond() == 6))
		{
			++killedsakum;
			if (killedsakum >= 1)
			{
				st.setCond(7);
				st.playSound(SOUND_MIDDLE);
				killedsakum = 0;
				st.giveItems(Stone, 1, false);
			}
		}
		return null;
	}
	
	private String makeMessage(Player player)
	{
		ClassId classId = player.getClassId();
		StringBuilder html = new StringBuilder();
		for (ClassId cid : ClassId.VALUES)
		{
			if (cid == ClassId.INSPECTOR)
			{
				continue;
			}
			if (cid.childOf(classId) && (cid.getClassLevel().ordinal() == (classId.getClassLevel().ordinal() + 1)))
			{
				html.append("<a action=\"bypass -h Quest ").append(getClass().getSimpleName()).append(" changeclass ").append(cid.getId()).append(" ").append("\">").append(HtmlUtils.htmlClassName(cid.getId())).append("</a><br>");
			}
		}
		return html.toString();
	}
}
