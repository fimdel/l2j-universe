/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package events.SearchRaidBoss;

import l2p.commons.threading.RunnableImpl;
import l2p.commons.util.Rnd;
import l2p.gameserver.Announcements;
import l2p.gameserver.Config;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.listener.actor.OnDeathListener;
import l2p.gameserver.listener.actor.player.OnPlayerEnterListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.SimpleSpawner;
import l2p.gameserver.model.actor.listener.CharListenerList;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.RadarControl;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.network.serverpackets.components.ChatType;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class SearchRaidBoss_Antharas extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener {
    private static final Logger _log = LoggerFactory.getLogger(SearchRaidBoss_Antharas.class);
    private static List<SimpleSpawner> _spawns = new ArrayList<SimpleSpawner>();
    private static ScheduledFuture<?> _snowmanShoutTask;
    private static ScheduledFuture<?> _saveTask;
    private static ScheduledFuture<?> _sayTask;
    private static ScheduledFuture<?> _eatTask;

    public static SnowmanState _snowmanState;

    private static NpcInstance _snowman;
    private static Creature _thomas;

    public static enum SnowmanState {
        CAPTURED,
        KILLED,
        SAVED
    }

    private static final int INITIAL_SAVE_DELAY = 11 * 60 * 1000; // 10 мин
    private static final int SAVE_INTERVAL = 60 * 60 * 1000; // 30 мин      + 60
    private static final int SNOWMAN_SHOUT_INTERVAL = 1 * 60 * 1000; // 1 мин
    private static final int THOMAS_EAT_DELAY = 10 * 60 * 1000; // 30 мин
    private static final int SATNA_SAY_INTERVAL = 5 * 60 * 1000; // 5 мин
    private static final int EVENT_MANAGER_ID = 70016;
    private static final int CTREE_ID = 70017;
    private static final int EVENT_REWARDER_ID = 70016;
    private static final int МилаяКоровка = 19387;
    private static final int Antharas = 70003;
    private static final int SANTA_BUFF_REUSE = 12 * 3600 * 1000; // 12 hours

    private static boolean _active = false;

    @Override
    public void onLoad() {
        CharListenerList.addGlobal(this);
        if (isActive()) {
            _active = true;
            spawnEventManagers();
            _log.info("Loaded Event: SearchRaidBoss_Antharas [state: activated]");
            _saveTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SaveTask(), INITIAL_SAVE_DELAY, SAVE_INTERVAL);
            _sayTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SayTask(), SATNA_SAY_INTERVAL, SATNA_SAY_INTERVAL);
            _snowmanState = SnowmanState.SAVED;
        } else
            _log.info("Loaded Event: SearchRaidBoss_Antharas [state: deactivated]");
    }

    /**
     * Читает статус эвента из базы.
     */
    private static boolean isActive() {
        return IsActive("SearchRaidBoss_Antharas");
    }

    /**
     * Запускает эвент
     */
    public void startEvent() {
        Player player = getSelf();
        if (!player.getPlayerAccess().IsEventGm)
            return;

        /* FIXME */
        if (Boolean.FALSE) {
            player.sendMessage("Event is currently disabled");
            return;
        }

        if (SetActive("SearchRaidBoss_Antharas", true)) {
            System.out.println("Event 'SearchRaidBoss_Antharas' started.");
            Announcements.getInstance().announceByCustomMessage("scripts.events.SearchRaidBoss_Antharas.AnnounceEventStarted", null);
            if (_saveTask == null)
                _saveTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SaveTask(), INITIAL_SAVE_DELAY, SAVE_INTERVAL);
            if (_sayTask == null)
                _sayTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SayTask(), SATNA_SAY_INTERVAL, SATNA_SAY_INTERVAL);
            _snowmanState = SnowmanState.SAVED;
        } else
            player.sendMessage("Event 'SearchRaidBoss_Antharas' already started.");

        _active = true;

        show("admin/events.htm", player);
    }

    /**
     * Останавливает эвент
     */
    public void stopEvent() {
        Player player = getSelf();
        if (!player.getPlayerAccess().IsEventGm)
            return;
        if (SetActive("SearchRaidBoss_Antharas", false)) {
            unSpawnEventManagers();
            if (_snowman != null)
                _snowman.deleteMe();
            if (_thomas != null)
                _thomas.deleteMe();
            System.out.println("Event 'SearchRaidBoss_Antharas' stopped.");
            Announcements.getInstance().announceByCustomMessage("scripts.events.SearchRaidBoss_Antharas.AnnounceEventStoped", null);
            if (_saveTask != null) {
                _saveTask.cancel(false);
                _saveTask = null;
            }
            if (_sayTask != null) {
                _sayTask.cancel(false);
                _sayTask = null;
            }
            if (_eatTask != null) {
                _eatTask.cancel(false);
                _eatTask = null;
            }
            _snowmanState = SnowmanState.SAVED;
        } else
            player.sendMessage("Event 'SearchRaidBoss_Antharas' not started.");

        _active = false;

        show("admin/events.htm", player);
    }

    /**
     * Спавнит эвент менеджеров и рядом ёлки
     */
    private void spawnEventManagers() {
        final int EVENT_MANAGERS[][] = {
                {81921, 148921, -3467, 16384},
                {146405, 28360, -2269, 49648},
                {19319, 144919, -3103, 31135},
                {-82805, 149890, -3129, 16384},
                {-12347, 122549, -3104, 16384},
                {110642, 220165, -3655, 61898},
                {116619, 75463, -2721, 20881},
                {85513, 16014, -3668, 23681},
                {81999, 53793, -1496, 61621},
                {148159, -55484, -2734, 44315},
                {44185, -48502, -797, 27479},
                {86899, -143229, -1293, 8192}};

        final int CTREES[][] = {
                {81961, 148921, -3467, 0},
                {146445, 28360, -2269, 0},
                {19319, 144959, -3103, 0},
                {-82845, 149890, -3129, 0},
                {-12387, 122549, -3104, 0},
                {110602, 220165, -3655, 0},
                {116659, 75463, -2721, 0},
                {85553, 16014, -3668, 0},
                {81999, 53743, -1496, 0},
                {148199, -55484, -2734, 0},
                {44185, -48542, -797, 0},
                {86859, -143229, -1293, 0}};

        SpawnNPCs(EVENT_MANAGER_ID, EVENT_MANAGERS, _spawns);
        SpawnNPCs(CTREE_ID, CTREES, _spawns);
    }

    /**
     * Удаляет спавн эвент менеджеров
     */
    private void unSpawnEventManagers() {
        deSpawnNPCs(_spawns);
    }

    @Override
    public void onReload() {
        if (_saveTask != null)
            _saveTask.cancel(false);
        _saveTask = null;
        if (_sayTask != null)
            _sayTask.cancel(false);
        _sayTask = null;
        _snowmanState = SnowmanState.SAVED;
    }

    @Override
    public void onShutdown() {
        unSpawnEventManagers();
    }

    /**
     * Обработчик смерти мобов
     */
    @Override
    public void onDeath(Creature cha, Creature killer) {
        if (_active && killer != null) {
            Player pKiller = killer.getPlayer();
            if (pKiller != null && SimpleCheckDrop(cha, killer) && Rnd.get(1000) < Config.EVENT_SAVING_SNOWMAN_SANTA_SPAWN_CHANCE) {
                List<Player> players = new ArrayList<Player>();
                if (pKiller.isInParty())
                    players = pKiller.getParty().getPartyMembers();
                else
                    players.add(pKiller);
            }
        }
    }

    public void locateSnowman() {
        Player player = getSelf();
        if (!_active || player.isActionsDisabled() || player.isSitting() || player.getLastNpc() == null || player.getLastNpc().getDistance(player) > 300)
            return;

        if (_snowman != null) {
            // Убираем и ставим флажок на карте и стрелку на компасе
            player.sendPacket(new RadarControl(2, 2, _snowman.getLoc()), new RadarControl(0, 1, _snowman.getLoc()));
            player.sendPacket(new SystemMessage(SystemMessage.S2_S1).addZoneName(_snowman.getLoc()).addString("Ищите Antaras в "));
        } else
            player.sendPacket(Msg.YOUR_TARGET_CANNOT_BE_FOUND);
    }

    @Override
    public void onPlayerEnter(Player player) {
        if (_active)
            Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.SearchRaidBoss_Antharas.AnnounceEventStarted", null);
    }

    private static Location getRandomSpawnPoint() {
        return new Location(26174, -17134, -2747);
    }

    // Индюк захватывает снеговика
    public void captureSnowman() {
        Location spawnPoint = getRandomSpawnPoint();

        for (Player player : GameObjectsStorage.getAllPlayersForIterate()) {
            Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.SearchRaidBoss_Antharas.AnnounceSnowmanCaptured", null, ChatType.CRITICAL_ANNOUNCE);
            player.sendPacket(new SystemMessage(SystemMessage.S2_S1).addZoneName(spawnPoint).addString("Ищите Antharas в "));
            // Убираем и ставим флажок на карте и стрелку на компасе
            player.sendPacket(new RadarControl(2, 2, spawnPoint), new RadarControl(0, 1, spawnPoint));
        }

        // Спауним снеговика
        NpcTemplate template = NpcHolder.getInstance().getTemplate(МилаяКоровка);
        if (template == null) {
            System.out.println("WARNING! events.SearchRaidBoss_Antharas.captureSnowman template is null for npc: " + МилаяКоровка);
            Thread.dumpStack();
            return;
        }

        SimpleSpawner sp = new SimpleSpawner(template);
        sp.setLoc(spawnPoint);
        sp.setAmount(1);
        sp.setRespawnDelay(0);
        _snowman = sp.doSpawn(true);

        if (_snowman == null)
            return;

        // Спауним Томаса
        template = NpcHolder.getInstance().getTemplate(Antharas);
        if (template == null) {
            System.out.println("WARNING! events.SearchRaidBoss_Antharas.captureSnowman template is null for npc: " + Antharas);
            Thread.dumpStack();
            return;
        }

        Location pos = Location.findPointToStay(_snowman, 100, 120);

        sp = new SimpleSpawner(template);
        sp.setLoc(pos);
        sp.setAmount(1);
        sp.setRespawnDelay(0);
        _thomas = sp.doSpawn(true);

        if (_thomas == null)
            return;

        _snowmanState = SnowmanState.CAPTURED;

        // Если по каким-то причинам таск существует, останавливаем его
        if (_snowmanShoutTask != null) {
            _snowmanShoutTask.cancel(false);
            _snowmanShoutTask = null;
        }
        _snowmanShoutTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new ShoutTask(), 1, SNOWMAN_SHOUT_INTERVAL);

        if (_eatTask != null) {
            _eatTask.cancel(false);
            _eatTask = null;
        }
        _eatTask = executeTask("events.SearchRaidBoss_Antharas.SearchRaidBoss_Antharas", "eatSnowman", new Object[0], THOMAS_EAT_DELAY);
    }

    // Индюк захавывает снеговика
    public static void eatSnowman() {
        if (_snowman == null || _thomas == null)
            return;

        for (Player player : GameObjectsStorage.getAllPlayersForIterate())
            Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.SearchRaidBoss_Antharas.AnnounceSnowmanKilled", null, ChatType.CRITICAL_ANNOUNCE);

        _snowmanState = SnowmanState.KILLED;

        if (_snowmanShoutTask != null) {
            _snowmanShoutTask.cancel(false);
            _snowmanShoutTask = null;
        }

        _snowman.deleteMe();
        _thomas.deleteMe();
    }

    // Индюк умер, освобождаем снеговика
    public static void freeSnowman(Creature topDamager) {
        if (_snowman == null || topDamager == null || !topDamager.isPlayable())
            return;

        for (Player player : GameObjectsStorage.getAllPlayersForIterate())
            Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.SearchRaidBoss_Antharas.AnnounceSnowmanSaved", null, ChatType.CRITICAL_ANNOUNCE);

        _snowmanState = SnowmanState.SAVED;

        if (_snowmanShoutTask != null) {
            _snowmanShoutTask.cancel(false);
            _snowmanShoutTask = null;
        }
        if (_eatTask != null) {
            _eatTask.cancel(false);
            _eatTask = null;
        }

        Player player = topDamager.getPlayer();
        Functions.npcSayCustomMessage(_snowman, "scripts.events.SearchRaidBoss_Antharas.SnowmanSayTnx", player.getName());
        addItem(player, 20034, 3); // Revita-Pop
        addItem(player, 20338, 1); // Rune of Experience Points 50%	10 Hour Expiration Period
        addItem(player, 20344, 1); // Rune of SP 50% 10 Hour Expiration Period

        ThreadPoolManager.getInstance().execute(new RunnableImpl() {
            @Override
            public void runImpl() {
                _snowman.deleteMe();
            }

        });
    }

    public class ShoutTask extends RunnableImpl {
        @Override
        public void runImpl() {
            if (!_active || _snowman == null || _snowmanState != SnowmanState.CAPTURED)
                return;

            Functions.npcShoutCustomMessage(_snowman, "scripts.events.SearchRaidBoss_Antharas.SnowmanShout");
        }
    }

    public class SayTask extends RunnableImpl {
        @Override
        public void runImpl() {
            if (!_active)
                return;

            for (SimpleSpawner s : _spawns)
                if (s.getCurrentNpcId() == EVENT_MANAGER_ID)
                    Functions.npcSayCustomMessage(s.getLastSpawn(), "scripts.events.SavingSnowman.SantaSay");
        }
    }

    public class SaveTask extends RunnableImpl {
        @Override
        public void runImpl() {
            if (!_active || _snowmanState == SnowmanState.CAPTURED)
                return;

            captureSnowman();
        }
    }
}
