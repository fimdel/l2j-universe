/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class SurrenderPledgeWar extends L2GameServerPacket {
    private String _pledgeName;
    private String _char;

    public SurrenderPledgeWar(String pledge, String charName) {
        _pledgeName = pledge;
        _char = charName;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x67);
        writeS(_pledgeName);
        writeS(_char);
    }
}