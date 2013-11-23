package l2p.gameserver.network.serverpackets;

/**
 * Format: ch
 * Протокол 828: при отправке пакета клиенту ничего не происходит.
 */
public class ExPlayScene extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx449(0x5c);
        writeD(0x00); //Kamael
    }
}