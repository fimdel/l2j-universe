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
public class ValakasManager extends Functions implements ScriptFile, OnDeathListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ValakasManager.class);
	/**
	 * Field _teleportCubeLocation.
	 */
	private static final int[][] _teleportCubeLocation =
	{
		{
			214880,
			-116144,
			-1644,
			0
		},
		{
			213696,
			-116592,
			-1644,
			0
		},
		{
			212112,
			-116688,
			-1644,
			0
		},
		{
			211184,
			-115472,
			-1664,
			0
		},
		{
			210336,
			-114592,
			-1644,
			0
		},
		{
			211360,
			-113904,
			-1644,
			0
		},
		{
			213152,
			-112352,
			-1644,
			0
		},
		{
			214032,
			-113232,
			-1644,
			0
		},
		{
			214752,
			-114592,
			-1644,
			0
		},
		{
			209824,
			-115568,
			-1421,
			0
		},
		{
			210528,
			-112192,
			-1403,
			0
		},
		{
			213120,
			-111136,
			-1408,
			0
		},
		{
			215184,
			-111504,
			-1392,
			0
		},
		{
			215456,
			-117328,
			-1392,
			0
		},
		{
			213200,
			-118160,
			-1424,
			0
		}
	};
	/**
	 * Field _teleportCube.
	 */
	private static List<NpcInstance> _teleportCube = new ArrayList<>();
	/**
	 * Field _spawnedMinions.
	 */
	private static List<NpcInstance> _spawnedMinions = new ArrayList<>();
	/**
	 * Field _valakas.
	 */
	static BossInstance _valakas;
	/**
	 * Field _valakasSpawnTask.
	 */
	private static ScheduledFuture<?> _valakasSpawnTask = null;
	/**
	 * Field _intervalEndTask.
	 */
	private static ScheduledFuture<?> _intervalEndTask = null;
	/**
	 * Field _socialTask.
	 */
	static ScheduledFuture<?> _socialTask = null;
	/**
	 * Field _mobiliseTask.
	 */
	private static ScheduledFuture<?> _mobiliseTask = null;
	/**
	 * Field _moveAtRandomTask.
	 */
	private static ScheduledFuture<?> _moveAtRandomTask = null;
	/**
	 * Field _respawnValakasTask.
	 */
	private static ScheduledFuture<?> _respawnValakasTask = null;
	/**
	 * Field _sleepCheckTask.
	 */
	static ScheduledFuture<?> _sleepCheckTask = null;
	/**
	 * Field _onAnnihilatedTask.
	 */
	private static ScheduledFuture<?> _onAnnihilatedTask = null;
	/**
	 * Field Valakas. (value is 29028)
	 */
	private static final int Valakas = 29028;
	/**
	 * Field _teleportCubeId. (value is 31759)
	 */
	private static final int _teleportCubeId = 31759;
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
	 * Field FWV_LIMITUNTILSLEEP.
	 */
	private static final int FWV_LIMITUNTILSLEEP = 20 * 60000;
	/**
	 * Field FWV_APPTIMEOFVALAKAS.
	 */
	private static final int FWV_APPTIMEOFVALAKAS = 10 * 60000;
	/**
	 * Field FWV_FIXINTERVALOFVALAKAS.
	 */
	private static final int FWV_FIXINTERVALOFVALAKAS = 11 * 24 * 60 * 60000;
	/**
	 * Field Dying.
	 */
	private static boolean Dying = false;
	/**
	 * Field TELEPORT_POSITION.
	 */
	private static final Location TELEPORT_POSITION = new Location(203940, -111840, 66);
	/**
	 * Field _entryLocked.
	 */
	private static boolean _entryLocked = false;
	
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
				if ((_lastAttackTime + FWV_LIMITUNTILSLEEP) < System.currentTimeMillis())
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
	 * @author Mobius
	 */
	private static class SpawnDespawn extends RunnableImpl
	{
		/**
		 * Field _distance.
		 */
		private static final int _distance = 2550;
		/**
		 * Field _taskId.
		 */
		private final int _taskId;
		/**
		 * Field _players.
		 */
		private final List<Player> _players = getPlayersInside();
		
		/**
		 * Constructor for SpawnDespawn.
		 * @param taskId int
		 */
		SpawnDespawn(int taskId)
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
					_valakas = (BossInstance) Functions.spawn(new Location(212852, -114842, -1632, 833), Valakas);
					_valakas.block();
					_valakas.broadcastPacket(new PlaySound(PlaySound.Type.MUSIC, "BS03_A", 1, _valakas.getObjectId(), _valakas.getLoc()));
					_state.setRespawnDate(Rnd.get(FWV_FIXINTERVALOFVALAKAS, FWV_FIXINTERVALOFVALAKAS));
					_state.setState(EpicBossState.State.ALIVE);
					_state.update();
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(2), 16);
					break;
				case 2:
					_valakas.broadcastPacket(new SocialAction(_valakas.getObjectId(), 1));
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1800, 180, -1, 1500, 15000, 0, 0, 1, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(3), 1500);
					break;
				case 3:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1300, 180, -5, 3000, 15000, 0, -5, 1, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(4), 3300);
					break;
				case 4:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 500, 180, -8, 600, 15000, 0, 60, 1, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(5), 2900);
					break;
				case 5:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 800, 180, -8, 2700, 15000, 0, 30, 1, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(6), 2700);
					break;
				case 6:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 200, 250, 70, 0, 15000, 30, 80, 1, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(7), 1);
					break;
				case 7:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1100, 250, 70, 2500, 15000, 30, 80, 1, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(8), 3200);
					break;
				case 8:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 700, 150, 30, 0, 15000, -10, 60, 1, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(9), 1400);
					break;
				case 9:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1200, 150, 20, 2900, 15000, -10, 30, 1, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(10), 6700);
					break;
				case 10:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 750, 170, -10, 3400, 15000, 10, -15, 1, 0);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(11), 5700);
					break;
				case 11:
					for (Player pc : _players)
					{
						pc.leaveMovieMode();
					}
					_valakas.unblock();
					broadcastScreenMessage(NpcString.VALAKAS_ARROGAANT_FOOL_YOU_DARE_TO_CHALLENGE_ME);
					if (_valakas.getAI().getIntention() == AI_INTENTION_ACTIVE)
					{
						_valakas.moveToLocation(new Location(Rnd.get(211080, 214909), Rnd.get(-115841, -112822), -1662, 0), 0, false);
					}
					_sleepCheckTask = ThreadPoolManager.getInstance().schedule(new CheckLastAttack(), 600000);
					break;
				case 12:
					_valakas.broadcastPacket(new PlaySound(PlaySound.Type.MUSIC, "B03_D", 1, _valakas.getObjectId(), _valakas.getLoc()));
					broadcastScreenMessage(NpcString.VALAKAS_THE_EVIL_FIRE_DRAGON_VALAKAS_DEFEATED);
					onValakasDie();
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 2000, 130, -1, 0, 15000, 0, 0, 1, 1);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(13), 500);
					break;
				case 13:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1100, 210, -5, 3000, 15000, -13, 0, 1, 1);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(14), 3500);
					break;
				case 14:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1300, 200, -8, 3000, 15000, 0, 15, 1, 1);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(15), 4500);
					break;
				case 15:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1000, 190, 0, 500, 15000, 0, 10, 1, 1);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(16), 500);
					break;
				case 16:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1700, 120, 0, 2500, 15000, 12, 40, 1, 1);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(17), 4600);
					break;
				case 17:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1700, 20, 0, 700, 15000, 10, 10, 1, 1);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(18), 750);
					break;
				case 18:
					for (Player pc : _players)
					{
						if (pc.getDistance(_valakas) <= _distance)
						{
							pc.enterMovieMode();
							pc.specialCamera(_valakas, 1700, 10, 0, 1000, 15000, 20, 70, 1, 1);
						}
						else
						{
							pc.leaveMovieMode();
						}
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(19), 2500);
					break;
				case 19:
					for (Player pc : _players)
					{
						pc.leaveMovieMode();
						pc.altOnMagicUseTimer(pc, SkillTable.getInstance().getInfo(23312, 1));
					}
					break;
			}
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
		return (int) (Config.ALT_RAID_RESPAWN_MULTIPLIER * FWV_FIXINTERVALOFVALAKAS);
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
		else if (self.isNpc() && (self.getNpcId() == Valakas))
		{
			ThreadPoolManager.getInstance().schedule(new SpawnDespawn(12), 1);
		}
	}
	
	/**
	 * Method onValakasDie.
	 */
	static void onValakasDie()
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
		for (int[] ints : _teleportCubeLocation)
		{
			_teleportCube.add(Functions.spawn(new Location(ints[0], ints[1], ints[2], ints[3]), _teleportCubeId));
		}
		Log.add("Valakas died", "bosses");
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
		_entryLocked = false;
		if (_valakas != null)
		{
			_valakas.deleteMe();
		}
		for (NpcInstance npc : _spawnedMinions)
		{
			npc.deleteMe();
		}
		for (NpcInstance cube : _teleportCube)
		{
			cube.getSpawn().stopRespawn();
			cube.deleteMe();
		}
		_teleportCube.clear();
		if (_valakasSpawnTask != null)
		{
			_valakasSpawnTask.cancel(false);
			_valakasSpawnTask = null;
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
		if (_sleepCheckTask != null)
		{
			_sleepCheckTask.cancel(false);
			_sleepCheckTask = null;
		}
		if (_respawnValakasTask != null)
		{
			_respawnValakasTask.cancel(false);
			_respawnValakasTask = null;
		}
		if (_onAnnihilatedTask != null)
		{
			_onAnnihilatedTask.cancel(false);
			_onAnnihilatedTask = null;
		}
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
	 * Method setValakasSpawnTask.
	 */
	public synchronized static void setValakasSpawnTask()
	{
		if (_valakasSpawnTask == null)
		{
			_valakasSpawnTask = ThreadPoolManager.getInstance().schedule(new SpawnDespawn(1), FWV_APPTIMEOFVALAKAS);
		}
		_entryLocked = true;
	}
	
	/**
	 * Method isEnableEnterToLair.
	 * @return boolean
	 */
	public static boolean isEnableEnterToLair()
	{
		return _state.getState() == EpicBossState.State.NOTSPAWN;
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
	 * Method addValakasMinion.
	 * @param npc NpcInstance
	 */
	public static void addValakasMinion(NpcInstance npc)
	{
		_spawnedMinions.add(npc);
	}
	
	/**
	 * Method init.
	 */
	private void init()
	{
		CharListenerList.addGlobal(this);
		_state = new EpicBossState(Valakas);
		_zone = ReflectionUtils.getZone("[valakas_epic]");
		_log.info("ValakasManager: State of Valakas is " + _state.getState() + ".");
		if (!_state.getState().equals(EpicBossState.State.NOTSPAWN))
		{
			setIntervalEndTask();
		}
		_log.info("ValakasManager: Next spawn date of Valakas is " + TimeUtils.toSimpleFormat(_state.getRespawnDate()) + ".");
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
		CommandChannel cc = ccleader.getParty().getCommandChannel();
		if (cc.getChannelLeader() != ccleader)
		{
			ccleader.sendPacket(Msg.ONLY_THE_ALLIANCE_CHANNEL_LEADER_CAN_ATTEMPT_ENTRY);
			return;
		}
		if (cc.getMemberCount() > 200)
		{
			ccleader.sendMessage("The maximum of 200 players can invade the Valakas Nest");
			return;
		}
		if (_state.getState() != EpicBossState.State.NOTSPAWN)
		{
			ccleader.sendMessage("Valakas is still reborning. You cannot invade the nest now");
			return;
		}
		if (_entryLocked || (_state.getState() == EpicBossState.State.ALIVE))
		{
			ccleader.sendMessage("Valakas has already been reborned and is being attacked. The entrance is sealed.");
			return;
		}
		for (Player p : cc)
		{
			if (p.isDead() || p.isFlying() || p.isCursedWeaponEquipped() || !p.isInRange(ccleader, 500))
			{
				ccleader.sendMessage("Command Channel member " + p.getName() + " doesn't meet the requirements to enter the nest");
				return;
			}
		}
		for (Player p : cc)
		{
			p.teleToLocation(TELEPORT_POSITION);
		}
		setValakasSpawnTask();
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
	}
}
