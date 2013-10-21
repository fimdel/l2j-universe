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
package lineage2.gameserver.network.clientpackets;

import java.nio.BufferUnderflowException;
import java.util.List;

import lineage2.commons.net.nio.impl.ReceivablePacket;
import lineage2.gameserver.GameServer;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.GameClient;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class L2GameClientPacket extends ReceivablePacket<GameClient>
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(L2GameClientPacket.class);
	
	/**
	 * Method read.
	 * @return boolean
	 */
	@Override
	public final boolean read()
	{
		try
		{
			readImpl();
			return true;
		}
		catch (BufferUnderflowException e)
		{
			_client.onPacketReadFail();
			_log.error("Client: " + _client + " - Failed reading: " + getType() + " - Server Version: " + GameServer.getInstance().getVersion().getRevisionNumber(), e);
		}
		catch (Exception e)
		{
			_log.error("Client: " + _client + " - Failed reading: " + getType() + " - Server Version: " + GameServer.getInstance().getVersion().getRevisionNumber(), e);
		}
		return false;
	}
	
	/**
	 * Method readImpl.
	 * @throws Exception
	 */
	protected abstract void readImpl() throws Exception;
	
	/**
	 * Method run.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public final void run()
	{
		GameClient client = getClient();
		try
		{
			runImpl();
		}
		catch (Exception e)
		{
			_log.error("Client: " + client + " - Failed running: " + getType() + " - Server Version: " + GameServer.getInstance().getVersion().getRevisionNumber(), e);
		}
	}
	
	/**
	 * Method runImpl.
	 * @throws Exception
	 */
	protected abstract void runImpl() throws Exception;
	
	/**
	 * Method readS.
	 * @param len int
	 * @return String
	 */
	protected String readS(int len)
	{
		String ret = readS();
		return ret.length() > len ? ret.substring(0, len) : ret;
	}
	
	/**
	 * Method sendPacket.
	 * @param packet L2GameServerPacket
	 */
	protected void sendPacket(L2GameServerPacket packet)
	{
		getClient().sendPacket(packet);
	}
	
	/**
	 * Method sendPacket.
	 * @param packets L2GameServerPacket[]
	 */
	protected void sendPacket(L2GameServerPacket... packets)
	{
		getClient().sendPacket(packets);
	}
	
	/**
	 * Method sendPackets.
	 * @param packets List<L2GameServerPacket>
	 */
	protected void sendPackets(List<L2GameServerPacket> packets)
	{
		getClient().sendPackets(packets);
	}
	
	/**
	 * Method getType.
	 * @return String
	 */
	public String getType()
	{
		return "[C] " + getClass().getSimpleName();
	}

	public Player getActiveChar()
	{
		return getClient().getActiveChar();
	}
}
