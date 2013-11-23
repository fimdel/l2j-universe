/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets.company.pledge;

import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.model.pledge.RankPrivs;
import l2p.gameserver.model.pledge.UnitMember;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;

public class PledgeReceivePowerInfo extends L2GameServerPacket {
    private int PowerGrade, privs;
    private String member_name;

    public PledgeReceivePowerInfo(UnitMember member) {
        PowerGrade = member.getPowerGrade();
        member_name = member.getName();
        if (member.isClanLeader())
            privs = Clan.CP_ALL;
        else {
            RankPrivs temp = member.getClan().getRankPrivs(member.getPowerGrade());
            if (temp != null)
                privs = temp.getPrivs();
            else
                privs = 0;
        }
    }

    @Override
    protected final void writeImpl() {
        writeEx(0x3e);
        writeD(PowerGrade);
        writeS(member_name);
        writeD(privs);
    }
}
