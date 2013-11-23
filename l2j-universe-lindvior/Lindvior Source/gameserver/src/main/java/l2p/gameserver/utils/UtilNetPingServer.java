package l2p.gameserver.utils;

/**
 * Created with IntelliJ IDEA.
 * User: Mazaffaka
 * Date: 08.02.13
 * Time: 7:09
 */
public class UtilNetPingServer {
    final static String staticIP = "46.4.28.6";


    public static boolean checkServerIp() {
        //    Server.SERVER_MODE = Server.MODE_LOGINSERVER;
        l2p.gameserver.Config.load();
        String currentIP = l2p.gameserver.Config.GAMESERVER_HOSTNAME;
        String[] arrayIp = currentIP.split(", ");
        if (currentIP.equals(staticIP)) {
            return true;
        }
        return false;
    }

    public static String getServerHost() {
        String server_hosts = null;
        if (checkServerIp()) {
            server_hosts = l2p.gameserver.Config.GAMESERVER_HOSTNAME;
            return server_hosts;
        }
        server_hosts = "127.0.0.1";
        return server_hosts;
    }
}
