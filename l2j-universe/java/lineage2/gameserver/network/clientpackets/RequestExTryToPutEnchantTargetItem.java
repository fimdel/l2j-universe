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
import lineage2.gameserver.model.items.etcitems.EnchantScrollInfo;
import lineage2.gameserver.model.items.etcitems.EnchantScrollManager;
import lineage2.gameserver.network.serverpackets.ExPutEnchantTargetItemResult;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExTryToPutEnchantTargetItem extends AbstractEnchantPacket
{
	/**
	 * Field _objectId.
	 */
	private int _objectId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		if (!isValidPlayer(player))
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			player.setEnchantScroll(null);
			return;
		}
		PcInventory inventory = player.getInventory();
		ItemInstance itemToEnchant = inventory.getItemByObjectId(_objectId);
		ItemInstance scroll = player.getEnchantScroll();
		if ((itemToEnchant == null) || (scroll == null))
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			player.setEnchantScroll(null);
			return;
		}
		Log.add(player.getName() + "|Trying to put enchant|" + itemToEnchant.getItemId() + "|+" + itemToEnchant.getEnchantLevel() + "|" + itemToEnchant.getObjectId(), "enchants");
		EnchantScrollInfo esi = EnchantScrollManager.getScrollInfo(scroll.getItemId());
		if (esi == null)
		{
			player.sendActionFailed();
			return;
		}
		if (!checkItem(itemToEnchant, esi))
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			player.sendPacket(SystemMsg.INAPPROPRIATE_ENCHANT_CONDITIONS);
			player.setEnchantScroll(null);
			return;
		}
		if ((scroll = inventory.getItemByObjectId(scroll.getObjectId())) == null)
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			player.setEnchantScroll(null);
			return;
		}
		if ((itemToEnchant.getEnchantLevel() >= esi.getMax()) || (itemToEnchant.getEnchantLevel() < esi.getMin()))
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			player.sendPacket(SystemMsg.INAPPROPRIATE_ENCHANT_CONDITIONS);
			player.setEnchantScroll(null);
			return;
		}
		if (itemToEnchant.getOwnerId() != player.getObjectId())
		{
			player.sendPacket(ExPutEnchantTargetItemResult.FAIL);
			player.setEnchantScroll(null);
			return;
		}
		player.sendPacket(ExPutEnchantTargetItemResult.SUCCESS);
	}
}
