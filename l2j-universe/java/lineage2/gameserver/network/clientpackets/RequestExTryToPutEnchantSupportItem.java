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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.network.serverpackets.ExPutEnchantSupportItemResult;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExTryToPutEnchantSupportItem extends L2GameClientPacket
{
	/**
	 * Field _itemId.
	 */
	private int _itemId;
	/**
	 * Field _catalystId.
	 */
	private int _catalystId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_catalystId = readD();
		_itemId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		PcInventory inventory = activeChar.getInventory();
		ItemInstance itemToEnchant = inventory.getItemByObjectId(_itemId);
		ItemInstance catalyst = inventory.getItemByObjectId(_catalystId);
		if (ItemFunctions.checkCatalyst(itemToEnchant, catalyst))
		{
			activeChar.sendPacket(new ExPutEnchantSupportItemResult(1));
		}
		else
		{
			activeChar.sendPacket(new ExPutEnchantSupportItemResult(0));
		}
	}
}
