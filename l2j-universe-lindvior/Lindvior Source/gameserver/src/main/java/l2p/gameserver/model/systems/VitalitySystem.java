/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.model.systems;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.Config;
import l2p.gameserver.cache.Msg;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.model.Player;
import l2p.gameserver.network.serverpackets.ExVitalityPointInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ALF
 * @date 23.10.2012
 */
public class VitalitySystem {
    private int _points;
    private int _items;
    private final Player _player;

    public VitalitySystem(Player player) {
        _player = player;
    }

    public double getExpMod() {
        // Виталка не действует когда нету очков виталити
        if (_points == 0)
            return 1;

        // Если игрок премиум EXP х3.
        if (_player.getPremiumPoints() > 0)
            return Config.ALT_VITALITY_PREMIUM_RATE_EXP;

        // В других случаях EXP x2
        return Config.ALT_VITALITY_RATE_EXP;
    }

    public double getSpMod() {
        // Виталка не действует когда нету очков виталити
        if (_points == 0)
            return 1;

        // Если игрок премиум SP х2.
        if (_player.getPremiumPoints() > 0)
            return Config.ALT_VITALITY_PREMIUM_RATE_SP;

        // В других случаях SP x2
        return Config.ALT_VITALITY_RATE_SP;
    }

    public double getDropMod() {
        if (_points == 0)
            return 1;

        if (_player.getPremiumPoints() > 0)
            return Config.ALT_VITALITY_PREMIUM_RATE_DROP;

        return Config.ALT_VITALITY_RATE_DROP;
    }

    public int getPoints() {
        return _points;
    }

    public void decPoints(int c) {
        if (_player.getLevel() < 10)
            return;

        if (_points - c < 0) {
            _points = 0;
            _player.sendPacket(new ExVitalityPointInfo(_points));
            _player.sendPacket(Msg.VITALITY_IS_FULLY_EXHAUSTED);
            return;
        }

        _points -= c;
        _player.sendPacket(new ExVitalityPointInfo(_points));
    }

    public void incPoints(int c) {
        if (_points + c > Config.MAX_VITALITY) {
            _points = Config.MAX_VITALITY;
            _player.sendPacket(new ExVitalityPointInfo(_points));
            _player.sendPacket(Msg.YOUR_VITALITY_IS_AT_MAXIMUM);
            return;
        }

        _points += c;
        _player.sendPacket(new ExVitalityPointInfo(_points));
    }

    public int getItems() {
        return _items;
    }

    public void decItems() {
        if (_items == 0)
            return;
        _items--;
    }

    public void store() {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("UPDATE `vitality_points` SET `points`=?, `items`=? WHERE `account_name`=?");
            statement.setInt(1, _points);
            statement.setInt(2, _items);
            statement.setString(3, _player.getAccountName());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    public void restore() {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;

        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement("SELECT * FROM `vitality_points` WHERE `account_name`=?");
            statement.setString(1, _player.getAccountName());
            rset = statement.executeQuery();
            if (rset.next()) {
                _points = rset.getInt("points");
                _items = rset.getInt("items");
            } else {
                DbUtils.closeQuietly(statement);
                _points = Config.MAX_VITALITY;
                statement = con.prepareStatement("INSERT IGNORE INTO `vitality_points` (account_name, points, items) VALUES (?,?,?)");
                statement.setString(1, _player.getAccountName());
                statement.setInt(2, Config.MAX_VITALITY);
                statement.setInt(3, Config.MAX_VITALITY_ITEMS);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
        }
    }

    public void setMaximumPoints() {
        _points = Config.MAX_VITALITY;
    }

}
