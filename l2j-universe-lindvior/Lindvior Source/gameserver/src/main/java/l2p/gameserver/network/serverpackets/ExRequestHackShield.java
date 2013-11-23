package l2p.gameserver.network.serverpackets;

public class ExRequestHackShield extends L2GameServerPacket {
    @Override
    protected final void writeImpl() {
        writeEx449(0x49);
    }
}