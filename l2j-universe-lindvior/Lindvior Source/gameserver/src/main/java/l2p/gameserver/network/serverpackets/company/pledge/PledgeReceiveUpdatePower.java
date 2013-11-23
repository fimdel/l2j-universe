/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgeReceiveUpdatePower extends L2GameServerPacket {
    private int _privs;

    public PledgeReceiveUpdatePower(int privs) {
        _privs = privs;
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x43);
        writeD(_privs); //Filler??????
    }
}
