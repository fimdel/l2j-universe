package l2p.gameserver.ccpGuard;


import l2p.gameserver.ccpGuard.commons.config.BaseConfig;
import l2p.gameserver.ccpGuard.commons.utils.NetList;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;


public class ConfigProtect extends BaseConfig {
    protected static Logger _log = Logger.getLogger(ConfigProtect.class.getName());

    /**
     * Argot's Protection config
     */
    public static boolean PROTECT_ENABLE;
    public static boolean PROTECT_DEBUG;
    public static NetList PROTECT_UNPROTECTED_IPS;
    public static boolean PROTECT_GS_STORE_HWID;
    public static boolean PROTECT_ENABLE_HWID_BANS;
    public static int PROTECT_CONST_VALUE;
    public static int PROTECT_WINDOWS_COUNT;
    public static int PROTECT_PROTOCOL_VERSION;
    public static boolean PROTECT_KICK_WITH_EMPTY_HWID;
    public static boolean PROTECT_KICK_WITH_LASTERROR_HWID;
    public static boolean PROTECT_ENABLE_HWID_LOCK;
    public static boolean PROTECT_ENABLE_GG_SYSTEM;
    public static long PROTECT_GG_SEND_INTERVAL;
    public static long PROTECT_GG_RECV_INTERVAL;
    public static long PROTECT_TASK_GG_INVERVAL;
    public static int PROTECT_TOTAL_PENALTY;
    public static String PROTECT_HTML_SHOW;
    public static int PROTECT_PENALTY_IG;
    public static int PROTECT_PENALTY_BOT;
    public static int PROTECT_PENALTY_ACP;
    public static int PROTECT_PENALTY_CONSOLE_CMD;
    public static int PROTECT_PENALTY_L2PHX;
    public static int PROTECT_PENALTY_L2CONTROL;
    public static int PROTECT_PUNISHMENT_ILLEGAL_SOFT;
    public static boolean PROTECT_HWID_PROBLEM_RESOLVED;

    public static int PROTECT_BITS_HOOKER;
    public static int PROTECT_BITS_AIKA;
    public static int PROTECT_BITS_ERROR;


    public static String HOST_L2EXT;
    public static int PORT_L2EXT;


    public static String PROTECT_SERVER_TITLE;
    public static int PROTECT_SERVER_TITLE_X;
    public static int PROTECT_SERVER_TITLE_Y;
    public static int PROTECT_SERVER_TITLE_COLOR;

    public static int PROTECT_ONLINE_PACKET_TIME;
    public static int PROTECT_ONLINE_PACKET_X;
    public static int PROTECT_ONLINE_PACKET_Y;
    public static int PROTECT_ONLINE_PACKET_COLOR;

    public static int PROTECT_PING_PACKET_TIME;
    public static int PROTECT_PING_PACKET_X;
    public static int PROTECT_PING_PACKET_Y;
    public static int PROTECT_PING_PACKET_COLOR;


    public static int PROTECT_UPDATER;
    public static int PROTECT_OPCODE;
    public static boolean DDOS_PROTECTION_ENABLED;
    public static String DDOS_PROTECTION_COMMAND;


    public static final String PROTECT_FILE = "./config/CCP/protection.properties";

    public static void load() {
        File fp = new File(PROTECT_FILE);
        PROTECT_ENABLE = fp.exists();
        if (PROTECT_ENABLE)
            try {
                Properties protectSettings = new Properties();
                InputStream is = new FileInputStream(fp);
                protectSettings.load(is);
                is.close();
                PROTECT_UNPROTECTED_IPS = new NetList();

                String _ips = getProperty(protectSettings, "UpProtectedIPs", "");
                if (_ips.equals(""))
                    PROTECT_UNPROTECTED_IPS.LoadFromString(_ips, ",");

                PROTECT_HTML_SHOW = getProperty(protectSettings, "ShowHtml", "none");
                //возможность отключить защиту при наличии файла PROTECT_FILE
                PROTECT_ENABLE = getBooleanProperty(protectSettings, "EnableProtect", true);
                //не выносить в конфиг, только при необходимости
                PROTECT_CONST_VALUE = getIntProperty(protectSettings, "ServerConst", 0);
                //ограничение окон для одного компьютера
                PROTECT_WINDOWS_COUNT = getIntProperty(protectSettings, "AllowedWindowsCount", 99);
                PROTECT_PROTOCOL_VERSION = getIntProperty(protectSettings, "ProtectProtocolVersion", 777);
                PROTECT_KICK_WITH_EMPTY_HWID = getBooleanProperty(protectSettings, "KickWithEmptyHWID", true);
                PROTECT_KICK_WITH_LASTERROR_HWID = getBooleanProperty(protectSettings, "KickWithLastErrorHWID", false);
                PROTECT_ENABLE_HWID_LOCK = getBooleanProperty(protectSettings, "EnableHWIDLock", false);
                PROTECT_ENABLE_GG_SYSTEM = getBooleanProperty(protectSettings, "EnableGGSystem", true);
                PROTECT_DEBUG = getBooleanProperty(protectSettings, "ProtectDebug", false);
                //не выносить в конфиг. сообщать о ней, только в случае проблем
                PROTECT_HWID_PROBLEM_RESOLVED = getBooleanProperty(protectSettings, "HwidProblemResolved", false);
                //ToDo
                PROTECT_GG_SEND_INTERVAL = getLongProperty(protectSettings, "GGSendInterval", 60000);
                PROTECT_GG_RECV_INTERVAL = getLongProperty(protectSettings, "GGRecvInterval", 8000);
                PROTECT_TASK_GG_INVERVAL = getLongProperty(protectSettings, "GGTaskInterval", 5000);
                PROTECT_TOTAL_PENALTY = getIntProperty(protectSettings, "TotalPenaltyPoint", 10);
                PROTECT_HTML_SHOW = getProperty(protectSettings, "ShowHtml", "none");
                PROTECT_PUNISHMENT_ILLEGAL_SOFT = getIntProperty(protectSettings, "PunishmentIllegalSoft", 1);
                PROTECT_PENALTY_IG = getIntProperty(protectSettings, "PenaltyIG", 10);
                PROTECT_PENALTY_BOT = getIntProperty(protectSettings, "PenaltyBot", 10);
                PROTECT_PENALTY_L2PHX = getIntProperty(protectSettings, "PenaltyL2phx", 10);
                PROTECT_PENALTY_L2CONTROL = getIntProperty(protectSettings, "PenaltyL2Control", 10);
                PROTECT_PENALTY_CONSOLE_CMD = getIntProperty(protectSettings, "PenaltyConsoleCMD", 1);
                PROTECT_ONLINE_PACKET_TIME = getIntProperty(protectSettings, "OnlinePacketTime", 0);

                PROTECT_SERVER_TITLE = getProperty(protectSettings, "ServerTitle", null);
                PROTECT_SERVER_TITLE_X = getIntProperty(protectSettings, "ServerTitlePosX", 260);
                PROTECT_SERVER_TITLE_Y = getIntProperty(protectSettings, "ServerTitlePosY", 8);
                PROTECT_SERVER_TITLE_COLOR = getIntHexProperty(protectSettings, "ServerTitleColor", 0xFF00FF00);

                PROTECT_ONLINE_PACKET_TIME = getIntProperty(protectSettings, "OnlinePacketTime", 0);
                PROTECT_ONLINE_PACKET_X = getIntProperty(protectSettings, "OnlinePacketPosX", 260);
                PROTECT_ONLINE_PACKET_Y = getIntProperty(protectSettings, "OnlinePacketPosY", 20);
                PROTECT_ONLINE_PACKET_COLOR = getIntHexProperty(protectSettings, "OnlinePacketColor", 0xFF00FF00);

                PROTECT_PING_PACKET_TIME = getIntProperty(protectSettings, "PingPacketTime", 0);
                PROTECT_PING_PACKET_X = getIntProperty(protectSettings, "PingPacketPosX", 260);
                PROTECT_PING_PACKET_Y = getIntProperty(protectSettings, "PingPacketPosY", 32);
                PROTECT_PING_PACKET_COLOR = getIntHexProperty(protectSettings, "PingPacketColor", 0xFF00FF00);

                DDOS_PROTECTION_ENABLED = getBooleanProperty(protectSettings, "EnablePortKnock", false);
                DDOS_PROTECTION_COMMAND = getProperty(protectSettings, "PortKnockCommand", "ipset -A %ip% game_ok");

                PROTECT_UPDATER = getIntProperty(protectSettings, "UpdaterCheck", 0);
                PROTECT_OPCODE = getIntProperty(protectSettings, "UpdaterOpcode", 0);

                PROTECT_BITS_HOOKER = getIntProperty(protectSettings, "PenaltyHooker", 10);
                PROTECT_BITS_AIKA = getIntProperty(protectSettings, "PenaltyAika", 10);
                PROTECT_BITS_ERROR = getIntProperty(protectSettings, "PenaltyBitsError", 0);


            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    // it has no instancies
    private ConfigProtect() {
    }

}
