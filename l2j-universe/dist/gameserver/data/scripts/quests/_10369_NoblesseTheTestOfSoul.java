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

import org.apache.commons.lang3.ArrayUtils;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.listener.actor.OnMagicUseListener;
import lineage2.gameserver.model.*;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

/**
* @author Jocy,vegax
* @version $Revision: 1.0 $
*/
public class _10369_NoblesseTheTestOfSoul extends Quest implements ScriptFile, OnMagicUseListener
{

	//npc
	private static final int Cerenas = 31281;
	private static final int EvasAltar = 33686;
	private static final int Lanya = 33696;
	private static final int FlameFlower = 27483;
	private static final int Helping = 19293;
	private static final int HelpingRune = 19293;
	private static final int HelpingTree = 27486;
	private static final int SeedWaler = 18678;


	//monster
	private static final int OneWho = 27482;
	private static final int[] HotSprings =
		{
		21320,
		21322,
		21323
		};
	private static final int[] IsleOf =
		{
		22262,
		22263,
		22264
		};

	//skill
	private static final int Trower = 9442;
	//private static final int EmptyHotSkill = 9443;
	private static final int HelpingS = 9444;
	//private static final int SummonStoneSkill = 9445;

	//item
	private static final int HelpingSeed = 34961;
	private static final int Ashes = 34962;
	private static final int SOEAdneCastle = 34981;
	private static final int SackContaining = 34913;
	private static final int HfCeoW = 34892; //isle item
	private static final int SOEIsleofPrayer = 34980;
	private static final int EnergyOfFire = 34891;
	private static final int SOEForgeOfTheGods = 34979;
	private static final int Trowel = 34890;
	private static final int HardLeather = 34889;
	private static final int SOEHotSprings = 34978;
	private static final int SummoningStone = 34912;
	private static final int EmptyHot = 34887;
	private static final int HotFull = 34888;
	private static final int NovellProphecy = 34886;
	private static int NoblessTiara = 7694;
	private static int DimensionalDiamond = 7562;
	private static int NoblessePrivi = 34983;

	public _10369_NoblesseTheTestOfSoul()
	{
		super(false);
		addStartNpc(Cerenas);
		addTalkId(Lanya, EvasAltar, Cerenas, SeedWaler, Helping, HelpingRune);
		addQuestItem(NoblessTiara, DimensionalDiamond, NoblessePrivi, HelpingSeed, Ashes, NovellProphecy, SOEAdneCastle, SackContaining, HfCeoW, SOEIsleofPrayer, EnergyOfFire, SOEForgeOfTheGods, Trowel, HardLeather, SOEHotSprings, SummoningStone, EmptyHot, HotFull);
		addKillId(OneWho);
		addKillId(HotSprings);
		addKillId(IsleOf);
		addLevelCheck(75, 99);
		addSubClassCheck();
		addQuestCompletedCheck(_10385_RedThreadofFate.class);
	}

	@Override
	public void onShutdown()
	{
	}

	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
	}

	@Override
	public void onReload()
	{
		CharListenerList.removeGlobal(this);
		CharListenerList.addGlobal(this);
	}

	@Override
	public void onMagicUse(Creature actor, Skill skill, Creature target, boolean alt)
	{
		if (actor == null || !actor.isPlayer() || target == null || !target.isNpc())
		{
			return;
		}
		QuestState st = ((Player) actor).getQuestState(_10369_NoblesseTheTestOfSoul.class);

		if (st == null)
			return;
		NpcInstance npc = (NpcInstance) target;
		Player player = st.getPlayer();
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		switch (skill.getId())
		{
			case HelpingS:
				if (npcId == Helping && cond == 16) //Aden Castle
				{
					ItemFunctions.removeItem(st.getPlayer(), HelpingSeed, 1L, true);
					/*NpcInstance mob =*/ st.addSpawn(HelpingTree, 148216, 14856, -1393);
					st.giveItems(Ashes, 1);
					st.playSound("ItemSound.quest_middle");
					st.setCond(17);
				}
				break;
			case Trower:
				if ((st.getCond() == 10) && (npcId == FlameFlower)  && !npc.isDead())
				{
					st.giveItems(EnergyOfFire, 1);
					st.playSound("ItemSound.quest_itemget");
					npc.doDie(player);
				}
				if ((st.getQuestItemsCount(EnergyOfFire) >= 5))
				{
					st.playSound(SOUND_MIDDLE);
					st.setCond(11);
				}
		}
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st) 
	{
		int npcId = npc.getNpcId();
		int cond = st.getCond();
		Player player = st.getPlayer();
		if (player == null)
		{
			return null;
		}
		if (npcId == OneWho && cond == 2)
		{
			st.giveItems(NovellProphecy, 1);
			st.playSound("ItemSound.quest_itemget");
			st.setCond(3);
		}
		if ((st.getCond() == 8 ) && ArrayUtils.contains(HotSprings, npcId) && Rnd.chance(40))
		{
			st.giveItems(HardLeather, 1);
			st.playSound("ItemSound.quest_itemget");
		}
		if ((st.getQuestItemsCount(HardLeather) >= 10))
		{
			st.setCond(9);
			st.playSound(SOUND_MIDDLE);
		}

		if ((st.getCond() == 12) && ArrayUtils.contains(IsleOf, npcId) && Rnd.chance(40))
		{
			st.giveItems(HfCeoW , 1);
			st.playSound("ItemSound.quest_itemget");
		}
		if ((st.getQuestItemsCount(HfCeoW) >= 10))
		{
			st.setCond(13);
			st.playSound(SOUND_MIDDLE);
		}
		return null;
	}
	
	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		String htmltext = event;
		int cond = st.getCond();
		if (cond == 0 && event.equalsIgnoreCase("showMovie"))
		{
			st.setState(STARTED);
			st.setCond(1);
			st.getPlayer().showQuestMovie(SceneMovie.sc_noble_opening);
			st.playSound("ItemSound.quest_accept");
			st.startQuestTimer("", 25000);
			htmltext = null;
		}
		else if (cond == 1 && event.equalsIgnoreCase("Cerenas-6.htm"))
		{
			st.setCond(2);
			st.playSound("ItemSound.quest_middle");
		}
		else if (cond == 3 && event.equalsIgnoreCase("showMovie"))
		{
			st.getPlayer().showQuestMovie(SceneMovie.sc_noble_ending);
			st.playSound("ItemSound.quest_middle");
			st.startQuestTimer("", 25000);
			htmltext = null;
			st.takeItems(NovellProphecy, -1);
			st.setCond(4);
		}
		else if (cond == 4 && event.equalsIgnoreCase("Cerenas-10.htm"))
		{
			st.playSound("ItemSound.quest_middle");
			player.teleToLocation(new Location(-122136, -116552, -5797));
			st.setCond(5);
		}
		else if (cond == 5 && event.equalsIgnoreCase("Evas-3.htm"))
		{
			st.giveItems(EmptyHot, 1);
			st.giveItems(SummoningStone, 1);
			st.giveItems(SOEHotSprings, 1);
			st.playSound("ItemSound.quest_middle");
			st.setCond(6);
		}
		else if (cond == 7 && event.equalsIgnoreCase("Lanya-2.htm"))
		{
			st.takeItems(HotFull, -1);
			st.playSound("ItemSound.quest_middle");
			st.setCond(8);
		}
		else if (cond == 9 && event.equalsIgnoreCase("Lanya-5.htm"))
		{
			st.takeItems(HardLeather, -10);
			st.giveItems(Trowel, 1);
			st.giveItems(SOEForgeOfTheGods, 1);
			st.playSound("ItemSound.quest_middle");
			st.setCond(10);
		}
		else if (cond == 11 && event.equalsIgnoreCase("Lanya-8.htm"))
		{
			st.takeItems(EnergyOfFire, -5);
			st.takeItems(Trowel, -1);
			st.giveItems(SOEIsleofPrayer, 1);
			st.playSound("ItemSound.quest_middle");
			st.setCond(12);
		}
		else if (cond == 15 && event.equalsIgnoreCase("Evas-5.htm"))
		{
			st.giveItems(HelpingSeed, 1);
			st.giveItems(SOEAdneCastle, 1);
			st.playSound("ItemSound.quest_middle");
			st.setCond(16);
		}
		else if (event.equalsIgnoreCase("Evas-7.htm"))
		{
			if (st.getPlayer().getLevel() >= 75)
			{
				st.takeItems(Ashes, -1);
				st.takeItems(SummoningStone, -1);
				st.giveItems(7694, 1);
				st.giveItems(7562, 10);
				st.giveItems(34983, 1);
				st.addExpAndSp(12625440, 0);
				st.setState(COMPLETED);
				st.exitCurrentQuest(false);
				Olympiad.addNoble(st.getPlayer());
				st.getPlayer().setNoble(true);
				st.getPlayer().updatePledgeClass();
				st.getPlayer().updateNobleSkills();
				st.getPlayer().sendSkillList();
				st.getPlayer().broadcastUserInfo();
				st.playSound(SOUND_FINISH);
			}
		}
		return htmltext;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		Player player = st.getPlayer();
		int cond = st.getCond();
		int npcId = npc.getNpcId();
		switch (npcId)
		{
			case Cerenas:
				if (cond == 0)
				{
					if (st.getPlayer().isBaseClassActive())
					{
						return "Subclass only!";
					}
					if (st.getPlayer().getSubClassList().size() < 2)
					{
						return "You do not have subclass!";
					}
					if (st.getPlayer().getLevel() >= 75)
					{
						htmltext = "Cerenas-1.htm";
					}
					else
					{
						htmltext = "Cerenas-no.htm";
						st.exitCurrentQuest(true);
					}
				}
				else if (cond == 1)
				{
					htmltext = "Cerenas-4.htm";
				}
				else if (cond == 3)
				{
					htmltext = "Cerenas-7.htm";
				}
				else if (cond == 4)
				{
					htmltext = "Cerenas-8.htm";
				}
				else if (cond == 14)
				{
					htmltext = "Cerenas-11.htm";
					st.playSound("ItemSound.quest_middle");
					player.teleToLocation(new Location(-122136, -116552, -5797));
					st.setCond(15);
				}
				else if (cond == 17)
				{
					htmltext = "Cerenas-12.htm";
					st.playSound("ItemSound.quest_middle");
					player.teleToLocation(new Location(-122136, -116552, -5797));
					st.setCond(18);
				}
				break;
			case Lanya:
				if (cond == 6 && st.getQuestItemsCount(HotFull) == 1)
				{
					st.playSound("ItemSound.quest_middle");
					st.setCond(7);
					htmltext = "Lanya-1.htm";
				}
				else if (cond == 7)
				{
					htmltext = "Lanya-1.htm";
				}
				else if (cond == 9)
				{
					htmltext = "Lanya-4.htm";
				}
				else if (cond == 10)
				{
					htmltext = "Lanya-6.htm";
				}
				else if (cond == 11)
				{
					htmltext = "Lanya-7.htm";
				}
				else if (cond == 13)
				{
					htmltext = "Lanya-9.htm";
					st.takeItems(HfCeoW, -10);
					st.playSound("ItemSound.quest_middle");
					st.setCond(14);
				}
				break;
			case EvasAltar:
				if (cond == 5)
				{
					htmltext = "Evas-1.htm";
				}	
				if (cond == 15)
				{
					htmltext = "Evas-4.htm";
				}
				if (cond == 18)
				{
					htmltext = "Evas-6.htm";
				}
				break;
		}
		return htmltext;
	}
}