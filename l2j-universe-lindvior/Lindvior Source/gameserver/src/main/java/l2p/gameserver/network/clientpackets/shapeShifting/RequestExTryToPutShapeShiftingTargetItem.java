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
import l2p.gameserver.network.serverpackets.Shape_Shifting.ExPut_Shape_Shifting_Target_Item_Result;
import l2p.gameserver.network.serverpackets.Shape_Shifting.ExShape_Shifting_Result;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.templates.item.support.AppearanceStone;
import l2p.gameserver.templates.item.type.ExItemType;
import l2p.gameserver.templates.item.type.ItemGrade;
import org.apache.commons.lang3.ArrayUtils;

public class RequestExTryToPutShapeShiftingTargetItem extends L2GameClientPacket {
    private int _targetItemObjId;

    @Override
    protected void readImpl() {
        _targetItemObjId = readD();
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
        ItemInstance stone = player.getAppearanceStone();
        if (targetItem == null || stone == null) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            return;
        }

        if (!targetItem.canBeAppearance()) {
            player.sendPacket(ExPut_Shape_Shifting_Target_Item_Result.FAIL);
            return;
        }

        if (targetItem.getLocation() != ItemInstance.ItemLocation.INVENTORY && targetItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL) {
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

        if (appearanceStone.getType() != AppearanceStone.ShapeType.RESTORE && targetItem.getVisualId() > 0 || appearanceStone.getType() == AppearanceStone.ShapeType.RESTORE && targetItem.getVisualId() == 0) {
            player.sendPacket(ExPut_Shape_Shifting_Target_Item_Result.FAIL);
            return;
        }

        if (!targetItem.isOther() && targetItem.getTemplate().getItemGrade() == ItemGrade.NONE) {
            player.sendPacket(SystemMsg.YOU_CANNOT_MODIFY_OR_RESTORE_NOGRADE_ITEMS);
            return;
        }

        ItemGrade[] stoneGrades = appearanceStone.getGrades();
        if (stoneGrades != null && stoneGrades.length > 0) {
            if (!ArrayUtils.contains(stoneGrades, targetItem.getTemplate().getItemGrade())) {
                player.sendPacket(SystemMsg.ITEM_GRADES_DO_NOT_MATCH);
                return;
            }
        }

        AppearanceStone.ShapeTargetType[] targetTypes = appearanceStone.getTargetTypes();
        if (targetTypes == null || targetTypes.length == 0) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            return;
        }

        if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.ALL)) {
            if (targetItem.isWeapon()) {
                if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.WEAPON)) {
                    player.sendPacket(SystemMsg.WEAPONS_ONLY);
                    return;
                }
            } else if (targetItem.isArmor()) {
                if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.ARMOR)) {
                    player.sendPacket(SystemMsg.ARMOR_ONLY);
                    return;
                }
            } else {
                if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.ACCESSORY)) {
                    player.sendPacket(SystemMsg.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
                    return;
                }
            }
        }

        ExItemType[] itemTypes = appearanceStone.getItemTypes();
        if (itemTypes != null && itemTypes.length > 0) {
            if (!ArrayUtils.contains(itemTypes, targetItem.getExItemType())) {
                player.sendPacket(SystemMsg.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
                return;
            }
        }

        // Запрет на обработку чужих вещей, баг может вылезти на серверных лагах
        if (targetItem.getOwnerId() != player.getObjectId()) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            return;
        }

        player.sendPacket(new ExPut_Shape_Shifting_Target_Item_Result(ExPut_Shape_Shifting_Target_Item_Result.SUCCESS_RESULT, appearanceStone.getCost()));
    }
}
