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

import java.nio.ByteBuffer;

import lineage2.gameserver.network.loginservercon.lspackets.AuthResponse;
import lineage2.gameserver.network.loginservercon.lspackets.ChangePasswordResponse;
import lineage2.gameserver.network.loginservercon.lspackets.GetAccountInfo;
import lineage2.gameserver.network.loginservercon.lspackets.KickPlayer;
import lineage2.gameserver.network.loginservercon.lspackets.LoginServerFail;
import lineage2.gameserver.network.loginservercon.lspackets.PingRequest;
import lineage2.gameserver.network.loginservercon.lspackets.PlayerAuthResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PacketHandler
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(PacketHandler.class);
	
	/**
	 * Method handlePacket.
	 * @param buf ByteBuffer
	 * @return ReceivablePacket
	 */
	public static ReceivablePacket handlePacket(ByteBuffer buf)
	{
		ReceivablePacket packet = null;
		int id = buf.get() & 0xff;
		switch (id)
		{
			case 0x00:
				packet = new AuthResponse();
				break;
			case 0x01:
				packet = new LoginServerFail();
				break;
			case 0x02:
				packet = new PlayerAuthResponse();
				break;
			case 0x03:
				packet = new KickPlayer();
				break;
			case 0x04:
				packet = new GetAccountInfo();
				break;
			case 0x06:
				packet = new ChangePasswordResponse();
				break;
			case 0xff:
				packet = new PingRequest();
				break;
			default:
				_log.error("Received unknown packet: " + Integer.toHexString(id));
		}
		return packet;
	}
}
