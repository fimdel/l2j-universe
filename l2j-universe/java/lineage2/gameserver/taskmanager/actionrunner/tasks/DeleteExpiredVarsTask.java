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
package lineage2.gameserver.taskmanager.actionrunner.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.utils.Strings;

import org.apache.log4j.Logger;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DeleteExpiredVarsTask extends AutomaticTask
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = Logger.getLogger(DeleteExpiredVarsTask.class);
	
	/**
	 * Constructor for DeleteExpiredVarsTask.
	 */
	public DeleteExpiredVarsTask()
	{
		super();
	}
	
	/**
	 * Method doTask.
	 */
	@Override
	public void doTask()
	{
		System.currentTimeMillis();
		Connection con = null;
		PreparedStatement query = null;
		Map<Integer, String> varMap = new HashMap<>();
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			query = con.prepareStatement("SELECT obj_id, name FROM character_variables WHERE expire_time > 0 AND expire_time < ?");
			query.setLong(1, System.currentTimeMillis());
			rs = query.executeQuery();
			while (rs.next())
			{
				String name = rs.getString("name");
				String obj_id = Strings.stripSlashes(rs.getString("obj_id"));
				varMap.put(Integer.parseInt(obj_id), name);
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, query, rs);
		}
		if (!varMap.isEmpty())
		{
			for (Map.Entry<Integer, String> entry : varMap.entrySet())
			{
				Player player = GameObjectsStorage.getPlayer(entry.getKey());
				if ((player != null) && player.isOnline())
				{
					player.unsetVar(entry.getValue());
				}
				else
				{
					mysql.set("DELETE FROM `character_variables` WHERE `obj_id`=? AND `type`='user-var' AND `name`=? LIMIT 1", entry.getKey(), entry.getValue());
				}
			}
		}
	}
	
	/**
	 * Method reCalcTime.
	 * @param start boolean
	 * @return long
	 */
	@Override
	public long reCalcTime(boolean start)
	{
		return System.currentTimeMillis() + 600000L;
	}
}
