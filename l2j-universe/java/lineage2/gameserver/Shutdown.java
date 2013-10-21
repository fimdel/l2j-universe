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

import java.util.Timer;
import java.util.TimerTask;

import lineage2.commons.net.nio.impl.SelectorThread;
import lineage2.commons.time.cron.SchedulingPattern;
import lineage2.commons.time.cron.SchedulingPattern.InvalidPatternException;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.instancemanager.CoupleManager;
import lineage2.gameserver.instancemanager.CursedWeaponsManager;
import lineage2.gameserver.instancemanager.games.FishingChampionShipManager;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Hero;
import lineage2.gameserver.model.entity.olympiad.OlympiadDatabase;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.loginservercon.LoginServerCommunication;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Scripts;
import lineage2.gameserver.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Shutdown extends Thread
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Shutdown.class);
	/**
	 * Field SHUTDOWN. (value is 0)
	 */
	public static final int SHUTDOWN = 0;
	/**
	 * Field RESTART. (value is 2)
	 */
	public static final int RESTART = 2;
	/**
	 * Field NONE. (value is -1)
	 */
	public static final int NONE = -1;
	/**
	 * Field _instance.
	 */
	private static final Shutdown _instance = new Shutdown();
	
	/**
	 * Method getInstance.
	 * @return Shutdown
	 */
	public static final Shutdown getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field counter.
	 */
	private Timer counter;
	/**
	 * Field shutdownMode.
	 */
	int shutdownMode;
	/**
	 * Field shutdownCounter.
	 */
	int shutdownCounter;
	
	/**
	 * @author Mobius
	 */
	private class ShutdownCounter extends TimerTask
	{
		/**
		 * Constructor for ShutdownCounter.
		 */
		public ShutdownCounter()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			switch (shutdownCounter)
			{
				case 1800:
				case 900:
				case 600:
				case 300:
				case 240:
				case 180:
				case 120:
				case 60:
					Announcements.getInstance().announceByCustomMessage("THE_SERVER_WILL_BE_COMING_DOWN_IN_S1_MINUTES", new String[]
					{
						String.valueOf(shutdownCounter / 60)
					});
					break;
				case 30:
				case 20:
				case 10:
				case 5:
					Announcements.getInstance().announceToAll(new SystemMessage(SystemMessage.THE_SERVER_WILL_BE_COMING_DOWN_IN_S1_SECONDS__PLEASE_FIND_A_SAFE_PLACE_TO_LOG_OUT).addNumber(shutdownCounter));
					break;
				case 0:
					switch (shutdownMode)
					{
						case SHUTDOWN:
							Runtime.getRuntime().exit(SHUTDOWN);
							break;
						case RESTART:
							Runtime.getRuntime().exit(RESTART);
							break;
					}
					cancel();
					return;
			}
			shutdownCounter--;
		}
	}
	
	/**
	 * Constructor for Shutdown.
	 */
	private Shutdown()
	{
		setName(getClass().getSimpleName());
		setDaemon(true);
		shutdownMode = NONE;
	}
	
	/**
	 * Method getSeconds.
	 * @return int
	 */
	public int getSeconds()
	{
		return shutdownMode == NONE ? -1 : shutdownCounter;
	}
	
	/**
	 * Method getMode.
	 * @return int
	 */
	public int getMode()
	{
		return shutdownMode;
	}
	
	/**
	 * Method schedule.
	 * @param seconds int
	 * @param shutdownMode int
	 */
	public synchronized void schedule(int seconds, int shutdownMode)
	{
		if (seconds < 0)
		{
			return;
		}
		if (counter != null)
		{
			counter.cancel();
		}
		this.shutdownMode = shutdownMode;
		shutdownCounter = seconds;
		_log.info("Scheduled server " + (shutdownMode == SHUTDOWN ? "shutdown" : "restart") + " in " + Util.formatTime(seconds) + ".");
		counter = new Timer("ShutdownCounter", true);
		counter.scheduleAtFixedRate(new ShutdownCounter(), 0, 1000L);
	}
	
	/**
	 * Method schedule.
	 * @param time String
	 * @param shutdownMode int
	 */
	public void schedule(String time, int shutdownMode)
	{
		SchedulingPattern cronTime;
		try
		{
			cronTime = new SchedulingPattern(time);
		}
		catch (InvalidPatternException e)
		{
			return;
		}
		int seconds = (int) ((cronTime.next(System.currentTimeMillis()) / 1000L) - (System.currentTimeMillis() / 1000L));
		schedule(seconds, shutdownMode);
	}
	
	/**
	 * Method cancel.
	 */
	public synchronized void cancel()
	{
		shutdownMode = NONE;
		if (counter != null)
		{
			counter.cancel();
		}
		counter = null;
	}
	
	/**
	 * Method run.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		System.out.println("Shutting down LS/GS communication...");
		LoginServerCommunication.getInstance().shutdown();
		System.out.println("Shutting down scripts...");
		Scripts.getInstance().shutdown();
		System.out.println("Disconnecting players...");
		disconnectAllPlayers();
		System.out.println("Saving data...");
		saveData();
		try
		{
			System.out.println("Shutting down thread pool...");
			ThreadPoolManager.getInstance().shutdown();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Shutting down selector...");
		if (GameServer.getInstance() != null)
		{
			for (SelectorThread<GameClient> st : GameServer.getInstance().getSelectorThreads())
			{
				try
				{
					st.shutdown();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		try
		{
			System.out.println("Shutting down database communication...");
			DatabaseFactory.getInstance().shutdown();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("Shutdown finished.");
	}
	
	/**
	 * Method saveData.
	 */
	private void saveData()
	{
		if (Config.ENABLE_OLYMPIAD)
		{
			try
			{
				OlympiadDatabase.save();
				System.out.println("Olympiad: Data saved.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		if (Config.ALLOW_WEDDING)
		{
			try
			{
				CoupleManager.getInstance().store();
				System.out.println("CoupleManager: Data saved.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			FishingChampionShipManager.getInstance().shutdown();
			System.out.println("FishingChampionShipManager: Data saved.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			Hero.getInstance().shutdown();
			System.out.println("Hero: Data saved.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (Config.ALLOW_CURSED_WEAPONS)
		{
			try
			{
				CursedWeaponsManager.getInstance().saveData();
				System.out.println("CursedWeaponsManager: Data saved,");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method disconnectAllPlayers.
	 */
	private void disconnectAllPlayers()
	{
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			try
			{
				player.logout();
			}
			catch (Exception e)
			{
				System.out.println("Error while disconnecting: " + player + "!");
				e.printStackTrace();
			}
		}
	}
}
