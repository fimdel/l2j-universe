/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 24.07.12
 * Time: 11:00
 */
public class RequestCrystallizeItemCancel extends L2GameClientPacket {
    @Override
    protected void readImpl() throws Exception {
    }

    @Override
    protected void runImpl() throws Exception {
        Player activeChar = getClient().getActiveChar();

        if (activeChar == null) {
            return;
        }
        activeChar.sendActionFailed();
    }
}
