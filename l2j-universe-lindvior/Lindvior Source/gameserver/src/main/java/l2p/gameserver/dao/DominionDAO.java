package l2p.gameserver.dao;

import l2p.commons.dao.JdbcEntityState;
import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.model.entity.residence.Dominion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author VISTALL
 * @date 18:10/15.04.2011
 */
public class DominionDAO {
    private static final Logger _log = LoggerFactory.getLogger(DominionDAO.class);
    private static final DominionDAO _instance = new DominionDAO();

    public static final String SELECT_SQL_QUERY = "SELECT lord_object_id, wards FROM dominion WHERE id=?";
    public static final String UPDATE_SQL_QUERY = "UPDATE dominion SET lord_object_id=?, wards=? WHERE id=?";

    public static DominionDAO getInstance() {
        return _instance;
    }

    public void select(Dominion dominion) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(SELECT_SQL_QUERY);
            statement.setInt(1, dominion.getId());
            rset = statement.executeQuery();
            if (rset.next()) {
                dominion.setLordObjectId(rset.getInt("lord_object_id"));

                String flags = rset.getString("wards");
                if (!flags.isEmpty()) {
                    String[] values = flags.split(";");
                    for (int i = 0; i < values.length; i++)
                        dominion.addFlag(Integer.parseInt(values[i]));
                }
            }
        } catch (Exception e) {
            _log.error("Dominion.loadData(): " + e, e);
        } finally {
            DbUtils.closeQuietly(con, statement, rset);
        }
    }

    public void update(Dominion residence) {
        if (!residence.getJdbcState().isUpdatable())
            return;

        residence.setJdbcState(JdbcEntityState.STORED);
        update0(residence);
    }

    private void update0(Dominion dominion) {
        String wardsString = "";
        int[] flags = dominion.getFlags();
        if (flags.length > 0)
            for (int flag : flags)
                wardsString += flag + ";";

        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(UPDATE_SQL_QUERY);
            statement.setInt(1, dominion.getLordObjectId());
            statement.setString(2, wardsString);
            statement.setInt(3, dominion.getId());
            statement.execute();
        } catch (Exception e) {
            _log.warn("DominionDAO#update0(Dominion): " + e, e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }
}
