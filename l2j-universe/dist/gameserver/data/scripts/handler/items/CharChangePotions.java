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

import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CharChangePotions extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private static final int[] _itemIds =
	{
		5235,
		5236,
		5237,
		5238,
		5239,
		5240,
		5241,
		5242,
		5243,
		5244,
		5245,
		5246,
		5247,
		5248
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
		if (!player.getInventory().destroyItem(item, 1))
		{
			player.sendActionFailed();
			return false;
		}
		switch (itemId)
		{
			case 5235:
				player.setFace(0);
				break;
			case 5236:
				player.setFace(1);
				break;
			case 5237:
				player.setFace(2);
				break;
			case 5238:
				player.setHairColor(0);
				break;
			case 5239:
				player.setHairColor(1);
				break;
			case 5240:
				player.setHairColor(2);
				break;
			case 5241:
				player.setHairColor(3);
				break;
			case 5242:
				player.setHairStyle(0);
				break;
			case 5243:
				player.setHairStyle(1);
				break;
			case 5244:
				player.setHairStyle(2);
				break;
			case 5245:
				player.setHairStyle(3);
				break;
			case 5246:
				player.setHairStyle(4);
				break;
			case 5247:
				player.setHairStyle(5);
				break;
			case 5248:
				player.setHairStyle(6);
				break;
		}
		player.broadcastPacket(new MagicSkillUse(player, player, 2003, 1, 1, 0));
		player.broadcastCharInfo();
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
