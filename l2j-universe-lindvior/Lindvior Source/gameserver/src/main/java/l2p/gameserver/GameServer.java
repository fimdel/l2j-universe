/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver;

import l2p.commons.lang.StatsUtils;
import l2p.commons.listener.Listener;
import l2p.commons.listener.ListenerList;
import l2p.commons.net.nio.impl.SelectorThread;
import l2p.commons.versioning.Version;
import l2p.gameserver.cache.CrestCache;
import l2p.gameserver.dao.CharacterDAO;
import l2p.gameserver.dao.ItemsDAO;
import l2p.gameserver.data.BoatHolder;
import l2p.gameserver.data.xml.Parsers;
import l2p.gameserver.data.xml.holder.EventHolder;
import l2p.gameserver.data.xml.holder.ResidenceHolder;
import l2p.gameserver.data.xml.holder.StaticObjectHolder;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.geodata.geoeditorcon.GeoEditorListener;
import l2p.gameserver.handler.admincommands.AdminCommandHandler;
import l2p.gameserver.handler.items.ItemHandler;
import l2p.gameserver.handler.test.BypassHolder;
import l2p.gameserver.handler.usercommands.UserCommandHandler;
import l2p.gameserver.handler.voicecommands.VoicedCommandHandler;
import l2p.gameserver.idfactory.IdFactory;
import l2p.gameserver.instancemanager.*;
import l2p.gameserver.instancemanager.commission.CommissionShopManager;
import l2p.gameserver.instancemanager.games.FishingChampionShipManager;
import l2p.gameserver.instancemanager.games.LotteryManager;
import l2p.gameserver.instancemanager.games.MiniGameScoreManager;
import l2p.gameserver.instancemanager.itemauction.ItemAuctionManager;
import l2p.gameserver.instancemanager.naia.NaiaCoreManager;
import l2p.gameserver.instancemanager.naia.NaiaTowerManager;
import l2p.gameserver.listener.GameListener;
import l2p.gameserver.listener.game.OnShutdownListener;
import l2p.gameserver.listener.game.OnStartListener;
import l2p.gameserver.loginservercon.LoginServerCommunication;
import l2p.gameserver.model.AutoChatHandler;
import l2p.gameserver.model.World;
import l2p.gameserver.model.entity.Hero;
import l2p.gameserver.model.entity.MonsterRace;
import l2p.gameserver.model.entity.olympiad.Olympiad;
import l2p.gameserver.model.items.etcitems.AttributeStoneManager;
import l2p.gameserver.model.items.etcitems.EnchantScrollManager;
import l2p.gameserver.model.items.etcitems.LifeStoneManager;
import l2p.gameserver.model.systems.VitalityBooty;
import l2p.gameserver.network.GameClient;
import l2p.gameserver.network.GamePacketHandler;
import l2p.gameserver.network.telnet.TelnetServer;
import l2p.gameserver.scripts.Scripts;
import l2p.gameserver.tables.*;
import l2p.gameserver.taskmanager.ItemsAutoDestroy;
import l2p.gameserver.taskmanager.TaskManager;
import l2p.gameserver.taskmanager.tasks.RestoreOfflineTraders;
import l2p.gameserver.utils.Strings;
import l2p.gameserver.utils.UtilNetPingServer;
import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;

public class GameServer {
    public static final int LOGIN_SERVER_PROTOCOL = 2;
    private static final Logger _log = LoggerFactory.getLogger(GameServer.class);

    public class GameServerListenerList extends ListenerList<GameServer> {
        public void onStart() {
            for (Listener<GameServer> listener : getListeners()) {
                if (OnStartListener.class.isInstance(listener)) {
                    ((OnStartListener) listener).onStart();
                }
            }
        }

        public void onShutdown() {
            for (Listener<GameServer> listener : getListeners()) {
                if (OnShutdownListener.class.isInstance(listener)) {
                    ((OnShutdownListener) listener).onShutdown();
                }
            }
        }
    }

    public static GameServer _instance;

    private final SelectorThread<GameClient> _selectorThreads[];
    private final Version version;
    private TelnetServer statusServer;
    private final GameServerListenerList _listeners;

    private final int _serverStarted;

    public SelectorThread<GameClient>[] getSelectorThreads() {
        return _selectorThreads;
    }

    public int time() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public int uptime() {
        return time() - _serverStarted;
    }

    @SuppressWarnings("unchecked")
    public GameServer() throws Exception {
        long serverLoadStart = System.currentTimeMillis();
        _instance = this;
        _serverStarted = time();
        _listeners = new GameServerListenerList();

        new File("./log/").mkdir();

        version = new Version(GameServer.class);

        _log.info("=================================================");
        _log.info("Developer team: .......... Murzik God Dev");
        _log.info("Revision: ................ " + version.getRevisionNumber());
        _log.info("Build date: .............. " + version.getBuildDate());
        _log.info("Compiler version: ........ " + version.getBuildJdk());
        _log.info("=================================================");

        // Initialize config
        Config.load();
        Config.GAMESERVER_HOSTNAME = UtilNetPingServer.getServerHost();
        // Check binding address
        checkFreePorts();
        // Initialize database
        Class.forName(Config.DATABASE_DRIVER).newInstance();
        DatabaseFactory.getInstance().getConnection().close();

        IdFactory _idFactory = IdFactory.getInstance();
        if (!_idFactory.isInitialized()) {
            _log.error("Could not read object IDs from DB. Please Check Your Data.");
            throw new Exception("Could not initialize the ID factory");
        }

        CacheManager.getInstance();

        ThreadPoolManager.getInstance();

        /*
           * Лучше прогружать мененджеры итемов ДО скриптов так как искрипты используют таблицы этих мененжеров Иначе - скриптам вернутся пустые таблицы!!!
           */
        AttributeStoneManager.load();
        LifeStoneManager.load();
        EnchantScrollManager.load();
        VitalityBooty.load();

        if (Config.ACCEPT_GEOEDITOR_CONN) {
            GeoEditorListener.getInstance();
        }
        _log.info(Strings.sectionPrint("GeoData loading..."));
        GeoEngine.load();

        _log.info(Strings.sectionPrint("Scripts loading..."));
        Scripts.getInstance();

        Strings.reload();

        GameTimeController.getInstance();

        World.init();

        _log.info(Strings.sectionPrint("Static data loading..."));
        Parsers.parseAll();

        ItemsDAO.getInstance();

        CrestCache.getInstance();

        CharacterDAO.getInstance();

        ClanTable.getInstance();

        SkillTreeTable.getInstance();

        AugmentationData.getInstance();

        PetSkillsTable.getInstance();

        ItemAuctionManager.getInstance();

        _log.info(Strings.sectionPrint("Spawn loading..."));
        SpawnManager.getInstance().spawnAll();

        StaticObjectHolder.getInstance().spawnAll();

        RaidBossSpawnManager.getInstance();

        Scripts.getInstance().init();

        DimensionalRiftManager.getInstance();

        Announcements.getInstance();

        LotteryManager.getInstance();

        PlayerMessageStack.getInstance();

        if (Config.AUTODESTROY_ITEM_AFTER > 0) {
            ItemsAutoDestroy.getInstance();
        }

        MonsterRace.getInstance();

        AutoChatHandler _autoChatHandler = AutoChatHandler.getInstance();
        _log.info("AutoChatHandler: Loaded " + _autoChatHandler.size() + " handlers in total.");

        if (Config.ENABLE_OLYMPIAD) {
            Olympiad.load();
            Hero.getInstance();
        }

        PetitionManager.getInstance();

        CursedWeaponsManager.getInstance();

        if (!Config.ALLOW_WEDDING) {
            CoupleManager.getInstance();
            _log.info("CoupleManager initialized");
        }

        ItemHandler.getInstance();
        BypassHolder.getInstance();
        AdminCommandHandler.getInstance().log();
        UserCommandHandler.getInstance().log();
        VoicedCommandHandler.getInstance().log();

        TaskManager.getInstance();

        _log.info(Strings.sectionPrint("Events loading..."));
        ResidenceHolder.getInstance().callInit();

        EventHolder.getInstance().callInit();

        BoatHolder.getInstance().spawnAll();
        CastleManorManager.getInstance();

        Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());

        _log.info("IdFactory: Free ObjectID's remaining: " + IdFactory.getInstance().size());

        CoupleManager.getInstance();

        if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED) {
            FishingChampionShipManager.getInstance();
        }

        _log.info(Strings.sectionPrint("Hellbound loading..."));
        HellboundManager.getInstance();

        _log.info(Strings.sectionPrint("Naia loading..."));
        NaiaTowerManager.getInstance();

        NaiaCoreManager.getInstance();

        _log.info(Strings.sectionPrint("Gracia loading..."));
        SoDManager.getInstance();

        SoIManager.getInstance();

        SoHManager.getInstance();

        HarnakUndegroundManager.getInstance();

        BloodAltarManager.getInstance();

        MiniGameScoreManager.getInstance();

        _log.info(Strings.sectionPrint("Vote reward manager loading..."));
        if (Config.L2TOP_MANAGER_ENABLED) {
            L2TopManager.getInstance();
        }
        if (Config.MMO_TOP_MANAGER_ENABLED) {
            MMOTopManager.getInstance();
        }

        CommissionShopManager.getInstance();

        AwakingManager.getInstance();

        PartySubstituteManager.getInstance();

        ArcanManager.getInstance();
        // WorldStatisticsManager.getInstance();
        DimensionalRiftManager.getInstance();

        SubClassTable.getInstance();

        EnchantHPBonusTable.getInstance();

        DynamicQuestManager.getInstance().init();

        ParnassusManager.getInstance();

        BaltusManager.getInstance();

        if (Config.GARBAGE_COLLECTOR_INTERVAL > 0) {
            Class.forName(GarbageCollector.class.getName());
        }

        Shutdown.getInstance().schedule(Config.RESTART_AT_TIME, Shutdown.RESTART);

        _log.info("GameServer Started");
        _log.info("Maximum Numbers of Connected Players: " + Config.MAXIMUM_ONLINE_USERS);

        l2p.gameserver.ccpGuard.Protection.Init();

        GamePacketHandler gph = new GamePacketHandler();

        InetAddress serverAddr = Config.GAMESERVER_HOSTNAME.equalsIgnoreCase("*") ? null : InetAddress.getByName(Config.GAMESERVER_HOSTNAME);

        _selectorThreads = new SelectorThread[Config.PORTS_GAME.length];
        for (int i = 0; i < Config.PORTS_GAME.length; i++) {
            _selectorThreads[i] = new SelectorThread<GameClient>(Config.SELECTOR_CONFIG, gph, gph, gph, null);
            _selectorThreads[i].openServerSocket(serverAddr, Config.PORTS_GAME[i]);
            _selectorThreads[i].start();
        }

        LoginServerCommunication.getInstance().start();

        if (Config.SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART) {
            ThreadPoolManager.getInstance().schedule(new RestoreOfflineTraders(), 30000L);
        }

        getListeners().onStart();

        if (Config.IS_TELNET_ENABLED) {
            statusServer = new TelnetServer();
        } else {
            _log.info("Telnet server is currently disabled.");
        }

        _log.info("=================================================");
        String memUsage = new StringBuilder().append(StatsUtils.getMemUsage()).toString();
        for (String line : memUsage.split("\n")) {
            _log.info(line);
        }
        _log.info("=================================================");
        long serverLoadEnd = System.currentTimeMillis();
        _log.info("Server Loaded in " + ((serverLoadEnd - serverLoadStart) / 1000) + " seconds");

    }

    public GameServerListenerList getListeners() {
        return _listeners;
    }

    public static GameServer getInstance() {
        return _instance;
    }

    public <T extends GameListener> boolean addListener(T listener) {
        return _listeners.add(listener);
    }

    public <T extends GameListener> boolean removeListener(T listener) {
        return _listeners.remove(listener);
    }

    public static void checkFreePorts() {
        boolean binded = false;
        while (!binded) {
            for (int PORT_GAME : Config.PORTS_GAME) {
                try {
                    ServerSocket ss;
                    if (Config.GAMESERVER_HOSTNAME.equalsIgnoreCase("*")) {
                        ss = new ServerSocket(PORT_GAME);
                    } else {
                        ss = new ServerSocket(PORT_GAME, 50, InetAddress.getByName(Config.GAMESERVER_HOSTNAME));
                    }
                    ss.close();
                    binded = true;
                } catch (Exception e) {
                    _log.warn("Port " + PORT_GAME + " is allready binded. Please free it and restart server.");
                    binded = false;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new GameServer();
    }

    public Version getVersion() {
        return version;
    }

    public TelnetServer getStatusServer() {
        return statusServer;
    }
}
