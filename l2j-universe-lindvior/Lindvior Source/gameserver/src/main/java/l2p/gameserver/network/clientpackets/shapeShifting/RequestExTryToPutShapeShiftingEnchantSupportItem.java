/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets.shapeShifting;

import l2p.gameserver.data.xml.holder.EnchantItemHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.PcInventory;
import l2p.gameserver.network.clientpackets.L2GameClientPacket;
import l2p.gameserver.network.serverpackets.Shape_Shifting.ExPut_Shape_Shifting_Extraction_Item_Result;
import l2p.gameserver.network.serverpackets.Shape_Shifting.ExShape_Shifting_Result;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.templates.item.support.AppearanceStone;

public class RequestExTryToPutShapeShiftingEnchantSupportItem extends L2GameClientPacket {
    private int _targetItemObjId;
    private int _extracItemObjId;

    @Override
    protected void readImpl() {
        _targetItemObjId = readD();
        _extracItemObjId = readD();
    }

    @Override
    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null)
            return;

        if (player.isActionsDisabled() || player.isInStoreMode() || player.isInTrade()) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            return;
        }

        PcInventory inventory = player.getInventory();
        ItemInstance targetItem = inventory.getItemByObjectId(_targetItemObjId);
        ItemInstance extracItem = inventory.getItemByObjectId(_extracItemObjId);
        ItemInstance stone = player.getAppearanceStone();
        if (targetItem == null || extracItem == null || stone == null) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            return;
        }

        if (!extracItem.canBeAppearance()) {
            player.sendPacket(ExPut_Shape_Shifting_Extraction_Item_Result.FAIL);
            return;
        }

        if (extracItem.getLocation() != ItemInstance.ItemLocation.INVENTORY && extracItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            return;
        }

        if ((stone = inventory.getItemByObjectId(stone.getObjectId())) == null) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            return;
        }

        AppearanceStone appearanceStone = EnchantItemHolder.getInstance().getAppearanceStone(stone.getItemId());
        if (appearanceStone == null) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            return;
        }

        if (appearanceStone.getType() == AppearanceStone.ShapeType.RESTORE || appearanceStone.getType() == AppearanceStone.ShapeType.FIXED) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            return;
        }

        /*if(!extracItem.isOther() && extracItem.getGrade() == ItemGrade.NONE)
          {
              player.sendPacket(SystemMsg.ITEM_GRADES_DO_NOT_MATCH);
              return;
          }*/

        //if(!extracItem.isOther() && targetItem.getTemplate().getItemGrade().ordinal() <= extracItem.getTemplate().getItemGrade().ordinal())
        //{
        //	player.sendPacket(SystemMsg.YOU_CANNOT_EXTRACT_FROM_ITEMS_THAT_ARE_HIGHERGRADE_THAN_ITEMS_TO_BE_MODIFIED);
        //	return;
        //}

        if (extracItem.getVisualId() > 0) {
            player.sendPacket(SystemMsg.YOU_CANNOT_EXTRACT_FROM_A_MODIFIED_ITEM);
            return;
        }

        if (targetItem.getExItemType() != extracItem.getExItemType()) {
            player.sendPacket(SystemMsg.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
            return;
        }

        // Запрет на обработку чужих вещей, баг может вылезти на серверных лагах
        if (extracItem.getOwnerId() != player.getObjectId()) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            return;
        }

        player.setAppearanceExtractItem(extracItem);
        //ItemFunctions.addItem(player
        player.sendPacket(ExPut_Shape_Shifting_Extraction_Item_Result.SUCCESS);
    }
}
