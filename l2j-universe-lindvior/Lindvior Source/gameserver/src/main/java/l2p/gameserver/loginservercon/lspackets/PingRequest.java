package l2p.gameserver.loginservercon.lspackets;

import l2p.gameserver.loginservercon.LoginServerCommunication;
import l2p.gameserver.loginservercon.ReceivablePacket;
import l2p.gameserver.loginservercon.gspackets.PingResponse;

public class PingRequest extends ReceivablePacket {
    @Override
    public void readImpl() {

    }

    @Override
    protected void runImpl() {
        LoginServerCommunication.getInstance().sendPacket(new PingResponse());
    }
}