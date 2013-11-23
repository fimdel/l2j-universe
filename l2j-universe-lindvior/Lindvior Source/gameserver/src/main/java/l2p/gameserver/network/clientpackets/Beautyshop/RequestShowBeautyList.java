/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.clientpackets.Beautyshop;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.clientpackets.L2GameClientPacket;
import l2p.gameserver.network.serverpackets.Beautyshop.ExResponseBeautyListPacket;

public class RequestShowBeautyList extends L2GameClientPacket {
    private int check;

    @Override
    protected void readImpl() throws Exception {
        check = readD();       // 0 это причёска, 1 это лицо
    }

    @Override
    protected void runImpl() throws Exception {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
        activeChar.sendPacket(new ExResponseBeautyListPacket(activeChar, check));
    }
}
