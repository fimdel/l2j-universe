package l2p.gameserver.network.serverpackets;

/**
 * Даный пакет показывает менюшку для ввода серийника. Можно что-то придумать :)
 * Format: (ch)
 */
public class ShowPCCafeCouponShowUI extends L2GameServerPacket {
    @Override
    protected final void writeImpl() {
        writeEx449(0x44);
    }
}