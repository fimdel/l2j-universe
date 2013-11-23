/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.party;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PartySmallWindowDelete extends L2GameServerPacket {
    private final int _objId;
    private final String _name;

    public PartySmallWindowDelete(Player member) {
        _objId = member.getObjectId();
        _name = member.getName();
    }

    @Override
    protected final void writeImpl() {
        writeC(0x51);
        writeD(_objId);
        writeS(_name);
    }
}