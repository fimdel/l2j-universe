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
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.tables.ClanTable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClanDataDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ClanDataDAO.class);
	/**
	 * Field _instance.
	 */
	private static final ClanDataDAO _instance = new ClanDataDAO();
	/**
	 * Field SELECT_CASTLE_OWNER. (value is ""SELECT clan_id FROM clan_data WHERE hasCastle = ? LIMIT 1"")
	 */
	public static final String SELECT_CASTLE_OWNER = "SELECT clan_id FROM clan_data WHERE hasCastle = ? LIMIT 1";
	/**
	 * Field SELECT_FORTRESS_OWNER. (value is ""SELECT clan_id FROM clan_data WHERE hasFortress = ? LIMIT 1"")
	 */
	public static final String SELECT_FORTRESS_OWNER = "SELECT clan_id FROM clan_data WHERE hasFortress = ? LIMIT 1";
	/**
	 * Field SELECT_CLANHALL_OWNER. (value is ""SELECT clan_id FROM clan_data WHERE hasHideout = ? LIMIT 1"")
	 */
	public static final String SELECT_CLANHALL_OWNER = "SELECT clan_id FROM clan_data WHERE hasHideout = ? LIMIT 1";
	
	/**
	 * Method getInstance.
	 * @return ClanDataDAO
	 */
	public static ClanDataDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method getOwner.
	 * @param c Castle
	 * @return Clan
	 */
	public Clan getOwner(Castle c)
	{
		return getOwner(c, SELECT_CASTLE_OWNER);
	}
	
	/**
	 * Method getOwner.
	 * @param f Fortress
	 * @return Clan
	 */
	public Clan getOwner(Fortress f)
	{
		return getOwner(f, SELECT_FORTRESS_OWNER);
	}
	
	/**
	 * Method getOwner.
	 * @param c ClanHall
	 * @return Clan
	 */
	public Clan getOwner(ClanHall c)
	{
		return getOwner(c, SELECT_CLANHALL_OWNER);
	}
	
	/**
	 * Method getOwner.
	 * @param residence Residence
	 * @param sql String
	 * @return Clan
	 */
	private Clan getOwner(Residence residence, String sql)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(sql);
			statement.setInt(1, residence.getId());
			rset = statement.executeQuery();
			if (rset.next())
			{
				return ClanTable.getInstance().getClan(rset.getInt("clan_id"));
			}
		}
		catch (Exception e)
		{
			_log.error("ClanDataDAO.getOwner(Residence, String)", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return null;
	}
}
