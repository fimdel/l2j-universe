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
package services.community;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.StringTokenizer;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.handler.bbs.CommunityBoardManager;
import lineage2.gameserver.handler.bbs.ICommunityBoardHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ShowBoard;
import lineage2.gameserver.scripts.ScriptFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ManageFavorites implements ScriptFile, ICommunityBoardHandler
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ManageFavorites.class);
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (Config.COMMUNITYBOARD_ENABLED)
		{
			_log.info("CommunityBoard: Manage Favorites service loaded.");
			CommunityBoardManager.getInstance().registerHandler(this);
		}
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		if (Config.COMMUNITYBOARD_ENABLED)
		{
			CommunityBoardManager.getInstance().removeHandler(this);
		}
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
	
	/**
	 * Method getBypassCommands.
	 * @return String[] * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#getBypassCommands()
	 */
	@Override
	public String[] getBypassCommands()
	{
		return new String[]
		{
			"_bbsgetfav",
			"_bbsaddfav_List",
			"_bbsdelfav_"
		};
	}
	
	/**
	 * Method onBypassCommand.
	 * @param player Player
	 * @param bypass String
	 * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#onBypassCommand(Player, String)
	 */
	@Override
	public void onBypassCommand(Player player, String bypass)
	{
		StringTokenizer st = new StringTokenizer(bypass, "_");
		String cmd = st.nextToken();
		if ("bbsgetfav".equals(cmd))
		{
			Connection con = null;
			PreparedStatement statement = null;
			ResultSet rset = null;
			StringBuilder fl = new StringBuilder("");
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("SELECT * FROM `bbs_favorites` WHERE `object_id` = ? ORDER BY `add_date` DESC");
				statement.setInt(1, player.getObjectId());
				rset = statement.executeQuery();
				String tpl = HtmCache.getInstance().getNotNull("scripts/services/community/bbs_favoritetpl.htm", player);
				while (rset.next())
				{
					String fav = tpl.replace("%fav_title%", rset.getString("fav_title"));
					fav = fav.replace("%fav_bypass%", rset.getString("fav_bypass"));
					fav = fav.replace("%add_date%", String.format("%1$te.%1$tm.%1$tY %1$tH:%1tM", new Date(rset.getInt("add_date") * 1000L)));
					fav = fav.replace("%fav_id%", String.valueOf(rset.getInt("fav_id")));
					fl.append(fav);
				}
			}
			catch (Exception e)
			{
			}
			finally
			{
				DbUtils.closeQuietly(con, statement, rset);
			}
			String html = HtmCache.getInstance().getNotNull("scripts/services/community/bbs_getfavorite.htm", player);
			html = html.replace("%FAV_LIST%", fl.toString());
			ShowBoard.separateAndSend(html, player);
		}
		else if ("bbsaddfav".equals(cmd))
		{
			String fav = player.getSessionVar("add_fav");
			player.setSessionVar("add_fav", null);
			if (fav != null)
			{
				String favs[] = fav.split("&");
				if (favs.length > 1)
				{
					Connection con = null;
					PreparedStatement statement = null;
					try
					{
						con = DatabaseFactory.getInstance().getConnection();
						statement = con.prepareStatement("REPLACE INTO `bbs_favorites`(`object_id`, `fav_bypass`, `fav_title`, `add_date`) VALUES(?, ?, ?, ?)");
						statement.setInt(1, player.getObjectId());
						statement.setString(2, favs[0]);
						statement.setString(3, favs[1]);
						statement.setInt(4, (int) (System.currentTimeMillis() / 1000));
						statement.execute();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					finally
					{
						DbUtils.closeQuietly(con, statement);
					}
				}
			}
			onBypassCommand(player, "_bbsgetfav");
		}
		else if ("bbsdelfav".equals(cmd))
		{
			int fav_id = Integer.parseInt(st.nextToken());
			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("DELETE FROM `bbs_favorites` WHERE `fav_id` = ? and `object_id` = ?");
				statement.setInt(1, fav_id);
				statement.setInt(2, player.getObjectId());
				statement.execute();
			}
			catch (Exception e)
			{
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
			onBypassCommand(player, "_bbsgetfav");
		}
	}
	
	/**
	 * Method onWriteCommand.
	 * @param player Player
	 * @param bypass String
	 * @param arg1 String
	 * @param arg2 String
	 * @param arg3 String
	 * @param arg4 String
	 * @param arg5 String
	 * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#onWriteCommand(Player, String, String, String, String, String, String)
	 */
	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
	}
}
