/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;

public class ExAdenaInvenCount extends L2GameServerPacket {
    public int invsize;
    public long count;

    public ExAdenaInvenCount(Player pl) {
        count = pl.getInventory().getAdena();
        invsize = pl.getInventory().getSize();
    }

    @Override
    protected void writeImpl() {
        writeEx(0x148);
        writeQ(count);
        writeD(invsize);
    }
}
