package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.PcCafe;
import l2p.gameserver.model.Player;

/**
 * format: chS
 */
public class RequestPCCafeCouponUse extends L2GameClientPacket {
    private String couponCode;

    @Override
    protected void readImpl() {
        couponCode = readS();
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;
        else
            PcCafe.requestEnterCode(player, couponCode);
    }
}