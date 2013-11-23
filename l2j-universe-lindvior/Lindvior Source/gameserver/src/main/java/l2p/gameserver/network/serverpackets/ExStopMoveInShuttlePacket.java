package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.utils.Location;

/**
 * @author Bonux
 */
public class ExStopMoveInShuttlePacket extends L2GameServerPacket {
    private int _playerObjectId, _shuttleId, _playerHeading;
    private Location _loc;

    public ExStopMoveInShuttlePacket(Player cha) {
        _playerObjectId = cha.getObjectId();
        _shuttleId = cha.getBoat().getBoatId();
        _loc = cha.getInBoatPosition();
        _playerHeading = cha.getHeading();
    }

    @Override
    protected final void writeImpl() {
        writeEx449(0xCF);
        writeD(_playerObjectId);//Player ObjID
        writeD(_shuttleId);//Shuttle ID (Arkan: 1,2)
        writeD(_loc.x); // X in shuttle
        writeD(_loc.y); // Y in shuttle
        writeD(_loc.z); // Z in shuttle
        writeD(_playerHeading); // H in shuttle
    }
}