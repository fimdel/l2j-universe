package events.DeathMatch;

import l2p.commons.geometry.Polygon;
import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.Announcements;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.instancemanager.ServerVariables;
import l2p.gameserver.listener.actor.OnDeathListener;
import l2p.gameserver.listener.actor.player.OnPlayerExitListener;
import l2p.gameserver.listener.actor.player.OnTeleportListener;
import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.*;
import l2p.gameserver.model.actor.listener.CharListenerList;
import l2p.gameserver.model.base.TeamType;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.entity.events.impl.DuelEvent;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.model.entity.residence.Castle;
import l2p.gameserver.model.entity.residence.Residence;
import l2p.gameserver.network.serverpackets.Revive;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.network.serverpackets.components.CustomMessage;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.skills.AbnormalEffect;
import l2p.gameserver.tables.SkillTable;
import l2p.gameserver.templates.DoorTemplate;
import l2p.gameserver.templates.ZoneTemplate;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.PositionUtils;
import l2p.gameserver.utils.ReflectionUtils;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;

//TODO: сделать единый интерфейс для всех эвентов и наследовать от него
public class DeathMatch extends Functions implements ScriptFile, OnDeathListener, OnTeleportListener, OnPlayerExitListener {

    private static final Logger _log = LoggerFactory.getLogger(DeathMatch.class);

    private static ScheduledFuture<?> _startTask;

    private static final int[] doors = new int[]{24190001, 24190002, 24190003, 24190004};

    private static List<Long> players_list1 = new CopyOnWriteArrayList<Long>();
    private static List<Long> players_list2 = new CopyOnWriteArrayList<Long>();
    private static List<Long> live_list1 = new CopyOnWriteArrayList<Long>();
    private static List<Long> live_list2 = new CopyOnWriteArrayList<Long>();

    private static int[][] mage_buffs = new int[Config.EVENT_DeathMatchMageBuffs.length][2];
    private static int[][] fighter_buffs = new int[Config.EVENT_DeathMatchFighterBuffs.length][2];

    private static int[][] rewards = new int[Config.EVENT_DeathMatchRewards.length][2];

    private static Map<Long, Location> playerRestoreCoord = new LinkedHashMap<Long, Location>();

    private static Map<Long, String> boxes = new LinkedHashMap<Long, String>();

    private static boolean _isRegistrationActive = false;
    private static int _status = 0;
    private static int _time_to_start;
    private static int _category;
    private static int _minLevel;
    private static int _maxLevel;
    private static int _autoContinue = 0;
    private static boolean _active = false;
    private static Skill buff;

    private static ScheduledFuture<?> _endTask;

    private static Zone _zone = ReflectionUtils.getZone("[colosseum_battle]");
    private static Map<String, ZoneTemplate> _zones = new HashMap<String, ZoneTemplate>();
    private static IntObjectMap<DoorTemplate> _doors = new HashIntObjectMap<DoorTemplate>();
    private static ZoneListener _zoneListener = new ZoneListener();

    private static Territory team1spawn = new Territory().add(new Polygon().add(149878, 47505).add(150262, 47513).add(150502, 47233).add(150507, 46300).add(150256, 46002).add(149903, 46005).setZmin(-3408).setZmax(-3308));

    private static Territory team2spawn = new Territory().add(new Polygon().add(149027, 46005).add(148686, 46003).add(148448, 46302).add(148449, 47231).add(148712, 47516).add(149014, 47527).setZmin(-3408).setZmax(-3308));

    @Override
    public void onLoad() {
        CharListenerList.addGlobal(this);

        _zones.put("[colosseum_battle]", ReflectionUtils.getZone("[colosseum_battle]").getTemplate());
        for (final int doorId : doors)
            _doors.put(doorId, ReflectionUtils.getDoor(doorId).getTemplate());

        _active = ServerVariables.getString("DeathMatch", "off").equalsIgnoreCase("on");
        if (isActive())
            scheduleEventStart();

        _zone.addListener(_zoneListener);

        int i = 0;

        if (Config.EVENT_DeathMatchBuffPlayers && Config.EVENT_DeathMatchMageBuffs.length != 0)
            for (String skill : Config.EVENT_DeathMatchMageBuffs) {
                String[] splitSkill = skill.split(",");
                mage_buffs[i][0] = Integer.parseInt(splitSkill[0]);
                mage_buffs[i][1] = Integer.parseInt(splitSkill[1]);
                i++;
            }

        i = 0;

        if (Config.EVENT_DeathMatchBuffPlayers && Config.EVENT_DeathMatchMageBuffs.length != 0)
            for (String skill : Config.EVENT_DeathMatchFighterBuffs) {
                String[] splitSkill = skill.split(",");
                fighter_buffs[i][0] = Integer.parseInt(splitSkill[0]);
                fighter_buffs[i][1] = Integer.parseInt(splitSkill[1]);
                i++;
            }

        i = 0;
        if (Config.EVENT_DeathMatchRewards.length != 0)
            for (String reward : Config.EVENT_DeathMatchRewards) {
                String[] splitReward = reward.split(",");
                rewards[i][0] = Integer.parseInt(splitReward[0]);
                rewards[i][1] = Integer.parseInt(splitReward[1]);
                i++;
            }

        _log.info("Loaded Event: DeathMatch");
    }

    @Override
    public void onReload() {
        if (_startTask != null) {
            _startTask.cancel(false);
            _startTask = null;
        }
    }

    @Override
    public void onShutdown() {
        onReload();
    }

    private static boolean isActive() {
        return _active;
    }

    public void activateEvent() {
        Player player = getSelf();
        if (!player.getPlayerAccess().IsEventGm)
            return;

        if (!isActive()) {
            if (_startTask == null)
                scheduleEventStart();
            ServerVariables.set("DeathMatch", "on");
            _log.info("Event 'DeathMatch' activated.");
            Announcements.getInstance().announceByCustomMessage("scripts.events.DeathMatch.AnnounceEventStarted", null);
        } else
            player.sendMessage("Event 'DeathMatch' already active.");

        _active = true;

        show("admin/events/events.htm", player);
    }

    public void deactivateEvent() {
        Player player = getSelf();
        if (!player.getPlayerAccess().IsEventGm)
            return;

        if (isActive()) {
            if (_startTask != null) {
                _startTask.cancel(false);
                _startTask = null;
            }
            ServerVariables.unset("DeathMatch");
            _log.info("Event 'DeathMatch' deactivated.");
            Announcements.getInstance().announceByCustomMessage("scripts.events.DeathMatch.AnnounceEventStoped", null);
        } else
            player.sendMessage("Event 'DeathMatch' not active.");

        _active = false;

        show("admin/events/events.htm", player);
    }

    public static boolean isRunned() {
        return _isRegistrationActive || _status > 0;
    }

    public static int getMinLevelForCategory(int category) {
        switch (category) {
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
            case 8:
                return 96;
        }
        return 0;
    }

    public static int getMaxLevelForCategory(int category) {
        switch (category) {
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
                return 95;
            case 8:
                return 99;
        }
        return 0;
    }

    public static int getCategory(int level) {
        if (level >= 20 && level <= 29)
            return 1;
        else if (level >= 30 && level <= 39)
            return 2;
        else if (level >= 40 && level <= 51)
            return 3;
        else if (level >= 52 && level <= 61)
            return 4;
        else if (level >= 62 && level <= 75)
            return 5;
        else if (level >= 76 && level <= 85)
            return 6;
        else if (level >= 86 && level <= 95)
            return 7;
        else if (level >= 96 && level <= 99)
            return 8;
        return 0;
    }

    public void start(String[] var) {
        Player player = getSelf();
        if (var.length != 2) {
            show(new CustomMessage("common.Error", player), player);
            return;
        }

        Integer category;
        Integer autoContinue;
        try {
            category = Integer.valueOf(var[0]);
            autoContinue = Integer.valueOf(var[1]);
        } catch (Exception e) {
            show(new CustomMessage("common.Error", player), player);
            return;
        }

        _category = category;
        _autoContinue = autoContinue;

        if (_category == -1) {
            _minLevel = 1;
            _maxLevel = 99;
        } else {
            _minLevel = getMinLevelForCategory(_category);
            _maxLevel = getMaxLevelForCategory(_category);
        }

        if (_endTask != null) {
            show(new CustomMessage("common.TryLater", player), player);
            return;
        }

        _status = 0;
        _isRegistrationActive = true;
        _time_to_start = Config.EVENT_DeathMatchTime;

        players_list1 = new CopyOnWriteArrayList<Long>();
        players_list2 = new CopyOnWriteArrayList<Long>();
        live_list1 = new CopyOnWriteArrayList<Long>();
        live_list2 = new CopyOnWriteArrayList<Long>();

        playerRestoreCoord = new LinkedHashMap<Long, Location>();

        String[] param = {String.valueOf(_time_to_start), String.valueOf(_minLevel), String.valueOf(_maxLevel)};
        sayToAll("scripts.events.DeathMatch.AnnouncePreStart", param);

        executeTask("events.DeathMatch.DeathMatch", "question", new Object[0], 10000);
        executeTask("events.DeathMatch.DeathMatch", "announce", new Object[0], 60000);
    }

    public static void sayToAll(String address, String[] replacements) {
        Announcements.getInstance().announceByCustomMessage(address, replacements, ChatType.CRITICAL_ANNOUNCE);
    }

    public static void question() {
        for (Player player : GameObjectsStorage.getAllPlayersForIterate())
            if (player != null && !player.isDead() && player.getLevel() >= _minLevel && player.getLevel() <= _maxLevel && player.getReflection().isDefault() && !player.isInOlympiadMode() && !player.isInObserverMode())
                player.scriptRequest(new CustomMessage("scripts.events.DeathMatch.AskPlayer", player).toString(), "events.DeathMatch.DeathMatch:addPlayer", new Object[0]);
    }

    public static void announce() {
        if (_time_to_start > 1) {
            _time_to_start--;
            String[] param = {String.valueOf(_time_to_start), String.valueOf(_minLevel), String.valueOf(_maxLevel)};
            sayToAll("scripts.events.DeathMatch.AnnouncePreStart", param);
            executeTask("events.DeathMatch.DeathMatch", "announce", new Object[0], 60000);
        } else if (players_list1.isEmpty() || players_list2.isEmpty() || players_list1.size() < Config.EVENT_DeathMatchMinPlayerInTeam || players_list2.size() < Config.EVENT_DeathMatchMinPlayerInTeam) {
            sayToAll("scripts.events.DeathMatch.AnnounceEventCancelled", null);
            _isRegistrationActive = false;
            _status = 0;
            executeTask("events.DeathMatch.DeathMatch", "autoContinue", new Object[0], 10000);
            return;
        } else {
            _status = 1;
            _isRegistrationActive = false;
            sayToAll("scripts.events.DeathMatch.AnnounceEventStarting", null);
            executeTask("events.DeathMatch.DeathMatch", "prepare", new Object[0], 5000);
        }
    }

    public void addPlayer() {
        Player player = getSelf();
        if (player == null || !checkPlayer(player, true) || !checkDualBox(player))
            return;

        int team = 0, size1 = players_list1.size(), size2 = players_list2.size();

        if (size1 == Config.EVENT_DeathMatchMaxPlayerInTeam && size2 == Config.EVENT_DeathMatchMaxPlayerInTeam) {
            show(new CustomMessage("scripts.events.DeathMatch.CancelledCount", player), player);
            _isRegistrationActive = false;
            return;
        }

        if (!Config.EVENT_DeathMatchAllowMultiReg) {
            if ("IP".equalsIgnoreCase(Config.EVENT_DeathMatchCheckWindowMethod))
                boxes.put(player.getStoredId(), player.getIP());
            if ("HWid".equalsIgnoreCase(Config.EVENT_DeathMatchCheckWindowMethod))
                boxes.put(player.getStoredId(), player.getNetConnection().getHWID());
        }

        if (size1 > size2)
            team = 2;
        else if (size1 < size2)
            team = 1;
        else
            team = Rnd.get(1, 2);

        if (team == 1) {
            players_list1.add(player.getStoredId());
            live_list1.add(player.getStoredId());
            show(new CustomMessage("scripts.events.DeathMatch.Registered", player), player);
        } else if (team == 2) {
            players_list2.add(player.getStoredId());
            live_list2.add(player.getStoredId());
            show(new CustomMessage("scripts.events.DeathMatch.Registered", player), player);
        } else
            _log.info("WTF??? Command id 0 in DeathMatch...");
    }

    public static boolean checkPlayer(Player player, boolean first) {

        if (first && (!_isRegistrationActive || player.isDead())) {
            show(new CustomMessage("scripts.events.Late", player), player);
            return false;
        }

        if (first && (players_list1.contains(player.getStoredId()) || players_list2.contains(player.getStoredId()))) {
            show(new CustomMessage("scripts.events.DeathMatch.Cancelled", player), player);
            if (players_list1.contains(player.getStoredId()))
                players_list1.remove(player.getStoredId());
            if (players_list2.contains(player.getStoredId()))
                players_list2.remove(player.getStoredId());
            if (live_list1.contains(player.getStoredId()))
                live_list1.remove(player.getStoredId());
            if (live_list2.contains(player.getStoredId()))
                live_list2.remove(player.getStoredId());
            if (boxes.containsKey(player.getStoredId()))
                boxes.remove(player.getStoredId());
            return false;
        }

        if (player.getLevel() < _minLevel || player.getLevel() > _maxLevel) {
            show(new CustomMessage("scripts.events.DeathMatch.CancelledLevel", player), player);
            return false;
        }

        if (player.isMounted()) {
            show(new CustomMessage("scripts.events.DeathMatch.Cancelled", player), player);
            return false;
        }

        if (player.isCursedWeaponEquipped()) {
            show(new CustomMessage("scripts.events.CtF.Cancelled", player), player);
            return false;
        }

        if (player.isInDuel()) {
            show(new CustomMessage("scripts.events.DeathMatch.CancelledDuel", player), player);
            return false;
        }

        if (player.getTeam() != TeamType.NONE || player.isInFightClub()) {
            show(new CustomMessage("scripts.events.DeathMatch.CancelledOtherEvent", player), player);
            return false;
        }

        if (player.getOlympiadGame() != null || first && Olympiad.isRegistered(player)) {
            show(new CustomMessage("scripts.events.DeathMatch.CancelledOlympiad", player), player);
            return false;
        }

        if (player.isInParty() && player.getParty().isInDimensionalRift()) {
            show(new CustomMessage("scripts.events.DeathMatch.CancelledOtherEvent", player), player);
            return false;
        }

        if (player.isInObserverMode()) {
            show(new CustomMessage("scripts.event.DeathMatch.CancelledObserver", player), player);
            return false;
        }

        if (player.isTeleporting()) {
            show(new CustomMessage("scripts.events.DeathMatch.CancelledTeleport", player), player);
            return false;
        }
        return true;
    }

    public static void prepare() {
        ReflectionUtils.getDoor(24190002).closeMe();
        ReflectionUtils.getDoor(24190003).closeMe();

        cleanPlayers();
        clearArena();
        executeTask("events.DeathMatch.DeathMatch", "ressurectPlayers", new Object[0], 1000);
        executeTask("events.DeathMatch.DeathMatch", "healPlayers", new Object[0], 2000);
        executeTask("events.DeathMatch.DeathMatch", "teleportPlayersToColiseum", new Object[0], 3000);
        executeTask("events.DeathMatch.DeathMatch", "paralyzePlayers", new Object[0], 4000);
        executeTask("events.DeathMatch.DeathMatch", "buffPlayers", new Object[0], 5000);
        executeTask("events.DeathMatch.DeathMatch", "go", new Object[0], 60000);

        sayToAll("scripts.events.DeathMatch.AnnounceFinalCountdown", null);
    }

    public static void go() {
        _status = 2;
        upParalyzePlayers();
        checkLive();
        clearArena();
        sayToAll("scripts.events.DeathMatch.AnnounceFight", null);

        _endTask = executeTask("events.DeathMatch.DeathMatch", "endBattle", new Object[0], 300000);
    }

    public static void endBattle() {
        _status = 0;
        removeAura();
        ReflectionUtils.getDoor(24190002).openMe();
        ReflectionUtils.getDoor(24190003).openMe();
        boxes.clear();
        if (live_list1.isEmpty()) {
            sayToAll("scripts.events.DeathMatch.AnnounceFinishedBlueWins", null);
            giveItemsToWinner(false, true, 1);
        } else if (live_list2.isEmpty()) {
            sayToAll("scripts.events.DeathMatch.AnnounceFinishedRedWins", null);
            giveItemsToWinner(true, false, 1);
        } else if (live_list1.size() < live_list2.size()) {
            sayToAll("scripts.events.DeathMatch.AnnounceFinishedBlueWins", null);
            giveItemsToWinner(false, true, 1);
        } else if (live_list1.size() > live_list2.size()) {
            sayToAll("scripts.events.DeathMatch.AnnounceFinishedRedWins", null);
            giveItemsToWinner(true, false, 1);
        } else if (live_list1.size() == live_list2.size()) {
            sayToAll("scripts.events.DeathMatch.AnnounceFinishedDraw", null);
            giveItemsToWinner(true, true, 0.5);
        }

        sayToAll("scripts.events.DeathMatch.AnnounceEnd", null);
        executeTask("events.DeathMatch.DeathMatch", "end", new Object[0], 30000);
        _isRegistrationActive = false;
        if (_endTask != null) {
            _endTask.cancel(false);
            _endTask = null;
        }
    }

    public static void end() {
        executeTask("events.DeathMatch.DeathMatch", "ressurectPlayers", new Object[0], 1000);
        executeTask("events.DeathMatch.DeathMatch", "healPlayers", new Object[0], 2000);
        executeTask("events.DeathMatch.DeathMatch", "teleportPlayers", new Object[0], 3000);
        executeTask("events.DeathMatch.DeathMatch", "autoContinue", new Object[0], 10000);
    }

    public void autoContinue() {
        live_list1.clear();
        live_list2.clear();
        players_list1.clear();
        players_list2.clear();

        if (_autoContinue > 0) {
            if (_autoContinue >= 6) {
                _autoContinue = 0;
                return;
            }
            start(new String[]{"" + (_autoContinue + 1), "" + (_autoContinue + 1)});
        } else
            scheduleEventStart();
    }

    public static void giveItemsToWinner(boolean team1, boolean team2, double rate) {
        if (team1)
            for (Player player : getPlayers(players_list1))
                for (int i = 0; i < rewards.length; i++)
                    addItem(player, rewards[i][0], Math.round((Config.EVENT_DeathMatchrate ? player.getLevel() : 1) * rewards[i][1] * rate));
        if (team2)
            for (Player player : getPlayers(players_list2))
                for (int i = 0; i < rewards.length; i++)
                    addItem(player, rewards[i][0], Math.round((Config.EVENT_DeathMatchrate ? player.getLevel() : 1) * rewards[i][1] * rate));
    }

    public static void teleportPlayersToColiseum() {
        for (Player player : getPlayers(players_list1)) {
            unRide(player);

            if (!Config.EVENT_DeathMatchAllowSummons)
                unSummonPet(player, true);

            DuelEvent duel = player.getEvent(DuelEvent.class);
            if (duel != null)
                duel.abortDuel(player);

            playerRestoreCoord.put(player.getStoredId(), new Location(player.getX(), player.getY(), player.getZ()));

            player.teleToLocation(Territory.getRandomLoc(team1spawn), ReflectionManager.DEFAULT.getGeoIndex());
            player.setIsInDeathMatch(true);

            if (!Config.EVENT_DeathMatchAllowBuffs) {
                player.getEffectList().stopAllEffects();
                if (player.getPet() != null)
                    player.getPet().getEffectList().stopAllEffects();
            }
        }

        for (Player player : getPlayers(players_list2)) {
            unRide(player);

            if (!Config.EVENT_DeathMatchAllowSummons)
                unSummonPet(player, true);

            playerRestoreCoord.put(player.getStoredId(), new Location(player.getX(), player.getY(), player.getZ()));

            player.teleToLocation(Territory.getRandomLoc(team2spawn), ReflectionManager.DEFAULT.getGeoIndex());
            player.setIsInDeathMatch(true);

            if (!Config.EVENT_DeathMatchAllowBuffs) {
                player.getEffectList().stopAllEffects();
                if (player.getSummonList().getPet() != null)
                    player.getSummonList().getPet().getEffectList().stopAllEffects();
            }
        }
    }

    public static void teleportPlayers() {
        for (Player player : getPlayers(players_list1)) {
            if (player == null || !playerRestoreCoord.containsKey(player.getStoredId()))
                continue;
            player.teleToLocation(playerRestoreCoord.get(player.getStoredId()), ReflectionManager.DEFAULT);
        }

        for (Player player : getPlayers(players_list2)) {
            if (player == null || !playerRestoreCoord.containsKey(player.getStoredId()))
                continue;
            player.teleToLocation(playerRestoreCoord.get(player.getStoredId()), ReflectionManager.DEFAULT);
        }

        playerRestoreCoord.clear();
    }

    public static void paralyzePlayers() {
        for (Player player : getPlayers(players_list1)) {
            if (player == null)
                continue;

            if (!player.isRooted()) {
                player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
                player.startRooted();
                player.startAbnormalEffect(AbnormalEffect.ROOT);
            }

            if (player.getSummonList().getPet() != null && !player.getSummonList().getPet().isRooted()) {
                player.getSummonList().getPet().startRooted();
                player.getSummonList().getPet().startAbnormalEffect(AbnormalEffect.ROOT);
            }
        }
        for (Player player : getPlayers(players_list2)) {

            if (!player.isRooted()) {
                player.getEffectList().stopEffect(Skill.SKILL_MYSTIC_IMMUNITY);
                player.startRooted();
                player.startAbnormalEffect(AbnormalEffect.ROOT);
            }

            if (player.getSummonList().getPet() != null && !player.getSummonList().getPet().isRooted()) {
                player.getSummonList().getPet().startRooted();
                player.getSummonList().getPet().startAbnormalEffect(AbnormalEffect.ROOT);
            }
        }
    }

    public static void upParalyzePlayers() {
        for (Player player : getPlayers(players_list1)) {
            if (player.isRooted()) {
                player.stopRooted();
                player.stopAbnormalEffect(AbnormalEffect.ROOT);
            }

            if (player.getSummonList().getPet() != null && player.getSummonList().getPet().isRooted()) {
                player.getSummonList().getPet().stopRooted();
                player.getSummonList().getPet().stopAbnormalEffect(AbnormalEffect.ROOT);
            }

        }
        for (Player player : getPlayers(players_list2)) {
            if (player.isRooted()) {
                player.stopRooted();
                player.stopAbnormalEffect(AbnormalEffect.ROOT);
            }
            if (player.getSummonList().getPet() != null && player.getSummonList().getPet().isRooted()) {
                player.getSummonList().getPet().stopRooted();
                player.getSummonList().getPet().stopAbnormalEffect(AbnormalEffect.ROOT);
            }
        }
    }

    public static void ressurectPlayers() {
        for (Player player : getPlayers(players_list1))
            if (player.isDead()) {
                player.restoreExp();
                player.setCurrentCp(player.getMaxCp());
                player.setCurrentHp(player.getMaxHp(), true);
                player.setCurrentMp(player.getMaxMp());
                player.broadcastPacket(new Revive(player));
            }
        for (Player player : getPlayers(players_list2))
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
    }

    public static void cleanPlayers() {
        for (Player player : getPlayers(players_list1))
            if (!checkPlayer(player, false))
                removePlayer(player);
        for (Player player : getPlayers(players_list2))
            if (!checkPlayer(player, false))
                removePlayer(player);
    }

    public static void checkLive() {
        List<Long> new_live_list1 = new CopyOnWriteArrayList<Long>();
        List<Long> new_live_list2 = new CopyOnWriteArrayList<Long>();

        for (Long storeId : live_list1) {
            Player player = GameObjectsStorage.getAsPlayer(storeId);
            if (player != null)
                new_live_list1.add(storeId);
        }

        for (Long storeId : live_list2) {
            Player player = GameObjectsStorage.getAsPlayer(storeId);
            if (player != null)
                new_live_list2.add(storeId);
        }

        live_list1 = new_live_list1;
        live_list2 = new_live_list2;

        for (Player player : getPlayers(live_list1))
            if (player.isInZone(_zone) && !player.isDead() && !player.isLogoutStarted())
                player.setTeam(TeamType.RED);
            else
                loosePlayer(player);

        for (Player player : getPlayers(live_list2))
            if (player.isInZone(_zone) && !player.isDead() && !player.isLogoutStarted())
                player.setTeam(TeamType.BLUE);
            else
                loosePlayer(player);

        if (live_list1.size() < 1 || live_list2.size() < 1)
            endBattle();
    }

    public static void removeAura() {
        for (Player player : getPlayers(live_list1)) {
            player.setTeam(TeamType.NONE);
            if (player.getSummonList().getPet() != null)
                player.getSummonList().getPet().setTeam(TeamType.NONE);
            player.setIsInDeathMatch(false);
        }
        for (Player player : getPlayers(live_list2)) {
            player.setTeam(TeamType.NONE);
            if (player.getSummonList().getPet() != null)
                player.getSummonList().getPet().setTeam(TeamType.NONE);
            player.setIsInDeathMatch(false);
        }
    }

    public static void clearArena() {
        for (GameObject obj : _zone.getObjects())
            if (obj != null) {
                Player player = obj.getPlayer();
                if (player != null && !live_list1.contains(player.getStoredId()) && !live_list2.contains(player.getStoredId()))
                    player.teleToLocation(147451, 46728, -3410, ReflectionManager.DEFAULT);
            }
    }

    @Override
    public void onDeath(Creature self, Creature killer) {
        if (_status > 1 && self.isPlayer() && self.getTeam() != TeamType.NONE && (live_list1.contains(self.getStoredId()) || live_list2.contains(self.getStoredId()))) {
            loosePlayer((Player) self);
            checkLive();
            self.getPlayer().setIsInDeathMatch(false);
        }

    }

    @Override
    public void onTeleport(Player player, int x, int y, int z, Reflection reflection) {
        if (_zone.checkIfInZone(x, y, z, reflection))
            return;

        if (_status > 1 && player != null && player.getTeam() != TeamType.NONE && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId()))) {
            removePlayer(player);
            checkLive();
        }
    }

    @Override
    public void onPlayerExit(Player player) {
        if (player.getTeam() == TeamType.NONE)
            return;

        if (_status == 0 && _isRegistrationActive && player.getTeam() != TeamType.NONE && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId()))) {
            removePlayer(player);
            return;
        }

        if (_status == 1 && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId()))) {
            player.teleToLocation(playerRestoreCoord.get(player.getStoredId()), ReflectionManager.DEFAULT);
            removePlayer(player);
            return;
        }

        if (_status > 1 && player != null && player.getTeam() != TeamType.NONE && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId()))) {
            removePlayer(player);
            checkLive();
        }
    }

    private static class ZoneListener implements OnZoneEnterLeaveListener {
        @Override
        public void onZoneEnter(Zone zone, Creature cha) {
            if (cha == null)
                return;
            Player player = cha.getPlayer();
            if (_status > 0 && player != null && !live_list1.contains(player.getStoredId()) && !live_list2.contains(player.getStoredId()))
                player.teleToLocation(147451, 46728, -3410, ReflectionManager.DEFAULT);
        }

        @Override
        public void onZoneLeave(Zone zone, Creature cha) {
            if (cha == null)
                return;
            Player player = cha.getPlayer();
            if (_status > 1 && player != null && player.getTeam() != TeamType.NONE && (live_list1.contains(player.getStoredId()) || live_list2.contains(player.getStoredId()))) {
                double angle = PositionUtils.convertHeadingToDegree(cha.getHeading()); // СѓРіРѕР» РІ РіСЂР°РґСѓСЃР°С…
                double radian = Math.toRadians(angle - 90); // СѓРіРѕР» РІ
                // СЂР°РґРёР°РЅР°С…
                int x = (int) (cha.getX() + 250 * Math.sin(radian));
                int y = (int) (cha.getY() - 250 * Math.cos(radian));
                int z = cha.getZ();
                player.teleToLocation(x, y, z, ReflectionManager.DEFAULT.getGeoIndex());
            }
        }
    }

    private static void loosePlayer(Player player) {
        if (player != null) {
            live_list1.remove(player.getStoredId());
            live_list2.remove(player.getStoredId());
            player.setTeam(TeamType.NONE);
            show(new CustomMessage("scripts.events.DeathMatch.YouLose", player), player);
        }
    }

    private static void removePlayer(Player player) {
        if (player != null) {
            live_list1.remove(player.getStoredId());
            live_list2.remove(player.getStoredId());
            players_list1.remove(player.getStoredId());
            players_list2.remove(player.getStoredId());
            playerRestoreCoord.remove(player.getStoredId());
            player.setIsInDeathMatch(false);

            if (!Config.EVENT_DeathMatchAllowMultiReg)
                boxes.remove(player.getStoredId());

            player.setTeam(TeamType.NONE);
        }
    }

    private static List<Player> getPlayers(List<Long> list) {
        List<Player> result = new ArrayList<Player>();
        for (Long storeId : list) {
            Player player = GameObjectsStorage.getAsPlayer(storeId);
            if (player != null)
                result.add(player);
        }
        return result;
    }

    public static void buffPlayers() {

        for (Player player : getPlayers(players_list1))
            if (player.isMageClass())
                mageBuff(player);
            else
                fighterBuff(player);

        for (Player player : getPlayers(players_list2))
            if (player.isMageClass())
                mageBuff(player);
            else
                fighterBuff(player);
    }

    public void scheduleEventStart() {
        try {
            Calendar currentTime = Calendar.getInstance();
            Calendar nextStartTime = null;
            Calendar testStartTime = null;

            for (String timeOfDay : Config.EVENT_DeathMatchStartTime) {
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
            _log.warn("DeathMatch: Error figuring out a start time. Check DeathMatchEventInterval in config file.");
        }
    }

    public static void mageBuff(Player player) {
        for (int i = 0; i < mage_buffs.length; i++) {
            buff = SkillTable.getInstance().getInfo(mage_buffs[i][0], mage_buffs[i][1]);
            buff.getEffects(player, player, false, false);
        }
    }

    public static void fighterBuff(Player player) {
        for (int i = 0; i < fighter_buffs.length; i++) {
            buff = SkillTable.getInstance().getInfo(fighter_buffs[i][0], fighter_buffs[i][1]);
            buff.getEffects(player, player, false, false);
        }
    }

    private static boolean checkDualBox(Player player) {
        if (!Config.EVENT_DeathMatchAllowMultiReg)
            if ("IP".equalsIgnoreCase(Config.EVENT_DeathMatchCheckWindowMethod)) {
                if (boxes.containsValue(player.getIP())) {
                    show(new CustomMessage("scripts.events.DeathMatch.CancelledBox", player), player);
                    return false;
                }
            } else if ("HWid".equalsIgnoreCase(Config.EVENT_DeathMatchCheckWindowMethod))
                if (boxes.containsValue(player.getNetConnection().getHWID())) {
                    show(new CustomMessage("scripts.events.DeathMatch.CancelledBox", player), player);
                    return false;
                }
        return true;
    }

    public class StartTask extends RunnableImpl {

        @Override
        public void runImpl() {
            if (!_active)
                return;

            if (isPvPEventStarted()) {
                _log.info("DeathMatch not started: another event is already running");
                return;
            }

            for (Residence c : ResidenceHolder.getInstance().getResidenceList(Castle.class))
                if (c.getSiegeEvent() != null && c.getSiegeEvent().isInProgress()) {
                    _log.debug("DeathMatch not started: CastleSiege in progress");
                    return;
                }

            if (Config.EVENT_DeathMatchCategories)
                start(new String[]{"1", "1"});
            else
                start(new String[]{"-1", "-1"});
        }
    }
}
