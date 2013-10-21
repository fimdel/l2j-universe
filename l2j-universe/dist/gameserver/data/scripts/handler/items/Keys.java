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

import gnu.trove.set.hash.TIntHashSet;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.DoorHolder;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.DoorTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Keys extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private int[] _itemIds = null;
	
	/**
	 * Constructor for Keys.
	 */
	public Keys()
	{
		final TIntHashSet keys = new TIntHashSet();
		for (DoorTemplate door : DoorHolder.getInstance().getDoors().values())
		{
			if ((door != null) && (door.getKey() > 0))
			{
				keys.add(door.getKey());
			}
		}
		_itemIds = keys.toArray();
	}
	
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
		final Player player = playable.getPlayer();
		final GameObject target = player.getTarget();
		if ((target == null) || !target.isDoor())
		{
			player.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return false;
		}
		final DoorInstance door = (DoorInstance) target;
		if (door.isOpen())
		{
			player.sendPacket(Msg.IT_IS_NOT_LOCKED);
			return false;
		}
		if ((door.getKey() <= 0) || (item.getItemId() != door.getKey()))
		{
			player.sendPacket(Msg.YOU_ARE_UNABLE_TO_UNLOCK_THE_DOOR);
			return false;
		}
		if (player.getDistance(door) > 300)
		{
			player.sendPacket(Msg.YOU_CANNOT_CONTROL_BECAUSE_YOU_ARE_TOO_FAR);
			return false;
		}
		if (!player.getInventory().destroyItem(item, 1L))
		{
			player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			return false;
		}
		player.sendPacket(SystemMessage2.removeItems(item.getItemId(), 1));
		player.sendMessage(new CustomMessage("lineage2.gameserver.skills.skillclasses.Unlock.Success", player));
		door.openMe(player, true);
		return true;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}
