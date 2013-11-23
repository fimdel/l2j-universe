/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.clientpackets.Beautyshop;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.clientpackets.L2GameClientPacket;

public class NotifyExitBeautyshop extends L2GameClientPacket {
    @Override
    protected void readImpl() throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void runImpl() throws Exception {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
    }
}
