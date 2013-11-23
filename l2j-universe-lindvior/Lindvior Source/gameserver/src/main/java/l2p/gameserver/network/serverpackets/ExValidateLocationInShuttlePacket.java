package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.utils.Location;

/**
 * @author Bonux
 */
public class ExValidateLocationInShuttlePacket extends L2GameServerPacket {
    private int _playerObjectId, _shuttleId;
    private Location _loc;

    public ExValidateLocationInShuttlePacket(Player cha) {
        _playerObjectId = cha.getObjectId();
        _shuttleId = cha.getBoat().getBoatId();
        _loc = cha.getInBoatPosition();
    }

    @Override
    protected final void writeImpl() {
        writeEx449(0xD0);
        writeD(_playerObjectId); // Player ObjID
        writeD(_shuttleId); // Shuttle ID (Arkan: 1,2; Cruma: 3)
        writeD(_loc.x); // X in shuttle
        writeD(_loc.y); // Y in shuttle
        writeD(_loc.z); // Z in shuttle
        writeD(_loc.h); // H in shuttle
    }
}