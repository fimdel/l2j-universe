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
package handler.voicecommands;

import java.util.List;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler;
import lineage2.gameserver.handler.voicecommands.VoicedCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.UnitMember;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CWHPrivileges implements IVoicedCommandHandler, ScriptFile
{
	/**
	 * Field _commandList.
	 */
	private final String[] _commandList = new String[]
	{
		"clan"
	};
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		VoicedCommandHandler.getInstance().registerVoicedCommandHandler(this);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
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
	 * Method getVoicedCommandList.
	 * @return String[] * @see lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return _commandList;
	}
	
	/**
	 * Method useVoicedCommand.
	 * @param command String
	 * @param activeChar Player
	 * @param args String
	 * @return boolean * @see lineage2.gameserver.handler.voicecommands.IVoicedCommandHandler#useVoicedCommand(String, Player, String)
	 */
	@Override
	public boolean useVoicedCommand(String command, Player activeChar, String args)
	{
		if (activeChar.getClan() == null)
		{
			return false;
		}
		if (command.equals("clan"))
		{
			if (Config.ALT_ALLOW_CLAN_COMMAND_ONLY_FOR_CLAN_LEADER && !activeChar.isClanLeader())
			{
				return false;
			}
			if (!((activeChar.getClanPrivileges() & Clan.CP_CL_MANAGE_RANKS) == Clan.CP_CL_MANAGE_RANKS))
			{
				return false;
			}
			if (args != null)
			{
				String[] param = args.split(" ");
				if (param.length > 0)
				{
					if (param[0].equalsIgnoreCase("allowwh") && (param.length > 1))
					{
						UnitMember cm = activeChar.getClan().getAnyMember(param[1]);
						if ((cm != null) && (cm.getPlayer() != null))
						{
							if (cm.getPlayer().getVarB("canWhWithdraw"))
							{
								cm.getPlayer().unsetVar("canWhWithdraw");
								activeChar.sendMessage("Privilege removed successfully");
							}
							else
							{
								cm.getPlayer().setVar("canWhWithdraw", "1", -1);
								activeChar.sendMessage("Privilege given successfully");
							}
						}
						else if (cm != null)
						{
							int state = mysql.simple_get_int("value", "character_variables", "obj_id=" + cm.getObjectId() + " AND name LIKE 'canWhWithdraw'");
							if (state > 0)
							{
								mysql.set("DELETE FROM `character_variables` WHERE obj_id=" + cm.getObjectId() + " AND name LIKE 'canWhWithdraw' LIMIT 1");
								activeChar.sendMessage("Privilege removed successfully");
							}
							else
							{
								mysql.set("INSERT INTO character_variables  (obj_id, type, name, value, expire_time) VALUES (" + cm.getObjectId() + ",'user-var','canWhWithdraw','1',-1)");
								activeChar.sendMessage("Privilege given successfully");
							}
						}
						else
						{
							activeChar.sendMessage("Player not found.");
						}
					}
					else if (param[0].equalsIgnoreCase("list"))
					{
						StringBuilder sb = new StringBuilder("SELECT `obj_id` FROM `character_variables` WHERE `obj_id` IN (");
						List<UnitMember> members = activeChar.getClan().getAllMembers();
						for (int i = 0; i < members.size(); i++)
						{
							sb.append(members.get(i).getObjectId());
							if (i < (members.size() - 1))
							{
								sb.append(',');
							}
						}
						sb.append(") AND `name`='canWhWithdraw'");
						List<Object> list = mysql.get_array(sb.toString());
						sb = new StringBuilder("<html><body>Clan CP (.clan)<br><br><table>");
						for (Object o_id : list)
						{
							for (UnitMember m : members)
							{
								if (m.getObjectId() == Integer.parseInt(o_id.toString()))
								{
									sb.append("<tr><td width=10></td><td width=60>").append(m.getName()).append("</td><td width=20><button width=50 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" action=\"bypass -h user_clan allowwh ").append(m.getName()).append("\" value=\"Remove\">").append("<br></td></tr>");
								}
							}
						}
						sb.append("<tr><td width=10></td><td width=20><button width=60 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" action=\"bypass -h user_clan\" value=\"Back\"></td></tr></table></body></html>");
						Functions.show(sb.toString(), activeChar, null);
						return true;
					}
				}
			}
			String dialog = HtmCache.getInstance().getNotNull("scripts/services/clan.htm", activeChar);
			if (!Config.SERVICES_EXPAND_CWH_ENABLED)
			{
				dialog = dialog.replaceFirst("%whextprice%", "service disabled");
			}
			else
			{
				dialog = dialog.replaceFirst("%whextprice%", Config.SERVICES_EXPAND_CWH_PRICE + " " + ItemHolder.getInstance().getTemplate(Config.SERVICES_EXPAND_CWH_ITEM).getName());
			}
			Functions.show(dialog, activeChar, null);
			return true;
		}
		return false;
	}
}
