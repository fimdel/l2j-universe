/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.data.xml.holder.EnchantItemHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.PcInventory;
import l2p.gameserver.network.serverpackets.ExPut_Shape_Shifting_Extraction_Item_Result;
import l2p.gameserver.network.serverpackets.ExShape_Shifting_Result;
import l2p.gameserver.network.serverpackets.components.SystemMessageId;
import l2p.gameserver.templates.item.support.AppearanceStone;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 12.10.12
 * Time: 20:28
 * создал B0nux
 */
public class RequestExTryToPutShapeShiftingEnchantSupportItem extends L2GameClientPacket {
    private int _targetItemObjId;
    private int _extracItemObjId;

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null) {
            return;
        }
        if ((player.isActionsDisabled()) || (player.isInStoreMode()) || (player.isInTrade())) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            return;
        }

        PcInventory inventory = player.getInventory();
        ItemInstance targetItem = inventory.getItemByObjectId(this._targetItemObjId);
        ItemInstance extracItem = inventory.getItemByObjectId(this._extracItemObjId);
        ItemInstance stone = player.getEnchantItem();
        if ((targetItem == null) || (extracItem == null) || (stone == null)) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            return;
        }

        if (!extracItem.canBeAppearance()) {
            player.sendPacket(ExPut_Shape_Shifting_Extraction_Item_Result.FAIL);
            player.setEnchantItem(null);
            return;
        }

        if ((extracItem.getLocation() != ItemInstance.ItemLocation.INVENTORY) && (extracItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL)) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            return;
        }

        if ((stone = inventory.getItemByObjectId(stone.getObjectId())) == null) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            return;
        }

        AppearanceStone appearanceStone = EnchantItemHolder.getInstance().getAppearanceStone(stone.getItemId());
        if (appearanceStone == null) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            return;
        }

        if ((appearanceStone.getType() == AppearanceStone.ShapeType.RESTORE) || (appearanceStone.getType() == AppearanceStone.ShapeType.FIXED)) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            return;
        }

        if ((!extracItem.getTemplate().isOther()) && (targetItem.getTemplate().getCrystalType().ordinal() <= extracItem.getTemplate().getCrystalType().ordinal())) {
            player.sendPacket(SystemMessageId.YOU_CANNOT_EXTRACT_FROM_ITEMS_THAT_ARE_HIGHERGRADE_THAN_ITEMS_TO_BE_MODIFIED);
            return;
        }

        if (extracItem.getVisualId() > 0) {
            player.sendPacket(SystemMessageId.YOU_CANNOT_EXTRACT_FROM_A_MODIFIED_ITEM);
            return;
        }

        if (targetItem.getTemplate().getExItemType() != extracItem.getTemplate().getExItemType()) {
            player.sendPacket(SystemMessageId.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
            return;
        }

        if (extracItem.getOwnerId() != player.getObjectId()) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            return;
        }

        player.setEnchantItem(extracItem);
        player.sendPacket(ExPut_Shape_Shifting_Extraction_Item_Result.SUCCESS);
    }

    @Override
    protected void readImpl() {
        _targetItemObjId = readD();
        _extracItemObjId = readD();
    }
}
