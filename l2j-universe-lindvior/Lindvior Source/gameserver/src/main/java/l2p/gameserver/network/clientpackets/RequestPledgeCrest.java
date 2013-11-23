/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.CrestCache;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.company.pledge.PledgeCrest;

public class RequestPledgeCrest extends L2GameClientPacket {
    // format: cd

    private int _crestId;

    @Override
    protected void readImpl() {
        _crestId = readD();
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
        if (_crestId == 0)
            return;
        byte[] data = CrestCache.getInstance().getPledgeCrest(_crestId);
        if (data != null) {
            PledgeCrest pc = new PledgeCrest(_crestId, data);
            sendPacket(pc);
        }
    }
}