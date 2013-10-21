package instances;

import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.Quest;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.NpcSay;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.utils.Location;

/**
 * @author KilRoy
 */
public class EvilIncubator extends Reflection
{
	private static final int ADOLPH_HELPERS1 = 33171;
	private static final int ADOLPH_HELPERS2 = 33172;
	private static final int ADOLPH_HELPERS3 = 33173;
	private static final int ADOLPH_HELPERS4 = 33174;
	private static final int VAN_ARCHER = 33414;
	private static final int VAN_INFANTRY = 33415;
	private static final int VAL_DEFENSE_WALL = 33416;
	private static final int ADOLPH_NPC = 33170;
	private static final int[] MONSTERS = { 27430, 27431, 27432, 27433, 27434 };
	private static final int DEATH_WOUND = 27429;
	private static final Location START_BATTLE_LOCATION = new Location(56167, -175615, -7944, 49653);
	private static final Location[] MONSTERS_WAVE_COORDS = { 
		new Location(56205, -177550, -7944, 63),
		new Location(56095, -177550, -7944, 233),
		new Location(56245, -177550, -7944, 255),
		new Location(56125, -177550, -7944, 97),
		new Location(56165, -177550, -7944, 33),
		new Location(55645, -176695, -7944, 15),
		new Location(55645, -176765, -7944, 97),
		new Location(55645, -176735, -7944, 209),
		new Location(55645, -176735, -7944, 47),
		new Location(56590, -176744, -7944, 141),
		new Location(56595, -176695, -7944, 269),
		new Location(56642, -176560, -7952, 263),
		new Location(56023, -177087, -7952, 16026),
		new Location(56212, -176074, -7944, 37403) };

	private List<NpcInstance> ADOLPH;
	private int GUARD1 = 0;
	private int GUARD2 = 0;
	private boolean sayLocker = false;
	private int firstWaveMonsters = 0;
	private int secondWaveMonsters = 0;
	private int mobKilled = 0;
	private int instanceStage = 0;
	private QuestState state;

	private DeathListener deathListener = new DeathListener();

	public EvilIncubator(QuestState state)
	{
		super();
		this.state = state;
	}

	@Override
	protected void onCreate()
	{
		super.onCreate();
	}

	@Override
	public void onPlayerEnter(Player player)
	{
		super.onPlayerEnter(player);
		setQuestCond(5);
		addSpawnWithoutRespawn(ADOLPH_NPC, new Location(56171, -172733, -7952, 49180), 0);
		addSpawnWithoutRespawn(ADOLPH_HELPERS1, new Location(56276, -172657, -7952, 49820), 0);
		addSpawnWithoutRespawn(ADOLPH_HELPERS2, new Location(56357, -172654, -7952, 49044), 0);
		addSpawnWithoutRespawn(ADOLPH_HELPERS3, new Location(56067, -172651, -7952, 49356), 0);
		addSpawnWithoutRespawn(ADOLPH_HELPERS4, new Location(55970, -172654, -7952, 49208), 0);
		ThreadPoolManager.getInstance().schedule(new SayChatTask(NpcString.I_CAN_HEAL_YOU_DURING_COMBAT, ADOLPH_HELPERS1), 10000);
		ThreadPoolManager.getInstance().schedule(new SayChatTask(NpcString.I_HIT_THINGS_THEY_FALL_DEAD, ADOLPH_HELPERS2), 6000);
		ThreadPoolManager.getInstance().schedule(new SayChatTask(NpcString.WHAT_DO_I_FEEL_WHEN_I_KILL_SHILENS_MONSTERS_RECOIL, ADOLPH_HELPERS3), 3000);
		ThreadPoolManager.getInstance().schedule(new SayChatTask(NpcString.MY_SUMMONS_ARE_NOT_AFRAID_OF_SHILENS_MONSTER, ADOLPH_HELPERS4), 9000);
	}

	public void startFirstWave(Player player)
	{
		sayLocker = false;
		instanceStage = 1;
		player.teleToLocation(START_BATTLE_LOCATION, this);
		addSpawnWithoutRespawn(GUARD1, new Location(56367, -175707, -7981, 53248), 0);
		addSpawnWithoutRespawn(GUARD2, new Location(55982, -175707, -7981, 53248), 0);
		List<NpcInstance> npc = getAllByNpcId(ADOLPH_NPC, true);
		if(!npc.isEmpty())
			npc.get(0).deleteMe();
		addSpawnWithoutRespawn(ADOLPH_NPC, START_BATTLE_LOCATION, 0);
		ADOLPH = getAllByNpcId(ADOLPH_NPC, true);
		addSpawnWithoutRespawn(VAN_ARCHER, new Location(55982, -176068, -7981, 53248), 0);
		addSpawnWithoutRespawn(VAN_ARCHER, new Location(55167, -176068, -7975, 53248), 0);
		addSpawnWithoutRespawn(VAN_ARCHER, new Location(56382, -176068, -7981, 53248), 0);
		addSpawnWithoutRespawn(VAN_INFANTRY, new Location(56297, -176220, -7981, 53248), 0);
		addSpawnWithoutRespawn(VAN_INFANTRY, new Location(56032, -176220, -7981, 53248), 0);
		addSpawnWithoutRespawn(VAL_DEFENSE_WALL, new Location(55889, -176208, -7952, 49416), 0);
		addSpawnWithoutRespawn(VAL_DEFENSE_WALL, new Location(56280, -176226, -7952, 48740), 0);
		addSpawnWithoutRespawn(VAL_DEFENSE_WALL, new Location(56473, -176248, -7952, 49824), 0);
		addSpawnWithoutRespawn(VAL_DEFENSE_WALL, new Location(56077, -176202, -7944, 49180), 0);
		ThreadPoolManager.getInstance().schedule(new SayChatTask(NpcString.DID_SOMEONE_CRY_MEDIC_HERE_BE_HEALED, ADOLPH_HELPERS1), 11000);
		ThreadPoolManager.getInstance().schedule(new SayChatTask(NpcString.THE_ONLY_GOOD_SHILEN_CREATURE_IS_A_DEAD_ONE, ADOLPH_HELPERS2), 2000);
		ThreadPoolManager.getInstance().schedule(new SayChatTask(NpcString.BOOM_HEADSHOT, ADOLPH_HELPERS3), 5000);
		ThreadPoolManager.getInstance().schedule(new SayChatTask(NpcString.I_M_ON_FIRE_NO_WAIT_THAT_WOULD_BE_YOU, ADOLPH_HELPERS4), 7000);
		ThreadPoolManager.getInstance().schedule(new FirstWaveTask(), 10000);
	}

	public void startSecondWave()
	{
		instanceStage = 2;
		ThreadPoolManager.getInstance().schedule(new SecondWaveTask(), 3000);
	}

	public void setHelperId(int npcId)
	{
		if(GUARD1 == 0)
			GUARD1 = npcId;
		else
			GUARD2 = npcId;
	}

	public void deleteSelectedHelper(int npcId)
	{
		List<NpcInstance> npc = getAllByNpcId(npcId, true);
		if(!npc.isEmpty())
			npc.get(0).deleteMe();
	}

	public void deleteNotSelectedHelper()
	{
		sayLocker = true;
		List<NpcInstance> npc = getAllByNpcId(ADOLPH_HELPERS1, true);
		npc.addAll(getAllByNpcId(ADOLPH_HELPERS2, true));
		npc.addAll(getAllByNpcId(ADOLPH_HELPERS3, true));
		npc.addAll(getAllByNpcId(ADOLPH_HELPERS4, true));
		if(!npc.isEmpty())
		{
			npc.get(0).deleteMe();
			npc.get(1).deleteMe();
		}
	}

	public void sendInstanceState(int state)
	{
		if(state == 1)
		{
			ADOLPH.get(0).broadcastPacket(new NpcSay(ADOLPH.get(0), ChatType.NPC_SAY, NpcString.CREATURES_HAVE_STOPPED_ATTACKING_USE_THIS_TIME_TO_REST_AND_RECOVER));
			for(Player player : getPlayers())
				player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_HAVE_STOPPER_THEIR_ATTACK_REST_AND_THEN_SPEEAK_WITH_ADOLPH, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
		}
		if(state == 2)
		{
			ADOLPH.get(0).broadcastPacket(new NpcSay(ADOLPH.get(0), ChatType.NPC_SAY, NpcString.THE_CRY_OF_FATE_PENDANT_WILL_BE_HELPFUL_TO_YOU_PLEASE_EQUIP_IT_AND_BRING_OUT_THE_POWER_OF_THE_PENDANT_TO_PREPARE_FOR_THE_NEXT_FIGHT));
		}
		if(state == 3)
		{
			for(Player player : getPlayers())
				player.sendPacket(new ExShowScreenMessage(NpcString.AGH_HUMANS_HA_IT_DOES_NOT_MATTER_YOUR_WORLD_WILL_END_ANYWAYS, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
		}
	}

	private void invokeDeathListener()
	{
		for(NpcInstance npc : getNpcs())
			npc.addListener(deathListener);
	}

	private void spawnMonsters()
	{
		Location coords = MONSTERS_WAVE_COORDS[Rnd.get(MONSTERS_WAVE_COORDS.length)];
		for(int i = 0;i < MONSTERS.length;i++)
		{
			addSpawnWithoutRespawn(MONSTERS[i], coords, 50);
			if(secondWaveMonsters == 9)
				addSpawnWithoutRespawn(DEATH_WOUND, coords, 50);
			invokeDeathListener();
		}
		if(secondWaveMonsters == 9)
		{
			addSpawnWithoutRespawn(DEATH_WOUND, coords, 50);
			invokeDeathListener();
		}
	}

	private void setQuestCond(int cond)
	{
		state.setCond(cond);
		state.playSound(Quest.SOUND_MIDDLE);
	}

	public int getInstanceStage()
	{
		return instanceStage;
	}

	private class SayChatTask extends RunnableImpl
	{
		private NpcString msg;
		private int npcId;

		public SayChatTask(NpcString msg, int npcId)
		{
			this.msg = msg;
			this.npcId = npcId;
		}

		@Override
		public void runImpl() throws Exception
		{
			List<NpcInstance> npc = getAllByNpcId(npcId, true);
			if(!npc.isEmpty())
				npc.get(0).broadcastPacket(new NpcSay(npc.get(0), ChatType.NPC_SAY, msg));
			if(!sayLocker && !npc.isEmpty())
				ThreadPoolManager.getInstance().schedule(this, 8000);
		}
	}

	private class FirstWaveTask extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			firstWaveMonsters++;
			if(firstWaveMonsters != 6)
			{
				for(Player player : getPlayers())
					player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
				spawnMonsters();
				ThreadPoolManager.getInstance().schedule(this, 40000);
			}
			else
			{
				for(Player player : getPlayers())
					player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
					
				spawnMonsters();
			}
		}
	}

	private class SecondWaveTask extends RunnableImpl
	{
		@Override
		public void runImpl() throws Exception
		{
			secondWaveMonsters++;
			if(secondWaveMonsters != 9)
			{
				for(Player player : getPlayers())
					player.sendPacket(new ExShowScreenMessage(NpcString.CREATURES_RESURECTED_DEFEND_YOURSELF, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
				spawnMonsters();
				ThreadPoolManager.getInstance().schedule(this, 30000);
			}
			else
			{
				for(Player player : getPlayers())
				{
					player.sendPacket(new ExShowScreenMessage(NpcString.I_DEATH_WOUND_CHAMPION_OF_SHILEN_SHALL_END_YOUR_WORLD, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, ExShowScreenMessage.STRING_TYPE, 0, true, 0));
					player.broadcastPacket(new Earthquake(ADOLPH.get(0).getLoc(), 50, 4));
				}
				spawnMonsters();
			}
		}
	}

	private class DeathListener implements OnDeathListener
	{
		@Override
		public void onDeath(Creature self, Creature killer)
		{
			if(!self.isNpc())
				return;
			if(self.getNpcId() == 27430 || self.getNpcId() == 27431 || self.getNpcId() == 27432 || self.getNpcId() == 27433 || self.getNpcId() == 27434 || self.getNpcId() == 27429)
			{
				mobKilled++;
				if(mobKilled >= 30 && instanceStage == 1)
				{
					mobKilled = 0;
					setQuestCond(9);
					sendInstanceState(1);
				}
				if(mobKilled >= 46 && instanceStage == 2)
				{
					mobKilled = 0;
					deathListener = null;
					sendInstanceState(3);
					setQuestCond(12);
				}
			}
		}
	}
}