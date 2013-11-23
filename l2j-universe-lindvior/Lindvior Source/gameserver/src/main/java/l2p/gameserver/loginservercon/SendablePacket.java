package l2p.gameserver.loginservercon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

public abstract class SendablePacket extends l2p.commons.net.nio.SendablePacket<LoginServerCommunication> {
    private static final Logger _log = LoggerFactory.getLogger(SendablePacket.class);

    @Override
    public LoginServerCommunication getClient() {
        return LoginServerCommunication.getInstance();
    }

    @Override
    protected ByteBuffer getByteBuffer() {
        return getClient().getWriteBuffer();
    }

    @Override
    public boolean write() {
        try {
            writeImpl();
        } catch (Exception e) {
            _log.error("", e);
        }
        return true;
    }

    protected abstract void writeImpl();
}
