package l2p.gameserver.network.clientpackets;

import l2p.gameserver.instancemanager.itemauction.ItemAuction;
import l2p.gameserver.instancemanager.itemauction.ItemAuctionInstance;
import l2p.gameserver.instancemanager.itemauction.ItemAuctionManager;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.ExItemAuctionInfo;

/**
 * @author n0nam3
 */
public final class RequestInfoItemAuction extends L2GameClientPacket {
    private int _instanceId;

    @Override
    protected final void readImpl() {
        _instanceId = readD();
    }

    @Override
    protected final void runImpl() {
        final Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        activeChar.getAndSetLastItemAuctionRequest();

        final ItemAuctionInstance instance = ItemAuctionManager.getInstance().getManagerInstance(_instanceId);
        if (instance == null)
            return;

        final ItemAuction auction = instance.getCurrentAuction();
        NpcInstance broker = activeChar.getLastNpc();
        if (auction == null || broker == null || broker.getNpcId() != _instanceId || activeChar.getDistance(broker.getX(), broker.getY()) > Creature.INTERACTION_DISTANCE)
            return;

        activeChar.sendPacket(new ExItemAuctionInfo(true, auction, instance.getNextAuction()));
    }
}