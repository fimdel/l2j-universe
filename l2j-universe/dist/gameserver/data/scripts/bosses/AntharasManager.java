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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.CommandChannel;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.BossInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;
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
public class AntharasManager extends Functions implements ScriptFile, OnDeathListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(AntharasManager.class);
	/**
	 * Field _teleportCubeId. (value is 31859)
	 */
	private static final int _teleportCubeId = 31859;
	/**
	 * Field ANTHARAS_STRONG. (value is 29068)
	 */
	private static final int ANTHARAS_STRONG = 29068;
	/**
	 * Field PORTAL_STONE. (value is 3865)
	 */
	private static final int PORTAL_STONE = 3865;
	/**
	 * Field TELEPORT_POSITION.
	 */
	private static final Location TELEPORT_POSITION = new Location(179892, 114915, -7704);
	/**
	 * Field _teleportCubeLocation.
	 */
	private static final Location _teleportCubeLocation = new Location(177615, 114941, -7709, 0);
	/**
	 * Field _antharasLocation.
	 */
	static final Location _antharasLocation = new Location(181911, 114835, -7678, 32542);
	/**
	 * Field _antharas.
	 */
	static BossInstance _antharas;
	/**
	 * Field _teleCube.
	 */
	private static NpcInstance _teleCube;
	/**
	 * Field _spawnedMinions.
	 */
	private static final List<NpcInstance> _spawnedMinions = new ArrayList<>();
	/**
	 * Field _monsterSpawnTask.
	 */
	private static ScheduledFuture<?> _monsterSpawnTask;
	/**
	 * Field _intervalEndTask.
	 */
	private static ScheduledFuture<?> _intervalEndTask;
	/**
	 * Field _socialTask.
	 */
	static ScheduledFuture<?> _socialTask;
	/**
	 * Field _moveAtRandomTask.
	 */
	private static ScheduledFuture<?> _moveAtRandomTask;
	/**
	 * Field _sleepCheckTask.
	 */
	static ScheduledFuture<?> _sleepCheckTask;
	/**
	 * Field _onAnnihilatedTask.
	 */
	private static ScheduledFuture<?> _onAnnihilatedTask;
	/**
	 * Field _state.
	 */
	static EpicBossState _state;
	/**
	 * Field _zone.
	 */
	private static Zone _zone;
	/**
	 * Field _lastAttackTime.
	 */
	static long _lastAttackTime = 0;
	/**
	 * Field FWA_LIMITUNTILSLEEP.
	 */
	private static final int FWA_LIMITUNTILSLEEP = 15 * 60000;
	/**
	 * Field FWA_FIXINTERVALOFANTHARAS.
	 */
	private static final int FWA_FIXINTERVALOFANTHARAS = 11 * 24 * 60 * 60000;
	/**
	 * Field FWA_APPTIMEOFANTHARAS.
	 */
	private static final int FWA_APPTIMEOFANTHARAS = 5 * 60000;
	/**
	 * Field Dying.
	 */
	private static boolean Dying = false;
	/**
	 * Field _entryLocked.
	 */
	private static boolean _entryLocked = false;
	
	/**
	 * @author Mobius
	 */
	private static class AntharasSpawn extends RunnableImpl
	{
		/**
		 * Field _distance.
		 */
		private static final int _distance = 2550;
		/**
		 * Field _taskId.
		 */
		private int _taskId = 0;
		/**
		 * Field _players.
		 */
		private final List<Player> _players = getPlayersInside();
		
		/**
		 * Constructor for AntharasSpawn.
		 * @param taskId int
		 */
		AntharasSpawn(int taskId)
		{
			_taskId = taskId;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			switch (_taskId)
			{
				case 1:
					_antharas = (BossInstance) Functions.spawn(_antharasLocation, ANTHARAS_STRONG);
					_antharas.setAggroRange(0);
					_state.setRespawnDate(Rnd.get(FWA_FIXINTERVALOFANTHARAS, FWA_FIXINTERVALOFANTHARAS));
					_state.setState(EpicBossState.State.ALIVE);
					_state.update();
					_socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(2), 2000);
					break;
				case 2:
					for (Player pc : _players)
					{
						if (pc.getDistance(_antharas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_antharas, 700, 13, -19, 0, 20000, 0, 0, 0, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(3), 3000);
					break;
				case 3:
					_antharas.broadcastPacket(new SocialAction(_antharas.getObjectId(), 1));
					for (Player pc : _players)
					{
						if (pc.getDistance(_antharas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_antharas, 700, 13, 0, 6000, 20000, 0, 0, 0, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(4), 10000);
					break;
				case 4:
					_antharas.broadcastPacket(new SocialAction(_antharas.getObjectId(), 2));
					for (Player pc : _players)
					{
						if (pc.getDistance(_antharas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_antharas, 3700, 0, -3, 0, 10000, 0, 0, 0, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(5), 200);
					break;
				case 5:
					for (Player pc : _players)
					{
						if (pc.getDistance(_antharas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_antharas, 1100, 0, -3, 22000, 30000, 0, 0, 0, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(6), 10800);
					break;
				case 6:
					for (Player pc : _players)
					{
						if (pc.getDistance(_antharas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_antharas, 1100, 0, -3, 300, 7000, 0, 0, 0, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(7), 7000);
					break;
				case 7:
					for (Player pc : _players)
					{
						pc.leaveMovieMode();
					}
					broadcastScreenMessage(NpcString.ANTHARAS_YOU_CANNOT_HOPE_TO_DEFEAT_ME);
					_antharas.broadcastPacket(new PlaySound(PlaySound.Type.MUSIC, "BS02_A", 1, _antharas.getObjectId(), _antharas.getLoc()));
					_antharas.setAggroRange(_antharas.getTemplate().aggroRange);
					_antharas.setRunning();
					_antharas.moveToLocation(new Location(179011, 114871, -7704), 0, false);
					_sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 600000);
					break;
				case 8:
					for (Player pc : _players)
					{
						if (pc.getDistance(_antharas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_antharas, 1200, 20, -10, 0, 13000, 0, 0, 0, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(9), 13000);
					break;
				case 9:
					for (Player pc : _players)
					{
						pc.leaveMovieMode();
						pc.altOnMagicUseTimer(pc, SkillTable.getInstance().getInfo(23312, 1));
					}
					broadcastScreenMessage(NpcString.ANTHARAS_THE_EVIL_LAND_DRAGON_ANTHARAS_DEFEATED);
					onAntharasDie();
					break;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class CheckLastAttack extends RunnableImpl
	{
		/**
		 * Constructor for CheckLastAttack.
		 */
		CheckLastAttack()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_state.getState() == EpicBossState.State.ALIVE)
			{
				if ((_lastAttackTime + FWA_LIMITUNTILSLEEP) < System.currentTimeMillis())
				{
					sleep();
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
	private static class IntervalEnd extends RunnableImpl
	{
		/**
		 * Constructor for IntervalEnd.
		 */
		IntervalEnd()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_state.setState(EpicBossState.State.NOTSPAWN);
			_state.update();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class onAnnihilated extends RunnableImpl
	{
		/**
		 * Constructor for onAnnihilated.
		 */
		onAnnihilated()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			sleep();
		}
	}
	
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
	 * Method getPlayersInside.
	 * @return List<Player>
	 */
	static List<Player> getPlayersInside()
	{
		return getZone().getInsidePlayers();
	}
	
	/**
	 * Method getRespawnInterval.
	 * @return int
	 */
	private static int getRespawnInterval()
	{
		return (int) (Config.ALT_RAID_RESPAWN_MULTIPLIER * FWA_FIXINTERVALOFANTHARAS);
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
	 * Method onAntharasDie.
	 */
	static void onAntharasDie()
	{
		if (Dying)
		{
			return;
		}
		Dying = true;
		_state.setRespawnDate(getRespawnInterval());
		_state.setState(EpicBossState.State.INTERVAL);
		_state.update();
		_entryLocked = false;
		_teleCube = Functions.spawn(_teleportCubeLocation, _teleportCubeId);
		Log.add("Antharas died", "bosses");
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
		if (self.isPlayer() && (_state != null) && (_state.getState() == State.ALIVE) && (_zone != null) && _zone.checkIfInZone(self.getX(), self.getY()))
		{
			checkAnnihilated();
		}
		else if (self.isNpc() && (self.getNpcId() == ANTHARAS_STRONG))
		{
			ThreadPoolManager.getInstance().schedule(new AntharasSpawn(8), 10);
		}
	}
	
	/**
	 * Method setIntervalEndTask.
	 */
	private static void setIntervalEndTask()
	{
		setUnspawn();
		if (_state.getState().equals(EpicBossState.State.ALIVE))
		{
			_state.setState(EpicBossState.State.NOTSPAWN);
			_state.update();
			return;
		}
		if (!_state.getState().equals(EpicBossState.State.INTERVAL))
		{
			_state.setRespawnDate(getRespawnInterval());
			_state.setState(EpicBossState.State.INTERVAL);
			_state.update();
		}
		_intervalEndTask = ThreadPoolManager.getInstance().schedule(new IntervalEnd(), _state.getInterval());
	}
	
	/**
	 * Method setUnspawn.
	 */
	private static void setUnspawn()
	{
		banishForeigners();
		if (_antharas != null)
		{
			_antharas.deleteMe();
		}
		for (NpcInstance npc : _spawnedMinions)
		{
			npc.deleteMe();
		}
		if (_teleCube != null)
		{
			_teleCube.deleteMe();
		}
		_entryLocked = false;
		if (_monsterSpawnTask != null)
		{
			_monsterSpawnTask.cancel(false);
			_monsterSpawnTask = null;
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
		if (_moveAtRandomTask != null)
		{
			_moveAtRandomTask.cancel(false);
			_moveAtRandomTask = null;
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
	 * Method init.
	 */
	private void init()
	{
		_state = new EpicBossState(ANTHARAS_STRONG);
		_zone = ReflectionUtils.getZone("[antharas_epic]");
		CharListenerList.addGlobal(this);
		_log.info("AntharasManager: State of Antharas is " + _state.getState() + ".");
		if (!_state.getState().equals(EpicBossState.State.NOTSPAWN))
		{
			setIntervalEndTask();
		}
		_log.info("AntharasManager: Next spawn date of Antharas is " + TimeUtils.toSimpleFormat(_state.getRespawnDate()) + ".");
	}
	
	/**
	 * Method sleep.
	 */
	static void sleep()
	{
		setUnspawn();
		if (_state.getState().equals(EpicBossState.State.ALIVE))
		{
			_state.setState(EpicBossState.State.NOTSPAWN);
			_state.update();
		}
	}
	
	/**
	 * Method setLastAttackTime.
	 */
	public static void setLastAttackTime()
	{
		_lastAttackTime = System.currentTimeMillis();
	}
	
	/**
	 * Method setAntharasSpawnTask.
	 */
	public synchronized static void setAntharasSpawnTask()
	{
		if (_monsterSpawnTask == null)
		{
			_monsterSpawnTask = ThreadPoolManager.getInstance().schedule(new AntharasSpawn(1), FWA_APPTIMEOFANTHARAS);
		}
		_entryLocked = true;
	}
	
	/**
	 * Method broadcastScreenMessage.
	 * @param npcs NpcString
	 */
	public static void broadcastScreenMessage(NpcString npcs)
	{
		for (Player p : getPlayersInside())
		{
			p.sendPacket(new ExShowScreenMessage(npcs, 8000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, false));
		}
	}
	
	/**
	 * Method addSpawnedMinion.
	 * @param npc NpcInstance
	 */
	public static void addSpawnedMinion(NpcInstance npc)
	{
		_spawnedMinions.add(npc);
	}
	
	/**
	 * Method enterTheLair.
	 * @param ccleader Player
	 */
	public static void enterTheLair(Player ccleader)
	{
		if (ccleader == null)
		{
			return;
		}
		if ((ccleader.getParty() == null) || !ccleader.getParty().isInCommandChannel())
		{
			ccleader.sendPacket(Msg.YOU_CANNOT_ENTER_BECAUSE_YOU_ARE_NOT_IN_A_CURRENT_COMMAND_CHANNEL);
			return;
		}
		final CommandChannel cc = ccleader.getParty().getCommandChannel();
		if (!cc.getChannelLeader().equals(ccleader))
		{
			ccleader.sendPacket(Msg.ONLY_THE_ALLIANCE_CHANNEL_LEADER_CAN_ATTEMPT_ENTRY);
			return;
		}
		if (cc.getMemberCount() > 200)
		{
			ccleader.sendMessage("The maximum of 200 players can invade the Antharas Nest");
			return;
		}
		if (_state.getState() != EpicBossState.State.NOTSPAWN)
		{
			ccleader.sendMessage("Antharas is still reborning. You cannot invade the nest now");
			return;
		}
		if (_entryLocked || (_state.getState() == EpicBossState.State.ALIVE))
		{
			ccleader.sendMessage("Antharas has already been reborned and is being attacked. The entrance is sealed.");
			return;
		}
		for (Player p : cc)
		{
			if (p.isDead() || p.isFlying() || p.isCursedWeaponEquipped() || (p.getInventory().getCountOf(PORTAL_STONE) < 1) || !p.isInRange(ccleader, 500))
			{
				ccleader.sendMessage("Command Channel member " + p.getName() + " doesn't meet the requirements to enter the nest");
				return;
			}
		}
		for (Player p : cc)
		{
			p.teleToLocation(TELEPORT_POSITION);
		}
		setAntharasSpawnTask();
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
		sleep();
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
