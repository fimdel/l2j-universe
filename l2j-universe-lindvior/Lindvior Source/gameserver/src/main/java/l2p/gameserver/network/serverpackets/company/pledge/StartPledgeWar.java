/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class StartPledgeWar extends L2GameServerPacket {
    private String _pledgeName;
    private String _char;

    public StartPledgeWar(String pledge, String charName) {
        _pledgeName = pledge;
        _char = charName;
    }

    @Override
    protected final void writeImpl() {
        writeC(0x63);
        writeS(_char);
        writeS(_pledgeName);
    }
}