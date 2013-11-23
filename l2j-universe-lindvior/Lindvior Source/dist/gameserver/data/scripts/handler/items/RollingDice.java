package handler.items;

import l2p.commons.util.Rnd;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.Dice;
import l2p.gameserver.network.serverpackets.SystemMessage;

public class RollingDice extends ScriptItemHandler {
    // all the items ids that this handler knowns
    private static final int[] _itemIds = {4625, 4626, 4627, 4628};

    @Override
    public boolean useItem(Playable playable, ItemInstance item, boolean ctrl) {
        if (playable == null || !playable.isPlayer())
            return false;
        Player player = (Player) playable;

        int itemId = item.getItemId();

        if (player.isInOlympiadMode()) {
            player.sendPacket(Msg.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
            return false;
        }

        if (player.isSitting()) {
            player.sendPacket(Msg.YOU_CANNOT_MOVE_WHILE_SITTING);
            return false;
        }

        int number = Rnd.get(1, 6);
        if (number == 0) {
            player.sendPacket(Msg.YOU_MAY_NOT_THROW_THE_DICE_AT_THIS_TIMETRY_AGAIN_LATER);
            return false;
        }

        player.broadcastPacket(new Dice(player.getObjectId(), itemId, number, player.getX() - 30, player.getY() - 30, player.getZ()), new SystemMessage(SystemMessage.S1_HAS_ROLLED_S2).addString(player.getName()).addNumber(number));

        return true;
    }

    @Override
    public final int[] getItemIds() {
        return _itemIds;
    }
}