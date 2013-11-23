/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.model.pledge.UnitMember;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgeReceiveMemberInfo extends L2GameServerPacket {
    private UnitMember _member;

    public PledgeReceiveMemberInfo(UnitMember member) {
        _member = member;
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x3f);

        writeD(_member.getPledgeType());
        writeS(_member.getName());
        writeS(_member.getTitle());
        writeD(_member.getPowerGrade());
        writeS(_member.getSubUnit().getName());
        writeS(_member.getRelatedName()); // apprentice/sponsor name if any
    }
}