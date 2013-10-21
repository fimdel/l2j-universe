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
package handler.items;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.Dice;
import lineage2.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RollingDice extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private static final int[] _itemIds =
	{
		4625,
		4626,
		4627,
		4628
	};
	
	/**
	 * Method useItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean * @see lineage2.gameserver.handler.items.IItemHandler#useItem(Playable, ItemInstance, boolean)
	 */
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if ((playable == null) || !playable.isPlayer())
		{
			return false;
		}
		final Player player = (Player) playable;
		final int itemId = item.getItemId();
		if (player.isInOlympiadMode())
		{
			player.sendPacket(Msg.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
			return false;
		}
		if (player.isSitting())
		{
			player.sendPacket(Msg.YOU_CANNOT_MOVE_WHILE_SITTING);
			return false;
		}
		final int number = Rnd.get(1, 6);
		if (number == 0)
		{
			player.sendPacket(Msg.YOU_MAY_NOT_THROW_THE_DICE_AT_THIS_TIMETRY_AGAIN_LATER);
			return false;
		}
		player.broadcastPacket(new Dice(player.getObjectId(), itemId, number, player.getX() - 30, player.getY() - 30, player.getZ()), new SystemMessage(SystemMessage.S1_HAS_ROLLED_S2).addString(player.getName()).addNumber(number));
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public final int[] getItemIds()
	{
		return _itemIds;
	}
}
