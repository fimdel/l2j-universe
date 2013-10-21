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
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.Residence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SiegeClanDAO
{
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT clan_id, param, date FROM siege_clans WHERE residence_id=? AND type=? ORDER BY date"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT clan_id, param, date FROM siege_clans WHERE residence_id=? AND type=? ORDER BY date";
	/**
	 * Field INSERT_SQL_QUERY. (value is ""INSERT INTO siege_clans(residence_id, clan_id, param, type, date) VALUES (?, ?, ?, ?, ?)"")
	 */
	public static final String INSERT_SQL_QUERY = "INSERT INTO siege_clans(residence_id, clan_id, param, type, date) VALUES (?, ?, ?, ?, ?)";
	/**
	 * Field UPDATE_SQL_QUERY. (value is ""UPDATE siege_clans SET type=?, param=? WHERE residence_id=? AND clan_id=?"")
	 */
	public static final String UPDATE_SQL_QUERY = "UPDATE siege_clans SET type=?, param=? WHERE residence_id=? AND clan_id=?";
	/**
	 * Field DELETE_SQL_QUERY. (value is ""DELETE FROM siege_clans WHERE residence_id=? AND clan_id=? AND type=?"")
	 */
	public static final String DELETE_SQL_QUERY = "DELETE FROM siege_clans WHERE residence_id=? AND clan_id=? AND type=?";
	/**
	 * Field DELETE_SQL_QUERY2. (value is ""DELETE FROM siege_clans WHERE residence_id=?"")
	 */
	public static final String DELETE_SQL_QUERY2 = "DELETE FROM siege_clans WHERE residence_id=?";
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SiegeClanDAO.class);
	/**
	 * Field _instance.
	 */
	private static final SiegeClanDAO _instance = new SiegeClanDAO();
	
	/**
	 * Method getInstance.
	 * @return SiegeClanDAO
	 */
	public static SiegeClanDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method load.
	 * @param residence Residence
	 * @param name String
	 * @return List<SiegeClanObject>
	 */
	public List<SiegeClanObject> load(Residence residence, String name)
	{
		List<SiegeClanObject> siegeClans = Collections.emptyList();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, residence.getId());
			statement.setString(2, name);
			rset = statement.executeQuery();
			siegeClans = new ArrayList<>();
			while (rset.next())
			{
				int clanId = rset.getInt("clan_id");
				long param = rset.getLong("param");
				long date = rset.getLong("date");
				SiegeClanObject object = residence.<SiegeEvent<?, ?>> getSiegeEvent().newSiegeClan(name, clanId, param, date);
				if (object != null)
				{
					siegeClans.add(object);
				}
				else
				{
					_log.info("SiegeClanDAO#load(Residence, String): null clan: " + clanId + "; residence: " + residence.getId());
				}
			}
		}
		catch (Exception e)
		{
			_log.warn("SiegeClanDAO#load(Residence, String): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return siegeClans;
	}
	
	/**
	 * Method insert.
	 * @param residence Residence
	 * @param siegeClan SiegeClanObject
	 */
	public void insert(Residence residence, SiegeClanObject siegeClan)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setInt(1, residence.getId());
			statement.setInt(2, siegeClan.getObjectId());
			statement.setLong(3, siegeClan.getParam());
			statement.setString(4, siegeClan.getType());
			statement.setLong(5, siegeClan.getDate());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("SiegeClanDAO#insert(Residence, SiegeClan): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method delete.
	 * @param residence Residence
	 * @param siegeClan SiegeClanObject
	 */
	public void delete(Residence residence, SiegeClanObject siegeClan)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setInt(1, residence.getId());
			statement.setInt(2, siegeClan.getObjectId());
			statement.setString(3, siegeClan.getType());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("SiegeClanDAO#delete(Residence, SiegeClan): " + e, e);
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
			statement = con.prepareStatement(DELETE_SQL_QUERY2);
			statement.setInt(1, residence.getId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("SiegeClanDAO#delete(Residence): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method update.
	 * @param residence Residence
	 * @param siegeClan SiegeClanObject
	 */
	public void update(Residence residence, SiegeClanObject siegeClan)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_SQL_QUERY);
			statement.setString(1, siegeClan.getType());
			statement.setLong(2, siegeClan.getParam());
			statement.setInt(3, residence.getId());
			statement.setInt(4, siegeClan.getObjectId());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("SiegeClanDAO#update(Residence, SiegeClan): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
