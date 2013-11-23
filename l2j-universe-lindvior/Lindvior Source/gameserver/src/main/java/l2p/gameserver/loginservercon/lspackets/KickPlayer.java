package l2p.gameserver.loginservercon.lspackets;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.loginservercon.LoginServerCommunication;
import l2p.gameserver.loginservercon.ReceivablePacket;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.GameClient;
import l2p.gameserver.network.serverpackets.ServerClose;

public class KickPlayer extends ReceivablePacket {
    String account;

    @Override
    public void readImpl() {
        account = readS();
    }

    @Override
    protected void runImpl() {
        GameClient client = LoginServerCommunication.getInstance().removeWaitingClient(account);
        if (client == null)
            client = LoginServerCommunication.getInstance().removeAuthedClient(account);
        if (client == null)
            return;

        Player activeChar = client.getActiveChar();
        if (activeChar != null) {
            //FIXME [G1ta0] сообщение чаще всего не показывается, т.к. при закрытии соединения очередь на отправку очищается
            activeChar.sendPacket(Msg.ANOTHER_PERSON_HAS_LOGGED_IN_WITH_THE_SAME_ACCOUNT);
            activeChar.kick();
        } else
            client.close(ServerClose.STATIC);
    }
}