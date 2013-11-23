package l2p.gameserver.network.serverpackets;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 01.07.12
 * Time: 11:57
 * предположительно запрос лидеру пати на принятие найденого и согласившегося на замену игрока
 */
public class ExRegistPartySubstitute extends L2GameServerPacket {
    public static final int REGISTER_OK = 1;
    public static final int REGISTER_TIMEOUT = 0;

    private int _objId;
    private int _code;

    public ExRegistPartySubstitute(int objId, int code) {
        _objId = objId;
        _code = code;
    }

    @Override
    protected void writeImpl() {
        writeEx(0x105);
        writeD(_objId);
        writeD(_code);
    }
}
