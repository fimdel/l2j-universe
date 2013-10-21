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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.scripts.ScriptFile;

public class _111_ElrokianHuntersProof extends Quest implements ScriptFile
{
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
	
	private static final int Marquez = 32113;
	private static final int Asamah = 32115;
	private static final int Kirikachin = 32116;
	private static final int[] Velociraptor =
	{
		22196,
		22197,
		22198,
		22218,
		22223
	};
	private static final int[] Ornithomimus =
	{
		22200,
		22201,
		22202,
		22219,
		22224,
		22744,
		22742
	};
	private static final int[] Deinonychus =
	{
		22203,
		22204,
		22205,
		22220,
		22225,
		22745,
		22743
	};
	private static final int[] Pachycephalosaurus =
	{
		22208,
		22209,
		22210,
		22221,
		22226
	};
	private static final int DiaryFragment = 8768;
	private static final int OrnithomimusClaw = 8770;
	private static final int DeinonychusBone = 8771;
	private static final int PachycephalosaurusSkin = 8772;
	private static final int ElrokianTrap = 8763;
	private static final int TrapStone = 8764;
	
	public _111_ElrokianHuntersProof()
	{
		super(true);
		addStartNpc(Marquez);
		addTalkId(Asamah);
		addTalkId(Kirikachin);
		addKillId(Velociraptor);
		addKillId(Ornithomimus);
		addKillId(Deinonychus);
		addKillId(Pachycephalosaurus);
		addQuestItem(DiaryFragment, OrnithomimusClaw, DeinonychusBone, PachycephalosaurusSkin);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		int cond = st.getCond();
		Player player = st.getPlayer();
		if (event.equalsIgnoreCase("marquez_q111_2.htm") && (cond == 0))
		{
			st.setCond(2);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("asamah_q111_2.htm"))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("marquez_q111_4.htm"))
		{
			st.setCond(4);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("marquez_q111_6.htm"))
		{
			st.setCond(6);
			st.takeItems(DiaryFragment, -1);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("kirikachin_q111_2.htm"))
		{
			st.setCond(7);
			player.sendPacket(new PlaySound("EtcSound.elcroki_song_full"));
		}
		else if (event.equalsIgnoreCase("kirikachin_q111_3.htm"))
		{
			st.setCond(8);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("asamah_q111_4.htm"))
		{
			st.setCond(9);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("asamah_q111_5.htm"))
		{
			st.setCond(10);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("asamah_q111_7.htm"))
		{
			st.takeItems(OrnithomimusClaw, -1);
			st.takeItems(DeinonychusBone, -1);
			st.takeItems(PachycephalosaurusSkin, -1);
			st.setCond(12);
			st.playSound(SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("asamah_q111_8.htm"))
		{
			st.giveItems(ADENA_ID, 4257180);
			st.addExpAndSp(19973970, 22122270);
			st.giveItems(ElrokianTrap, 1);
			st.giveItems(TrapStone, 100);
			st.setState(COMPLETED);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		if (npcId == Marquez)
		{
			if ((st.getPlayer().getLevel() >= 75) && (cond == 0))
			{
				htmltext = "marquez_q111_1.htm";
			}
			else if ((st.getPlayer().getLevel() < 75) && (cond == 0))
			{
				htmltext = "marquez_q111_0.htm";
			}
			else if (cond == 3)
			{
				htmltext = "marquez_q111_3.htm";
			}
			else if (cond == 5)
			{
				htmltext = "marquez_q111_5.htm";
			}
		}
		else if (npcId == Asamah)
		{
			if (cond == 2)
			{
				htmltext = "asamah_q111_1.htm";
			}
			else if (cond == 8)
			{
				htmltext = "asamah_q111_3.htm";
			}
			else if (cond == 11)
			{
				htmltext = "asamah_q111_6.htm";
			}
		}
		else if (npcId == Kirikachin)
		{
			if (cond == 6)
			{
				htmltext = "kirikachin_q111_1.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		int id = npc.getNpcId();
		int cond = st.getCond();
		if (cond == 4)
		{
			for (int i : Velociraptor)
			{
				if ((id == i) && (st.getQuestItemsCount(DiaryFragment) < 50))
				{
					st.giveItems(DiaryFragment, 1, false);
					if (st.getQuestItemsCount(DiaryFragment) == 50)
					{
						st.playSound(SOUND_MIDDLE);
						st.setCond(5);
						return null;
					}
					st.playSound(SOUND_ITEMGET);
				}
			}
		}
		else if (cond == 10)
		{
			for (int i : Ornithomimus)
			{
				if ((id == i) && (st.getQuestItemsCount(OrnithomimusClaw) < 10))
				{
					st.giveItems(OrnithomimusClaw, 1, false);
					return null;
				}
			}
			for (int i : Deinonychus)
			{
				if ((id == i) && (st.getQuestItemsCount(DeinonychusBone) < 10))
				{
					st.giveItems(DeinonychusBone, 1, false);
					return null;
				}
			}
			for (int i : Pachycephalosaurus)
			{
				if ((id == i) && (st.getQuestItemsCount(PachycephalosaurusSkin) < 10))
				{
					st.giveItems(PachycephalosaurusSkin, 1, false);
					return null;
				}
			}
			if ((st.getQuestItemsCount(OrnithomimusClaw) >= 10) && (st.getQuestItemsCount(DeinonychusBone) >= 10) && (st.getQuestItemsCount(PachycephalosaurusSkin) >= 10))
			{
				st.setCond(11);
				st.playSound(SOUND_MIDDLE);
				return null;
			}
		}
		return null;
	}
}
