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
package lineage2.loginserver.gameservercon.gspackets;

import lineage2.loginserver.gameservercon.GameServer;
import lineage2.loginserver.gameservercon.ReceivablePacket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PingResponse extends ReceivablePacket
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(PingResponse.class);
	/**
	 * Field _serverTime.
	 */
	private long _serverTime;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_serverTime = readQ();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		GameServer gameServer = getGameServer();
		if (!gameServer.isAuthed())
		{
			return;
		}
		gameServer.getConnection().onPingResponse();
		long diff = System.currentTimeMillis() - _serverTime;
		if (Math.abs(diff) > 999)
		{
			_log.warn("Gameserver " + gameServer.getId() + " [" + gameServer.getName() + "] : time offset " + diff + " ms.");
		}
	}
}
