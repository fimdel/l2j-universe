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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.handler.bbs.CommunityBoardManager;
import lineage2.gameserver.handler.bbs.ICommunityBoardHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ShowBoard;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.ScriptFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ManageMemo implements ScriptFile, ICommunityBoardHandler
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ManageMemo.class);
	/**
	 * Field MEMO_PER_PAGE. (value is 12)
	 */
	private static final int MEMO_PER_PAGE = 12;
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (Config.COMMUNITYBOARD_ENABLED)
		{
			_log.info("CommunityBoard: Manage Memo service loaded.");
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
			"_bbsmemo",
			"_mmread_",
			"_mmlist_",
			"_mmcrea",
			"_mmwrite",
			"_mmmodi_",
			"_mmdele"
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
		player.setSessionVar("add_fav", null);
		String html = HtmCache.getInstance().getNotNull("scripts/services/community/bbs_memo_list.htm", player);
		if ("bbsmemo".equals(cmd) || "_mmlist_1".equals(bypass))
		{
			int count = getMemoCount(player);
			html = html.replace("%memo_list%", getMemoList(player, 1, count));
			html = html.replace("%prev_page%", "");
			html = html.replace("%page%", "1");
			String pages = "<td>1</td>\n\n";
			if (count > MEMO_PER_PAGE)
			{
				int pgs = count / MEMO_PER_PAGE;
				if ((count % MEMO_PER_PAGE) != 0)
				{
					pgs++;
				}
				html = html.replace("%next_page%", "bypass _mmlist_2");
				for (int i = 2; i <= pgs; i++)
				{
					pages += "<td><a action=\"bypass _mmlist_" + i + "\"> " + i + " </a></td>\n\n";
				}
			}
			else
			{
				html = html.replace("%next_page%", "");
			}
			html = html.replace("%pages%", pages);
		}
		else if ("mmlist".equals(cmd))
		{
			int currPage = Integer.parseInt(st.nextToken());
			int count = getMemoCount(player);
			html = html.replace("%memo_list%", getMemoList(player, currPage, count));
			html = html.replace("%prev_page%", "bypass _mmlist_" + (currPage - 1));
			html = html.replace("%page%", String.valueOf(currPage));
			String pages = "";
			int pgs = count / MEMO_PER_PAGE;
			if ((count % MEMO_PER_PAGE) != 0)
			{
				pgs++;
			}
			if (count > (currPage * MEMO_PER_PAGE))
			{
				html = html.replace("%next_page%", "bypass _mmlist_" + (currPage + 1));
			}
			else
			{
				html = html.replace("%next_page%", "");
			}
			for (int i = 1; i <= pgs; i++)
			{
				if (i == currPage)
				{
					pages += "<td>" + i + "</td>\n\n";
				}
				else
				{
					pages += "<td height=15><a action=\"bypass _mmlist_" + i + "\"> " + i + " </a></td>\n\n";
				}
			}
			html = html.replace("%pages%", pages);
		}
		else if ("mmcrea".equals(cmd))
		{
			if (getMemoCount(player) >= 100)
			{
				player.sendPacket(new SystemMessage(SystemMessage.MEMO_BOX_IS_FULL_100_MEMO_MAXIMUM));
				onBypassCommand(player, "_mmlist_1");
				return;
			}
			String page = st.nextToken();
			html = HtmCache.getInstance().getNotNull("scripts/services/community/bbs_memo_edit.htm", player);
			html = html.replace("%page%", page);
			html = html.replace("%memo_id%", "0");
			html = html.replace("%TREE%", "&nbsp;>&nbsp;Create a record?");
			player.sendPacket(new ShowBoard(html, "1001", player));
			List<String> args = new ArrayList<>();
			args.add("0");
			args.add("0");
			args.add("0");
			args.add("0");
			args.add("0");
			args.add("0");
			args.add("");
			args.add("0");
			args.add("");
			args.add("0");
			args.add("");
			args.add("");
			args.add("");
			args.add("1970-01-01 00:00:00 ");
			args.add("1970-01-01 00:00:00 ");
			args.add("0");
			args.add("0");
			args.add("");
			player.sendPacket(new ShowBoard(args));
			return;
		}
		else if ("mmread".equals(cmd))
		{
			int memoId = Integer.parseInt(st.nextToken());
			String page = st.nextToken();
			Connection con = null;
			PreparedStatement statement = null;
			ResultSet rset = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("SELECT * FROM `bbs_memo` WHERE `account_name` = ? and memo_id = ?");
				statement.setString(1, player.getAccountName());
				statement.setInt(2, memoId);
				rset = statement.executeQuery();
				if (rset.next())
				{
					String post = HtmCache.getInstance().getNotNull("scripts/services/community/bbs_memo_read.htm", player);
					post = post.replace("%title%", rset.getString("title"));
					post = post.replace("%char_name%", rset.getString("char_name"));
					post = post.replace("%post_date%", String.format("%1$tY-%1$tm-%1$te %1$tH:%1tM:%1$tS", new Date(rset.getInt("post_date") * 1000L)));
					post = post.replace("%memo%", rset.getString("memo").replace("\n", "<br1>"));
					post = post.replace("%page%", page);
					post = post.replace("%memo_id%", String.valueOf(memoId));
					ShowBoard.separateAndSend(post, player);
					return;
				}
			}
			catch (Exception e)
			{
			}
			finally
			{
				DbUtils.closeQuietly(con, statement, rset);
			}
			onBypassCommand(player, "_bbsmemo");
			return;
		}
		else if ("mmdele".equals(cmd))
		{
			int memoId = Integer.parseInt(st.nextToken());
			Connection con = null;
			PreparedStatement statement = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("DELETE FROM `bbs_memo` WHERE `account_name` = ? and memo_id = ?");
				statement.setString(1, player.getAccountName());
				statement.setInt(2, memoId);
				statement.execute();
			}
			catch (Exception e)
			{
			}
			finally
			{
				DbUtils.closeQuietly(con, statement);
			}
			onBypassCommand(player, "_mmlist_1");
			return;
		}
		else if ("mmmodi".equals(cmd))
		{
			int memoId = Integer.parseInt(st.nextToken());
			String page = st.nextToken();
			Connection con = null;
			PreparedStatement statement = null;
			ResultSet rset = null;
			try
			{
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("SELECT * FROM `bbs_memo` WHERE `account_name` = ? and memo_id = ?");
				statement.setString(1, player.getAccountName());
				statement.setInt(2, memoId);
				rset = statement.executeQuery();
				if (rset.next())
				{
					html = HtmCache.getInstance().getNotNull("scripts/services/community/bbs_memo_edit.htm", player);
					html = html.replace("%page%", page);
					html = html.replace("%memo_id%", String.valueOf(memoId));
					html = html.replace("%TREE%", "&nbsp;>&nbsp;<a action=\"bypass _mmread_" + memoId + "_" + page + "\">Note: " + rset.getString("title") + "</a>&nbsp;>&nbsp;Editing");
					player.sendPacket(new ShowBoard(html, "1001", player));
					List<String> args = new ArrayList<>();
					args.add("0");
					args.add("0");
					args.add(String.valueOf(memoId));
					args.add("0");
					args.add("0");
					args.add("0");
					args.add(player.getName());
					args.add("0");
					args.add(player.getAccountName());
					args.add("0");
					args.add(rset.getString("title"));
					args.add(rset.getString("title"));
					args.add(rset.getString("memo"));
					args.add(String.format("%1$tY-%1$tm-%1$te %1$tH:%1tM:%1$tS", new Date(rset.getInt("post_date") * 1000L)));
					args.add(String.format("%1$tY-%1$tm-%1$te %1$tH:%1tM:%1$tS", new Date(rset.getInt("post_date") * 1000L)));
					args.add("0");
					args.add("0");
					args.add("");
					player.sendPacket(new ShowBoard(args));
					return;
				}
			}
			catch (Exception e)
			{
			}
			finally
			{
				DbUtils.closeQuietly(con, statement, rset);
			}
			onBypassCommand(player, "_mmlist_" + page);
			return;
		}
		ShowBoard.separateAndSend(html, player);
	}
	
	/**
	 * Method onWriteCommand.
	 * @param player Player
	 * @param bypass String
	 * @param arg1 String
	 * @param arg2 String
	 * @param arg3 String
	 * @paratr * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#onWriteCommand(Player, String, String, String, String, String, String)ing, String, String, String, String)
	 */
	@Override
	public void onWriteCommand(Player player, String bypass, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
		StringTokenizer st = new StringTokenizer(bypass, "_");
		String cmd = st.nextToken();
		if ("mmwrite".equals(cmd))
		{
			if (getMemoCount(player) >= 100)
			{
				player.sendPacket(new SystemMessage(SystemMessage.MEMO_BOX_IS_FULL_100_MEMO_MAXIMUM));
				onBypassCommand(player, "_mmlist_1");
				return;
			}
			if ((arg3 != null) && !arg3.isEmpty() && (arg4 != null) && !arg4.isEmpty())
			{
				String title = arg3.replace("<", "");
				title = title.replace(">", "");
				title = title.replace("&", "");
				title = title.replace("$", "");
				if (title.length() > 128)
				{
					title = title.substring(0, 128);
				}
				String memo = arg4.replace("<", "");
				memo = memo.replace(">", "");
				memo = memo.replace("&", "");
				memo = memo.replace("$", "");
				if (memo.length() > 1000)
				{
					memo = memo.substring(0, 1000);
				}
				int memoId = 0;
				if ((arg2 != null) && !arg2.isEmpty())
				{
					memoId = Integer.parseInt(arg2);
				}
				if ((title.length() > 0) && (memo.length() > 0))
				{
					Connection con = null;
					PreparedStatement stmt = null;
					try
					{
						con = DatabaseFactory.getInstance().getConnection();
						if (memoId > 0)
						{
							stmt = con.prepareStatement("UPDATE bbs_memo SET title = ?, memo = ? WHERE memo_id = ? AND account_name = ?");
							stmt.setString(1, title);
							stmt.setString(2, memo);
							stmt.setInt(3, memoId);
							stmt.setString(4, player.getAccountName());
							stmt.execute();
						}
						else
						{
							stmt = con.prepareStatement("INSERT INTO bbs_memo(account_name, char_name, ip, title, memo, post_date) VALUES(?, ?, ?, ?, ?, ?)");
							stmt.setString(1, player.getAccountName());
							stmt.setString(2, player.getName());
							stmt.setString(3, player.getNetConnection().getIpAddr());
							stmt.setString(4, title);
							stmt.setString(5, memo);
							stmt.setInt(6, (int) (System.currentTimeMillis() / 1000));
							stmt.execute();
						}
					}
					catch (Exception e)
					{
					}
					finally
					{
						DbUtils.closeQuietly(con, stmt);
					}
				}
			}
		}
		onBypassCommand(player, "_bbsmemo");
	}
	
	/**
	 * Method getMemoList.
	 * @param player Player * * @return String @param count int
	 * @return String
	 */
	private static String getMemoList(Player player, int page, int count)
	{
		StringBuilder memoList = new StringBuilder("");
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			if (count > 0)
			{
				int start = (page - 1) * MEMO_PER_PAGE;
				int end = page * MEMO_PER_PAGE;
				con = DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement("SELECT memo_id,title,post_date FROM `bbs_memo` WHERE `account_name` = ? ORDER BY post_date DESC LIMIT " + start + "," + end);
				statement.setString(1, player.getAccountName());
				rset = statement.executeQuery();
				String tpl = HtmCache.getInstance().getNotNull("scripts/services/community/bbs_memo_post.htm", player);
				while (rset.next())
				{
					String post = tpl;
					post = post.replace("%memo_id%", String.valueOf(rset.getInt("memo_id")));
					post = post.replace("%memo_title%", rset.getString("title"));
					post = post.replace("%page%", String.valueOf(page));
					post = post.replace("%memo_date%", String.format("%1$te-%1$tm-%1$tY", new Date(rset.getInt("post_date") * 1000L)));
					memoList.append(post);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return memoList.toString();
	}
	
	/**
	 * Method ge* * @return int@param player Player
	 * @return int
	 */
	private static int getMemoCount(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		int count = 0;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT count(*) as cnt FROM bbs_memo WHERE `account_name` = ?");
			statement.setString(1, player.getAccountName());
			rset = statement.executeQuery();
			if (rset.next())
			{
				count = rset.getInt("cnt");
			}
		}
		catch (Exception e)
		{
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return count;
	}
}
