/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.CrestCache;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.company.pledge.ExPledgeCrestLarge;

public class RequestPledgeCrestLarge extends L2GameClientPacket {
    // format: chd
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
        byte[] data = CrestCache.getInstance().getPledgeCrestLarge(_crestId);
        if (data != null) {
            ExPledgeCrestLarge pcl = new ExPledgeCrestLarge(_crestId, data);
            sendPacket(pcl);
        }
    }
}