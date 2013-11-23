/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExShape_Shifting_Result;

public class RequestExCancelShape_Shifting_Item extends L2GameClientPacket {
    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();

        if (player != null) {
            player.setEnchantItem(null);
            player.setEnchantSupportItem(null);
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
        }
    }

    @Override
    protected void readImpl() {
    }
}
