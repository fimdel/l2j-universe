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
package lineage2.loginserver.serverpackets;

import lineage2.loginserver.L2LoginClient;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Init extends L2LoginServerPacket
{
	/**
	 * Field _sessionId.
	 */
	private final int _sessionId;
	/**
	 * Field _publicKey.
	 */
	private final byte[] _publicKey;
	/**
	 * Field _blowfishKey.
	 */
	private final byte[] _blowfishKey;
	
	/**
	 * Constructor for Init.
	 * @param client L2LoginClient
	 */
	public Init(L2LoginClient client)
	{
		this(client.getScrambledModulus(), client.getBlowfishKey(), client.getSessionId());
	}
	
	/**
	 * Constructor for Init.
	 * @param publickey byte[]
	 * @param blowfishkey byte[]
	 * @param sessionId int
	 */
	public Init(byte[] publickey, byte[] blowfishkey, int sessionId)
	{
		_sessionId = sessionId;
		_publicKey = publickey;
		_blowfishKey = blowfishkey;
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x00);
		writeD(_sessionId);
		writeD(0x0000c621);
		writeB(_publicKey);
		writeD(0x29DD954E);
		writeD(0x77C39CFC);
		writeD(0x97ADB620);
		writeD(0x07BDE0F7);
		writeB(_blowfishKey);
		writeC(0x00);
	}
}
