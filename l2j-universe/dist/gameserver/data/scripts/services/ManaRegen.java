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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ManaRegen extends Functions
{
	/**
	 * Field ADENA. (value is 57)
	 */
	private static final int ADENA = 57;
	/**
	 * Field PRICE. (value is 5)
	 */
	private static final long PRICE = 5;
	
	/**
	 * Method DoManaRegen.
	 */
	public void DoManaRegen()
	{
		Player player = getSelf();
		long mp = (long) Math.floor(player.getMaxMp() - player.getCurrentMp());
		long fullCost = mp * PRICE;
		if (fullCost <= 0)
		{
			player.sendPacket(Msg.NOTHING_HAPPENED);
			return;
		}
		if (getItemCount(player, ADENA) >= fullCost)
		{
			removeItem(player, ADENA, fullCost);
			player.sendPacket(new SystemMessage(SystemMessage.S1_MPS_HAVE_BEEN_RESTORED).addNumber(mp));
			player.setCurrentMp(player.getMaxMp());
		}
		else
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
		}
	}
}
