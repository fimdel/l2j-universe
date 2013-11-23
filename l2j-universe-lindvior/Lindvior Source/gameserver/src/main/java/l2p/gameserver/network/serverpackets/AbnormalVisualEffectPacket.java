package l2p.gameserver.network.serverpackets;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 17.06.12
 * Time: 1:32
 */
public class AbnormalVisualEffectPacket extends L2GameServerPacket {

    protected final void writeImpl() {
        writeC(0xA5);
    }

}
