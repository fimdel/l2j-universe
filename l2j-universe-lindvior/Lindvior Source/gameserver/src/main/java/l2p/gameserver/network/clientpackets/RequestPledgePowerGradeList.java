/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.model.pledge.RankPrivs;
import l2p.gameserver.network.serverpackets.company.pledge.PledgePowerGradeList;

public class RequestPledgePowerGradeList extends L2GameClientPacket {
    @Override
    protected void readImpl() {
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
        Clan clan = activeChar.getClan();
        if (clan != null) {
            RankPrivs[] privs = clan.getAllRankPrivs();
            activeChar.sendPacket(new PledgePowerGradeList(privs));
        }
    }
}