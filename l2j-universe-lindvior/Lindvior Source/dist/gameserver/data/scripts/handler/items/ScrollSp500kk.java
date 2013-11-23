/*
 * Copyright Mazaffaka Project (c) 2012.
 */

package handler.items;

import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;

public class ScrollSp500kk extends ScriptItemHandler {
    private static final int[] _itemIds = {34742};

    @Override
    public boolean useItem(Playable playable, ItemInstance item, boolean ctrl) {
        if (!playable.isPlayer())
            return false;
        if (playable.getLevel() < 76)
            return false;
        Player activeChar = (Player) playable;
        if (activeChar.getActiveSubClass().isSub() || activeChar.getActiveSubClass().isDouble()) {
            activeChar.setSp(500000000);
        }
        return true;
    }

    @Override
    public int[] getItemIds() {
        return _itemIds;
    }
}
