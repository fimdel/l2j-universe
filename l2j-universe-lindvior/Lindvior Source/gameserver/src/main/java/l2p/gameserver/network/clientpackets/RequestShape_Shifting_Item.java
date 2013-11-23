/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.commons.dao.JdbcEntityState;
import l2p.commons.lang.ArrayUtils;
import l2p.gameserver.data.xml.holder.EnchantItemHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.PcInventory;
import l2p.gameserver.network.serverpackets.ExShape_Shifting_Result;
import l2p.gameserver.network.serverpackets.InventoryUpdate;
import l2p.gameserver.network.serverpackets.components.SystemMessageId;
import l2p.gameserver.templates.item.support.AppearanceStone;
import l2p.gameserver.templates.item.type.ExItemType;
import l2p.gameserver.templates.item.type.ItemGrade;

public class RequestShape_Shifting_Item extends L2GameClientPacket {
    private int _targetItemObjId;

    protected void runImpl() {
        Player player = getClient().getActiveChar();
        if (player == null) {
            return;
        }
        if ((player.isActionsDisabled()) || (player.isInStoreMode()) || (player.isInTrade())) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            player.setEnchantSupportItem(null);
            return;
        }

        PcInventory inventory = player.getInventory();
        ItemInstance targetItem = inventory.getItemByObjectId(this._targetItemObjId);
        ItemInstance stone = player.getEnchantItem();
        if ((targetItem == null) || (stone == null)) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            player.setEnchantSupportItem(null);
            return;
        }

        if (stone.getOwnerId() != player.getObjectId()) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            player.setEnchantSupportItem(null);
            return;
        }

        if (!targetItem.canBeAppearance()) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            player.setEnchantSupportItem(null);
            return;
        }

        if ((targetItem.getLocation() != ItemInstance.ItemLocation.INVENTORY) && (targetItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL)) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            player.setEnchantSupportItem(null);
            return;
        }

        if ((stone = inventory.getItemByObjectId(stone.getObjectId())) == null) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            player.setEnchantSupportItem(null);
            return;
        }

        AppearanceStone appearanceStone = EnchantItemHolder.getInstance().getAppearanceStone(stone.getItemId());
        if (appearanceStone == null) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            player.setEnchantSupportItem(null);
            return;
        }

        if (((appearanceStone.getType() != AppearanceStone.ShapeType.RESTORE) && (targetItem.getVisualId() > 0)) || ((appearanceStone.getType() == AppearanceStone.ShapeType.RESTORE) && (targetItem.getVisualId() == 0))) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            player.setEnchantSupportItem(null);
            return;
        }

        if ((!targetItem.getTemplate().isOther()) && (targetItem.getCrystalType() == ItemGrade.NONE)) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            player.setEnchantSupportItem(null);
            return;
        }

        ItemGrade[] stoneGrades = appearanceStone.getGrades();
        if ((stoneGrades != null) && (stoneGrades.length > 0)) {
            if (!ArrayUtils.contains(stoneGrades, targetItem.getCrystalType())) {
                player.sendPacket(ExShape_Shifting_Result.CANCEL);
                player.setEnchantItem(null);
                player.setEnchantSupportItem(null);
                return;
            }
        }

        AppearanceStone.ShapeTargetType[] targetTypes = appearanceStone.getTargetTypes();
        if ((targetTypes == null) || (targetTypes.length == 0)) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            player.setEnchantSupportItem(null);
            return;
        }

        if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.ALL)) {
            if (targetItem.isWeapon()) {
                if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.WEAPON)) {
                    player.sendPacket(ExShape_Shifting_Result.CANCEL);
                    player.setEnchantItem(null);
                    player.setEnchantSupportItem(null);
                }

            } else if (targetItem.isArmor()) {
                if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.ARMOR)) {
                    player.sendPacket(ExShape_Shifting_Result.CANCEL);
                    player.setEnchantItem(null);
                    player.setEnchantSupportItem(null);
                }

            } else if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.ACCESSORY)) {
                player.sendPacket(ExShape_Shifting_Result.CANCEL);
                player.setEnchantItem(null);
                player.setEnchantSupportItem(null);
                return;
            }

        }

        ExItemType[] itemTypes = appearanceStone.getItemTypes();
        if ((itemTypes != null) && (itemTypes.length > 0)) {
            if (!ArrayUtils.contains(itemTypes, targetItem.getTemplate().getExItemType())) {
                player.sendPacket(ExShape_Shifting_Result.CANCEL);
                player.setEnchantItem(null);
                player.setEnchantSupportItem(null);
                return;
            }
        }

        ItemInstance extracItem = player.getEnchantSupportItem();
        int extracItemId = 0;
        if ((appearanceStone.getType() != AppearanceStone.ShapeType.RESTORE) && (appearanceStone.getType() != AppearanceStone.ShapeType.FIXED)) {
            if (extracItem == null) {
                player.sendPacket(ExShape_Shifting_Result.CANCEL);
                player.setEnchantItem(null);
                player.setEnchantSupportItem(null);
                return;
            }

            if (!extracItem.canBeAppearance()) {
                player.sendPacket(ExShape_Shifting_Result.CANCEL);
                player.setEnchantItem(null);
                player.setEnchantSupportItem(null);
                return;
            }

            if ((extracItem.getLocation() != ItemInstance.ItemLocation.INVENTORY) && (extracItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL)) {
                player.sendPacket(ExShape_Shifting_Result.CANCEL);
                player.setEnchantItem(null);
                player.setEnchantSupportItem(null);
                return;
            }

            if ((!extracItem.getTemplate().isOther()) && (targetItem.getCrystalType().ordinal() <= extracItem.getCrystalType().ordinal())) {
                player.sendPacket(ExShape_Shifting_Result.CANCEL);
                player.setEnchantItem(null);
                player.setEnchantSupportItem(null);
                return;
            }

            if (extracItem.getVisualId() > 0) {
                player.sendPacket(ExShape_Shifting_Result.CANCEL);
                player.setEnchantItem(null);
                player.setEnchantSupportItem(null);
                return;
            }

            if (targetItem.getTemplate().getExItemType() != extracItem.getTemplate().getExItemType()) {
                player.sendPacket(ExShape_Shifting_Result.CANCEL);
                player.setEnchantItem(null);
                player.setEnchantSupportItem(null);
                return;
            }

            if (extracItem.getOwnerId() != player.getObjectId()) {
                player.sendPacket(ExShape_Shifting_Result.CANCEL);
                player.setEnchantItem(null);
                player.setEnchantSupportItem(null);
                return;
            }
            extracItemId = extracItem.getItemId();
        }

        if (targetItem.getOwnerId() != player.getObjectId()) {
            player.sendPacket(ExShape_Shifting_Result.CANCEL);
            player.setEnchantItem(null);
            player.setEnchantSupportItem(null);
            return;
        }

        inventory.writeLock();
        try {
            long cost = appearanceStone.getCost();
            if (cost > player.getAdena()) {
                player.sendPacket(SystemMessageId.YOU_CANNOT_MODIFY_AS_YOU_DO_NOT_HAVE_ENOUGH_ADENA);
                player.sendPacket(ExShape_Shifting_Result.CANCEL);
                player.setEnchantItem(null);
                player.setEnchantSupportItem(null);
                return;
            }
            if (stone.getCount() < 1L) {
                player.sendPacket(ExShape_Shifting_Result.CANCEL);
                player.setEnchantItem(null);
                player.setEnchantSupportItem(null);
                return;
            }
            if (appearanceStone.getType() == AppearanceStone.ShapeType.NORMAL) {
                if (!inventory.destroyItem(extracItem, 1L)) {
                    player.sendPacket(ExShape_Shifting_Result.CANCEL);
                    player.setEnchantItem(null);
                    player.setEnchantSupportItem(null);
                    return;
                }
            }
            inventory.destroyItem(stone, 1L);
            player.reduceAdena(cost);

            boolean equipped = false;
            if ((equipped = targetItem.isEquipped())) {
                inventory.isRefresh = true;
                inventory.unEquipItem(targetItem);
            }

            switch (appearanceStone.getType().ordinal()) {
                case 1:
                    targetItem.setVisualId(0);
                    break;
                case 2:
                case 3:
                    targetItem.setVisualId(extracItem.getItemId());
                    break;
                case 4:
                    targetItem.setVisualId(appearanceStone.getExtractItemId());
            }

            targetItem.setJdbcState(JdbcEntityState.UPDATED);
            targetItem.update();

            if (equipped) {
                inventory.equipItem(targetItem);
                inventory.isRefresh = false;
            }
            player.sendPacket(SystemMessageId.YOU_HAVE_SPENT_S1_ON_A_SUCCESSFUL_APPEARANCE_MODIFICATION.clone().addNumber(cost));
        } finally {
            inventory.writeUnlock();
        }

        player.sendPacket(new InventoryUpdate().addModifiedItem(targetItem));

        player.setEnchantItem(null);
        player.setEnchantSupportItem(null);
        player.sendPacket(new ExShape_Shifting_Result(targetItem.getItemId(), extracItemId));
    }

    protected void readImpl() {
        _targetItemObjId = readD();
    }
}
