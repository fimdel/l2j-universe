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
import java.text.SimpleDateFormat;
import java.util.Date;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.handler.bbs.CommunityBoardManager;
import lineage2.gameserver.handler.bbs.ICommunityBoardHandler;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ShowBoard;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.BbsUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CommunityStats implements ScriptFile, ICommunityBoardHandler
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CommunityStats.class);
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (Config.COMMUNITYBOARD_ENABLED)
		{
			_log.info("CommunityBoard: Stats service loaded.");
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
	 * @return String[]
	 * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#getBypassCommands()
	 */
	@Override
	public String[] getBypassCommands()
	{
		return new String[]
		{
			"_bbsstat;",
			"_bbsstat;pk",
			"_bbsstat;online",
			"_bbsstat;clan",
			"_bbsstat;castle"
		};
	}
	
	/**
	 */
	public class CBStatMan
	{
		/**
		 * Field PlayerId.
		 */
		public int PlayerId = 0;
		/**
		 * Field ChName.
		 */
		public String ChName = "";
		/**
		 * Field ChGameTime.
		 */
		public int ChGameTime = 0;
		/**
		 * Field ChPk.
		 */
		public int ChPk = 0;
		/**
		 * Field ChPvP.
		 */
		public int ChPvP = 0;
		/**
		 * Field ChOnOff.
		 */
		public int ChOnOff = 0;
		/**
		 * Field ChSex.
		 */
		public int ChSex = 0;
		/**
		 * Field NameCastl.
		 */
		public String NameCastl;
		/**
		 * Field siegeDate.
		 */
		public Object siegeDate;
		/**
		 * Field Percent.
		 */
		public String Percent;
		/**
		 * Field id2.
		 */
		public Object id2;
		/**
		 * Field id.
		 */
		public int id;
		/**
		 * Field ClanLevel.
		 */
		public int ClanLevel;
		/**
		 * Field hasCastle.
		 */
		public int hasCastle;
		/**
		 * Field ReputationClan.
		 */
		public int ReputationClan;
		/**
		 * Field AllyName.
		 */
		public String AllyName;
		/**
		 * Field ClanName.
		 */
		public String ClanName;
		/**
		 * Field Owner.
		 */
		public String Owner;
	}
	
	/**
	 * Method onBypassCommand.
	 * @param player Player
	 * @param command String
	 * @see lineage2.gameserver.handler.bbs.ICommunityBoardHandler#onBypassCommand(Player, String)
	 */
	@Override
	public void onBypassCommand(Player player, String command)
	{
		if (command.equals("_bbsstat;"))
		{
			showPvp(player);
		}
		else if (command.startsWith("_bbsstat;pk"))
		{
			showPK(player);
		}
		else if (command.startsWith("_bbsstat;online"))
		{
			showOnline(player);
		}
		else if (command.startsWith("_bbsstat;clan"))
		{
			showClan(player);
		}
		else if (command.startsWith("_bbsstat;castle"))
		{
			showCastle(player);
		}
		else
		{
			ShowBoard.separateAndSend("<html><body><br><br><center>_bbsstat function: " + command + " not implemented yet</center><br><br></body></html>", player);
		}
	}
	
	/**
	 * Method showPvp.
	 * @param player Player
	 */
	private void showPvp(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY pvpkills DESC LIMIT 10;");
			rs = statement.executeQuery();
			StringBuilder html = new StringBuilder();
			html.append("<table width=570>");
			while (rs.next())
			{
				CBStatMan tp = new CBStatMan();
				tp.PlayerId = rs.getInt("obj_Id");
				tp.ChName = rs.getString("char_name");
				tp.ChSex = rs.getInt("sex");
				tp.ChGameTime = rs.getInt("onlinetime");
				tp.ChPk = rs.getInt("pkkills");
				tp.ChPvP = rs.getInt("pvpkills");
				tp.ChOnOff = rs.getInt("online");
				String sex = tp.ChSex == 1 ? "F" : "M";
				String color;
				String OnOff;
				if (tp.ChOnOff == 1)
				{
					OnOff = "Online.";
					color = "00CC00";
				}
				else
				{
					OnOff = "Offline.";
					color = "D70000";
				}
				html.append("<tr>");
				html.append("<td width=250>" + tp.ChName + "</td>");
				html.append("<td width=50>" + sex + "</td>");
				html.append("<td width=100>" + OnlineTime(tp.ChGameTime) + "</td>");
				html.append("<td width=50>" + tp.ChPk + "</td>");
				html.append("<td width=50><font color=00CC00>" + tp.ChPvP + "</font></td>");
				html.append("<td width=100><font color=" + color + ">" + OnOff + "</font></td>");
				html.append("</tr>");
			}
			html.append("</table>");
			String content = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/stats/stats_top_pvp.htm", player);
			content = content.replace("%stats_top_pvp%", html.toString());
			content = BbsUtil.htmlBuff(content, player);
			ShowBoard.separateAndSend(content, player);
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	/**
	 * Method showPK.
	 * @param player Player
	 */
	private void showPK(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY pkkills DESC LIMIT 10;");
			rs = statement.executeQuery();
			StringBuilder html = new StringBuilder();
			html.append("<table width=570>");
			while (rs.next())
			{
				CBStatMan tp = new CBStatMan();
				tp.PlayerId = rs.getInt("obj_Id");
				tp.ChName = rs.getString("char_name");
				tp.ChSex = rs.getInt("sex");
				tp.ChGameTime = rs.getInt("onlinetime");
				tp.ChPk = rs.getInt("pkkills");
				tp.ChPvP = rs.getInt("pvpkills");
				tp.ChOnOff = rs.getInt("online");
				String sex = tp.ChSex == 1 ? "F" : "M";
				String color;
				String OnOff;
				if (tp.ChOnOff == 1)
				{
					OnOff = "Online.";
					color = "00CC00";
				}
				else
				{
					OnOff = "Offline.";
					color = "D70000";
				}
				html.append("<tr>");
				html.append("<td width=250>" + tp.ChName + "</td>");
				html.append("<td width=50>" + sex + "</td>");
				html.append("<td width=100>" + OnlineTime(tp.ChGameTime) + "</td>");
				html.append("<td width=50><font color=00CC00>" + tp.ChPk + "</font></td>");
				html.append("<td width=50>" + tp.ChPvP + "</td>");
				html.append("<td width=100><font color=" + color + ">" + OnOff + "</font></td>");
				html.append("</tr>");
			}
			html.append("</table>");
			String content = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/stats/stats_top_pk.htm", player);
			content = content.replace("%stats_top_pk%", html.toString());
			content = BbsUtil.htmlBuff(content, player);
			ShowBoard.separateAndSend(content, player);
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	/**
	 * Method showOnline.
	 * @param player Player
	 */
	private void showOnline(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY onlinetime DESC LIMIT 10;");
			rs = statement.executeQuery();
			StringBuilder html = new StringBuilder();
			html.append("<table width=570>");
			while (rs.next())
			{
				CBStatMan tp = new CBStatMan();
				tp.PlayerId = rs.getInt("obj_Id");
				tp.ChName = rs.getString("char_name");
				tp.ChSex = rs.getInt("sex");
				tp.ChGameTime = rs.getInt("onlinetime");
				tp.ChPk = rs.getInt("pkkills");
				tp.ChPvP = rs.getInt("pvpkills");
				tp.ChOnOff = rs.getInt("online");
				String sex = tp.ChSex == 1 ? "F" : "M";
				String color;
				String OnOff;
				if (tp.ChOnOff == 1)
				{
					OnOff = "Online.";
					color = "00CC00";
				}
				else
				{
					OnOff = "Offline.";
					color = "D70000";
				}
				html.append("<tr>");
				html.append("<td width=250>" + tp.ChName + "</td>");
				html.append("<td width=50>" + sex + "</td>");
				html.append("<td width=100><font color=00CC00>" + OnlineTime(tp.ChGameTime) + "</font></td>");
				html.append("<td width=50>" + tp.ChPk + "</td>");
				html.append("<td width=50>" + tp.ChPvP + "</td>");
				html.append("<td width=100><font color=" + color + ">" + OnOff + "</font></td>");
				html.append("</tr>");
			}
			html.append("</table>");
			String content = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/stats/stats_online.htm", player);
			content = content.replace("%stats_online%", html.toString());
			content = BbsUtil.htmlBuff(content, player);
			ShowBoard.separateAndSend(content, player);
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	/**
	 * Method showCastle.
	 * @param player Player
	 */
	private void showCastle(Player player)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT * FROM castle ORDER BY id DESC LIMIT 10;");
			rs = statement.executeQuery();
			StringBuilder html = new StringBuilder();
			html.append("<table width=570>");
			String Owner = null;
			String color = "FFFFFF";
			while (rs.next())
			{
				CBStatMan tp = new CBStatMan();
				tp.id = rs.getInt("id");
				tp.NameCastl = rs.getString("name");
				tp.Percent = (rs.getString("tax_percent") + "%");
				tp.siegeDate = sdf.format(new Date(rs.getLong("siege_date")));
				Owner = CastleManorManager.getInstance().getOwner(tp.id);
				if (Owner != null)
				{
					color = "00CC00";
				}
				else
				{
					color = "FFFFFF";
					Owner = "Not Owned";
				}
				html.append("<tr>");
				html.append("<td width=150>" + tp.NameCastl + "</td>");
				html.append("<td width=100>" + tp.Percent + "</td>");
				html.append("<td width=200><font color=" + color + ">" + Owner + "</font></td>");
				html.append("<td width=150>" + tp.siegeDate + "</td>");
				html.append("</tr>");
			}
			html.append("</table>");
			String content = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/stats/stats_castle.htm", player);
			content = content.replace("%stats_castle%", html.toString());
			content = BbsUtil.htmlBuff(content, player);
			ShowBoard.separateAndSend(content, player);
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	/**
	 * Method showClan.
	 * @param player Player
	 */
	private void showClan(Player player)
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT clan_subpledges.name,clan_data.clan_level,clan_data.reputation_score,clan_data.hasCastle,ally_data.ally_name FROM clan_data LEFT JOIN ally_data ON clan_data.ally_id = ally_data.ally_id LEFT JOIN `clan_subpledges` ON clan_data.clan_id = clan_subpledges.clan_id WHERE clan_data.clan_level>0 AND clan_subpledges.leader_id != '' order by clan_data.clan_level desc limit 10;");
			rs = statement.executeQuery();
			StringBuilder html = new StringBuilder();
			html.append("<table width=570>");
			while (rs.next())
			{
				CBStatMan tp = new CBStatMan();
				tp.ClanName = rs.getString("name");
				tp.AllyName = rs.getString("ally_name");
				tp.ReputationClan = rs.getInt("reputation_score");
				tp.ClanLevel = rs.getInt("clan_level");
				tp.hasCastle = rs.getInt("hasCastle");
				String hasCastle = "";
				String castleColor = "D70000";
				switch (tp.hasCastle)
				{
					case 1:
						hasCastle = "Gludio";
						castleColor = "00CC00";
						break;
					case 2:
						hasCastle = "Dion";
						castleColor = "00CC00";
						break;
					case 3:
						hasCastle = "Giran";
						castleColor = "00CC00";
						break;
					case 4:
						hasCastle = "Oren";
						castleColor = "00CC00";
						break;
					case 5:
						hasCastle = "Aden";
						castleColor = "00CC00";
						break;
					case 6:
						hasCastle = "Innadril";
						castleColor = "00CC00";
						break;
					case 7:
						hasCastle = "Goddard";
						castleColor = "00CC00";
						break;
					case 8:
						hasCastle = "Rune";
						castleColor = "00CC00";
						break;
					case 9:
						hasCastle = "Schuttgart";
						castleColor = "00CC00";
						break;
					default:
						hasCastle = "None";
						castleColor = "D70000";
						break;
				}
				html.append("<tr>");
				html.append("<td width=150>" + tp.ClanName + "</td>");
				if (tp.AllyName != null)
				{
					html.append("<td width=150>" + tp.AllyName + "</td>");
				}
				else
				{
					html.append("<td width=150>No Ally</td>");
				}
				html.append("<td width=100>" + tp.ReputationClan + "</td>");
				html.append("<td width=50>" + tp.ClanLevel + "</td>");
				html.append("<td width=100><font color=" + castleColor + ">" + hasCastle + "</font></td>");
				html.append("</tr>");
			}
			html.append("</table>");
			String content = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/stats/stats_clan.htm", player);
			content = content.replace("%stats_clan%", html.toString());
			content = BbsUtil.htmlBuff(content, player);
			ShowBoard.separateAndSend(content, player);
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rs);
		}
	}
	
	/**
	 * Method OnlineTime.
	 * @param time int
	 * @return String
	 */
	String OnlineTime(int time)
	{
		long onlinetimeH;
		int onlinetimeM;
		if (((time / 60 / 60) - 0.5) <= 0)
		{
			onlinetimeH = 0;
		}
		else
		{
			onlinetimeH = Math.round((time / 60 / 60) - 0.5);
		}
		onlinetimeM = Math.round(((time / 60 / 60) - onlinetimeH) * 60);
		return "" + onlinetimeH + "h. " + onlinetimeM + "m.";
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
