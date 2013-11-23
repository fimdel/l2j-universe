/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestExchangeSubstitute extends L2GameClientPacket {
    private static final Logger _log = LoggerFactory.getLogger(RequestExchangeSubstitute.class);
    private int partyMasterServerID;
    private int partyChangeMemberServerID;
    private int waitingPlayerServerID;

    @Override
    protected void readImpl() throws Exception {
        partyMasterServerID = readD();
        partyChangeMemberServerID = readD();
        waitingPlayerServerID = readD();
    }

    @Override
    protected void runImpl() throws Exception {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
        _log.info("[IMPLEMENT ME!] RequestExchangeSubstitute: partyMasterServerID:" + this.partyMasterServerID + " partyChangeMemberServerID:" + this.partyChangeMemberServerID + " waitingPlayerServerID:" + this.waitingPlayerServerID);
    }
}
