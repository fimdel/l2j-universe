/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.alliance;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class StopAllianceWar extends L2GameServerPacket {
    private String _allianceName;
    private String _char;

    public StopAllianceWar(String alliance, String charName) {
        _allianceName = alliance;
        _char = charName;
    }

    @Override
    protected final void writeImpl() {
        writeC(0xc4);
        writeS(_allianceName);
        writeS(_char);
    }
}