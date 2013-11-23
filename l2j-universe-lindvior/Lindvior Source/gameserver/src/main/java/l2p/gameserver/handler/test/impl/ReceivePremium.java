/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.handler.test.impl;

import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExGetPremiumItemList;
import l2p.gameserver.network.serverpackets.components.SystemMessageId;

public class ReceivePremium extends BypassHandler {
    public String[] getBypassList() {
        return new String[]{"ReceivePremium"};
    }

    public void onBypassCommand(String command, Player activeChar, Creature target) {
        if ((target == null) || (!target.isNpc())) {
            return;
        }
        if (activeChar.getPremiumItemList().isEmpty()) {
            activeChar.sendPacket(SystemMessageId.THERE_ARE_NO_MORE_VITAMIN_ITEMS_TO_BE_FOUND);
            return;
        }

        activeChar.sendPacket(new ExGetPremiumItemList(activeChar));
    }
}
