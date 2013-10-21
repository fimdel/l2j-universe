/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.entity.residence.Fortress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FortressDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(FortressDAO.class);
	/**
	 * Field _instance.
	 */
	private static final FortressDAO _instance = new FortressDAO();
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT * FROM fortress WHERE id = ?"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT * FROM fortress WHERE id = ?";
	/**
	 * Field UPDATE_SQL_QUERY. (value is ""UPDATE fortress SET castle_id=?, state=?, cycle=?, reward_count=?, paid_cycle=?, supply_count=?, siege_date=?, last_siege_date=?, own_date=?, facility_0=?, facility_1=?, facility_2=?, facility_3=?, facility_4=? WHERE id=?"")
	 */
	public static final String UPDATE_SQL_QUERY = "UPDATE fortress SET castle_id=?, state=?, cycle=?, reward_count=?, paid_cycle=?, supply_count=?, siege_date=?, last_siege_date=?, own_date=?, facility_0=?, facility_1=?, facility_2=?, facility_3=?, facility_4=? WHERE id=?";
	
	/**
	 * Method getInstance.
	 * @return FortressDAO
	 */
	public static FortressDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method select.
	 * @param fortress Fortress
	 */
	public void select(Fortress fortress)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, fortress.getId());
			rset = statement.executeQuery();
			if (rset.next())
			{
				fortress.setFortState(rset.getInt("state"), rset.getInt("castle_id"));
				fortress.setCycle(rset.getInt("cycle"));
				fortress.setRewardCount(rset.getInt("reward_count"));
				fortress.setPaidCycle(rset.getInt("paid_cycle"));
				fortress.setSupplyCount(rset.getInt("supply_count"));
				fortress.getSiegeDate().setTimeInMillis(rset.getLong("siege_date"));
				fortress.getLastSiegeDate().setTimeInMillis(rset.getLong("last_siege_date"));
				fortress.getOwnDate().setTimeInMillis(rset.getLong("own_date"));
				for (int i = 0; i < Fortress.FACILITY_MAX; i++)
				{
					fortress.setFacilityLevel(i, rset.getInt("facility_" + i));
				}
			}
		}
		catch (Exception e)
		{
			_log.error("FortressDAO.select(Fortress):" + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method update.
	 * @param fortress Fortress
	 */
	public void update(Fortress fortress)
	{
		if (!fortress.getJdbcState().isUpdatable())
		{
			return;
		}
		fortress.setJdbcState(JdbcEntityState.STORED);
		update0(fortress);
	}
	
	/**
	 * Method update0.
	 * @param fortress Fortress
	 */
	private void update0(Fortress fortress)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_SQL_QUERY);
			statement.setInt(1, fortress.getCastleId());
			statement.setInt(2, fortress.getContractState());
			statement.setInt(3, fortress.getCycle());
			statement.setInt(4, fortress.getRewardCount());
			statement.setInt(5, fortress.getPaidCycle());
			statement.setInt(6, fortress.getSupplyCount());
			statement.setLong(7, fortress.getSiegeDate().getTimeInMillis());
			statement.setLong(8, fortress.getLastSiegeDate().getTimeInMillis());
			statement.setLong(9, fortress.getOwnDate().getTimeInMillis());
			for (int i = 0; i < Fortress.FACILITY_MAX; i++)
			{
				statement.setInt(10 + i, fortress.getFacilityLevel(i));
			}
			statement.setInt(10 + Fortress.FACILITY_MAX, fortress.getId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("FortressDAO#update0(Fortress): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
