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
package lineage2.gameserver.network.loginservercon;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.loginservercon.gspackets.AuthRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LoginServerCommunication extends Thread
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(LoginServerCommunication.class);
	/**
	 * Field instance.
	 */
	private static final LoginServerCommunication instance = new LoginServerCommunication();
	
	/**
	 * Method getInstance.
	 * @return LoginServerCommunication
	 */
	public static final LoginServerCommunication getInstance()
	{
		return instance;
	}
	
	/**
	 * Field waitingClients.
	 */
	private final Map<String, GameClient> waitingClients = new HashMap<>();
	/**
	 * Field authedClients.
	 */
	private final Map<String, GameClient> authedClients = new HashMap<>();
	/**
	 * Field lock.
	 */
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	/**
	 * Field readLock.
	 */
	private final Lock readLock = lock.readLock();
	/**
	 * Field writeLock.
	 */
	private final Lock writeLock = lock.writeLock();
	/**
	 * Field readBuffer.
	 */
	private final ByteBuffer readBuffer = ByteBuffer.allocate(64 * 1024).order(ByteOrder.LITTLE_ENDIAN);
	/**
	 * Field writeBuffer.
	 */
	private final ByteBuffer writeBuffer = ByteBuffer.allocate(64 * 1024).order(ByteOrder.LITTLE_ENDIAN);
	/**
	 * Field sendQueue.
	 */
	private final Queue<SendablePacket> sendQueue = new ArrayDeque<>();
	/**
	 * Field sendLock.
	 */
	private final Lock sendLock = new ReentrantLock();
	/**
	 * Field isPengingWrite.
	 */
	private final AtomicBoolean isPengingWrite = new AtomicBoolean();
	/**
	 * Field key.
	 */
	private SelectionKey key;
	/**
	 * Field selector.
	 */
	private Selector selector;
	/**
	 * Field shutdown.
	 */
	private boolean shutdown;
	/**
	 * Field restart.
	 */
	private boolean restart;
	
	/**
	 * Constructor for LoginServerCommunication.
	 */
	private LoginServerCommunication()
	{
		try
		{
			selector = Selector.open();
		}
		catch (IOException e)
		{
			_log.error("", e);
		}
	}
	
	/**
	 * Method connect.
	 * @throws IOException
	 */
	private void connect() throws IOException
	{
		_log.info("Connecting to loginserver on " + Config.GAME_SERVER_LOGIN_HOST + ":" + Config.GAME_SERVER_LOGIN_PORT);
		SocketChannel channel = SocketChannel.open();
		channel.configureBlocking(false);
		key = channel.register(selector, SelectionKey.OP_CONNECT);
		channel.connect(new InetSocketAddress(Config.GAME_SERVER_LOGIN_HOST, Config.GAME_SERVER_LOGIN_PORT));
	}
	
	/**
	 * Method sendPacket.
	 * @param packet SendablePacket
	 */
	public void sendPacket(SendablePacket packet)
	{
		if (isShutdown())
		{
			return;
		}
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
	private boolean disableWriteInterest() throws CancelledKeyException
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
	private boolean enableWriteInterest() throws CancelledKeyException
	{
		if (!(isPengingWrite.getAndSet(true)))
		{
			key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
			return true;
		}
		return false;
	}
	
	/**
	 * Method getReadBuffer.
	 * @return ByteBuffer
	 */
	protected ByteBuffer getReadBuffer()
	{
		return readBuffer;
	}
	
	/**
	 * Method getWriteBuffer.
	 * @return ByteBuffer
	 */
	protected ByteBuffer getWriteBuffer()
	{
		return writeBuffer;
	}
	
	/**
	 * Method run.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		Set<SelectionKey> keys;
		Iterator<SelectionKey> iterator;
		SelectionKey key;
		int opts;
		while (!shutdown)
		{
			restart = false;
			try
			{
				loop:
				while (!isShutdown())
				{
					connect();
					selector.select(5000L);
					keys = selector.selectedKeys();
					if (keys.isEmpty())
					{
						throw new IOException("Connection timeout.");
					}
					iterator = keys.iterator();
					try
					{
						while (iterator.hasNext())
						{
							key = iterator.next();
							iterator.remove();
							opts = key.readyOps();
							switch (opts)
							{
								case SelectionKey.OP_CONNECT:
									connect(key);
									break loop;
							}
						}
					}
					catch (CancelledKeyException e)
					{
						break;
					}
				}
				loop:
				while (!isShutdown())
				{
					selector.select();
					keys = selector.selectedKeys();
					iterator = keys.iterator();
					try
					{
						while (iterator.hasNext())
						{
							key = iterator.next();
							iterator.remove();
							opts = key.readyOps();
							switch (opts)
							{
								case SelectionKey.OP_WRITE:
									write(key);
									break;
								case SelectionKey.OP_READ:
									read(key);
									break;
								case SelectionKey.OP_READ | SelectionKey.OP_WRITE:
									write(key);
									read(key);
									break;
							}
						}
					}
					catch (CancelledKeyException e)
					{
						break loop;
					}
				}
			}
			catch (IOException e)
			{
				_log.error("LoginServer I/O error: " + e.getMessage());
			}
			close();
			try
			{
				Thread.sleep(5000L);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
	
	/**
	 * Method read.
	 * @param key SelectionKey
	 * @throws IOException
	 */
	private void read(SelectionKey key) throws IOException
	{
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buf = getReadBuffer();
		int count;
		count = channel.read(buf);
		if (count == -1)
		{
			throw new IOException("End of stream.");
		}
		if (count == 0)
		{
			return;
		}
		buf.flip();
		while (tryReadPacket(key, buf))
		{
		}
	}
	
	/**
	 * Method tryReadPacket.
	 * @param key SelectionKey
	 * @param buf ByteBuffer
	 * @return boolean * @throws IOException
	 */
	private boolean tryReadPacket(SelectionKey key, ByteBuffer buf) throws IOException
	{
		int pos = buf.position();
		if (buf.remaining() > 2)
		{
			int size = buf.getShort() & 0xffff;
			if (size <= 2)
			{
				throw new IOException("Incorrect packet size: <= 2");
			}
			size -= 2;
			if (size <= buf.remaining())
			{
				int limit = buf.limit();
				buf.limit(pos + size + 2);
				ReceivablePacket rp = PacketHandler.handlePacket(buf);
				if (rp != null)
				{
					if (rp.read())
					{
						ThreadPoolManager.getInstance().execute(rp);
					}
				}
				buf.limit(limit);
				buf.position(pos + size + 2);
				if (!buf.hasRemaining())
				{
					buf.clear();
					return false;
				}
				return true;
			}
			buf.position(pos);
		}
		buf.compact();
		return false;
	}
	
	/**
	 * Method write.
	 * @param key SelectionKey
	 * @throws IOException
	 */
	private void write(SelectionKey key) throws IOException
	{
		SocketChannel channel = (SocketChannel) key.channel();
		ByteBuffer buf = getWriteBuffer();
		boolean done;
		sendLock.lock();
		try
		{
			int i = 0;
			SendablePacket sp;
			while ((i++ < 64) && ((sp = sendQueue.poll()) != null))
			{
				int headerPos = buf.position();
				buf.position(headerPos + 2);
				sp.write();
				int dataSize = buf.position() - headerPos - 2;
				if (dataSize == 0)
				{
					buf.position(headerPos);
					continue;
				}
				buf.position(headerPos);
				buf.putShort((short) (dataSize + 2));
				buf.position(headerPos + dataSize + 2);
			}
			done = sendQueue.isEmpty();
			if (done)
			{
				disableWriteInterest();
			}
		}
		finally
		{
			sendLock.unlock();
		}
		buf.flip();
		channel.write(buf);
		if (buf.remaining() > 0)
		{
			buf.compact();
			done = false;
		}
		else
		{
			buf.clear();
		}
		if (!done)
		{
			if (enableWriteInterest())
			{
				selector.wakeup();
			}
		}
	}
	
	/**
	 * Method connect.
	 * @param key SelectionKey
	 * @throws IOException
	 */
	private void connect(SelectionKey key) throws IOException
	{
		SocketChannel channel = (SocketChannel) key.channel();
		channel.finishConnect();
		key.interestOps(key.interestOps() & ~SelectionKey.OP_CONNECT);
		key.interestOps(key.interestOps() | SelectionKey.OP_READ);
		sendPacket(new AuthRequest());
	}
	
	/**
	 * Method close.
	 */
	private void close()
	{
		restart = !shutdown;
		sendLock.lock();
		try
		{
			sendQueue.clear();
		}
		finally
		{
			sendLock.unlock();
		}
		readBuffer.clear();
		writeBuffer.clear();
		isPengingWrite.set(false);
		try
		{
			if (key != null)
			{
				key.channel().close();
				key.cancel();
			}
		}
		catch (IOException e)
		{
		}
		writeLock.lock();
		try
		{
			waitingClients.clear();
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	/**
	 * Method shutdown.
	 */
	public void shutdown()
	{
		shutdown = true;
		selector.wakeup();
	}
	
	/**
	 * Method isShutdown.
	 * @return boolean
	 */
	public boolean isShutdown()
	{
		return shutdown || restart;
	}
	
	/**
	 * Method restart.
	 */
	public void restart()
	{
		restart = true;
		selector.wakeup();
	}
	
	/**
	 * Method addWaitingClient.
	 * @param client GameClient
	 * @return GameClient
	 */
	public GameClient addWaitingClient(GameClient client)
	{
		writeLock.lock();
		try
		{
			return waitingClients.put(client.getLogin(), client);
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	/**
	 * Method removeWaitingClient.
	 * @param account String
	 * @return GameClient
	 */
	public GameClient removeWaitingClient(String account)
	{
		writeLock.lock();
		try
		{
			return waitingClients.remove(account);
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	/**
	 * Method addAuthedClient.
	 * @param client GameClient
	 * @return GameClient
	 */
	public GameClient addAuthedClient(GameClient client)
	{
		writeLock.lock();
		try
		{
			return authedClients.put(client.getLogin(), client);
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	/**
	 * Method removeAuthedClient.
	 * @param login String
	 * @return GameClient
	 */
	public GameClient removeAuthedClient(String login)
	{
		writeLock.lock();
		try
		{
			return authedClients.remove(login);
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	/**
	 * Method getAuthedClient.
	 * @param login String
	 * @return GameClient
	 */
	public GameClient getAuthedClient(String login)
	{
		readLock.lock();
		try
		{
			return authedClients.get(login);
		}
		finally
		{
			readLock.unlock();
		}
	}
	
	/**
	 * Method removeClient.
	 * @param client GameClient
	 * @return GameClient
	 */
	public GameClient removeClient(GameClient client)
	{
		writeLock.lock();
		try
		{
			if (client.isAuthed())
			{
				return authedClients.remove(client.getLogin());
			}
			return waitingClients.remove(client.getSessionKey());
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	/**
	 * Method getAccounts.
	 * @return String[]
	 */
	public String[] getAccounts()
	{
		readLock.lock();
		try
		{
			return authedClients.keySet().toArray(new String[authedClients.size()]);
		}
		finally
		{
			readLock.unlock();
		}
	}
}
