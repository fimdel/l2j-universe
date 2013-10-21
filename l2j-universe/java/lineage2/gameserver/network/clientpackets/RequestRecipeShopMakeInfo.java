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

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ManufactureItem;
import lineage2.gameserver.network.serverpackets.RecipeShopItemInfo;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestRecipeShopMakeInfo extends L2GameClientPacket
{
	/**
	 * Field _manufacturerId.
	 */
	private int _manufacturerId;
	/**
	 * Field _recipeId.
	 */
	private int _recipeId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_manufacturerId = readD();
		_recipeId = readD();
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
		if (activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}
		Player manufacturer = (Player) activeChar.getVisibleObject(_manufacturerId);
		if ((manufacturer == null) || (manufacturer.getPrivateStoreType() != Player.STORE_PRIVATE_MANUFACTURE) || !manufacturer.isInRangeZ(activeChar, Creature.INTERACTION_DISTANCE))
		{
			activeChar.sendActionFailed();
			return;
		}
		long price = -1;
		for (ManufactureItem i : manufacturer.getCreateList())
		{
			if (i.getRecipeId() == _recipeId)
			{
				price = i.getCost();
				break;
			}
		}
		if (price == -1)
		{
			activeChar.sendActionFailed();
			return;
		}
		activeChar.sendPacket(new RecipeShopItemInfo(activeChar, manufacturer, _recipeId, price, 0xFFFFFFFF));
	}
}
