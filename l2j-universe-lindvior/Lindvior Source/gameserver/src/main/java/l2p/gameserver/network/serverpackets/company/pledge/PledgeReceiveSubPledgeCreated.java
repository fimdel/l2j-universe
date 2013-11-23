/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.model.pledge.SubUnit;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgeReceiveSubPledgeCreated extends L2GameServerPacket {
    private int type;
    private String _name, leader_name;

    public PledgeReceiveSubPledgeCreated(SubUnit subPledge) {
        type = subPledge.getType();
        _name = subPledge.getName();
        leader_name = subPledge.getLeaderName();
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x40);

        writeD(0x01);
        writeD(type);
        writeS(_name);
        writeS(leader_name);
    }
}