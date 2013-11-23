/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.party;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * Format: ch S
 */
public class ExAskJoinPartyRoom extends L2GameServerPacket {
    private String _charName;
    private String _roomName;

    public ExAskJoinPartyRoom(String charName, String roomName) {
        _charName = charName;
        _roomName = roomName;
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x35);
        writeS(_charName);
        writeS(_roomName);
    }
}