package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;

public class RequestChangeAttributeCancel extends L2GameClientPacket {

    @Override
    public void readImpl() {
    }

    @Override
    public void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        activeChar.sendActionFailed();

    }

}