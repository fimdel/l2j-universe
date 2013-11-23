/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;

import java.util.List;

/**
 * Format: (chd) ddd[dS]d[dS]
 * d: unknown
 * d: always -1
 * d: blue players number
 * [
 * d: player object id
 * S: player name
 * ]
 * d: blue players number
 * [
 * d: player object id
 * S: player name
 * ]
 */
public class ExCubeGameTeamList extends L2GameServerPacket {
    List<Player> _bluePlayers;
    List<Player> _redPlayers;
    int _roomNumber;
    int _timeleft;

    public ExCubeGameTeamList(List<Player> redPlayers, List<Player> bluePlayers, int roomNumber) {
        _redPlayers = redPlayers;
        _bluePlayers = bluePlayers;
        _roomNumber = roomNumber - 1;
    }

    public ExCubeGameTeamList(List<Player> redPlayers, List<Player> bluePlayers, int roomNumber, int timeleft) {
        _redPlayers = redPlayers;
        _bluePlayers = bluePlayers;
        _roomNumber = roomNumber - 1;
        _timeleft = timeleft;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x98);
        writeD(0x00);

        writeD(_roomNumber);
        writeD(_timeleft);

        writeD(_bluePlayers.size());
        for (Player player : _bluePlayers) {
            writeD(player.getObjectId());
            writeS(player.getName());
        }
        writeD(_redPlayers.size());
        for (Player player : _redPlayers) {
            writeD(player.getObjectId());
            writeS(player.getName());
        }
    }
}