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

import java.nio.ByteBuffer;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("rawtypes")
public abstract class ReceivablePacket<T extends MMOClient> extends lineage2.commons.net.nio.ReceivablePacket<T>
{
	/**
	 * Field _client.
	 */
	protected T _client;
	/**
	 * Field _buf.
	 */
	protected ByteBuffer _buf;
	
	/**
	 * Method setByteBuffer.
	 * @param buf ByteBuffer
	 */
	protected void setByteBuffer(ByteBuffer buf)
	{
		_buf = buf;
	}
	
	/**
	 * Method getByteBuffer.
	 * @return ByteBuffer
	 */
	@Override
	protected ByteBuffer getByteBuffer()
	{
		return _buf;
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
	@Override
	public T getClient()
	{
		return _client;
	}
	
	/**
	 * Method read.
	 * @return boolean
	 */
	@Override
	protected abstract boolean read();
}
