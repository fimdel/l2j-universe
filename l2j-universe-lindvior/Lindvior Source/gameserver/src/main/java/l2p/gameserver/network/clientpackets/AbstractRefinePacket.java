/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.model.items.etcitems.LifeStoneGrade;
import l2p.gameserver.model.items.etcitems.LifeStoneInfo;
import l2p.gameserver.model.items.etcitems.LifeStoneManager;
import l2p.gameserver.templates.item.type.ItemGrade;

/**
 * @author ALF
 * @date 28.06.2012
 */
public abstract class AbstractRefinePacket extends L2GameClientPacket {
    protected static final int GEMSTONE_D = 2130;
    protected static final int GEMSTONE_C = 2131;
    protected static final int GEMSTONE_B = 2132;
    protected static final int GEMSTONE_A = 2133;

    protected static final boolean isValid(Player player, ItemInstance item, ItemInstance refinerItem, ItemInstance gemStones) {
        if (!isValid(player, item, refinerItem))
            return false;

        if (gemStones.getOwnerId() != player.getObjectId())
            return false;

        if (gemStones.getLocation() != ItemInstance.ItemLocation.INVENTORY)
            return false;

        final ItemGrade grade = item.getTemplate().getItemGrade();

        LifeStoneInfo ls = LifeStoneManager.getStoneInfo(refinerItem.getItemId());

        if (getGemStoneId(grade) != gemStones.getItemId())
            return false;

        if (getGemStoneCount(ls.getGrade(), grade) > gemStones.getCount())
            return false;

        return true;
    }

    protected static final boolean isValid(Player player, ItemInstance item, ItemInstance refinerItem) {
        if (!isValid(player, item))
            return false;

        if (refinerItem.getLocation() != ItemInstance.ItemLocation.INVENTORY)
            return false;

        LifeStoneInfo ls = LifeStoneManager.getStoneInfo(refinerItem.getItemId());

        if (player.getLevel() < ls.getLevel())
            return false;

        if (!item.canBeAugmented(player, ls.getGrade()))
            return false;

        return true;
    }

    protected static final boolean isValid(Player player, ItemInstance item) {
        if (!isValid(player))
            return false;

        // Source item can be equipped or in inventory
        switch (item.getLocation()) {
            case INVENTORY:
            case PAPERDOLL:
                break;
            default:
                return false;
        }

        return true;
    }

    protected static final boolean isValid(Player player) {
        if (player.isActionsDisabled())
            return false;

        if (player.isInStoreMode())
            return false;

        if (player.isInTrade())
            return false;

        if (player.getLevel() < 46)
            return false;

        return true;
    }

    protected static final int getGemStoneId(ItemGrade grade) {
        switch (grade) {
            case C:
            case B:
                return GEMSTONE_D;
            case A:
            case S:
                return GEMSTONE_C;
            case S80:
            case S84:
                return GEMSTONE_B;
            case R:
            case R95:
            case R99:
                return GEMSTONE_A;
            default:
                return 0;
        }
    }

    protected static final int getGemStoneCount(LifeStoneGrade lsGrade, ItemGrade itemGrade) {
        switch (lsGrade) {
            case ACCESSORY: {
                switch (itemGrade) {
                    case C:
                        return 200;
                    case B:
                        return 300;
                    case A:
                        return 200;
                    case S:
                        return 250;
                    case S80:
                        return 360;
                    case S84:
                        return 480;
                    case R:
                        return 26;
                    case R95:
                        return 90;
                    case R99:
                        return 236;
                    default:
                        return 0;
                }
            }
            case UNDERWEAR: {
                return 26;
            }
            default: {
                switch (itemGrade) {
                    case C:
                        return 20;
                    case B:
                        return 30;
                    case A:
                        return 20;
                    case S:
                        return 25;
                    case S80:
                        return 36;
                    case S84:
                        return 48;
                    case R:
                        return 13;
                    case R95:
                        return 15;
                    case R99:
                        return 118;
                    default:
                        return 0;
                }
            }

        }
    }
}
