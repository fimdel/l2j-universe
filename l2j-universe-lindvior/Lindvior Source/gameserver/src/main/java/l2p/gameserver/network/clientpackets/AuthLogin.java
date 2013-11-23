package l2p.gameserver.network.clientpackets;

import l2p.gameserver.Shutdown;
import l2p.gameserver.loginservercon.LoginServerCommunication;
import l2p.gameserver.loginservercon.SessionKey;
import l2p.gameserver.loginservercon.gspackets.PlayerAuthRequest;
import l2p.gameserver.network.GameClient;
import l2p.gameserver.network.serverpackets.LoginFail;
import l2p.gameserver.network.serverpackets.ServerClose;

/**
 * cSddddd
 * cSdddddQ
 * loginName + keys must match what the loginserver used.
 */
public class AuthLogin extends L2GameClientPacket {
    private String _loginName;
    private int _playKey1;
    private int _playKey2;
    private int _loginKey1;
    private int _loginKey2;
    private byte[] _data = new byte[108];


    @Override
    protected void readImpl() {
        _loginName = readS(32).toLowerCase();
        _playKey2 = readD();
        _playKey1 = readD();
        _loginKey1 = readD();
        _loginKey2 = readD();
        l2p.gameserver.ccpGuard.Protection.doReadAuthLogin(getClient(), _buf, _data);

    }

    @Override
    protected void runImpl() {
        GameClient client = getClient();

        if (!l2p.gameserver.ccpGuard.Protection.doAuthLogin(getClient(), _data, _loginName))
            return;

        SessionKey key = new SessionKey(_loginKey1, _loginKey2, _playKey1, _playKey2);
        client.setSessionId(key);
        client.setLoginName(_loginName);

        if (Shutdown.getInstance().getMode() != Shutdown.NONE && Shutdown.getInstance().getSeconds() <= 15)
            client.closeNow(false);
        else {
            if (LoginServerCommunication.getInstance().isShutdown()) {
                client.close(new LoginFail(LoginFail.SYSTEM_ERROR_LOGIN_LATER));
                return;
            }

            GameClient oldClient = LoginServerCommunication.getInstance().addWaitingClient(client);
            if (oldClient != null)
                oldClient.close(ServerClose.STATIC);

            LoginServerCommunication.getInstance().sendPacket(new PlayerAuthRequest(client));
        }
    }
}
