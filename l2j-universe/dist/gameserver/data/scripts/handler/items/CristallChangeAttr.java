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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ExChangeAttributeItemList;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.ItemTemplate;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CristallChangeAttr extends ScriptItemHandler
{
	/**
	 * Field ITEM_IDS.
	 */
	private final int[] ITEM_IDS =
	{
		33502,
		33835,
		33836,
		33837,
		33833,
		33834
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
		if (player.getPrivateStoreType() != Player.STORE_PRIVATE_NONE)
		{
			player.sendPacket(SystemMsg.YOU_CAN_NOT_CHANGE_THE_ATTRIBUTE_WHILE_OPERATING_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			return false;
		}
		switch (item.getItemId())
		{
			case 33502:
				sendAttributeItemList(item.getItemId(), player, ItemTemplate.Grade.S, ItemTemplate.Grade.S80);
				break;
			case 33833:
				sendAttributeItemList(item.getItemId(), player, ItemTemplate.Grade.S);
				break;
			case 33834:
				sendAttributeItemList(item.getItemId(), player, ItemTemplate.Grade.S80);
				break;
			case 33835:
				sendAttributeItemList(item.getItemId(), player, ItemTemplate.Grade.R);
				break;
			case 33836:
				sendAttributeItemList(item.getItemId(), player, ItemTemplate.Grade.R95);
				break;
			case 33837:
				sendAttributeItemList(item.getItemId(), player, ItemTemplate.Grade.R99);
				break;
		}
		return true;
	}
	
	/**
	 * Method sendAttributeItemList.
	 * @param itemId int
	 * @param player Player
	 * @param grades ItemTemplate.Grade[]
	 * @return boolean
	 */
	private boolean sendAttributeItemList(int itemId, Player player, ItemTemplate.Grade... grades)
	{
		final List<ItemInfo> itemsList = new ArrayList<>();
		final ItemInstance[] items = player.getInventory().getItems();
		for (ItemInstance item : items)
		{
			if (item.isWeapon() && (item.getAttackElementValue() > 0))
			{
				if (ArrayUtils.contains(grades, item.getCrystalType()))
				{
					itemsList.add(new ItemInfo(item));
				}
			}
		}
		if (itemsList.size() == 0)
		{
			player.sendPacket(SystemMsg.THE_ITEM_FOR_CHANGING_AN_ATTRIBUTE_DOES_NOT_EXIST);
			return false;
		}
		player.sendPacket(new ExChangeAttributeItemList(itemId, itemsList.toArray(new ItemInfo[itemsList.size()])));
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return ITEM_IDS;
	}
}
