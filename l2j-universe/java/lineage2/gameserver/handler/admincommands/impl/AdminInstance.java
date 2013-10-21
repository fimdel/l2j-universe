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

import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.instancemanager.SoDManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminInstance implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_instance.
		 */
		admin_instance,
		/**
		 * Field admin_instance_id.
		 */
		admin_instance_id,
		/**
		 * Field admin_collapse.
		 */
		admin_collapse,
		/**
		 * Field admin_reset_reuse.
		 */
		admin_reset_reuse,
		/**
		 * Field admin_reset_reuse_all.
		 */
		admin_reset_reuse_all,
		/**
		 * Field admin_set_reuse.
		 */
		admin_set_reuse,
		/**
		 * Field admin_addtiatkill.
		 */
		admin_addtiatkill
	}
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;
		if (!activeChar.getPlayerAccess().CanTeleport)
		{
			return false;
		}
		switch (command)
		{
			case admin_instance:
				listOfInstances(activeChar);
				break;
			case admin_instance_id:
				if (wordList.length > 1)
				{
					listOfCharsForInstance(activeChar, wordList[1]);
				}
				break;
			case admin_collapse:
				if (!activeChar.getReflection().isDefault())
				{
					activeChar.getReflection().collapse();
				}
				else
				{
					activeChar.sendMessage("Cannot collapse default reflection!");
				}
				break;
			case admin_reset_reuse:
				if ((wordList.length > 1) && (activeChar.getTarget() != null) && activeChar.getTarget().isPlayer())
				{
					Player p = activeChar.getTarget().getPlayer();
					p.removeInstanceReuse(Integer.parseInt(wordList[1]));
					Functions.sendDebugMessage(activeChar, "Instance reuse has been removed");
				}
				break;
			case admin_reset_reuse_all:
				if ((activeChar.getTarget() != null) && activeChar.getTarget().isPlayer())
				{
					Player p = activeChar.getTarget().getPlayer();
					p.removeAllInstanceReuses();
					Functions.sendDebugMessage(activeChar, "All instance reuses has been removed");
				}
				break;
			case admin_set_reuse:
				if (activeChar.getReflection() != null)
				{
					activeChar.getReflection().setReenterTime(System.currentTimeMillis());
				}
				break;
			case admin_addtiatkill:
				SoDManager.addTiatKill();
				break;
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[] * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
	
	/**
	 * Method listOfInstances.
	 * @param activeChar Player
	 */
	private void listOfInstances(Player activeChar)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuffer replyMSG = new StringBuffer("<html><title>Instance Menu</title><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center>List of Instances</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_admin\" width=40 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table><br><br>");
		for (Reflection reflection : ReflectionManager.getInstance().getAll())
		{
			if ((reflection == null) || reflection.isDefault() || reflection.isCollapseStarted())
			{
				continue;
			}
			int countPlayers = 0;
			if (reflection.getPlayers() != null)
			{
				countPlayers = reflection.getPlayers().size();
			}
			replyMSG.append("<a action=\"bypass -h admin_instance_id ").append(reflection.getId()).append(" \">").append(reflection.getName()).append('(').append(countPlayers).append(" players). Id: ").append(reflection.getId()).append("</a><br>");
		}
		replyMSG.append("<button value=\"Refresh\" action=\"bypass -h admin_instance\" width=50 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\">");
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
	
	/**
	 * Method listOfCharsForInstance.
	 * @param activeChar Player
	 * @param sid String
	 */
	private void listOfCharsForInstance(Player activeChar, String sid)
	{
		Reflection reflection = ReflectionManager.getInstance().get(Integer.parseInt(sid));
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuffer replyMSG = new StringBuffer("<html><title>Instance Menu</title><body><br>");
		if (reflection != null)
		{
			replyMSG.append("<table width=260><tr>");
			replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
			replyMSG.append("<td width=180><center>List of players in ").append(reflection.getName()).append("</center></td>");
			replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_instance\" width=40 height=20 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\"></td>");
			replyMSG.append("</tr></table><br><br>");
			for (Player player : reflection.getPlayers())
			{
				replyMSG.append("<a action=\"bypass -h admin_teleportto ").append(player.getName()).append(" \">").append(player.getName()).append("</a><br>");
			}
		}
		else
		{
			replyMSG.append("Instance not active.<br>");
			replyMSG.append("<a action=\"bypass -h admin_instance\">Back to list.</a><br>");
		}
		replyMSG.append("</body></html>");
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
}
