/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.commons.lang.ArrayUtils;
import l2p.gameserver.data.xml.holder.EnchantItemHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.PcInventory;
import l2p.gameserver.network.serverpackets.ExPut_Shape_Shifting_Target_Item_Result;
import l2p.gameserver.network.serverpackets.ExShape_Shifting_Result;
import l2p.gameserver.network.serverpackets.components.SystemMessageId;
import l2p.gameserver.templates.item.support.AppearanceStone;
import l2p.gameserver.templates.item.type.ExItemType;
import l2p.gameserver.templates.item.type.ItemGrade;

public class RequestExTryToPut_Shape_Shifting_TargetItem extends L2GameClientPacket {
    private int _targetItemObjId;

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
        ItemInstance stone = player.getEnchantItem();
        if ((targetItem == null) || (stone == null)) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            return;
        }

        if (!targetItem.canBeAppearance()) {
            player.sendPacket(ExPut_Shape_Shifting_Target_Item_Result.FAIL);
            player.setEnchantItem(null);
            return;
        }

        if ((targetItem.getLocation() != ItemInstance.ItemLocation.INVENTORY) && (targetItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL)) {
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

        if (((appearanceStone.getType() != AppearanceStone.ShapeType.RESTORE) && (targetItem.getVisualId() > 0)) || ((appearanceStone.getType() == AppearanceStone.ShapeType.RESTORE) && (targetItem.getVisualId() == 0))) {
            player.sendPacket(ExPut_Shape_Shifting_Target_Item_Result.FAIL);
            player.setEnchantItem(null);
            return;
        }

        if ((!targetItem.getTemplate().isOther()) && targetItem.getCrystalType() == ItemGrade.NONE) {
            player.sendPacket(SystemMessageId.YOU_CANNOT_MODIFY_OR_RESTORE_NOGRADE_ITEMS);
            return;
        }

        ItemGrade[] stoneGrades = appearanceStone.getGrades();
        if ((stoneGrades != null) && (stoneGrades.length > 0)) {
            if (!ArrayUtils.contains(stoneGrades, targetItem.getCrystalType())) {
                player.sendPacket(SystemMessageId.ITEM_GRADES_DO_NOT_MATCH);
                return;
            }
        }

        AppearanceStone.ShapeTargetType[] targetTypes = appearanceStone.getTargetTypes();
        if ((targetTypes == null) || (targetTypes.length == 0)) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            return;
        }

        if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.ALL)) {
            if (targetItem.isWeapon()) {
                if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.WEAPON)) {
                    player.sendPacket(SystemMessageId.WEAPONS_ONLY);
                }

            } else if (targetItem.isArmor()) {
                if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.ARMOR)) {
                    player.sendPacket(SystemMessageId.ARMOR_ONLY);
                }

            } else if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.ACCESSORY)) {
                player.sendPacket(SystemMessageId.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
                return;
            }

        }

        ExItemType[] itemTypes = appearanceStone.getItemTypes();
        if ((itemTypes != null) && (itemTypes.length > 0)) {
            if (!ArrayUtils.contains(itemTypes, targetItem.getTemplate().getExItemType())) {
                player.sendPacket(SystemMessageId.THIS_ITEM_DOES_NOT_MEET_REQUIREMENTS);
                return;
            }

        }

        if (targetItem.getOwnerId() != player.getObjectId()) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            return;
        }

        player.sendPacket(new ExPut_Shape_Shifting_Target_Item_Result(1, appearanceStone.getCost()));
    }

    protected void readImpl() {
        _targetItemObjId = readD();
    }
}
