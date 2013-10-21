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
package lineage2.gameserver.utils;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BbsUtil
{
	/**
	 * Method htmlAll.
	 * @param htm String
	 * @param player Player
	 * @return String
	 */
	public static String htmlAll(String htm, Player player)
	{
		String html_all = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "block/allpages.htm", player);
		String html_menu = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "block/menu.htm", player);
		String html_copy = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "block/copyright.htm", player);
		html_all = html_all.replace("%main_menu%", html_menu);
		html_all = html_all.replace("%body_page%", htm);
		html_all = html_all.replace("%copyright%", html_copy);
		html_all = html_all.replace("%copyrightsym%", "©");
		return html_all;
	}
	
	/**
	 * Method htmlBuff.
	 * @param htm String
	 * @param player Player g
	 * @return String
	 */
	public static String htmlBuff(String htm, Player player)
	{
		String html_option = HtmCache.getInstance().getNotNull(Config.BBS_HOME_DIR + "pages/buffer/block/option.htm", player);
		htm = htm.replace("%main_optons%", html_option);
		htm = htmlAll(htm, player);
		return htm;
	}
}
