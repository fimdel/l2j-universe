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

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SelectorStats
{
	/**
	 * Field _connectionsTotal.
	 */
	private final AtomicLong _connectionsTotal = new AtomicLong();
	/**
	 * Field _connectionsCurrent.
	 */
	private final AtomicLong _connectionsCurrent = new AtomicLong();
	/**
	 * Field _connectionsMax.
	 */
	private final AtomicLong _connectionsMax = new AtomicLong();
	/**
	 * Field _incomingBytesTotal.
	 */
	private final AtomicLong _incomingBytesTotal = new AtomicLong();
	/**
	 * Field _outgoingBytesTotal.
	 */
	private final AtomicLong _outgoingBytesTotal = new AtomicLong();
	/**
	 * Field _incomingPacketsTotal.
	 */
	private final AtomicLong _incomingPacketsTotal = new AtomicLong();
	/**
	 * Field _outgoingPacketsTotal.
	 */
	private final AtomicLong _outgoingPacketsTotal = new AtomicLong();
	/**
	 * Field _bytesMaxPerRead.
	 */
	private final AtomicLong _bytesMaxPerRead = new AtomicLong();
	/**
	 * Field _bytesMaxPerWrite.
	 */
	private final AtomicLong _bytesMaxPerWrite = new AtomicLong();
	
	/**
	 * Method increaseOpenedConnections.
	 */
	public void increaseOpenedConnections()
	{
		if (_connectionsCurrent.incrementAndGet() > _connectionsMax.get())
		{
			_connectionsMax.incrementAndGet();
		}
		_connectionsTotal.incrementAndGet();
	}
	
	/**
	 * Method decreseOpenedConnections.
	 */
	public void decreseOpenedConnections()
	{
		_connectionsCurrent.decrementAndGet();
	}
	
	/**
	 * Method increaseIncomingBytes.
	 * @param size int
	 */
	public void increaseIncomingBytes(int size)
	{
		if (size > _bytesMaxPerRead.get())
		{
			_bytesMaxPerRead.set(size);
		}
		_incomingBytesTotal.addAndGet(size);
	}
	
	/**
	 * Method increaseOutgoingBytes.
	 * @param size int
	 */
	public void increaseOutgoingBytes(int size)
	{
		if (size > _bytesMaxPerWrite.get())
		{
			_bytesMaxPerWrite.set(size);
		}
		_outgoingBytesTotal.addAndGet(size);
	}
	
	/**
	 * Method increaseIncomingPacketsCount.
	 */
	public void increaseIncomingPacketsCount()
	{
		_incomingPacketsTotal.incrementAndGet();
	}
	
	/**
	 * Method increaseOutgoingPacketsCount.
	 */
	public void increaseOutgoingPacketsCount()
	{
		_outgoingPacketsTotal.incrementAndGet();
	}
	
	/**
	 * Method getTotalConnections.
	 * @return long
	 */
	public long getTotalConnections()
	{
		return _connectionsTotal.get();
	}
	
	/**
	 * Method getCurrentConnections.
	 * @return long
	 */
	public long getCurrentConnections()
	{
		return _connectionsCurrent.get();
	}
	
	/**
	 * Method getMaximumConnections.
	 * @return long
	 */
	public long getMaximumConnections()
	{
		return _connectionsMax.get();
	}
	
	/**
	 * Method getIncomingBytesTotal.
	 * @return long
	 */
	public long getIncomingBytesTotal()
	{
		return _incomingBytesTotal.get();
	}
	
	/**
	 * Method getOutgoingBytesTotal.
	 * @return long
	 */
	public long getOutgoingBytesTotal()
	{
		return _outgoingBytesTotal.get();
	}
	
	/**
	 * Method getIncomingPacketsTotal.
	 * @return long
	 */
	public long getIncomingPacketsTotal()
	{
		return _incomingPacketsTotal.get();
	}
	
	/**
	 * Method getOutgoingPacketsTotal.
	 * @return long
	 */
	public long getOutgoingPacketsTotal()
	{
		return _outgoingPacketsTotal.get();
	}
	
	/**
	 * Method getMaxBytesPerRead.
	 * @return long
	 */
	public long getMaxBytesPerRead()
	{
		return _bytesMaxPerRead.get();
	}
	
	/**
	 * Method getMaxBytesPerWrite.
	 * @return long
	 */
	public long getMaxBytesPerWrite()
	{
		return _bytesMaxPerWrite.get();
	}
}
