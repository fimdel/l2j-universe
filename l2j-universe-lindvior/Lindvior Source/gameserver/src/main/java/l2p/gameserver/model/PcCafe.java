package l2p.gameserver.model;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.Config;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PcCafe {
    private static final Logger _log = LoggerFactory.getLogger(PcCafe.class);

    private static final String SELECT_PCCAFE_CODE = "SELECT serial_code, coupon_use, coupon_value FROM pccafe_coupons WHERE serial_code=?";
    private static final String UPDATE_PCCAFE_CODE = "UPDATE pccafe_coupons SET coupon_use=?, used_by=? WHERE serial_code=?";

    public static void requestEnterCode(Player player, String couponCode) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;

        int use, value = 0;

        if (!Util.isMatchingRegexp(couponCode, Config.ALT_PCBANG_POINTS_COUPON_TEMPLATE)) {
            player.sendMessage(player.isLangRus() ? "Не верный формат кода купона. Попробуйте повторить ввод." : "Wrong coupon code format. Try to enter serial number again.");
            player.setVar("PCC_CODE_ATTEMPTS", player.getVarInt("PCC_CODE_ATTEMPTS") + 1, System.currentTimeMillis() / 1000 + Config.ALT_PCBANG_POINTS_BAN_TIME * 60);
            return;
        }

        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(SELECT_PCCAFE_CODE);
            statement.setString(1, couponCode);
            rset = statement.executeQuery();
            if (rset.next()) {
                use = rset.getInt("coupon_use");
                value = rset.getInt("coupon_value");

                if (use != 0) {
                    player.sendMessage(player.isLangRus() ? "Данный купон уже был использован. Введите верный номер купона." : "This coupon already has been used. Please enter true number of the coupon.");
                    player.setVar("PCC_CODE_ATTEMPTS", player.getVarInt("PCC_CODE_ATTEMPTS") + 1, System.currentTimeMillis() / 1000 + Config.ALT_PCBANG_POINTS_BAN_TIME * 60);
                } else
                    calculateCodeReward(player, couponCode, value);
            } else {
                player.sendMessage(player.isLangRus() ? "Не верный код купона. Попробуйте повторить ввод." : "Wrong coupon code. Try once again.");
                player.setVar("PCC_CODE_ATTEMPTS", player.getVarInt("PCC_CODE_ATTEMPTS") + 1, System.currentTimeMillis() / 1000 + Config.ALT_PCBANG_POINTS_BAN_TIME * 60);
            }
            statement.close();
        } catch (Exception e) {
            _log.info("Error while reading serial code.", e);
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
        }
    }

    private static void calculateCodeReward(Player player, String couponCode, int value) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(UPDATE_PCCAFE_CODE);
            statement.setInt(1, 1);
            statement.setString(2, player.getName());
            statement.setString(3, couponCode);
            statement.execute();
            statement.close();

            player.unsetVar("PCC_CODE_ATTEMPTS");
            player.addPcBangPoints(value, false);
        } catch (Exception e) {
            _log.info("Error while calculating reward for serial code.", e);
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
        }
    }
}
