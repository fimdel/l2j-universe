package l2p.gameserver.network.serverpackets;

public class ExPutEnchantTargetItemResult extends L2GameServerPacket {
    public static final L2GameServerPacket FAIL = new ExPutEnchantTargetItemResult(0);
    public static final L2GameServerPacket SUCCESS = new ExPutEnchantTargetItemResult(1);

    private int _result;

    public ExPutEnchantTargetItemResult(int result) {
        _result = result;
    }

    @Override
    protected void writeImpl() {
        writeEx449(0x81);
        writeD(_result);
    }
}