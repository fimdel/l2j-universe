/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeInfo;
import l2p.gameserver.tables.ClanTable;

public class RequestPledgeInfo extends L2GameClientPacket {
    private int _clanId;

    @Override
    protected void readImpl() {
        _clanId = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
        if (_clanId < 10000000) {
            activeChar.sendActionFailed();
            return;
        }
        Clan clan = ClanTable.getInstance().getClan(_clanId);
        if (clan == null) {
            //Util.handleIllegalPlayerAction(activeChar, "RequestPledgeInfo[40]", "Clan data for clanId " + _clanId + " is missing", 1);
            //_log.warn("Host " + getClient().getIpAddr() + " possibly sends fake packets. activeChar: " + activeChar);
            activeChar.sendActionFailed();
            return;
        }

        activeChar.sendPacket(new PledgeInfo(clan));
    }
}