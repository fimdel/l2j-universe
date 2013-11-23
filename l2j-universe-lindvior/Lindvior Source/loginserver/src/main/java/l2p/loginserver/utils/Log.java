package l2p.loginserver.utils;

import l2p.commons.dbutils.DbUtils;
import l2p.loginserver.Config;
import l2p.loginserver.accounts.Account;
import l2p.loginserver.database.L2DatabaseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Log {
    private final static Logger _log = LoggerFactory.getLogger(Log.class);

    public static void LogAccount(Account account) {
        if (!Config.LOGIN_LOG)
            return;

        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = L2DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("INSERT INTO account_log (time, login, ip) VALUES(?,?,?)");
            statement.setInt(1, account.getLastAccess());
            statement.setString(2, account.getLogin());
            statement.setString(3, account.getLastIP());
            statement.execute();
        } catch (Exception e) {
            _log.error("", e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }
}
