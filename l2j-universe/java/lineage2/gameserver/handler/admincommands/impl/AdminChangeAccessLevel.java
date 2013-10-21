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
package lineage2.gameserver.handler.admincommands.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.utils.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminChangeAccessLevel implements IAdminCommandHandler
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(AdminChangeAccessLevel.class);
	
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_changelvl.
		 */
		admin_changelvl,
		/**
		 * Field admin_moders.
		 */
		admin_moders,
		/**
		 * Field admin_moders_add.
		 */
		admin_moders_add,
		/**
		 * Field admin_moders_del.
		 */
		admin_moders_del,
		/**
		 * Field admin_penalty.
		 */
		admin_penalty
	}
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean
	 * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;
		if (!activeChar.getPlayerAccess().CanGmEdit)
		{
			return false;
		}
		switch (command)
		{
			case admin_changelvl:
				if (wordList.length == 2)
				{
					int lvl = Integer.parseInt(wordList[1]);
					if (activeChar.getTarget().isPlayer())
					{
						((Player) activeChar.getTarget()).setAccessLevel(lvl);
					}
				}
				else if (wordList.length == 3)
				{
					int lvl = Integer.parseInt(wordList[2]);
					Player player = GameObjectsStorage.getPlayer(wordList[1]);
					if (player != null)
					{
						player.setAccessLevel(lvl);
					}
				}
				break;
			case admin_moders:
				showModersPannel(activeChar);
				break;
			case admin_moders_add:
				if ((activeChar.getTarget() == null) || !activeChar.getTarget().isPlayer())
				{
					activeChar.sendMessage("Incorrect target. Please select a player.");
					showModersPannel(activeChar);
					return false;
				}
				Player modAdd = activeChar.getTarget().getPlayer();
				if (Config.gmlist.containsKey(modAdd.getObjectId()))
				{
					activeChar.sendMessage("Error: Moderator " + modAdd.getName() + " already in server access list.");
					showModersPannel(activeChar);
					return false;
				}
				String newFName = "m" + modAdd.getObjectId() + ".xml";
				if (!Files.copyFile(Config.GM_ACCESS_FILES_DIR + "template/moderator.xml", Config.GM_ACCESS_FILES_DIR + newFName))
				{
					activeChar.sendMessage("Error: Failed to copy access-file.");
					showModersPannel(activeChar);
					return false;
				}
				String res = "";
				try
				{
					BufferedReader in = new BufferedReader(new FileReader(Config.GM_ACCESS_FILES_DIR + newFName));
					String str;
					while ((str = in.readLine()) != null)
					{
						res += str + "\n";
					}
					in.close();
					res = res.replaceFirst("ObjIdPlayer", "" + modAdd.getObjectId());
					Files.writeFile(Config.GM_ACCESS_FILES_DIR + newFName, res);
				}
				catch (Exception e)
				{
					activeChar.sendMessage("Error: Failed to modify object ID in access-file.");
					File fDel = new File(Config.GM_ACCESS_FILES_DIR + newFName);
					if (fDel.exists())
					{
						fDel.delete();
					}
					showModersPannel(activeChar);
					return false;
				}
				File af = new File(Config.GM_ACCESS_FILES_DIR + newFName);
				if (!af.exists())
				{
					activeChar.sendMessage("Error: Failed to read access-file for " + modAdd.getName());
					showModersPannel(activeChar);
					return false;
				}
				Config.loadGMAccess(af);
				modAdd.setPlayerAccess(Config.gmlist.get(modAdd.getObjectId()));
				activeChar.sendMessage("Moderator " + modAdd.getName() + " added.");
				showModersPannel(activeChar);
				break;
			case admin_moders_del:
				if (wordList.length < 2)
				{
					activeChar.sendMessage("Please specify moderator object ID to delete moderator.");
					showModersPannel(activeChar);
					return false;
				}
				int oid = Integer.parseInt(wordList[1]);
				if (Config.gmlist.containsKey(oid))
				{
					Config.gmlist.remove(oid);
				}
				else
				{
					activeChar.sendMessage("Error: Moderator with object ID " + oid + " not found in server access lits.");
					showModersPannel(activeChar);
					return false;
				}
				Player modDel = GameObjectsStorage.getPlayer(oid);
				if (modDel != null)
				{
					modDel.setPlayerAccess(null);
				}
				String fname = "m" + oid + ".xml";
				File f = new File(Config.GM_ACCESS_FILES_DIR + fname);
				if (!f.exists() || !f.isFile() || !f.delete())
				{
					activeChar.sendMessage("Error: Can't delete access-file: " + fname);
					showModersPannel(activeChar);
					return false;
				}
				if (modDel != null)
				{
					activeChar.sendMessage("Moderator " + modDel.getName() + " deleted.");
				}
				else
				{
					activeChar.sendMessage("Moderator with object ID " + oid + " deleted.");
				}
				showModersPannel(activeChar);
				break;
			case admin_penalty:
				if (wordList.length < 2)
				{
					activeChar.sendMessage("USAGE: //penalty charName [count] [reason]");
					return false;
				}
				int count = 1;
				if (wordList.length > 2)
				{
					count = Integer.parseInt(wordList[2]);
				}
				String reason = "не указана";
				if (wordList.length > 3)
				{
					reason = wordList[3];
				}
				int oId = 0;
				Player player = GameObjectsStorage.getPlayer(wordList[1]);
				if ((player != null) && player.getPlayerAccess().CanBanChat)
				{
					oId = player.getObjectId();
					int oldPenaltyCount = 0;
					String oldPenalty = player.getVar("penaltyChatCount");
					if (oldPenalty != null)
					{
						oldPenaltyCount = Integer.parseInt(oldPenalty);
					}
					player.setVar("penaltyChatCount", "" + (oldPenaltyCount + count), -1);
				}
				else
				{
					oId = mysql.simple_get_int("obj_Id", "characters", "`char_name`='" + wordList[1] + "'");
					if (oId > 0)
					{
						Integer oldCount = (Integer) mysql.get("SELECT `value` FROM character_variables WHERE `obj_id` = " + oId + " AND `name` = 'penaltyChatCount'");
						mysql.set("REPLACE INTO character_variables (obj_id, type, name, value, expire_time) VALUES (" + oId + ",'user-var','penaltyChatCount','" + (oldCount + count) + "',-1)");
					}
				}
				if (oId > 0)
				{
					if (Config.BANCHAT_ANNOUNCE_FOR_ALL_WORLD)
					{
						Announcements.getInstance().announceToAll(activeChar + " о�?трафовал модератора " + wordList[1] + " на " + count + ", причина: " + reason + ".");
					}
					else
					{
						Announcements.shout(activeChar, activeChar + " о�?трафовал модератора " + wordList[1] + " на " + count + ", причина: " + reason + ".", ChatType.CRITICAL_ANNOUNCE);
					}
				}
				break;
		}
		return true;
	}
	
	/**
	 * Method showModersPannel.
	 * @param activeChar Player
	 */
	private static void showModersPannel(Player activeChar)
	{
		NpcHtmlMessage reply = new NpcHtmlMessage(5);
		String html = "Moderators managment panel.<br>";
		File dir = new File(Config.GM_ACCESS_FILES_DIR);
		if (!dir.exists() || !dir.isDirectory())
		{
			html += "Error: Can't open permissions folder.";
			reply.setHtml(html);
			activeChar.sendPacket(reply);
			return;
		}
		html += "<p align=right>";
		html += "<button width=120 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" action=\"bypass -h admin_moders_add\" value=\"Add modrator\">";
		html += "</p><br>";
		html += "<center><font color=LEVEL>Moderators:</font></center>";
		html += "<table width=285>";
		for (File f : dir.listFiles())
		{
			if (f.isDirectory() || ((f.getName().length() == 0) || (f.getName().charAt(0) != 'm')) || !f.getName().endsWith(".xml"))
			{
				continue;
			}
			int oid = Integer.parseInt(f.getName().substring(1, 10));
			String pName = getPlayerNameByObjId(oid);
			boolean on = false;
			if ((pName == null) || pName.isEmpty())
			{
				pName = "" + oid;
			}
			else
			{
				on = GameObjectsStorage.getPlayer(pName) != null;
			}
			html += "<tr>";
			html += "<td width=140>" + pName;
			html += on ? " <font color=\"33CC66\">(on)</font>" : "";
			html += "</td>";
			html += "<td width=45><button width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" action=\"bypass -h admin_moders_log " + oid + "\" value=\"Logs\"></td>";
			html += "<td width=45><button width=20 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" action=\"bypass -h admin_moders_del " + oid + "\" value=\"X\"></td>";
			html += "</tr>";
		}
		html += "</table>";
		reply.setHtml(html);
		activeChar.sendPacket(reply);
	}
	
	/**
	 * Method getPlayerNameByObjId.
	 * @param oid int
	 * @return String
	 */
	private static String getPlayerNameByObjId(int oid)
	{
		String pName = null;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT `char_name` FROM `characters` WHERE `obj_Id`=\"" + oid + "\" LIMIT 1");
			rset = statement.executeQuery();
			if (rset.next())
			{
				pName = rset.getString(1);
			}
		}
		catch (Exception e)
		{
			_log.warn("SQL Error: " + e);
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return pName;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[]
	 * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}
