/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

import l2p.gameserver.model.Player;

public class ExUISetting extends L2GameServerPacket {
    private final byte data[];

    public ExUISetting(Player player) {
        data = player.getKeyBindings();
    }

    @Override
    protected void writeImpl() {
        writeEx(0x71);
        writeD(data.length);
        writeB(data);
    }
}
