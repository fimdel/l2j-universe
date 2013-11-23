/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.loginserver.serverpackets;

import l2p.commons.net.AdvancedIp;
import l2p.commons.net.utils.NetUtils;
import l2p.loginserver.GameServerManager;
import l2p.loginserver.L2LoginClient;
import l2p.loginserver.accounts.Account;
import l2p.loginserver.gameservercon.GameServer;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * ServerList
 * Format: cc [cddcchhcdc]
 * <p/>
 * c: server list size (number of servers)
 * c: last server
 * [ (repeat for each servers)
 * c: server id (ignored by client?)
 * d: server ip
 * d: server port
 * c: age limit (used by client?)
 * c: pvp or not (used by client?)
 * h: current number of players
 * h: max number of players
 * c: 0 if server is down
 * d: 2nd bit: clock
 * 3rd bit: wont dsiplay server name
 * 4th bit: test server (used by client?)
 * c: 0 if you dont want to display brackets in front of sever name
 * ]
 * <p/>
 * Server will be considered as Good when the number of  online players
 * is less than half the maximum. as Normal between half and 4/5
 * and Full when there's more than 4/5 of the maximum number of players
 */
public final class ServerList extends L2LoginServerPacket {
    private List<ServerData> _servers = new ArrayList<ServerData>();
    private int _lastServer;

    private static class ServerData {
        int serverId;
        InetAddress ip;
        int port;
        int online;
        int maxPlayers;
        boolean status;
        boolean pvp;
        boolean brackets;
        int type;
        int ageLimit;
        int playerSize;
        int[] deleteChars;

        ServerData(int serverId, InetAddress ip, int port, boolean pvp, boolean brackets, int type, int online, int maxPlayers, boolean status, int size, int ageLimit, int[] d) {
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

    public ServerList(Account account, L2LoginClient client) {
        _lastServer = account.getLastServer();

        for (GameServer gs : GameServerManager.getInstance().getGameServers()) {
            boolean added = false;

            InetAddress ip;
            try {
                ip = NetUtils.isInternalIP(account.getLastIP()) ? gs.getInternalHost() : gs.getExternalHost();
            } catch (UnknownHostException e) {
                continue;
            }

            Pair<Integer, int[]> entry = account.getAccountInfo(gs.getId());

            //			_servers.add(new ServerData(gs.getId(), ip, gs.getPort(), gs.isPvp(), gs.isShowingBrackets(), gs.getServerType(), gs.getOnline(), gs.getMaxPlayers(), gs.isOnline(), entry == null ? 0 : entry.getKey(), gs.getAgeLimit(), entry == null ? ArrayUtils.EMPTY_INT_ARRAY : entry.getValue()));

            if (gs.getAdvancedIps() != null) {
                for (AdvancedIp advancedIp : gs.getAdvancedIps()) {
                    if (!added && NetUtils.CheckSubNet(client.getIpAddress(), advancedIp)) {
                        try {
                            _servers.add(new ServerData(gs.getId(), InetAddress.getByName(advancedIp.ipAdress), gs.getPort(), gs.isPvp(), gs.isShowingBrackets(), gs.getServerType(), gs.getOnline(), gs.getMaxPlayers(), gs.isOnline(), entry == null ? 0 : entry.getKey(), gs.getAgeLimit(), entry == null ? ArrayUtils.EMPTY_INT_ARRAY : entry.getValue()));
                            added = true;
                        } catch (Exception e) {
                        }
                    }
                }
            }
            if (!added) {
                _servers.add(new ServerData(gs.getId(), ip, gs.getPort(), gs.isPvp(), gs.isShowingBrackets(), gs.getServerType(), gs.getOnline(), gs.getMaxPlayers(), gs.isOnline(), entry == null ? 0 : entry.getKey(), gs.getAgeLimit(), entry == null ? ArrayUtils.EMPTY_INT_ARRAY : entry.getValue()));
            }
        }
    }

    @Override
    protected void writeImpl() {
        writeC(0x04);
        writeC(_servers.size());
        writeC(_lastServer);
        for (ServerData server : _servers) {
            writeC(server.serverId);
            InetAddress i4 = server.ip;
            byte[] raw = i4.getAddress();
            writeC(raw[0] & 0xff);
            writeC(raw[1] & 0xff);
            writeC(raw[2] & 0xff);
            writeC(raw[3] & 0xff);
            writeD(server.port);
            writeC(server.ageLimit); // age limit
            writeC(server.pvp ? 0x01 : 0x00);
            writeH(server.online);
            writeH(server.maxPlayers);
            writeC(server.status ? 0x01 : 0x00);
            writeD(server.type);
            writeC(server.brackets ? 0x01 : 0x00);
        }

        writeH(0x00); // -??
        writeC(_servers.size());
        for (ServerData server : _servers) {
            writeC(server.serverId);
            writeC(server.playerSize); // acc player size
            writeC(server.deleteChars.length);
            for (int t : server.deleteChars) {
                writeD((int) (t - System.currentTimeMillis() / 1000L));
            }
        }
    }
}