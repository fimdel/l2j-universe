package l2p.loginserver;

import l2p.commons.net.nio.impl.IAcceptFilter;
import l2p.commons.net.nio.impl.IClientFactory;
import l2p.commons.net.nio.impl.IMMOExecutor;
import l2p.commons.net.nio.impl.MMOConnection;
import l2p.commons.threading.RunnableImpl;
import l2p.loginserver.serverpackets.Init;

import java.nio.channels.SocketChannel;

public class SelectorHelper implements IMMOExecutor<L2LoginClient>, IClientFactory<L2LoginClient>, IAcceptFilter {
    @Override
    public void execute(Runnable r) {
        ThreadPoolManager.getInstance().execute(r);
    }

    @Override
    public L2LoginClient create(MMOConnection<L2LoginClient> con) {
        final L2LoginClient client = new L2LoginClient(con);
        client.sendPacket(new Init(client));
        ThreadPoolManager.getInstance().schedule(new RunnableImpl() {
            @Override
            public void runImpl() {
                client.closeNow(false);
            }
        }, Config.LOGIN_TIMEOUT);
        return client;
    }

    @Override
    public boolean accept(SocketChannel sc) {
        return !IpBanManager.getInstance().isIpBanned(sc.socket().getInetAddress().getHostAddress());
    }
}