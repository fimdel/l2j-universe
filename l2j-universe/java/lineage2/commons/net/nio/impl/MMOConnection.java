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
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("rawtypes")
public class MMOConnection<T extends MMOClient>
{
	/**
	 * Field _selectorThread.
	 */
	private final SelectorThread<T> _selectorThread;
	/**
	 * Field _selectionKey.
	 */
	private final SelectionKey _selectionKey;
	/**
	 * Field _socket.
	 */
	private final Socket _socket;
	/**
	 * Field _writableByteChannel.
	 */
	private final WritableByteChannel _writableByteChannel;
	/**
	 * Field _readableByteChannel.
	 */
	private final ReadableByteChannel _readableByteChannel;
	/**
	 * Field _sendQueue.
	 */
	private final Queue<SendablePacket<T>> _sendQueue;
	/**
	 * Field _recvQueue.
	 */
	private final Queue<ReceivablePacket<T>> _recvQueue;
	/**
	 * Field _client.
	 */
	private T _client;
	/**
	 * Field _secondaryWriteBuffer. Field _primaryWriteBuffer. Field _readBuffer.
	 */
	private ByteBuffer _readBuffer, _primaryWriteBuffer, _secondaryWriteBuffer;
	/**
	 * Field _pendingClose.
	 */
	private boolean _pendingClose;
	/**
	 * Field _pendingCloseTime.
	 */
	private long _pendingCloseTime;
	/**
	 * Field _closed.
	 */
	private boolean _closed;
	/**
	 * Field _pendingWriteTime.
	 */
	private long _pendingWriteTime;
	/**
	 * Field _isPengingWrite.
	 */
	private final AtomicBoolean _isPengingWrite = new AtomicBoolean();
	
	/**
	 * Constructor for MMOConnection.
	 * @param selectorThread SelectorThread<T>
	 * @param socket Socket
	 * @param key SelectionKey
	 */
	public MMOConnection(SelectorThread<T> selectorThread, Socket socket, SelectionKey key)
	{
		_selectorThread = selectorThread;
		_selectionKey = key;
		_socket = socket;
		_writableByteChannel = socket.getChannel();
		_readableByteChannel = socket.getChannel();
		_sendQueue = new ArrayDeque<>();
		_recvQueue = new MMOExecutableQueue<>(selectorThread.getExecutor());
	}
	
	/**
	 * Method setClient.
	 * @param client T
	 */
	protected void setClient(T client)
	{
		_client = client;
	}
	
	/**
	 * Method getClient.
	 * @return T
	 */
	public T getClient()
	{
		return _client;
	}
	
	/**
	 * Method recvPacket.
	 * @param rp ReceivablePacket<T>
	 */
	public void recvPacket(ReceivablePacket<T> rp)
	{
		if (rp == null)
		{
			return;
		}
		if (isClosed())
		{
			return;
		}
		_recvQueue.add(rp);
	}
	
	/**
	 * Method sendPacket.
	 * @param sp SendablePacket<T>
	 */
	public void sendPacket(SendablePacket<T> sp)
	{
		if (sp == null)
		{
			return;
		}
		synchronized (this)
		{
			if (isClosed())
			{
				return;
			}
			_sendQueue.add(sp);
		}
		scheduleWriteInterest();
	}
	
	/**
	 * Method sendPacket.
	 * @param args SendablePacket<T>[]
	 */
	@SuppressWarnings("unchecked")
	public void sendPacket(SendablePacket<T>... args)
	{
		if ((args == null) || (args.length == 0))
		{
			return;
		}
		synchronized (this)
		{
			if (isClosed())
			{
				return;
			}
			for (SendablePacket<T> sp : args)
			{
				if (sp != null)
				{
					_sendQueue.add(sp);
				}
			}
		}
		scheduleWriteInterest();
	}
	
	/**
	 * Method sendPackets.
	 * @param args List<? extends SendablePacket<T>>
	 */
	public void sendPackets(List<? extends SendablePacket<T>> args)
	{
		if ((args == null) || args.isEmpty())
		{
			return;
		}
		SendablePacket<T> sp;
		synchronized (this)
		{
			if (isClosed())
			{
				return;
			}
			for (int i = 0; i < args.size(); i++)
			{
				if ((sp = args.get(i)) != null)
				{
					_sendQueue.add(sp);
				}
			}
		}
		scheduleWriteInterest();
	}
	
	/**
	 * Method getSelectionKey.
	 * @return SelectionKey
	 */
	protected SelectionKey getSelectionKey()
	{
		return _selectionKey;
	}
	
	/**
	 * Method disableReadInterest.
	 */
	protected void disableReadInterest()
	{
		try
		{
			_selectionKey.interestOps(_selectionKey.interestOps() & ~SelectionKey.OP_READ);
		}
		catch (CancelledKeyException e)
		{
		}
	}
	
	/**
	 * Method scheduleWriteInterest.
	 */
	protected void scheduleWriteInterest()
	{
		try
		{
			if (_isPengingWrite.compareAndSet(false, true))
			{
				_pendingWriteTime = System.currentTimeMillis();
			}
		}
		catch (CancelledKeyException e)
		{
		}
	}
	
	/**
	 * Method disableWriteInterest.
	 */
	protected void disableWriteInterest()
	{
		try
		{
			if (_isPengingWrite.compareAndSet(true, false))
			{
				_selectionKey.interestOps(_selectionKey.interestOps() & ~SelectionKey.OP_WRITE);
			}
		}
		catch (CancelledKeyException e)
		{
		}
	}
	
	/**
	 * Method enableWriteInterest.
	 */
	protected void enableWriteInterest()
	{
		if (_isPengingWrite.compareAndSet(true, false))
		{
			_selectionKey.interestOps(_selectionKey.interestOps() | SelectionKey.OP_WRITE);
		}
	}
	
	/**
	 * Method isPendingWrite.
	 * @return boolean
	 */
	protected boolean isPendingWrite()
	{
		return _isPengingWrite.get();
	}
	
	/**
	 * Method getPendingWriteTime.
	 * @return long
	 */
	public long getPendingWriteTime()
	{
		return _pendingWriteTime;
	}
	
	/**
	 * Method getSocket.
	 * @return Socket
	 */
	public Socket getSocket()
	{
		return _socket;
	}
	
	/**
	 * Method getWritableChannel.
	 * @return WritableByteChannel
	 */
	public WritableByteChannel getWritableChannel()
	{
		return _writableByteChannel;
	}
	
	/**
	 * Method getReadableByteChannel.
	 * @return ReadableByteChannel
	 */
	public ReadableByteChannel getReadableByteChannel()
	{
		return _readableByteChannel;
	}
	
	/**
	 * Method getSendQueue.
	 * @return Queue<SendablePacket<T>>
	 */
	protected Queue<SendablePacket<T>> getSendQueue()
	{
		return _sendQueue;
	}
	
	/**
	 * Method getRecvQueue.
	 * @return Queue<ReceivablePacket<T>>
	 */
	protected Queue<ReceivablePacket<T>> getRecvQueue()
	{
		return _recvQueue;
	}
	
	/**
	 * Method createWriteBuffer.
	 * @param buf ByteBuffer
	 */
	protected void createWriteBuffer(ByteBuffer buf)
	{
		if (_primaryWriteBuffer == null)
		{
			_primaryWriteBuffer = _selectorThread.getPooledBuffer();
			_primaryWriteBuffer.put(buf);
		}
		else
		{
			ByteBuffer temp = _selectorThread.getPooledBuffer();
			temp.put(buf);
			int remaining = temp.remaining();
			_primaryWriteBuffer.flip();
			int limit = _primaryWriteBuffer.limit();
			if (remaining >= _primaryWriteBuffer.remaining())
			{
				temp.put(_primaryWriteBuffer);
				_selectorThread.recycleBuffer(_primaryWriteBuffer);
				_primaryWriteBuffer = temp;
			}
			else
			{
				_primaryWriteBuffer.limit(remaining);
				temp.put(_primaryWriteBuffer);
				_primaryWriteBuffer.limit(limit);
				_primaryWriteBuffer.compact();
				_secondaryWriteBuffer = _primaryWriteBuffer;
				_primaryWriteBuffer = temp;
			}
		}
	}
	
	/**
	 * Method hasPendingWriteBuffer.
	 * @return boolean
	 */
	protected boolean hasPendingWriteBuffer()
	{
		return _primaryWriteBuffer != null;
	}
	
	/**
	 * Method movePendingWriteBufferTo.
	 * @param dest ByteBuffer
	 */
	protected void movePendingWriteBufferTo(ByteBuffer dest)
	{
		_primaryWriteBuffer.flip();
		dest.put(_primaryWriteBuffer);
		_selectorThread.recycleBuffer(_primaryWriteBuffer);
		_primaryWriteBuffer = _secondaryWriteBuffer;
		_secondaryWriteBuffer = null;
	}
	
	/**
	 * Method setReadBuffer.
	 * @param buf ByteBuffer
	 */
	protected void setReadBuffer(ByteBuffer buf)
	{
		_readBuffer = buf;
	}
	
	/**
	 * Method getReadBuffer.
	 * @return ByteBuffer
	 */
	public ByteBuffer getReadBuffer()
	{
		return _readBuffer;
	}
	
	/**
	 * Method isClosed.
	 * @return boolean
	 */
	public boolean isClosed()
	{
		return _pendingClose || _closed;
	}
	
	/**
	 * Method isPengingClose.
	 * @return boolean
	 */
	public boolean isPengingClose()
	{
		return _pendingClose;
	}
	
	/**
	 * Method getPendingCloseTime.
	 * @return long
	 */
	public long getPendingCloseTime()
	{
		return _pendingCloseTime;
	}
	
	/**
	 * Method close.
	 * @throws IOException
	 */
	protected void close() throws IOException
	{
		_closed = true;
		_socket.close();
	}
	
	/**
	 * Method closeNow.
	 */
	protected void closeNow()
	{
		synchronized (this)
		{
			if (isClosed())
			{
				return;
			}
			_sendQueue.clear();
			_pendingClose = true;
			_pendingCloseTime = System.currentTimeMillis();
		}
		disableReadInterest();
		disableWriteInterest();
	}
	
	/**
	 * Method close.
	 * @param sp SendablePacket<T>
	 */
	public void close(SendablePacket<T> sp)
	{
		synchronized (this)
		{
			if (isClosed())
			{
				return;
			}
			_sendQueue.clear();
			sendPacket(sp);
			_pendingClose = true;
			_pendingCloseTime = System.currentTimeMillis();
		}
		disableReadInterest();
	}
	
	/**
	 * Method closeLater.
	 */
	protected void closeLater()
	{
		synchronized (this)
		{
			if (isClosed())
			{
				return;
			}
			_pendingClose = true;
			_pendingCloseTime = System.currentTimeMillis();
		}
	}
	
	/**
	 * Method releaseBuffers.
	 */
	protected void releaseBuffers()
	{
		if (_primaryWriteBuffer != null)
		{
			_selectorThread.recycleBuffer(_primaryWriteBuffer);
			_primaryWriteBuffer = null;
			if (_secondaryWriteBuffer != null)
			{
				_selectorThread.recycleBuffer(_secondaryWriteBuffer);
				_secondaryWriteBuffer = null;
			}
		}
		if (_readBuffer != null)
		{
			_selectorThread.recycleBuffer(_readBuffer);
			_readBuffer = null;
		}
	}
	
	/**
	 * Method clearQueues.
	 */
	protected void clearQueues()
	{
		_sendQueue.clear();
		_recvQueue.clear();
	}
	
	/**
	 * Method onDisconnection.
	 */
	protected void onDisconnection()
	{
		getClient().onDisconnection();
	}
	
	/**
	 * Method onForcedDisconnection.
	 */
	protected void onForcedDisconnection()
	{
		getClient().onForcedDisconnection();
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "MMOConnection: selector=" + _selectorThread + "; client=" + getClient();
	}
}
