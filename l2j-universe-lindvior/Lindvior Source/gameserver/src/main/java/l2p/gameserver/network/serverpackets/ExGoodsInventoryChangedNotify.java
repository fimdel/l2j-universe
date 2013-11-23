/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

/**
 * @author VISTALL
 * @date 11:33/03.07.2011
 */
public class ExGoodsInventoryChangedNotify extends L2GameServerPacket {
    public static final L2GameServerPacket STATIC = new ExGoodsInventoryChangedNotify();

    @Override
    protected void writeImpl() {
        writeEx(0x111);
        writeC(1);
    }
}
