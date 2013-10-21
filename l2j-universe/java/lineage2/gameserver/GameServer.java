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
package lineage2.gameserver;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;

import lineage2.commons.lang.StatsUtils;
import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.commons.net.nio.impl.SelectorThread;
import lineage2.commons.versioning.Version;
import lineage2.gameserver.cache.CrestCache;
import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.dao.ItemsDAO;
import lineage2.gameserver.data.BoatHolder;
import lineage2.gameserver.data.xml.Parsers;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.data.xml.holder.StaticObjectHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.handler.admincommands.AdminCommandHandler;
import lineage2.gameserver.handler.items.ItemHandler;
import lineage2.gameserver.handler.usercommands.UserCommandHandler;
import lineage2.gameserver.handler.voicecommands.VoicedCommandHandler;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.instancemanager.ArcanManager;
import lineage2.gameserver.instancemanager.AwakingManager;
import lineage2.gameserver.instancemanager.BloodAltarManager;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.instancemanager.CoupleManager;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.instancemanager.DelusionChamberManager;
import lineage2.gameserver.instancemanager.FindPartyManager;
import lineage2.gameserver.instancemanager.HarnakUndegroundManager;
import lineage2.gameserver.instancemanager.HellboundManager;
import lineage2.gameserver.instancemanager.L2TopManager;
import lineage2.gameserver.instancemanager.MMOTopManager;
import lineage2.gameserver.instancemanager.PetitionManager;
import lineage2.gameserver.instancemanager.PlayerMessageStack;
import lineage2.gameserver.instancemanager.RaidBossSpawnManager;
import lineage2.gameserver.instancemanager.SMSWayToPay;
import lineage2.gameserver.instancemanager.SoDManager;
import lineage2.gameserver.instancemanager.SoHManager;
import lineage2.gameserver.instancemanager.SoIManager;
import lineage2.gameserver.instancemanager.SpawnManager;
import lineage2.gameserver.instancemanager.WorldStatisticsManager;
import lineage2.gameserver.instancemanager.commission.CommissionShopManager;
import lineage2.gameserver.instancemanager.games.FishingChampionShipManager;
import lineage2.gameserver.instancemanager.games.LotteryManager;
import lineage2.gameserver.instancemanager.games.MiniGameScoreManager;
import lineage2.gameserver.instancemanager.itemauction.ItemAuctionManager;
import lineage2.gameserver.instancemanager.naia.NaiaCoreManager;
import lineage2.gameserver.instancemanager.naia.NaiaTowerManager;
import lineage2.gameserver.listener.GameListener;
import lineage2.gameserver.listener.game.OnShutdownListener;
import lineage2.gameserver.listener.game.OnStartListener;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.entity.MonsterRace;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.model.items.etcitems.AttributeStoneManager;
import lineage2.gameserver.model.items.etcitems.CrystallizationManager;
import lineage2.gameserver.model.items.etcitems.EnchantScrollManager;
import lineage2.gameserver.model.items.etcitems.LifeStoneManager;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.GamePacketHandler;
import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.network.telnet.TelnetServer;
import lineage2.gameserver.scripts.Scripts;
import lineage2.gameserver.tables.AttributeDamageResistTable;
import lineage2.gameserver.tables.AugmentationData;
import lineage2.gameserver.tables.ClanTable;
import lineage2.gameserver.tables.DualClassTable;
import lineage2.gameserver.tables.EnchantHPBonusTable;
import lineage2.gameserver.tables.EnchantStatBonusTable;
import lineage2.gameserver.tables.FakePlayersTable;
import lineage2.gameserver.tables.PetSkillsTable;
import lineage2.gameserver.tables.SkillTreeTable;
import lineage2.gameserver.tables.SubClassTable;
import lineage2.gameserver.taskmanager.ItemsAutoDestroy;
import lineage2.gameserver.taskmanager.TaskManager;
import lineage2.gameserver.taskmanager.tasks.RestoreOfflineTraders;
import lineage2.gameserver.utils.Strings;
import net.sf.ehcache.CacheManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GameServer
{
	/**
	 * Field LOGIN_SERVER_PROTOCOL. (value is 2)
	 */
	public static final int LOGIN_SERVER_PROTOCOL = 2;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(GameServer.class);
	
	/**
	 * @author Mobius
	 */
	public class GameServerListenerList extends ListenerList<GameServer>
	{
		/**
		 * Method onStart.
		 */
		public void onStart()
		{
			for (Listener<GameServer> listener : getListeners())
			{
				if (OnStartListener.class.isInstance(listener))
				{
					((OnStartListener) listener).onStart();
				}
			}
		}
		
		/**
		 * Method onShutdown.
		 */
		public void onShutdown()
		{
			for (Listener<GameServer> listener : getListeners())
			{
				if (OnShutdownListener.class.isInstance(listener))
				{
					((OnShutdownListener) listener).onShutdown();
				}
			}
		}
	}
	
	/**
	 * Field _instance.
	 */
	public static GameServer _instance;
	/**
	 * Field _selectorThreads.
	 */
	private final SelectorThread<GameClient> _selectorThreads[];
	/**
	 * Field version.
	 */
	private final Version version;
	/**
	 * Field statusServer.
	 */
	private TelnetServer statusServer;
	/**
	 * Field _listeners.
	 */
	private final GameServerListenerList _listeners;
	/**
	 * Field _serverStarted.
	 */
	private final int _serverStarted;
	
	/**
	 * Method getSelectorThreads.
	 * @return SelectorThread<GameClient>[]
	 */
	public SelectorThread<GameClient>[] getSelectorThreads()
	{
		return _selectorThreads;
	}
	
	/**
	 * Method time.
	 * @return int
	 */
	public int time()
	{
		return (int) (System.currentTimeMillis() / 1000);
	}
	
	/**
	 * Method uptime.
	 * @return int
	 */
	public int uptime()
	{
		return time() - _serverStarted;
	}
	
	/**
	 * Constructor for GameServer.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public GameServer() throws Exception
	{
		_instance = this;
		_serverStarted = time();
		_listeners = new GameServerListenerList();
		new File("./log/").mkdir();
		version = new Version(GameServer.class);
		_log.info("=================================================");
		_log.info("Revision: ................ " + version.getRevisionNumber());
		_log.info("Build date: .............. " + version.getBuildDate());
		_log.info("Compiler version: ........ " + version.getBuildJdk());
		_log.info("=================================================");
		Config.load();
		checkFreePorts();
		Class.forName(Config.DATABASE_DRIVER).newInstance();
		DatabaseFactory.getInstance().getConnection().close();
		IdFactory _idFactory = IdFactory.getInstance();
		if (!_idFactory.isInitialized())
		{
			_log.error("Could not read object IDs from DB. Please Check Your Data.");
			throw new Exception("Could not initialize the ID factory");
		}
		CacheManager.getInstance();
		ThreadPoolManager.getInstance();
		AttributeStoneManager.load();
		LifeStoneManager.load();
		EnchantScrollManager.load();
		CrystallizationManager.load();
		Scripts.getInstance();
		GeoEngine.load();
		Strings.reload();
		GameTimeController.getInstance();
		World.init();
		Parsers.parseAll();
		ItemsDAO.getInstance();
		CrestCache.getInstance();
		CharacterDAO.getInstance();
		ClanTable.getInstance();
		FakePlayersTable.getInstance();
		SkillTreeTable.getInstance();
		AugmentationData.getInstance();
		EnchantHPBonusTable.getInstance();
		EnchantStatBonusTable.getInstance();
		AttributeDamageResistTable.getInstance();
		PetSkillsTable.getInstance();
		ItemAuctionManager.getInstance();
		CommissionShopManager.getInstance();
		SpawnManager.getInstance().spawnAll();
		StaticObjectHolder.getInstance().spawnAll();
		RaidBossSpawnManager.getInstance();
		Scripts.getInstance().init();
		DelusionChamberManager.getInstance();
		Announcements.getInstance();
		LotteryManager.getInstance();
		PlayerMessageStack.getInstance();
		if (Config.AUTODESTROY_ITEM_AFTER > 0)
		{
			ItemsAutoDestroy.getInstance();
		}
		MonsterRace.getInstance();
		if (Config.ENABLE_OLYMPIAD)
		{
			Olympiad.load();
			Hero.getInstance();
		}
		PetitionManager.getInstance();
		CursedWeaponsManager.getInstance();
		if (!Config.ALLOW_WEDDING)
		{
			CoupleManager.getInstance();
			_log.info("CoupleManager initialized");
		}
		ItemHandler.getInstance();
		AdminCommandHandler.getInstance().log();
		UserCommandHandler.getInstance().log();
		VoicedCommandHandler.getInstance().log();
		TaskManager.getInstance();
		_log.info("=[Events]=========================================");
		ResidenceHolder.getInstance().callInit();
		EventHolder.getInstance().callInit();
		_log.info("==================================================");
		BoatHolder.getInstance().spawnAll();
		CastleManorManager.getInstance();
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		_log.info("IdFactory: Free ObjectID's remaining: " + IdFactory.getInstance().size());
		CoupleManager.getInstance();
		if (Config.ALT_FISH_CHAMPIONSHIP_ENABLED)
		{
			FishingChampionShipManager.getInstance();
		}
		HellboundManager.getInstance();
		NaiaTowerManager.getInstance();
		NaiaCoreManager.getInstance();
		SoDManager.getInstance();
		SoIManager.getInstance();
        SoHManager.getInstance();
        HarnakUndegroundManager.getInstance();

        BloodAltarManager.getInstance();
		L2TopManager.getInstance();
		MMOTopManager.getInstance();
		SMSWayToPay.getInstance();
		MiniGameScoreManager.getInstance();
		AwakingManager.getInstance();
		FindPartyManager.getInstance().load();
		
        ArcanManager.getInstance();
        WorldStatisticsManager.getInstance();

		SubClassTable.getInstance();
		DualClassTable.getInstance();
		if (Config.GARBAGE_COLLECTOR_INTERVAL > 0)
		{
			Class.forName(GarbageCollector.class.getName());
		}
		Shutdown.getInstance().schedule(Config.RESTART_AT_TIME, Shutdown.RESTART);
		_log.info("GameServer Started");
		_log.info("Maximum Numbers of Connected Players: " + Config.MAXIMUM_ONLINE_USERS);
		GamePacketHandler gph = new GamePacketHandler();
		InetAddress serverAddr = Config.GAMESERVER_HOSTNAME.equalsIgnoreCase("*") ? null : InetAddress.getByName(Config.GAMESERVER_HOSTNAME);
		_selectorThreads = new SelectorThread[Config.PORTS_GAME.length];
		for (int i = 0; i < Config.PORTS_GAME.length; i++)
		{
			_selectorThreads[i] = new SelectorThread<>(Config.SELECTOR_CONFIG, gph, gph, gph, null);
			_selectorThreads[i].openServerSocket(serverAddr, Config.PORTS_GAME[i]);
			_selectorThreads[i].start();
		}
		LoginServerCommunication.getInstance().start();
		if (Config.SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART)
		{
			ThreadPoolManager.getInstance().schedule(new RestoreOfflineTraders(), 30000L);
		}
		getListeners().onStart();
		if (Config.IS_TELNET_ENABLED)
		{
			statusServer = new TelnetServer();
		}
		else
		{
			_log.info("Telnet server is currently disabled.");
		}
		_log.info("=================================================");
		String memUsage = new StringBuilder().append(StatsUtils.getMemUsage()).toString();
		for (String line : memUsage.split("\n"))
		{
			_log.info(line);
		}
		_log.info("=================================================");
	}
	
	/**
	 * Method getListeners.
	 * @return GameServerListenerList
	 */
	public GameServerListenerList getListeners()
	{
		return _listeners;
	}
	
	/**
	 * Method getInstance.
	 * @return GameServer
	 */
	public static GameServer getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method addListener.
	 * @param listener T
	 * @return boolean
	 */
	public <T extends GameListener> boolean addListener(T listener)
	{
		return _listeners.add(listener);
	}
	
	/**
	 * Method removeListener.
	 * @param listener T
	 * @return boolean
	 */
	public <T extends GameListener> boolean removeListener(T listener)
	{
		return _listeners.remove(listener);
	}
	
	/**
	 * Method checkFreePorts.
	 */
	public static void checkFreePorts()
	{
		boolean binded = false;
		while (!binded)
		{
			for (int PORT_GAME : Config.PORTS_GAME)
			{
				try
				{
					ServerSocket ss;
					if (Config.GAMESERVER_HOSTNAME.equalsIgnoreCase("*"))
					{
						ss = new ServerSocket(PORT_GAME);
					}
					else
					{
						ss = new ServerSocket(PORT_GAME, 50, InetAddress.getByName(Config.GAMESERVER_HOSTNAME));
					}
					ss.close();
					binded = true;
				}
				catch (Exception e)
				{
					_log.warn("Port " + PORT_GAME + " is allready binded. Please free it and restart server.");
					binded = false;
					try
					{
						Thread.sleep(1000);
					}
					catch (InterruptedException e2)
					{
					}
				}
			}
		}
	}
	
	/**
	 * Method main.
	 * @param args String[]
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		new GameServer();
	}
	
	/**
	 * Method getVersion.
	 * @return Version
	 */
	public Version getVersion()
	{
		return version;
	}
	
	/**
	 * Method getStatusServer.
	 * @return TelnetServer
	 */
	public TelnetServer getStatusServer()
	{
		return statusServer;
	}
}
