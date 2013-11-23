/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.model.pledge.UnitMember;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeReceiveMemberInfo;

public class RequestPledgeMemberInfo extends L2GameClientPacket {
    // format: (ch)dS
    @SuppressWarnings("unused")
    private int _pledgeType;
    private String _target;

    @Override
    protected void readImpl() {
        _pledgeType = readD();
        _target = readS(16);
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
        Clan clan = activeChar.getClan();
        if (clan != null) {
            UnitMember cm = clan.getAnyMember(_target);
            if (cm != null)
                activeChar.sendPacket(new PledgeReceiveMemberInfo(cm));
        }
    }
}