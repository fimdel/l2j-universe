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
package lineage2.gameserver.model.entity.olympiad;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.configuration.ExProperties;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.dao.OlympiadNobleDAO;
import lineage2.gameserver.instancemanager.OlympiadHistoryManager;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.MultiValueIntegerMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Olympiad
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Olympiad.class);
	/**
	 * Field _nobles.
	 */
	public static Map<Integer, StatsSet> _nobles;
	/**
	 * Field _noblesRank.
	 */
	public static Map<Integer, Integer> _noblesRank;
	/**
	 * Field _heroesToBe.
	 */
	public static List<StatsSet> _heroesToBe;
	/**
	 * Field _nonClassBasedRegisters.
	 */
	public static List<Integer> _nonClassBasedRegisters = new CopyOnWriteArrayList<>();
	/**
	 * Field _classBasedRegisters.
	 */
	public static MultiValueIntegerMap _classBasedRegisters = new MultiValueIntegerMap();
	/**
	 * Field OLYMPIAD_HTML_PATH. (value is ""olympiad/"")
	 */
	public static final String OLYMPIAD_HTML_PATH = "olympiad/";
	/**
	 * Field CHAR_ID. (value is ""char_id"")
	 */
	public static final String CHAR_ID = "char_id";
	/**
	 * Field CLASS_ID. (value is ""class_id"")
	 */
	public static final String CLASS_ID = "class_id";
	/**
	 * Field CHAR_NAME. (value is ""char_name"")
	 */
	public static final String CHAR_NAME = "char_name";
	/**
	 * Field POINTS. (value is ""olympiad_points"")
	 */
	public static final String POINTS = "olympiad_points";
	/**
	 * Field POINTS_PAST. (value is ""olympiad_points_past"")
	 */
	public static final String POINTS_PAST = "olympiad_points_past";
	/**
	 * Field POINTS_PAST_STATIC. (value is ""olympiad_points_past_static"")
	 */
	public static final String POINTS_PAST_STATIC = "olympiad_points_past_static";
	/**
	 * Field COMP_DONE. (value is ""competitions_done"")
	 */
	public static final String COMP_DONE = "competitions_done";
	/**
	 * Field COMP_WIN. (value is ""competitions_win"")
	 */
	public static final String COMP_WIN = "competitions_win";
	/**
	 * Field COMP_LOOSE. (value is ""competitions_loose"")
	 */
	public static final String COMP_LOOSE = "competitions_loose";
	/**
	 * Field GAME_CLASSES_COUNT. (value is ""game_classes_count"")
	 */
	public static final String GAME_CLASSES_COUNT = "game_classes_count";
	/**
	 * Field GAME_NOCLASSES_COUNT. (value is ""game_noclasses_count"")
	 */
	public static final String GAME_NOCLASSES_COUNT = "game_noclasses_count";
	/**
	 * Field _olympiadEnd.
	 */
	public static long _olympiadEnd;
	/**
	 * Field _validationEnd.
	 */
	public static long _validationEnd;
	/**
	 * Field _period.
	 */
	public static int _period;
	/**
	 * Field _nextWeeklyChange.
	 */
	public static long _nextWeeklyChange;
	/**
	 * Field _currentCycle.
	 */
	public static int _currentCycle;
	/**
	 * Field _compEnd.
	 */
	private static long _compEnd;
	/**
	 * Field _compStart.
	 */
	private static Calendar _compStart;
	/**
	 * Field _inCompPeriod.
	 */
	public static boolean _inCompPeriod;
	/**
	 * Field _isOlympiadEnd.
	 */
	public static boolean _isOlympiadEnd;
	/**
	 * Field _scheduledOlympiadEnd.
	 */
	private static ScheduledFuture<?> _scheduledOlympiadEnd;
	/**
	 * Field _scheduledManagerTask.
	 */
	public static ScheduledFuture<?> _scheduledManagerTask;
	/**
	 * Field _scheduledWeeklyTask.
	 */
	public static ScheduledFuture<?> _scheduledWeeklyTask;
	/**
	 * Field _scheduledValdationTask.
	 */
	public static ScheduledFuture<?> _scheduledValdationTask;
	/**
	 * Field STADIUMS.
	 */
	public static final Stadia[] STADIUMS = new Stadia[Config.OLYMPIAD_STADIAS_COUNT];
	/**
	 * Field _manager.
	 */
	public static OlympiadManager _manager;
	/**
	 * Field _npcs.
	 */
	private static List<NpcInstance> _npcs = new ArrayList<>();
	
	/**
	 * Method load.
	 */
	public static void load()
	{
		_nobles = new ConcurrentHashMap<>();
		_currentCycle = ServerVariables.getInt("Olympiad_CurrentCycle", -1);
		_period = ServerVariables.getInt("Olympiad_Period", -1);
		_olympiadEnd = ServerVariables.getLong("Olympiad_End", -1);
		_validationEnd = ServerVariables.getLong("Olympiad_ValdationEnd", -1);
		_nextWeeklyChange = ServerVariables.getLong("Olympiad_NextWeeklyChange", -1);
		ExProperties olympiadProperties = Config.load(Config.OLYMPIAD_DATA_FILE);
		if (_currentCycle == -1)
		{
			_currentCycle = olympiadProperties.getProperty("CurrentCycle", 1);
		}
		if (_period == -1)
		{
			_period = olympiadProperties.getProperty("Period", 0);
		}
		if (_olympiadEnd == -1)
		{
			_olympiadEnd = olympiadProperties.getProperty("OlympiadEnd", 0L);
		}
		if (_validationEnd == -1)
		{
			_validationEnd = olympiadProperties.getProperty("ValdationEnd", 0L);
		}
		if (_nextWeeklyChange == -1)
		{
			_nextWeeklyChange = olympiadProperties.getProperty("NextWeeklyChange", 0L);
		}
		initStadiums();
		OlympiadHistoryManager.getInstance();
		OlympiadNobleDAO.getInstance().select();
		OlympiadDatabase.loadNoblesRank();
		switch (_period)
		{
			case 0:
				if ((_olympiadEnd == 0) || (_olympiadEnd < Calendar.getInstance().getTimeInMillis()))
				{
					OlympiadDatabase.setNewOlympiadEnd();
				}
				else
				{
					_isOlympiadEnd = false;
				}
				break;
			case 1:
				_isOlympiadEnd = true;
				_scheduledValdationTask = ThreadPoolManager.getInstance().schedule(new ValidationTask(), getMillisToValidationEnd());
				break;
			default:
				_log.warn("Olympiad System: Omg something went wrong in loading!! Period = " + _period);
				return;
		}
		_log.info("Olympiad System: Loading Olympiad System....");
		if (_period == 0)
		{
			_log.info("Olympiad System: Currently in Olympiad Period");
		}
		else
		{
			_log.info("Olympiad System: Currently in Validation Period");
		}
		_log.info("Olympiad System: Period Ends....");
		long milliToEnd;
		if (_period == 0)
		{
			milliToEnd = getMillisToOlympiadEnd();
		}
		else
		{
			milliToEnd = getMillisToValidationEnd();
		}
		double numSecs = (milliToEnd / 1000) % 60;
		double countDown = ((milliToEnd / 1000) - numSecs) / 60;
		int numMins = (int) Math.floor(countDown % 60);
		countDown = (countDown - numMins) / 60;
		int numHours = (int) Math.floor(countDown % 24);
		int numDays = (int) Math.floor((countDown - numHours) / 24);
		_log.info("Olympiad System: In " + numDays + " days, " + numHours + " hours and " + numMins + " mins.");
		if (_period == 0)
		{
			_log.info("Olympiad System: Next Weekly Change is in....");
			milliToEnd = getMillisToWeekChange();
			double numSecs2 = (milliToEnd / 1000) % 60;
			double countDown2 = ((milliToEnd / 1000) - numSecs2) / 60;
			int numMins2 = (int) Math.floor(countDown2 % 60);
			countDown2 = (countDown2 - numMins2) / 60;
			int numHours2 = (int) Math.floor(countDown2 % 24);
			int numDays2 = (int) Math.floor((countDown2 - numHours2) / 24);
			_log.info("Olympiad System: In " + numDays2 + " days, " + numHours2 + " hours and " + numMins2 + " mins.");
		}
		_log.info("Olympiad System: Loaded " + _nobles.size() + " Noblesses");
		if (_period == 0)
		{
			init();
		}
	}
	
	/**
	 * Method initStadiums.
	 */
	private static void initStadiums()
	{
		for (int i = 0; i < STADIUMS.length; i++)
		{
			if (STADIUMS[i] == null)
			{
				STADIUMS[i] = new Stadia();
			}
		}
	}
	
	/**
	 * Method init.
	 */
	public static void init()
	{
		if (_period == 1)
		{
			return;
		}
		int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		if ((nowDay > Calendar.SUNDAY) && (nowDay < Calendar.FRIDAY) && !Config.ALT_OLY_DAYS)
		{
			_log.info("Olympiad System: NotStarted. Day of week: " + nowDay);
			return;
		}
		_log.info("Olympiad System: Started. Day of week: " + nowDay);
		_compStart = Calendar.getInstance();
		_compStart.set(Calendar.HOUR_OF_DAY, Config.ALT_OLY_START_TIME);
		_compStart.set(Calendar.MINUTE, Config.ALT_OLY_MIN);
		_compEnd = _compStart.getTimeInMillis() + Config.ALT_OLY_CPERIOD;
		if (_scheduledOlympiadEnd != null)
		{
			_scheduledOlympiadEnd.cancel(false);
		}
		_scheduledOlympiadEnd = ThreadPoolManager.getInstance().schedule(new OlympiadEndTask(), getMillisToOlympiadEnd());
		updateCompStatus();
		if (_scheduledWeeklyTask != null)
		{
			_scheduledWeeklyTask.cancel(false);
		}
		_scheduledWeeklyTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new WeeklyTask(), getMillisToWeekChange(), Config.ALT_OLY_WPERIOD);
	}
	
	/**
	 * Method registerNoble.
	 * @param noble Player
	 * @param type CompType
	 * @return boolean
	 */
	public static synchronized boolean registerNoble(Player noble, CompType type)
	{
		if (!_inCompPeriod || _isOlympiadEnd)
		{
			noble.sendPacket(SystemMsg.THE_GRAND_OLYMPIAD_GAMES_ARE_NOT_CURRENTLY_IN_PROGRESS);
			return false;
		}
		if (getMillisToOlympiadEnd() <= (600 * 1000))
		{
			noble.sendPacket(SystemMsg.THE_GRAND_OLYMPIAD_GAMES_ARE_NOT_CURRENTLY_IN_PROGRESS);
			return false;
		}
		if (getMillisToCompEnd() <= (600 * 1000))
		{
			noble.sendPacket(SystemMsg.THE_GRAND_OLYMPIAD_GAMES_ARE_NOT_CURRENTLY_IN_PROGRESS);
			return false;
		}
		if (noble.isCursedWeaponEquipped())
		{
			noble.sendPacket(SystemMsg.YOU_CANNOT_REGISTER_WHILE_IN_POSSESSION_OF_A_CURSED_WEAPON);
			return false;
		}
		StatsSet nobleInfo = _nobles.get(noble.getObjectId());
		if (!validPlayer(noble, noble, type))
		{
			return false;
		}
		if (getNoblePoints(noble.getObjectId()) < 3)
		{
			noble.sendMessage(new CustomMessage("lineage2.gameserver.model.entity.Olympiad.LessPoints", noble));
			return false;
		}
		if (noble.getOlympiadGame() != null)
		{
			return false;
		}
		int classId = nobleInfo.getInteger(CLASS_ID);
		switch (type)
		{
			case CLASSED:
			{
				_classBasedRegisters.put(classId, noble.getObjectId());
				noble.sendPacket(SystemMsg.YOU_HAVE_BEEN_REGISTERED_FOR_THE_GRAND_OLYMPIAD_WAITING_LIST_FOR_A_CLASS_SPECIFIC_MATCH);
				break;
			}
			case NON_CLASSED:
			{
				_nonClassBasedRegisters.add(noble.getObjectId());
				noble.sendPacket(SystemMsg.YOU_ARE_CURRENTLY_REGISTERED_FOR_A_1V1_CLASS_IRRELEVANT_MATCH);
				break;
			}
			default:
				break;
		}
		return true;
	}
	
	/**
	 * Method validPlayer.
	 * @param sendPlayer Player
	 * @param validPlayer Player
	 * @param type CompType
	 * @return boolean
	 */
	private static boolean validPlayer(Player sendPlayer, Player validPlayer, CompType type)
	{
		if (!validPlayer.isNoble())
		{
			sendPlayer.sendPacket(new SystemMessage2(SystemMsg.C1_DOES_NOT_MEET_THE_PARTICIPATION_REQUIREMENTS_ONLY_NOBLESSE_CHARACTERS_CAN_PARTICIPATE_IN_THE_OLYMPIAD).addName(validPlayer));
			return false;
		}
		if (validPlayer.getBaseClassId() != validPlayer.getClassId().getId())
		{
			sendPlayer.sendPacket(new SystemMessage2(SystemMsg.C1_DOES_NOT_MEET_THE_PARTICIPATION_REQUIREMENTS_SUBCLASS_CHARACTER_CANNOT_PARTICIPATE_IN_THE_OLYMPIAD).addName(validPlayer));
			return false;
		}
		if (!validPlayer.isAwaking())
		{
			sendPlayer.sendMessage("You don't meet the requirement to play in the Olympiad.");
			return false;
		}
		int[] ar = getWeekGameCounts(validPlayer.getObjectId());
		switch (type)
		{
			case CLASSED:
				if (_classBasedRegisters.containsValue(validPlayer.getObjectId()))
				{
					sendPlayer.sendPacket(new SystemMessage2(SystemMsg.C1_IS_ALREADY_REGISTERED_ON_THE_CLASS_MATCH_WAITING_LIST).addName(validPlayer));
					return false;
				}
				if (ar[1] == 0)
				{
					validPlayer.sendPacket(SystemMsg.THE_TOTAL_NUMBER_OF_MATCHES_THAT_CAN_BE_ENTERED_IN_1_WEEK_IS_60_CLASS_IRRELEVANT_INDIVIDUAL_MATCHES_30_SPECIFIC_MATCHES_AND_10_TEAM_MATCHES);
					return false;
				}
				break;
			case NON_CLASSED:
				if (_nonClassBasedRegisters.contains(validPlayer.getObjectId()))
				{
					sendPlayer.sendPacket(new SystemMessage2(SystemMsg.C1_IS_ALREADY_REGISTERED_ON_THE_WAITING_LIST_FOR_THE_CLASS_IRRELEVANT_INDIVIDUAL_MATCH).addName(validPlayer));
					return false;
				}
				if (ar[2] == 0)
				{
					validPlayer.sendPacket(SystemMsg.THE_TOTAL_NUMBER_OF_MATCHES_THAT_CAN_BE_ENTERED_IN_1_WEEK_IS_60_CLASS_IRRELEVANT_INDIVIDUAL_MATCHES_30_SPECIFIC_MATCHES_AND_10_TEAM_MATCHES);
					return false;
				}
				break;
			default:
				break;
		}
		if (ar[0] == 0)
		{
			validPlayer.sendPacket(SystemMsg.THE_MAXIMUM_MATCHES_YOU_CAN_PARTICIPATE_IN_1_WEEK_IS_70);
			return false;
		}
		if (isRegisteredInComp(validPlayer))
		{
			sendPlayer.sendPacket(new SystemMessage2(SystemMsg.C1_IS_ALREADY_REGISTERED_ON_THE_MATCH_WAITING_LIST).addName(validPlayer));
			return false;
		}
		return true;
	}
	
	/**
	 * Method logoutPlayer.
	 * @param player Player
	 */
	public static synchronized void logoutPlayer(Player player)
	{
		_classBasedRegisters.removeValue(player.getObjectId());
		_nonClassBasedRegisters.remove(new Integer(player.getObjectId()));
		OlympiadGame game = player.getOlympiadGame();
		if (game != null)
		{
			try
			{
				if (!game.logoutPlayer(player) && !game.validated)
				{
					game.endGame(20000, true);
				}
			}
			catch (Exception e)
			{
				_log.error("", e);
			}
		}
	}
	
	/**
	 * Method unRegisterNoble.
	 * @param noble Player
	 * @return boolean
	 */
	public static synchronized boolean unRegisterNoble(Player noble)
	{
		if (!_inCompPeriod || _isOlympiadEnd)
		{
			noble.sendPacket(SystemMsg.THE_GRAND_OLYMPIAD_GAMES_ARE_NOT_CURRENTLY_IN_PROGRESS);
			return false;
		}
		if (!noble.isNoble())
		{
			noble.sendPacket(SystemMsg.THE_GRAND_OLYMPIAD_GAMES_ARE_NOT_CURRENTLY_IN_PROGRESS);
			return false;
		}
		if (!isRegistered(noble))
		{
			noble.sendPacket(SystemMsg.YOU_ARE_NOT_CURRENTLY_REGISTERED_FOR_THE_GRAND_OLYMPIAD);
			return false;
		}
		OlympiadGame game = noble.getOlympiadGame();
		if (game != null)
		{
			if (game.getStatus() == BattleStatus.Begin_Countdown)
			{
				noble.sendMessage("Now you can't cancel participation in the Grand Olympiad.");
				return false;
			}
			try
			{
				if (!game.logoutPlayer(noble) && !game.validated)
				{
					game.endGame(20000, true);
				}
			}
			catch (Exception e)
			{
				_log.error("", e);
			}
		}
		_classBasedRegisters.removeValue(noble.getObjectId());
		_nonClassBasedRegisters.remove(new Integer(noble.getObjectId()));
		noble.sendPacket(SystemMsg.YOU_HAVE_BEEN_REMOVED_FROM_THE_GRAND_OLYMPIAD_WAITING_LIST);
		return true;
	}
	
	/**
	 * Method updateCompStatus.
	 */
	private static synchronized void updateCompStatus()
	{
		long milliToStart = getMillisToCompBegin();
		double numSecs = (milliToStart / 1000) % 60;
		double countDown = ((milliToStart / 1000) - numSecs) / 60;
		int numMins = (int) Math.floor(countDown % 60);
		countDown = (countDown - numMins) / 60;
		int numHours = (int) Math.floor(countDown % 24);
		int numDays = (int) Math.floor((countDown - numHours) / 24);
		_log.info("Olympiad System: Competition Period Starts in " + numDays + " days, " + numHours + " hours and " + numMins + " mins.");
		_log.info("Olympiad System: Event starts/started: " + _compStart.getTime());
		ThreadPoolManager.getInstance().schedule(new CompStartTask(), getMillisToCompBegin());
	}
	
	/**
	 * Method getMillisToOlympiadEnd.
	 * @return long
	 */
	private static long getMillisToOlympiadEnd()
	{
		return _olympiadEnd - System.currentTimeMillis();
	}
	
	/**
	 * Method getMillisToValidationEnd.
	 * @return long
	 */
	static long getMillisToValidationEnd()
	{
		if (_validationEnd > System.currentTimeMillis())
		{
			return _validationEnd - System.currentTimeMillis();
		}
		return 10L;
	}
	
	/**
	 * Method isOlympiadEnd.
	 * @return boolean
	 */
	public static boolean isOlympiadEnd()
	{
		return _isOlympiadEnd;
	}
	
	/**
	 * Method inCompPeriod.
	 * @return boolean
	 */
	public static boolean inCompPeriod()
	{
		return _inCompPeriod;
	}
	
	/**
	 * Method getMillisToCompBegin.
	 * @return long
	 */
	private static long getMillisToCompBegin()
	{
		if ((_compStart.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) && (_compEnd > Calendar.getInstance().getTimeInMillis()))
		{
			return 10L;
		}
		if (_compStart.getTimeInMillis() > Calendar.getInstance().getTimeInMillis())
		{
			return _compStart.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
		}
		return setNewCompBegin();
	}
	
	/**
	 * Method setNewCompBegin.
	 * @return long
	 */
	private static long setNewCompBegin()
	{
		_compStart = Calendar.getInstance();
		_compStart.set(Calendar.HOUR_OF_DAY, Config.ALT_OLY_START_TIME);
		_compStart.set(Calendar.MINUTE, Config.ALT_OLY_MIN);
		_compStart.add(Calendar.HOUR_OF_DAY, 24);
		_compEnd = _compStart.getTimeInMillis() + Config.ALT_OLY_CPERIOD;
		_log.info("Olympiad System: New Schedule @ " + _compStart.getTime());
		return _compStart.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
	}
	
	/**
	 * Method getMillisToCompEnd.
	 * @return long
	 */
	public static long getMillisToCompEnd()
	{
		return _compEnd - Calendar.getInstance().getTimeInMillis();
	}
	
	/**
	 * Method getMillisToWeekChange.
	 * @return long
	 */
	private static long getMillisToWeekChange()
	{
		if (_nextWeeklyChange > Calendar.getInstance().getTimeInMillis())
		{
			return _nextWeeklyChange - Calendar.getInstance().getTimeInMillis();
		}
		return 10L;
	}
	
	/**
	 * Method doWeekTasks.
	 */
	public static synchronized void doWeekTasks()
	{
		if (_period == 1)
		{
			return;
		}
		for (Map.Entry<Integer, StatsSet> entry : _nobles.entrySet())
		{
			StatsSet set = entry.getValue();
			Player player = GameObjectsStorage.getPlayer(entry.getKey());
			if (_period != 1)
			{
				set.set(POINTS, set.getInteger(POINTS) + Config.OLYMPIAD_POINTS_WEEKLY);
			}
			set.set(GAME_CLASSES_COUNT, 0);
			set.set(GAME_NOCLASSES_COUNT, 0);
			if (player != null)
			{
				player.sendPacket(new SystemMessage2(SystemMsg.C1_HAS_EARNED_S2_POINTS_IN_THE_GRAND_OLYMPIAD_GAMES).addName(player).addInteger(Config.OLYMPIAD_POINTS_WEEKLY));
			}
		}
	}
	
	/**
	 * Method getCurrentCycle.
	 * @return int
	 */
	public static int getCurrentCycle()
	{
		return _currentCycle;
	}
	
	/**
	 * Method addSpectator.
	 * @param id int
	 * @param spectator Player
	 */
	public static synchronized void addSpectator(int id, Player spectator)
	{
		if ((spectator.getOlympiadGame() != null) || isRegistered(spectator) || Olympiad.isRegisteredInComp(spectator))
		{
			spectator.sendPacket(SystemMsg.YOU_MAY_NOT_OBSERVE_A_GRAND_OLYMPIAD_GAMES_MATCH_WHILE_YOU_ARE_ON_THE_WAITING_LIST);
			return;
		}
		if ((_manager == null) || (_manager.getOlympiadInstance(id) == null) || (_manager.getOlympiadInstance(id).getStatus() == BattleStatus.Begining) || (_manager.getOlympiadInstance(id).getStatus() == BattleStatus.Begin_Countdown))
		{
			spectator.sendPacket(SystemMsg.THE_GRAND_OLYMPIAD_GAMES_ARE_NOT_CURRENTLY_IN_PROGRESS);
			return;
		}
		for (Summon summon : spectator.getSummonList())
		{
			summon.unSummon();
		}
		OlympiadGame game = getOlympiadGame(id);
		List<Location> spawns = game.getReflection().getInstancedZone().getTeleportCoords();
		if (spawns.size() < 3)
		{
			Location c1 = spawns.get(0);
			Location c2 = spawns.get(1);
			spectator.enterOlympiadObserverMode(new Location((c1.x + c2.x) / 2, (c1.y + c2.y) / 2, (c1.z + c2.z) / 2), game, game.getReflection());
		}
		else
		{
			spectator.enterOlympiadObserverMode(spawns.get(2), game, game.getReflection());
		}
	}
	
	/**
	 * Method removeSpectator.
	 * @param id int
	 * @param spectator Player
	 */
	public static synchronized void removeSpectator(int id, Player spectator)
	{
		if ((_manager == null) || (_manager.getOlympiadInstance(id) == null))
		{
			return;
		}
		_manager.getOlympiadInstance(id).removeSpectator(spectator);
	}
	
	/**
	 * Method getSpectators.
	 * @param id int
	 * @return List<Player>
	 */
	public static List<Player> getSpectators(int id)
	{
		if ((_manager == null) || (_manager.getOlympiadInstance(id) == null))
		{
			return null;
		}
		return _manager.getOlympiadInstance(id).getSpectators();
	}
	
	/**
	 * Method getOlympiadGame.
	 * @param gameId int
	 * @return OlympiadGame
	 */
	public static OlympiadGame getOlympiadGame(int gameId)
	{
		if ((_manager == null) || (gameId < 0))
		{
			return null;
		}
		return _manager.getOlympiadGames().get(gameId);
	}
	
	/**
	 * Method getWaitingList.
	 * @return int[]
	 */
	public static synchronized int[] getWaitingList()
	{
		if (!inCompPeriod())
		{
			return null;
		}
		int[] array = new int[2];
		array[0] = _classBasedRegisters.totalSize();
		array[1] = _nonClassBasedRegisters.size();
		return array;
	}
	
	/**
	 * Method getNoblessePasses.
	 * @param player Player
	 * @return int
	 */
	public static synchronized int getNoblessePasses(Player player)
	{
		int objId = player.getObjectId();
		StatsSet noble = _nobles.get(objId);
		if (noble == null)
		{
			return 0;
		}
		int points = noble.getInteger(POINTS_PAST);
		if (points == 0)
		{
			return 0;
		}
		int rank = _noblesRank.get(objId);
		switch (rank)
		{
			case 1:
				points = Config.ALT_OLY_RANK1_POINTS;
				break;
			case 2:
				points = Config.ALT_OLY_RANK2_POINTS;
				break;
			case 3:
				points = Config.ALT_OLY_RANK3_POINTS;
				break;
			case 4:
				points = Config.ALT_OLY_RANK4_POINTS;
				break;
			default:
				points = Config.ALT_OLY_RANK5_POINTS;
		}
		if (player.isHero() || Hero.getInstance().isInactiveHero(player.getObjectId()))
		{
			points += Config.ALT_OLY_HERO_POINTS;
		}
		noble.set(POINTS_PAST, 0);
		OlympiadDatabase.saveNobleData(objId);
		return points * Config.ALT_OLY_GP_PER_POINT;
	}
	
	/**
	 * Method isRegistered.
	 * @param noble Player
	 * @return boolean
	 */
	public static synchronized boolean isRegistered(Player noble)
	{
		if (_classBasedRegisters.containsValue(noble.getObjectId()))
		{
			return true;
		}
		if (_nonClassBasedRegisters.contains(noble.getObjectId()))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method isRegisteredInComp.
	 * @param player Player
	 * @return boolean
	 */
	public static synchronized boolean isRegisteredInComp(Player player)
	{
		if (isRegistered(player))
		{
			return true;
		}
		if ((_manager == null) || (_manager.getOlympiadGames() == null))
		{
			return false;
		}
		for (OlympiadGame g : _manager.getOlympiadGames().values())
		{
			if ((g != null) && g.isRegistered(player.getObjectId()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method getNoblePoints.
	 * @param objId int
	 * @return int
	 */
	public static synchronized int getNoblePoints(int objId)
	{
		StatsSet noble = _nobles.get(objId);
		if (noble == null)
		{
			return 0;
		}
		return noble.getInteger(POINTS);
	}
	
	/**
	 * Method getNoblePointsPast.
	 * @param objId int
	 * @return int
	 */
	public static synchronized int getNoblePointsPast(int objId)
	{
		StatsSet noble = _nobles.get(objId);
		if (noble == null)
		{
			return 0;
		}
		return noble.getInteger(POINTS_PAST);
	}
	
	/**
	 * Method getCompetitionDone.
	 * @param objId int
	 * @return int
	 */
	public static synchronized int getCompetitionDone(int objId)
	{
		StatsSet noble = _nobles.get(objId);
		if (noble == null)
		{
			return 0;
		}
		return noble.getInteger(COMP_DONE);
	}
	
	/**
	 * Method getCompetitionWin.
	 * @param objId int
	 * @return int
	 */
	public static synchronized int getCompetitionWin(int objId)
	{
		StatsSet noble = _nobles.get(objId);
		if (noble == null)
		{
			return 0;
		}
		return noble.getInteger(COMP_WIN);
	}
	
	/**
	 * Method getCompetitionLoose.
	 * @param objId int
	 * @return int
	 */
	public static synchronized int getCompetitionLoose(int objId)
	{
		StatsSet noble = _nobles.get(objId);
		if (noble == null)
		{
			return 0;
		}
		return noble.getInteger(COMP_LOOSE);
	}
	
	/**
	 * Method getWeekGameCounts.
	 * @param objId int
	 * @return int[]
	 */
	public static synchronized int[] getWeekGameCounts(int objId)
	{
		int[] ar = new int[3];
		StatsSet noble = _nobles.get(objId);
		if (noble == null)
		{
			return ar;
		}
		ar[0] = Config.GAME_MAX_LIMIT - noble.getInteger(GAME_CLASSES_COUNT) - noble.getInteger(GAME_NOCLASSES_COUNT);
		ar[1] = Config.GAME_CLASSES_COUNT_LIMIT - noble.getInteger(GAME_CLASSES_COUNT);
		ar[2] = Config.GAME_NOCLASSES_COUNT_LIMIT - noble.getInteger(GAME_NOCLASSES_COUNT);
		return ar;
	}
	
	/**
	 * Method getStadiums.
	 * @return Stadia[]
	 */
	public static Stadia[] getStadiums()
	{
		return STADIUMS;
	}
	
	/**
	 * Method getNpcs.
	 * @return List<NpcInstance>
	 */
	public static List<NpcInstance> getNpcs()
	{
		return _npcs;
	}
	
	/**
	 * Method addOlympiadNpc.
	 * @param npc NpcInstance
	 */
	public static void addOlympiadNpc(NpcInstance npc)
	{
		_npcs.add(npc);
	}
	
	/**
	 * Method changeNobleName.
	 * @param objId int
	 * @param newName String
	 */
	public static void changeNobleName(int objId, String newName)
	{
		StatsSet noble = _nobles.get(objId);
		if (noble == null)
		{
			return;
		}
		noble.set(CHAR_NAME, newName);
		OlympiadDatabase.saveNobleData(objId);
	}
	
	/**
	 * Method getNobleName.
	 * @param objId int
	 * @return String
	 */
	public static String getNobleName(int objId)
	{
		StatsSet noble = _nobles.get(objId);
		if (noble == null)
		{
			return null;
		}
		return noble.getString(CHAR_NAME);
	}
	
	/**
	 * Method getNobleClass.
	 * @param objId int
	 * @return int
	 */
	public static int getNobleClass(int objId)
	{
		StatsSet noble = _nobles.get(objId);
		if (noble == null)
		{
			return 0;
		}
		return noble.getInteger(CLASS_ID);
	}
	
	/**
	 * Method manualSetNoblePoints.
	 * @param objId int
	 * @param points int
	 */
	public static void manualSetNoblePoints(int objId, int points)
	{
		StatsSet noble = _nobles.get(objId);
		if (noble == null)
		{
			return;
		}
		noble.set(POINTS, points);
		OlympiadDatabase.saveNobleData(objId);
	}
	
	/**
	 * Method isNoble.
	 * @param objId int
	 * @return boolean
	 */
	public static synchronized boolean isNoble(int objId)
	{
		return _nobles.get(objId) != null;
	}
	
	/**
	 * Method addNoble.
	 * @param noble Player
	 */
	public static synchronized void addNoble(Player noble)
	{
		if (!_nobles.containsKey(noble.getObjectId()))
		{
			int classId = noble.getBaseClassId();
			if (classId < 139)
			{
				return;
			}
			StatsSet statDat = new StatsSet();
			statDat.set(CLASS_ID, classId);
			statDat.set(CHAR_NAME, noble.getName());
			statDat.set(POINTS, Config.OLYMPIAD_POINTS_DEFAULT);
			statDat.set(POINTS_PAST, 0);
			statDat.set(POINTS_PAST_STATIC, 0);
			statDat.set(COMP_DONE, 0);
			statDat.set(COMP_WIN, 0);
			statDat.set(COMP_LOOSE, 0);
			statDat.set(GAME_CLASSES_COUNT, 0);
			statDat.set(GAME_NOCLASSES_COUNT, 0);
			_nobles.put(noble.getObjectId(), statDat);
			OlympiadDatabase.saveNobleData(noble.getObjectId());
		}
	}
	
	/**
	 * Method removeNoble.
	 * @param noble Player
	 */
	public static synchronized void removeNoble(Player noble)
	{
		_nobles.remove(noble.getObjectId());
		OlympiadDatabase.saveNobleData();
	}
}
