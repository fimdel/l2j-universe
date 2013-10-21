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
package bosses;

import static lineage2.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.BossInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.Earthquake;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.TimeUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bosses.EpicBossState.State;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BaiumManager extends Functions implements ScriptFile, OnDeathListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(BaiumManager.class);
	
	/**
	 * @author Mobius
	 */
	public static class CallArchAngel extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (SimpleSpawner spawn : _angelSpawns)
			{
				_angels.add(spawn.doSpawn(true));
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class CheckLastAttack extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_state.getState().equals(EpicBossState.State.ALIVE))
			{
				if ((_lastAttackTime + FWB_LIMITUNTILSLEEP) < System.currentTimeMillis())
				{
					sleepBaium();
				}
				else
				{
					_sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 60000);
				}
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class CubeSpawn extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_teleportCube = _teleportCubeSpawn.doSpawn(true);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class IntervalEnd extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_state.setState(EpicBossState.State.NOTSPAWN);
			_state.update();
			_statueSpawn.doSpawn(true);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class KillPc extends RunnableImpl
	{
		/**
		 * Field _boss.
		 */
		private final BossInstance _boss;
		/**
		 * Field _target.
		 */
		private final Player _target;
		
		/**
		 * Constructor for KillPc.
		 * @param target Player
		 * @param boss BossInstance
		 */
		public KillPc(Player target, BossInstance boss)
		{
			_target = target;
			_boss = boss;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final Skill skill = SkillTable.getInstance().getInfo(4136, 1);
			if ((_target != null) && (skill != null))
			{
				_boss.setTarget(_target);
				_boss.doCast(skill, _target, false);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class MoveAtRandom extends RunnableImpl
	{
		/**
		 * Field _npc.
		 */
		private final NpcInstance _npc;
		/**
		 * Field _pos.
		 */
		private final Location _pos;
		
		/**
		 * Constructor for MoveAtRandom.
		 * @param npc NpcInstance
		 * @param pos Location
		 */
		public MoveAtRandom(NpcInstance npc, Location pos)
		{
			_npc = npc;
			_pos = pos;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_npc.getAI().getIntention() == AI_INTENTION_ACTIVE)
			{
				_npc.moveToLocation(_pos, 0, false);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class SetMobilised extends RunnableImpl
	{
		/**
		 * Field _boss.
		 */
		private final BossInstance _boss;
		
		/**
		 * Constructor for SetMobilised.
		 * @param boss BossInstance
		 */
		public SetMobilised(BossInstance boss)
		{
			_boss = boss;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_boss.stopImmobilized();
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class Social extends RunnableImpl
	{
		/**
		 * Field _action.
		 */
		private final int _action;
		/**
		 * Field _npc.
		 */
		private final NpcInstance _npc;
		
		/**
		 * Constructor for Social.
		 * @param npc NpcInstance
		 * @param actionId int
		 */
		public Social(NpcInstance npc, int actionId)
		{
			_npc = npc;
			_action = actionId;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final SocialAction sa = new SocialAction(_npc.getObjectId(), _action);
			_npc.broadcastPacket(sa);
		}
	}
	
	/**
	 * Field _callAngelTask.
	 */
	private static ScheduledFuture<?> _callAngelTask = null;
	/**
	 * Field _cubeSpawnTask.
	 */
	private static ScheduledFuture<?> _cubeSpawnTask = null;
	/**
	 * Field _intervalEndTask.
	 */
	private static ScheduledFuture<?> _intervalEndTask = null;
	/**
	 * Field _killPcTask.
	 */
	private static ScheduledFuture<?> _killPcTask = null;
	/**
	 * Field _mobiliseTask.
	 */
	private static ScheduledFuture<?> _mobiliseTask = null;
	/**
	 * Field _moveAtRandomTask.
	 */
	private static ScheduledFuture<?> _moveAtRandomTask = null;
	/**
	 * Field _sleepCheckTask.
	 */
	static ScheduledFuture<?> _sleepCheckTask = null;
	/**
	 * Field _socialTask.
	 */
	private static ScheduledFuture<?> _socialTask = null;
	/**
	 * Field _socialTask2.
	 */
	private static ScheduledFuture<?> _socialTask2 = null;
	/**
	 * Field _onAnnihilatedTask.
	 */
	private static ScheduledFuture<?> _onAnnihilatedTask = null;
	/**
	 * Field _state.
	 */
	static EpicBossState _state;
	/**
	 * Field _lastAttackTime.
	 */
	static long _lastAttackTime = 0;
	/**
	 * Field _npcBaium.
	 */
	private static NpcInstance _npcBaium;
	/**
	 * Field _statueSpawn.
	 */
	static SimpleSpawner _statueSpawn = null;
	/**
	 * Field _teleportCube.
	 */
	static NpcInstance _teleportCube = null;
	/**
	 * Field _teleportCubeSpawn.
	 */
	static SimpleSpawner _teleportCubeSpawn = null;
	/**
	 * Field _monsters.
	 */
	private static final List<NpcInstance> _monsters = new ArrayList<>();
	/**
	 * Field _monsterSpawn.
	 */
	private static final Map<Integer, SimpleSpawner> _monsterSpawn = new ConcurrentHashMap<>();
	/**
	 * Field _angels.
	 */
	static List<NpcInstance> _angels = new ArrayList<>();
	/**
	 * Field _angelSpawns.
	 */
	static List<SimpleSpawner> _angelSpawns = new ArrayList<>();
	/**
	 * Field _zone.
	 */
	private static Zone _zone;
	/**
	 * Field ARCHANGEL. (value is 29021)
	 */
	private final static int ARCHANGEL = 29021;
	/**
	 * Field BAIUM. (value is 29020)
	 */
	private final static int BAIUM = 29020;
	/**
	 * Field BAIUM_NPC. (value is 29025)
	 */
	private final static int BAIUM_NPC = 29025;
	/**
	 * Field Dying.
	 */
	private static boolean Dying = false;
	/**
	 * Field ANGEL_LOCATION.
	 */
	private final static Location[] ANGEL_LOCATION = new Location[]
	{
		new Location(113004, 16209, 10076, 60242),
		new Location(114053, 16642, 10076, 4411),
		new Location(114563, 17184, 10076, 49241),
		new Location(116356, 16402, 10076, 31109),
		new Location(115015, 16393, 10076, 32760),
		new Location(115481, 15335, 10076, 16241),
		new Location(114680, 15407, 10051, 32485),
		new Location(114886, 14437, 10076, 16868),
		new Location(115391, 17593, 10076, 55346),
		new Location(115245, 17558, 10076, 35536)
	};
	/**
	 * Field CUBE_LOCATION.
	 */
	private final static Location CUBE_LOCATION = new Location(115203, 16620, 10078, 0);
	/**
	 * Field STATUE_LOCATION.
	 */
	private final static Location STATUE_LOCATION = new Location(115996, 17417, 10106, 41740);
	/**
	 * Field TELEPORT_CUBE. (value is 31759)
	 */
	private final static int TELEPORT_CUBE = 31759;
	/**
	 * Field FWB_LIMITUNTILSLEEP.
	 */
	private final static int FWB_LIMITUNTILSLEEP = 30 * 60000;
	/**
	 * Field FWB_FIXINTERVALOFBAIUM.
	 */
	private final static int FWB_FIXINTERVALOFBAIUM = 5 * 24 * 60 * 60000;
	/**
	 * Field FWB_RANDOMINTERVALOFBAIUM.
	 */
	private final static int FWB_RANDOMINTERVALOFBAIUM = 8 * 60 * 60000;
	
	/**
	 * Method banishForeigners.
	 */
	private static void banishForeigners()
	{
		for (Player player : getPlayersInside())
		{
			player.teleToClosestTown();
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class onAnnihilated extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			sleepBaium();
		}
	}
	
	/**
	 * Method checkAnnihilated.
	 */
	private synchronized static void checkAnnihilated()
	{
		if ((_onAnnihilatedTask == null) && isPlayersAnnihilated())
		{
			_onAnnihilatedTask = ThreadPoolManager.getInstance().schedule(new onAnnihilated(), 5000);
		}
	}
	
	/**
	 * Method deleteArchangels.
	 */
	private static void deleteArchangels()
	{
		for (NpcInstance angel : _angels)
		{
			if ((angel != null) && (angel.getSpawn() != null))
			{
				angel.getSpawn().stopRespawn();
				angel.deleteMe();
			}
		}
		_angels.clear();
	}
	
	/**
	 * Method getPlayersInside.
	 * @return List<Player>
	 */
	private static List<Player> getPlayersInside()
	{
		return getZone().getInsidePlayers();
	}
	
	/**
	 * Method getZone.
	 * @return Zone
	 */
	public static Zone getZone()
	{
		return _zone;
	}
	
	/**
	 * Method init.
	 */
	private void init()
	{
		_state = new EpicBossState(BAIUM);
		_zone = ReflectionUtils.getZone("[baium_epic]");
		CharListenerList.addGlobal(this);
		try
		{
			final SimpleSpawner tempSpawn;
			_statueSpawn = new SimpleSpawner(NpcHolder.getInstance().getTemplate(BAIUM_NPC));
			_statueSpawn.setAmount(1);
			_statueSpawn.setLoc(STATUE_LOCATION);
			_statueSpawn.stopRespawn();
			tempSpawn = new SimpleSpawner(NpcHolder.getInstance().getTemplate(BAIUM));
			tempSpawn.setAmount(1);
			_monsterSpawn.put(BAIUM, tempSpawn);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			final NpcTemplate Cube = NpcHolder.getInstance().getTemplate(TELEPORT_CUBE);
			_teleportCubeSpawn = new SimpleSpawner(Cube);
			_teleportCubeSpawn.setAmount(1);
			_teleportCubeSpawn.setLoc(CUBE_LOCATION);
			_teleportCubeSpawn.setRespawnDelay(60);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			final NpcTemplate angel = NpcHolder.getInstance().getTemplate(ARCHANGEL);
			SimpleSpawner spawnDat;
			_angelSpawns.clear();
			final List<Integer> random = new ArrayList<>();
			for (int i = 0; i < 5; i++)
			{
				int r = -1;
				while ((r == -1) || random.contains(r))
				{
					r = Rnd.get(10);
				}
				random.add(r);
			}
			for (int i : random)
			{
				spawnDat = new SimpleSpawner(angel);
				spawnDat.setAmount(1);
				spawnDat.setLoc(ANGEL_LOCATION[i]);
				spawnDat.setRespawnDelay(300000);
				_angelSpawns.add(spawnDat);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		_log.info("BaiumManager: State of Baium is " + _state.getState() + ".");
		if (_state.getState().equals(EpicBossState.State.NOTSPAWN))
		{
			_statueSpawn.doSpawn(true);
		}
		else if (_state.getState().equals(EpicBossState.State.ALIVE))
		{
			_state.setState(EpicBossState.State.NOTSPAWN);
			_state.update();
			_statueSpawn.doSpawn(true);
		}
		else if (_state.getState().equals(EpicBossState.State.INTERVAL) || _state.getState().equals(EpicBossState.State.DEAD))
		{
			setIntervalEndTask();
		}
		_log.info("BaiumManager: Next spawn date: " + TimeUtils.toSimpleFormat(_state.getRespawnDate()));
	}
	
	/**
	 * Method isPlayersAnnihilated.
	 * @return boolean
	 */
	private static boolean isPlayersAnnihilated()
	{
		for (Player pc : getPlayersInside())
		{
			if (!pc.isDead())
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method onDeath.
	 * @param self Creature
	 * @param killer Creature
	 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
	 */
	@Override
	public void onDeath(Creature self, Creature killer)
	{
		if (self.isPlayer() && (_state != null) && (_state.getState() == State.ALIVE) && (_zone != null) && _zone.checkIfInZone(self))
		{
			checkAnnihilated();
		}
		else if (self.isNpc() && (self.getNpcId() == BAIUM))
		{
			onBaiumDie(self);
		}
	}
	
	/**
	 * Method onBaiumDie.
	 * @param self Creature
	 */
	public static void onBaiumDie(Creature self)
	{
		if (Dying)
		{
			return;
		}
		Dying = true;
		self.broadcastPacket(new PlaySound(PlaySound.Type.MUSIC, "BS02_D", 1, 0, self.getLoc()));
		_state.setRespawnDate(getRespawnInterval());
		_state.setState(EpicBossState.State.INTERVAL);
		_state.update();
		Log.add("Baium died", "bosses");
		deleteArchangels();
		_cubeSpawnTask = ThreadPoolManager.getInstance().schedule(new CubeSpawn(), 10000);
	}
	
	/**
	 * Method getRespawnInterval.
	 * @return int
	 */
	private static int getRespawnInterval()
	{
		return (int) (Config.ALT_RAID_RESPAWN_MULTIPLIER * (FWB_FIXINTERVALOFBAIUM + Rnd.get(0, FWB_RANDOMINTERVALOFBAIUM)));
	}
	
	/**
	 * Method setIntervalEndTask.
	 */
	private static void setIntervalEndTask()
	{
		setUnspawn();
		if (!_state.getState().equals(EpicBossState.State.INTERVAL))
		{
			_state.setRespawnDate(getRespawnInterval());
			_state.setState(EpicBossState.State.INTERVAL);
			_state.update();
		}
		_intervalEndTask = ThreadPoolManager.getInstance().schedule(new IntervalEnd(), _state.getInterval());
	}
	
	/**
	 * Method setLastAttackTime.
	 */
	public static void setLastAttackTime()
	{
		_lastAttackTime = System.currentTimeMillis();
	}
	
	/**
	 * Method setUnspawn.
	 */
	public static void setUnspawn()
	{
		banishForeigners();
		deleteArchangels();
		for (NpcInstance mob : _monsters)
		{
			mob.getSpawn().stopRespawn();
			mob.deleteMe();
		}
		_monsters.clear();
		if (_teleportCube != null)
		{
			_teleportCube.getSpawn().stopRespawn();
			_teleportCube.deleteMe();
			_teleportCube = null;
		}
		if (_cubeSpawnTask != null)
		{
			_cubeSpawnTask.cancel(false);
			_cubeSpawnTask = null;
		}
		if (_intervalEndTask != null)
		{
			_intervalEndTask.cancel(false);
			_intervalEndTask = null;
		}
		if (_socialTask != null)
		{
			_socialTask.cancel(false);
			_socialTask = null;
		}
		if (_mobiliseTask != null)
		{
			_mobiliseTask.cancel(false);
			_mobiliseTask = null;
		}
		if (_moveAtRandomTask != null)
		{
			_moveAtRandomTask.cancel(false);
			_moveAtRandomTask = null;
		}
		if (_socialTask2 != null)
		{
			_socialTask2.cancel(false);
			_socialTask2 = null;
		}
		if (_killPcTask != null)
		{
			_killPcTask.cancel(false);
			_killPcTask = null;
		}
		if (_callAngelTask != null)
		{
			_callAngelTask.cancel(false);
			_callAngelTask = null;
		}
		if (_sleepCheckTask != null)
		{
			_sleepCheckTask.cancel(false);
			_sleepCheckTask = null;
		}
		if (_onAnnihilatedTask != null)
		{
			_onAnnihilatedTask.cancel(false);
			_onAnnihilatedTask = null;
		}
	}
	
	/**
	 * Method sleepBaium.
	 */
	static void sleepBaium()
	{
		setUnspawn();
		Log.add("Baium going to sleep, spawning statue", "bosses");
		_state.setState(EpicBossState.State.NOTSPAWN);
		_state.update();
		_statueSpawn.doSpawn(true);
	}
	
	/**
	 * @author Mobius
	 */
	public static class EarthquakeTask extends RunnableImpl
	{
		/**
		 * Field baium.
		 */
		private final BossInstance baium;
		
		/**
		 * Constructor for EarthquakeTask.
		 * @param _baium BossInstance
		 */
		public EarthquakeTask(BossInstance _baium)
		{
			baium = _baium;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final Earthquake eq = new Earthquake(baium.getLoc(), 40, 5);
			baium.broadcastPacket(eq);
		}
	}
	
	/**
	 * Method spawnBaium.
	 * @param NpcBaium NpcInstance
	 * @param awake_by Player
	 */
	public static void spawnBaium(NpcInstance NpcBaium, Player awake_by)
	{
		Dying = false;
		_npcBaium = NpcBaium;
		final SimpleSpawner baiumSpawn = _monsterSpawn.get(BAIUM);
		baiumSpawn.setLoc(_npcBaium.getLoc());
		_npcBaium.getSpawn().stopRespawn();
		_npcBaium.deleteMe();
		final BossInstance baium = (BossInstance) baiumSpawn.doSpawn(true);
		_monsters.add(baium);
		_state.setRespawnDate(getRespawnInterval());
		_state.setState(EpicBossState.State.ALIVE);
		_state.update();
		Log.add("Spawned Baium, awake by: " + awake_by, "bosses");
		setLastAttackTime();
		baium.startImmobilized();
		baium.broadcastPacket(new PlaySound(PlaySound.Type.MUSIC, "BS02_A", 1, 0, baium.getLoc()));
		baium.broadcastPacket(new SocialAction(baium.getObjectId(), 2));
		_socialTask = ThreadPoolManager.getInstance().schedule(new Social(baium, 3), 15000);
		ThreadPoolManager.getInstance().schedule(new EarthquakeTask(baium), 25000);
		_socialTask2 = ThreadPoolManager.getInstance().schedule(new Social(baium, 1), 25000);
		_killPcTask = ThreadPoolManager.getInstance().schedule(new KillPc(awake_by, baium), 26000);
		_callAngelTask = ThreadPoolManager.getInstance().schedule(new CallArchAngel(), 35000);
		_mobiliseTask = ThreadPoolManager.getInstance().schedule(new SetMobilised(baium), 35500);
		final Location pos = new Location(Rnd.get(112826, 116241), Rnd.get(15575, 16375), 10078, 0);
		_moveAtRandomTask = ThreadPoolManager.getInstance().schedule(new MoveAtRandom(baium, pos), 36000);
		_sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 600000);
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		init();
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		sleepBaium();
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		// empty method
	}
}
