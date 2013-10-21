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
package events.DestructionOfFlag;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.collections.LazyArrayList;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerExitListener;
import lineage2.gameserver.listener.actor.player.OnTeleportListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Effect;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.Revive;
import lineage2.gameserver.network.serverpackets.SkillList;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.skills.effects.EffectTemplate;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;
import lineage2.gameserver.utils.ReflectionUtils;

import org.apache.log4j.Logger;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DestructionOfFlag extends Functions implements ScriptFile, OnDeathListener, OnTeleportListener, OnPlayerExitListener
{
	/**
	 * Field _log.
	 */
	static Logger _log = Logger.getLogger(DestructionOfFlag.class.getName());
	/**
	 * Field REMOVE_BUFFS. (value is false)
	 */
	private static final boolean REMOVE_BUFFS = false;
	/**
	 * Field BUFFS_TO_REMOVE.
	 */
	private static final int[][] BUFFS_TO_REMOVE =
	{
		{
			1,
			1
		},
		{
			2
		},
		{
			3,
			1
		}
	};
	/**
	 * Field REWARD.
	 */
	private static final int[] REWARD =
	{
		57,
		2000000000
	};
	/**
	 * Field startTime.
	 */
	private static final String[][] startTime =
	{
		{
			"2:35",
			"2:56"
		},
		{
			"5:35",
			"5:56"
		},
		{
			"8:35",
			"8:56"
		},
		{
			"11:35",
			"11:56"
		},
		{
			"14:30",
			"14:46"
		},
		{
			"17:30",
			"17:46"
		},
		{
			"19:45",
			"19:56"
		},
		{
			"20:45",
			"20:56"
		},
		{
			"23:30",
			"23:46"
		}
	};
	/**
	 * Field MIN_PLAYERS.
	 */
	private static int MIN_PLAYERS = 0;
	/**
	 * Field npcs.
	 */
	@SuppressWarnings("unused")
	private static int[][] npcs =
	{
		{
			31143,
			10000,
			10676,
			-3455,
			0
		},
		{
			31143,
			10000,
			10676,
			-3455,
			0
		},
		{
			31143,
			10000,
			10676,
			-3455,
			0
		}
	};
	/**
	 * Field _spawnNpcs.
	 */
	@SuppressWarnings("unused")
	private static boolean _spawnNpcs = false;
	/**
	 * Field _listAllowSaveBuffs.
	 */
	private static int[] _listAllowSaveBuffs =
	{
		1388,
		1389,
		1068,
		1040,
		1086,
		1085,
		1242,
		1059,
		1240,
		1078,
		1077,
		1303,
		1204,
		1062,
		1542,
		1397,
		1045,
		1048,
		1087,
		1043,
		1268,
		1259,
		1243,
		1035,
		1304,
		1036,
		1191,
		1182,
		1189,
		1352,
		1354,
		1353,
		1393,
		1392,
		1499,
		1501,
		1502,
		1500,
		1519,
		1503,
		1504,
		1251,
		1252,
		1253,
		1002,
		1284,
		1308,
		1309,
		1391,
		1007,
		1009,
		1006,
		1461,
		1010,
		1390,
		1310,
		1362,
		1413,
		1535,
		275,
		276,
		274,
		273,
		271,
		365,
		272,
		277,
		310,
		307,
		311,
		309,
		915,
		530,
		269,
		266,
		264,
		267,
		268,
		265,
		349,
		364,
		764,
		529,
		304,
		270,
		306,
		305,
		308,
		363,
		914,
		4700,
		4703,
		4699,
		4702,
		825,
		828,
		827,
		829,
		826,
		830,
		1356,
		1355,
		1357,
		1363
	};
	/**
	 * Field _listBuff.
	 */
	static int[][][] _listBuff =
	{
		{
			{
				1086,
				2
			},
			{
				4342,
				2
			},
			{
				1068,
				3
			},
			{
				1240,
				3
			},
			{
				1077,
				3
			},
			{
				1242,
				3
			}
		},
		{
			{
				4342,
				2
			},
			{
				1059,
				3
			},
			{
				1085,
				3
			},
			{
				1078,
				6
			},
			{
				1062,
				2
			}
		}
	};
	/**
	 * Field ALLOW_RESTRICT_SKILLS.
	 */
	private static boolean ALLOW_RESTRICT_SKILLS = false;
	/**
	 * Field RESTRICT_SKILLS.
	 */
	private static int[][] RESTRICT_SKILLS =
	{
		{
			1218,
			0
		},
		{
			1234,
			1
		},
		{
			1410,
			2
		}
	};
	/**
	 * Field colors.
	 */
	public static String[] colors =
	{
		"00ff00",
		"ffffff",
		"00ffff"
	};
	/**
	 * Field players_list.
	 */
	private static List<Long> players_list = new CopyOnWriteArrayList<>();
	/**
	 * Field live_list.
	 */
	static List<Long> live_list = new CopyOnWriteArrayList<>();
	/**
	 * Field ALLOW_RESTRICT_ITEMS.
	 */
	private static boolean ALLOW_RESTRICT_ITEMS = false;
	/**
	 * Field RESTRICT_ITEMS.
	 */
	private static int[] RESTRICT_ITEMS =
	{
		725,
		727
	};
	/**
	 * Field PROTECT_IP_ACTIVE.
	 */
	private static boolean PROTECT_IP_ACTIVE = false;
	/**
	 * Field _startTask.
	 */
	private static ScheduledFuture<?> _startTask;
	/**
	 * Field _saveBuffList.
	 */
	static HashMap<Long, LazyArrayList<Effect>> _saveBuffList = new HashMap<>();
	/**
	 * Field _spawns.
	 */
	private static List<SimpleSpawner> _spawns = new ArrayList<>();
	/**
	 * Field EVENT_MANAGER_ID.
	 */
	private static int EVENT_MANAGER_ID = 31143;
	
	/**
	 * Method spawnNpcs.
	 */
	private static void spawnNpcs()
	{
		final int[][] EVENT_MANAGERS =
		{
			{
				10000,
				10676,
				-3455,
				0
			},
			{
				10000,
				10676,
				-3455,
				0
			},
			{
				10000,
				10676,
				-3455,
				0
			}
		};
		SpawnNPCs(EVENT_MANAGER_ID, EVENT_MANAGERS, _spawns);
	}
	
	/**
	 * Method despawnNpcs.
	 */
	private static void despawnNpcs()
	{
		deSpawnNPCs(_spawns);
	}
	
	/**
	 * Method onPlayerExit.
	 * @param player Player
	 * @see lineage2.gameserver.listener.actor.player.OnPlayerExitListener#onPlayerExit(Player)
	 */
	@Override
	public void onPlayerExit(Player player)
	{
		if (player.getTeam() == TeamType.NONE)
		{
			return;
		}
		if ((_status == 0) && _isRegistrationActive && live_list.contains(player.getStoredId()))
		{
			removePlayer(player);
			return;
		}
		if ((_status == 1) && live_list.contains(player.getStoredId()))
		{
			removePlayer(player);
			try
			{
				String var = player.getVar("DestructionOfFlag_backCoords");
				if ((var == null) || var.equals(""))
				{
					return;
				}
				String[] coords = var.split(" ");
				if (coords.length != 4)
				{
					return;
				}
				player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]), Integer.parseInt(coords[3]));
				player.unsetVar("DestructionOfFlag_backCoords");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return;
		}
		if ((_status > 1) && (player.getTeam() != TeamType.NONE) && live_list.contains(player.getStoredId()))
		{
			removePlayer(player);
			checkLive();
		}
	}
	
	/**
	 * Method checkLive.
	 */
	public static void checkLive()
	{
		List<Long> new_live_list = new CopyOnWriteArrayList<>();
		for (Long storeId : live_list)
		{
			Player player = GameObjectsStorage.getAsPlayer(storeId);
			if (player != null)
			{
				new_live_list.add(storeId);
			}
		}
		live_list = new_live_list;
		for (Player player : getPlayers(live_list))
		{
			if (player.isInZone(_zone) && !player.isDead() && !player.isLogoutStarted())
			{
				player.setTeam(TeamType.RED);
			}
			else
			{
				loosePlayer(player);
			}
		}
		if (live_list.size() <= 1)
		{
			endBattle(0);
		}
	}
	
	/**
	 * Method loosePlayer.
	 * @param player Player
	 */
	private static void loosePlayer(Player player)
	{
		if (player != null)
		{
			live_list.remove(player.getStoredId());
			player.setTeam(TeamType.NONE);
			show(new CustomMessage("scripts.events.LastHero.YouLose", player), player);
		}
	}
	
	/**
	 * Method onDeath.
	 * @param self Creature
	 * @param killer Creature
	 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
	 */
	@SuppressWarnings("unused")
	@Override
	public void onDeath(Creature self, Creature killer)
	{
		if ((_status > 1) && self.isPlayer() && (self.getTeam() != TeamType.NONE) && live_list.contains(self.getStoredId()))
		{
			Player player = (Player) self;
			loosePlayer(player);
			checkLive();
			if ((killer != null) && killer.isPlayer() && ((killer.getPlayer().expertiseIndex - player.expertiseIndex) > 2) && !killer.getPlayer().getIP().equals(player.getIP()))
			{
				addItem((Player) killer, 4657, Math.round(false ? player.getLevel() * 150 : 1 * 150));
			}
		}
	}
	
	/**
	 * Method onTeleport.
	 * @param player Player
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param reflection Reflection
	 * @see lineage2.gameserver.listener.actor.player.OnTeleportListener#onTeleport(Player, int, int, int, Reflection)
	 */
	@Override
	public void onTeleport(Player player, int x, int y, int z, Reflection reflection)
	{
		if (_zone.checkIfInZone(x, y, z, reflection))
		{
			return;
		}
		if ((_status > 1) && (player.getTeam() != TeamType.NONE) && live_list.contains(player.getStoredId()))
		{
			removePlayer(player);
			checkLive();
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class StartTask implements Runnable
	{
		/**
		 * Field endTime.
		 */
		private final String endTime;
		
		/**
		 * Constructor for StartTask.
		 * @param endTime String
		 */
		public StartTask(String endTime)
		{
			this.endTime = endTime;
		}
		
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			if (!_active)
			{
				_log.info("DestructionOfFlag: is not Active");
				return;
			}
			if (isPvPEventStarted())
			{
				_log.info("DestructionOfFlag not started: another event is already running");
				return;
			}
			for (Residence c : ResidenceHolder.getInstance().getResidenceList(Castle.class))
			{
				if ((c.getSiegeEvent() != null) && c.getSiegeEvent().isInProgress())
				{
					_log.debug("LastHero not started: CastleSiege in progress");
					return;
				}
			}
			_log.info("DestructionOfFlag: started, end Time: " + endTime);
			start(new String[]
			{
				"-1",
				"-1",
				endTime
			});
		}
	}
	
	/**
	 * Field startTasks.
	 */
	private static List<ScheduledFuture<?>> startTasks = new ArrayList<>();
	/**
	 * Field players_list1.
	 */
	static LazyArrayList<Long> players_list1 = new LazyArrayList<>();
	/**
	 * Field players_list2.
	 */
	static LazyArrayList<Long> players_list2 = new LazyArrayList<>();
	/**
	 * Field players_list3.
	 */
	static LazyArrayList<Long> players_list3 = new LazyArrayList<>();
	/**
	 * Field players_list4.
	 */
	private static LazyArrayList<Long> players_list4 = new LazyArrayList<>();
	/**
	 * Field whiteFlag.
	 */
	static MonsterInstance whiteFlag = null;
	/**
	 * Field greenFlag.
	 */
	static MonsterInstance greenFlag = null;
	/**
	 * Field yellowFlag.
	 */
	static MonsterInstance yellowFlag = null;
	/**
	 * Field blackFlag.
	 */
	static MonsterInstance blackFlag = null;
	/**
	 * Field _isRegistrationActive.
	 */
	private static boolean _isRegistrationActive = false;
	/**
	 * Field _status.
	 */
	public static int _status = 0;
	/**
	 * Field _time_to_start.
	 */
	private static int _time_to_start;
	/**
	 * Field _category.
	 */
	private static int _category;
	/**
	 * Field _minLevel.
	 */
	private static int _minLevel;
	/**
	 * Field _maxLevel.
	 */
	private static int _maxLevel;
	/**
	 * Field _autoContinue.
	 */
	private static int _autoContinue = 0;
	/**
	 * Field ALLOW_BUFFS.
	 */
	private static boolean ALLOW_BUFFS = true;
	/**
	 * Field ALLOW_CLAN_SKILL.
	 */
	private static boolean ALLOW_CLAN_SKILL = true;
	/**
	 * Field ALLOW_HERO_SKILL.
	 */
	private static boolean ALLOW_HERO_SKILL = true;
	/**
	 * Field EVENT_DestructionOfFlag_rate.
	 */
	private static boolean EVENT_DestructionOfFlag_rate = false;
	/**
	 * Field ALLOW_PETS.
	 */
	private static boolean ALLOW_PETS = true;
	/**
	 * Field TIME_FOR_RES.
	 */
	private static int TIME_FOR_RES = 5;
	/**
	 * Field _zone.
	 */
	private static Zone _zone = ReflectionUtils.getZone("[colosseum_battle]");
	/**
	 * Field _zoneListener.
	 */
	private static ZoneListener _zoneListener = new ZoneListener();
	/**
	 * Field team1loc.
	 */
	private static Location team1loc = new Location(-82952, -44344, -11496, -11396);
	/**
	 * Field team2loc.
	 */
	private static Location team2loc = new Location(-82536, -47016, -11504, -11404);
	/**
	 * Field team3loc.
	 */
	private static Location team3loc = new Location(-80680, -44296, -11496, -11396);
	/**
	 * Field team4loc.
	 */
	private static Location team4loc = new Location(-78680, -41296, -11496, -11204);
	/**
	 * Field _resurrectionList.
	 */
	private static HashMap<Long, ScheduledFuture<?>> _resurrectionList = new HashMap<>();
	
	/**
	 * Method canSpawnPet.
	 * @param player Player
	 * @return boolean
	 */
	public static boolean canSpawnPet(Player player)
	{
		if (players_list1.contains(player.getObjectId()) || players_list2.contains(player.getObjectId()))
		{
			if (!ALLOW_PETS)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
		_zone.addListener(_zoneListener);
		for (String[] s : startTime)
		{
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(s[0].split(":")[0]));
			cal.set(Calendar.MINUTE, Integer.valueOf(s[0].split(":")[1]));
			cal.set(Calendar.SECOND, 0);
			while (cal.getTimeInMillis() < System.currentTimeMillis())
			{
				cal.add(Calendar.DAY_OF_YEAR, 1);
			}
			ScheduledFuture<?> startTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new StartTask(s[1]), cal.getTimeInMillis() - System.currentTimeMillis(), 86400000);
			startTasks.add(startTask);
			spawnNpcs();
		}
		_active = ServerVariables.getString("DestructionOfFlag", "off").equalsIgnoreCase("on");
		_log.info("Loaded Event: DestructionOfFlag");
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		_zone.removeListener(_zoneListener);
		if (_startTask != null)
		{
			_startTask.cancel(false);
			_startTask = null;
		}
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		onReload();
	}
	
	/**
	 * Field _active.
	 */
	static boolean _active = false;
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private static boolean isActive()
	{
		return _active;
	}
	
	/**
	 * Method activateEvent.
	 */
	public void activateEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (!isActive())
		{
			for (String[] s : startTime)
			{
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(s[0].split(":")[0]));
				cal.set(Calendar.MINUTE, Integer.valueOf(s[0].split(":")[1]));
				cal.set(Calendar.SECOND, 0);
				while (cal.getTimeInMillis() < System.currentTimeMillis())
				{
					cal.add(Calendar.DAY_OF_YEAR, 1);
				}
				ScheduledFuture<?> startTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new StartTask(s[1]), cal.getTimeInMillis() - System.currentTimeMillis(), 86400000);
				startTasks.add(startTask);
			}
			ServerVariables.set("DestructionOfFlag", "on");
			_log.info("Event 'DestructionOfFlag' activated.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.DestructionOfFlag.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'DestructionOfFlag' already active.");
		}
		_active = true;
		show("admin/events.htm", player);
	}
	
	/**
	 * Method deactivateEvent.
	 */
	public void deactivateEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (isActive())
		{
			startTasks.clear();
			ServerVariables.unset("DestructionOfFlag");
			_log.info("Event 'DestructionOfFlag' deactivated.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.DestructionOfFlag.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event 'DestructionOfFlag' not active.");
		}
		_active = false;
		show("admin/events.htm", player);
	}
	
	/**
	 * Method isRunned.
	 * @return boolean
	 */
	public static boolean isRunned()
	{
		return _isRegistrationActive || (_status > 0);
	}
	
	/**
	 * Method DialogAppend_31225.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31225(Integer val)
	{
		if (val == 0)
		{
			Player player = getSelf();
			show("data/scripts/events/DestructionOfFlag/31225.html", player);
		}
		return "";
	}
	
	/**
	 * Method getMinLevelForCategory.
	 * @param category int
	 * @return int
	 */
	public static int getMinLevelForCategory(int category)
	{
		switch (category)
		{
			case 1:
				return 30;
			case 2:
				return 40;
			case 3:
				return 50;
			case 4:
				return 62;
			case 5:
				return 72;
			case 6:
				return 86;
		}
		return 0;
	}
	
	/**
	 * Method getMaxLevelForCategory.
	 * @param category int
	 * @return int
	 */
	public static int getMaxLevelForCategory(int category)
	{
		switch (category)
		{
			case 1:
				return 39;
			case 2:
				return 49;
			case 3:
				return 61;
			case 4:
				return 71;
			case 5:
				return 85;
			case 6:
				return 99;
		}
		return 0;
	}
	
	/**
	 * Method getCategory.
	 * @param level int
	 * @return int
	 */
	public static int getCategory(int level)
	{
		if ((level >= 30) && (level <= 39))
		{
			return 1;
		}
		else if ((level >= 40) && (level <= 49))
		{
			return 2;
		}
		else if ((level >= 50) && (level <= 61))
		{
			return 3;
		}
		else if ((level >= 62) && (level <= 71))
		{
			return 4;
		}
		else if ((level >= 72) && (level <= 85))
		{
			return 5;
		}
		else if (level >= 99)
		{
			return 6;
		}
		return 0;
	}
	
	/**
	 * Method start.
	 * @param var String[]
	 */
	public void start(String[] var)
	{
		if (var.length != 3)
		{
			_log.info("Destruction of Flag: Error start, var length: " + var.length);
			return;
		}
		Integer category;
		Integer autoContinue;
		try
		{
			category = Integer.valueOf(var[0]);
			autoContinue = Integer.valueOf(var[1]);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return;
		}
		_category = category;
		_autoContinue = autoContinue;
		if (_category == -1)
		{
			_minLevel = 1;
			_maxLevel = 99;
		}
		else
		{
			_minLevel = getMinLevelForCategory(_category);
			_maxLevel = getMaxLevelForCategory(_category);
		}
		_status = 0;
		_isRegistrationActive = true;
		_time_to_start = 3;
		players_list1 = new LazyArrayList<>();
		players_list2 = new LazyArrayList<>();
		if (whiteFlag != null)
		{
			whiteFlag.deleteMe();
		}
		if (greenFlag != null)
		{
			greenFlag.deleteMe();
		}
		try
		{
			greenFlag = (MonsterInstance) spawn(team1loc, 35426);
			greenFlag.setName("White Flag");
			greenFlag.setLevel(99);
			greenFlag.isParalyzed();
			greenFlag.setCurrentHp(greenFlag.getMaxHp(), true);
			whiteFlag = (MonsterInstance) spawn(team2loc, 35426);
			whiteFlag.setName("Green Flag");
			whiteFlag.setLevel(99);
			whiteFlag.isParalyzed();
			whiteFlag.setCurrentHp(whiteFlag.getMaxHp(), true);
			yellowFlag = (MonsterInstance) spawn(team3loc, 35426);
			yellowFlag.setName("Yellow Flag");
			yellowFlag.setLevel(99);
			yellowFlag.isParalyzed();
			yellowFlag.setCurrentHp(yellowFlag.getMaxHp(), true);
			blackFlag = (MonsterInstance) spawn(team4loc, 35426);
			blackFlag.setName("Black Flag");
			blackFlag.setLevel(99);
			blackFlag.isParalyzed();
			blackFlag.setCurrentHp(blackFlag.getMaxHp(), true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		whiteFlag.decayMe();
		greenFlag.decayMe();
		yellowFlag.decayMe();
		blackFlag.decayMe();
		String[] param =
		{
			String.valueOf(_time_to_start),
			String.valueOf(_minLevel),
			String.valueOf(_maxLevel)
		};
		sayToAll("scripts.events.DestructionOfFlag.AnnouncePreStart", param);
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "question", new Object[0], 10000);
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "announce", new Object[]
		{
			var[2]
		}, 60000);
	}
	
	/**
	 * Method sayToAll.
	 * @param address String
	 * @param replacements String[]
	 */
	public static void sayToAll(String address, String[] replacements)
	{
		Announcements.getInstance().announceByCustomMessage(address, replacements, ChatType.CRITICAL_ANNOUNCE);
	}
	
	/**
	 * Method question.
	 */
	public static void question()
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			if ((player != null) && (player.getLevel() >= _minLevel) && (player.getLevel() <= _maxLevel) && (player.getReflection().getId() <= 0) && !player.isInOlympiadMode())
			{
				player.scriptRequest(new CustomMessage("scripts.events.DestructionOfFlag.AskPlayer", player).toString(), "events.DestructionOfFlag.DestructionOfFlag:addPlayer", new Object[0]);
			}
		}
	}
	
	/**
	 * Method announce.
	 * @param s String
	 */
	public static void announce(String s)
	{
		if (players_list1.isEmpty() || players_list2.isEmpty())
		{
			sayToAll("scripts.events.DestructionOfFlag.AnnounceEventCancelled", null);
			_isRegistrationActive = false;
			_status = 0;
			executeTask("events.DestructionOfFlag.DestructionOfFlag", "autoContinue", new Object[0], 10000);
			return;
		}
		if (_time_to_start > 1)
		{
			_time_to_start--;
			String[] param =
			{
				String.valueOf(_time_to_start),
				String.valueOf(_minLevel),
				String.valueOf(_maxLevel)
			};
			sayToAll("scripts.events.DestructionOfFlag.AnnouncePreStart", param);
			executeTask("events.DestructionOfFlag.DestructionOfFlag", "announce", new Object[]
			{
				s
			}, 60000);
		}
		else
		{
			_status = 1;
			_isRegistrationActive = false;
			sayToAll("scripts.events.DestructionOfFlag.AnnounceEventStarting", null);
			executeTask("events.DestructionOfFlag.DestructionOfFlag", "prepare", new Object[]
			{
				s
			}, 5000);
		}
	}
	
	/**
	 * Method addPlayer.
	 */
	public void addPlayer()
	{
		Player player = getSelf();
		if ((player == null) || !checkPlayer(player, true))
		{
			return;
		}
		int min = Math.min(Math.min(players_list1.size(), players_list2.size()), players_list3.size());
		if (min == players_list1.size())
		{
			players_list1.add(player.getStoredId());
		}
		else if (min == players_list2.size())
		{
			players_list2.add(player.getStoredId());
		}
		else
		{
			players_list3.add(player.getStoredId());
		}
		show(new CustomMessage("scripts.events.DestructionOfFlag.Registered", player), player);
	}
	
	/**
	 * Method checkPlayer.
	 * @param player Player
	 * @param first boolean
	 * @return boolean
	 */
	public static boolean checkPlayer(Player player, boolean first)
	{
		if (first && (!_isRegistrationActive || player.isDead()))
		{
			show(new CustomMessage("scripts.events.Late", player), player);
			return false;
		}
		if (first && players_list.contains(player.getStoredId()))
		{
			show(new CustomMessage("scripts.events.LastHero.Cancelled", player), player);
			return false;
		}
		if ((player.getLevel() < _minLevel) || (player.getLevel() > _maxLevel))
		{
			show(new CustomMessage("scripts.events.LastHero.CancelledLevel", player), player);
			return false;
		}
		if (player.isMounted())
		{
			show(new CustomMessage("scripts.events.LastHero.Cancelled", player), player);
			return false;
		}
		if (player.isInDuel())
		{
			show(new CustomMessage("scripts.events.LastHero.CancelledDuel", player), player);
			return false;
		}
		if (player.getTeam() != TeamType.NONE)
		{
			show(new CustomMessage("scripts.events.LastHero.CancelledOtherEvent", player), player);
			return false;
		}
		if ((player.getOlympiadGame() != null) || (first && Olympiad.isRegistered(player)))
		{
			show(new CustomMessage("scripts.events.LastHero.CancelledOlympiad", player), player);
			return false;
		}
		if (player.isTeleporting())
		{
			show(new CustomMessage("scripts.events.LastHero.CancelledTeleport", player), player);
			return false;
		}
		if (first && PROTECT_IP_ACTIVE && sameIp(player))
		{
			show("Вы не можете учав�?твоват�? на �?венте, �? ва�?им IP уже кто-то зареге�?трирован.", player, null);
			return false;
		}
		if (player.getObserverMode() != 0)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * Method prepare.
	 * @param s String
	 */
	public static void prepare(String s)
	{
		ReflectionUtils.getDoor(17160024).openMe();
		ReflectionUtils.getDoor(17160023).openMe();
		ReflectionUtils.getDoor(17160020).openMe();
		ReflectionUtils.getDoor(17160019).openMe();
		ReflectionUtils.getDoor(17160022).openMe();
		ReflectionUtils.getDoor(17160021).openMe();
		whiteFlag.spawnMe();
		greenFlag.spawnMe();
		yellowFlag.spawnMe();
		blackFlag.spawnMe();
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "ressurectPlayers", new Object[0], 1000);
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "healPlayers", new Object[0], 2000);
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "saveBackCoords", new Object[0], 3000);
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "paralyzePlayers", new Object[0], 4000);
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "teleportPlayersToColiseum", new Object[0], 5000);
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "go", new Object[]
		{
			s
		}, 60000);
		sayToAll("scripts.events.DestructionOfFlag.AnnounceFinalCountdown", null);
	}
	
	/**
	 * Method go.
	 * @param s String
	 */
	public static void go(String s)
	{
		if ((players_list1.size() < MIN_PLAYERS) || (players_list2.size() < MIN_PLAYERS) || (players_list3.size() < MIN_PLAYERS))
		{
			Announcements.getInstance().announceToAll("DestructionOfFlag: �?вент завер�?ен, не было набрано минимал�?ное кол-во уча�?тников.");
			executeTask("events.DestructionOfFlag.DestructionOfFlag", "autoContinue", new Object[0], 1000);
			return;
		}
		_status = 2;
		upParalyzePlayers();
		clearArena();
		sayToAll("scripts.events.DestructionOfFlag.AnnounceFight", null);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(s.split(":")[0]));
		cal.set(Calendar.MINUTE, Integer.valueOf(s.split(":")[1]));
		cal.set(Calendar.SECOND, 0);
		while (cal.getTimeInMillis() < System.currentTimeMillis())
		{
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		ThreadPoolManager.getInstance().schedule(new timer((int) (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000), 0);
	}
	
	/**
	 * Method endBattle.
	 * @param win int
	 */
	public static void endBattle(int win)
	{
		if (_status == 0)
		{
			return;
		}
		_status = 0;
		if (whiteFlag != null)
		{
			whiteFlag.deleteMe();
			whiteFlag = null;
		}
		if (greenFlag != null)
		{
			greenFlag.deleteMe();
			greenFlag = null;
		}
		if (yellowFlag != null)
		{
			yellowFlag.deleteMe();
			yellowFlag = null;
		}
		if (blackFlag != null)
		{
			blackFlag.deleteMe();
			blackFlag = null;
		}
		ReflectionUtils.getDoor(17160024).closeMe();
		ReflectionUtils.getDoor(17160023).closeMe();
		ReflectionUtils.getDoor(17160020).closeMe();
		ReflectionUtils.getDoor(17160019).closeMe();
		ReflectionUtils.getDoor(17160022).closeMe();
		ReflectionUtils.getDoor(17160021).closeMe();
		if (win != 0)
		{
			if (win == 1)
			{
				Announcements.getInstance().announceToAll("�?обедила команда Белых!");
				giveItemsToWinner(win, 1);
			}
			else if (win == 2)
			{
				Announcements.getInstance().announceToAll("�?обедила команда Зеленых!");
				giveItemsToWinner(win, 1);
			}
			else if (win == 3)
			{
				Announcements.getInstance().announceToAll("�?обедила команда Желтых!");
				giveItemsToWinner(win, 1);
			}
			else if (win == 4)
			{
				Announcements.getInstance().announceToAll("�?обедила команда Черных!");
				giveItemsToWinner(win, 1);
			}
		}
		else
		{
			Announcements.getInstance().announceToAll("�?обедив�?их нет.");
		}
		sayToAll("scripts.events.DestructionOfFlag.AnnounceEnd", null);
		end();
		_isRegistrationActive = false;
	}
	
	/**
	 * Method end.
	 */
	public static void end()
	{
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "removeAura", new Object[0], 1000);
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "ressurectPlayers", new Object[0], 2000);
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "healPlayers", new Object[0], 3000);
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "teleportPlayersToSavedCoordsAll", new Object[0], 4000);
		executeTask("events.DestructionOfFlag.DestructionOfFlag", "autoContinue", new Object[0], 10000);
		backBuff();
		despawnNpcs();
	}
	
	/**
	 * Method autoContinue.
	 */
	public void autoContinue()
	{
		players_list1.clear();
		players_list2.clear();
		players_list3.clear();
		players_list4.clear();
		_saveBuffList.clear();
		if (_autoContinue > 0)
		{
			if (_autoContinue >= 6)
			{
				_autoContinue = 0;
				return;
			}
			start(new String[]
			{
				"" + (_autoContinue + 1),
				"" + (_autoContinue + 1)
			});
		}
	}
	
	/**
	 * Method giveItemsToWinner.
	 * @param win int
	 * @param rate double
	 */
	public static void giveItemsToWinner(int win, double rate)
	{
		if (win == 1)
		{
			for (Player player : getPlayers(players_list1))
			{
				for (int i = 0; i < REWARD.length; i += 2)
				{
					addItem(player, REWARD[i], Math.round((EVENT_DestructionOfFlag_rate ? player.getLevel() : 1) * REWARD[i + 1] * rate));
				}
			}
		}
		if (win == 2)
		{
			for (Player player : getPlayers(players_list2))
			{
				for (int i = 0; i < REWARD.length; i += 2)
				{
					addItem(player, REWARD[i], Math.round((EVENT_DestructionOfFlag_rate ? player.getLevel() : 1) * REWARD[i + 1] * rate));
				}
			}
		}
		if (win == 3)
		{
			for (Player player : getPlayers(players_list3))
			{
				for (int i = 0; i < REWARD.length; i += 2)
				{
					addItem(player, REWARD[i], Math.round((EVENT_DestructionOfFlag_rate ? player.getLevel() : 1) * REWARD[i + 1] * rate));
				}
			}
		}
	}
	
	/**
	 * Method saveBackCoords.
	 */
	public static void saveBackCoords()
	{
		for (Player player : getPlayers(players_list1))
		{
			player.setVar("DestructionOfFlag_backCoords", player.getX() + " " + player.getY() + " " + player.getZ() + " " + player.getReflection().getId(), 0);
			player.setVar("DestructionOfFlag_nameColor", Integer.toHexString(player.getNameColor()), 0);
		}
		for (Player player : getPlayers(players_list2))
		{
			player.setVar("DestructionOfFlag_backCoords", player.getX() + " " + player.getY() + " " + player.getZ() + " " + player.getReflection().getId(), 0);
			player.setVar("DestructionOfFlag_nameColor", Integer.toHexString(player.getNameColor()), 0);
		}
		for (Player player : getPlayers(players_list3))
		{
			player.setVar("DestructionOfFlag_backCoords", player.getX() + " " + player.getY() + " " + player.getZ() + " " + player.getReflection().getId(), 0);
			player.setVar("DestructionOfFlag_nameColor", Integer.toHexString(player.getNameColor()), 0);
		}
		for (Player player : getPlayers(players_list4))
		{
			player.setVar("DestructionOfFlag_backCoords", player.getX() + " " + player.getY() + " " + player.getZ() + " " + player.getReflection().getId(), 0);
			player.setVar("DestructionOfFlag_nameColor", Integer.toHexString(player.getNameColor()), 0);
		}
		cleanPlayers();
		clearArena();
	}
	
	/**
	 * Method teleportPlayersToColiseum.
	 */
	public static void teleportPlayersToColiseum()
	{
		for (Player player : getPlayers(players_list1))
		{
			unRide(player);
			unSummonPet(player, true);
			if (REMOVE_BUFFS)
			{
				for (int buff[] : BUFFS_TO_REMOVE)
				{
					List<Effect> effects;
					if ((effects = player.getEffectList().getEffectsBySkillId(buff[0])) != null)
					{
						if (buff.length == 2)
						{
							for (Effect effect : effects)
							{
								if (effect.getSkill().getLevel() == buff[1])
								{
									player.getEffectList().stopEffect(buff[0]);
								}
							}
						}
						else if (buff.length == 1)
						{
							for (Effect effect : effects)
							{
								player.getEffectList().stopEffect(buff[0]);
							}
						}
					}
				}
			}
			Location pos = getLocForPlayer(player.getStoredId());
			if (pos != null)
			{
				player.teleToLocation(pos);
			}
			else
			{
				removePlayer(player);
			}
		}
		for (Player player : getPlayers(players_list2))
		{
			unRide(player);
			unSummonPet(player, true);
			if (REMOVE_BUFFS)
			{
				for (int buff[] : BUFFS_TO_REMOVE)
				{
					List<Effect> effects;
					if ((effects = player.getEffectList().getEffectsBySkillId(buff[0])) != null)
					{
						if (buff.length == 2)
						{
							for (Effect effect : effects)
							{
								if (effect.getSkill().getLevel() == buff[1])
								{
									player.getEffectList().stopEffect(buff[0]);
								}
							}
						}
						else if (buff.length == 1)
						{
							for (Effect effect : effects)
							{
								player.getEffectList().stopEffect(buff[0]);
							}
						}
					}
				}
			}
			Location pos = getLocForPlayer(player.getStoredId());
			if (pos != null)
			{
				player.teleToLocation(pos);
			}
			else
			{
				removePlayer(player);
			}
		}
		for (Player player : getPlayers(players_list3))
		{
			unRide(player);
			unSummonPet(player, true);
			if (REMOVE_BUFFS)
			{
				for (int buff[] : BUFFS_TO_REMOVE)
				{
					List<Effect> effects;
					if ((effects = player.getEffectList().getEffectsBySkillId(buff[0])) != null)
					{
						if (buff.length == 2)
						{
							for (Effect effect : effects)
							{
								if (effect.getSkill().getLevel() == buff[1])
								{
									player.getEffectList().stopEffect(buff[0]);
								}
							}
						}
						else if (buff.length == 1)
						{
							for (Effect effect : effects)
							{
								player.getEffectList().stopEffect(buff[0]);
							}
						}
					}
				}
			}
			Location pos = getLocForPlayer(player.getStoredId());
			if (pos != null)
			{
				player.teleToLocation(pos);
			}
			else
			{
				removePlayer(player);
			}
		}
	}
	
	/**
	 * Method teleportPlayersToSavedCoords.
	 * @param command int
	 */
	public static void teleportPlayersToSavedCoords(int command)
	{
		switch (command)
		{
			case 1:
				for (Player player : getPlayers(players_list1))
				{
					teleportPlayerToSavedCoords(player);
				}
				break;
			case 2:
				for (Player player : getPlayers(players_list2))
				{
					teleportPlayerToSavedCoords(player);
				}
				break;
			case 3:
				for (Player player : getPlayers(players_list3))
				{
					teleportPlayerToSavedCoords(player);
				}
				break;
			case 4:
				for (Player player : getPlayers(players_list4))
				{
					teleportPlayerToSavedCoords(player);
				}
				break;
		}
	}
	
	/**
	 * Method teleportPlayersToSavedCoordsAll.
	 */
	public static void teleportPlayersToSavedCoordsAll()
	{
		for (Player player : getPlayers(players_list1))
		{
			teleportPlayerToSavedCoords(player);
		}
		for (Player player : getPlayers(players_list2))
		{
			teleportPlayerToSavedCoords(player);
		}
		for (Player player : getPlayers(players_list3))
		{
			teleportPlayerToSavedCoords(player);
		}
		for (Player player : getPlayers(players_list4))
		{
			teleportPlayerToSavedCoords(player);
		}
	}
	
	/**
	 * Method teleportPlayerToSavedCoords.
	 * @param player Player
	 */
	public static void teleportPlayerToSavedCoords(Player player)
	{
		try
		{
			String var = player.getVar("DestructionOfFlag_backCoords");
			String color = player.getVar("DestructionOfFlag_nameColor");
			if (!color.isEmpty())
			{
				player.setNameColor(Integer.decode("0x" + color));
			}
			if ((var == null) || var.equals(""))
			{
				return;
			}
			String[] coords = var.split(" ");
			if (coords.length != 4)
			{
				return;
			}
			player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]), Integer.parseInt(coords[3]));
			player.unsetVar("DestructionOfFlag_backCoords");
			player.unsetVar("DestructionOfFlag_nameColor");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method paralyzePlayers.
	 */
	public static void paralyzePlayers()
	{
		Skill revengeSkill = SkillTable.getInstance().getInfo(Skill.SKILL_RAID_CURSE, 1);
		for (Player player : getPlayers(players_list))
		{
			player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
			revengeSkill.getEffects(player, player, false, false);
		}
	}
	
	/**
	 * Method upParalyzePlayers.
	 */
	public static void upParalyzePlayers()
	{
		for (Player player : getPlayers(players_list))
		{
			player.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
			player.leaveParty();
		}
	}
	
	/**
	 * Method removeBuff.
	 */
	public static void removeBuff()
	{
		saveBuffList();
		for (Player player : getPlayers(players_list1))
		{
			if (player != null)
			{
				try
				{
					if (player.isCastingNow())
					{
						player.abortCast(true, true);
					}
					if (!ALLOW_CLAN_SKILL)
					{
						if (player.getClan() != null)
						{
							for (Skill skill : player.getClan().getAllSkills())
							{
								player.removeSkill(skill, false);
							}
						}
					}
					if (!ALLOW_HERO_SKILL)
					{
						if (player.isHero())
						{
							Hero.removeSkills(player);
						}
					}
					if (!ALLOW_BUFFS)
					{
						player.getEffectList().stopAllEffects();
						ThreadPoolManager.getInstance().schedule(new buffPlayer(player), 0);
					}
					player.sendPacket(new SkillList(player));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		for (Player player : getPlayers(players_list2))
		{
			if (player != null)
			{
				try
				{
					if (player.isCastingNow())
					{
						player.abortCast(true, true);
					}
					if (!ALLOW_CLAN_SKILL)
					{
						if (player.getClan() != null)
						{
							for (Skill skill : player.getClan().getAllSkills())
							{
								player.removeSkill(skill, false);
							}
						}
					}
					if (!ALLOW_HERO_SKILL)
					{
						if (player.isHero())
						{
							Hero.removeSkills(player);
						}
					}
					if (!ALLOW_BUFFS)
					{
						player.getEffectList().stopAllEffects();
						ThreadPoolManager.getInstance().schedule(new buffPlayer(player), 0);
					}
					player.sendPacket(new SkillList(player));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		for (Player player : getPlayers(players_list3))
		{
			if (player != null)
			{
				try
				{
					if (player.isCastingNow())
					{
						player.abortCast(true, true);
					}
					if (!ALLOW_CLAN_SKILL)
					{
						if (player.getClan() != null)
						{
							for (Skill skill : player.getClan().getAllSkills())
							{
								player.removeSkill(skill, false);
							}
						}
					}
					if (!ALLOW_HERO_SKILL)
					{
						if (player.isHero())
						{
							Hero.removeSkills(player);
						}
					}
					if (!ALLOW_BUFFS)
					{
						player.getEffectList().stopAllEffects();
						ThreadPoolManager.getInstance().schedule(new buffPlayer(player), 0);
					}
					player.sendPacket(new SkillList(player));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Method backBuff.
	 */
	public static void backBuff()
	{
		for (Player player : getPlayers(players_list1))
		{
			if (player == null)
			{
				continue;
			}
			try
			{
				player.getEffectList().stopAllEffects();
				if (!ALLOW_CLAN_SKILL)
				{
					if (player.getClan() != null)
					{
						for (Skill skill : player.getClan().getAllSkills())
						{
							if (skill.getMinPledgeClass() <= player.getPledgeClass())
							{
								player.addSkill(skill, false);
							}
						}
					}
				}
				if (!ALLOW_HERO_SKILL)
				{
					if (player.isHero())
					{
						Hero.addSkills(player);
					}
				}
				player.sendPacket(new SkillList(player));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		for (Player player : getPlayers(players_list2))
		{
			if (player == null)
			{
				continue;
			}
			try
			{
				player.getEffectList().stopAllEffects();
				if (!ALLOW_CLAN_SKILL)
				{
					if (player.getClan() != null)
					{
						for (Skill skill : player.getClan().getAllSkills())
						{
							if (skill.getMinPledgeClass() <= player.getPledgeClass())
							{
								player.addSkill(skill, false);
							}
						}
					}
				}
				if (!ALLOW_HERO_SKILL)
				{
					if (player.isHero())
					{
						Hero.addSkills(player);
					}
				}
				player.sendPacket(new SkillList(player));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		for (Player player : getPlayers(players_list3))
		{
			if (player == null)
			{
				continue;
			}
			try
			{
				player.getEffectList().stopAllEffects();
				if (!ALLOW_CLAN_SKILL)
				{
					if (player.getClan() != null)
					{
						for (Skill skill : player.getClan().getAllSkills())
						{
							if (skill.getMinPledgeClass() <= player.getPledgeClass())
							{
								player.addSkill(skill, false);
							}
						}
					}
				}
				if (!ALLOW_HERO_SKILL)
				{
					if (player.isHero())
					{
						Hero.addSkills(player);
					}
				}
				player.sendPacket(new SkillList(player));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		restoreBuffList();
	}
	
	/**
	 * Method ressurectPlayers.
	 */
	public static void ressurectPlayers()
	{
		for (Player player : getPlayers(players_list1))
		{
			ressurectPlayer(player);
		}
		for (Player player : getPlayers(players_list2))
		{
			ressurectPlayer(player);
		}
		for (Player player : getPlayers(players_list3))
		{
			ressurectPlayer(player);
		}
		for (Player player : getPlayers(players_list4))
		{
			ressurectPlayer(player);
		}
	}
	
	/**
	 * Method ressurectPlayer.
	 * @param player Player
	 */
	public static void ressurectPlayer(Player player)
	{
		if (player.isDead())
		{
			player.restoreExp();
			player.setCurrentCp(player.getMaxCp());
			player.setCurrentHp(player.getMaxHp(), true);
			player.setCurrentMp(player.getMaxMp());
			player.broadcastPacket(new Revive(player));
		}
	}
	
	/**
	 * Method healPlayers.
	 */
	public static void healPlayers()
	{
		for (Player player : getPlayers(players_list1))
		{
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
			player.setCurrentCp(player.getMaxCp());
		}
		for (Player player : getPlayers(players_list2))
		{
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
			player.setCurrentCp(player.getMaxCp());
		}
		for (Player player : getPlayers(players_list3))
		{
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
			player.setCurrentCp(player.getMaxCp());
		}
		for (Player player : getPlayers(players_list4))
		{
			player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
			player.setCurrentCp(player.getMaxCp());
		}
	}
	
	/**
	 * Method cleanPlayers.
	 */
	public static void cleanPlayers()
	{
		for (Player player : getPlayers(players_list1))
		{
			if (!checkPlayer(player, false))
			{
				removePlayer(player);
			}
			else
			{
				setTeam(player);
			}
		}
		for (Player player : getPlayers(players_list2))
		{
			if (!checkPlayer(player, false))
			{
				removePlayer(player);
			}
			else
			{
				setTeam(player);
			}
		}
		for (Player player : getPlayers(players_list3))
		{
			if (!checkPlayer(player, false))
			{
				removePlayer(player);
			}
			else
			{
				setTeam(player);
			}
		}
		for (Player player : getPlayers(players_list4))
		{
			if (!checkPlayer(player, false))
			{
				removePlayer(player);
			}
			else
			{
				setTeam(player);
			}
		}
	}
	
	/**
	 * Method clearArena.
	 */
	public static void clearArena()
	{
		for (GameObject obj : _zone.getObjects())
		{
			if (obj != null)
			{
				Player player = obj.getPlayer();
				if ((player != null) && (playerInCommand(player.getStoredId()) == 0))
				{
					player.teleToLocation(147451, 46728, -3410);
				}
			}
		}
	}
	
	/**
	 * Method doDie.
	 * @param self Creature
	 * @param killer Creature
	 */
	public static void doDie(Creature self, Creature killer)
	{
		if ((_status <= 1) || (self == null))
		{
			return;
		}
		if (self.isPlayer() && (playerInCommand(self.getStoredId()) > 0))
		{
			self.sendMessage("Через " + TIME_FOR_RES + " �?екунд вы будите во�?�?тановлены.");
			_resurrectionList.put(self.getStoredId(), executeTask("events.DestructionOfFlag.DestructionOfFlag", "resurrectAtBase", new Object[]
			{
				(Player) self
			}, TIME_FOR_RES * 100));
		}
		if ((self instanceof MonsterInstance) && ((self == greenFlag) || (self == whiteFlag) || (self == yellowFlag) || (self == blackFlag)))
		{
			lossTeam((MonsterInstance) self);
		}
	}
	
	/**
	 * Method resurrectAtBase.
	 * @param player Player
	 */
	public static void resurrectAtBase(Player player)
	{
		if (playerInCommand(player.getStoredId()) <= 0)
		{
			return;
		}
		if (player.isDead())
		{
			ressurectPlayer(player);
		}
		Location pos = getLocForPlayer(player.getStoredId());
		if (pos != null)
		{
			player.teleToLocation(pos);
		}
		else
		{
			removePlayer(player);
		}
		if (!ALLOW_BUFFS)
		{
			ThreadPoolManager.getInstance().schedule(new buffPlayer(player), 0);
		}
		else
		{
			ThreadPoolManager.getInstance().schedule(new restoreBuffListForPlayer(player), 0);
		}
	}
	
	/**
	 * Method OnEscape.
	 * @param player Player
	 * @return Location
	 */
	public static Location OnEscape(Player player)
	{
		if ((_status > 1) && (player != null) && (playerInCommand(player.getStoredId()) > 0))
		{
			removePlayer(player);
		}
		return null;
	}
	
	/**
	 * Method OnPlayerExit.
	 * @param player Player
	 */
	public static void OnPlayerExit(Player player)
	{
		if ((player == null) || (playerInCommand(player.getStoredId()) < 1))
		{
			return;
		}
		if ((_status == 0) && _isRegistrationActive && (playerInCommand(player.getStoredId()) > 0))
		{
			removePlayer(player);
			return;
		}
		if ((_status == 1) && (playerInCommand(player.getStoredId()) > 0))
		{
			removePlayer(player);
			return;
		}
		OnEscape(player);
	}
	
	/**
	 */
	public static class TeleportTask implements Runnable
	{
		/**
		 * Field loc.
		 */
		Location loc;
		/**
		 * Field target.
		 */
		Creature target;
		
		/**
		 * Constructor for TeleportTask.
		 * @param target Creature
		 * @param loc Location
		 */
		public TeleportTask(Creature target, Location loc)
		{
			this.target = target;
			this.loc = loc;
			target.startStunning();
		}
		
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			target.stopStunning();
			target.teleToLocation(loc);
		}
	}
	
	/**
	 * Method removePlayer.
	 * @param player Player
	 */
	private static void removePlayer(Player player)
	{
		players_list1.remove(player.getStoredId());
		players_list2.remove(player.getStoredId());
		players_list3.remove(player.getStoredId());
		players_list4.remove(player.getStoredId());
		teleportPlayerToSavedCoords(player);
	}
	
	/**
	 * Method getPlayers.
	 * @param list List<Long>
	 * @return LazyArrayList<Player>
	 */
	static LazyArrayList<Player> getPlayers(List<Long> list)
	{
		LazyArrayList<Player> result = new LazyArrayList<>();
		for (Long storeId : list)
		{
			Player player = GameObjectsStorage.getAsPlayer(storeId);
			if (player != null)
			{
				result.add(player);
			}
		}
		return result;
	}
	
	/**
	 * Method saveBuffList.
	 */
	public static void saveBuffList()
	{
		Effect skill[];
		for (Player player : getPlayers(players_list1))
		{
			if (player != null)
			{
				skill = player.getEffectList().getAllFirstEffects();
				if (skill.length == 0)
				{
					continue;
				}
				for (Effect effect : skill)
				{
					if (!_saveBuffList.containsKey(player.getStoredId()))
					{
						_saveBuffList.put(player.getStoredId(), new LazyArrayList<Effect>());
					}
					for (int id : _listAllowSaveBuffs)
					{
						if (effect.getSkill().getId() == id)
						{
							_saveBuffList.get(player.getStoredId()).add(effect);
						}
					}
				}
			}
		}
		for (Player player : getPlayers(players_list2))
		{
			if (player != null)
			{
				skill = player.getEffectList().getAllFirstEffects();
				if (skill.length == 0)
				{
					continue;
				}
				for (Effect effect : skill)
				{
					if (!_saveBuffList.containsKey(player.getStoredId()))
					{
						_saveBuffList.put(player.getStoredId(), new LazyArrayList<Effect>());
					}
					for (int id : _listAllowSaveBuffs)
					{
						if (effect.getSkill().getId() == id)
						{
							_saveBuffList.get(player.getStoredId()).add(effect);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Method restoreBuffList.
	 */
	public static void restoreBuffList()
	{
		Player player;
		for (long objId : _saveBuffList.keySet())
		{
			player = GameObjectsStorage.getAsPlayer(objId);
			ThreadPoolManager.getInstance().schedule(new restoreBuffListForPlayer(player), 100);
		}
	}
	
	/**
	 */
	public static class restoreBuffListForPlayer implements Runnable
	{
		/**
		 * Field player.
		 */
		Player player;
		
		/**
		 * Constructor for restoreBuffListForPlayer.
		 * @param player Player
		 */
		restoreBuffListForPlayer(Player player)
		{
			this.player = player;
		}
		
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			if (player == null)
			{
				return;
			}
			player.getEffectList().stopAllEffects();
			LazyArrayList<Effect> effects = _saveBuffList.get(player.getStoredId());
			if ((effects != null) && (effects.size() > 0))
			{
				for (Effect effect : effects)
				{
					for (EffectTemplate et : effect.getSkill().getEffectTemplates())
					{
						Env env = new Env(player, player, effect.getSkill());
						env.value = Integer.MAX_VALUE;
						Effect e = et.getEffect(env);
						e.setPeriod(effect.getPeriod());
						e.getEffected().getEffectList().addEffect(e);
					}
					try
					{
						Thread.sleep(150);
					}
					catch (Exception e)
					{
					}
				}
			}
			player.setCurrentCp(player.getMaxCp());
			player.setCurrentHp(player.getMaxHp(), true);
			player.setCurrentMp(player.getMaxMp());
		}
	}
	
	/**
	 */
	public static class buffPlayer implements Runnable
	{
		/**
		 * Field player.
		 */
		Player player;
		
		/**
		 * Constructor for buffPlayer.
		 * @param player Player
		 */
		buffPlayer(Player player)
		{
			this.player = player;
		}
		
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			if (player == null)
			{
				return;
			}
			Skill skill;
			for (int[] buff : _listBuff[player.isMageClass() ? 1 : 0])
			{
				skill = SkillTable.getInstance().getInfo(buff[0], buff[1]);
				for (EffectTemplate et : skill.getEffectTemplates())
				{
					Env env = new Env(player, player, skill);
					env.value = Integer.MAX_VALUE;
					Effect e = et.getEffect(env);
					e.setPeriod(600000);
					e.getEffected().getEffectList().addEffect(e);
				}
				try
				{
					Thread.sleep(150);
				}
				catch (Exception e)
				{
				}
			}
			player.setCurrentCp(player.getMaxCp());
			player.setCurrentHp(player.getMaxHp(), true);
			player.setCurrentMp(player.getMaxMp());
		}
	}
	
	/**
	 */
	public static class timer implements Runnable
	{
		/**
		 * Field time.
		 */
		int time;
		
		/**
		 * Constructor for timer.
		 * @param time int
		 */
		timer(int time)
		{
			this.time = time;
		}
		
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			int sec;
			String message;
			while ((time > 0) && (_status == 2))
			{
				sec = time - ((time / 60) * 60);
				for (Player player : getPlayers(players_list1))
				{
					if (sec < 10)
					{
						message = " �?�?тало�?�? минут: " + Integer.toString(time / 60) + ":0" + Integer.toString(sec) + " ";
					}
					else
					{
						message = " �?�?тало�?�? минут: " + Integer.toString(time / 60) + ":" + Integer.toString(sec) + " ";
					}
					if (greenFlag != null)
					{
						message += "\n Green Flag: " + greenFlag.getCurrentHp() + " Hp ";
					}
					if (whiteFlag != null)
					{
						message += "\n White Flag: " + whiteFlag.getCurrentHp() + " Hp ";
					}
					if (yellowFlag != null)
					{
						message += "\n Yellow Flag: " + yellowFlag.getCurrentHp() + " Hp ";
					}
					if (blackFlag != null)
					{
						message += "\n Black Flag: " + blackFlag.getCurrentHp() + " Hp ";
					}
					player.sendPacket(new ExShowScreenMessage(message, 2000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, false));
				}
				for (Player player : getPlayers(players_list2))
				{
					if (sec < 10)
					{
						message = " �?�?тало�?�? минут: " + Integer.toString(time / 60) + ":0" + Integer.toString(sec) + " ";
					}
					else
					{
						message = " �?�?тало�?�? минут: " + Integer.toString(time / 60) + ":" + Integer.toString(sec) + " ";
					}
					if (whiteFlag != null)
					{
						message += "\n White Flag: " + whiteFlag.getCurrentHp() + " Hp ";
					}
					if (greenFlag != null)
					{
						message += "\n Green Flag: " + greenFlag.getCurrentHp() + " Hp ";
					}
					if (yellowFlag != null)
					{
						message += "\n Yellow Flag: " + yellowFlag.getCurrentHp() + " Hp ";
					}
					if (blackFlag != null)
					{
						message += "\n Black Flag: " + blackFlag.getCurrentHp() + " Hp ";
					}
					player.sendPacket(new ExShowScreenMessage(message, 2000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, false));
				}
				for (Player player : getPlayers(players_list3))
				{
					if (sec < 10)
					{
						message = " �?�?тало�?�? минут: " + Integer.toString(time / 60) + ":0" + Integer.toString(sec) + " ";
					}
					else
					{
						message = " �?�?тало�?�? минут: " + Integer.toString(time / 60) + ":" + Integer.toString(sec) + " ";
					}
					if (blackFlag != null)
					{
						message += "\n Black Flag: " + blackFlag.getCurrentHp() + " Hp ";
					}
					if (yellowFlag != null)
					{
						message += "\n Yellow Flag: " + yellowFlag.getCurrentHp() + " Hp ";
					}
					if (greenFlag != null)
					{
						message += "\n Green Flag: " + greenFlag.getCurrentHp() + " Hp ";
					}
					if (whiteFlag != null)
					{
						message += "\n White Flag: " + whiteFlag.getCurrentHp() + " Hp ";
					}
					player.sendPacket(new ExShowScreenMessage(message, 2000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, false));
				}
				try
				{
					Thread.sleep(1000);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				time--;
			}
			endBattle(0);
		}
	}
	
	/**
	 * Method playerInCommand.
	 * @param objectId long
	 * @return int
	 */
	public static int playerInCommand(long objectId)
	{
		return players_list1.contains(objectId) ? 1 : players_list2.contains(objectId) ? 2 : players_list3.contains(objectId) ? 3 : 0;
	}
	
	/**
	 * Method getLocForPlayer.
	 * @param objectId long
	 * @return Location
	 */
	public static Location getLocForPlayer(long objectId)
	{
		switch (playerInCommand(objectId))
		{
			case 1:
				return (Location.coordsRandomize(team1loc, 50, 200));
			case 2:
				return (Location.coordsRandomize(team2loc, 50, 200));
			case 3:
				return (Location.coordsRandomize(team3loc, 50, 200));
			case 4:
				return (Location.coordsRandomize(team4loc, 50, 200));
			default:
				return null;
		}
	}
	
	/**
	 * Method setTeam.
	 * @param player Player
	 */
	public static void setTeam(Player player)
	{
		int command = playerInCommand(player.getStoredId());
		if ((command < 1) || (command > 3))
		{
			removePlayer(player);
			return;
		}
		player.setNameColor(Integer.decode("0x" + colors[playerInCommand(player.getStoredId()) - 1]));
	}
	
	/**
	 * Method lossTeam.
	 * @param flag MonsterInstance
	 */
	public static void lossTeam(MonsterInstance flag)
	{
		if (flag == greenFlag)
		{
			lossTeam(players_list1);
			greenFlag.deleteMe();
			if (players_list2.isEmpty())
			{
				endBattle(2);
			}
			else if (players_list2.isEmpty())
			{
				endBattle(3);
			}
			else if (players_list2.isEmpty())
			{
				endBattle(4);
			}
			else if (players_list3.isEmpty())
			{
				endBattle(2);
			}
			else if (players_list3.isEmpty())
			{
				endBattle(3);
			}
			else if (players_list3.isEmpty())
			{
				endBattle(4);
			}
			else if (players_list4.isEmpty())
			{
				endBattle(2);
			}
			else if (players_list4.isEmpty())
			{
				endBattle(3);
			}
			else if (players_list4.isEmpty())
			{
				endBattle(4);
			}
		}
		if (flag == whiteFlag)
		{
			lossTeam(players_list2);
			whiteFlag.deleteMe();
			if (players_list1.isEmpty())
			{
				endBattle(1);
			}
			else if (players_list1.isEmpty())
			{
				endBattle(3);
			}
			else if (players_list1.isEmpty())
			{
				endBattle(4);
			}
			else if (players_list3.isEmpty())
			{
				endBattle(1);
			}
			else if (players_list3.isEmpty())
			{
				endBattle(3);
			}
			else if (players_list3.isEmpty())
			{
				endBattle(4);
			}
			else if (players_list4.isEmpty())
			{
				endBattle(1);
			}
			else if (players_list4.isEmpty())
			{
				endBattle(3);
			}
			else if (players_list4.isEmpty())
			{
				endBattle(4);
			}
		}
		if (flag == yellowFlag)
		{
			lossTeam(players_list3);
			yellowFlag.deleteMe();
			if (players_list1.isEmpty())
			{
				endBattle(1);
			}
			else if (players_list1.isEmpty())
			{
				endBattle(2);
			}
			else if (players_list1.isEmpty())
			{
				endBattle(4);
			}
			else if (players_list2.isEmpty())
			{
				endBattle(1);
			}
			else if (players_list2.isEmpty())
			{
				endBattle(2);
			}
			else if (players_list2.isEmpty())
			{
				endBattle(4);
			}
			else if (players_list4.isEmpty())
			{
				endBattle(1);
			}
			else if (players_list4.isEmpty())
			{
				endBattle(2);
			}
			else if (players_list4.isEmpty())
			{
				endBattle(4);
			}
		}
		if (flag == blackFlag)
		{
			lossTeam(players_list4);
			blackFlag.deleteMe();
			if (players_list1.isEmpty())
			{
				endBattle(1);
			}
			else if (players_list1.isEmpty())
			{
				endBattle(2);
			}
			else if (players_list1.isEmpty())
			{
				endBattle(3);
			}
			else if (players_list2.isEmpty())
			{
				endBattle(1);
			}
			else if (players_list2.isEmpty())
			{
				endBattle(2);
			}
			else if (players_list2.isEmpty())
			{
				endBattle(3);
			}
			else if (players_list3.isEmpty())
			{
				endBattle(1);
			}
			else if (players_list3.isEmpty())
			{
				endBattle(2);
			}
			else if (players_list3.isEmpty())
			{
				endBattle(3);
			}
		}
		flag.deleteMe();
	}
	
	/**
	 * Method lossTeam.
	 * @param team LazyArrayList<Long>
	 */
	public static void lossTeam(LazyArrayList<Long> team)
	{
		Player player;
		for (long objId : team)
		{
			player = GameObjectsStorage.getAsPlayer(objId);
			if (player != null)
			{
				removePlayer(player);
				player.sendMessage("Ва�? фла�� - уничтожен. Вы проиграли.");
			}
		}
		team.clear();
	}
	
	/**
	 * Method canJoinParty.
	 * @param player Player
	 * @param target Player
	 * @return boolean
	 */
	public static boolean canJoinParty(Player player, Player target)
	{
		return !((playerInCommand(player.getStoredId()) > 0) || (playerInCommand(target.getStoredId()) > 0)) || (playerInCommand(player.getStoredId()) == playerInCommand(target.getStoredId()));
	}
	
	/**
	 * Method canUseItem.
	 * @param player Player
	 * @param item ItemInstance
	 * @return boolean
	 */
	public static boolean canUseItem(Player player, ItemInstance item)
	{
		if (ALLOW_RESTRICT_ITEMS && (playerInCommand(player.getStoredId()) > 0))
		{
			for (int restrict_id : RESTRICT_ITEMS)
			{
				if (item.getItemId() == restrict_id)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Method useSkill.
	 * @param player Creature
	 * @param target Creature
	 * @param skill Skill
	 * @return boolean
	 */
	public static boolean useSkill(Creature player, Creature target, Skill skill)
	{
		return checkTarget(player, target, skill);
	}
	
	/**
	 * Method checkTarget.
	 * @param player Player
	 * @param target Creature
	 * @return boolean
	 */
	public static boolean checkTarget(Player player, Creature target)
	{
		return checkTarget(player, target, null);
	}
	
	/**
	 * Method checkTarget.
	 * @param character Creature
	 * @param target Creature
	 * @param skill Skill
	 * @return boolean
	 */
	public static boolean checkTarget(Creature character, Creature target, Skill skill)
	{
		if (_status < 2)
		{
			return true;
		}
		if ((character instanceof Player) && (target != null) && (target != character))
		{
			if (playerInCommand(character.getStoredId()) > 0)
			{
				if (target instanceof MonsterInstance)
				{
					if (getMonsterTeam(target) == playerInCommand(character.getObjectId()))
					{
						_log.info("Monster Team: " + getMonsterTeam(target) + " | Player Team: " + playerInCommand(character.getObjectId()));
						return false;
					}
					return true;
				}
				if (skill != null)
				{
					if (ALLOW_RESTRICT_SKILLS)
					{
						for (int[] restrict : RESTRICT_SKILLS)
						{
							if (skill.getId() == restrict[0])
							{
								if (restrict[1] == 0)
								{
									return character.getStoredId().equals(target.getStoredId());
								}
								if (restrict[1] == 1)
								{
									return playerInCommand(character.getStoredId()) == playerInCommand(target.getStoredId());
								}
							}
						}
					}
					if (playerInCommand(target.getStoredId()) > 0)
					{
						switch (skill.getSkillType())
						{
							case BUFF:
							case HEAL:
							case HEAL_PERCENT:
							case BALANCE:
							case COMBATPOINTHEAL:
							case MANAHEAL:
							case MANAHEAL_PERCENT:
								return playerInCommand(character.getStoredId()) == playerInCommand(target.getStoredId());
							default:
								for (Creature targ : skill.getTargets(character, target, true))
								{
									if (targ instanceof Player)
									{
										if (playerInCommand(character.getStoredId()) == playerInCommand(targ.getStoredId()))
										{
											return false;
										}
									}
									else if (getMonsterTeam(targ) == playerInCommand(character.getObjectId()))
									{
										return false;
									}
								}
						}
					}
					if (playerInCommand(target.getStoredId()) == 0)
					{
						switch (skill.getSkillType())
						{
							case MDAM:
							case PDAM:
							case DISCORD:
							case AGGRESSION:
							case BLEED:
							case STUN:
							case DEBUFF:
								return playerInCommand(character.getStoredId()) != playerInCommand(target.getStoredId());
							default:
								for (Creature targ : skill.getTargets(character, target, true))
								{
									if (targ instanceof Player)
									{
										if (playerInCommand(character.getStoredId()) != playerInCommand(targ.getStoredId()))
										{
											return false;
										}
									}
									else if (getMonsterTeam(targ) == playerInCommand(character.getObjectId()))
									{
										return false;
									}
								}
						}
					}
					for (Creature targ : skill.getTargets(character, target, true))
					{
						if (targ instanceof Player)
						{
							if (playerInCommand(character.getStoredId()) == playerInCommand(targ.getStoredId()))
							{
								return false;
							}
						}
						else if (getMonsterTeam(targ) == playerInCommand(character.getObjectId()))
						{
							return false;
						}
					}
				}
				return playerInCommand(character.getStoredId()) != playerInCommand(target.getStoredId());
			}
			if ((playerInCommand(target.getStoredId()) > 0) || (getMonsterTeam(target) > 0))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method getMonsterTeam.
	 * @param monster Creature
	 * @return int
	 */
	private static int getMonsterTeam(Creature monster)
	{
		if (monster.getStoredId().equals(greenFlag.getStoredId()))
		{
			return 1;
		}
		else if (monster.getStoredId().equals(whiteFlag.getStoredId()))
		{
			return 2;
		}
		else if (monster.getStoredId().equals(yellowFlag.getStoredId()))
		{
			return 3;
		}
		else if (monster.getStoredId().equals(blackFlag.getStoredId()))
		{
			return 4;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Method sameIp.
	 * @param player Player
	 * @return boolean
	 */
	public static boolean sameIp(Player player)
	{
		Player part;
		for (long objId : players_list1)
		{
			part = GameObjectsStorage.getAsPlayer(objId);
			if (part == null)
			{
				continue;
			}
			if (player.getNetConnection().getIpAddr().equals(part.getNetConnection().getIpAddr()))
			{
				return true;
			}
		}
		for (long objId : players_list2)
		{
			part = GameObjectsStorage.getAsPlayer(objId);
			if (part == null)
			{
				continue;
			}
			if (player.getNetConnection().getIpAddr().startsWith(part.getNetConnection().getIpAddr()))
			{
				return true;
			}
		}
		for (long objId : players_list3)
		{
			part = GameObjectsStorage.getAsPlayer(objId);
			if (part == null)
			{
				continue;
			}
			if (player.getNetConnection().getIpAddr().startsWith(part.getNetConnection().getIpAddr()))
			{
				return true;
			}
		}
		for (long objId : players_list4)
		{
			part = GameObjectsStorage.getAsPlayer(objId);
			if (part == null)
			{
				continue;
			}
			if (player.getNetConnection().getIpAddr().startsWith(part.getNetConnection().getIpAddr()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 */
	private static class ZoneListener implements OnZoneEnterLeaveListener
	{
		/**
		 * Constructor for ZoneListener.
		 */
		public ZoneListener()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
			if (cha == null)
			{
				return;
			}
			Player player = cha.getPlayer();
			if ((_status > 0) && (player != null) && !live_list.contains(player.getStoredId()))
			{
				ThreadPoolManager.getInstance().schedule(new TeleportTask(cha, new Location(147451, 46728, -3410)), 3000);
			}
		}
		
		/**
		 * Method onZoneLeave.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
		 */
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
			if (cha == null)
			{
				return;
			}
			Player player = cha.getPlayer();
			if ((_status > 1) && (player != null) && (player.getTeam() != TeamType.NONE) && live_list.contains(player.getStoredId()))
			{
				double angle = PositionUtils.convertHeadingToDegree(cha.getHeading());
				double radian = Math.toRadians(angle - 90);
				int x = (int) (cha.getX() + (50 * Math.sin(radian)));
				int y = (int) (cha.getY() - (50 * Math.cos(radian)));
				int z = cha.getZ();
				ThreadPoolManager.getInstance().schedule(new TeleportTask(cha, new Location(x, y, z)), 3000);
			}
		}
	}
}
