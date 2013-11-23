package l2p.loginserver.gameservercon.lspackets;

import l2p.loginserver.gameservercon.SendablePacket;

public class PingRequest extends SendablePacket {
    @Override
    protected void writeImpl() {
        writeC(0xff);
    }
}