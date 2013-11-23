package l2p.gameserver.loginservercon.gspackets;

import l2p.gameserver.loginservercon.SendablePacket;

public class OnlineStatus extends SendablePacket {
    private boolean _online;

    public OnlineStatus(boolean online) {
        _online = online;
    }

    @Override
    protected void writeImpl() {
        writeC(0x01);
        writeC(_online ? 1 : 0);
    }
}
