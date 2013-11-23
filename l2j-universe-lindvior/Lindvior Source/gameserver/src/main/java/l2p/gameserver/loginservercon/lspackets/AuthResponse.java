package l2p.gameserver.loginservercon.lspackets;

import l2p.gameserver.loginservercon.LoginServerCommunication;
import l2p.gameserver.loginservercon.ReceivablePacket;
import l2p.gameserver.loginservercon.gspackets.OnlineStatus;
import l2p.gameserver.loginservercon.gspackets.PlayerInGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthResponse extends ReceivablePacket {
    private static final Logger _log = LoggerFactory.getLogger(AuthResponse.class);

    private int _serverId;
    private String _serverName;

    @Override
    protected void readImpl() {
        _serverId = readC();
        _serverName = readS();
    }

    @Override
    protected void runImpl() {
        _log.info("Registered on loginserver as " + _serverId + " [" + _serverName + "]");

        sendPacket(new OnlineStatus(true));

        String[] accounts = LoginServerCommunication.getInstance().getAccounts();
        for (String account : accounts)
            sendPacket(new PlayerInGame(account));
    }
}
