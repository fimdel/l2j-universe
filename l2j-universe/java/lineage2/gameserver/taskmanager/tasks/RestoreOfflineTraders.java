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
package lineage2.gameserver.taskmanager.tasks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RestoreOfflineTraders extends RunnableImpl
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(RestoreOfflineTraders.class);
	
	/**
	 * Method runImpl.
	 */
	@Override
	public void runImpl()
	{
		int count = 0;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			if (Config.SERVICES_OFFLINE_TRADE_SECONDS_TO_KICK > 0)
			{
				int expireTimeSecs = (int) ((System.currentTimeMillis() / 1000L) - Config.SERVICES_OFFLINE_TRADE_SECONDS_TO_KICK);
				statement = con.prepareStatement("DELETE FROM character_variables WHERE name = 'offline' AND value < ?");
				statement.setLong(1, expireTimeSecs);
				statement.executeUpdate();
				DbUtils.close(statement);
			}
			statement = con.prepareStatement("DELETE FROM character_variables WHERE name = 'offline' AND obj_id IN (SELECT obj_id FROM characters WHERE accessLevel < 0)");
			statement.executeUpdate();
			DbUtils.close(statement);
			statement = con.prepareStatement("SELECT obj_id, value FROM character_variables WHERE name = 'offline'");
			rset = statement.executeQuery();
			int objectId;
			int expireTimeSecs;
			Player p;
			while (rset.next())
			{
				objectId = rset.getInt("obj_id");
				expireTimeSecs = rset.getInt("value");
				p = Player.restore(objectId);
				if (p == null)
				{
					continue;
				}
				if (p.isDead())
				{
					p.kick();
					continue;
				}
				p.setNameColor(Config.SERVICES_OFFLINE_TRADE_NAME_COLOR);
				p.setOfflineMode(true);
				p.setIsOnline(true);
				p.spawnMe();
				if ((p.getClan() != null) && (p.getClan().getAnyMember(p.getObjectId()) != null))
				{
					p.getClan().getAnyMember(p.getObjectId()).setPlayerInstance(p, false);
				}
				if (Config.SERVICES_OFFLINE_TRADE_SECONDS_TO_KICK > 0)
				{
					p.startKickTask(((Config.SERVICES_OFFLINE_TRADE_SECONDS_TO_KICK + expireTimeSecs) - (System.currentTimeMillis() / 1000L)) * 1000L);
				}
				if (Config.SERVICES_TRADE_ONLY_FAR)
				{
					for (Player player : World.getAroundPlayers(p, Config.SERVICES_TRADE_RADIUS, 200))
					{
						if (player.isInStoreMode())
						{
							if (player.isInOfflineMode())
							{
								player.setOfflineMode(false);
								player.kick();
								_log.warn("Offline trader: " + player + " kicked.");
							}
							else
							{
								player.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
								player.standUp();
								player.broadcastCharInfo();
							}
						}
					}
				}
				count++;
			}
		}
		catch (Exception e)
		{
			_log.error("Error while restoring offline traders!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		_log.info("Restored " + count + " offline traders");
	}
}
