package l2p.gameserver.network.clientpackets;

import l2p.gameserver.cache.ItemInfoCache;
import l2p.gameserver.model.items.ItemInfo;
import l2p.gameserver.network.serverpackets.ActionFail;
import l2p.gameserver.network.serverpackets.ExRpItemLink;

public class RequestExRqItemLink extends L2GameClientPacket {
    private int _objectId;

    @Override
    protected void readImpl() {
        _objectId = readD();
    }

    @Override
    protected void runImpl() {
        ItemInfo item;
        if ((item = ItemInfoCache.getInstance().get(_objectId)) == null)
            sendPacket(ActionFail.STATIC);
        else
            sendPacket(new ExRpItemLink(item));
    }
}