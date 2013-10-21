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
public abstract class SendablePacket<T extends MMOClient> extends lineage2.commons.net.nio.SendablePacket<T>
{
	/**
	 * Method getByteBuffer.
	 * @return ByteBuffer
	 */
	@Override
	protected ByteBuffer getByteBuffer()
	{
		return ((SelectorThread) Thread.currentThread()).getWriteBuffer();
	}
	
	/**
	 * Method getClient.
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T getClient()
	{
		return (T) ((SelectorThread) Thread.currentThread()).getWriteClient();
	}
	
	/**
	 * Method write.
	 * @return boolean
	 */
	@Override
	protected abstract boolean write();
}
