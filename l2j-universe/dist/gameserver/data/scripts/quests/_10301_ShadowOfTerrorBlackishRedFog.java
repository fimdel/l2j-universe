package quests;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

public class _10301_ShadowOfTerrorBlackishRedFog extends Quest implements ScriptFile
{
	//massives && single Npc
	private static List<NpcInstance> _Priests = new ArrayList<NpcInstance>();
	private static List<NpcInstance> _MobsToDie = new ArrayList<NpcInstance>();
	private static NpcInstance _mainZhrec = null;
	//npc & items
	private static final int RADA = 33100;
	private static final int SLAKI = 32893;
	private static final int CRYSTALL = 17604;
	private static final int SPIRIT_ITEM = 17588;
	private static final int LARGE_VERDANT_WILDS = 33489;

	private static final int WISP = 32938;
	private boolean _spawned = false;

	public _10301_ShadowOfTerrorBlackishRedFog()
	{
		super(false);
		addStartNpc(RADA);
		addTalkId(RADA);
		addTalkId(SLAKI);
		addKillId(WISP);
		addQuestItem(CRYSTALL);
		addQuestItem(SPIRIT_ITEM);
		addSkillUseId(LARGE_VERDANT_WILDS);
		
		addLevelCheck(90, 99);
	}

	@Override
	public String onSkillUse(NpcInstance npc, Skill skill, QuestState qs)
	{
		int skillId = skill.getId();
		int npcId = npc.getNpcId();
		if (skillId == 12011 && npcId == LARGE_VERDANT_WILDS && _spawned == false)
		{
			NpcUtils.spawnSingle(WISP, Location.findPointToStay(qs.getPlayer().getLoc(), 50, 100, 0), 120000);
			NpcUtils.spawnSingle(WISP, Location.findPointToStay(qs.getPlayer().getLoc(), 50, 100, 0), 120000);
		}
		return null;
	}
	

	@Override
	public String onEvent(String event, QuestState st, NpcInstance npc)
	{
		Player player = st.getPlayer();
		if(player == null)
		{
			return null;
		}
		if(event.equalsIgnoreCase("33100-8.htm"))
		{
			st.setCond(2);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			st.giveItems(CRYSTALL, 8);
		}
		if(event.equalsIgnoreCase("33100-10.htm"))
		{
			st.giveItems(CRYSTALL, 3);
		}
		if(event.equalsIgnoreCase("32893-6.htm"))
		{
			st.giveItems(57, 1863420);
			st.giveItems(17380, 1);
			st.addExpAndSp(26920620, 11389320);
			st.getPlayer().unsetVar("instance10301");
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		if(event.equalsIgnoreCase("enterInstance"))
		{
			enterInstance(st, 192); //instance ID
			st.startQuestTimer("initNpcs", 5000); //delay as in movie
			return null;
		}
		else if(event.equalsIgnoreCase("initNpcs"))
		{
			_mainZhrec = player.getReflection().addSpawnWithoutRespawn(33361, new Location(183989, 85803, -7752, 22740), 0);
			player.getReflection().addSpawnWithoutRespawn(33362, new Location(183961, 85734, -7752, 19716), 0);

			NpcInstance _npc2 = player.getReflection().addSpawnWithoutRespawn(33361, new Location(183993, 86055, -7752, 42404), 0);
			NpcInstance _npc3 = player.getReflection().addSpawnWithoutRespawn(33361, new Location(183767, 85872, -7752, 2944), 0);
			NpcInstance _npc4 = player.getReflection().addSpawnWithoutRespawn(33361, new Location(183913, 86084, -7752, 49296), 0);
			NpcInstance _npc5 = player.getReflection().addSpawnWithoutRespawn(33361, new Location(183763, 85967, -7752, 64408), 0);
			NpcInstance _npc6 = player.getReflection().addSpawnWithoutRespawn(33361, new Location(183813, 85799, -7752, 9620), 0);
			NpcInstance _npc7 = player.getReflection().addSpawnWithoutRespawn(33361, new Location(183904, 85780, -7752, 15428), 0);
			NpcInstance _npc8 = player.getReflection().addSpawnWithoutRespawn(33361, new Location(183825, 86049, -7752, 55496), 0);
			NpcInstance _npc9 = player.getReflection().addSpawnWithoutRespawn(33361, new Location(184042, 85974, -7752, 35684), 0);
			NpcInstance _npc10 = player.getReflection().addSpawnWithoutRespawn(33361, new Location(184048, 85887, -7752, 31132), 0);
			
			NpcInstance _npc13 = player.getReflection().addSpawnWithoutRespawn(33364, new Location(183914, 85885, -7752, 44604), 0);
			NpcInstance _npc18 = player.getReflection().addSpawnWithoutRespawn(33365, new Location(183952, 85953, -7752, 2932), 0);
			NpcInstance _npc20 = player.getReflection().addSpawnWithoutRespawn(33366, new Location(183872, 85953, -7752, 11224), 0);
			NpcInstance _npc16 = player.getReflection().addSpawnWithoutRespawn(33367, new Location(183904, 85969, -7752, 59344), 0);
			NpcInstance _npc19 = player.getReflection().addSpawnWithoutRespawn(33368, new Location(183952, 85905, -7752, 30144), 0);
			NpcInstance _npc17 = player.getReflection().addSpawnWithoutRespawn(33369, new Location(183920, 85953, -7752, 16456), 0);
			NpcInstance _npc15 = player.getReflection().addSpawnWithoutRespawn(33370, new Location(183888, 85921, -7752, 7764), 0);
			NpcInstance _npc14 = player.getReflection().addSpawnWithoutRespawn(33371, new Location(183856, 85905, -7752, 25720), 0);
			NpcInstance _npc12 = player.getReflection().addSpawnWithoutRespawn(33372, new Location(183922, 85916, -7752, 56760), 0);
			
			_Priests.add(_npc2);
			_Priests.add(_npc3);
			_Priests.add(_npc4);
			_Priests.add(_npc5);
			_Priests.add(_npc6);
			_Priests.add(_npc7);
			_Priests.add(_npc8);
			_Priests.add(_npc9);
			_Priests.add(_npc10);

			_MobsToDie.add(_npc12);
			_MobsToDie.add(_npc13);
			_MobsToDie.add(_npc14);
			_MobsToDie.add(_npc15);
			_MobsToDie.add(_npc16);
			_MobsToDie.add(_npc17);
			_MobsToDie.add(_npc18);
			_MobsToDie.add(_npc19);
			_MobsToDie.add(_npc20);

			st.startQuestTimer("startDiologue1", 5000); //delay as in movie
			return null;
		}

		else if(event.equalsIgnoreCase("startDiologue1"))
		{
			//			Functions.npcSay(_mainZhrec, NpcString.DRINK_THE_SACRIFICE_OF_BLOOD_THAT_WE_HAVE);
			st.startQuestTimer("startDiologue2", 3000); //dlg 2
			return null;
		}
		else if(event.equalsIgnoreCase("startDiologue2"))
		{
			for(NpcInstance npcP : _Priests)
			{
				if(Rnd.chance(50))
				{
					Functions.npcSay(npcP, NpcString.AND_BRING_DOWN_THE_HAMMER_OF_JUSTICE);
				}
				else
				{
					Functions.npcSay(npcP, NpcString.FOR_THE_DESTRUCTION_AND_RESURRECTION);
				}
			}	
			for(int i = 0 ; i < 5 ; i++)
				st.getPlayer().broadcastPacket(new MagicSkillUse(st.getPlayer(), 14496, 1, 500, 0));
			st.startQuestTimer("startDiologue3", 5000); //dlg 3
			return null;
		}

		else if(event.equalsIgnoreCase("startDiologue3"))
		{
			for(NpcInstance npcP : _Priests)
			{
				Functions.npcSay(npcP, NpcString.DEAR_THE_GODDESS_OF_DESTRUCTION_THE_LIGHT_AND_THEIR_CREATURES_FEAR_YOU);	
			}
			_mainZhrec.broadcastPacket(new MagicSkillUse(_mainZhrec, 14497, 1, 5000, 0));		
			st.startQuestTimer("startKillTimer", 5000); //dlg 3
			return null;
		}

		else if(event.equalsIgnoreCase("startKillTimer"))
		{
			for(NpcInstance npcP : _MobsToDie)
			{
				npcP.doDie(null);
			}
			st.startQuestTimer("startSpawnLast", 5000); //spawn guards
			return null;
		}

		else if(event.equalsIgnoreCase("startSpawnLast"))
		{
			NpcInstance npcP = player.getReflection().addSpawnWithoutRespawn(33365, new Location(player.getX()+Rnd.get(150), 85780+Rnd.get(150), -7752, 15428), 0);
			player.getReflection().addSpawnWithoutRespawn(33365, new Location(player.getX() + Rnd.get(150), 85780 + Rnd.get(150), -7752, 15428), 0);
			player.getReflection().addSpawnWithoutRespawn(33365, new Location(player.getX() + Rnd.get(150), 85780 + Rnd.get(150), -7752, 15428), 0);
			player.getReflection().addSpawnWithoutRespawn(33365, new Location(player.getX() + Rnd.get(150), 85780 + Rnd.get(150), -7752, 15428), 0);
			player.getReflection().addSpawnWithoutRespawn(33365, new Location(player.getX() + Rnd.get(150), 85780 + Rnd.get(150), -7752, 15428), 0);
			player.getReflection().addSpawnWithoutRespawn(33365, new Location(player.getX() + Rnd.get(150), 85780 + Rnd.get(150), -7752, 15428), 0);
			Functions.npcSay(npcP, NpcString.AH_UH_AH_UH_AH);
			st.startQuestTimer("ExitInstance", 3000); //exitInstance
			return null;
		}
		else if(event.equalsIgnoreCase("ExitInstance"))
		{
			st.getPlayer().setVar("instance10301", "true", -1);
			st.getPlayer().teleToLocation(207559, 86429, -1000, 0);
			return null;
		}
		return event;
	}

	@Override
	public String onTalk(NpcInstance npc, QuestState st)
	{
		Player player = st.getPlayer();
		int npcId = npc.getNpcId();
		int state = st.getState();
		int cond = st.getCond();
		if(npcId == RADA)
		{
			if(state == COMPLETED)
			{
				return "33100-comp.htm";
			}
			if(player.getLevel() < 90)
			{
				return "33100-lvl.htm";
			}
			if(cond == 0)
			{
				return "33100.htm";
			}
			if(state == 2)
			{
				if(cond == 2 && st.getQuestItemsCount(CRYSTALL) == 0)
				{
					return "33100-9.htm";
				}
				if(cond == 3)
				{
					return "33100-11.htm";
				}
			}

		}
		if(npcId == SLAKI)
		{
			if(state == COMPLETED)
			{
				return "32893-comp.htm";
			}

			if(cond == 3 && st.getQuestItemsCount(SPIRIT_ITEM) >= 1 && player.getVar("instance10301") != null)
			{
				return "32893-1.htm";
			}
		}
		return "noquest";
	}

	public String onKill(NpcInstance npc, QuestState st)
	{
		int cond = st.getCond();
		if(cond != 2)
		{
			return null;
		}
		if(!Rnd.chance(10))
		{
			return null;
		}
		st.giveItems(SPIRIT_ITEM, 1);
		st.takeItems(CRYSTALL, -1);
		st.setCond(3);
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