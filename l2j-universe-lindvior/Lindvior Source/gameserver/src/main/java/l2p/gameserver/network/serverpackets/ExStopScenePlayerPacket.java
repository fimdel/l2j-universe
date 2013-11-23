package l2p.gameserver.network.serverpackets;

/**
 * @author Bonux
 */
public class ExStopScenePlayerPacket extends L2GameServerPacket {
    private final int _movieId;

    public ExStopScenePlayerPacket(int movieId) {
        _movieId = movieId;
    }

    @Override
    protected final void writeImpl() {
        writeEx449(0xE6);
        writeD(_movieId);
    }
}
