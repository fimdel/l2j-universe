package l2p.loginserver.clientpackets;

import l2p.commons.net.nio.impl.ReceivablePacket;
import l2p.loginserver.L2LoginClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class L2LoginClientPacket extends ReceivablePacket<L2LoginClient> {
    private static Logger _log = LoggerFactory.getLogger(L2LoginClientPacket.class);

    @Override
    protected final boolean read() {
        try {
            readImpl();
            return true;
        } catch (Exception e) {
            _log.error("", e);
            return false;
        }
    }

    @Override
    public void run() {
        try {
            runImpl();
        } catch (Exception e) {
            _log.error("", e);
        }
    }

    protected abstract void readImpl();

    protected abstract void runImpl() throws Exception;
}
