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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExPutItemResultForVariationMake;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestConfirmTargetItem extends AbstractRefinePacket
{
	/**
	 * Field _itemObjId.
	 */
	private int _itemObjId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_itemObjId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		ItemInstance item = activeChar.getInventory().getItemByObjectId(_itemObjId);
		if (item == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		if (!isValid(activeChar, item))
		{
			if (item.isAugmented())
			{
				activeChar.sendPacket(Msg.ONCE_AN_ITEM_IS_AUGMENTED_IT_CANNOT_BE_AUGMENTED_AGAIN);
				return;
			}
			activeChar.sendPacket(Msg.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}
		activeChar.sendPacket(new ExPutItemResultForVariationMake(_itemObjId), Msg.SELECT_THE_CATALYST_FOR_AUGMENTATION);
	}
}
