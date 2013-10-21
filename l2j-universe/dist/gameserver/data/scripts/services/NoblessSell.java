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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.olympiad.Olympiad;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class NoblessSell extends Functions
{
	/**
	 * Method get.
	 */
	public void get()
	{
		Player player = getSelf();
		if (player.isNoble())
		{
			return;
		}
		if (player.getSubLevel() < 75)
		{
			player.sendMessage("You must make sub class level 75 first.");
			return;
		}
		if (player.getInventory().destroyItemByItemId(Config.SERVICES_NOBLESS_SELL_ITEM, Config.SERVICES_NOBLESS_SELL_PRICE))
		{
			becomeNoble();
		}
		else if (Config.SERVICES_NOBLESS_SELL_ITEM == 57)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		}
		else
		{
			player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
		}
	}
	
	/**
	 * Method becomeNoble.
	 */
	public void becomeNoble()
	{
		Player player = getSelf();
		if ((player == null) || player.isNoble())
		{
			return;
		}
		Olympiad.addNoble(player);
		player.setNoble(true);
		player.updatePledgeClass();
		player.updateNobleSkills();
		player.sendSkillList();
		player.broadcastUserInfo();
	}
}
