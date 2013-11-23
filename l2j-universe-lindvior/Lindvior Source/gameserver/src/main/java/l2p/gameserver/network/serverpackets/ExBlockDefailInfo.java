/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package l2p.gameserver.network.serverpackets;

/**
 * @author Bonux
 */
public class ExBlockDefailInfo extends L2GameServerPacket {
    private final String _blockName;
    private final String _blockMemo;

    public ExBlockDefailInfo(String name, String memo) {
        _blockName = name;
        _blockMemo = memo;

    }

    @Override
    protected final void writeImpl() {
        writeEx(0xEF);
        writeS(_blockName);
        writeS(_blockMemo);
    }
}
