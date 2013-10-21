package quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ai.NpcArcherAI;
import ai.NpcHealerAI;
import ai.NpcMageAI;
import ai.NpcWarriorAI;

import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.ClassLevel;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

public abstract class SagasSuperclass extends Quest
{
	public SagasSuperclass(boolean party)
	{
		super(party);
	}

	protected int StartNPC = 0;
	protected Race StartRace;

	// massives
	private static List<NpcInstance> _npcWaves = new ArrayList<NpcInstance>();

	private static final int Avanguard_aden = 33407;
	private static final int Avanguard_corpse1 = 33166;
	private static final int Avanguard_corpse2 = 33167;
	private static final int Avanguard_corpse3 = 33168;
	private static final int Avanguard_corpse4 = 33169;
	private static final int Avanguard_member = 33165;
	// instance npc:
	private static final int Avanguard_camptain = 33170;

	private static final int Avanguard_Ellis = 33171;
	private static final int Avanguard_Barton = 33172;
	private static final int Avanguard_Xaok = 33173;
	private static final int Avanguard_Ellia = 33174;
	// npc helpers
	private static final int Van_Archer = 33414;
	private static final int Van_Infantry = 33415;

	// monsters
	private static final int Shaman = 27430;
	private static final int Slayer = 27431;
	private static final int Pursuer = 27432;
	private static final int Priest_Darkness = 27433;
	private static final int Guard_Darkness = 27434;
	// boss
	private static final int Death_wound = 27425;

	// items
	private static final int DeadSoldierOrbs = 17748;
	private static final int Ring_Shout = 17484;
	// onKill won't work here because mobs also killing mobs
	private DeathListener deathListener = new DeathListener();

	protected static Map<Integer, Class<?>> Quests = new HashMap<Integer, Class<?>>();
	static
	{
		Quests.put(10341, _10341_DayOfDestinyHumanFate.class);
		Quests.put(10342, _10342_DayOfDestinyElvenFate.class);
		Quests.put(10343, _10343_DayOfDestinyDarkElfsFate.class);
		Quests.put(10344, _10344_DayOfDestinyOrcsFate.class);
		Quests.put(10345, _10345_DayOfDestinyDwarfsFate.class);
		Quests.put(10346, _10346_DayOfDestinyKamaelsFate.class);
	}

	protected static int[][] QuestRace = new int[][] { { 0 }, { 1 }, { 2 }, { 3 }, { 4 }, { 5 } };

	protected void init()
	{
		addStartNpc(StartNPC);
		addTalkId(StartNPC);
		addTalkId(Avanguard_aden);
		addTalkId(Avanguard_corpse1);
		addTalkId(Avanguard_corpse2);
		addTalkId(Avanguard_corpse3);
		addTalkId(Avanguard_corpse4);
		addTalkId(Avanguard_member);
		addTalkId(Avanguard_camptain);
		addTalkId(Avanguard_Ellis);
		addTalkId(Avanguard_Barton);
		addTalkId(Avanguard_Xaok);
		addTalkId(Avanguard_Ellia);

		addQuestItem(DeadSoldierOrbs);
		addQuestItem(Ring_Shout);

		addLevelCheck(76, 99);
	}

	private static void initFriendNpc(Player player)
	{
		int npcId1 = Integer.parseInt(player.getVar("sel1")); // first chosen
		int npcId2 = Integer.parseInt(player.getVar("sel2")); // second chosen
		int npcId3 = Avanguard_camptain; // adolf
		int npcId4 = Van_Archer; // 3 archers
		int npcId5 = Van_Infantry; // 3 infantry soldiers
		// spawn npc helpers
		NpcInstance sel1 = player.getReflection().addSpawnWithoutRespawn(npcId1, new Location(55976, -175672, -7980, 49151), 0);
		NpcInstance sel2 = player.getReflection().addSpawnWithoutRespawn(npcId2, new Location(56328, -175672, -7980, 49151), 0);
		NpcInstance adolf = player.getReflection().addSpawnWithoutRespawn(npcId3, new Location(56168, -175576, -7974, 49151), 0);
		// archers
		NpcInstance archer1 = player.getReflection().addSpawnWithoutRespawn(npcId4, new Location(56392, -176232, -7980, 49151), 0);
		NpcInstance archer2 = player.getReflection().addSpawnWithoutRespawn(npcId4, new Location(56184, -176168, -7974, 49151), 0);
		NpcInstance archer3 = player.getReflection().addSpawnWithoutRespawn(npcId4, new Location(55976, -176136, -7980, 49151), 0);
		// infantry
		NpcInstance infantry1 = player.getReflection().addSpawnWithoutRespawn(npcId5, new Location(56168, -176712, -7973, 49151), 0);
		NpcInstance infantry2 = player.getReflection().addSpawnWithoutRespawn(npcId5, new Location(55960, -176696, -7973, 49151), 0);
		NpcInstance infantry3 = player.getReflection().addSpawnWithoutRespawn(npcId5, new Location(56376, -176712, -7973, 49151), 0);

		switch(npcId1)
		{
			case 33171:
				sel1.setAI(new NpcHealerAI(sel1));
				break;
			case 33172:
				sel1.setAI(new NpcWarriorAI(sel1));
				break;
			case 33173:
				sel1.setAI(new NpcArcherAI(sel1));
				break;
			case 33174:
				sel1.setAI(new NpcMageAI(sel1));
				break;
			default:
				break;
		} 

		switch(npcId2)
		{
			case 33171:
				sel2.setAI(new NpcHealerAI(sel2));
				break;
			case 33172:
				sel2.setAI(new NpcWarriorAI(sel2));
				break;
			case 33173:
				sel2.setAI(new NpcArcherAI(sel2));
				break;
			case 33174:
				sel2.setAI(new NpcMageAI(sel2));
				break;
			default:
				break;
		}
		adolf.setAI(new NpcWarriorAI(adolf));
		archer1.setAI(new NpcArcherAI(archer1));
		archer2.setAI(new NpcArcherAI(archer2));
		archer3.setAI(new NpcArcherAI(archer3));
		infantry1.setAI(new NpcWarriorAI(infantry1));
		infantry2.setAI(new NpcWarriorAI(infantry2));
		infantry3.setAI(new NpcWarriorAI(infantry3));
		player.unsetVar("sel1");
		player.unsetVar("sel2");
	}

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		String htmltext = event;
		Player player = st.getPlayer();

		if(event.equalsIgnoreCase(StartNPC + "-5.htm"))
		{
			st.setCond(1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("33407-1.htm"))
		{
			st.setCond(2);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("33407-4.htm"))
		{
			st.takeItems(DeadSoldierOrbs, -1);
			st.setCond(4);
		}
		else if(event.equalsIgnoreCase("33166-1.htm"))
		{
			if(player.getVar("orb1") != null)
				return "33166-got.htm";

			player.setVar("orb1", "1", -1);
			st.giveItems(DeadSoldierOrbs, 1);
			st.playSound(SOUND_MIDDLE);
			checkOrbs(player, st);
		}
		else if(event.equalsIgnoreCase("33167-1.htm"))
		{
			if(player.getVar("orb2") != null)
				return "33167-got.htm";

			player.setVar("orb2", "1", -1);
			st.giveItems(DeadSoldierOrbs, 1);
			st.playSound(SOUND_MIDDLE);
			checkOrbs(player, st);
		}
		else if(event.equalsIgnoreCase("33168-1.htm"))
		{
			if(player.getVar("orb3") != null)
				return "33168-got.htm";

			player.setVar("orb3", "1", -1);
			st.giveItems(DeadSoldierOrbs, 1);
			st.playSound(SOUND_MIDDLE);
			checkOrbs(player, st);
		}
		else if(event.equalsIgnoreCase("33169-1.htm"))
		{
			if(player.getVar("orb4") != null)
				return "33168-got.htm";

			player.setVar("orb4", "1", -1);
			st.giveItems(DeadSoldierOrbs, 1);
			st.playSound(SOUND_MIDDLE);
			checkOrbs(player, st);
		}

		else if(event.equalsIgnoreCase("33170-2.htm"))
		{
			st.setCond(6);
			st.playSound(SOUND_MIDDLE);
		}

		else if(event.equalsIgnoreCase("33170-6.htm"))
		{
			st.setCond(10);
			if(st.getQuestItemsCount(Ring_Shout) == 0)
				st.giveItems(Ring_Shout, 1); // ring
			Functions.npcSay(npc, NpcString.THE_CRY_OF_FATE_PENDANT_WILL_BE_HELPFUL_TO_YOU_PLEASE_EQUIP_IT_AND_BRING_OUT_THE_POWER_OF_THE_PENDANT_TO_PREPARE_FOR_THE_NEXT_FIGHT);
		}

		else if(event.equalsIgnoreCase("selection"))
		{
			if(player.getVar("sel1") == null)
			{
				player.setVar("sel1", npc.getNpcId(), -1);
				npc.deleteMe();
				return null;
			}
			if(player.getVar("sel2") == null)
			{
				player.setVar("sel2", npc.getNpcId(), -1);
				npc.deleteMe();
				st.setCond(7);
				return null;
			}
		}

		else if(event.equalsIgnoreCase("enterinstance"))
		{
			if(!_npcWaves.isEmpty())
				_npcWaves.clear();
			player.unsetVar("wave");
			player.unsetVar("sel1");
			player.unsetVar("sel2");
			// maybe take some other quest items?
			st.setCond(5);
			enterInstance(st, 185);
			return null;
		}

		else if(event.equalsIgnoreCase("battleField"))
		{
			// missing parts of the instance:
			// init npcs
			initFriendNpc(player);
			// init waves
			st.startQuestTimer("wave1", 2000);
			player.teleToLocation(56168, -175576, -7974, player.getReflection().getId());
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			st.setCond(8);
			return null;
		}

		else if(event.equalsIgnoreCase("wave1"))
		{
			initWave1(player);
			return null;
		}

		else if(event.equalsIgnoreCase("2"))
		{
			initWave2(player);
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			return null;
		}

		else if(event.equalsIgnoreCase("3"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave3(player);
			return null;
		}
		else if(event.equalsIgnoreCase("4"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave4(player);
			return null;
		}
		else if(event.equalsIgnoreCase("5"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave5(player);
			return null;
		}
		else if(event.equalsIgnoreCase("6"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave6(player);
			return null;
		}
		else if(event.equalsIgnoreCase("8"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave8(player);
			st.startQuestTimer("9", 30000);
			return null;
		}
		else if(event.equalsIgnoreCase("9"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave9(player);
			st.startQuestTimer("10", 30000);
			return null;
		}
		else if(event.equalsIgnoreCase("10"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave10(player);
			st.startQuestTimer("11", 30000);
			return null;
		}
		else if(event.equalsIgnoreCase("11"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave11(player);
			st.startQuestTimer("12", 30000);
			return null;
		}
		else if(event.equalsIgnoreCase("12"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave12(player);
			// boss is comming after we killed all the waves.
			player.unsetVar("wave");
			player.setVar("wave", 12, -1);
			return null;
		}

		else if(event.equalsIgnoreCase("13"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.I_DEATH_WOUND_CHAMPION_OF_SHILEN_SHALL_END_YOUR_WORLD, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave13(player);
			// boss is comming after we killed all the waves.
			player.unsetVar("wave");
			player.setVar("wave", 13, -1);
			return null;
		}
		else if(event.equalsIgnoreCase("firstStandCompleted"))
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_HAVE_STOPPED_THEIR_ATTACK_REST_AND_THEN_SPEAK_WITH_ADOLPH, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			st.setCond(9);
			return null;
		}

		else if(event.equalsIgnoreCase("engagesecondstand"))
		{
			// init second stand
			// init waves
			st.startQuestTimer("8", 30000);
			st.setCond(11);
			player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			initWave7(player);
			return null;
		}

		else if(event.equalsIgnoreCase("secondStandCompleted"))
		{
			player.unsetVar("wave");
			st.setCond(12);
			return null;
		}
		else if(event.startsWith("giveme"))
		{
			if(event.equalsIgnoreCase("givemered"))
				st.giveItems(9570, 1);
			else if(event.equalsIgnoreCase("givemeblue"))
				st.giveItems(9571, 1);
			else if(event.equalsIgnoreCase("givemegreen"))
				st.giveItems(9572, 1);

			int _reqClass = -1;
			for(ClassId cid : ClassId.VALUES)
			{
				if(cid.childOf(player.getClassId()) && cid.getClassLevel().ordinal() == player.getClassId().getClassLevel().ordinal() + 1)
					_reqClass = cid.getId();
			}

			if(_reqClass == -1)
				player.sendMessage("Something gone wrong, please contact administrator!");

			player.setClassId(_reqClass, false, false);
			player.broadcastPacket(new MagicSkillUse(player, player, 5103, 1, 1000, 0));
			st.giveItems(ADENA_ID, 5000000);
			st.addExpAndSp(2050000, 0);
			st.giveItems(9627, 1);
			st.takeItems(DeadSoldierOrbs, -1);
			st.setState(COMPLETED);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
			player.broadcastUserInfo();
			player.sendPacket(new ExShowScreenMessage(NpcString.CONGRATULATIONS_YOU_WILL_NOW_GRADUATE_FROM_THE_CLAN_ACADEMY_AND_LEAVE_YOUR_CURRENT_CLAN_AS_A_GRADUATE_OF_THE_ACADEMY_YOU_CAN_IMMEDIATELY_JOIN_A_CLAN_AS_A_REGULAR_MEMBER_WITHOUT_BEING_SUBJECT_TO_ANY_PENALTIES, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			return StartNPC + "-7.htm";
		}
		return htmltext;

	}

	private void initWave13(Player player)
	{
		// _npcWaves
		player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55976, -177544, -7980, 16383), 0);
		player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(55864, -177544, -8320, 16383), 0);
		player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(55768, -177544, -8320, 16383), 0);
		player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56376, -177544, -8320, 16383), 0);
		player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56336, -177544, -8320, 16383), 0);
		NpcInstance boss = player.getReflection().addSpawnWithoutRespawn(Death_wound, new Location(56168, -177544, -7974, 16383), 0);
		boss.broadcastPacket(new Earthquake(boss.getLoc(), 40, 10));
		boss.addListener(deathListener);

	}

	private void initWave12(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56872, -176648, -7975, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56904, -176744, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(56824, -176728, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(56728, -176664, -7974, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56680, -176776, -7974, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56664, -176712, -7974, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56696, -176632, -7974, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
	}

	private void initWave11(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55512, -176648, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55512, -176712, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(55576, -176696, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(55544, -176776, -7974, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(55432, -176808, -7980, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55432, -176680, -7974, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55592, -176632, -7974, 16383), 0);
		NpcInstance npc8 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55640, -176712, -7974, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		_npcWaves.add(npc8);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
		npc8.addListener(deathListener);
	}

	private void initWave10(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56184, -177672, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56088, -177704, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(56152, -177592, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Priest_Darkness, new Location(56264, -177496, -7978, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56184, -177464, -7974, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56104, -177448, -7974, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56024, -177448, -7980, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
	}

	private void initWave9(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56696, -176744, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56712, -176664, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56776, -176808, -7980, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56696, -176808, -7980, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56616, -176728, -7974, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56600, -176648, -7974, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56584, -176584, -7980, 16383), 0);
		NpcInstance npc8 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56712, -176552, -7980, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		_npcWaves.add(npc8);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
		npc8.addListener(deathListener);
	}

	private void initWave8(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55432, -176680, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55432, -176744, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(55432, -176648, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(55496, -176792, -7976, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(55464, -176680, -7974, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(55576, -176584, -7980, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55416, -176776, -7974, 16383), 0);
		NpcInstance npc8 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55368, -176664, -7974, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		_npcWaves.add(npc8);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
		npc8.addListener(deathListener);
	}

	private void initWave7(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55432, -176680, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55432, -176744, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(55432, -176648, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(55464, -176680, -7974, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55416, -176776, -7974, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55368, -176664, -7974, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
	}

	private void initWave6(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56840, -176712, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56824, -176648, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56824, -176584, -7980, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56872, -176632, -7974, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56904, -176696, -7974, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56904, -176792, -7976, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
	}

	private void initWave5(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55448, -176760, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55464, -176664, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(55560, -176744, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(55512, -176824, -7980, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55448, -176808, -7980, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55400, -176776, -7974, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55384, -176696, -7974, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
	}

	private void initWave4(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56216, -177624, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Shaman, new Location(56088, -177624, -7975, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56168, -177544, -7975, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56296, -177512, -7980, 16383), 0);
		NpcInstance npc5 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56376, -177512, -7980, 16383), 0);
		NpcInstance npc6 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(55944, -177512, -7980, 16383), 0);
		NpcInstance npc7 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56296, -177448, -7979, 16383), 0);
		NpcInstance npc8 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(55992, -177384, -7980, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		_npcWaves.add(npc5);
		_npcWaves.add(npc6);
		_npcWaves.add(npc7);
		_npcWaves.add(npc8);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		npc5.addListener(deathListener);
		npc6.addListener(deathListener);
		npc7.addListener(deathListener);
		npc8.addListener(deathListener);
	}

	private void initWave3(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56808, -176680, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56824, -176792, -7979, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56760, -176712, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56824, -176584, -7980, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
	}

	private void initWave2(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56808, -176680, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56824, -176792, -7979, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56760, -176712, -7974, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56824, -176584, -7980, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
	}

	private void initWave1(Player player)
	{
		// _npcWaves
		NpcInstance npc1 = player.getReflection().addSpawnWithoutRespawn(Slayer, new Location(56168, -177592, -7974, 16383), 0);
		NpcInstance npc2 = player.getReflection().addSpawnWithoutRespawn(Pursuer, new Location(56248, -177576, -7974, 16383), 0);
		NpcInstance npc3 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56072, -177544, -7977, 16383), 0);
		NpcInstance npc4 = player.getReflection().addSpawnWithoutRespawn(Guard_Darkness, new Location(56312, -177576, -7980, 16383), 0);
		_npcWaves.add(npc1);
		_npcWaves.add(npc2);
		_npcWaves.add(npc3);
		_npcWaves.add(npc4);
		npc1.addListener(deathListener);
		npc2.addListener(deathListener);
		npc3.addListener(deathListener);
		npc4.addListener(deathListener);
		player.setVar("wave", 1, -1);
	}

	private class DeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if(self.isNpc())
			{
				NpcInstance npc = (NpcInstance) self;
				if(_npcWaves.contains(npc) || npc.getNpcId() == Death_wound)
				{
					// we need to find our player in this instance, let's search
					for(Player p : npc.getReflection().getPlayers())
					{
						// the only player inside is ours
						if(p == null) // maybe left the instance
							continue;
						QuestState st = findQuest(p);
						onKill(npc, st);
					}
				}
			}
		}
	}

	@Override
	public String onKill(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int wave = Integer.parseInt(player.getVar("wave"));
		if(npc.getNpcId() == Death_wound)
		{
			player.sendPacket(new ExShowScreenMessage(NpcString.AGH_HUMANS_HA_IT_DOES_NOT_MATTER_YOUR_WORLD_WILL_END_ANYWAYS, 10000, ScreenMessageAlign.MIDDLE_CENTER, true));
			st.startQuestTimer("secondStandCompleted", 1000);
			return null;
		}

		if(checkWave(player, npc, wave, st))
		{
			return null;
		}
		return null;
	}

	public static boolean checkWave(Player player, NpcInstance npc, int waveId, QuestState st)
	{
		if(_npcWaves.contains(npc))
		{
			_npcWaves.remove(npc);
		}
		if(waveId < 7) // after the first stand we go on timers anyway
		{
			if(_npcWaves.isEmpty())
			{
				int _nextWave = waveId + 1;
				player.setVar("wave", _nextWave, -1);
				if(_nextWave == 7)
					st.startQuestTimer("firstStandCompleted", 5000);
				else
					st.startQuestTimer("" + _nextWave + "", 7000);
			}
		}
		if(waveId == 12 && _npcWaves.isEmpty())
		{
			st.startQuestTimer("13", 2000);
		}
		return true;
	}

	private static void checkOrbs(Player player, QuestState st)
	{
		if(st.getQuestItemsCount(DeadSoldierOrbs) == 4)
		{
			st.playSound(SOUND_MIDDLE);
			st.setCond(3);
			player.unsetVar("orb1");
			player.unsetVar("orb2");
			player.unsetVar("orb3");
			player.unsetVar("orb4");
		}
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		int id = st.getState();
		int npcId = npc.getNpcId();
		Player player = st.getPlayer();
		String htmltext = "noquest";
		if(!canTakeQuest(player))
			return StartNPC + ".htm";

		if(id == COMPLETED)
		{
			//QUEST COMPLETED BUT PLAYER WANT TO TRICLASS WITH ANOTHER SUBCLASS
			st.exitCurrentQuest(true);
		}

		if(npcId == StartNPC)
		{
			if(cond == 0)
				return StartNPC + "-1.htm";
			else if(cond == 1)
				return StartNPC + "-got.htm";
			else if(cond == 13)
				return StartNPC + "-6.htm";
		}
		else if(npcId == Avanguard_aden)
		{
			if(cond == 1)
				return "33407.htm";
			else if(cond == 2)
				return "33407-2.htm";
			else if(cond == 3)
				return "33407-3.htm";
		}
		else if(npcId == Avanguard_corpse1)
		{
			if(cond == 2)
				return "33166.htm";
		}
		else if(npcId == Avanguard_corpse2)
		{
			if(cond == 2)
				return "33167.htm";
		}
		else if(npcId == Avanguard_corpse3)
		{
			if(cond == 2)
				return "33168.htm";
		}
		else if(npcId == Avanguard_corpse4)
		{
			if(cond == 2)
				return "33169.htm";
		}
		else if(npcId == Avanguard_member)
		{
			if(cond >= 4)
				return "33165.htm";
		}

		else if(npcId == Avanguard_camptain)
		{
			if(cond == 5)
				return "33170-1.htm";
			else if(cond == 7)
				return "33170-3.htm";
			else if(cond == 9)
				return "33170-5.htm";
			else if(cond == 10)
				return "33170-7.htm";
			else if(cond == 12)
			{
				st.setCond(13);
				st.giveItems(736, 1); // SOE
				npc.broadcastPacket(new SocialAction(npc.getObjectId(), 3));
				return "33170-8.htm";
			}
		}

		else if(npcId == Avanguard_Ellis)
		{
			if(cond == 6)
				return "33171-1.htm";
		}

		else if(npcId == Avanguard_Barton)
		{
			if(cond == 6)
				return "33172-1.htm";
		}

		else if(npcId == Avanguard_Xaok)
		{
			if(cond == 6)
				return "33173-1.htm";
		}

		else if(npcId == Avanguard_Ellia)
		{
			if(cond == 6)
				return "33174-1.htm";
		}
		return htmltext;
	}

	private boolean canTakeQuest(Player player)
	{
		if(player == null)
			return false;
		if(player.getLevel() < 76)
			return false;
		if(!player.getClassId().isOfLevel(ClassLevel.Third))
			return false;
		if(player.getClassId().getRace() != StartRace)
			return false;
		return true;
	}

	public QuestState findQuest(Player player)
	{
		QuestState st = null;
		st = player.getQuestState(Quests.get(questId()));
		if(st != null)
		{
			return st;
		}
		return null;
	}
	
	public int questId()
	{
		return 0;
	}
}
