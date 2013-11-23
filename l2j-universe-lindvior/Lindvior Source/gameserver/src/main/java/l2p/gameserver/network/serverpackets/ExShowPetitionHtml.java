package l2p.gameserver.network.serverpackets;

public class ExShowPetitionHtml extends L2GameServerPacket {
    @Override
    protected void writeImpl() {
        writeEx449(0xB1);
        // TODO dx[dcS]
    }
}