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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.DeathPenalty;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SubClass;
import lineage2.gameserver.model.base.SubClassType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharacterSubclassDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CharacterSubclassDAO.class);
	/**
	 * Field _instance.
	 */
	private static CharacterSubclassDAO _instance = new CharacterSubclassDAO();
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT class_id, default_class_id, exp, sp, curHp, curCp, curMp, active, type, death_penalty, certification, dual_certificationFROM character_subclasses WHERE char_obj_id=?"")
	 */
	public static final String SELECT_SQL_QUERY = "SELECT class_id, default_class_id, exp, sp, curHp, curCp, curMp, active, type, death_penalty, certification, dual_certification FROM character_subclasses WHERE char_obj_id=?";
	/**
	 * Field INSERT_SQL_QUERY. (value is ""INSERT INTO character_subclasses (char_obj_id, class_id, default_class_id, exp, sp, curHp, curMp, curCp, maxHp, maxMp, maxCp, level, active, type, death_penalty, certification, dual_certification) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"")
	 */
	public static final String INSERT_SQL_QUERY = "INSERT INTO character_subclasses (char_obj_id, class_id, default_class_id, exp, sp, curHp, curMp, curCp, maxHp, maxMp, maxCp, level, active, type, death_penalty, certification, dual_certification) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Method getInstance.
	 * @return CharacterSubclassDAO
	 */
	public static CharacterSubclassDAO getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method insert.
	 * @param objId int
	 * @param classId int
	 * @param dafaultClassId int
	 * @param exp long
	 * @param sp int
	 * @param curHp double
	 * @param curMp double
	 * @param curCp double
	 * @param maxHp double
	 * @param maxMp double
	 * @param maxCp double
	 * @param level int
	 * @param active boolean
	 * @param type SubClassType
	 * @param deathPenalty DeathPenalty
	 * @param certification int
	 * @return boolean
	 */
	public boolean insert(int objId, int classId, int dafaultClassId, long exp, int sp, double curHp, double curMp, double curCp, double maxHp, double maxMp, double maxCp, int level, boolean active, SubClassType type, DeathPenalty deathPenalty, int certification, int dual_certification)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setInt(1, objId);
			statement.setInt(2, classId);
			statement.setInt(3, dafaultClassId);
			statement.setLong(4, exp);
			statement.setInt(5, sp);
			statement.setDouble(6, curHp);
			statement.setDouble(7, curMp);
			statement.setDouble(8, curCp);
			statement.setDouble(9, maxHp);
			statement.setDouble(10, maxMp);
			statement.setDouble(11, maxCp);
			statement.setInt(12, level);
			statement.setInt(13, (active ? 1 : 0));
			statement.setInt(14, type.ordinal());
			statement.setInt(15, deathPenalty == null ? 0 : 6);
			statement.setInt(16, certification);
			statement.setInt(17, dual_certification);
			statement.executeUpdate();
		}
		catch (final Exception e)
		{
			_log.error("CharacterSubclassDAO:insert(player)", e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}
	
	/**
	 * Method restore.
	 * @param player Player
	 * @return List<SubClass>
	 */
	public List<SubClass> restore(Player player)
	{
		List<SubClass> result = new ArrayList<SubClass>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, player.getObjectId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				SubClass subClass = new SubClass();
				subClass.setClassId(rset.getInt("class_id"));
				subClass.setDefaultClassId(rset.getInt("default_class_id"));
				subClass.setExp(rset.getLong("exp"));
				subClass.setSp(rset.getInt("sp"));
				subClass.setHp(rset.getDouble("curHp"));
				subClass.setMp(rset.getDouble("curMp"));
				subClass.setCp(rset.getDouble("curCp"));
				subClass.setLogonHp(rset.getDouble("curHp"));
				subClass.setLogonMp(rset.getDouble("curMp"));
				subClass.setLogonCp(rset.getDouble("curCp"));
				subClass.setActive(rset.getInt("active") == 1);
				subClass.setType(SubClassType.VALUES[rset.getInt("type")]);
				subClass.setDeathPenalty(new DeathPenalty(player, rset.getInt("death_penalty")));
				subClass.setCertification(rset.getInt("certification"));
				subClass.setDualCertification(rset.getInt("dual_certification"));
				result.add(subClass);
			}
		}
		catch (Exception e)
		{
			_log.error("CharacterSubclassDAO:restore(player)", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return result;
	}
	
	/**
	 * Method store.
	 * @param player Player
	 * @return boolean
	 */
	public boolean store(Player player)
	{
		Connection con = null;
		Statement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.createStatement();
			StringBuilder sb;
			for (SubClass subClass : player.getSubClassList().values())
			{
				sb = new StringBuilder("UPDATE character_subclasses SET ");
				sb.append("exp=").append(subClass.getExp()).append(',');
				sb.append("sp=").append(subClass.getSp()).append(',');
				sb.append("curHp=").append(subClass.getHp()).append(',');
				sb.append("curMp=").append(subClass.getMp()).append(',');
				sb.append("curCp=").append(subClass.getCp()).append(',');
				sb.append("level=").append(subClass.getLevel()).append(',');
				sb.append("active=").append(subClass.isActive() ? 1 : 0).append(',');
				sb.append("type=").append(subClass.getType().ordinal()).append(',');
				sb.append("death_penalty=").append(subClass.getDeathPenalty(player).getLevelOnSaveDB(player)).append(',');
				sb.append("certification=").append(subClass.getCertification()).append(',');
				sb.append("dual_certification=").append(subClass.getDualCertification());
				sb.append(" WHERE char_obj_id=").append(player.getObjectId()).append(" AND class_id=").append(subClass.getClassId()).append(" LIMIT 1");
				statement.executeUpdate(sb.toString());
			}
			sb = new StringBuilder("UPDATE character_subclasses SET ");
			sb.append("maxHp=").append(player.getMaxHp()).append(',');
			sb.append("maxMp=").append(player.getMaxMp()).append(',');
			sb.append("maxCp=").append(player.getMaxCp());
			sb.append(" WHERE char_obj_id=").append(player.getObjectId()).append(" AND active=1 LIMIT 1");
			statement.executeUpdate(sb.toString());
		}
		catch (final Exception e)
		{
			_log.error("CharacterSubclassDAO:store(player)", e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}
}
