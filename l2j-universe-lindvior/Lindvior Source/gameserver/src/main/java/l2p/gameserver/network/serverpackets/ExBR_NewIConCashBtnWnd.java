/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

public class ExBR_NewIConCashBtnWnd extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx(0x144);
        writeH(0);
    }
}
