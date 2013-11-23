/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExGoodsInventoryResult;

/**
 * @author VISTALL
 * @date 23:33/23.03.2011
 */
public class RequestGoodsInventoryInfo extends L2GameClientPacket {
    private int unk1;

    @Override
    protected void readImpl() throws Exception {
        unk1 = readC();
    }

    @Override
    protected void runImpl() throws Exception {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;
        player.sendPacket(new ExGoodsInventoryResult(-6));
    }

    @Override
    public String getType() {
        return "[C] D0:B1 RequestGoodsInventoryInfo";
    }
}
