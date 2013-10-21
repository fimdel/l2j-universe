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

import lineage2.gameserver.Config;
import lineage2.gameserver.network.serverpackets.KeyPacket;
import lineage2.gameserver.network.serverpackets.SendStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ProtocolVersion extends L2GameClientPacket
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ProtocolVersion.class);
	/**
	 * Field _version.
	 */
	private int _version;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_version = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		if ((_version == 65534) || (_version == -2))
		{
			_client.closeNow(false);
			return;
		}
		else if ((_version == 65533) || (_version == -3))
		{
			_log.info("Status request from IP : " + getClient().getIpAddr());
			if (Config.RWHO_LOG)
			{
				_log.info(getClient().toString() + " RWHO received");
			}
			getClient().close(new SendStatus());
			return;
		}
		else if ((_version < Config.MIN_PROTOCOL_REVISION) || (_version > Config.MAX_PROTOCOL_REVISION))
		{
			_log.warn("Unknown protocol revision : " + _version + ", client : " + _client);
			getClient().close(new KeyPacket(null));
			return;
		}
		getClient().setRevision(_version);
		sendPacket(new KeyPacket(_client.enableCrypt()));
	}
}
