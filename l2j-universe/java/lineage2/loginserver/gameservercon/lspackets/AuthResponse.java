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
package lineage2.loginserver.gameservercon.lspackets;

import lineage2.loginserver.gameservercon.GameServer;
import lineage2.loginserver.gameservercon.SendablePacket;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AuthResponse extends SendablePacket
{
	/**
	 * Field serverId.
	 */
	private final int serverId;
	/**
	 * Field name.
	 */
	private final String name;
	
	/**
	 * Constructor for AuthResponse.
	 * @param gs GameServer
	 */
	public AuthResponse(GameServer gs)
	{
		serverId = gs.getId();
		name = gs.getName();
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x00);
		writeC(serverId);
		writeS(name);
	}
}
