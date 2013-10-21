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
package services;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ExpandCWH extends Functions
{
	/**
	 * Method get.
	 */
	public void get()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (!Config.SERVICES_EXPAND_CWH_ENABLED)
		{
			show("Серви�? откл�?чен.", player);
			return;
		}
		if (player.getClan() == null)
		{
			player.sendMessage("You must be in clan.");
			return;
		}
		if (player.getInventory().destroyItemByItemId(Config.SERVICES_EXPAND_CWH_ITEM, Config.SERVICES_EXPAND_CWH_PRICE))
		{
			player.getClan().setWhBonus(player.getClan().getWhBonus() + 1);
			player.sendMessage("Warehouse capacity is now " + (Config.WAREHOUSE_SLOTS_CLAN + player.getClan().getWhBonus()));
		}
		else if (Config.SERVICES_EXPAND_CWH_ITEM == 57)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		}
		else
		{
			player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
		}
		/**
		 * Method show.
		 */
		show();
	}
	
	public void show()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (!Config.SERVICES_EXPAND_WAREHOUSE_ENABLED)
		{
			show("Серви�? откл�?чен.", player);
			return;
		}
		if (player.getClan() == null)
		{
			player.sendMessage("You must be in clan.");
			return;
		}
		ItemTemplate item = ItemHolder.getInstance().getTemplate(Config.SERVICES_EXPAND_CWH_ITEM);
		String out = "";
		out += "<html><body>Ра�?�?ирение кланового �?клада";
		out += "<br><br><table>";
		out += "<tr><td>Текущий размер:</td><td>" + (Config.WAREHOUSE_SLOTS_CLAN + player.getClan().getWhBonus()) + "</td></tr>";
		out += "<tr><td>Стоимо�?т�? �?лота:</td><td>" + Config.SERVICES_EXPAND_CWH_PRICE + " " + item.getName() + "</td></tr>";
		out += "</table><br><br>";
		out += "<button width=100 height=15 back=\"L2UI_CT1.Button_DF_Down\" fore=\"L2UI_CT1.Button_DF\" action=\"bypass -h scripts_services.ExpandCWH:get\" value=\"Ра�?�?ирит�?\">";
		out += "</body></html>";
		show(out, player);
	}
}
