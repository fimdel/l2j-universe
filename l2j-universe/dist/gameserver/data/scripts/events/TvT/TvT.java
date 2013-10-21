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
package events.TvT;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.geometry.Polygon;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerExitListener;
import lineage2.gameserver.listener.actor.player.OnTeleportListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.base.TeamType;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.network.serverpackets.Revive;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;
import lineage2.gameserver.utils.ReflectionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TvT extends Functions implements ScriptFile, OnDeathListener, OnTeleportListener, OnPlayerExitListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(TvT.class);
	
	/**
	 * @author Mobius
	 */
	public class StartTask extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!_active)
			{
				return;
			}
			if (isPvPEventStarted())
			{
				_log.info("TvT not started: another event is already running");
				return;
			}
			if (!Rnd.chance(Config.EVENT_TvTChanceToStart))
			{
				_log.debug("TvT not started: chance");
				return;
			}
			for (Residence c : ResidenceHolder.getInstance().getResidenceList(Castle.class))
			{
				if ((c.getSiegeEvent() != null) && c.getSiegeEvent().isInProgress())
				{
					_log.debug("TvT not started: CastleSiege in progress");
					return;
				}
			}
			start(new String[]
			{
				"1",
				"1"
			});
		}
	}
	
	/**
	 * Field _startTask.
	 */
	private static ScheduledFuture<?> _startTask;
	/**
	 * Field players_list1.
	 */
	private static List<Long> players_list1 = new CopyOnWriteArrayList<>();
	/**
	 * Field players_list2.
	 */
	private static List<Long> players_list2 = new CopyOnWriteArrayList<>();
	/**
	 * Field live_list1.
	 */
	static List<Long> live_list1 = new CopyOnWriteArrayList<>();
	/**
	 * Field live_list2.
	 */
	static List<Long> live_list2 = new CopyOnWriteArrayList<>();
	/**
	 * Field _isRegistrationActive.
	 */
	private static boolean _isRegistrationActive = false;
	/**
	 * Field _status.
	 */
	static int _status = 0;
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
	 * Field _endTask.
	 */
	private static ScheduledFuture<?> _endTask;
	/**
	 * Field _zone.
	 */
	private static Zone _zone = ReflectionUtils.getZone("[colosseum_battle]");
	/**
	 * Field _zoneListener.
	 */
	private static ZoneListener _zoneListener = new ZoneListener();
	/**
	 * Field team1spawn.
	 */
	private static Territory team1spawn = new Territory().add(new Polygon().add(149878, 47505).add(150262, 47513).add(150502, 47233).add(150507, 46300).add(150256, 46002).add(149903, 46005).setZmin(-3408).setZmax(-3308));
	/**
	 * Field team2spawn.
	 */
	private static Territory team2spawn = new Territory().add(new Polygon().add(149027, 46005).add(148686, 46003).add(148448, 46302).add(148449, 47231).add(148712, 47516).add(149014, 47527).setZmin(-3408).setZmax(-3308));
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
		_zone.addListener(_zoneListener);
		_startTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new StartTask(), 3600000, 3600000);
		_active = ServerVariables.getString("TvT", "off").equalsIgnoreCase("on");
		_log.info("Loaded Event: TvT");
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
			if (_startTask == null)
			{
				_startTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new StartTask(), 3600000, 3600000);
			}
			ServerVariables.set("TvT", "on");
			_log.info("Event 'TvT' activated.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.TvT.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'TvT' already active.");
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
			if (_startTask != null)
			{
				_startTask.cancel(false);
				_startTask = null;
			}
			ServerVariables.unset("TvT");
			_log.info("Event 'TvT' deactivated.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.TvT.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event 'TvT' not active.");
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
	 * Method getMinLevelForCategory.
	 * @param category int
	 * @return int
	 */
	public static int getMinLevelForCategory(int category)
	{
		switch (category)
		{
			case 1:
				return 20;
			case 2:
				return 30;
			case 3:
				return 40;
			case 4:
				return 52;
			case 5:
				return 62;
			case 6:
				return 76;
			case 7:
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
				return 29;
			case 2:
				return 39;
			case 3:
				return 51;
			case 4:
				return 61;
			case 5:
				return 75;
			case 6:
				return 85;
			case 7:
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
		if ((level >= 20) && (level <= 29))
		{
			return 1;
		}
		else if ((level >= 30) && (level <= 39))
		{
			return 2;
		}
		else if ((level >= 40) && (level <= 51))
		{
			return 3;
		}
		else if ((level >= 52) && (level <= 61))
		{
			return 4;
		}
		else if ((level >= 62) && (level <= 75))
		{
			return 5;
		}
		else if ((level >= 76) && (level <= 85))
		{
			return 6;
		}
		else if (level >= 86)
		{
			return 7;
		}
		return 0;
	}
	
	/**
	 * Method start.
	 * @param var String[]
	 */
	public void start(String[] var)
	{
		Player player = getSelf();
		if (var.length != 2)
		{
			show(new CustomMessage("common.Error", player), player);
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
			show(new CustomMessage("common.Error", player), player);
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
		if (_endTask != null)
		{
			show(new CustomMessage("common.TryLater", player), player);
			return;
		}
		_status = 0;
		_isRegistrationActive = true;
		_time_to_start = Config.EVENT_TvTTime;
		players_list1 = new CopyOnWriteArrayList<>();
		players_list2 = new CopyOnWriteArrayList<>();
		live_list1 = new CopyOnWriteArrayList<>();
		live_list2 = new CopyOnWriteArrayList<>();
		String[] param =
		{
			String.valueOf(_time_to_start),
			String.valueOf(_minLevel),
			String.valueOf(_maxLevel)
		};
		sayToAll("scripts.events.TvT.AnnouncePreStart", param);
		executeTask("events.TvT.TvT", "question", new Object[0], 10000);
		executeTask("events.TvT.TvT", "announce", new Object[0], 60000);
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
			if ((player != null) && !player.isDead() && (player.getLevel() >= _minLevel) && (player.getLevel() <= _maxLevel) && player.getReflection().isDefault() && !player.isInOlympiadMode() && !player.isInObserverMode())
			{
				player.scriptRequest(new CustomMessage("scripts.events.TvT.AskPlayer", player).toString(), "events.TvT.TvT:addPlayer", new Object[0]);
			}
		}
	}
	
	/**
	 * Method announce.
	 */
	public static void announce()
	{
		if (players_list1.isEmpty() || players_list2.isEmpty())
		{
			sayToAll("scripts.events.TvT.AnnounceEventCancelled", null);
			_isRegistrationActive = false;
			_status = 0;
			executeTask("events.TvT.TvT", "autoContinue", new Object[0], 10000);
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
			sayToAll("scripts.events.TvT.AnnouncePreStart", param);
			executeTask("events.TvT.TvT", "announce", new Object[0], 60000);
		}
		else
		{
			_status = 1;
			_isRegistrationActive = false;
			sayToAll("scripts.events.TvT.AnnounceEventStarting", null);
			executeTask("events.TvT.TvT", "prepare", new Object[0], 5000);
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
		int team = 0, size1 = players_list1.size(), size2 = players_list2.size();
		if (size1 > size2)
		{
			team = 2;
		}
		else if (size1 < size2)
		{
			team = 1;
		}
		else
		{
			team = Rnd.get(1, 2);
		}
		if (team == 1)
		{
			players_list1.add(player.getStoredId());
			live_list1.add(player.getStoredId());
			show(new CustomMessage("scripts.events.TvT.Registered", player), player);
		}
		else if (team == 2)
		{
			players_list2.add(player.getStoredId());
			live_list2.add(player.getStoredId());
			show(new CustomMessage("scripts.events.TvT.Registered", player), player);
		}
		else
		{
			_log.info("WTF??? Command id 0 in TvT...");
		}
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
		if (first && (players_list1.contains(player.getStoredId()) || players_list2.contains(player.getStoredId())))
		{
			show(new CustomMessage("scripts.events.TvT.Cancelled", player), player);
			return false;
		}
		if ((player.getLevel() < _minLevel) || (player.getLevel() > _maxLevel))
		{
			show(new CustomMessage("scripts.events.TvT.CancelledLevel", player), player);
			return false;
		}
		if (player.isMounted())
		{
			show(new CustomMessage("scripts.events.TvT.Cancelled", player), player);
			return false;
		}
		if (player.isInDuel())
		{
			show(new CustomMessage("scripts.events.TvT.CancelledDuel", player), player);
			return false;
		}
		if (player.getTeam() != TeamType.NONE)
		{
			show(new CustomMessage("scripts.events.TvT.CancelledOtherEvent", player), player);
			return false;
		}
		if ((player.getOlympiadGame() != null) || (first && Olympiad.isRegistered(player)))
		{
			show(new CustomMessage("scripts.events.TvT.CancelledOlympiad", player), player);
			return false;
		}
		if (player.isTeleporting())
		{
			show(new CustomMessage("scripts.events.TvT.CancelledTeleport", player), player);
			return false;
		}
		return true;
	}
	
	/**
	 * Method prepare.
	 */
	public static void prepare()
	{
		ReflectionUtils.getDoor(24190002).closeMe();
		ReflectionUtils.getDoor(24190003).closeMe();
		cleanPlayers();
		clearArena();
		executeTask("events.TvT.TvT", "ressurectPlayers", new Object[0], 1000);
		executeTask("events.TvT.TvT", "healPlayers", new Object[0], 2000);
		executeTask("events.TvT.TvT", "paralyzePlayers", new Object[0], 4000);
		executeTask("events.TvT.TvT", "teleportPlayersToColiseum", new Object[0], 5000);
		executeTask("events.TvT.TvT", "go", new Object[0], 60000);
		sayToAll("scripts.events.TvT.AnnounceFinalCountdown", null);
	}
	
	/**
	 * Method go.
	 */
	public static void go()
	{
		_status = 2;
		upParalyzePlayers();
		checkLive();
		clearArena();
		sayToAll("scripts.events.TvT.AnnounceFight", null);
		_endTask = executeTask("events.TvT.TvT", "endBattle", new Object[0], 300000);
	}
	
	/**
	 * Method endBattle.
	 */
	public static void endBattle()
	{
		ReflectionUtils.getDoor(24190002).openMe();
		ReflectionUtils.getDoor(24190003).openMe();
		_status = 0;
		removeAura();
		if (live_list1.isEmpty())
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedBlueWins", null);
			giveItemsToWinner(false, true, 1);
		}
		else if (live_list2.isEmpty())
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedRedWins", null);
			giveItemsToWinner(true, false, 1);
		}
		else if (live_list1.size() < live_list2.size())
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedBlueWins", null);
			giveItemsToWinner(false, true, 1);
		}
		else if (live_list1.size() > live_list2.size())
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedRedWins", null);
			giveItemsToWinner(true, false, 1);
		}
		else if (live_list1.size() == live_list2.size())
		{
			sayToAll("scripts.events.TvT.AnnounceFinishedDraw", null);
			giveItemsToWinner(true, true, 0.5);
		}
		sayToAll("scripts.events.TvT.AnnounceEnd", null);
		executeTask("events.TvT.TvT", "end", new Object[0], 30000);
		_isRegistrationActive = false;
		if (_endTask != null)
		{
			_endTask.cancel(false);
			_endTask = null;
		}
	}
	
	/**
	 * Method end.
	 */
	public static void end()
	{
		executeTask("events.TvT.TvT", "ressurectPlayers", new Object[0], 1000);
		executeTask("events.TvT.TvT", "healPlayers", new Object[0], 2000);
		executeTask("events.TvT.TvT", "teleportPlayers", new Object[0], 3000);
		executeTask("events.TvT.TvT", "autoContinue", new Object[0], 10000);
	}
	
	/**
	 * Method autoContinue.
	 */
	public void autoContinue()
	{
		live_list1.clear();
		live_list2.clear();
		players_list1.clear();
		players_list2.clear();
		if (_autoContinue > 0)
		{
			if (_autoContinue >= 7)
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
	 * @param team1 boolean
	 * @param team2 boolean
	 * @param rate double
	 */
	public static void giveItemsToWinner(boolean team1, boolean team2, double rate)
	{
		if (team1)
		{
			for (Player player : getPlayers(players_list1))
			{
				addItem(player, Config.EVENT_TvTItemID, Math.round((Config.EVENT_TvT_rate ? player.getLevel() : 1) * Config.EVENT_TvTItemCOUNT * rate));
			}
		}
		if (team2)
		{
			for (Player player : getPlayers(players_list2))
			{
				addItem(player, Config.EVENT_TvTItemID, Math.round((Config.EVENT_TvT_rate ? player.getLevel() : 1) * Config.EVENT_TvTItemCOUNT * rate));
			}
		}
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
			player.teleToLocation(Territory.getRandomLoc(team1spawn));
		}
		for (Player player : getPlayers(players_list2))
		{
			unRide(player);
			unSummonPet(player, true);
			player.teleToLocation(Territory.getRandomLoc(team2spawn));
		}
	}
	
	/**
	 * Method teleportPlayers.
	 */
	public static void teleportPlayers()
	{
		for (Player player : getPlayers(players_list1))
		{
			player.teleToLocation(151480, 46712, -3400);
		}
		for (Player player : getPlayers(players_list2))
		{
			player.teleToLocation(147640, 46712, -3400);
		}
	}
	
	/**
	 * Method paralyzePlayers.
	 */
	public static void paralyzePlayers()
	{
		Skill revengeSkill = SkillTable.getInstance().getInfo(Skill.SKILL_RAID_CURSE, 1);
		for (Player player : getPlayers(players_list1))
		{
			player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
			revengeSkill.getEffects(player, player, false, false);
			for (Summon summon : player.getSummonList())
			{
				revengeSkill.getEffects(player, summon, false, false);
			}
		}
		for (Player player : getPlayers(players_list2))
		{
			player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
			revengeSkill.getEffects(player, player, false, false);
			for (Summon summon : player.getSummonList())
			{
				revengeSkill.getEffects(player, summon, false, false);
			}
		}
	}
	
	/**
	 * Method upParalyzePlayers.
	 */
	public static void upParalyzePlayers()
	{
		for (Player player : getPlayers(players_list1))
		{
			player.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
			for (Summon summon : player.getSummonList())
			{
				summon.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
			}
			player.leaveParty();
		}
		for (Player player : getPlayers(players_list2))
		{
			player.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
			for (Summon summon : player.getSummonList())
			{
				summon.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
			}
			player.leaveParty();
		}
	}
	
	/**
	 * Method ressurectPlayers.
	 */
	public static void ressurectPlayers()
	{
		for (Player player : getPlayers(players_list1))
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
		for (Player player : getPlayers(players_list2))
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
		}
		for (Player player : getPlayers(players_list2))
		{
			if (!checkPlayer(player, false))
			{
				removePlayer(player);
			}
		}
	}
	
	/**
	 * Method checkLive.
	 */
	public static void checkLive()
	{
		List<Long> new_live_list1 = new CopyOnWriteArrayList<>();
		List<Long> new_live_list2 = new CopyOnWriteArrayList<>();
		for (Long storeId : live_list1)
		{
			Player player = GameObjectsStorage.getAsPlayer(storeId);
			if (player != null)
			{
				new_live_list1.add(storeId);
			}
		}
		for (Long storeId : live_list2)
		{
			Player player = GameObjectsStorage.getAsPlayer(storeId);
			if (player != null)
			{
				new_live_list2.add(storeId);
			}
		}
		live_list1 = new_live_list1;
		live_list2 = new_live_list2;
		for (Player player : getPlayers(live_list1))
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
		for (Player player : getPlayers(live_list2))
		{
			if (player.isInZone(_zone) && !player.isDead() && !player.isLogoutStarted())
			{
				player.setTeam(TeamType.BLUE);
			}
			else
			{
				loosePlayer(player);
			}
		}
		if ((live_list1.size() < 1) || (live_list2.size() < 1))
		{
			endBattle();
		}
	}
	
	/**
	 * Method removeAura.
	 */
	public static void removeAura()
	{
		for (Player player : getPlayers(live_list1))
		{
			player.setTeam(TeamType.NONE);
		}
		for (Player player : getPlayers(live_list2))
		{
			player.setTeam(TeamType.NONE);
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
				if ((player != null) && !live_list1.contains(player.getStoredId()) && !live_list2.contains(player.getStoredId()))
				{
					player.teleToLocation(147451, 46728, -3410);
				}
			}
		}
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
		if ((_status > 1) && self.isPlayer() && (self.getTeam() != TeamType.NONE) && (live_list1.contains(self.getStoredId()) || live_list2.contains(self.getStoredId())))
		{
			loosePlayer((Player) self);
			checkLive();
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
		if ((_status > 1) && (player != null) && (player.getTeam() != TeamType.NONE) && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId())))
		{
			removePlayer(player);
			checkLive();
		}
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
		if ((_status == 0) && _isRegistrationActive && (player.getTeam() != TeamType.NONE) && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId())))
		{
			removePlayer(player);
			return;
		}
		if ((_status == 1) && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId())))
		{
			removePlayer(player);
			try
			{
				String var = player.getVar("TvT_backCoords");
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
				player.unsetVar("TvT_backCoords");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return;
		}
		if ((_status > 1) && (player != null) && (player.getTeam() != TeamType.NONE) && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId())))
		{
			removePlayer(player);
			checkLive();
		}
	}
	
	/**
	 * @author Mobius
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
			if ((_status > 0) && (player != null) && !live_list1.contains(player.getStoredId()) && !live_list2.contains(player.getStoredId()))
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
			if ((_status > 1) && (player != null) && (player.getTeam() != TeamType.NONE) && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId())))
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
	
	/**
	 * @author Mobius
	 */
	private static class TeleportTask extends RunnableImpl
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
			target.block();
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			target.unblock();
			target.teleToLocation(loc);
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
			live_list1.remove(player.getStoredId());
			live_list2.remove(player.getStoredId());
			player.setTeam(TeamType.NONE);
			show(new CustomMessage("scripts.events.TvT.YouLose", player), player);
		}
	}
	
	/**
	 * Method removePlayer.
	 * @param player Player
	 */
	private static void removePlayer(Player player)
	{
		if (player != null)
		{
			live_list1.remove(player.getStoredId());
			live_list2.remove(player.getStoredId());
			players_list1.remove(player.getStoredId());
			players_list2.remove(player.getStoredId());
			player.setTeam(TeamType.NONE);
		}
	}
	
	/**
	 * Method getPlayers.
	 * @param list List<Long>
	 * @return List<Player>
	 */
	private static List<Player> getPlayers(List<Long> list)
	{
		List<Player> result = new ArrayList<>();
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
}
