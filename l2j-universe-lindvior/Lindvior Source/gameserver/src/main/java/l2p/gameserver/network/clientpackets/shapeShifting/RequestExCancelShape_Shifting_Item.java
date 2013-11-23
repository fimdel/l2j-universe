/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets.shapeShifting;

import l2p.gameserver.model.Player;
import l2p.gameserver.network.clientpackets.L2GameClientPacket;
import l2p.gameserver.network.serverpackets.Shape_Shifting.ExShape_Shifting_Result;

public class RequestExCancelShape_Shifting_Item extends L2GameClientPacket {
    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        player.setAppearanceStone(null);
        player.setAppearanceExtractItem(null);
        player.sendPacket(ExShape_Shifting_Result.FAIL);
    }

    @Override
    protected void readImpl() {
    }
}
