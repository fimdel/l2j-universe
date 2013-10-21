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

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CastleHiredGuardDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CastleHiredGuardDAO.class);
	/**
	 * Field _instance.
	 */
	private static final CastleHiredGuardDAO _instance = new CastleHiredGuardDAO();
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT * FROM castle_hired_guards WHERE residence_id=?"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT * FROM castle_hired_guards WHERE residence_id=?";
	/**
	 * Field INSERT_SQL_QUERY. (value is ""INSERT INTO castle_hired_guards(residence_id, item_id, x, y, z) VALUES (?, ?, ?, ?, ?)"")
	 */
	public static final String INSERT_SQL_QUERY = "INSERT INTO castle_hired_guards(residence_id, item_id, x, y, z) VALUES (?, ?, ?, ?, ?)";
	/**
	 * Field DELETE_SQL_QUERY. (value is ""DELETE FROM castle_hired_guards WHERE residence_id=?"")
	 */
	public static final String DELETE_SQL_QUERY = "DELETE FROM castle_hired_guards WHERE residence_id=?";
	/**
	 * Field DELETE_SQL_QUERY2. (value is ""DELETE FROM castle_hired_guards WHERE residence_id=? AND item_id=? AND x=? AND y=? AND z=?"")
	 */
	public static final String DELETE_SQL_QUERY2 = "DELETE FROM castle_hired_guards WHERE residence_id=? AND item_id=? AND x=? AND y=? AND z=?";
	
	/**
	 * Method getInstance.
	 * @return CastleHiredGuardDAO
	 */
	public static CastleHiredGuardDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method load.
	 * @param r Castle
	 */
	public void load(Castle r)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, r.getId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int itemId = rset.getInt("item_id");
				Location loc = new Location(rset.getInt("x"), rset.getInt("y"), rset.getInt("z"));
				ItemInstance item = ItemFunctions.createItem(itemId);
				item.spawnMe(loc);
				r.getSpawnMerchantTickets().add(item);
			}
		}
		catch (Exception e)
		{
			_log.error("CastleHiredGuardDAO:load(Castle): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method insert.
	 * @param residence Residence
	 * @param itemId int
	 * @param loc Location
	 */
	public void insert(Residence residence, int itemId, Location loc)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setInt(1, residence.getId());
			statement.setInt(2, itemId);
			statement.setInt(3, loc.x);
			statement.setInt(4, loc.y);
			statement.setInt(5, loc.z);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("CastleHiredGuardDAO:insert(Residence, int, Location): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method delete.
	 * @param residence Residence
	 * @param item ItemInstance
	 */
	public void delete(Residence residence, ItemInstance item)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY2);
			statement.setInt(1, residence.getId());
			statement.setInt(2, item.getItemId());
			statement.setInt(3, item.getLoc().x);
			statement.setInt(4, item.getLoc().y);
			statement.setInt(5, item.getLoc().z);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("CastleHiredGuardDAO:delete(Residence): " + e, e);
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
			_log.error("CastleHiredGuardDAO:delete(Residence): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
