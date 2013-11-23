/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets.PledgeRecruit;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.clientpackets.L2GameClientPacket;

public class RequestPledgeDraftListSearch extends L2GameClientPacket {
    @Override
    protected void readImpl() throws Exception {
        readD();
        readD();
        readD();
        readS();
        readD();
        readD();
    }

    @Override
    protected void runImpl() throws Exception {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
    }
}
