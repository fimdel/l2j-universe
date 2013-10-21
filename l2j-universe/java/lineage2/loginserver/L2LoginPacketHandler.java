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
package lineage2.loginserver;

import java.nio.ByteBuffer;

import lineage2.commons.net.nio.impl.IPacketHandler;
import lineage2.commons.net.nio.impl.ReceivablePacket;
import lineage2.loginserver.L2LoginClient.LoginClientState;
import lineage2.loginserver.clientpackets.AuthGameGuard;
import lineage2.loginserver.clientpackets.RequestAuthLogin;
import lineage2.loginserver.clientpackets.RequestServerList;
import lineage2.loginserver.clientpackets.RequestServerLogin;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class L2LoginPacketHandler implements IPacketHandler<L2LoginClient>
{
	/**
	 * Method handlePacket.
	 * @param buf ByteBuffer
	 * @param client L2LoginClient
	 * @return ReceivablePacket<L2LoginClient>
	 */
	@Override
	public ReceivablePacket<L2LoginClient> handlePacket(ByteBuffer buf, L2LoginClient client)
	{
		int opcode = buf.get() & 0xFF;
		ReceivablePacket<L2LoginClient> packet = null;
		LoginClientState state = client.getState();
		switch (state)
		{
			case CONNECTED:
				if (opcode == 0x07)
				{
					packet = new AuthGameGuard();
				}
				break;
			case AUTHED_GG:
				if (opcode == 0x00)
				{
					packet = new RequestAuthLogin();
				}
				break;
			case AUTHED:
				if (opcode == 0x05)
				{
					packet = new RequestServerList();
				}
				else if (opcode == 0x02)
				{
					packet = new RequestServerLogin();
				}
				break;
			default:
				break;
		}
		return packet;
	}
}
