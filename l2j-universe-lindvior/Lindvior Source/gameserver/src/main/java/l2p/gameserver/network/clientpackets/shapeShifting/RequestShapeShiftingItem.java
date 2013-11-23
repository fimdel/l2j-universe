/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets.shapeShifting;

import l2p.commons.dao.JdbcEntityState;
import l2p.gameserver.data.xml.holder.EnchantItemHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.PcInventory;
import l2p.gameserver.network.clientpackets.L2GameClientPacket;
import l2p.gameserver.network.serverpackets.InventoryUpdate;
import l2p.gameserver.network.serverpackets.Shape_Shifting.ExShape_Shifting_Result;
import l2p.gameserver.network.serverpackets.SystemMessage2;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.templates.item.support.AppearanceStone;
import l2p.gameserver.templates.item.type.ExItemType;
import l2p.gameserver.templates.item.type.ItemGrade;
import org.apache.commons.lang3.ArrayUtils;

public class RequestShapeShiftingItem extends L2GameClientPacket {
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
            player.setAppearanceExtractItem(null);
            return;
        }

        PcInventory inventory = player.getInventory();
        ItemInstance targetItem = inventory.getItemByObjectId(_targetItemObjId);
        ItemInstance stone = player.getAppearanceStone();
        if (targetItem == null || stone == null) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            player.setAppearanceExtractItem(null);
            return;
        }

        if (stone.getOwnerId() != player.getObjectId()) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            player.setAppearanceExtractItem(null);
            return;
        }

        if (!targetItem.canBeAppearance()) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            player.setAppearanceExtractItem(null);
            return;
        }

        if (targetItem.getLocation() != ItemInstance.ItemLocation.INVENTORY && targetItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            player.setAppearanceExtractItem(null);
            return;
        }

        if ((stone = inventory.getItemByObjectId(stone.getObjectId())) == null) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            player.setAppearanceExtractItem(null);
            return;
        }

        AppearanceStone appearanceStone = EnchantItemHolder.getInstance().getAppearanceStone(stone.getItemId());
        if (appearanceStone == null) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            player.setAppearanceExtractItem(null);
            return;
        }

        if (appearanceStone.getType() != AppearanceStone.ShapeType.RESTORE && targetItem.getVisualId() > 0 || appearanceStone.getType() == AppearanceStone.ShapeType.RESTORE && targetItem.getVisualId() == 0) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            player.setAppearanceExtractItem(null);
            return;
        }

        if (!targetItem.isOther() && targetItem.getTemplate().getItemGrade() == ItemGrade.NONE) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            player.setAppearanceExtractItem(null);
            return;
        }

        ItemGrade[] stoneGrades = appearanceStone.getGrades();
        if (stoneGrades != null && stoneGrades.length > 0) {
            if (!ArrayUtils.contains(stoneGrades, targetItem.getTemplate().getItemGrade())) {
                player.sendPacket(ExShape_Shifting_Result.FAIL);
                player.setAppearanceStone(null);
                player.setAppearanceExtractItem(null);
                return;
            }
        }

        AppearanceStone.ShapeTargetType[] targetTypes = appearanceStone.getTargetTypes();
        if (targetTypes == null || targetTypes.length == 0) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            player.setAppearanceExtractItem(null);
            return;
        }

        if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.ALL)) {
            if (targetItem.isWeapon()) {
                if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.WEAPON)) {
                    player.sendPacket(ExShape_Shifting_Result.FAIL);
                    player.setAppearanceStone(null);
                    player.setAppearanceExtractItem(null);
                    return;
                }
            } else if (targetItem.isArmor()) {
                if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.ARMOR)) {
                    player.sendPacket(ExShape_Shifting_Result.FAIL);
                    player.setAppearanceStone(null);
                    player.setAppearanceExtractItem(null);
                    return;
                }
            } else {
                if (!ArrayUtils.contains(targetTypes, AppearanceStone.ShapeTargetType.ACCESSORY)) {
                    player.sendPacket(ExShape_Shifting_Result.FAIL);
                    player.setAppearanceStone(null);
                    player.setAppearanceExtractItem(null);
                    return;
                }
            }
        }

        ExItemType[] itemTypes = appearanceStone.getItemTypes();
        if (itemTypes != null && itemTypes.length > 0) {
            if (!ArrayUtils.contains(itemTypes, targetItem.getExItemType())) {
                player.sendPacket(ExShape_Shifting_Result.FAIL);
                player.setAppearanceStone(null);
                player.setAppearanceExtractItem(null);
                return;
            }
        }

        ItemInstance extracItem = player.getAppearanceExtractItem();
        int extracItemId = 0;
        if (appearanceStone.getType() != AppearanceStone.ShapeType.RESTORE && appearanceStone.getType() != AppearanceStone.ShapeType.FIXED) {
            if (extracItem == null) {
                player.sendPacket(ExShape_Shifting_Result.FAIL);
                player.setAppearanceStone(null);
                player.setAppearanceExtractItem(null);
                return;
            }

            if (!extracItem.canBeAppearance()) {
                player.sendPacket(ExShape_Shifting_Result.FAIL);
                player.setAppearanceStone(null);
                player.setAppearanceExtractItem(null);
                return;
            }

            if (extracItem.getLocation() != ItemInstance.ItemLocation.INVENTORY && extracItem.getLocation() != ItemInstance.ItemLocation.PAPERDOLL) {
                player.sendPacket(ExShape_Shifting_Result.FAIL);
                player.setAppearanceStone(null);
                player.setAppearanceExtractItem(null);
                return;
            }

            /*if(!extracItem.isOther() && extracItem.getGrade() == ItemGrade.NONE)
               {
                   player.sendPacket(ExShape_Shifting_Result.FAIL);
                   player.setAppearanceStone(null);
                   player.setAppearanceExtractItem(null);
                   return;
               }*/

            if (!extracItem.isOther() && targetItem.getTemplate().getItemGrade().ordinal() <= extracItem.getTemplate().getItemGrade().ordinal()) {
                player.sendPacket(ExShape_Shifting_Result.FAIL);
                player.setAppearanceStone(null);
                player.setAppearanceExtractItem(null);
                return;
            }

            if (extracItem.getVisualId() > 0) {
                player.sendPacket(ExShape_Shifting_Result.FAIL);
                player.setAppearanceStone(null);
                player.setAppearanceExtractItem(null);
                return;
            }

            if (targetItem.getExItemType() != extracItem.getExItemType()) {
                player.sendPacket(ExShape_Shifting_Result.FAIL);
                player.setAppearanceStone(null);
                player.setAppearanceExtractItem(null);
                return;
            }

            // Запрет на обработку чужих вещей, баг может вылезти на серверных лагах
            if (extracItem.getOwnerId() != player.getObjectId()) {
                player.sendPacket(ExShape_Shifting_Result.FAIL);
                player.setAppearanceStone(null);
                player.setAppearanceExtractItem(null);
                return;
            }
            extracItemId = extracItem.getItemId();
        }

        // Запрет на обработку чужих вещей, баг может вылезти на серверных лагах
        if (targetItem.getOwnerId() != player.getObjectId()) {
            player.sendPacket(ExShape_Shifting_Result.FAIL);
            player.setAppearanceStone(null);
            player.setAppearanceExtractItem(null);
            return;
        }

        inventory.writeLock();
        try {
            long cost = appearanceStone.getCost();
            if (cost > player.getAdena()) {
                player.sendPacket(SystemMsg.YOU_CANNOT_MODIFY_AS_YOU_DO_NOT_HAVE_ENOUGH_ADENA);
                player.sendPacket(ExShape_Shifting_Result.FAIL);
                player.setAppearanceStone(null);
                player.setAppearanceExtractItem(null);
                return;
            }

            if (stone.getCount() < 1L) {
                player.sendPacket(ExShape_Shifting_Result.FAIL);
                player.setAppearanceStone(null);
                player.setAppearanceExtractItem(null);
                return;
            }

            if (appearanceStone.getType() == AppearanceStone.ShapeType.NORMAL) {
                if (!inventory.destroyItem(extracItem, 1L)) {
                    player.sendPacket(ExShape_Shifting_Result.FAIL);
                    player.setAppearanceStone(null);
                    player.setAppearanceExtractItem(null);
                    return;
                }
            }

            inventory.destroyItem(stone, 1);
            player.reduceAdena(cost);

            boolean equipped = false;
            if (equipped = targetItem.isEquipped()) {
                inventory.isRefresh = true;
                inventory.unEquipItem(targetItem);
            }

            switch (appearanceStone.getType()) {
                case RESTORE:
                    targetItem.setVisualId(0);
                    break;
                case NORMAL:
                    targetItem.setVisualId(extracItem.getItemId());
                    break;
                case BLESSED:
                    targetItem.setVisualId(extracItem.getItemId());
                    break;
                case FIXED:
                    targetItem.setVisualId(appearanceStone.getExtractItemId());
                    break;
            }

            targetItem.setJdbcState(JdbcEntityState.UPDATED);
            targetItem.update();

            if (equipped) {
                inventory.equipItem(targetItem);
                inventory.isRefresh = false;
            }
            player.sendPacket(new SystemMessage2(SystemMsg.YOU_HAVE_SPENT_S1_ON_A_SUCCESSFUL_APPEARANCE_MODIFICATION).addLong(cost));
        } finally {
            inventory.writeUnlock();
        }

        //player.sendPacket(new InventoryUpdate().addModifiedItem(player, targetItem));
        player.sendPacket(new InventoryUpdate().addModifiedItem(targetItem));
        //player.sendPacket(new IStaticPacket[] { new ExShape_Shifting_Result(targetItem.getItemId(), extracItem.getVisualId()), new UserInfo(player), new ExBR_ExtraUserInfo(player) });

        player.setAppearanceStone(null);
        player.setAppearanceExtractItem(null);
        player.sendPacket(new ExShape_Shifting_Result(ExShape_Shifting_Result.SUCCESS_RESULT, targetItem.getItemId(), extracItemId));
    }
}
