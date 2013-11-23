package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;

/**
 * @author : Darvin
 */
public class RequestCommissionCancel extends L2GameClientPacket {

    @Override
    protected void readImpl() {
        //
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        System.out.println("RequestCommissionCancel");
    }

}
