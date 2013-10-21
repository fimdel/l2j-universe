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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.WarehouseFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class WarehouseInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for WarehouseInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public WarehouseInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method getHtmlPath.
	 * @param npcId int
	 * @param val int
	 * @param player Player
	 * @return String
	 */
	@Override
	public String getHtmlPath(int npcId, int val, Player player)
	{
		String pom = "";
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		if (getTemplate().getHtmRoot() != null)
		{
			return getTemplate().getHtmRoot() + pom + ".htm";
		}
		return "warehouse/" + pom + ".htm";
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
		if (player.getEnchantScroll() != null)
		{
			Log.add("Player " + player.getName() + " trying to use enchant exploit[Warehouse], ban this player!", "illegal-actions");
			player.setEnchantScroll(null);
			return;
		}
		if (command.startsWith("WithdrawP"))
		{
			int val = Integer.parseInt(command.substring(10));
			if (val == 99)
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("warehouse/personal.htm");
				html.replace("%npcname%", getName());
				player.sendPacket(html);
			}
			else
			{
				WarehouseFunctions.showRetrieveWindow(player, val);
			}
		}
		else if (command.equals("DepositP"))
		{
			WarehouseFunctions.showDepositWindow(player);
		}
		else if (command.startsWith("WithdrawC"))
		{
			int val = Integer.parseInt(command.substring(10));
			if (val == 99)
			{
				NpcHtmlMessage html = new NpcHtmlMessage(player, this);
				html.setFile("warehouse/clan.htm");
				html.replace("%npcname%", getName());
				player.sendPacket(html);
			}
			else
			{
				WarehouseFunctions.showWithdrawWindowClan(player, val);
			}
		}
		else if (command.equals("DepositC"))
		{
			WarehouseFunctions.showDepositWindowClan(player);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
