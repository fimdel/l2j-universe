/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.PremiumItem;
import l2p.gameserver.network.serverpackets.ExGoodsInventoryInfo;
import l2p.gameserver.network.serverpackets.ExGoodsInventoryResult;
import l2p.gameserver.utils.ItemFunctions;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 24.07.12
 * Time: 11:15
 */
public class RequestUseGoodsInventoryItem extends L2GameClientPacket {
    private long _itemNum;
    private int _unk1;
    private long _itemcount;

    @Override
    protected void readImpl() {
        _unk1 = readC();
        _itemNum = readQ();
        if (_unk1 != 1) {
            _itemcount = readQ();
        }
    }

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;
        if (activeChar.getPrivateStoreType() != 0) {
            activeChar.sendPacket(new ExGoodsInventoryResult(-5));
            return;
        }

        if (activeChar.getPremiumItemList().isEmpty()) {
            activeChar.sendPacket(new ExGoodsInventoryResult(-6));
            return;
        }

        if (activeChar.getInventory().getSize() >= activeChar.getInventoryLimit() * 0.8) {
            activeChar.sendPacket(new ExGoodsInventoryResult(-3));
            return;
        }

        PremiumItem _item = activeChar.getPremiumItemList().get((int) _itemNum);

        if (_item == null) {
            return;
        }
        if ((_itemcount != 0L) && (_item.getCount() < _itemcount)) {
            return;
        }

        ItemFunctions.addItem(activeChar, _item.getItemId(), _itemcount, true);

        long itemsLeft = _item.getCount() - _itemcount;

        if (_itemcount < _item.getCount()) {
            _item.updateCount(itemsLeft);
            activeChar.updatePremiumItem((int) _itemNum, _item.getCount() - _itemcount);
        } else {
            activeChar.deletePremiumItem((int) _itemNum);
        }

        activeChar.sendPacket(new ExGoodsInventoryInfo(activeChar.getPremiumItemList()));
    }

    @Override
    public String getType() {
        return "[C] D0:B2 RequestUseGoodsInventoryItem";
    }
}
