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
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BuyHeroStatus extends Functions
{
	/**
	 * Method list.
	 */
	public void list()
	{
		Player player = getSelf();
		if (!Config.SERVICES_HERO_SELL_ENABLED)
		{
			show(HtmCache.getInstance().getNotNull("npcdefault.htm", player), player);
			return;
		}
		String html = null;
		html = HtmCache.getInstance().getNotNull("scripts/services/BuyHero.htm", player);
		String add = "";
		for (int i = 0; i < Config.SERVICES_HERO_SELL_DAY.length; i++)
		{
			add += "<a action=\"bypass -h scripts_services.BuyHeroStatus:get " + i + "\">" + "for " + Config.SERVICES_HERO_SELL_DAY[i] + " days - " + Config.SERVICES_HERO_SELL_PRICE[i] + " " + ItemHolder.getInstance().getTemplate(Config.SERVICES_HERO_SELL_ITEM[i]).getName() + "</a><br>";
		}
		html = html.replaceFirst("%toreplace%", add);
		show(html, player);
	}
	
	/**
	 * Method get.
	 * @param param String[]
	 */
	public void get(String[] param)
	{
		Player player = getSelf();
		if (!Config.SERVICES_HERO_SELL_ENABLED)
		{
			show(HtmCache.getInstance().getNotNull("npcdefault.htm", player), player);
			return;
		}
		int i = Integer.parseInt(param[0]);
		if ((Functions.getItemCount(player, Config.SERVICES_HERO_SELL_ITEM[i]) >= Config.SERVICES_HERO_SELL_PRICE[i]) && player.isNoble())
		{
			if (!player.isHero())
			{
				player.setVar("HeroPeriod", (System.currentTimeMillis() + (60 * 1000 * 60 * 24 * Config.SERVICES_HERO_SELL_DAY[i])), -1);
				Functions.removeItem(player, Config.SERVICES_HERO_SELL_ITEM[i], Config.SERVICES_HERO_SELL_PRICE[i]);
				player.setHero(player);
			}
		}
		else
		{
			player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
		}
	}
}
