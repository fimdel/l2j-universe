package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.PcInventory;
import l2p.gameserver.model.items.etcitems.EnchantScrollInfo;
import l2p.gameserver.model.items.etcitems.EnchantScrollManager;
import l2p.gameserver.network.serverpackets.ExPutEnchantTargetItemResult;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.utils.Log;

public class RequestExTryToPutEnchantTargetItem extends AbstractEnchantPacket {
    private int _objectId;

    @Override
    protected void readImpl() {
        _objectId = readD();
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        if (!isValidPlayer(player)) {
            player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
            player.setEnchantScroll(null);
            return;
        }

        PcInventory inventory = player.getInventory();
        ItemInstance itemToEnchant = inventory.getItemByObjectId(_objectId);
        ItemInstance scroll = player.getEnchantScroll();

        if (itemToEnchant == null || scroll == null) {
            player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
            player.setEnchantScroll(null);
            return;
        }

        Log.add(player.getName() + "|Trying to put enchant|" + itemToEnchant.getItemId() + "|+" + itemToEnchant.getEnchantLevel() + "|"
                + itemToEnchant.getObjectId(), "enchants");

        EnchantScrollInfo esi = EnchantScrollManager.getScrollInfo(scroll.getItemId());

        if (esi == null) {
            player.sendActionFailed();
            return;
        }

        if (!checkItem(itemToEnchant, esi)) {
            player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
            player.sendPacket(SystemMsg.INAPPROPRIATE_ENCHANT_CONDITIONS);
            player.setEnchantScroll(null);
            return;
        }

        if ((scroll = inventory.getItemByObjectId(scroll.getObjectId())) == null) {
            player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
            player.setEnchantScroll(null);
            return;
        }

        if (itemToEnchant.getEnchantLevel() >= esi.getMax() || itemToEnchant.getEnchantLevel() < esi.getMin()) {
            player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
            player.sendPacket(SystemMsg.INAPPROPRIATE_ENCHANT_CONDITIONS);
            player.setEnchantScroll(null);
            return;
        }

        // Запрет на заточку чужих вещей, баг может вылезти на серверных лагах
        if (itemToEnchant.getOwnerId() != player.getObjectId()) {
            player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
            player.setEnchantScroll(null);
            return;
        }

        player.sendPacket(ExPutEnchantTargetItemResult.SUCCESS);
    }
}
