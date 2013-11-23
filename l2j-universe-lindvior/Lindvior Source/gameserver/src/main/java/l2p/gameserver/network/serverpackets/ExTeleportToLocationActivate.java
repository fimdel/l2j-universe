/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

public class ExTeleportToLocationActivate extends L2GameServerPacket {

    @Override
    protected final void writeImpl() {
        writeEx(0x154);
    }
}
