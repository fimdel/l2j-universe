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
package lineage2.gameserver.model.instances;

import java.util.StringTokenizer;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class WyvernManagerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(WyvernManagerInstance.class);
	
	/**
	 * Constructor for WyvernManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public WyvernManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken();
		boolean condition = validateCondition(player);
		if (actualCommand.equalsIgnoreCase("RideHelp"))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("wyvern/help_ride.htm");
			html.replace("%npcname%", "Wyvern Manager " + getName());
			player.sendPacket(html);
			player.sendActionFailed();
		}
		if (condition)
		{
			if (actualCommand.equalsIgnoreCase("RideWyvern") && player.isClanLeader())
			{
				if (!player.isRiding() || !PetDataTable.isStrider(player.getMountNpcId()))
				{
					NpcHtmlMessage html = new NpcHtmlMessage(player, this);
					html.setFile("wyvern/not_ready.htm");
					html.replace("%npcname%", "Wyvern Manager " + getName());
					player.sendPacket(html);
				}
				else if ((player.getInventory().getItemByItemId(1460) == null) || (player.getInventory().getItemByItemId(1460).getCount() < 25))
				{
					NpcHtmlMessage html = new NpcHtmlMessage(player, this);
					html.setFile("wyvern/havenot_cry.htm");
					html.replace("%npcname%", "Wyvern Manager " + getName());
					player.sendPacket(html);
				}
				else if (player.getInventory().destroyItemByItemId(1460, 25L))
				{
					player.setMount(PetDataTable.WYVERN_ID, player.getMountObjId(), player.getMountLevel());
					NpcHtmlMessage html = new NpcHtmlMessage(player, this);
					html.setFile("wyvern/after_ride.htm");
					html.replace("%npcname%", "Wyvern Manager " + getName());
					player.sendPacket(html);
				}
			}
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		if (!validateCondition(player))
		{
			NpcHtmlMessage html = new NpcHtmlMessage(player, this);
			html.setFile("wyvern/lord_only.htm");
			html.replace("%npcname%", "Wyvern Manager " + getName());
			player.sendPacket(html);
			player.sendActionFailed();
			return;
		}
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		html.setFile("wyvern/lord_here.htm");
		html.replace("%Char_name%", String.valueOf(player.getName()));
		html.replace("%npcname%", "Wyvern Manager " + getName());
		player.sendPacket(html);
		player.sendActionFailed();
	}
	
	/**
	 * Method validateCondition.
	 * @param player Player
	 * @return boolean
	 */
	private boolean validateCondition(Player player)
	{
		Residence residence = getCastle();
		if ((residence != null) && (residence.getId() > 0))
		{
			if (player.getClan() != null)
			{
				if ((residence.getOwnerId() == player.getClanId()) && player.isClanLeader())
				{
					return true;
				}
			}
		}
		residence = getFortress();
		if ((residence != null) && (residence.getId() > 0))
		{
			if (player.getClan() != null)
			{
				if ((residence.getOwnerId() == player.getClanId()) && player.isClanLeader())
				{
					return true;
				}
			}
		}
		residence = getClanHall();
		if ((residence != null) && (residence.getId() > 0))
		{
			if (player.getClan() != null)
			{
				if ((residence.getOwnerId() == player.getClanId()) && player.isClanLeader())
				{
					return true;
				}
			}
		}
		return false;
	}
}
