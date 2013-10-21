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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.entity.residence.Residence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CastleDamageZoneDAO
{
	/**
	 * Field _instance.
	 */
	private static final CastleDamageZoneDAO _instance = new CastleDamageZoneDAO();
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CastleDoorUpgradeDAO.class);
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT zone FROM castle_damage_zones WHERE residence_id=?"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT zone FROM castle_damage_zones WHERE residence_id=?";
	/**
	 * Field INSERT_SQL_QUERY. (value is ""INSERT INTO castle_damage_zones (residence_id, zone) VALUES (?,?)"")
	 */
	public static final String INSERT_SQL_QUERY = "INSERT INTO castle_damage_zones (residence_id, zone) VALUES (?,?)";
	/**
	 * Field DELETE_SQL_QUERY. (value is ""DELETE FROM castle_damage_zones WHERE residence_id=?"")
	 */
	public static final String DELETE_SQL_QUERY = "DELETE FROM castle_damage_zones WHERE residence_id=?";
	
	/**
	 * Method getInstance.
	 * @return CastleDamageZoneDAO
	 */
	public static CastleDamageZoneDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method load.
	 * @param r Residence
	 * @return List<String>
	 */
	public List<String> load(Residence r)
	{
		List<String> set = Collections.emptyList();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, r.getId());
			rset = statement.executeQuery();
			set = new ArrayList<>();
			while (rset.next())
			{
				set.add(rset.getString("zone"));
			}
		}
		catch (Exception e)
		{
			_log.error("CastleDamageZoneDAO:load(Residence): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return set;
	}
	
	/**
	 * Method insert.
	 * @param residence Residence
	 * @param name String
	 */
	public void insert(Residence residence, String name)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setInt(1, residence.getId());
			statement.setString(2, name);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("CastleDamageZoneDAO:insert(Residence, String): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method delete.
	 * @param residence Residence
	 */
	public void delete(Residence residence)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setInt(1, residence.getId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("CastleDamageZoneDAO:delete(Residence): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
