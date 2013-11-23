/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.handler.items.impl;

import l2p.gameserver.data.xml.holder.EnchantItemHolder;
import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.Shape_Shifting.ExChooseShapeShiftingItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppearanceStones extends AbstractItemHandler {

    private static final Logger _log = LoggerFactory.getLogger(AppearanceStones.class);

    @Override
    public boolean useItem(Playable playable, ItemInstance item, boolean ctrl) {
        if (playable == null || !playable.isPlayer())
            return false;

        Player player = (Player) playable;

        if (player.getAppearanceStone() != null)
            return false;

        player.setAppearanceStone(item);
        player.sendPacket(new ExChooseShapeShiftingItem(EnchantItemHolder.getInstance().getAppearanceStone(item.getItemId())));
        return true;
    }

    @Override
    public int[] getItemIds() {
        return EnchantItemHolder.getInstance().getAppearanceStones();
    }
}
