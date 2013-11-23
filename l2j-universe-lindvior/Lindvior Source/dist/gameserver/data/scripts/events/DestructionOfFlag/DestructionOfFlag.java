/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package events.DestructionOfFlag;

import l2p.commons.collections.LazyArrayList;
import l2p.gameserver.Announcements;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.instancemanager.ServerVariables;
import l2p.gameserver.listener.actor.OnDeathListener;
import l2p.gameserver.listener.actor.player.OnPlayerExitListener;
import l2p.gameserver.listener.actor.player.OnTeleportListener;
import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.*;
import l2p.gameserver.model.actor.listener.CharListenerList;
import l2p.gameserver.model.base.TeamType;
import l2p.gameserver.model.entity.Hero;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.model.entity.residence.Residence;
import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.Revive;
import l2p.gameserver.network.serverpackets.SkillList;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.skills.effects.EffectTemplate;
import l2p.gameserver.stats.Env;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.PositionUtils;
import l2p.gameserver.utils.ReflectionUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 06.04.12
 * Time: 21:29
 */

public class DestructionOfFlag extends Functions implements ScriptFile, OnDeathListener, OnTeleportListener, OnPlayerExitListener {
    private static Logger _log = Logger.getLogger(DestructionOfFlag.class.getName());
    private static final boolean REMOVE_BUFFS = false; //включен ли этот массив или нет
    private static final int[][] BUFFS_TO_REMOVE = { // какие бафы снимать при телепорте на эвент
            {1, 1}, {2}, {3, 1}//{id скила, лвл скила}, {id скила, лвл скила};(но если я не указываю лвл скила {id скила};, то снимаем любой лвл бафа, а если лвл скила указан, то снимает баф(скил) с определенным лвлом) пример:  {1, 1}, {2}, {3, 1}
    };

    private static final int[] REWARD = { //массив по выдачи бонуса {ID предмета, количество; ID предмета, количество поддержка}; возможно несколько бонусов
            57, 2000000000
    };

    private static final String[][] startTime = {

            {"2:35", "2:56"},
            {"5:35", "5:56"},
            {"8:35", "8:56"},
            {"11:35", "11:56"},
            {"14:30", "14:46"},
            {"17:30", "17:46"},
            {"19:45", "19:56"},
            {"20:45", "20:56"},
            {"23:30", "23:46"}


    };

    private static int MIN_PLAYERS = 0; // Минимальное кол-во игроков в каждой команде, для начала эвента.

    /**
     * Это список NPC которые будут спавнится после телепорта.
     * Формат массива: ID Npc, locX, locY, locZ, locH
     */
    @SuppressWarnings("unused")
    private static int[][] npcs = {
            {31143, 10000, 10676, -3455, 0}, // Собственно ID NPC, locX, locY, locZ, locH
            {31143, 10000, 10676, -3455, 0},
            {31143, 10000, 10676, -3455, 0}
    };
    // Спавнить вообще этих НПЦ?
    @SuppressWarnings("unused")
    private static boolean _spawnNpcs = false;

    private static int[] _listAllowSaveBuffs =  // Ниже список бафов, которые сохраняются, если ALLOW_BUFFS = true,
            // если конечно бафнуты на персонажа.
            {1388, 1389, 1068, 1040, 1086, 1085, 1242, 1059, 1240, 1078,
                    1077, 1303, 1204, 1062, 1542, 1397, 1045, 1048, 1087, 1043,
                    1268, 1259, 1243, 1035, 1304, 1036, 1191, 1182, 1189, 1352,
                    1354, 1353, 1393, 1392, 1499, 1501, 1502, 1500, 1519, 1503,
                    1504, 1251, 1252, 1253, 1002, 1284, 1308, 1309, 1391, 1007,
                    1009, 1006, 1461, 1010, 1390, 1310, 1362, 1413, 1535, 275,
                    276, 274, 273, 271, 365, 272, 277, 310, 307, 311, 309, 915,
                    530, 269, 266, 264, 267, 268, 265, 349, 364, 764, 529, 304,
                    270, 306, 305, 308, 363, 914, 4700, 4703, 4699, 4702, 825,
                    828, 827, 829, 826, 830, 1356, 1355, 1357, 1363};
    private static int[][][] _listBuff = // Это собственно массив бафов, которые бафаются на игроков в случае,
            {// если их бафы не сохраняются, т.е. ALLOW_BUFFS = false
                    { // Бафы на Война:
                            {1086, 2}, // Haste: 2 LvL
                            {4342, 2}, // Wind Walk: 2 LvL
                            {1068, 3}, // Might: 3 LvL
                            {1240, 3}, // Guidance: 3 LvL
                            {1077, 3}, // Focus: 3 LvL
                            {1242, 3} // Death Whisper: 3 LvL
                    },
                    { // Бафы на Мага:
                            {4342, 2}, // Wind Walk: 2 LvL
                            {1059, 3}, // Empower: 3 LvL
                            {1085, 3}, // Acumen: 3 LvL
                            {1078, 6}, // Concentration: 6 LvL
                            {1062, 2} // Berserker Spirit: 2 LvL
                    }

            };

    private static boolean ALLOW_RESTRICT_SKILLS = false;
    private static int[][] RESTRICT_SKILLS = {
            {1218, 0}, // ТОЛЬКО НА СЕБЯ
            {1234, 1}, // ЧЛЕНЫ СВОЕЙ КОМАНДЫ
            {1410, 2} // ЗАПРЕТ НА АТАКУ СВОЕГО ФЛАГА
            // остальные на всех...
    };

    public static String[] colors = {"00ff00", "ffffff", "00ffff"}; // Цвета для ников Команд (синий, красный, желтый).

    private static List<Long> players_list = new CopyOnWriteArrayList<Long>();
    private static List<Long> live_list = new CopyOnWriteArrayList<Long>();

    private static boolean ALLOW_RESTRICT_ITEMS = false;   // Включена ли проверка на использование запрещенных предметов?
    private static int[] RESTRICT_ITEMS = {725, 727}; // Сам список запрещенных предметов.
    private static boolean PROTECT_IP_ACTIVE = false;
    private static ScheduledFuture<?> _startTask;
    private static HashMap<Long, LazyArrayList<Effect>> _saveBuffList = new HashMap<Long, LazyArrayList<Effect>>();
    private static List<SimpleSpawner> _spawns = new ArrayList<SimpleSpawner>();
    private static int EVENT_MANAGER_ID = 31143;

    private static void spawnNpcs() {
        final int EVENT_MANAGERS[][] = {{10000, 10676, -3455, 0}, // Собственно ID NPC, locX, locY, locZ, locH
                {10000, 10676, -3455, 0},
                {10000, 10676, -3455, 0}};

        SpawnNPCs(EVENT_MANAGER_ID, EVENT_MANAGERS, _spawns);
    }

    private static void despawnNpcs() {
        deSpawnNPCs(_spawns);
    }

    @Override
    public void onPlayerExit(Player player) {
        if (player.getTeam() == TeamType.NONE)
            return;

        // Вышел или вылетел во время регистрации
        if (_status == 0 && _isRegistrationActive && live_list.contains(player.getStoredId())) {
            removePlayer(player);
            return;
        }

        // Вышел или вылетел во время телепортации
        if (_status == 1 && live_list.contains(player.getStoredId())) {
            removePlayer(player);

            try {
                String var = player.getVar("DestructionOfFlag_backCoords");
                if (var == null || var.equals(""))
                    return;
                String[] coords = var.split(" ");
                if (coords.length != 4)
                    return;
                player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]), Integer.parseInt(coords[3]));
                player.unsetVar("DestructionOfFlag_backCoords");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return;
        }

        // Вышел или вылетел во время эвента
        if (_status > 1 && player.getTeam() != TeamType.NONE && live_list.contains(player.getStoredId())) {
            removePlayer(player);
            checkLive();
        }
    }

    public static void checkLive() {
        List<Long> new_live_list = new CopyOnWriteArrayList<Long>();

        for (Long storeId : live_list) {
            Player player = GameObjectsStorage.getAsPlayer(storeId);
            if (player != null)
                new_live_list.add(storeId);
        }

        live_list = new_live_list;

        for (Player player : getPlayers(live_list))
            if (player.isInZone(_zone) && !player.isDead() && !player.isLogoutStarted())
                player.setTeam(TeamType.RED);
            else
                loosePlayer(player);

        if (live_list.size() <= 1)
            endBattle(0);
    }

    private static void loosePlayer(Player player) {
        if (player != null) {
            live_list.remove(player.getStoredId());
            player.setTeam(TeamType.NONE);
            show(new CustomMessage("scripts.events.LastHero.YouLose", player), player);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onDeath(Creature self, Creature killer) {
        if (_status > 1 && self.isPlayer() && self.getTeam() != TeamType.NONE && live_list.contains(self.getStoredId())) {
            Player player = (Player) self;
            loosePlayer(player);
            checkLive();
            if (killer != null && killer.isPlayer() && killer.getPlayer().expertiseIndex - player.expertiseIndex > 2 && !killer.getPlayer().getIP().equals(player.getIP()))
                addItem((Player) killer, 4657, Math.round(false ? player.getLevel() * 150 : 1 * 150));
        }
    }

    @Override
    public void onTeleport(Player player, int x, int y, int z, Reflection reflection) {
        if (_zone.checkIfInZone(x, y, z, reflection))
            return;

        if (_status > 1 && player.getTeam() != TeamType.NONE && live_list.contains(player.getStoredId())) {
            removePlayer(player);
            checkLive();
        }
    }

    public class StartTask implements Runnable {
        private String endTime;

        public StartTask(String endTime) {
            this.endTime = endTime;
        }

        public void run() {
            if (!_active) {
                _log.info("DestructionOfFlag: is not Active");
                return;
            }

            if (isPvPEventStarted()) {
                _log.info("DestructionOfFlag not started: another event is already running");
                return;

            }
            /*if(TerritorySiege.isInProgress())
            {
                _log.fine("DestructionOfFlag not started: TerritorySiege in progress");
                return;
            }      */

            for (Residence c : ResidenceHolder.getInstance().getResidenceList(Castle.class))
                if (c.getSiegeEvent() != null && c.getSiegeEvent().isInProgress()) {
                    _log.debug("LastHero not started: CastleSiege in progress");
                    return;
                }
            _log.info("DestructionOfFlag: started, end Time: " + endTime);
            start(new String[]{"-1", "-1", endTime});
        }
    }

    private static List<ScheduledFuture<?>> startTasks = new ArrayList<ScheduledFuture<?>>();

    private static LazyArrayList<Long> players_list1 = new LazyArrayList<Long>();
    private static LazyArrayList<Long> players_list2 = new LazyArrayList<Long>();
    private static LazyArrayList<Long> players_list3 = new LazyArrayList<Long>();
    private static LazyArrayList<Long> players_list4 = new LazyArrayList<Long>();

    private static MonsterInstance whiteFlag = null;
    private static MonsterInstance greenFlag = null;
    private static MonsterInstance yellowFlag = null;
    private static MonsterInstance blackFlag = null;

    private static boolean _isRegistrationActive = false;
    public static int _status = 0;
    private static int _time_to_start;
    private static int _category;
    private static int _minLevel;
    private static int _maxLevel;
    private static int _autoContinue = 0;

    /**
     * Настройки эвента, то, что в конфиге - не используется.
     */
    private static boolean ALLOW_BUFFS = true;
    private static boolean ALLOW_CLAN_SKILL = true;
    private static boolean ALLOW_HERO_SKILL = true;
    private static boolean EVENT_DestructionOfFlag_rate = false;
    private static boolean ALLOW_PETS = true; //Разрешить спавн пета?

    private static int TIME_FOR_RES = 5; // Через какое время после смерти персонажа восстанавливают.

    private static Zone _zone = ReflectionUtils.getZone("[colosseum_battle]");
    private static ZoneListener _zoneListener = new ZoneListener();

    private static Location team1loc = new Location(-82952, -44344, -11496, -11396);
    private static Location team2loc = new Location(-82536, -47016, -11504, -11404);
    private static Location team3loc = new Location(-80680, -44296, -11496, -11396);
    private static Location team4loc = new Location(-78680, -41296, -11496, -11204);

    private static HashMap<Long, ScheduledFuture<?>> _resurrectionList = new HashMap<Long, ScheduledFuture<?>>(); //Хранилище задач на Ressuction.

    public static boolean canSpawnPet(Player player) {
        if (players_list1.contains(player.getObjectId()) || players_list2.contains(player.getObjectId()))
            if (!ALLOW_PETS) return false;
        return true;
    }

    @Override
    public void onLoad() {
        CharListenerList.addGlobal(this);
        _zone.addListener(_zoneListener);

        for (String[] s : startTime) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(s[0].split(":")[0]));
            cal.set(Calendar.MINUTE, Integer.valueOf(s[0].split(":")[1]));
            cal.set(Calendar.SECOND, 0);
            while (cal.getTimeInMillis() < System.currentTimeMillis())
                cal.add(Calendar.DAY_OF_YEAR, 1);
            ScheduledFuture<?> startTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new StartTask(s[1]), cal.getTimeInMillis() - System.currentTimeMillis(), 86400000);
            startTasks.add(startTask);
            spawnNpcs();
        }

        _active = ServerVariables.getString("DestructionOfFlag", "off").equalsIgnoreCase("on");

        _log.info("Loaded Event: DestructionOfFlag");
    }

    @Override
    public void onReload() {
        _zone.removeListener(_zoneListener);
        if (_startTask != null) {
            _startTask.cancel(false);
            _startTask = null;
        }
    }

    @Override
    public void onShutdown() {
        onReload();
    }

    private static boolean _active = false;

    private static boolean isActive() {
        return _active;
    }


    public void activateEvent() {
        Player player = getSelf();
        if (!player.getPlayerAccess().IsEventGm)
            return;

        if (!isActive()) {
            for (String[] s : startTime) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(s[0].split(":")[0]));
                cal.set(Calendar.MINUTE, Integer.valueOf(s[0].split(":")[1]));
                cal.set(Calendar.SECOND, 0);
                while (cal.getTimeInMillis() < System.currentTimeMillis())
                    cal.add(Calendar.DAY_OF_YEAR, 1);
                ScheduledFuture<?> startTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new StartTask(s[1]), cal.getTimeInMillis() - System.currentTimeMillis(), 86400000);
                startTasks.add(startTask);
            }
            ServerVariables.set("DestructionOfFlag", "on");
            _log.info("Event 'DestructionOfFlag' activated.");
            Announcements.getInstance().announceByCustomMessage("scripts.events.DestructionOfFlag.AnnounceEventStarted", null);
        } else
            player.sendMessage("Event 'DestructionOfFlag' already active.");

        _active = true;

        show("admin/events.htm", player);
    }

    public void deactivateEvent() {
        Player player = getSelf();
        if (!player.getPlayerAccess().IsEventGm)
            return;

        if (isActive()) {
            startTasks.clear();
            ServerVariables.unset("DestructionOfFlag");
            _log.info("Event 'DestructionOfFlag' deactivated.");
            Announcements.getInstance().announceByCustomMessage("scripts.events.DestructionOfFlag.AnnounceEventStoped", null);
        } else
            player.sendMessage("Event 'DestructionOfFlag' not active.");

        _active = false;

        show("admin/events.htm", player);
    }

    public static boolean isRunned() {
        return _isRegistrationActive || _status > 0;
    }

    public String DialogAppend_31225(Integer val) {
        if (val == 0) {
            Player player = getSelf();
            show("data/scripts/events/DestructionOfFlag/31225.html", player);
        }
        return "";
    }

    public static int getMinLevelForCategory(int category) {
        switch (category) {
            case 1:
                return 85;
        }
        return 0;
    }

    public static int getMaxLevelForCategory(int category) {
        switch (category) {
            case 1:
                return 99;
        }
        return 0;
    }

    public static int getCategory(int level) {
        if (level >= 85 && level <= 99)
            return 1;
        return 0;
    }

    public void start(String[] var) {
        if (var.length != 3) {
            _log.info("Destruction of Flag: Error start, var length: " + var.length);
            return;
        }

        Integer category;
        Integer autoContinue;
        try {
            category = Integer.valueOf(var[0]);
            autoContinue = Integer.valueOf(var[1]);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        _category = category;
        _autoContinue = autoContinue;

        if (_category == -1) {
            _minLevel = 85;
            _maxLevel = 99;
        } else {
            _minLevel = getMinLevelForCategory(_category);
            _maxLevel = getMaxLevelForCategory(_category);
        }

        _status = 0;
        _isRegistrationActive = true;
        _time_to_start = 3;

        players_list1 = new LazyArrayList<Long>();
        players_list2 = new LazyArrayList<Long>();

        if (whiteFlag != null)
            whiteFlag.deleteMe();
        if (greenFlag != null)
            greenFlag.deleteMe();


        try {

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        whiteFlag.decayMe();
        greenFlag.decayMe();
        yellowFlag.decayMe();
        blackFlag.decayMe();

        String[] param = {String.valueOf(_time_to_start), String.valueOf(_minLevel), String.valueOf(_maxLevel)};
        sayToAll("scripts.events.DestructionOfFlag.AnnouncePreStart", param);

        executeTask("events.DestructionOfFlag.DestructionOfFlag", "question", new Object[0], 10000);
        executeTask("events.DestructionOfFlag.DestructionOfFlag", "announce", new Object[]{var[2]}, 60000);
    }

    public static void sayToAll(String address, String[] replacements) {
        Announcements.getInstance().announceByCustomMessage(address, replacements, ChatType.CRITICAL_ANNOUNCE);
    }

    public static void question() {
        for (Player player : GameObjectsStorage.getAllPlayersForIterate())
            if (player != null && player.getLevel() >= _minLevel && player.getLevel() <= _maxLevel && player.getReflection().getId() <= 0 && !player.isInOlympiadMode())
                player.scriptRequest(new CustomMessage("scripts.events.DestructionOfFlag.AskPlayer", player).toString(), "events.DestructionOfFlag.DestructionOfFlag:addPlayer", new Object[0]);
    }

    public static void announce(String s) {
        if (players_list1.isEmpty() || players_list2.isEmpty()) {
            sayToAll("scripts.events.DestructionOfFlag.AnnounceEventCancelled", null);
            _isRegistrationActive = false;
            _status = 0;
            executeTask("events.DestructionOfFlag.DestructionOfFlag", "autoContinue", new Object[0], 10000);
            return;
        }

        if (_time_to_start > 1) {
            _time_to_start--;
            String[] param = {String.valueOf(_time_to_start), String.valueOf(_minLevel), String.valueOf(_maxLevel)};
            sayToAll("scripts.events.DestructionOfFlag.AnnouncePreStart", param);
            executeTask("events.DestructionOfFlag.DestructionOfFlag", "announce", new Object[]{s}, 60000);
        } else {
            _status = 1;
            _isRegistrationActive = false;
            sayToAll("scripts.events.DestructionOfFlag.AnnounceEventStarting", null);
            executeTask("events.DestructionOfFlag.DestructionOfFlag", "prepare", new Object[]{s}, 5000);
        }
    }

    public void addPlayer() {
        Player player = getSelf();
        if (player == null || !checkPlayer(player, true))
            return;

        int min = Math.min(Math.min(players_list1.size(), players_list2.size()), players_list3.size());

        if (min == players_list1.size()) {
            players_list1.add(player.getStoredId());
        } else if (min == players_list2.size()) {
            players_list2.add(player.getStoredId());
        } else {
            players_list3.add(player.getStoredId());
        }
        show(new CustomMessage("scripts.events.DestructionOfFlag.Registered", player), player);
    }

    public static boolean checkPlayer(Player player, boolean first) {
        if (first && (!_isRegistrationActive || player.isDead())) {
            show(new CustomMessage("scripts.events.Late", player), player);
            return false;
        }

        if (first && players_list.contains(player.getStoredId())) {
            show(new CustomMessage("scripts.events.LastHero.Cancelled", player), player);
            return false;
        }

        if (player.getLevel() < _minLevel || player.getLevel() > _maxLevel) {
            show(new CustomMessage("scripts.events.LastHero.CancelledLevel", player), player);
            return false;
        }

        if (player.isMounted()) {
            show(new CustomMessage("scripts.events.LastHero.Cancelled", player), player);
            return false;
        }

        if (player.isInDuel()) {
            show(new CustomMessage("scripts.events.LastHero.CancelledDuel", player), player);
            return false;
        }

        if (player.getTeam() != TeamType.NONE) {
            show(new CustomMessage("scripts.events.LastHero.CancelledOtherEvent", player), player);
            return false;
        }

        if (player.getOlympiadGame() != null || first && Olympiad.isRegistered(player)) {
            show(new CustomMessage("scripts.events.LastHero.CancelledOlympiad", player), player);
            return false;
        }

        if (player.isTeleporting()) {
            show(new CustomMessage("scripts.events.LastHero.CancelledTeleport", player), player);
            return false;
        }

        if (first && PROTECT_IP_ACTIVE && sameIp(player)) {
            show("Вы не можете учавствовать на эвенте, с вашим IP уже кто-то зарегестрирован.", player, null);
            return false;
        }
        if (player.getObserverMode() != 0) {
            return false;
        }

        return true;
    }

    public static void prepare(String s) {
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
        executeTask("events.DestructionOfFlag.DestructionOfFlag", "go", new Object[]{s}, 60000);

        sayToAll("scripts.events.DestructionOfFlag.AnnounceFinalCountdown", null);
    }

    public static void go(String s) {
        if (players_list1.size() < MIN_PLAYERS || players_list2.size() < MIN_PLAYERS || players_list3.size() < MIN_PLAYERS) {
            Announcements.getInstance().announceToAll("DestructionOfFlag: эвент завершен, не было набрано минимальное кол-во участников.");
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
            cal.add(Calendar.DAY_OF_YEAR, 1);
        ThreadPoolManager.getInstance().schedule(new timer((int) (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000), 0);
    }

    public static void endBattle(int win) {
        if (_status == 0) return;
        _status = 0;

        if (whiteFlag != null) {
            whiteFlag.deleteMe();
            whiteFlag = null;
        }

        if (greenFlag != null) {
            greenFlag.deleteMe();
            greenFlag = null;
        }

        if (yellowFlag != null) {
            yellowFlag.deleteMe();
            yellowFlag = null;
        }

        if (blackFlag != null) {
            blackFlag.deleteMe();
            blackFlag = null;
        }

        ReflectionUtils.getDoor(17160024).closeMe();
        ReflectionUtils.getDoor(17160023).closeMe();
        ReflectionUtils.getDoor(17160020).closeMe();
        ReflectionUtils.getDoor(17160019).closeMe();
        ReflectionUtils.getDoor(17160022).closeMe();
        ReflectionUtils.getDoor(17160021).closeMe();

        if (win != 0) {
            if (win == 1) {
                Announcements.getInstance().announceToAll("Победила команда Белых!");
                giveItemsToWinner(win, 1);
            } else if (win == 2) {
                Announcements.getInstance().announceToAll("Победила команда Зеленых!");
                giveItemsToWinner(win, 1);
            } else if (win == 3) {
                Announcements.getInstance().announceToAll("Победила команда Желтых!");
                giveItemsToWinner(win, 1);
            } else if (win == 4) {
                Announcements.getInstance().announceToAll("Победила команда Черных!");
                giveItemsToWinner(win, 1);
            }
        } else Announcements.getInstance().announceToAll("Победивших нет.");


        sayToAll("scripts.events.DestructionOfFlag.AnnounceEnd", null);
        end();
        _isRegistrationActive = false;
    }

    public static void end() {
        executeTask("events.DestructionOfFlag.DestructionOfFlag", "removeAura", new Object[0], 1000);
        executeTask("events.DestructionOfFlag.DestructionOfFlag", "ressurectPlayers", new Object[0], 2000);
        executeTask("events.DestructionOfFlag.DestructionOfFlag", "healPlayers", new Object[0], 3000);
        executeTask("events.DestructionOfFlag.DestructionOfFlag", "teleportPlayersToSavedCoordsAll", new Object[0], 4000);
        executeTask("events.DestructionOfFlag.DestructionOfFlag", "autoContinue", new Object[0], 10000);
        backBuff();
        despawnNpcs();
    }

    public void autoContinue() {
        players_list1.clear();
        players_list2.clear();
        players_list3.clear();
        players_list4.clear();
        _saveBuffList.clear();

        if (_autoContinue > 0) {
            if (_autoContinue >= 6) {
                _autoContinue = 0;
                return;
            }
            start(new String[]{"" + (_autoContinue + 1), "" + (_autoContinue + 1)});
        }
    }

    public static void giveItemsToWinner(int win, double rate) {
        if (win == 1) {
            for (Player player : getPlayers(players_list1)) {
                for (int i = 0; i < REWARD.length; i += 2) {
                    addItem(player, REWARD[i], Math.round((EVENT_DestructionOfFlag_rate ? player.getLevel() : 1) * REWARD[i + 1] * rate));
                }
            }
        }
        if (win == 2) {
            for (Player player : getPlayers(players_list2)) {
                for (int i = 0; i < REWARD.length; i += 2) {
                    addItem(player, REWARD[i], Math.round((EVENT_DestructionOfFlag_rate ? player.getLevel() : 1) * REWARD[i + 1] * rate));
                }
            }
        }
        if (win == 3) {
            for (Player player : getPlayers(players_list3)) {
                for (int i = 0; i < REWARD.length; i += 2) {
                    addItem(player, REWARD[i], Math.round((EVENT_DestructionOfFlag_rate ? player.getLevel() : 1) * REWARD[i + 1] * rate));
                }
            }
        }
    }

    public static void saveBackCoords() {
        for (Player player : getPlayers(players_list1)) {
            player.setVar("DestructionOfFlag_backCoords", player.getX() + " " + player.getY() + " " + player.getZ() + " " + player.getReflection().getId(), 0);
            player.setVar("DestructionOfFlag_nameColor", Integer.toHexString(player.getNameColor()), 0);
        }
        for (Player player : getPlayers(players_list2)) {
            player.setVar("DestructionOfFlag_backCoords", player.getX() + " " + player.getY() + " " + player.getZ() + " " + player.getReflection().getId(), 0);
            player.setVar("DestructionOfFlag_nameColor", Integer.toHexString(player.getNameColor()), 0);
        }
        for (Player player : getPlayers(players_list3)) {
            player.setVar("DestructionOfFlag_backCoords", player.getX() + " " + player.getY() + " " + player.getZ() + " " + player.getReflection().getId(), 0);
            player.setVar("DestructionOfFlag_nameColor", Integer.toHexString(player.getNameColor()), 0);
        }
        for (Player player : getPlayers(players_list4)) {
            player.setVar("DestructionOfFlag_backCoords", player.getX() + " " + player.getY() + " " + player.getZ() + " " + player.getReflection().getId(), 0);
            player.setVar("DestructionOfFlag_nameColor", Integer.toHexString(player.getNameColor()), 0);
        }

        cleanPlayers();
        clearArena();
    }

    public static void teleportPlayersToColiseum() {
        for (Player player : getPlayers(players_list1)) {
            unRide(player);
            unSummonPet(player, true);
            if (REMOVE_BUFFS) {
                for (int buff[] : BUFFS_TO_REMOVE) {
                    List<Effect> effects;
                    if ((effects = player.getEffectList().getEffectsBySkillId(buff[0])) != null) {
                        if (buff.length == 2) {
                            for (Effect effect : effects) {
                                if (effect.getSkill().getLevel() == buff[1]) {
                                    player.getEffectList().stopEffect(buff[0]);
                                }
                            }
                        } else if (buff.length == 1) {
                            for (Effect effect : effects) {
                                player.getEffectList().stopEffect(buff[0]);
                            }
                        }
                    }
                }
            }
            Location pos = getLocForPlayer(player.getStoredId());
            if (pos != null) player.teleToLocation(pos);
            else removePlayer(player);
        }
        for (Player player : getPlayers(players_list2)) {
            unRide(player);
            unSummonPet(player, true);
            if (REMOVE_BUFFS) {
                for (int buff[] : BUFFS_TO_REMOVE) {
                    List<Effect> effects;
                    if ((effects = player.getEffectList().getEffectsBySkillId(buff[0])) != null) {
                        if (buff.length == 2) {
                            for (Effect effect : effects) {
                                if (effect.getSkill().getLevel() == buff[1]) {
                                    player.getEffectList().stopEffect(buff[0]);
                                }
                            }
                        } else if (buff.length == 1) {
                            for (Effect effect : effects) {
                                player.getEffectList().stopEffect(buff[0]);
                            }
                        }
                    }
                }
            }
            Location pos = getLocForPlayer(player.getStoredId());
            if (pos != null) player.teleToLocation(pos);
            else removePlayer(player);
        }
        for (Player player : getPlayers(players_list3)) {
            unRide(player);
            unSummonPet(player, true);
            if (REMOVE_BUFFS) {
                for (int buff[] : BUFFS_TO_REMOVE) {
                    List<Effect> effects;
                    if ((effects = player.getEffectList().getEffectsBySkillId(buff[0])) != null) {
                        if (buff.length == 2) {
                            for (Effect effect : effects) {
                                if (effect.getSkill().getLevel() == buff[1]) {
                                    player.getEffectList().stopEffect(buff[0]);
                                }
                            }
                        } else if (buff.length == 1) {
                            for (Effect effect : effects) {
                                player.getEffectList().stopEffect(buff[0]);
                            }
                        }
                    }
                }
            }
            Location pos = getLocForPlayer(player.getStoredId());
            if (pos != null) player.teleToLocation(pos);
            else removePlayer(player);
        }
    }

    public static void teleportPlayersToSavedCoords(int command) {
        switch (command) {
            case 1:
                for (Player player : getPlayers(players_list1))
                    teleportPlayerToSavedCoords(player);
                break;
            case 2:
                for (Player player : getPlayers(players_list2))
                    teleportPlayerToSavedCoords(player);
                break;
            case 3:
                for (Player player : getPlayers(players_list3))
                    teleportPlayerToSavedCoords(player);
                break;
            case 4:
                for (Player player : getPlayers(players_list4))
                    teleportPlayerToSavedCoords(player);
                break;
        }
    }

    public static void teleportPlayersToSavedCoordsAll() {
        for (Player player : getPlayers(players_list1))
            teleportPlayerToSavedCoords(player);
        for (Player player : getPlayers(players_list2))
            teleportPlayerToSavedCoords(player);
        for (Player player : getPlayers(players_list3))
            teleportPlayerToSavedCoords(player);
        for (Player player : getPlayers(players_list4))
            teleportPlayerToSavedCoords(player);
    }

    public static void teleportPlayerToSavedCoords(Player player) {
        try {
            String var = player.getVar("DestructionOfFlag_backCoords");
            String color = player.getVar("DestructionOfFlag_nameColor");
            if (!color.isEmpty())
                player.setNameColor(Integer.decode("0x" + color));
            if (var == null || var.equals(""))
                return;
            String[] coords = var.split(" ");
            if (coords.length != 4)
                return;
            player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]), Integer.parseInt(coords[3]));
            player.unsetVar("DestructionOfFlag_backCoords");
            player.unsetVar("DestructionOfFlag_nameColor");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void paralyzePlayers() {
        Skill revengeSkill = SkillTable.getInstance().getInfo(Skill.SKILL_RAID_CURSE, 1);
        for (Player player : getPlayers(players_list)) {
            player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
            revengeSkill.getEffects(player, player, false, false);
            //     if (player.getPet() != null)
            //        revengeSkill.getEffects(player, player.getPet(), false, false);
        }
    }

    public static void upParalyzePlayers() {
        for (Player player : getPlayers(players_list)) {
            player.getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);
            //     if (player.getPet() != null)
            //         player.getPet().getEffectList().stopEffect(Skill.SKILL_RAID_CURSE);

            player.leaveParty();
        }
    }

    public static void removeBuff() {
        saveBuffList();
        for (Player player : getPlayers(players_list1))
            if (player != null)
                try {
                    if (player.isCastingNow())
                        player.abortCast(true, true);

                    if (!ALLOW_CLAN_SKILL)
                        if (player.getClan() != null)
                            for (Skill skill : player.getClan().getAllSkills())
                                player.removeSkill(skill, false);

                    if (!ALLOW_HERO_SKILL)
                        if (player.isHero())
                            Hero.removeSkills(player);

                    if (!ALLOW_BUFFS) {
                        player.getEffectList().stopAllEffects();

                        //       if (player.getPet() != null) {
                        //           Summon summon = player.getPet();
                        //           summon.getEffectList().stopAllEffects();
                        //           if (summon.isPet())
                        //               summon.unSummon();
                        //       }

                        ThreadPoolManager.getInstance().schedule(new buffPlayer(player), 0);
                    }

                    player.sendPacket(new SkillList(player));
                } catch (Exception e) {
                    e.printStackTrace();
                }

        for (Player player : getPlayers(players_list2))
            if (player != null)
                try {
                    if (player.isCastingNow())
                        player.abortCast(true, true);

                    if (!ALLOW_CLAN_SKILL)
                        if (player.getClan() != null)
                            for (Skill skill : player.getClan().getAllSkills())
                                player.removeSkill(skill, false);

                    if (!ALLOW_HERO_SKILL)
                        if (player.isHero())
                            Hero.removeSkills(player);

                    if (!ALLOW_BUFFS) {
                        player.getEffectList().stopAllEffects();

                        //     if (player.getPet() != null) {
                        //         Summon summon = player.getPet();
                        //         summon.getEffectList().stopAllEffects();
                        //         if (summon.isPet())
                        //             summon.unSummon();
                        //     }

                        ThreadPoolManager.getInstance().schedule(new buffPlayer(player), 0);
                    }

                    player.sendPacket(new SkillList(player));
                } catch (Exception e) {
                    e.printStackTrace();
                }
        for (Player player : getPlayers(players_list3))
            if (player != null)
                try {
                    if (player.isCastingNow())
                        player.abortCast(true, true);

                    if (!ALLOW_CLAN_SKILL)
                        if (player.getClan() != null)
                            for (Skill skill : player.getClan().getAllSkills())
                                player.removeSkill(skill, false);

                    if (!ALLOW_HERO_SKILL)
                        if (player.isHero())
                            Hero.removeSkills(player);

                    if (!ALLOW_BUFFS) {
                        player.getEffectList().stopAllEffects();

                        //        if (player.getPet() != null) {
                        //            Summon summon = player.getPet();
                        //            summon.getEffectList().stopAllEffects();
                        //            if (summon.isPet())
                        //                summon.unSummon();
                        //        }

                        ThreadPoolManager.getInstance().schedule(new buffPlayer(player), 0);
                    }

                    player.sendPacket(new SkillList(player));
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

    public static void backBuff() {
        for (Player player : getPlayers(players_list1)) {
            if (player == null)
                continue;
            try {
                player.getEffectList().stopAllEffects();

                if (!ALLOW_CLAN_SKILL)
                    if (player.getClan() != null)
                        for (Skill skill : player.getClan().getAllSkills())
                            if (skill.getMinPledgeClass() <= player.getPledgeClass())
                                player.addSkill(skill, false);

                if (!ALLOW_HERO_SKILL)
                    if (player.isHero())
                        Hero.addSkills(player);

                player.sendPacket(new SkillList(player));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Player player : getPlayers(players_list2)) {
            if (player == null)
                continue;
            try {
                player.getEffectList().stopAllEffects();

                if (!ALLOW_CLAN_SKILL)
                    if (player.getClan() != null)
                        for (Skill skill : player.getClan().getAllSkills())
                            if (skill.getMinPledgeClass() <= player.getPledgeClass())
                                player.addSkill(skill, false);

                if (!ALLOW_HERO_SKILL)
                    if (player.isHero())
                        Hero.addSkills(player);

                player.sendPacket(new SkillList(player));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Player player : getPlayers(players_list3)) {
            if (player == null)
                continue;
            try {
                player.getEffectList().stopAllEffects();

                if (!ALLOW_CLAN_SKILL)
                    if (player.getClan() != null)
                        for (Skill skill : player.getClan().getAllSkills())
                            if (skill.getMinPledgeClass() <= player.getPledgeClass())
                                player.addSkill(skill, false);

                if (!ALLOW_HERO_SKILL)
                    if (player.isHero())
                        Hero.addSkills(player);

                player.sendPacket(new SkillList(player));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        restoreBuffList();
    }

    public static void ressurectPlayers() {
        for (Player player : getPlayers(players_list1))
            ressurectPlayer(player);
        for (Player player : getPlayers(players_list2))
            ressurectPlayer(player);
        for (Player player : getPlayers(players_list3))
            ressurectPlayer(player);
        for (Player player : getPlayers(players_list4))
            ressurectPlayer(player);
    }

    public static void ressurectPlayer(Player player) {
        if (player.isDead()) {
            player.restoreExp();
            player.setCurrentCp(player.getMaxCp());
            player.setCurrentHp(player.getMaxHp(), true);
            player.setCurrentMp(player.getMaxMp());
            player.broadcastPacket(new Revive(player));
        }
    }

    public static void healPlayers() {
        for (Player player : getPlayers(players_list1)) {
            player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
            player.setCurrentCp(player.getMaxCp());
        }
        for (Player player : getPlayers(players_list2)) {
            player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
            player.setCurrentCp(player.getMaxCp());
        }
        for (Player player : getPlayers(players_list3)) {
            player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
            player.setCurrentCp(player.getMaxCp());
        }
        for (Player player : getPlayers(players_list4)) {
            player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
            player.setCurrentCp(player.getMaxCp());
        }
    }

    public static void cleanPlayers() {
        for (Player player : getPlayers(players_list1))
            if (!checkPlayer(player, false))
                removePlayer(player);
            else
                setTeam(player);
        for (Player player : getPlayers(players_list2))
            if (!checkPlayer(player, false))
                removePlayer(player);
            else
                setTeam(player);
        for (Player player : getPlayers(players_list3))
            if (!checkPlayer(player, false))
                removePlayer(player);
            else
                setTeam(player);
        for (Player player : getPlayers(players_list4))
            if (!checkPlayer(player, false))
                removePlayer(player);
            else
                setTeam(player);
    }

    public static void clearArena() {
        for (GameObject obj : _zone.getObjects())
            if (obj != null) {
                Player player = obj.getPlayer();
                if (player != null && playerInCommand(player.getStoredId()) == 0)
                    player.teleToLocation(147451, 46728, -3410);
            }
    }

    public static void doDie(Creature self, Creature killer) {
        if (_status <= 1 || self == null) return;

        if (self.isPlayer() && playerInCommand(self.getStoredId()) > 0) {
            self.sendMessage("Через " + TIME_FOR_RES + " секунд вы будите восстановлены.");
            _resurrectionList.put(self.getStoredId(), executeTask("events.DestructionOfFlag.DestructionOfFlag", "resurrectAtBase", new Object[]{(Player) self}, TIME_FOR_RES * 100));
        }

        if (self instanceof MonsterInstance && (self == greenFlag || self == whiteFlag || self == yellowFlag || self == blackFlag)) {
            lossTeam((MonsterInstance) self);
        }
    }

    public static void resurrectAtBase(Player player) {
        if (playerInCommand(player.getStoredId()) <= 0)
            return;
        if (player.isDead())
            ressurectPlayer(player);

        Location pos = getLocForPlayer(player.getStoredId());
        if (pos != null) player.teleToLocation(pos);
        else removePlayer(player);

        if (!ALLOW_BUFFS) ThreadPoolManager.getInstance().schedule(new buffPlayer(player), 0);
        else ThreadPoolManager.getInstance().schedule(new restoreBuffListForPlayer(player), 0);
    }

    public static Location OnEscape(Player player) {
        if (_status > 1 && player != null && playerInCommand(player.getStoredId()) > 0)
            removePlayer(player);
        return null;
    }

    public static void OnPlayerExit(Player player) {
        if (player == null || playerInCommand(player.getStoredId()) < 1)
            return;

        // Вышел или вылетел во время регистрации
        if (_status == 0 && _isRegistrationActive && playerInCommand(player.getStoredId()) > 0) {
            removePlayer(player);
            return;
        }

        // Вышел или вылетел во время телепортации
        if (_status == 1 && playerInCommand(player.getStoredId()) > 0) {
            removePlayer(player);
            return;
        }

        // Вышел или вылетел во время эвента
        OnEscape(player);
    }

    public static class TeleportTask implements Runnable {
        Location loc;
        Creature target;

        public TeleportTask(Creature target, Location loc) {
            this.target = target;
            this.loc = loc;
            target.startStunning();
        }

        public void run() {
            target.stopStunning();
            target.teleToLocation(loc);
        }
    }

    private static void removePlayer(Player player) {
        players_list1.remove(player.getStoredId());
        players_list2.remove(player.getStoredId());
        players_list3.remove(player.getStoredId());
        players_list4.remove(player.getStoredId());
        teleportPlayerToSavedCoords(player);
    }

    private static LazyArrayList<Player> getPlayers(List<Long> list) {
        LazyArrayList<Player> result = new LazyArrayList<Player>();
        for (Long storeId : list) {
            Player player = GameObjectsStorage.getAsPlayer(storeId);
            if (player != null)
                result.add(player);
        }
        return result;
    }

    public static void saveBuffList() {
        Effect skill[];
        for (Player player : getPlayers(players_list1))
            if (player != null) {
                skill = player.getEffectList().getAllFirstEffects();
                if (skill.length == 0)
                    continue;
                for (Effect effect : skill) {
                    if (!_saveBuffList.containsKey(player.getStoredId()))
                        _saveBuffList.put(player.getStoredId(), new LazyArrayList<Effect>());

                    for (int id : _listAllowSaveBuffs)
                        if (effect.getSkill().getId() == id)
                            _saveBuffList.get(player.getStoredId()).add(effect);
                }
            }


        for (Player player : getPlayers(players_list2))
            if (player != null) {
                skill = player.getEffectList().getAllFirstEffects();
                if (skill.length == 0)
                    continue;
                for (Effect effect : skill) {
                    if (!_saveBuffList.containsKey(player.getStoredId()))
                        _saveBuffList.put(player.getStoredId(), new LazyArrayList<Effect>());

                    for (int id : _listAllowSaveBuffs)
                        if (effect.getSkill().getId() == id)
                            _saveBuffList.get(player.getStoredId()).add(effect);
                }
            }
    }

    public static void restoreBuffList() {
        Player player;
        for (long objId : _saveBuffList.keySet()) {
            player = GameObjectsStorage.getAsPlayer(objId);
            ThreadPoolManager.getInstance().schedule(new restoreBuffListForPlayer(player), 100);
        }
    }

    public static class restoreBuffListForPlayer implements Runnable {
        Player player;

        restoreBuffListForPlayer(Player player) {
            this.player = player;
        }

        public void run() {
            if (player == null)
                return;

            player.getEffectList().stopAllEffects();

            LazyArrayList<Effect> effects = _saveBuffList.get(player.getStoredId());

            if (effects != null && effects.size() > 0) {
                for (Effect effect : effects) {

                    for (EffectTemplate et : effect.getSkill().getEffectTemplates()) {
                        Env env = new Env(player, player, effect.getSkill());
                        env.value = Integer.MAX_VALUE;
                        Effect e = et.getEffect(env);
                        e.setPeriod(effect.getPeriod());// 3 часа
                        e.getEffected().getEffectList().addEffect(e);
                    }

                    try {
                        Thread.sleep(150);
                    } catch (Exception e) {
                    }
                }
            }
            player.setCurrentCp(player.getMaxCp());
            player.setCurrentHp(player.getMaxHp(), true);
            player.setCurrentMp(player.getMaxMp());
        }
    }

    public static class buffPlayer implements Runnable {
        Player player;

        buffPlayer(Player player) {
            this.player = player;
        }

        public void run() {
            if (player == null)
                return;
            Skill skill;
            for (int[] buff : _listBuff[player.isMageClass() ? 1 : 0]) {
                skill = SkillTable.getInstance().getInfo(buff[0], buff[1]);
                for (EffectTemplate et : skill.getEffectTemplates()) {
                    Env env = new Env(player, player, skill);
                    env.value = Integer.MAX_VALUE;
                    Effect e = et.getEffect(env);
                    e.setPeriod(600000);// 3 часа
                    e.getEffected().getEffectList().addEffect(e);
                }
                try {
                    Thread.sleep(150);
                } catch (Exception e) {
                }
            }
            player.setCurrentCp(player.getMaxCp());
            player.setCurrentHp(player.getMaxHp(), true);
            player.setCurrentMp(player.getMaxMp());
        }
    }

    public static class timer implements Runnable {
        int time;

        timer(int time) {
            this.time = time;
        }

        public void run() {
            int sec;
            String message;
            while (time > 0 && _status == 2) {
                sec = time - (time / 60) * 60;
                for (Player player : getPlayers(players_list1)) {
                    if (sec < 10)
                        message = " Осталось минут: " + Integer.toString(time / 60) + ":0" + Integer.toString(sec) + " ";
                    else
                        message = " Осталось минут: " + Integer.toString(time / 60) + ":" + Integer.toString(sec) + " ";
                    if (greenFlag != null) message += "\n Green Flag: " + greenFlag.getCurrentHp() + " Hp ";
                    if (whiteFlag != null) message += "\n White Flag: " + whiteFlag.getCurrentHp() + " Hp ";
                    if (yellowFlag != null) message += "\n Yellow Flag: " + yellowFlag.getCurrentHp() + " Hp ";
                    if (blackFlag != null) message += "\n Black Flag: " + blackFlag.getCurrentHp() + " Hp ";
                    player.sendPacket(new ExShowScreenMessage(message, 2000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, false));
                }

                for (Player player : getPlayers(players_list2)) {
                    if (sec < 10)
                        message = " Осталось минут: " + Integer.toString(time / 60) + ":0" + Integer.toString(sec) + " ";
                    else
                        message = " Осталось минут: " + Integer.toString(time / 60) + ":" + Integer.toString(sec) + " ";
                    if (whiteFlag != null) message += "\n White Flag: " + whiteFlag.getCurrentHp() + " Hp ";
                    if (greenFlag != null) message += "\n Green Flag: " + greenFlag.getCurrentHp() + " Hp ";
                    if (yellowFlag != null) message += "\n Yellow Flag: " + yellowFlag.getCurrentHp() + " Hp ";
                    if (blackFlag != null) message += "\n Black Flag: " + blackFlag.getCurrentHp() + " Hp ";
                    player.sendPacket(new ExShowScreenMessage(message, 2000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, false));
                }

                for (Player player : getPlayers(players_list3)) {
                    if (sec < 10)
                        message = " Осталось минут: " + Integer.toString(time / 60) + ":0" + Integer.toString(sec) + " ";
                    else
                        message = " Осталось минут: " + Integer.toString(time / 60) + ":" + Integer.toString(sec) + " ";
                    if (blackFlag != null) message += "\n Black Flag: " + blackFlag.getCurrentHp() + " Hp ";
                    if (yellowFlag != null) message += "\n Yellow Flag: " + yellowFlag.getCurrentHp() + " Hp ";
                    if (greenFlag != null) message += "\n Green Flag: " + greenFlag.getCurrentHp() + " Hp ";
                    if (whiteFlag != null) message += "\n White Flag: " + whiteFlag.getCurrentHp() + " Hp ";
                    player.sendPacket(new ExShowScreenMessage(message, 2000, ExShowScreenMessage.ScreenMessageAlign.BOTTOM_RIGHT, false));
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                time--;
            }
            endBattle(0);
        }

    }

    public static int playerInCommand(long objectId) {
        return players_list1.contains(objectId) ? 1 : players_list2.contains(objectId) ? 2 : players_list3.contains(objectId) ? 3 : 0;
    }

    public static Location getLocForPlayer(long objectId) {
        switch (playerInCommand(objectId)) {
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

    public static void setTeam(Player player) {
        int command = playerInCommand(player.getStoredId());
        if (command < 1 || command > 3) {
            removePlayer(player);
            return;
        }
        player.setNameColor(Integer.decode("0x" + colors[playerInCommand(player.getStoredId()) - 1]));
    }

    public static void lossTeam(MonsterInstance flag) {
        if (flag == greenFlag) {
            lossTeam(players_list1);
            greenFlag.deleteMe();
            if (players_list2.isEmpty()) endBattle(2);
            else if (players_list2.isEmpty()) endBattle(3);
            else if (players_list2.isEmpty()) endBattle(4);
            else if (players_list3.isEmpty()) endBattle(2);
            else if (players_list3.isEmpty()) endBattle(3);
            else if (players_list3.isEmpty()) endBattle(4);
            else if (players_list4.isEmpty()) endBattle(2);
            else if (players_list4.isEmpty()) endBattle(3);
            else if (players_list4.isEmpty()) endBattle(4);
        }
        if (flag == whiteFlag) {
            lossTeam(players_list2);
            whiteFlag.deleteMe();
            if (players_list1.isEmpty()) endBattle(1);
            else if (players_list1.isEmpty()) endBattle(3);
            else if (players_list1.isEmpty()) endBattle(4);
            else if (players_list3.isEmpty()) endBattle(1);
            else if (players_list3.isEmpty()) endBattle(3);
            else if (players_list3.isEmpty()) endBattle(4);
            else if (players_list4.isEmpty()) endBattle(1);
            else if (players_list4.isEmpty()) endBattle(3);
            else if (players_list4.isEmpty()) endBattle(4);
        }
        if (flag == yellowFlag) {
            lossTeam(players_list3);
            yellowFlag.deleteMe();
            if (players_list1.isEmpty()) endBattle(1);
            else if (players_list1.isEmpty()) endBattle(2);
            else if (players_list1.isEmpty()) endBattle(4);
            else if (players_list2.isEmpty()) endBattle(1);
            else if (players_list2.isEmpty()) endBattle(2);
            else if (players_list2.isEmpty()) endBattle(4);
            else if (players_list4.isEmpty()) endBattle(1);
            else if (players_list4.isEmpty()) endBattle(2);
            else if (players_list4.isEmpty()) endBattle(4);
        }
        if (flag == blackFlag) {
            lossTeam(players_list4);
            blackFlag.deleteMe();
            if (players_list1.isEmpty()) endBattle(1);
            else if (players_list1.isEmpty()) endBattle(2);
            else if (players_list1.isEmpty()) endBattle(3);
            else if (players_list2.isEmpty()) endBattle(1);
            else if (players_list2.isEmpty()) endBattle(2);
            else if (players_list2.isEmpty()) endBattle(3);
            else if (players_list3.isEmpty()) endBattle(1);
            else if (players_list3.isEmpty()) endBattle(2);
            else if (players_list3.isEmpty()) endBattle(3);
        }
        flag.deleteMe();
    }

    public static void lossTeam(LazyArrayList<Long> team) {
        Player player;
        for (long objId : team) {
            player = GameObjectsStorage.getAsPlayer(objId);
            if (player != null) {
                removePlayer(player);
                player.sendMessage("Ваш флаг - уничтожен. Вы проиграли.");
            }
        }
        team.clear();
    }

    public static boolean canJoinParty(Player player, Player target) {

        return !(playerInCommand(player.getStoredId()) > 0 || playerInCommand(target.getStoredId()) > 0) || playerInCommand(player.getStoredId()) == playerInCommand(target.getStoredId());
    }


    public static boolean canUseItem(Player player, ItemInstance item) {
        if (ALLOW_RESTRICT_ITEMS && playerInCommand(player.getStoredId()) > 0) {
            for (int restrict_id : RESTRICT_ITEMS)
                if (item.getItemId() == restrict_id) return false;
        }
        return true;
    }

    public static boolean useSkill(Creature player, Creature target, Skill skill) {
        return checkTarget(player, target, skill);
    }

    public static boolean checkTarget(Player player, Creature target) {
        return checkTarget(player, target, null);
    }

    public static boolean checkTarget(Creature character, Creature target, Skill skill) {
        if (_status < 2)
            return true;

        if (character instanceof Player && target != null && target != character) {
            if (playerInCommand(character.getStoredId()) > 0) {
                if (target instanceof MonsterInstance) {
                    if (getMonsterTeam(target) == playerInCommand(character.getObjectId())) {
                        _log.info("Monster Team: " + getMonsterTeam(target) + " | Player Team: " + playerInCommand(character.getObjectId()));
                        return false;
                    }

                    return true;
                }

                if (skill != null) {

                    if (ALLOW_RESTRICT_SKILLS) {
                        for (int[] restrict : RESTRICT_SKILLS)
                            if (skill.getId() == restrict[0]) {
                                if (restrict[1] == 0)
                                    return character.getStoredId().equals(target.getStoredId());

                                if (restrict[1] == 1)
                                    return playerInCommand(character.getStoredId()) == playerInCommand(target.getStoredId());
                            }
                    }

                    if (playerInCommand(target.getStoredId()) > 0) {
                        switch (skill.getSkillType()) {
                            case BUFF:
                            case HEAL:
                            case HEAL_PERCENT:
                            case BALANCE:
                            case COMBATPOINTHEAL:
                            case MANAHEAL:
                            case MANAHEAL_PERCENT:
                                return playerInCommand(character.getStoredId()) == playerInCommand(target.getStoredId());
                            default:
                                for (Creature targ : skill.getTargets(character, target, true)) {
                                    if (targ instanceof Player) {
                                        if (playerInCommand(character.getStoredId()) == playerInCommand(targ.getStoredId()))
                                            return false;
                                    } else if (getMonsterTeam(targ) == playerInCommand(character.getObjectId()))
                                        return false;
                                }
                        }
                    }

                    if (playerInCommand(target.getStoredId()) == 0) {
                        switch (skill.getSkillType()) {
                            case MDAM:
                            case PDAM:
                            case DISCORD:
                            case AGGRESSION:
                            case BLEED:
                            case STUN:
                            case DEBUFF:
                                return playerInCommand(character.getStoredId()) != playerInCommand(target.getStoredId());
                            default:
                                for (Creature targ : skill.getTargets(character, target, true)) {
                                    if (targ instanceof Player) {
                                        if (playerInCommand(character.getStoredId()) != playerInCommand(targ.getStoredId()))
                                            return false;
                                    } else if (getMonsterTeam(targ) == playerInCommand(character.getObjectId()))
                                        return false;

                                }
                        }
                    }


                    for (Creature targ : skill.getTargets(character, target, true)) {
                        if (targ instanceof Player) {
                            if (playerInCommand(character.getStoredId()) == playerInCommand(targ.getStoredId()))
                                return false;
                        } else if (getMonsterTeam(targ) == playerInCommand(character.getObjectId()))
                            return false;
                    }
                }

                return playerInCommand(character.getStoredId()) != playerInCommand(target.getStoredId());
            } else {
                if (playerInCommand(target.getStoredId()) > 0 || getMonsterTeam(target) > 0)
                    return false;
            }
        }
        return true;
    }

    private static int getMonsterTeam(Creature monster) {
        if (monster.getStoredId().equals(greenFlag.getStoredId())) return 1;
        else if (monster.getStoredId().equals(whiteFlag.getStoredId())) return 2;
        else if (monster.getStoredId().equals(yellowFlag.getStoredId())) return 3;
        else if (monster.getStoredId().equals(blackFlag.getStoredId())) return 4;
        else return 0;
    }

    public static boolean sameIp(Player player) {
        Player part;
        for (long objId : players_list1) {
            part = GameObjectsStorage.getAsPlayer(objId);
            if (part == null) continue;

            if (player.getNetConnection().getIpAddr().equals(part.getNetConnection().getIpAddr()))
                return true;
        }
        for (long objId : players_list2) {
            part = GameObjectsStorage.getAsPlayer(objId);
            if (part == null) continue;

            if (player.getNetConnection().getIpAddr().startsWith(part.getNetConnection().getIpAddr()))
                return true;
        }
        for (long objId : players_list3) {
            part = GameObjectsStorage.getAsPlayer(objId);
            if (part == null) continue;

            if (player.getNetConnection().getIpAddr().startsWith(part.getNetConnection().getIpAddr()))
                return true;
        }
        for (long objId : players_list4) {
            part = GameObjectsStorage.getAsPlayer(objId);
            if (part == null) continue;

            if (player.getNetConnection().getIpAddr().startsWith(part.getNetConnection().getIpAddr()))
                return true;
        }
        return false;
    }

    private static class ZoneListener implements OnZoneEnterLeaveListener {
        @Override
        public void onZoneEnter(Zone zone, Creature cha) {
            if (cha == null)
                return;
            Player player = cha.getPlayer();
            if (_status > 0 && player != null && !live_list.contains(player.getStoredId()))
                ThreadPoolManager.getInstance().schedule(new TeleportTask(cha, new Location(147451, 46728, -3410)), 3000);
        }

        @Override
        public void onZoneLeave(Zone zone, Creature cha) {
            if (cha == null)
                return;
            Player player = cha.getPlayer();
            if (_status > 1 && player != null && player.getTeam() != TeamType.NONE && live_list.contains(player.getStoredId())) {
                double angle = PositionUtils.convertHeadingToDegree(cha.getHeading()); // угол в градусах
                double radian = Math.toRadians(angle - 90); // угол в радианах
                int x = (int) (cha.getX() + 50 * Math.sin(radian));
                int y = (int) (cha.getY() - 50 * Math.cos(radian));
                int z = cha.getZ();
                ThreadPoolManager.getInstance().schedule(new TeleportTask(cha, new Location(x, y, z)), 3000);
            }
        }
    }
}

