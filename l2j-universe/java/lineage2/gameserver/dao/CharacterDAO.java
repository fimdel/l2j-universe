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
import lineage2.gameserver.model.Player;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharacterDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CharacterDAO.class);
	/**
	 * Field _instance.
	 */
	private static CharacterDAO _instance = new CharacterDAO();
	
	/**
	 * Method getInstance.
	 * @return CharacterDAO
	 */
	public static CharacterDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method deleteCharByObjId.
	 * @param objid int
	 */
	public void deleteCharByObjId(int objid)
	{
		if (objid < 0)
		{
			return;
		}
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM characters WHERE obj_Id=?");
			statement.setInt(1, objid);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method insert.
	 * @param player Player
	 * @return boolean
	 */
	public boolean insert(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO `characters` (account_name, obj_Id, char_name, face, hairStyle, hairColor, sex, karma, pvpkills, pkkills, clanid, createtime, deletetime, title, accesslevel, online, leaveclan, deleteclan, nochannel, pledge_type, pledge_rank, lvl_joined_academy, apprentice) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			statement.setString(1, player.getAccountName());
			statement.setInt(2, player.getObjectId());
			statement.setString(3, player.getName());
			statement.setInt(4, player.getFace());
			statement.setInt(5, player.getHairStyle());
			statement.setInt(6, player.getHairColor());
			statement.setInt(7, player.getSex());
			statement.setInt(8, player.getKarma());
			statement.setInt(9, player.getPvpKills());
			statement.setInt(10, player.getPkKills());
			statement.setInt(11, player.getClanId());
			statement.setLong(12, player.getCreateTime() / 1000);
			statement.setInt(13, player.getDeleteTimer());
			statement.setString(14, player.getTitle());
			statement.setInt(15, player.getAccessLevel());
			statement.setInt(16, player.isOnline() ? 1 : 0);
			statement.setLong(17, player.getLeaveClanTime() / 1000);
			statement.setLong(18, player.getDeleteClanTime() / 1000);
			statement.setLong(19, player.getNoChannel() > 0 ? player.getNoChannel() / 1000 : player.getNoChannel());
			statement.setInt(20, player.getPledgeType());
			statement.setInt(21, player.getPowerGrade());
			statement.setInt(22, player.getLvlJoinedAcademy());
			statement.setInt(23, player.getApprentice());
			statement.executeUpdate();
		}
		catch (final Exception e)
		{
			_log.error("", e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}
	
	/**
	 * Method getObjectIdByName.
	 * @param name String
	 * @return int
	 */
	public int getObjectIdByName(String name)
	{
		int result = 0;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT obj_Id FROM characters WHERE char_name=?");
			statement.setString(1, name);
			rset = statement.executeQuery();
			if (rset.next())
			{
				result = rset.getInt(1);
			}
		}
		catch (Exception e)
		{
			_log.error("CharNameTable.getObjectIdByName(String): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return result;
	}
	
	/**
	 * Method getNameByObjectId.
	 * @param objectId int
	 * @return String
	 */
	public String getNameByObjectId(int objectId)
	{
		String result = StringUtils.EMPTY;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT char_name FROM characters WHERE obj_Id=?");
			statement.setInt(1, objectId);
			rset = statement.executeQuery();
			if (rset.next())
			{
				result = rset.getString(1);
			}
		}
		catch (Exception e)
		{
			_log.error("CharNameTable.getObjectIdByName(int): " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return result;
	}
	
	/**
	 * Method accountCharNumber.
	 * @param account String
	 * @return int
	 */
	public int accountCharNumber(String account)
	{
		int number = 0;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT COUNT(char_name) FROM characters WHERE account_name=?");
			statement.setString(1, account);
			rset = statement.executeQuery();
			if (rset.next())
			{
				number = rset.getInt(1);
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return number;
	}
}
