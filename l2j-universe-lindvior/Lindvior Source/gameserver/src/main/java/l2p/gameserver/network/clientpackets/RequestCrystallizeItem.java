/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.CrystallizationInfo;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.Log;

import java.util.Collection;

public class RequestCrystallizeItem extends L2GameClientPacket {
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
        ItemInstance item = activeChar.getInventory().getItemByObjectId(this._objectId);
        if (item == null) {
            activeChar.sendActionFailed();
            return;
        }

        Log.LogItem(activeChar, "Crystalize", item);

        if (!activeChar.getInventory().destroyItemByObjectId(this._objectId, this._count)) {
            activeChar.sendActionFailed();
            return;
        }

        activeChar.sendPacket(SystemMsg.THE_ITEM_HAS_BEEN_SUCCESSFULLY_CRYSTALLIZED);

        int crystalId = item.getTemplate()._crystalType.getCrystalId();
        int crystalCount = item.getCrystalCountOnCrystallize();
        if ((crystalId > 0) && (crystalCount > 0)) {
            ItemFunctions.addItem(activeChar, crystalId, crystalCount, true);
        }
        Collection<CrystallizationInfo.CrystallizationItem> items = item.getTemplate().getCrystallizationInfo().getItems();
        for (CrystallizationInfo.CrystallizationItem i : items) {
            if (Rnd.chance(i.getChance()))
                ItemFunctions.addItem(activeChar, i.getItemId(), i.getCount(), true);
        }
        activeChar.sendChanges();
    }
}
