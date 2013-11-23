/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.pledge.Clan;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeReceiveWarList;

public class RequestPledgeWarList extends L2GameClientPacket {
    // format: (ch)dd
    static int _type;
    private int _page;

    @Override
    protected void readImpl() {
        _page = readD();
        _type = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        Clan clan = activeChar.getClan();
        if (clan != null)
            activeChar.sendPacket(new PledgeReceiveWarList(clan, _type, _page));
    }
}