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

public class _10374_ThatPlaceSuccubus extends Quest implements ScriptFile
{
	private static final int NPC_ANDREI = 31292;
	private static final int NPC_AGNES = 31588;
	private static final int NPC_ZENYA = 32140;
	private static final int MirageFighter = 23186;
	private static final int WarriorMirage = 23187;
	private static final int ShooterMirage = 23188;
	private static final int ShamanMirage = 23189;
	private static final int MartyrMirage = 23190;
	public static final String Mirage_Fighter = "MirageFighter";
	public static final String Warrior_Mirage = "WarriorMirage";
	public static final String Shooter_Mirage = "ShooterMirage";
	public static final String Shaman_Mirage = "ShamanMirage";
	public static final String Martyr_Mirage = "MartyrMirage";
	
	public _10374_ThatPlaceSuccubus()
	{
		super(false);
		addStartNpc(NPC_ANDREI);
		addTalkId(NPC_AGNES, NPC_ZENYA);
		addKillNpcWithLog(3, Mirage_Fighter, 15, MirageFighter);
		addKillNpcWithLog(3, Warrior_Mirage, 10, WarriorMirage);
		addKillNpcWithLog(3, Shooter_Mirage, 5, ShooterMirage);
		addKillNpcWithLog(3, Shaman_Mirage, 5, ShamanMirage);
		addKillNpcWithLog(3, Martyr_Mirage, 5, MartyrMirage);
		addLevelCheck(80, 99);
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		if (event.equalsIgnoreCase("31292-07.htm"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.playSound(SOUND_ACCEPT);
		}
		if (event.equalsIgnoreCase("31588-02.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_MIDDLE);
		}
		if (event.equalsIgnoreCase("32140-02.htm"))
		{
			st.setCond(3);
			st.playSound(SOUND_MIDDLE);
		}
		return event;
	}
	
	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		if (npcId == NPC_ANDREI)
		{
			switch (st.getState())
			{
				case COMPLETED:
					htmltext = "31292-06.htm";
					break;
				case CREATED:
					if (st.getPlayer().getLevel() >= 80)
					{
						if (st.getPlayer().getClassId().level() >= 4)
						{
							htmltext = "31292-01.htm";
						}
						else
						{
							htmltext = "31292-04.htm";
							st.exitCurrentQuest(true);
						}
					}
					else
					{
						htmltext = "31292-05.htm";
					}
					break;
				case STARTED:
					if (st.getCond() == 1)
					{
						htmltext = "31292-08.htm";
					}
			}
		}
		else if (npcId == NPC_AGNES)
		{
			if (st.isStarted())
			{
				if (st.getCond() == 1)
				{
					htmltext = "31588-01.htm";
				}
				else if (st.getCond() == 2)
				{
					htmltext = "31588-03.htm";
				}
			}
		}
		else if (npcId == NPC_ZENYA)
		{
			if (st.isStarted())
			{
				if (st.getCond() == 2)
				{
					htmltext = "32140-01.htm";
				}
				else if (st.getCond() == 3)
				{
					htmltext = "32140-03.htm";
				}
				else if (st.getCond() == 4)
				{
					htmltext = "32140-04.htm";
					st.addExpAndSp(23747100, 27618200);
					st.giveItems(57, 500560);
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(false);
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		if (st.getCond() != 3)
		{
			return null;
		}
		if (updateKill(npc, st))
		{
			st.unset(Mirage_Fighter);
			st.unset(Warrior_Mirage);
			st.unset(Shooter_Mirage);
			st.unset(Shaman_Mirage);
			st.unset(Martyr_Mirage);
			st.setCond(4);
			st.playSound(SOUND_MIDDLE);
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
