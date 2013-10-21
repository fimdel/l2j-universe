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
import java.util.List;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.instances.SummonInstance;
import lineage2.gameserver.skills.skillclasses.SummonServitor;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ServitorsDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(MentoringDAO.class);
	/**
	 * Field INSERT_SQL_QUERY. (value is ""INSERT INTO servitors(objId, ownerId, curHp, curMp, skill_id, skill_lvl) VALUES (?,?,?,?,?,?)"")
	 */
	private static final String INSERT_SQL_QUERY = "INSERT INTO servitors(objId, ownerId, curHp, curMp, skill_id, skill_lvl) VALUES (?,?,?,?,?,?)";
	/**
	 * Field SELECT_SQL_QUERY. (value is ""SELECT objId, curHp, curMp, skill_id, skill_lvl FROM servitors WHERE ownerId=?"")
	 */
	private static final String SELECT_SQL_QUERY = "SELECT objId, curHp, curMp, skill_id, skill_lvl FROM servitors WHERE ownerId=?";
	/**
	 * Field DELETE_SQL_QUERY. (value is ""DELETE FROM servitors WHERE ownerId=?"")
	 */
	private static final String DELETE_SQL_QUERY = "DELETE FROM servitors WHERE ownerId=?";
	/**
	 * Field ourInstance.
	 */
	private static final ServitorsDAO ourInstance = new ServitorsDAO();
	
	/**
	 * Method getInstance.
	 * @return ServitorsDAO
	 */
	public static ServitorsDAO getInstance()
	{
		return ourInstance;
	}
	
	/**
	 * Constructor for ServitorsDAO.
	 */
	private ServitorsDAO()
	{
	}
	
	/**
	 * Method store.
	 * @param summon Summon
	 */
	public void store(Summon summon)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setInt(1, summon.getObjectId());
			statement.setInt(2, summon.getPlayer().getObjectId());
			statement.setInt(3, (int) summon.getCurrentHp());
			statement.setInt(4, (int) summon.getCurrentMp());
			statement.setInt(5, summon.getSummonSkillId());
			statement.setInt(6, summon.getSummonSkillLvl());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.warn("ServitorsDAO:store(Summon)", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method restore.
	 * @param owner Player
	 * @return List<Summon>
	 */
	public List<Summon> restore(Player owner)
	{
		List<Summon> summons = new ArrayList<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_SQL_QUERY);
			statement.setInt(1, owner.getObjectId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int objId = rset.getInt("objId");
				int curHp = rset.getInt("curHp");
				int curMp = rset.getInt("curMp");
				int skill_id = rset.getInt("skill_id");
				int skill_lvl = rset.getInt("skill_lvl");
				Skill skill = SkillTable.getInstance().getInfo(skill_id, skill_lvl);
				if (skill == null)
				{
					continue;
				}
				if (skill.getSkillType() != Skill.SkillType.SUMMON)
				{
					continue;
				}
				NpcTemplate template = NpcHolder.getInstance().getTemplate(skill.getNpcId());
				SummonInstance s = new SummonInstance(objId, template, owner, ((SummonServitor) skill).getLifeTime(), ((SummonServitor) skill).getSummonPoint(), skill);
				s.setCurrentHp(curHp, true);
				s.setCurrentMp(curMp, true);
				summons.add(s);
			}
			DbUtils.closeQuietly(statement, rset);
			statement = con.prepareStatement(DELETE_SQL_QUERY);
			statement.setInt(1, owner.getObjectId());
			statement.execute();
			DbUtils.close(statement);
		}
		catch (Exception e)
		{
			_log.error("ServitorsDAO:restore(Player):" + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return summons;
	}
}
