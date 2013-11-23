package l2p.gameserver.ccpGuard.managers;


import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.ccpGuard.ConfigProtect;
import l2p.gameserver.ccpGuard.ProtectInfo;
import l2p.gameserver.ccpGuard.commons.utils.Log;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.network.GameClient;
import l2p.gameserver.network.serverpackets.GameGuardQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

public class ProtectManager {

    protected static Logger _log = Logger.getLogger(ProtectManager.class.getName());
    private static String _logFile = "ProtectManager";
    private static String _logMainFile = "protection_logs";
    private static ProtectManager _instance;
    private static ScheduledFuture<?> _GGTask = null;

    private class InfoSet {

        public String _playerName = "";
        public long _lastGGSendTime;
        public long _lastGGRecvTime;
        public int _attempts;
        public String _HWID = "";

        public InfoSet(final String name, final String HWID) {
            _playerName = name;
            _lastGGSendTime = System.currentTimeMillis();
            _lastGGRecvTime = _lastGGSendTime;
            _attempts = 0;
            _HWID = HWID;
        }
    }

    private static ConcurrentHashMap<String, InfoSet> _objects = new ConcurrentHashMap<String, InfoSet>();

    class GGTask implements Runnable {

        public void run() {
            long time = System.currentTimeMillis();
            for (final InfoSet object : _objects.values()) {
                //Нужно ли отправить пакет на ГГ
                if (time - object._lastGGSendTime >= ConfigProtect.PROTECT_GG_SEND_INTERVAL) {
                    try {
                        World.getPlayer(object._playerName).sendPacket(new GameGuardQuery());
                        object._lastGGSendTime = time;
                        //ToDo сделать через перменную lock
                        object._lastGGRecvTime = time + ConfigProtect.PROTECT_GG_RECV_INTERVAL + 1;
                    } catch (final Exception e) {
                        removePlayer(object._playerName);
                    }
                }
                //ToDo через перменную lock, заблокировать до следующей проверки
                //Проверка пришёл ли ответ от клиента
                if (time - object._lastGGRecvTime >= ConfigProtect.PROTECT_GG_RECV_INTERVAL) {
                    try {
                        final Player player = World.getPlayer(object._playerName);
                        if (!player.getNetConnection()._prot_info.getGGStatus()) {
                            //если не пришёл увеличиваем кол-во
                            if (object._attempts < 3) {
                                object._attempts++;
                            } else {
                                if (player != null) {
                                    final GameClient client = player.getNetConnection();
                                    Log.add("Player was kicked because GG packet not receive (3 attempts)|" + client._prot_info.toString(), _logMainFile);
                                }
                                player.kick();
                            }
                        }
                        object._lastGGRecvTime = time;
                    } catch (final Exception e) {
                        removePlayer(object._playerName);
                    }
                }
            }

            time = System.currentTimeMillis() - time;
            if (time > ConfigProtect.PROTECT_TASK_GG_INVERVAL) {
                Log.add("ALERT! TASK_SAVE_INTERVAL is too small, time to save characters in Queue = " + time + ", Config=" + ConfigProtect.PROTECT_TASK_GG_INVERVAL, _logMainFile);
            }
        }
    }

    public void addPlayer(ProtectInfo pi) {
        if (_objects.containsKey(pi.getPlayerName()) && ConfigProtect.PROTECT_DEBUG) {
            Log.add("trying to add player that already exists", _logFile);
            return;
        }
        storeHWID(pi);
        _objects.put(pi.getPlayerName(), new InfoSet(pi.getPlayerName(), pi.getHWID()));

        if (ConfigProtect.PROTECT_DEBUG) {
            Log.add(pi.toString(), _logFile);
        }
    }

    public void removePlayer(final String name) {
        if (!_objects.containsKey(name)) {
            if (ConfigProtect.PROTECT_DEBUG) {
                Log.add("trying to remove player that non exists : " + name, _logFile);
            }
        } else {
            _objects.remove(name);
        }
    }

    public ProtectManager() {
        startGGTask();
    }

    public static void Shutdown() {
        stopGGTask(false);
    }

    public static ProtectManager getInstance() {
        if (_instance == null) {
            _log.info("Initializing ProtectManager");
            _instance = new ProtectManager();
        }
        return _instance;
    }

    public void startGGTask() {
        stopGGTask(true);
        if (ConfigProtect.PROTECT_ENABLE_GG_SYSTEM) {
            _GGTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new GGTask(), ConfigProtect.PROTECT_TASK_GG_INVERVAL, ConfigProtect.PROTECT_TASK_GG_INVERVAL);
        }
    }

    public static void stopGGTask(final boolean mayInterruptIfRunning) {
        if (_GGTask != null) {
            try {
                _GGTask.cancel(mayInterruptIfRunning);
            } catch (Exception e) {
            }
            _GGTask = null;
        }
    }

    public int getCountByHWID(final String HWID) {
        int result = 0;
        for (final InfoSet object : _objects.values()) {
            if (object._HWID.equals(HWID)) {
                result++;
            }
        }
        return result;
    }

    public ArrayList<String> getNamesByHWID(final String HWID) {
        final ArrayList<String> names = new ArrayList<String>();
        for (final InfoSet object : _objects.values()) {
            if (object._HWID.equals(HWID)) {
                names.add(object._playerName);
            }
        }
        return names;
    }

    public void storeHWID(final ProtectInfo pi) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("UPDATE characters SET LastHWID=? WHERE obj_Id=?");
            statement.setString(1, pi.getHWID());
            statement.setInt(2, pi.getPlayerId());
            statement.execute();
        } catch (final Exception e) {
            _log.warning("could not store characters HWID:" + e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }
}
