package events.TvTArena;

import l2p.commons.dbutils.DbUtils;
import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.Announcements;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.data.htm.HtmCache;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.instancemanager.ReflectionManager;
import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.*;
import l2p.gameserver.model.base.TeamType;
import l2p.gameserver.model.entity.Hero;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage;
import l2p.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import l2p.gameserver.network.serverpackets.Revive;
import l2p.gameserver.network.serverpackets.SkillList;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.skills.AbnormalEffect;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.PositionUtils;
import l2p.gameserver.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public abstract class TVTArenaTemplate extends Functions {
    private static final Logger _log = LoggerFactory.getLogger(TVTArenaTemplate.class);

    private static final int[] KandinsDoorsToOpen = new int[]{17160033, 17160034, 17160038, 17160036, 17160035, 17160037, 17160040, 17160039};
    private static final int[] KoonDoorsToOpen = new int[]{17160029, 17160030, 17160028, 17160027, 17160026, 17160025, 17160032, 17160031};
    private static final int[] KuramDoorsToOpen = new int[]{17160023, 17160024, 17160018, 17160017, 17160020, 17160019, 17160022, 17160021};
    private static final int[] TarionDoorsToOpen = new int[]{17160011, 17160012, 17160010, 17160009, 17160016, 17160015, 17160014, 17160013};
    private static final int[] RioDoorsToOpen = new int[]{17160004, 17160003, 17160002, 17160001, 17160008, 17160007, 17160006, 17160005};

    protected int _managerId;
    protected String _className;

    protected Long _creatorId;
    protected NpcInstance _manager;
    protected int _status = 0;
    protected int _CharacterFound = 0;

    protected int _team1count = Config.EVENT_TVT_ARENA_TEAM_COUNT;
    protected int _team2count = Config.EVENT_TVT_ARENA_TEAM_COUNT;
    int _timeToStart;
    protected boolean _timeOutTask;

    int _team1min = 1;
    int _team1max = 1;
    int _team2min = 1;
    int _team2max = 1;
    int _nArenaId = 1;

    protected List<Location> _team1points;
    protected List<Location> _team2points;

    protected List<Long> _team1list;
    protected List<Long> _team2list;
    protected List<Long> _team1live;
    protected List<Long> _team2live;

    protected Zone _zone;
    protected ZoneListener _zoneListener;

    protected abstract void onLoad();

    protected abstract void onReload();

    public void template_stop() {
        if (_status <= 0)
            return;

        for (Player player : getPlayers(_team1list)) {
            sayToPlayers("Бой прерван по техническим причинам", true, player, 0);
            saveTeamPoints(player, Config.EVENT_TVT_ARENA_TECH_REASON, _nArenaId);
        }
        for (Player player : getPlayers(_team2list)) {
            sayToPlayers("Бой прерван по техническим причинам", true, player, 0);
            saveTeamPoints(player, Config.EVENT_TVT_ARENA_TECH_REASON, _nArenaId);
        }

        unParalyzeTeams();
        ressurectPlayers();
        healPlayers();
        removeBuff();
        teleportPlayersToSavedCoords();
        clearTeams();
        _status = 0;
        _timeOutTask = false;
    }

    public void template_create1(Player player) {
        if (_status > 0) {
            show("scripts/events/TvTArena/whait.htm", player);
            return;
        }

        if (player.getTeam() != TeamType.NONE) {
            show("scripts/events/TvTArena/already.htm", player);
            return;
        }
        show("scripts/events/TvTArena/" + _managerId + "-1.htm", player);
    }

    public void template_register(Player player) {
        String html = HtmCache.getInstance().getNotNull("scripts/events/TvTArena/" + _managerId + "-3.htm", player);
        String playersBlueList = "";
        String playersRedList = "";

        if (_status == 0) {
            show("scripts/events/TvTArena/notyet.htm", player);
            return;
        }

        if (_status > 1) {
            show("scripts/events/TvTArena/whait.htm", player);
            return;
        }

        if (player.getTeam() != TeamType.NONE) {
            show("scripts/events/TvTArena/already.htm", player);
            return;
        }

        //TODO крастоту в HTML
        for (Player plr : getPlayers(_team1list))
            playersBlueList += plr.getName() + "<br1>";

        for (Player plr : getPlayers(_team2list))
            playersRedList += plr.getName() + "<br1>";

        html = html.replace("%blue_team%", playersBlueList);
        html = html.replace("%red_team%", playersRedList);
        show(html, player);
    }

    public void template_check1(Player player, NpcInstance manager) {
        if (_status > 0) {
            show("scripts/events/TvTArena/whait.htm", player);
            return;
        }

        if (manager == null || !manager.isNpc()) {
            show("scripts/events/TvTArena/debil.htm", player);
            return;
        }

        _manager = manager;

        switch (_manager.getNpcId()) {
            case 36612:
                _team1min = 55;
                _team1max = 60;
                _team2min = 55;
                _team2max = 60;
                _nArenaId = 1;
                break;
            case 36613:
                _team1min = 61;
                _team1max = 69;
                _team2min = 61;
                _team2max = 69;
                _nArenaId = 2;
                break;
            case 36614:
                _team1min = 70;
                _team1max = 75;
                _team2min = 70;
                _team2max = 75;
                _nArenaId = 3;
                break;
            case 36615:
                _team1min = 76;
                _team1max = 80;
                _team2min = 76;
                _team2max = 80;
                _nArenaId = 4;
                break;
            case 36616:
                _team1min = 81;
                _team1max = 85;
                _team2min = 81;
                _team2max = 85;
                _nArenaId = 5;
                break;
            case 36617:
                _team1min = 86;
                _team1max = 91;
                _team2min = 86;
                _team2max = 91;
                _nArenaId = 6;
                break;
            case 36618:
                _team1min = 92;
                _team1max = 96;
                _team2min = 92;
                _team2max = 96;
                _nArenaId = 7;
                break;
            case 36619:
                _team1min = 97;
                _team1max = 99;
                _team2min = 97;
                _team2max = 99;
                _nArenaId = 8;
                break;
        }

        if (player.getLevel() < _team1min || player.getLevel() > _team1max)
            show("scripts/events/TvTArena/wrong_level.htm", player);
        else {
            _creatorId = player.getStoredId();
            player.setTeam(TeamType.BLUE);
            _status = 1;
            _team1list.clear();
            _team2list.clear();
            _team1live.clear();
            _team2live.clear();
            _team1list.add(player.getStoredId());
            player.setIsInTVTArena(true);
            String[] param = {player.getName(), String.valueOf(_nArenaId), String.valueOf(Config.EVENT_TVT_ARENA_TIME_TO_START)};
            sayToAll("scripts.events.TVTArena.AnnounceStart", param);
            executeTask("events.TVTArena." + _className, "announce", new Object[0], 60000);
            _timeToStart = Config.EVENT_TVT_ARENA_TIME_TO_START;
        }
    }

    public void template_register_check_blue(Player player) {
        if (_status == 0) {
            show("scripts/events/TvTArena/notyet.htm", player);
            return;
        }

        if (_status > 1) {
            show("scripts/events/TvTArena/whait.htm", player);
            return;
        }

        if (_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId())) {
            show("scripts/events/TvTArena/already.htm", player);
            return;
        }

        if (player.getTeam() != TeamType.NONE) {
            show("scripts/events/TvTArena/already.htm", player);
            return;
        }

        int size1 = _team1list.size();

        if (size1 < Config.EVENT_TVT_ARENA_TEAM_COUNT) {
            String t = null;
            if (tryRegister(1, player) != null)
                if ((t = tryRegister(1, player)) != null)
                    show(t, player);
        } else
            show("scripts/events/TvTArena/blue_team_list.htm", player);
    }

    public void template_register_check_red(Player player) {
        if (_status == 0) {
            show("scripts/events/TvTArena/notyet.htm", player);
            return;
        }

        if (_status > 1) {
            show("scripts/events/TvTArena/whait.htm", player);
            return;
        }

        if (_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId())) {
            show("scripts/events/TvTArena/already.htm", player);
            return;
        }

        if (player.getTeam() != TeamType.NONE) {
            show("scripts/events/TvTArena/already.htm", player);
            return;
        }

        int size2 = _team2list.size();

        if (size2 < Config.EVENT_TVT_ARENA_TEAM_COUNT) {
            String t = null;
            if (tryRegister(2, player) != null)
                if ((t = tryRegister(2, player)) != null)
                    show(t, player);
        } else
            show("scripts/events/TvTArena/red_team_list.htm", player);
    }

    private String tryRegister(int team, Player player) {
        if (team == 1) {
            if (player.getLevel() < _team1min || player.getLevel() > _team1max)
                return "Вы не подходите по уровню";
            if (_team1list.size() >= _team1count)
                return "Команда 1 переполнена";
            doRegister(1, player);
            return null;
        } else {
            if (player.getLevel() < _team2min || player.getLevel() > _team2max)
                return "Вы не подходите по уровню";
            if (_team2list.size() >= _team2count)
                return "Команда 2 переполнена";
            doRegister(2, player);
        }
        return null;
    }

    private void doRegister(int team, Player player) {
        if (team == 1) {
            _team1list.add(player.getStoredId());
            player.setTeam(TeamType.BLUE);
            for (Player plr : getPlayers(_team1list)) {
                sayToPlayers(plr.getName() + " зарегистрировался за 1 команду", true, plr, 1);
                saveTeamPoints(plr, -1, _nArenaId);
            }
            for (Player plr : getPlayers(_team2list)) {
                sayToPlayers(plr.getName() + " зарегистрировался за 1 команду", true, plr, 1);
                saveTeamPoints(plr, -1, _nArenaId);
            }
        } else {
            _team2list.add(player.getStoredId());
            player.setTeam(TeamType.RED);
            for (Player plr : getPlayers(_team2list)) {
                sayToPlayers(plr.getName() + " зарегистрировался за 2 команду", true, plr, 1);
                saveTeamPoints(plr, -1, _nArenaId);
            }
            for (Player plr : getPlayers(_team1list)) {
                sayToPlayers(plr.getName() + " зарегистрировался за 2 команду", true, plr, 1);
                saveTeamPoints(plr, -1, _nArenaId);
            }
        }

        if (_team1list.size() >= _team1count && _team2list.size() >= _team2count) {
            for (Player plr : getPlayers(_team1list))
                sayToPlayers("Команды готовы, старт через 1 минуту.", true, plr, 1);
            for (Player plr : getPlayers(_team2list))
                sayToPlayers("Команды готовы, старт через 1 минуту.", true, plr, 1);
            _timeToStart = 1;
        }
    }

    public void template_announce() {
        Player creator = GameObjectsStorage.getAsPlayer(_creatorId);

        if (_status != 1 || creator == null)
            return;

        _log.info("_timeToStart = " + _timeToStart);

        if (_timeToStart > 1) {
            _timeToStart--;
            String[] param = {creator.getName(), String.valueOf(_nArenaId), String.valueOf(Config.EVENT_TVT_ARENA_TIME_TO_START)};
            sayToAll("scripts.events.TVTArena.AnnounceStart", param);
            executeTask("events.TVTArena." + _className, "announce", new Object[0], 60000);
        } else if (_team2list.size() >= Config.EVENT_TVT_ARENA_TEAM_COUNT_MIN && _team1list.size() >= Config.EVENT_TVT_ARENA_TEAM_COUNT_MIN) {
            for (Player plr : getPlayers(_team1list))
                sayToPlayers("Подготовка к бою", true, plr, 1);
            for (Player plr : getPlayers(_team2list))
                sayToPlayers("Подготовка к бою", true, plr, 1);
            executeTask("events.TVTArena." + _className, "prepare", new Object[0], 5000);
        } else {
            sayToAll("scripts.events.TVTArena.AnnounceCanceled", null);
            for (Player plr : getPlayers(_team1list)) {
                sayToPlayers("Бой не состоялся, нет противников", true, plr, 1);
                saveTeamPoints(plr, Config.EVENT_TVT_ARENA_NO_PLAYERS, _nArenaId);
            }
            for (Player plr : getPlayers(_team2list)) {
                sayToPlayers("Бой не состоялся, нет противников", true, plr, 1);
                saveTeamPoints(plr, Config.EVENT_TVT_ARENA_NO_PLAYERS, _nArenaId);
            }
            _status = 0;
            clearTeams();
        }
    }

    public void template_prepare() {
        if (_status != 1)
            return;

        _status = 2;
        for (Player player : getPlayers(_team1list))
            if (!player.isDead())
                _team1live.add(player.getStoredId());
        for (Player player : getPlayers(_team2list))
            if (!player.isDead())
                _team2live.add(player.getStoredId());
        if (!checkTeams())
            return;
        saveBackCoords();
        clearArena();
        ressurectPlayers();
        removeBuff();
        healPlayers();
        paralyzeTeams();
        teleportTeamsToArena();

        for (Player player : getPlayers(_team1list))
            sayToPlayers("Бой начнется через 30 секунд", true, player, 0);
        for (Player player : getPlayers(_team2list))
            sayToPlayers("Бой начнется через 30 секунд", true, player, 0);

        executeTask("events.TVTArena." + _className, "start", new Object[0], 30000);
    }

    public void template_start() {
        if (_status != 2)
            return;

        if (!checkTeams())
            return;

        for (Player plr : getPlayers(_team1list))
            sayToPlayers("Go!!!", true, plr, 0);
        for (Player plr : getPlayers(_team2list))
            sayToPlayers("Go!!!", true, plr, 0);

        unParalyzeTeams();
        _status = 3;
        executeTask("events.TVTArena." + _className, "timeOut", new Object[0], Config.EVENT_TVT_ARENA_FIGHT_TIME * 60000);
        _timeOutTask = true;
    }

    public void clearArena() {
        for (GameObject obj : _zone.getObjects())
            if (obj != null && obj.isPlayable())
                ((Playable) obj).teleToLocation(_zone.getSpawn());
    }

    public boolean checkTeams() {
        if (_team1live.isEmpty()) {
            teamHasLost(1);
            return false;
        } else if (_team2live.isEmpty()) {
            teamHasLost(2);
            return false;
        }
        return true;
    }

    public void saveBackCoords() {
        for (Player player : getPlayers(_team1list))
            player.setVar("TVTArena_backCoords", player.getX() + " " + player.getY() + " " + player.getZ() + " " + player.getReflectionId(), -1);
        for (Player player : getPlayers(_team2list))
            player.setVar("TVTArena_backCoords", player.getX() + " " + player.getY() + " " + player.getZ() + " " + player.getReflectionId(), -1);
    }

    public void teleportPlayersToSavedCoords() {
        for (Player player : getPlayers(_team1list))
            try {
                String var = player.getVar("TVTArena_backCoords");
                if (var == null || var.equals(""))
                    continue;
                String[] coords = var.split(" ");
                if (coords.length != 4)
                    continue;
                player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]), Integer.parseInt(coords[3]));
                player.unsetVar("TVTArena_backCoords");
            } catch (Exception e) {
                e.printStackTrace();
            }
        for (Player player : getPlayers(_team2list))
            try {
                String var = player.getVar("TVTArena_backCoords");
                if (var == null || var.equals(""))
                    continue;
                String[] coords = var.split(" ");
                if (coords.length != 4)
                    continue;
                player.teleToLocation(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]), Integer.parseInt(coords[3]));
                player.unsetVar("TVTArena_backCoords");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void healPlayers() {
        for (Player player : getPlayers(_team1list)) {
            player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
            player.setCurrentCp(player.getMaxCp());
        }
        for (Player player : getPlayers(_team2list)) {
            player.setCurrentHpMp(player.getMaxHp(), player.getMaxMp());
            player.setCurrentCp(player.getMaxCp());
        }
    }

    public void ressurectPlayers() {
        for (Player player : getPlayers(_team1list))
            if (player.isDead()) {
                player.restoreExp();
                player.setCurrentCp(player.getMaxCp());
                player.setCurrentHp(player.getMaxHp(), true);
                player.setCurrentMp(player.getMaxMp());
                player.broadcastPacket(new Revive(player));
            }
        for (Player player : getPlayers(_team2list))
            if (player.isDead()) {
                player.restoreExp();
                player.setCurrentCp(player.getMaxCp());
                player.setCurrentHp(player.getMaxHp(), true);
                player.setCurrentMp(player.getMaxMp());
                player.broadcastPacket(new Revive(player));
            }
    }

    public void removeBuff() {
        for (Player player : getPlayers(_team1list))
            if (player != null)
                try {
                    if (player.isCastingNow())
                        player.abortCast(true, true);

                    if (!Config.EVENT_TVT_ARENA_ALLOW_CLAN_SKILL)
                        if (player.getClan() != null)
                            for (Skill skill : player.getClan().getAllSkills())
                                player.removeSkill(skill, false);

                    if (!Config.EVENT_TVT_ARENA_ALLOW_HERO_SKILL)
                        if (player.isHero())
                            Hero.removeSkills(player);

                    if (!Config.EVENT_TVT_ARENA_ALLOW_BUFFS) {
                        player.getEffectList().stopAllEffects();

                        if (player.getSummonList().getPet() != null) {
                            Summon summon = player.getSummonList().getPet();
                            summon.getEffectList().stopAllEffects();
                            if (summon.isPet())
                                summon.unSummon();
                        }

                        if (player.getAgathionId() > 0)
                            player.setAgathion(0);
                    }

                    player.sendPacket(new SkillList(player));
                } catch (Exception e) {
                    e.printStackTrace();
                }

        for (Player player : getPlayers(_team2list))
            if (player != null)
                try {
                    if (player.isCastingNow())
                        player.abortCast(true, true);

                    if (!Config.EVENT_TVT_ARENA_ALLOW_CLAN_SKILL)
                        if (player.getClan() != null)
                            for (Skill skill : player.getClan().getAllSkills())
                                player.removeSkill(skill, false);

                    if (!Config.EVENT_TVT_ARENA_ALLOW_HERO_SKILL)
                        if (player.isHero())
                            Hero.removeSkills(player);

                    if (!Config.EVENT_TVT_ARENA_ALLOW_BUFFS) {
                        player.getEffectList().stopAllEffects();

                        if (player.getSummonList().getPet() != null) {
                            Summon summon = player.getSummonList().getPet();
                            summon.getEffectList().stopAllEffects();
                            if (summon.isPet())
                                summon.unSummon();
                        }

                        if (player.getAgathionId() > 0)
                            player.setAgathion(0);
                    }

                    player.sendPacket(new SkillList(player));
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }

    public void paralyzeTeams() {
        for (Player player : getPlayers(_team1list)) {
            if (player == null)
                continue;

            player.setIsInTVTArena(true);

            if (!player.isRooted()) {
                player.startRooted();
                player.startAbnormalEffect(AbnormalEffect.ROOT);
            }

            if (player.getSummonList().getPet() != null && !player.getSummonList().getPet().isRooted()) {
                player.getSummonList().getPet().startRooted();
                player.getSummonList().getPet().startAbnormalEffect(AbnormalEffect.ROOT);
            }
        }
        for (Player player : getPlayers(_team2list)) {
            if (player == null)
                continue;

            player.setIsInTVTArena(true);

            if (!player.isRooted()) {
                player.startRooted();
                player.startAbnormalEffect(AbnormalEffect.ROOT);
            }

            if (player.getSummonList().getPet() != null && !player.getSummonList().getPet().isRooted()) {
                player.getSummonList().getPet().startRooted();
                player.getSummonList().getPet().startAbnormalEffect(AbnormalEffect.ROOT);
            }
        }
    }

    public void unParalyzeTeams() {
        for (Player player : getPlayers(_team1list)) {
            if (player.isRooted()) {
                player.stopRooted();
                player.stopAbnormalEffect(AbnormalEffect.ROOT);
            }

            if (player.getSummonList().getPet() != null && player.getSummonList().getPet().isRooted()) {
                player.getSummonList().getPet().stopRooted();
                player.getSummonList().getPet().stopAbnormalEffect(AbnormalEffect.ROOT);
            }
        }
        for (Player player : getPlayers(_team2list)) {
            if (player.isRooted()) {
                player.stopRooted();
                player.stopAbnormalEffect(AbnormalEffect.ROOT);
            }

            if (player.getSummonList().getPet() != null && player.getSummonList().getPet().isRooted()) {
                player.getSummonList().getPet().stopRooted();
                player.getSummonList().getPet().stopAbnormalEffect(AbnormalEffect.ROOT);
            }
        }
        switch (_manager.getNpcId()) {
            case 36612:
                for (int doorId : KandinsDoorsToOpen)
                    ReflectionUtils.getDoor(doorId).openMe();
                break;
            case 36613:
                for (int doorId : KoonDoorsToOpen)
                    ReflectionUtils.getDoor(doorId).openMe();
                break;
            case 36614:
                for (int doorId : KuramDoorsToOpen)
                    ReflectionUtils.getDoor(doorId).openMe();
                break;
            case 36615:
                for (int doorId : TarionDoorsToOpen)
                    ReflectionUtils.getDoor(doorId).openMe();
                break;
            case 36616:
                for (int doorId : RioDoorsToOpen)
                    ReflectionUtils.getDoor(doorId).openMe();
                break;
        }
    }

    public void teleportTeamsToArena() {
        Integer n = 0;
        for (Player player : getPlayers(_team1live)) {
            unRide(player);
            unSummonPet(player, true);
            player.teleToLocation(_team1points.get(n), ReflectionManager.DEFAULT);
            n++;
        }
        n = 0;
        for (Player player : getPlayers(_team2live)) {
            unRide(player);
            unSummonPet(player, true);
            player.teleToLocation(_team2points.get(n), ReflectionManager.DEFAULT);
            n++;
        }
    }

    public boolean playerHasLost(Player player) {
        if (player.getTeam() == TeamType.BLUE)
            _team1live.remove(player.getStoredId());
        else
            _team2live.remove(player.getStoredId());
        player.startRooted();
        player.startAbnormalEffect(AbnormalEffect.ROOT);
        return !checkTeams();
    }

    public void teamHasLost(Integer team_id) {
        if (team_id == 1) {
            for (Player plr : getPlayers(_team1list)) {
                sayToPlayers("Команда 2 победила", true, plr, 0);
                saveTeamPoints(plr, Config.EVENT_TVT_ARENA_TEAM_LOSS, _nArenaId);
            }
            for (Player plr : getPlayers(_team2list)) {
                sayToPlayers("Команда 2 победила", true, plr, 0);
                saveTeamPoints(plr, Config.EVENT_TVT_ARENA_TEAM_WIN, _nArenaId);
            }
        } else {
            for (Player plr : getPlayers(_team1list)) {
                sayToPlayers("Команда 1 победила", true, plr, 0);
                saveTeamPoints(plr, Config.EVENT_TVT_ARENA_TEAM_WIN, _nArenaId);
            }
            for (Player plr : getPlayers(_team2list)) {
                sayToPlayers("Команда 1 победила", true, plr, 0);
                saveTeamPoints(plr, Config.EVENT_TVT_ARENA_TEAM_LOSS, _nArenaId);
            }
        }
        unParalyzeTeams();
        ressurectPlayers();
        healPlayers();
        teleportPlayersToSavedCoords();
        clearTeams();
        _status = 0;
        _timeOutTask = false;
    }

    public void template_timeOut() {
        if (_timeOutTask && _status == 3) {
            for (Player plr : getPlayers(_team1list)) {
                sayToPlayers("Время истекло, ничья!", true, plr, 0);
                saveTeamPoints(plr, Config.EVENT_TVT_ARENA_TEAM_DRAW, _nArenaId);
            }
            for (Player plr : getPlayers(_team2list)) {
                sayToPlayers("Время истекло, ничья!", true, plr, 0);
                saveTeamPoints(plr, Config.EVENT_TVT_ARENA_TEAM_DRAW, _nArenaId);
            }
            unParalyzeTeams();
            teleportPlayersToSavedCoords();
            ressurectPlayers();
            healPlayers();
            clearTeams();
            _status = 0;
            _timeOutTask = false;
        }
    }

    public void clearTeams() {
        for (Player player : getPlayers(_team1list)) {
            player.setTeam(TeamType.NONE);
            player.setIsInTVTArena(false);
        }
        for (Player player : getPlayers(_team2list)) {
            player.setTeam(TeamType.NONE);
            player.setIsInTVTArena(false);
        }
        _team1list.clear();
        _team2list.clear();
        _team1live.clear();
        _team2live.clear();
    }

    public void onDeath(Creature self, Creature killer) {
        if (_status >= 2 && self.isPlayer() && self.getTeam() != TeamType.NONE && (_team1list.contains(self.getStoredId()) || _team2list.contains(self.getStoredId()))) {
            Player player = self.getPlayer();
            Player kplayer = killer.getPlayer();
            if (kplayer != null) {
                for (Player plr : getPlayers(_team1list))
                    sayToPlayers(kplayer.getName() + " убил " + player.getName(), true, plr, 1);
                for (Player plr : getPlayers(_team2list))
                    sayToPlayers(kplayer.getName() + " убил " + player.getName(), true, plr, 1);
                playerHasLost(player);
            } else
                playerHasLost(player);
        }
    }

    public void onPlayerExit(Player player) {
        if (player != null && _status > 0 && player.getTeam() != TeamType.NONE && (_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId())))
            switch (_status) {
                case 1:
                    removePlayer(player);
                    for (Player plr : getPlayers(_team1list))
                        sayToPlayers(player.getName() + " дисквалифицирован", true, plr, 1);
                    for (Player plr : getPlayers(_team2list))
                        sayToPlayers(player.getName() + " дисквалифицирован", true, plr, 1);

                    if (player.getStoredId() == _creatorId) {
                        for (Player plr : getPlayers(_team1list)) {
                            sayToPlayers("Бой прерван", true, plr, 0);
                            saveTeamPoints(plr, Config.EVENT_TVT_ARENA_TEAMLEADER_EXIT, _nArenaId);
                        }
                        for (Player plr : getPlayers(_team2list)) {
                            sayToPlayers("Бой прерван", true, plr, 0);
                            saveTeamPoints(plr, Config.EVENT_TVT_ARENA_TEAMLEADER_EXIT, _nArenaId);
                        }
                        teleportPlayersToSavedCoords();
                        unParalyzeTeams();
                        ressurectPlayers();
                        healPlayers();
                        clearTeams();

                        unParalyzeTeams();
                        clearTeams();
                        _status = 0;
                        _timeOutTask = false;
                    }
                    break;
                case 2:
                case 3:
                    removePlayer(player);
                    break;
            }
    }

    public void onTeleport(Player player) {
        if (player != null && _status > 1 && player.getTeam() != TeamType.NONE && player.isInZone(_zone))
            onPlayerExit(player);
    }

    protected static void sayToPlayers(String address, boolean bigFont, Player player, int nAlign) {
        switch (nAlign) {
            case 0:
                for (Player plr : GameObjectsStorage.getAllPlayersForIterate())
                    if (plr.isOnline())
                        player.sendPacket(new ExShowScreenMessage(address, 3000, ScreenMessageAlign.TOP_CENTER, true));
                break;
            case 1:
                for (Player plr : GameObjectsStorage.getAllPlayersForIterate())
                    if (plr.isOnline())
                        player.sendPacket(new ExShowScreenMessage(address, 3000, ScreenMessageAlign.TOP_CENTER, true));
                break;
        }
    }

    public void saveTeamPoints(Player player, int nPoints, int nArenaId) {
        Connection conSelect = null;
        PreparedStatement statementSelect = null;
        ResultSet rsetSelect = null;

        long eventTime = System.currentTimeMillis() / 1000;

        if (nPoints >= 0)
            try {
                conSelect = DatabaseFactory.getInstance().getConnection();
                statementSelect = conSelect.prepareStatement("SELECT * FROM event_tvt_arena WHERE obj_Id=?");
                statementSelect.setLong(1, player.getObjectId());
                rsetSelect = statementSelect.executeQuery();
                if (rsetSelect.next()) {
                    Connection conUpdate = null;
                    PreparedStatement statementUpdate = null;

                    try {
                        conUpdate = DatabaseFactory.getInstance().getConnection();
                        statementUpdate = conUpdate.prepareStatement("UPDATE event_tvt_arena SET char_points = char_points + ?, char_level = ? WHERE obj_Id = ?");
                        statementUpdate.setInt(1, nPoints);
                        statementUpdate.setInt(2, player.getLevel());
                        statementUpdate.setLong(3, player.getObjectId());
                        statementUpdate.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        DbUtils.closeQuietly(conUpdate, statementUpdate);
                    }
                } else {
                    Connection conInsertPlayer = null;
                    PreparedStatement statementInsertPlayer = null;
                    try {
                        conInsertPlayer = DatabaseFactory.getInstance().getConnection();
                        statementInsertPlayer = conInsertPlayer.prepareStatement("INSERT INTO event_tvt_arena (obj_Id, char_name, char_level, char_points, arena_id, event_time) VALUES(?,?,?,?,?,?)");
                        statementInsertPlayer.setLong(1, player.getObjectId());
                        statementInsertPlayer.setString(2, player.getName());
                        statementInsertPlayer.setInt(3, player.getLevel());
                        statementInsertPlayer.setInt(4, nPoints);
                        statementInsertPlayer.setInt(5, nArenaId);
                        statementInsertPlayer.setLong(6, eventTime);
                        statementInsertPlayer.execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        DbUtils.closeQuietly(conInsertPlayer, statementInsertPlayer);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DbUtils.closeQuietly(conSelect, statementSelect, rsetSelect);
            }
    }

    public static void sayToAll(String address, String[] replacements) {
        Announcements.getInstance().announceByCustomMessage(address, replacements, ChatType.CRITICAL_ANNOUNCE);
    }

    public class ZoneListener implements OnZoneEnterLeaveListener {
        @Override
        public void onZoneEnter(Zone zone, Creature cha) {
            if (cha == null)
                return;
            Player player = cha.getPlayer();
            if (_status >= 2 && player != null && !(_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId())))
                ThreadPoolManager.getInstance().schedule(new TeleportTask(cha, _zone.getSpawn()), 3000);
        }

        @Override
        public void onZoneLeave(Zone zone, Creature cha) {
            if (cha == null)
                return;
            Player player = cha.getPlayer();
            if (_status >= 2 && player != null && player.getTeam() != TeamType.NONE && (_team1list.contains(player.getStoredId()) || _team2list.contains(player.getStoredId()))) {
                double angle = PositionUtils.convertHeadingToDegree(cha.getHeading()); // угол в градусах
                double radian = Math.toRadians(angle - 90); // угол в радианах
                int x = (int) (cha.getX() + 50 * Math.sin(radian));
                int y = (int) (cha.getY() - 50 * Math.cos(radian));
                int z = cha.getZ();
                ThreadPoolManager.getInstance().schedule(new TeleportTask(cha, new Location(x, y, z)), 3000);
            }
        }
    }

    public class TeleportTask extends RunnableImpl {
        Location loc;
        Creature target;

        public TeleportTask(Creature target, Location loc) {
            this.target = target;
            this.loc = loc;
            target.block();
        }

        @Override
        public void runImpl() throws Exception {
            target.unblock();
            target.teleToLocation(loc);
        }
    }

    private void removePlayer(Player player) {
        if (player != null) {
            _team1list.remove(player.getStoredId());
            _team2list.remove(player.getStoredId());
            _team1live.remove(player.getStoredId());
            _team2live.remove(player.getStoredId());
            player.setTeam(TeamType.NONE);
        }
    }

    private List<Player> getPlayers(List<Long> list) {
        List<Player> result = new ArrayList<Player>();
        for (Long storeId : list) {
            Player player = GameObjectsStorage.getAsPlayer(storeId);
            if (player != null)
                result.add(player);
        }
        return result;
    }
}