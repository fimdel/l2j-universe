package l2p.gameserver.ccpGuard.managers;


import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.ccpGuard.ProtectInfo;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.GameClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HwidManager {

    private static HwidManager _instance;
    private static final Logger _log = Logger.getLogger(HwidManager.class.getName());
    static Map<Integer, HwidInfo> _listHWID;

    public static HwidManager getInstance() {
        if (_instance == null) {
            _instance = new HwidManager();
        }
        return _instance;
    }

    public HwidManager() {
        _listHWID = new HashMap<Integer, HwidInfo>();
        load();
        _log.config("HWIDManager: Loaded " + _listHWID.size() + " HWIDs");
    }

    private void load() {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("SELECT * FROM hwid_info");
            rset = statement.executeQuery();
            int counterHwidInfo = 0;
            while (rset.next()) {
                final HwidInfo hInfo = new HwidInfo(counterHwidInfo);
                hInfo.setHwids(rset.getString("HWID"));
                hInfo.setCount(rset.getInt("WindowsCount"));
                hInfo.setLogin(rset.getString("Account"));
                hInfo.setPlayerID(rset.getInt("PlayerID"));
                hInfo.setLockType(HwidInfo.LockType.valueOf(rset.getString("LockType")));
                _listHWID.put(counterHwidInfo, hInfo);
                counterHwidInfo++;
            }
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
        }
    }

    public static void reload() {
        _instance = new HwidManager();
    }

    public static boolean checkLockedHWID(final ProtectInfo pi) {
        if (_listHWID.size() == 0) {
            return false;
        }
        boolean result = false;
        for (int i = 0; i < _listHWID.size(); i++) {
            switch (_listHWID.get(i).getLockType()) {
                case NONE:
                    break;
                case PLAYER_LOCK:
                    if (pi.getPlayerId() != 0 && _listHWID.get(i).getPlayerID() == pi.getPlayerId()) {
                        if (_listHWID.get(i).getHWID().equals(pi.getHWID())) {
                            return false;
                        } else {
                            result = true;
                        }
                    }
                    break;
                case ACCOUNT_LOCK:
                    if (_listHWID.get(i).getLogin().equals(pi.getLoginName())) {
                        if (_listHWID.get(i).getHWID().equals(pi.getHWID())) {
                            return false;
                        } else {
                            result = true;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    public static int getAllowedWindowsCount(final ProtectInfo pi) {
        if (_listHWID.size() == 0) {
            return -1;
        }
        for (int i = 0; i < _listHWID.size(); i++) {
            if (_listHWID.get(i).getHWID().equals(pi.getHWID())) {
                if (_listHWID.get(i).getHWID().equals("")) {
                    return -1;
                } else {
                    return _listHWID.get(i).getCount();
                }
            }
        }
        return -1;
    }

    public static int getCountHwidInfo() {
        return _listHWID.size();
    }

    public static void updateHwidInfo(final Player player, final HwidInfo.LockType lockType) {
        updateHwidInfo(player, 1, lockType);
    }

    public static void updateHwidInfo(final Player player, final int windowscount) {
        updateHwidInfo(player, windowscount, HwidInfo.LockType.NONE);
    }

    public static void updateHwidInfo(final Player player, final int windowsCount, final HwidInfo.LockType lockType) {
        final GameClient client = player.getNetConnection();
        int counterHwidInfo = _listHWID.size();
        boolean isFound = false;
        for (int i = 0; i < _listHWID.size(); i++) {
            if (_listHWID.get(i).getHWID().equals(client._prot_info.getHWID())) {
                isFound = true;
                counterHwidInfo = i;
                break;
            }
        }
        Connection con = null;
        PreparedStatement statement = null;

        final HwidInfo hInfo = new HwidInfo(counterHwidInfo);
        hInfo.setHwids(client._prot_info.getHWID());
        hInfo.setCount(windowsCount);
        hInfo.setLogin(player.getAccountName());
        hInfo.setPlayerID(player.getObjectId());
        hInfo.setLockType(lockType);
        _listHWID.put(counterHwidInfo, hInfo);
        if (isFound) {
            try {
                con = DatabaseFactory.getInstance().getConnection();
                statement = con.prepareStatement("UPDATE hwid_info SET WindowsCount=?,Account=?,PlayerID=?,LockType=? WHERE HWID=?");
                statement.setInt(1, windowsCount);
                statement.setString(2, player.getAccountName());
                statement.setInt(3, player.getObjectId());
                statement.setString(4, lockType.toString());
                statement.setString(5, client._prot_info.getHWID());
                statement.execute();
            } catch (final Exception e) {
                e.printStackTrace();
            } finally {
                DbUtils.closeQuietly(con, statement);
            }
        } else {
            try {
                con = DatabaseFactory.getInstance().getConnection();
                statement = con.prepareStatement("INSERT INTO hwid_info (HWID, WindowsCount, Account, PlayerID, LockType) values (?,?,?,?,?)");
                statement.setString(1, client._prot_info.getHWID());
                statement.setInt(2, windowsCount);
                statement.setString(3, player.getAccountName());
                statement.setInt(4, player.getObjectId());
                statement.setString(5, lockType.toString());
                statement.execute();
            } catch (final Exception e) {
                e.printStackTrace();
            } finally {
                DbUtils.closeQuietly(con, statement);
            }
        }
    }
}
