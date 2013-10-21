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
package lineage2.loginserver.gameservercon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lineage2.commons.threading.RunnableImpl;
import lineage2.loginserver.Config;
import lineage2.loginserver.ThreadPoolManager;
import lineage2.loginserver.gameservercon.lspackets.PingRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GameServerConnection
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(GameServerConnection.class);
	/**
	 * Field readBuffer.
	 */
	final ByteBuffer readBuffer = ByteBuffer.allocate(64 * 1024).order(ByteOrder.LITTLE_ENDIAN);
	/**
	 * Field sendQueue.
	 */
	final Queue<SendablePacket> sendQueue = new ArrayDeque<>();
	/**
	 * Field sendLock.
	 */
	final Lock sendLock = new ReentrantLock();
	/**
	 * Field isPengingWrite.
	 */
	final AtomicBoolean isPengingWrite = new AtomicBoolean();
	/**
	 * Field selector.
	 */
	private final Selector selector;
	/**
	 * Field key.
	 */
	private final SelectionKey key;
	/**
	 * Field gameServer.
	 */
	GameServer gameServer;
	/**
	 * Field _pingTask.
	 */
	private Future<?> _pingTask;
	/**
	 * Field _pingRetry.
	 */
	int _pingRetry;
	
	/**
	 * @author Mobius
	 */
	private class PingTask extends RunnableImpl
	{
		/**
		 * Constructor for PingTask.
		 */
		public PingTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (Config.GAME_SERVER_PING_RETRY > 0)
			{
				if (_pingRetry > Config.GAME_SERVER_PING_RETRY)
				{
					_log.warn("Gameserver " + gameServer.getId() + " [" + gameServer.getName() + "] : ping timeout!");
					closeNow();
					return;
				}
			}
			_pingRetry++;
			sendPacket(new PingRequest());
		}
	}
	
	/**
	 * Constructor for GameServerConnection.
	 * @param key SelectionKey
	 */
	public GameServerConnection(SelectionKey key)
	{
		this.key = key;
		selector = key.selector();
	}
	
	/**
	 * Method sendPacket.
	 * @param packet SendablePacket
	 */
	public void sendPacket(SendablePacket packet)
	{
		boolean wakeUp;
		sendLock.lock();
		try
		{
			sendQueue.add(packet);
			wakeUp = enableWriteInterest();
		}
		catch (CancelledKeyException e)
		{
			return;
		}
		finally
		{
			sendLock.unlock();
		}
		if (wakeUp)
		{
			selector.wakeup();
		}
	}
	
	/**
	 * Method disableWriteInterest.
	 * @return boolean * @throws CancelledKeyException
	 */
	protected boolean disableWriteInterest() throws CancelledKeyException
	{
		if (isPengingWrite.compareAndSet(true, false))
		{
			key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
			return true;
		}
		return false;
	}
	
	/**
	 * Method enableWriteInterest.
	 * @return boolean * @throws CancelledKeyException
	 */
	protected boolean enableWriteInterest() throws CancelledKeyException
	{
		if (!(isPengingWrite.getAndSet(true)))
		{
			key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
			return true;
		}
		return false;
	}
	
	/**
	 * Method closeNow.
	 */
	public void closeNow()
	{
		key.interestOps(SelectionKey.OP_CONNECT);
		selector.wakeup();
	}
	
	/**
	 * Method onDisconnection.
	 */
	public void onDisconnection()
	{
		try
		{
			stopPingTask();
			readBuffer.clear();
			sendLock.lock();
			try
			{
				sendQueue.clear();
			}
			finally
			{
				sendLock.unlock();
			}
			isPengingWrite.set(false);
			if ((gameServer != null) && gameServer.isAuthed())
			{
				_log.info("Connection with gameserver " + gameServer.getId() + " [" + gameServer.getName() + "] lost.");
				_log.info("Setting gameserver down.");
				gameServer.setDown();
			}
			gameServer = null;
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
	}
	
	/**
	 * Method getReadBuffer.
	 * @return ByteBuffer
	 */
	ByteBuffer getReadBuffer()
	{
		return readBuffer;
	}
	
	/**
	 * Method getGameServer.
	 * @return GameServer
	 */
	GameServer getGameServer()
	{
		return gameServer;
	}
	
	/**
	 * Method setGameServer.
	 * @param gameServer GameServer
	 */
	void setGameServer(GameServer gameServer)
	{
		this.gameServer = gameServer;
	}
	
	/**
	 * Method getIpAddress.
	 * @return String
	 */
	public String getIpAddress()
	{
		return ((SocketChannel) key.channel()).socket().getInetAddress().getHostAddress();
	}
	
	/**
	 * Method onPingResponse.
	 */
	public void onPingResponse()
	{
		_pingRetry = 0;
	}
	
	/**
	 * Method startPingTask.
	 */
	public void startPingTask()
	{
		if (Config.GAME_SERVER_PING_DELAY == 0)
		{
			return;
		}
		_pingTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new PingTask(), Config.GAME_SERVER_PING_DELAY, Config.GAME_SERVER_PING_DELAY);
	}
	
	/**
	 * Method stopPingTask.
	 */
	public void stopPingTask()
	{
		if (_pingTask != null)
		{
			_pingTask.cancel(false);
			_pingTask = null;
		}
	}
}
