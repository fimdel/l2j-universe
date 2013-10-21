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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import lineage2.commons.net.utils.NetUtils;
import lineage2.loginserver.GameServerManager;
import lineage2.loginserver.accounts.Account;
import lineage2.loginserver.gameservercon.GameServer;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ServerList extends L2LoginServerPacket
{
	/**
	 * Field _servers.
	 */
	private final List<ServerData> _servers = new ArrayList<>();
	/**
	 * Field _lastServer.
	 */
	private final int _lastServer;
	
	/**
	 * @author Mobius
	 */
	private static class ServerData
	{
		/**
		 * Field serverId.
		 */
		int serverId;
		/**
		 * Field ip.
		 */
		InetAddress ip;
		/**
		 * Field port.
		 */
		int port;
		/**
		 * Field online.
		 */
		int online;
		/**
		 * Field maxPlayers.
		 */
		int maxPlayers;
		/**
		 * Field status.
		 */
		boolean status;
		/**
		 * Field pvp.
		 */
		boolean pvp;
		/**
		 * Field brackets.
		 */
		boolean brackets;
		/**
		 * Field type.
		 */
		int type;
		/**
		 * Field ageLimit.
		 */
		int ageLimit;
		/**
		 * Field playerSize.
		 */
		int playerSize;
		/**
		 * Field deleteChars.
		 */
		int[] deleteChars;
		
		/**
		 * Constructor for ServerData.
		 * @param serverId int
		 * @param ip InetAddress
		 * @param port int
		 * @param pvp boolean
		 * @param brackets boolean
		 * @param type int
		 * @param online int
		 * @param maxPlayers int
		 * @param status boolean
		 * @param size int
		 * @param ageLimit int
		 * @param d int[]
		 */
		ServerData(int serverId, InetAddress ip, int port, boolean pvp, boolean brackets, int type, int online, int maxPlayers, boolean status, int size, int ageLimit, int[] d)
		{
			this.serverId = serverId;
			this.ip = ip;
			this.port = port;
			this.pvp = pvp;
			this.brackets = brackets;
			this.type = type;
			this.online = online;
			this.maxPlayers = maxPlayers;
			this.status = status;
			playerSize = size;
			this.ageLimit = ageLimit;
			deleteChars = d;
		}
	}
	
	/**
	 * Constructor for ServerList.
	 * @param account Account
	 */
	public ServerList(Account account)
	{
		_lastServer = account.getLastServer();
		for (GameServer gs : GameServerManager.getInstance().getGameServers())
		{
			InetAddress ip;
			try
			{
				ip = NetUtils.isInternalIP(account.getLastIP()) ? gs.getInternalHost() : gs.getExternalHost();
			}
			catch (UnknownHostException e)
			{
				continue;
			}
			Pair<Integer, int[]> entry = account.getAccountInfo(gs.getId());
			_servers.add(new ServerData(gs.getId(), ip, gs.getPort(), gs.isPvp(), gs.isShowingBrackets(), gs.getServerType(), gs.getOnline(), gs.getMaxPlayers(), gs.isOnline(), entry == null ? 0 : entry.getKey(), gs.getAgeLimit(), entry == null ? ArrayUtils.EMPTY_INT_ARRAY : entry.getValue()));
		}
	}
	
	/**
	 * Method writeImpl.
	 */
	@Override
	protected void writeImpl()
	{
		writeC(0x04);
		writeC(_servers.size());
		writeC(_lastServer);
		for (ServerData server : _servers)
		{
			writeC(server.serverId);
			InetAddress i4 = server.ip;
			byte[] raw = i4.getAddress();
			writeC(raw[0] & 0xff);
			writeC(raw[1] & 0xff);
			writeC(raw[2] & 0xff);
			writeC(raw[3] & 0xff);
			writeD(server.port);
			writeC(server.ageLimit);
			writeC(server.pvp ? 0x01 : 0x00);
			writeH(server.online);
			writeH(server.maxPlayers);
			writeC(server.status ? 0x01 : 0x00);
			writeD(server.type);
			writeC(server.brackets ? 0x01 : 0x00);
		}
		writeH(0x00);
		writeC(_servers.size());
		for (ServerData server : _servers)
		{
			writeC(server.serverId);
			writeC(server.playerSize);
			writeC(server.deleteChars.length);
			for (int t : server.deleteChars)
			{
				writeD((int) (t - (System.currentTimeMillis() / 1000L)));
			}
		}
	}
}
