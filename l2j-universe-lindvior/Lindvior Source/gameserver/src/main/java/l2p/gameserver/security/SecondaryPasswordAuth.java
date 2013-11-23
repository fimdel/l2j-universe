package l2p.gameserver.security;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.Base64;
import l2p.gameserver.Config;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.network.GameClient;
import l2p.gameserver.network.serverpackets.Ex2ndPasswordAck;
import l2p.gameserver.network.serverpackets.Ex2ndPasswordCheck;
import l2p.gameserver.network.serverpackets.Ex2ndPasswordVerify;
import l2p.gameserver.network.serverpackets.ServerClose;
import l2p.gameserver.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author ALF
 * @data 09.02.2012
 */
public class SecondaryPasswordAuth {
    private static final Logger _log = LoggerFactory.getLogger(SecondaryPasswordAuth.class);

    private final GameClient _activeClient;

    private String _password;
    private int _wrongAttempts;
    private boolean _authed;

    private static final String VAR_PWD = "secauth_pwd";
    private static final String VAR_WTE = "secauth_wte";

    private static final String SELECT_PASSWORD = "SELECT var, value FROM account_gsdata WHERE account_name=? AND var LIKE 'secauth_%'";
    private static final String INSERT_PASSWORD = "INSERT INTO account_gsdata VALUES (?, ?, ?)";
    private static final String UPDATE_PASSWORD = "UPDATE account_gsdata SET value=? WHERE account_name=? AND var=?";

    private static final String INSERT_ATTEMPT = "INSERT INTO account_gsdata VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE value=?";

    public SecondaryPasswordAuth(GameClient activeClient) {
        _activeClient = activeClient;
        _password = null;
        _wrongAttempts = 0;
        _authed = false;
        loadPassword();
    }

    private void loadPassword() {
        String var, value = null;

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(SELECT_PASSWORD);
            statement.setString(1, _activeClient.getLogin());
            rs = statement.executeQuery();
            while (rs.next()) {
                var = rs.getString("var");
                value = rs.getString("value");

                if (var.equals(VAR_PWD))
                    _password = value;
                else if (var.equals(VAR_WTE))
                    _wrongAttempts = Integer.parseInt(value);
            }
        } catch (Exception e) {
            _log.warn("Error while reading password." + e);
        } finally {
            DbUtils.closeQuietly(con, statement, rs);
        }
    }

    public boolean savePassword(String password) {
        if (passwordExist()) {
            _log.warn("[SecondaryPasswordAuth]" + _activeClient.getLogin() + " forced savePassword");
            _activeClient.close(new ServerClose());
            return false;
        }

        if (!validatePassword(password)) {
            _activeClient.sendPacket(new Ex2ndPasswordAck(Ex2ndPasswordAck.WRONG_PATTERN));
            return false;
        }

        password = cryptPassword(password);

        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(INSERT_PASSWORD);
            statement.setString(1, _activeClient.getLogin());
            statement.setString(2, VAR_PWD);
            statement.setString(3, password);
            statement.execute();
        } catch (Exception e) {
            _log.warn("Error while writing password." + e);
            return false;
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
        _password = password;
        return true;
    }

    public boolean insertWrongAttempt(int attempts) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(INSERT_ATTEMPT);
            statement.setString(1, _activeClient.getLogin());
            statement.setString(2, VAR_WTE);
            statement.setString(3, Integer.toString(attempts));
            statement.setString(4, Integer.toString(attempts));
            statement.execute();
        } catch (Exception e) {
            _log.warn("Error while writing wrong attempts." + e);
            return false;
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
        return true;
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (!passwordExist()) {
            _log.warn("[SecondaryPasswordAuth]" + _activeClient.getLogin() + " forced changePassword");
            _activeClient.close(new ServerClose());
            return false;
        }

        if (!checkPassword(oldPassword, true))
            return false;

        if (!validatePassword(newPassword)) {
            _activeClient.sendPacket(new Ex2ndPasswordAck(Ex2ndPasswordAck.WRONG_PATTERN));
            return false;
        }

        newPassword = cryptPassword(newPassword);

        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(UPDATE_PASSWORD);
            statement.setString(1, newPassword);
            statement.setString(2, _activeClient.getLogin());
            statement.setString(3, VAR_PWD);
            statement.execute();
        } catch (Exception e) {
            _log.warn("Error while reading password." + e);
            return false;
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
        _password = newPassword;
        _authed = false;
        return true;
    }

    public boolean checkPassword(String password, boolean skipAuth) {
        password = cryptPassword(password);

        if (!password.equals(_password)) {
            _wrongAttempts++;
            if (_wrongAttempts < Config.SECOND_AUTH_MAX_ATTEMPTS) {
                _activeClient.sendPacket(new Ex2ndPasswordVerify(Ex2ndPasswordVerify.PASSWORD_WRONG, _wrongAttempts));
                insertWrongAttempt(_wrongAttempts);
                return false;
            } else {
                _activeClient.close(new Ex2ndPasswordVerify(Ex2ndPasswordVerify.PASSWORD_BAN, Config.SECOND_AUTH_MAX_ATTEMPTS));
                return false;
            }
        }
        if (!skipAuth) {
            _authed = true;
            _activeClient.sendPacket(new Ex2ndPasswordVerify(Ex2ndPasswordVerify.PASSWORD_OK, _wrongAttempts));
        }
        insertWrongAttempt(0);
        return true;
    }

    public boolean passwordExist() {
        return _password == null ? false : true;
    }

    public void openDialog() {
        if (passwordExist())
            _activeClient.sendPacket(new Ex2ndPasswordCheck(Ex2ndPasswordCheck.PASSWORD_PROMPT));
        else
            _activeClient.sendPacket(new Ex2ndPasswordCheck(Ex2ndPasswordCheck.PASSWORD_NEW));
    }

    public boolean isAuthed() {
        return _authed;
    }

    private String cryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] raw = password.getBytes("UTF-8");
            byte[] hash = md.digest(raw);
            return Base64.encodeBytes(hash);
        } catch (NoSuchAlgorithmException e) {
            _log.warn("[SecondaryPasswordAuth]Unsupported Algorythm");
        } catch (UnsupportedEncodingException e) {
            _log.warn("[SecondaryPasswordAuth]Unsupported Encoding");
        }
        return null;
    }

    private boolean validatePassword(String password) {
        if (!Util.isDigit(password))
            return false;

        if (password.length() < 6 || password.length() > 8)
            return false;

        //TODO: эта проверка НЕ РАБОЧАЯ!!!
        /*for (int i = 0; i < password.length()-1; i++)
          {
              char curCh = password.charAt(i);
              char nxtCh = password.charAt(i+1);

              if (curCh+1 == nxtCh)
                  return false;
              else if (curCh-1 == nxtCh)
                  return false;
              else if (curCh == nxtCh)
                  return false;
          }

          for (int i = 0; i < password.length()-2; i++)
          {
              String toChk = password.substring(i+1);
              StringBuffer chkEr = new StringBuffer(password.substring(i, i+2));

              if (toChk.contains(chkEr))
                  return false;
              else if (toChk.contains(chkEr.reverse()))
                  return false;
          }*/
        _wrongAttempts = 0;
        return true;
    }
}
