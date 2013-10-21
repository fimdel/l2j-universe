/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver;

import java.util.Calendar;

import lineage2.commons.listener.Listener;
import lineage2.commons.listener.ListenerList;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.listener.GameListener;
import lineage2.gameserver.listener.game.OnDayNightChangeListener;
import lineage2.gameserver.listener.game.OnStartListener;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ClientSetTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GameTimeController
{
	/**
	 * @author Mobius
	 */
	private class OnStartListenerImpl implements OnStartListener
	{
		/**
		 * Constructor for OnStartListenerImpl.
		 */
		public OnStartListenerImpl()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onStart.
		 * @see lineage2.gameserver.listener.game.OnStartListener#onStart()
		 */
		@Override
		public void onStart()
		{
			ThreadPoolManager.getInstance().execute(_dayChangeNotify);
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class CheckSunState extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (isNowNight())
			{
				getInstance().getListenerEngine().onNight();
			}
			else
			{
				getInstance().getListenerEngine().onDay();
			}
			for (Player player : GameObjectsStorage.getAllPlayersForIterate())
			{
				player.checkDayNightMessages();
				player.sendPacket(new ClientSetTime());
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	protected class GameTimeListenerList extends ListenerList<GameServer>
	{
		/**
		 * Method onDay.
		 */
		public void onDay()
		{
			for (Listener<GameServer> listener : getListeners())
			{
				if (OnDayNightChangeListener.class.isInstance(listener))
				{
					((OnDayNightChangeListener) listener).onDay();
				}
			}
		}
		
		/**
		 * Method onNight.
		 */
		public void onNight()
		{
			for (Listener<GameServer> listener : getListeners())
			{
				if (OnDayNightChangeListener.class.isInstance(listener))
				{
					((OnDayNightChangeListener) listener).onNight();
				}
			}
		}
	}
	
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(GameTimeController.class);
	/**
	 * Field TICKS_PER_SECOND. (value is 10)
	 */
	public static final int TICKS_PER_SECOND = 10;
	/**
	 * Field MILLIS_IN_TICK.
	 */
	public static final int MILLIS_IN_TICK = 1000 / TICKS_PER_SECOND;
	/**
	 * Field _instance.
	 */
	private static final GameTimeController _instance = new GameTimeController();
	/**
	 * Field _gameStartTime.
	 */
	private final long _gameStartTime;
	/**
	 * Field listenerEngine.
	 */
	private final GameTimeListenerList listenerEngine = new GameTimeListenerList();
	/**
	 * Field _dayChangeNotify.
	 */
	final Runnable _dayChangeNotify = new CheckSunState();
	
	/**
	 * Method getInstance.
	 * @return GameTimeController
	 */
	public static final GameTimeController getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for GameTimeController.
	 */
	private GameTimeController()
	{
		_gameStartTime = getDayStartTime();
		GameServer.getInstance().addListener(new OnStartListenerImpl());
		StringBuilder msg = new StringBuilder();
		msg.append("GameTimeController: initialized.").append(' ');
		msg.append("Current time is ");
		msg.append(getGameHour()).append(':');
		if (getGameMin() < 10)
		{
			msg.append('0');
		}
		msg.append(getGameMin());
		msg.append(" in the ");
		if (isNowNight())
		{
			msg.append("night");
		}
		else
		{
			msg.append("day");
		}
		msg.append('.');
		_log.info(msg.toString());
		long nightStart = 0;
		long dayStart = 60 * 60 * 1000;
		while ((_gameStartTime + nightStart) < System.currentTimeMillis())
		{
			nightStart += 4 * 60 * 60 * 1000;
		}
		while ((_gameStartTime + dayStart) < System.currentTimeMillis())
		{
			dayStart += 4 * 60 * 60 * 1000;
		}
		dayStart -= System.currentTimeMillis() - _gameStartTime;
		nightStart -= System.currentTimeMillis() - _gameStartTime;
		ThreadPoolManager.getInstance().scheduleAtFixedRate(_dayChangeNotify, nightStart, 4 * 60 * 60 * 1000L);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(_dayChangeNotify, dayStart, 4 * 60 * 60 * 1000L);
	}
	
	/**
	 * Method getDayStartTime.
	 * @return long
	 */
	private long getDayStartTime()
	{
		Calendar dayStart = Calendar.getInstance();
		int HOUR_OF_DAY = dayStart.get(Calendar.HOUR_OF_DAY);
		dayStart.add(Calendar.HOUR_OF_DAY, -(HOUR_OF_DAY + 1) % 4);
		dayStart.set(Calendar.MINUTE, 0);
		dayStart.set(Calendar.SECOND, 0);
		dayStart.set(Calendar.MILLISECOND, 0);
		return dayStart.getTimeInMillis();
	}
	
	/**
	 * Method isNowNight.
	 * @return boolean
	 */
	public boolean isNowNight()
	{
		return getGameHour() < 6;
	}
	
	/**
	 * Method getGameTime.
	 * @return int
	 */
	public int getGameTime()
	{
		return getGameTicks() / MILLIS_IN_TICK;
	}
	
	/**
	 * Method getGameHour.
	 * @return int
	 */
	public int getGameHour()
	{
		return (getGameTime() / 60) % 24;
	}
	
	/**
	 * Method getGameMin.
	 * @return int
	 */
	public int getGameMin()
	{
		return getGameTime() % 60;
	}
	
	/**
	 * Method getGameTicks.
	 * @return int
	 */
	public int getGameTicks()
	{
		return (int) ((System.currentTimeMillis() - _gameStartTime) / MILLIS_IN_TICK);
	}
	
	/**
	 * Method getListenerEngine.
	 * @return GameTimeListenerList
	 */
	public GameTimeListenerList getListenerEngine()
	{
		return listenerEngine;
	}
	
	/**
	 * Method addListener.
	 * @param listener T
	 * @return boolean
	 */
	public <T extends GameListener> boolean addListener(T listener)
	{
		return listenerEngine.add(listener);
	}
	
	/**
	 * Method removeListener.
	 * @param listener T
	 * @return boolean
	 */
	public <T extends GameListener> boolean removeListener(T listener)
	{
		return listenerEngine.remove(listener);
	}
}
