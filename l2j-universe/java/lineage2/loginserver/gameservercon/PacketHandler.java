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
package lineage2.loginserver.gameservercon;

import java.nio.ByteBuffer;

import lineage2.loginserver.gameservercon.gspackets.AuthRequest;
import lineage2.loginserver.gameservercon.gspackets.BonusRequest;
import lineage2.loginserver.gameservercon.gspackets.ChangeAccessLevel;
import lineage2.loginserver.gameservercon.gspackets.ChangePassword;
import lineage2.loginserver.gameservercon.gspackets.OnlineStatus;
import lineage2.loginserver.gameservercon.gspackets.PingResponse;
import lineage2.loginserver.gameservercon.gspackets.Player2ndAuthSetAttempts;
import lineage2.loginserver.gameservercon.gspackets.Player2ndAuthSetBanTime;
import lineage2.loginserver.gameservercon.gspackets.Player2ndAuthSetPassword;
import lineage2.loginserver.gameservercon.gspackets.PlayerAuthRequest;
import lineage2.loginserver.gameservercon.gspackets.PlayerInGame;
import lineage2.loginserver.gameservercon.gspackets.PlayerLogout;
import lineage2.loginserver.gameservercon.gspackets.SetAccountInfo;

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
	private static Logger _log = LoggerFactory.getLogger(PacketHandler.class);
	
	/**
	 * Method handlePacket.
	 * @param gs GameServer
	 * @param buf ByteBuffer
	 * @return ReceivablePacket
	 */
	public static ReceivablePacket handlePacket(GameServer gs, ByteBuffer buf)
	{
		ReceivablePacket packet = null;
		int id = buf.get() & 0xff;
		if (!gs.isAuthed())
		{
			switch (id)
			{
				case 0x00:
					packet = new AuthRequest();
					break;
				default:
					_log.error("Received unknown packet: " + Integer.toHexString(id));
			}
		}
		else
		{
			switch (id)
			{
				case 0x01:
					packet = new OnlineStatus();
					break;
				case 0x02:
					packet = new PlayerAuthRequest();
					break;
				case 0x03:
					packet = new PlayerInGame();
					break;
				case 0x04:
					packet = new PlayerLogout();
					break;
				case 0x05:
					packet = new SetAccountInfo();
					break;
				case 0x08:
					packet = new ChangePassword();
					break;
				case 0x10:
					packet = new BonusRequest();
					break;
				case 0x11:
					packet = new ChangeAccessLevel();
					break;
				case 0x15:
					packet = new Player2ndAuthSetPassword();
					break;
				case 0x16:
					packet = new Player2ndAuthSetAttempts();
					break;
				case 0x17:
					packet = new Player2ndAuthSetBanTime();
					break;
				case 0xff:
					packet = new PingResponse();
					break;
				default:
					_log.error("Received unknown packet: " + Integer.toHexString(id));
			}
		}
		return packet;
	}
}
