package events.TvTArena;

import l2p.commons.dbutils.DbUtils;
import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.Announcements;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.instancemanager.ServerVariables;
import l2p.gameserver.listener.actor.OnDeathListener;
import l2p.gameserver.listener.actor.player.OnPlayerExitListener;
import l2p.gameserver.listener.actor.player.OnTeleportListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.SimpleSpawner;
import l2p.gameserver.model.actor.listener.CharListenerList;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

/**
 * @author Darvin
 * @team Java Team
 * @site http://Java-Team.ru
 */
public class TvTArena2 extends Functions implements ScriptFile, OnDeathListener, OnTeleportListener, OnPlayerExitListener {
    private static final Logger _log = LoggerFactory.getLogger(events.TvTArena.TvTArena2.class);

    private static List<SimpleSpawner> _spawns = new ArrayList<SimpleSpawner>();
    private static int EVENT_MANAGER_ID = 36613;
    private static ScheduledFuture<?> _startTask;
    private static ScheduledFuture<?> _stopTask;

    private void spawnEventManagers() {
        final int EVENT_MANAGERS[][] = {{82376, 148808, -3492, 0}};

        SpawnNPCs(EVENT_MANAGER_ID, EVENT_MANAGERS, _spawns);
    }

    private void unSpawnEventManagers() {
        deSpawnNPCs(_spawns);
    }

    private static class TVTArena2Impl extends TVTArenaTemplate {
        @Override
        protected void onLoad() {
            _managerId = 36613;
            _className = "TVTArena2";
            _status = 0;

            _team1list = new CopyOnWriteArrayList<Long>();
            _team2list = new CopyOnWriteArrayList<Long>();
            _team1live = new CopyOnWriteArrayList<Long>();
            _team2live = new CopyOnWriteArrayList<Long>();

            _zoneListener = new ZoneListener();
            _zone = ReflectionUtils.getZone("[UnderGroundPvPRio]");
            _zone.addListener(_zoneListener);

            _team1points = new ArrayList<Location>();
            _team2points = new ArrayList<Location>();

            _team1points.add(new Location(-86728, -48424, -11529));
            _team1points.add(new Location(-87080, -48520, -11529));
            _team1points.add(new Location(-87186, -48872, -11529));
            _team1points.add(new Location(-86904, -49144, -11529));
            _team1points.add(new Location(-86552, -49048, -11529));
            _team1points.add(new Location(-83944, -48072, -11529));
            _team1points.add(new Location(-83496, -48072, -11529));
            _team1points.add(new Location(-83416, -47784, -11529));
            _team1points.add(new Location(-83640, -47544, -11529));

            _team2points.add(new Location(-85880, -46696, -11529));
            _team2points.add(new Location(-85992, -46328, -11529));
            _team2points.add(new Location(-86344, -46232, -11529));
            _team2points.add(new Location(-86600, -46488, -11529));
            _team2points.add(new Location(-86504, -46856, -11529));
            _team2points.add(new Location(-84488, -49016, -11529));
            _team2points.add(new Location(-84488, -49208, -11529));
            _team2points.add(new Location(-84216, -49496, -11529));
            _team2points.add(new Location(-83736, -49224, -11529));
        }

        @Override
        protected void onReload() {
            if (_status > 0)
                template_stop();
            _zone.removeListener(_zoneListener);
        }

    }

    private static TVTArenaTemplate _instance;

    public static TVTArenaTemplate getInstance() {
        if (_instance == null)
            _instance = new TVTArena2Impl();
        return _instance;
    }

    @Override
    public void onLoad() {
        CharListenerList.addGlobal(this);
        getInstance().onLoad();
        if (Config.EVENT_TVT_ARENA_ENABLED && !isActive()) {
            scheduleEventStart();
            scheduleEventStop();
            _log.info("Loaded Event: TVT Arena 2 [state: activated]");
        } else if (Config.EVENT_TVT_ARENA_ENABLED && isActive()) {
            spawnEventManagers();
            _log.info("Loaded Event: TVT Arena 2 [state: started]");
        } else
            _log.info("Loaded Event: TVT Arena 2 [state: deactivated]");
    }

    public boolean isActive() {
        return IsActive("TVTArena2");
    }

    public void scheduleEventStart() {
        try {
            Calendar currentTime = Calendar.getInstance();
            Calendar nextStartTime = null;
            Calendar testStartTime = null;

            for (String timeOfDay : Config.EVENT_TVT_ARENA_START_TIME) {
                testStartTime = Calendar.getInstance();
                testStartTime.setLenient(true);

                String[] splitTimeOfDay = timeOfDay.split(":");

                testStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTimeOfDay[0]));
                testStartTime.set(Calendar.MINUTE, Integer.parseInt(splitTimeOfDay[1]));

                if (testStartTime.getTimeInMillis() < currentTime.getTimeInMillis())
                    testStartTime.add(Calendar.DAY_OF_MONTH, 1);

                if (nextStartTime == null || testStartTime.getTimeInMillis() < nextStartTime.getTimeInMillis())
                    nextStartTime = testStartTime;

                if (_startTask != null) {
                    _startTask.cancel(false);
                    _startTask = null;
                }
                _startTask = ThreadPoolManager.getInstance().schedule(new StartTask(), nextStartTime.getTimeInMillis() - System.currentTimeMillis());
            }

            currentTime = null;
            nextStartTime = null;
            testStartTime = null;

        } catch (Exception e) {
            _log.warn("TVT Arena 2: Error figuring out a start time. Check EventStartTime in config file.");
        }
    }

    public class StartTask extends RunnableImpl {
        @Override
        public void runImpl() {
            spawnEventManagers();
            Announcements.getInstance().announceToAll("Начался евент TVT Arena 2");
            _log.info("Event TVT Arena 2 started");
        }
    }

    public class StopTask extends RunnableImpl {
        @Override
        public void runImpl() {
            unSpawnEventManagers();
            Announcements.getInstance().announceToAll("Окончен евент TVT Arena 2");
            _log.info("Event TVT Arena 2 stopped");
        }
    }

    public void scheduleEventStop() {
        try {
            Calendar currentTime = Calendar.getInstance();
            Calendar nextStartTime = null;
            Calendar testStartTime = null;

            for (String timeOfDay : Config.EVENT_TVT_ARENA_STOP_TIME) {
                testStartTime = Calendar.getInstance();
                testStartTime.setLenient(true);

                String[] splitTimeOfDay = timeOfDay.split(":");

                testStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTimeOfDay[0]));
                testStartTime.set(Calendar.MINUTE, Integer.parseInt(splitTimeOfDay[1]));

                if (testStartTime.getTimeInMillis() < currentTime.getTimeInMillis())
                    testStartTime.add(Calendar.DAY_OF_MONTH, 1);

                if (nextStartTime == null || testStartTime.getTimeInMillis() < nextStartTime.getTimeInMillis())
                    nextStartTime = testStartTime;

                if (_stopTask != null) {
                    _stopTask.cancel(false);
                    _stopTask = null;
                }
                _stopTask = ThreadPoolManager.getInstance().schedule(new StopTask(), nextStartTime.getTimeInMillis() - System.currentTimeMillis());
            }

            currentTime = null;
            nextStartTime = null;
            testStartTime = null;

        } catch (Exception e) {
            _log.warn("TVT Arena 2: Error figuring out a stop time. Check EventStopTime in config file.");
        }
    }

    @Override
    public void onReload() {
        getInstance().onReload();
        unSpawnEventManagers();
        _instance = null;
    }

    @Override
    public void onShutdown() {
        onReload();
    }

    @Override
    public void onDeath(Creature cha, Creature killer) {
        getInstance().onDeath(cha, killer);
    }

    @Override
    public void onPlayerExit(Player player) {
        getInstance().onPlayerExit(player);
    }

    @Override
    public void onTeleport(Player player, int x, int y, int z, Reflection reflection) {
        getInstance().onTeleport(player);
    }

    public String DialogAppend_36613(Integer val) {
        Player player = getSelf();
        if (val == 0)
            return HtmCache.getInstance().getNotNull("scripts/events/TVTArena/36613.htm", player);
        return "";
    }

    public void showRules() {
        Player player = getSelf();
        show("scripts/events/TVTArena/rules.htm", player);
    }

    public void create1() {
        getInstance().template_create1(getSelf());
    }

    public void register() {
        getInstance().template_register(getSelf());
    }

    public void check1() {
        getInstance().template_check1(getSelf(), getNpc());
    }

    public void register_check_blue() {
        getInstance().template_register_check_blue(getSelf());
    }

    public void register_check_red() {
        getInstance().template_register_check_red(getSelf());
    }

    public void stop() {
        getInstance().template_stop();
    }

    public void announce() {
        getInstance().template_announce();
    }

    public void prepare() {
        getInstance().template_prepare();
    }

    public void start() {
        getInstance().template_start();
    }

    public void timeOut() {
        getInstance().template_timeOut();
    }

    public void showStat() {
        Player player = getSelf();
        String html = HtmCache.getInstance().getNotNull("scripts/events/TVTArena/stat.htm", player);
        String playersTopList = "";

        playersTopList += "<table width=285>";
        playersTopList += "<tr>";
        playersTopList += "<td valign=\"top\" width=10></td>";
        playersTopList += "<td valign=\"top\" width=165><center>Игрок</center></td>";
        playersTopList += "<td valign=\"top\" width=50><center>Уровень</center></td>";
        playersTopList += "<td valign=\"top\" width=50><center>Рейтинг</center></td>";
        playersTopList += "<td valign=\"top\" width=10></td>";
        playersTopList += "</tr>";

        Connection conSelect = null;
        PreparedStatement statementSelect = null;
        ResultSet rsetSelect = null;

        try {
            conSelect = DatabaseFactory.getInstance().getConnection();
            statementSelect = conSelect.prepareStatement("SELECT char_name, char_level, char_points FROM event_tvt_arena WHERE arena_id=? ORDER BY char_points DESC LIMIT 0,9");
            statementSelect.setInt(1, 2);
            rsetSelect = statementSelect.executeQuery();
            while (rsetSelect.next()) {
                playersTopList += "<tr>";
                playersTopList += "<td valign=\"top\" width=10></td>";
                playersTopList += "<td valign=\"top\" width=165>" + rsetSelect.getString("char_name") + "</td>";
                playersTopList += "<td valign=\"top\" width=50><center>" + rsetSelect.getInt("char_level") + "</center></td>";
                playersTopList += "<td valign=\"top\" width=50><center>" + rsetSelect.getInt("char_points") + "</center></td>";
                playersTopList += "<td valign=\"top\" width=10></td>";
                playersTopList += "</tr>";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conSelect, statementSelect, rsetSelect);
        }

        playersTopList += "</table>";
        html = html.replace("%top_list%", playersTopList);
        show(html, player);
    }

    public void startEvent() {
        Player player = getSelf();
        if (!player.getPlayerAccess().IsEventGm)
            return;

        if (SetActive("TVTArena2", true)) {
            spawnEventManagers();
            _log.info("Event: TVT Arena 2 started");
            Announcements.getInstance().announceToAll("Начался евент TVT Arena 2");
        } else
            player.sendMessage("Event: TVT Arena 2 already started");

        show("admin/events/custom/custom.htm", player);
    }

    public void stopEvent() {
        Player player = getSelf();
        if (!player.getPlayerAccess().IsEventGm)
            return;

        if (SetActive("TVTArena2", false)) {
            ServerVariables.unset("TVTArena2");
            unSpawnEventManagers();
            stop();
            _log.info("Event: TVT Arena 2 stopped");
            Announcements.getInstance().announceToAll("Окончен евент TVT Arena 2");
        } else
            player.sendMessage("Event: TVT Arena 2 not started");

        show("admin/events/custom/custom.htm", player);
    }
}