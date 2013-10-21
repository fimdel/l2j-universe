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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NickColor extends Functions
{
	/**
	 * Method list.
	 */
	public void list()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		StringBuilder append = new StringBuilder();
		append.append("You can change nick color for small price ").append(Config.SERVICES_CHANGE_NICK_COLOR_PRICE).append(' ').append(ItemHolder.getInstance().getTemplate(Config.SERVICES_CHANGE_NICK_COLOR_ITEM).getName()).append('.');
		append.append("<br>Possible colors:<br>");
		for (String color : Config.SERVICES_CHANGE_NICK_COLOR_LIST)
		{
			append.append("<br><a action=\"bypass -h scripts_services.NickColor:change ").append(color).append("\"><font color=\"").append(color.substring(4, 6) + color.substring(2, 4) + color.substring(0, 2)).append("\">").append(color.substring(4, 6) + color.substring(2, 4) + color.substring(0, 2)).append("</font></a>");
		}
		append.append("<br><a action=\"bypass -h scripts_services.NickColor:change FFFFFF\"><font color=\"FFFFFF\">Revert to default (free)</font></a>");
		show(append.toString(), player, null);
	}
	
	/**
	 * Method change.
	 * @param param String[]
	 */
	public void change(String[] param)
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		if (param[0].equalsIgnoreCase("FFFFFF"))
		{
			player.setNameColor(Integer.decode("0xFFFFFF"));
			player.broadcastUserInfo();
			return;
		}
		if (player.getInventory().destroyItemByItemId(Config.SERVICES_CHANGE_NICK_COLOR_ITEM, Config.SERVICES_CHANGE_NICK_COLOR_PRICE))
		{
			player.setNameColor(Integer.decode("0x" + param[0]));
			player.broadcastUserInfo();
		}
		else if (Config.SERVICES_CHANGE_NICK_COLOR_ITEM == 57)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		}
		else
		{
			player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
		}
	}
}
