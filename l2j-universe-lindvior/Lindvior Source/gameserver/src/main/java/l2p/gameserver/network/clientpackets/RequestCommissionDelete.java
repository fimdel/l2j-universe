/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.commission.CommissionShopManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.TradeItem;
import l2p.gameserver.network.serverpackets.commission.ExResponseCommissionItemList;
import l2p.gameserver.network.serverpackets.components.IStaticPacket;
import l2p.gameserver.network.serverpackets.components.SystemMsg;

import java.util.List;

/**
 * @author : Darvin
 */
public class RequestCommissionDelete extends L2GameClientPacket {
    public long _bidId;
    public int _unk2;
    public int _unk3;

    @Override
    protected void readImpl() {
        _bidId = readQ();
        _unk2 = readD();
        _unk3 = readD();

    }

    private List<TradeItem> items;

    @Override
    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        IStaticPacket msg = CommissionShopManager.getInstance().returnExpiredItems();
        if (msg != null) {
            activeChar.sendPacket(msg);
            return;
        }
        activeChar.sendPacket(new ExResponseCommissionItemList(items));
        CommissionShopManager.getInstance().showCommissionInfo(activeChar, _unk2);
        activeChar.sendPacket(SystemMsg.CANCELLATION_OF_SALE_FOR_THE_ITEM_IS_SUCCESSFUL);
    }
}
