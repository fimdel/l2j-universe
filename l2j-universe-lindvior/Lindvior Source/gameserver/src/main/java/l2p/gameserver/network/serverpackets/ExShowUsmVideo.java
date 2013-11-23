package l2p.gameserver.network.serverpackets;

/**
 * @author ALF
 * @data 04.02.2012
 */
public class ExShowUsmVideo extends L2GameServerPacket {

    public static int GD1_INTRO = 2;

    public static final int Q001 = 0x01; // Какие то врата красные
    public static final int Q002 = 0x03; // Какие то врата синие
    public static final int Q003 = 0x04; // Какие то типа церберы
    public static final int Q004 = 0x05; // Ниче не показывает
    public static final int Q005 = 0x06; // Богиня разрушения предлагает славу
    // тьмы
    public static final int Q006 = 0x07; // Богиня разрушения... отомстить...
    public static final int Q007 = 0x08; // Богиня разрушения... Принеси тьме
    // велику. жертву
    public static final int Q009 = 0x09; // ниче нету
    public static final int Q010 = 0x0A; // Пробуждение, начало
    public static final int Q011 = 0x0B; //
    public static final int Q012 = 0x0C; //

    private int _id;
    private final int _param1;
    private final int _param2;
    private final int _param3;

    public ExShowUsmVideo(int id) {
        _id = id;
        _param1 = 0;
        _param2 = 0;
        _param3 = 0;
    }

    public ExShowUsmVideo(int id, int param1, int param2, int param3) {
        _id = id;
        _param1 = param1;
        _param2 = param2;
        _param3 = param3;
    }

    @Override
    protected void writeImpl() {
        writeEx449(0x10D);
        writeD(_id);
        writeD(_param1);
        writeD(_param2);
        writeD(_param3);
    }

}
