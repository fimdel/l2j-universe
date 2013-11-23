/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.SystemMsg;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ItemFunctions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author cruel
 */
public final class AngelCatInstance extends NpcInstance {
    private static final String VAR_DATA = "angelCat_buff";
    private static final String SELECT_DATA = "SELECT var, value FROM account_gsdata WHERE account_name=? AND var LIKE 'angelCat_buff'";
    private static final String INSERT_DATA = "INSERT INTO account_gsdata VALUES (?, ?, ?)";
    private static final String DELETE_DATA = "DELETE FROM account_gsdata WHERE account_name=?";

    public AngelCatInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this)) {
            return;
        }

        if (command.equalsIgnoreCase("give_buff")) {
            if (loadInfo(player)) {
                addInfo(player);
                ItemFunctions.addItem(player, 35669, 1, true);
            } else {
                player.sendPacket(SystemMsg.THIS_ACCOUNT_HAS_ALREADY_RECEIVED_A_GIFT);
            }
        } else {
            super.onBypassFeedback(player, command);
        }
    }

    private boolean loadInfo(Player player) {
        String var, value = null;

        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(SELECT_DATA);
            statement.setString(1, player.getAccountName());
            rs = statement.executeQuery();
            while (rs.next()) {
                var = rs.getString("var");
                value = rs.getString("value");
                long l = (System.currentTimeMillis() - Long.parseLong(value)) / 1000;
                if (l < 86400) {
                    return false;
                } else {
                    deleteInfo(player.getAccountName());
                }
            }
        } catch (Exception e) {
            return false;
        } finally {
            DbUtils.closeQuietly(con, statement, rs);
        }
        return true;
    }

    private void addInfo(Player player) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(INSERT_DATA);
            statement.setString(1, player.getAccountName());
            statement.setString(2, VAR_DATA);
            statement.setString(3, Long.toString(System.currentTimeMillis()));
            statement.execute();
        } catch (Exception e) {
            return;
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    public void deleteInfo(String account) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(DELETE_DATA);
            statement.setString(1, account);
            statement.execute();
        } catch (Exception e) {
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }
}