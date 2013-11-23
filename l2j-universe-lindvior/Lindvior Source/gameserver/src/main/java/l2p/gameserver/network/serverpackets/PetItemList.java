package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.instances.PetInstance;
import l2p.gameserver.model.items.ItemInstance;

public class PetItemList extends L2GameServerPacket {
    private ItemInstance[] items;

    public PetItemList(PetInstance cha) {
        items = cha.getInventory().getItems();
    }

    @Override
    protected final void writeImpl() {
        writeC(0xb3);
        writeH(items.length);

        for (ItemInstance item : items)
            writeItemInfo(item);
    }
}