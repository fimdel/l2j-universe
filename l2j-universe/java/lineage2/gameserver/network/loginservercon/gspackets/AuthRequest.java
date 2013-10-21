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
package lineage2.gameserver.network.loginservercon.gspackets;

import lineage2.gameserver.Config;
import lineage2.gameserver.GameServer;
import lineage2.gameserver.network.loginservercon.SendablePacket;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AuthRequest extends SendablePacket
{
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x00);
		writeD(GameServer.LOGIN_SERVER_PROTOCOL);
		writeC(Config.REQUEST_ID);
		writeC(Config.ACCEPT_ALTERNATE_ID ? 0x01 : 0x00);
		writeD(Config.LOGIN_SERVER_SERVER_TYPE);
		writeD(Config.LOGIN_SERVER_AGE_LIMIT);
		writeC(Config.LOGIN_SERVER_GM_ONLY ? 0x01 : 0x00);
		writeC(Config.LOGIN_SERVER_BRACKETS ? 0x01 : 0x00);
		writeC(Config.LOGIN_SERVER_IS_PVP ? 0x01 : 0x00);
		writeS(Config.EXTERNAL_HOSTNAME);
		writeS(Config.INTERNAL_HOSTNAME);
		writeH(Config.PORTS_GAME.length);
		for (int PORT_GAME : Config.PORTS_GAME)
		{
			writeH(PORT_GAME);
		}
		writeD(Config.MAXIMUM_ONLINE_USERS);
	}
}
