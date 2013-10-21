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
public abstract class MMOClient<T extends MMOConnection>
{
	/**
	 * Field _connection.
	 */
	private T _connection;
	/**
	 * Field isAuthed.
	 */
	private boolean isAuthed;
	
	/**
	 * Constructor for MMOClient.
	 * @param con T
	 */
	public MMOClient(T con)
	{
		_connection = con;
	}
	
	/**
	 * Method setConnection.
	 * @param con T
	 */
	protected void setConnection(T con)
	{
		_connection = con;
	}
	
	/**
	 * Method getConnection.
	 * @return T
	 */
	public T getConnection()
	{
		return _connection;
	}
	
	/**
	 * Method isAuthed.
	 * @return boolean
	 */
	public boolean isAuthed()
	{
		return isAuthed;
	}
	
	/**
	 * Method setAuthed.
	 * @param isAuthed boolean
	 */
	public void setAuthed(boolean isAuthed)
	{
		this.isAuthed = isAuthed;
	}
	
	/**
	 * Method closeNow.
	 * @param error boolean
	 */
	public void closeNow(boolean error)
	{
		if (isConnected())
		{
			_connection.closeNow();
		}
	}
	
	/**
	 * Method closeLater.
	 */
	public void closeLater()
	{
		if (isConnected())
		{
			_connection.closeLater();
		}
	}
	
	/**
	 * Method isConnected.
	 * @return boolean
	 */
	public boolean isConnected()
	{
		return (_connection != null) && !_connection.isClosed();
	}
	
	/**
	 * Method decrypt.
	 * @param buf ByteBuffer
	 * @param size int
	 * @return boolean
	 */
	public abstract boolean decrypt(ByteBuffer buf, int size);
	
	/**
	 * Method encrypt.
	 * @param buf ByteBuffer
	 * @param size int
	 * @return boolean
	 */
	public abstract boolean encrypt(ByteBuffer buf, int size);
	
	/**
	 * Method onDisconnection.
	 */
	protected void onDisconnection()
	{
	}
	
	/**
	 * Method onForcedDisconnection.
	 */
	protected void onForcedDisconnection()
	{
	}
}
