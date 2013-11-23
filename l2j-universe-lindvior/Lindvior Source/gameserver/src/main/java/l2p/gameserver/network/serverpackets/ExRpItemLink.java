package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.items.ItemInfo;

/**
 * ddQhdhhhhhdhhhhhhhh - Gracia Final
 */
public class ExRpItemLink extends L2GameServerPacket {
    private ItemInfo _item;

    public ExRpItemLink(ItemInfo item) {
        _item = item;
    }

    @Override
    protected final void writeImpl() {
        writeEx449(0x6c);
        writeItemInfo(_item);
    }
}