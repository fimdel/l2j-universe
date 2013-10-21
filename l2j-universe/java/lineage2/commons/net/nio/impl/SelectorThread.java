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
package lineage2.commons.net.nio.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SelectorThread<T extends MMOClient> extends Thread
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SelectorThread.class);
	
	/**
	 * Field _selector.
	 */
	private final Selector _selector = Selector.open();
	
	// Implementations
	/**
	 * Field _packetHandler.
	 */
	private final IPacketHandler<T> _packetHandler;
	/**
	 * Field _executor.
	 */
	private final IMMOExecutor<T> _executor;
	/**
	 * Field _clientFactory.
	 */
	private final IClientFactory<T> _clientFactory;
	/**
	 * Field _acceptFilter.
	 */
	private IAcceptFilter _acceptFilter;
	
	/**
	 * Field _shutdown.
	 */
	private boolean _shutdown;
	
	// Configs
	/**
	 * Field _sc.
	 */
	private final SelectorConfig _sc;
	/**
	 * Field HELPER_BUFFER_SIZE.
	 */
	private final int HELPER_BUFFER_SIZE;
	
	// MAIN BUFFERS
	/**
	 * Field DIRECT_WRITE_BUFFER.
	 */
	private final ByteBuffer DIRECT_WRITE_BUFFER;
	/**
	 * Field READ_BUFFER. Field WRITE_BUFFER.
	 */
	private final ByteBuffer WRITE_BUFFER, READ_BUFFER;
	/**
	 * Field WRITE_CLIENT.
	 */
	private T WRITE_CLIENT;
	
	// ByteBuffers General Purpose Pool
	/**
	 * Field _bufferPool.
	 */
	private final Queue<ByteBuffer> _bufferPool;
	/**
	 * Field _connections.
	 */
	private final List<MMOConnection<T>> _connections;
	
	/**
	 * Field ALL_SELECTORS.
	 */
	private static final List<SelectorThread> ALL_SELECTORS = new ArrayList<>();
	/**
	 * Field stats.
	 */
	private static SelectorStats stats = new SelectorStats();
	
	/**
	 * Constructor for SelectorThread.
	 * @param sc SelectorConfig
	 * @param packetHandler IPacketHandler<T>
	 * @param executor IMMOExecutor<T>
	 * @param clientFactory IClientFactory<T>
	 * @param acceptFilter IAcceptFilter
	 * @throws IOException
	 */
	public SelectorThread(SelectorConfig sc, IPacketHandler<T> packetHandler, IMMOExecutor<T> executor, IClientFactory<T> clientFactory, IAcceptFilter acceptFilter) throws IOException
	{
		synchronized (ALL_SELECTORS)
		{
			ALL_SELECTORS.add(this);
		}
		
		_sc = sc;
		_acceptFilter = acceptFilter;
		_packetHandler = packetHandler;
		_clientFactory = clientFactory;
		_executor = executor;
		
		_bufferPool = new ArrayDeque<>(_sc.HELPER_BUFFER_COUNT);
		_connections = new CopyOnWriteArrayList<>();
		
		DIRECT_WRITE_BUFFER = ByteBuffer.wrap(new byte[_sc.WRITE_BUFFER_SIZE]).order(_sc.BYTE_ORDER);
		WRITE_BUFFER = ByteBuffer.wrap(new byte[_sc.WRITE_BUFFER_SIZE]).order(_sc.BYTE_ORDER);
		READ_BUFFER = ByteBuffer.wrap(new byte[_sc.READ_BUFFER_SIZE]).order(_sc.BYTE_ORDER);
		HELPER_BUFFER_SIZE = Math.max(_sc.READ_BUFFER_SIZE, _sc.WRITE_BUFFER_SIZE);
		
		for (int i = 0; i < _sc.HELPER_BUFFER_COUNT; i++)
		{
			_bufferPool.add(ByteBuffer.wrap(new byte[HELPER_BUFFER_SIZE]).order(_sc.BYTE_ORDER));
		}
	}
	
	/**
	 * Method openServerSocket.
	 * @param address InetAddress
	 * @param tcpPort int
	 * @throws IOException
	 */
	public void openServerSocket(InetAddress address, int tcpPort) throws IOException
	{
		ServerSocketChannel selectable = ServerSocketChannel.open();
		selectable.configureBlocking(false);
		
		selectable.socket().bind(address == null ? new InetSocketAddress(tcpPort) : new InetSocketAddress(address, tcpPort));
		selectable.register(getSelector(), selectable.validOps());
		setName("SelectorThread:" + selectable.socket().getLocalPort());
	}
	
	/**
	 * Method getPooledBuffer.
	 * @return ByteBuffer
	 */
	protected ByteBuffer getPooledBuffer()
	{
		if (_bufferPool.isEmpty())
		{
			return ByteBuffer.wrap(new byte[HELPER_BUFFER_SIZE]).order(_sc.BYTE_ORDER);
		}
		return _bufferPool.poll();
	}
	
	/**
	 * Method recycleBuffer.
	 * @param buf ByteBuffer
	 */
	protected void recycleBuffer(ByteBuffer buf)
	{
		if (_bufferPool.size() < _sc.HELPER_BUFFER_COUNT)
		{
			buf.clear();
			_bufferPool.add(buf);
		}
	}
	
	/**
	 * Method freeBuffer.
	 * @param buf ByteBuffer
	 * @param con MMOConnection<T>
	 */
	protected void freeBuffer(ByteBuffer buf, MMOConnection<T> con)
	{
		if (buf == READ_BUFFER)
		{
			READ_BUFFER.clear();
		}
		else
		{
			con.setReadBuffer(null);
			recycleBuffer(buf);
		}
	}
	
	/**
	 * Method run.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{
		int totalKeys = 0;
		Set<SelectionKey> keys = null;
		Iterator<SelectionKey> itr = null;
		Iterator<MMOConnection<T>> conItr = null;
		SelectionKey key = null;
		MMOConnection<T> con = null;
		long currentMillis = 0L;
		
		// main loop
		for (;;)
		{
			try
			{
				
				if (isShuttingDown())
				{
					closeSelectorThread();
					break;
				}
				
				currentMillis = System.currentTimeMillis();
				
				conItr = _connections.iterator();
				while (conItr.hasNext())
				{
					con = conItr.next();
					if (con.isPengingClose())
					{
						if (!con.isPendingWrite() || ((currentMillis - con.getPendingCloseTime()) >= 10000L))
						{
							closeConnectionImpl(con);
							continue;
						}
					}
					if (con.isPendingWrite())
					{
						if ((currentMillis - con.getPendingWriteTime()) >= _sc.INTEREST_DELAY)
						{
							con.enableWriteInterest();
						}
					}
				}
				
				totalKeys = getSelector().selectNow();
				
				if (totalKeys > 0)
				{
					keys = getSelector().selectedKeys();
					itr = keys.iterator();
					
					while (itr.hasNext())
					{
						key = itr.next();
						itr.remove();
						
						if (key.isValid())
						{
							try
							{
								if (key.isAcceptable())
								{
									acceptConnection(key);
									continue;
								}
								else if (key.isConnectable())
								{
									finishConnection(key);
									continue;
								}
								
								if (key.isReadable())
								{
									readPacket(key);
								}
								if (key.isValid())
								{
									if (key.isWritable())
									{
										writePacket(key);
									}
								}
							}
							catch (CancelledKeyException cke)
							{
								
							}
						}
					}
				}
				
				try
				{
					Thread.sleep(_sc.SLEEP_TIME);
				}
				catch (InterruptedException ie)
				{
					
				}
			}
			catch (IOException e)
			{
				_log.error("Error in " + getName(), e);
				
				try
				{
					Thread.sleep(1000L);
				}
				catch (InterruptedException ie)
				{
					
				}
			}
		}
	}
	
	/**
	 * Method finishConnection.
	 * @param key SelectionKey
	 */
	protected void finishConnection(SelectionKey key)
	{
		try
		{
			((SocketChannel) key.channel()).finishConnect();
		}
		catch (IOException e)
		{
			MMOConnection<T> con = (MMOConnection<T>) key.attachment();
			T client = con.getClient();
			client.getConnection().onForcedDisconnection();
			closeConnectionImpl(client.getConnection());
		}
	}
	
	/**
	 * Method acceptConnection.
	 * @param key SelectionKey
	 */
	protected void acceptConnection(SelectionKey key)
	{
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		SocketChannel sc;
		SelectionKey clientKey;
		try
		{
			while ((sc = ssc.accept()) != null)
			{
				if ((getAcceptFilter() == null) || getAcceptFilter().accept(sc))
				{
					sc.configureBlocking(false);
					clientKey = sc.register(getSelector(), SelectionKey.OP_READ);
					
					MMOConnection<T> con = new MMOConnection<>(this, sc.socket(), clientKey);
					T client = getClientFactory().create(con);
					client.setConnection(con);
					con.setClient(client);
					clientKey.attach(con);
					
					_connections.add(con);
					stats.increaseOpenedConnections();
				}
				else
				{
					sc.close();
				}
			}
		}
		catch (IOException e)
		{
			_log.error("Error in " + getName(), e);
		}
	}
	
	/**
	 * Method readPacket.
	 * @param key SelectionKey
	 */
	protected void readPacket(SelectionKey key)
	{
		MMOConnection<T> con = (MMOConnection<T>) key.attachment();
		
		if (con.isClosed())
		{
			return;
		}
		
		ByteBuffer buf;
		int result = -2;
		
		if ((buf = con.getReadBuffer()) == null)
		{
			buf = READ_BUFFER;
		}
		
		// if we try to to do a read with no space in the buffer it will read 0 bytes
		// going into infinite loop
		if (buf.position() == buf.limit())
		{
			_log.error("Read buffer exhausted for client : " + con.getClient() + ", try to adjust buffer size, current : " + buf.capacity() + ", primary : " + (buf == READ_BUFFER) + ". Closing connection.");
			closeConnectionImpl(con);
		}
		else
		{
			
			try
			{
				result = con.getReadableByteChannel().read(buf);
			}
			catch (IOException e)
			{
				// error handling goes bellow
			}
			
			if (result > 0)
			{
				buf.flip();
				
				stats.increaseIncomingBytes(result);
				
				@SuppressWarnings("unused")
				int i;
				for (i = 0; this.tryReadPacket2(key, con, buf); i++)
				{
				}
			}
			else if (result == 0)
			{
				closeConnectionImpl(con);
			}
			else if (result == -1)
			{
				closeConnectionImpl(con);
			}
			else
			{
				con.onForcedDisconnection();
				closeConnectionImpl(con);
			}
		}
		
		if (buf == READ_BUFFER)
		{
			buf.clear();
		}
	}
	
	/**
	 * Method tryReadPacket2.
	 * @param key SelectionKey
	 * @param con MMOConnection<T>
	 * @param buf ByteBuffer
	 * @return boolean
	 */
	protected boolean tryReadPacket2(SelectionKey key, MMOConnection<T> con, ByteBuffer buf)
	{
		if (con.isClosed())
		{
			return false;
		}
		
		int pos = buf.position();
		if (buf.remaining() > _sc.HEADER_SIZE)
		{
			int size = buf.getShort() & 0xffff;
			
			if ((size <= _sc.HEADER_SIZE) || (size > _sc.PACKET_SIZE))
			{
				_log.error("Incorrect packet size : " + size + "! Client : " + con.getClient() + ". Closing connection.");
				closeConnectionImpl(con);
				return false;
			}
			
			size -= _sc.HEADER_SIZE;
			
			if (size <= buf.remaining())
			{
				stats.increaseIncomingPacketsCount();
				parseClientPacket(getPacketHandler(), buf, size, con);
				buf.position(pos + size + _sc.HEADER_SIZE);
				
				if (!buf.hasRemaining())
				{
					freeBuffer(buf, con);
					return false;
				}
				
				return true;
			}
			
			buf.position(pos);
		}
		
		if (pos == buf.capacity())
		{
			_log.warn("Read buffer exhausted for client : " + con.getClient() + ", try to adjust buffer size, current : " + buf.capacity() + ", primary : " + (buf == READ_BUFFER) + ".");
		}
		
		if (buf == READ_BUFFER)
		{
			allocateReadBuffer(con);
		}
		else
		{
			buf.compact();
		}
		
		return false;
	}
	
	/**
	 * Method allocateReadBuffer.
	 * @param con MMOConnection<T>
	 */
	protected void allocateReadBuffer(MMOConnection<T> con)
	{
		con.setReadBuffer(getPooledBuffer().put(READ_BUFFER));
		READ_BUFFER.clear();
	}
	
	/**
	 * Method parseClientPacket.
	 * @param handler IPacketHandler<T>
	 * @param buf ByteBuffer
	 * @param dataSize int
	 * @param con MMOConnection<T>
	 * @return boolean
	 */
	protected boolean parseClientPacket(IPacketHandler<T> handler, ByteBuffer buf, int dataSize, MMOConnection<T> con)
	{
		T client = con.getClient();
		
		int pos = buf.position();
		
		client.decrypt(buf, dataSize);
		buf.position(pos);
		
		if (buf.hasRemaining())
		{
			// apply limit
			int limit = buf.limit();
			buf.limit(pos + dataSize);
			ReceivablePacket<T> rp = handler.handlePacket(buf, client);
			
			if (rp != null)
			{
				rp.setByteBuffer(buf);
				rp.setClient(client);
				
				if (rp.read())
				{
					con.recvPacket(rp);
				}
				
				rp.setByteBuffer(null);
			}
			buf.limit(limit);
		}
		return true;
	}
	
	/**
	 * Method writePacket.
	 * @param key SelectionKey
	 */
	protected void writePacket(SelectionKey key)
	{
		MMOConnection<T> con = (MMOConnection<T>) key.attachment();
		
		prepareWriteBuffer(con);
		
		DIRECT_WRITE_BUFFER.flip();
		int size = DIRECT_WRITE_BUFFER.remaining();
		
		int result = -1;
		
		try
		{
			result = con.getWritableChannel().write(DIRECT_WRITE_BUFFER);
		}
		catch (IOException e)
		{
			// error handling goes on the if bellow
		}
		
		// check if no error happened
		if (result >= 0)
		{
			stats.increaseOutgoingBytes(result);
			
			// check if we written everything
			if (result != size)
			{
				con.createWriteBuffer(DIRECT_WRITE_BUFFER);
			}
			
			if (!con.getSendQueue().isEmpty() || con.hasPendingWriteBuffer())
			{
				con.scheduleWriteInterest();
			}
		}
		else
		{
			con.onForcedDisconnection();
			closeConnectionImpl(con);
		}
	}
	
	/**
	 * Method getWriteClient.
	 * @return T
	 */
	protected T getWriteClient()
	{
		return WRITE_CLIENT;
	}
	
	/**
	 * Method getWriteBuffer.
	 * @return ByteBuffer
	 */
	protected ByteBuffer getWriteBuffer()
	{
		return WRITE_BUFFER;
	}
	
	/**
	 * Method prepareWriteBuffer.
	 * @param con MMOConnection<T>
	 */
	protected void prepareWriteBuffer(MMOConnection<T> con)
	{
		WRITE_CLIENT = con.getClient();
		DIRECT_WRITE_BUFFER.clear();
		
		if (con.hasPendingWriteBuffer())
		{
			con.movePendingWriteBufferTo(DIRECT_WRITE_BUFFER);
		}
		
		if (DIRECT_WRITE_BUFFER.hasRemaining() && !con.hasPendingWriteBuffer())
		{
			int i;
			Queue<SendablePacket<T>> sendQueue = con.getSendQueue();
			SendablePacket<T> sp;
			
			for (i = 0; i < _sc.MAX_SEND_PER_PASS; i++)
			{
				synchronized (con)
				{
					if ((sp = sendQueue.poll()) == null)
					{
						break;
					}
				}
				
				try
				{
					stats.increaseOutgoingPacketsCount();
					putPacketIntoWriteBuffer(sp, true);
					WRITE_BUFFER.flip();
					if (DIRECT_WRITE_BUFFER.remaining() >= WRITE_BUFFER.limit())
					{
						DIRECT_WRITE_BUFFER.put(WRITE_BUFFER);
					}
					else
					{
						con.createWriteBuffer(WRITE_BUFFER);
						break;
					}
				}
				catch (Exception e)
				{
					_log.error("Error in " + getName(), e);
					break;
				}
			}
		}
		
		WRITE_BUFFER.clear();
		WRITE_CLIENT = null;
	}
	
	/**
	 * Method putPacketIntoWriteBuffer.
	 * @param sp SendablePacket<T>
	 * @param encrypt boolean
	 */
	protected final void putPacketIntoWriteBuffer(SendablePacket<T> sp, boolean encrypt)
	{
		WRITE_BUFFER.clear();
		
		// reserve space for the size
		int headerPos = WRITE_BUFFER.position();
		WRITE_BUFFER.position(headerPos + _sc.HEADER_SIZE);
		
		// write content to buffer
		sp.write();
		
		// size (incl header)
		int dataSize = WRITE_BUFFER.position() - headerPos - _sc.HEADER_SIZE;
		if (dataSize == 0)
		{
			WRITE_BUFFER.position(headerPos);
			return;
		}
		WRITE_BUFFER.position(headerPos + _sc.HEADER_SIZE);
		if (encrypt)
		{
			WRITE_CLIENT.encrypt(WRITE_BUFFER, dataSize);
			// recalculate size after encryption
			dataSize = WRITE_BUFFER.position() - headerPos - _sc.HEADER_SIZE;
		}
		
		// prepend header
		WRITE_BUFFER.position(headerPos);
		WRITE_BUFFER.putShort((short) (_sc.HEADER_SIZE + dataSize));
		WRITE_BUFFER.position(headerPos + _sc.HEADER_SIZE + dataSize);
	}
	
	/**
	 * Method getConfig.
	 * @return SelectorConfig
	 */
	protected SelectorConfig getConfig()
	{
		return _sc;
	}
	
	/**
	 * Method getSelector.
	 * @return Selector
	 */
	protected Selector getSelector()
	{
		return _selector;
	}
	
	/**
	 * Method getExecutor.
	 * @return IMMOExecutor<T>
	 */
	protected IMMOExecutor<T> getExecutor()
	{
		return _executor;
	}
	
	/**
	 * Method getPacketHandler.
	 * @return IPacketHandler<T>
	 */
	protected IPacketHandler<T> getPacketHandler()
	{
		return _packetHandler;
	}
	
	/**
	 * Method getClientFactory.
	 * @return IClientFactory<T>
	 */
	protected IClientFactory<T> getClientFactory()
	{
		return _clientFactory;
	}
	
	/**
	 * Method setAcceptFilter.
	 * @param acceptFilter IAcceptFilter
	 */
	public void setAcceptFilter(IAcceptFilter acceptFilter)
	{
		_acceptFilter = acceptFilter;
	}
	
	/**
	 * Method getAcceptFilter.
	 * @return IAcceptFilter
	 */
	protected IAcceptFilter getAcceptFilter()
	{
		return _acceptFilter;
	}
	
	/**
	 * Method closeConnectionImpl.
	 * @param con MMOConnection<T>
	 */
	protected void closeConnectionImpl(MMOConnection<T> con)
	{
		try
		{
			// notify connection
			con.onDisconnection();
		}
		finally
		{
			try
			{
				// close socket and the SocketChannel
				con.close();
			}
			catch (IOException e)
			{
				// ignore, we are closing anyway
			}
			finally
			{
				con.releaseBuffers();
				con.clearQueues();
				con.getClient().setConnection(null);
				con.getSelectionKey().attach(null);
				con.getSelectionKey().cancel();
				
				_connections.remove(con);
				
				stats.decreseOpenedConnections();
			}
		}
	}
	
	/**
	 * Method shutdown.
	 */
	public void shutdown()
	{
		_shutdown = true;
	}
	
	/**
	 * Method isShuttingDown.
	 * @return boolean
	 */
	public boolean isShuttingDown()
	{
		return _shutdown;
	}
	
	/**
	 * Method closeAllChannels.
	 */
	protected void closeAllChannels()
	{
		Set<SelectionKey> keys = getSelector().keys();
		for (SelectionKey key : keys)
		{
			try
			{
				key.channel().close();
			}
			catch (IOException e)
			{
				// ignore
			}
		}
	}
	
	/**
	 * Method closeSelectorThread.
	 */
	protected void closeSelectorThread()
	{
		closeAllChannels();
		
		try
		{
			getSelector().close();
		}
		catch (IOException e)
		{
			// Ignore
		}
	}
	
	/**
	 * Method getStats.
	 * @return CharSequence
	 */
	public static CharSequence getStats()
	{
		StringBuilder list = new StringBuilder();
		
		list.append("selectorThreadCount: .... ").append(ALL_SELECTORS.size()).append('\n');
		list.append("=================================================\n");
		list.append("getTotalConnections: .... ").append(stats.getTotalConnections()).append('\n');
		list.append("getCurrentConnections: .. ").append(stats.getCurrentConnections()).append('\n');
		list.append("getMaximumConnections: .. ").append(stats.getMaximumConnections()).append('\n');
		list.append("getIncomingBytesTotal: .. ").append(stats.getIncomingBytesTotal()).append('\n');
		list.append("getOutgoingBytesTotal: .. ").append(stats.getOutgoingBytesTotal()).append('\n');
		list.append("getIncomingPacketsTotal:  ").append(stats.getIncomingPacketsTotal()).append('\n');
		list.append("getOutgoingPacketsTotal:  ").append(stats.getOutgoingPacketsTotal()).append('\n');
		list.append("getMaxBytesPerRead: ..... ").append(stats.getMaxBytesPerRead()).append('\n');
		list.append("getMaxBytesPerWrite: .... ").append(stats.getMaxBytesPerWrite()).append('\n');
		list.append("=================================================\n");
		
		return list;
	}
}