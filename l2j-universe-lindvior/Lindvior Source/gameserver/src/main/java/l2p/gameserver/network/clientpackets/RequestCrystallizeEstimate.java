/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.ExGetCrystalizingEstimation;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Darvin
 */
public class RequestCrystallizeEstimate extends L2GameClientPacket {
    private int _objectId;
    private long _count;

    protected void readImpl() {
        this._objectId = readD();
        this._count = readQ();
    }

    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();

        if (activeChar == null) {
            return;
        }
        if (activeChar.isActionsDisabled()) {
            activeChar.sendActionFailed();
            return;
        }

        if (activeChar.isInTrade()) {
            activeChar.sendActionFailed();
            return;
        }

        ItemInstance item = activeChar.getInventory().getItemByObjectId(this._objectId);
        if (item == null) {
            activeChar.sendActionFailed();
            return;
        }

        if (!item.canBeCrystallized(activeChar)) {
            activeChar.sendPacket(SystemMsg.THIS_ITEM_CANNOT_BE_CRYSTALLIZED);
            return;
        }

        if (activeChar.isInStoreMode()) {
            activeChar.sendPacket(SystemMsg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
            return;
        }

        if (activeChar.isFishing()) {
            activeChar.sendPacket(SystemMsg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
            return;
        }

        int externalOrdinal = item.getTemplate()._crystalType.getExtOrdinal();

        int level = activeChar.getSkillLevel(248);
        if ((level < 1) || (externalOrdinal > level)) {
            activeChar.sendPacket(SystemMsg.YOU_MAY_NOT_CRYSTALLIZE_THIS_ITEM);
            activeChar.sendActionFailed();
            return;
        }

        activeChar.sendPacket(new ExGetCrystalizingEstimation(item));
    }
}
