/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package handler.items;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.MagicSkillUse;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.tables.SkillTable;

public class Potions extends SimpleItemHandler {
    private static final int[] ITEM_IDS = new int[]{7906, 7907, 7908, 7909, 7910, 7911, 9997, 9998, 9999, 10000, 10001, 10002, 14612};

    @Override
    public int[] getItemIds() {
        return ITEM_IDS;
    }

    @Override
    protected boolean useItemImpl(Player player, ItemInstance item, boolean ctrl) {
        int itemId = item.getItemId();

        if (player.isInOlympiadMode()) {
            player.sendPacket(new SystemMessage(SystemMessage.S1_CANNOT_BE_USED_DUE_TO_UNSUITABLE_TERMS).addItemName(itemId));
            return false;
        }

        if (!useItem(player, item, 1))
            return false;

        switch (itemId) {
            //Blessing of Fire
            case 7906:
                player.broadcastPacket(new MagicSkillUse(player, player, 2248, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2248, 1));
                break;
            //Blessing of Water
            case 7907:
                player.broadcastPacket(new MagicSkillUse(player, player, 2249, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2249, 1));
                break;
            //Blessing of Wind
            case 7908:
                player.broadcastPacket(new MagicSkillUse(player, player, 2250, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2250, 1));
                break;
            //Blessing of Earth
            case 7909:
                player.broadcastPacket(new MagicSkillUse(player, player, 2251, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2251, 1));
                break;
            //Blessing of Darkness
            case 7910:
                player.broadcastPacket(new MagicSkillUse(player, player, 2252, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2252, 1));
                break;
            //Blessing of Sanctity
            case 7911:
                player.broadcastPacket(new MagicSkillUse(player, player, 2253, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2253, 1));
                break;
            //Fire Resist Potion
            case 9997:
                player.broadcastPacket(new MagicSkillUse(player, player, 2335, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2335, 1));
                break;
            //Water Resist Potion
            case 9998:
                player.broadcastPacket(new MagicSkillUse(player, player, 2336, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2336, 1));
                break;
            //Earth Resist Potion
            case 9999:
                player.broadcastPacket(new MagicSkillUse(player, player, 2338, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2338, 1));
                break;
            //Wind Resist Potion
            case 10000:
                player.broadcastPacket(new MagicSkillUse(player, player, 2337, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2337, 1));
                break;
            //Darkness Resist Potion
            case 10001:
                player.broadcastPacket(new MagicSkillUse(player, player, 2340, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2340, 1));
                break;
            //Holy Resist Potion
            case 10002:
                player.broadcastPacket(new MagicSkillUse(player, player, 2339, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(2339, 1));
                break;
            // Christmas Red Sock
            case 14612:
                player.broadcastPacket(new MagicSkillUse(player, player, 23017, 1, 0, 0));
                player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(23017, 1));
                break;
            default:
                return false;
        }

        return true;
    }
}
