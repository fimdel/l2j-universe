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
package lineage2.gameserver.stats.conditions;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ConditionSlotItemId extends ConditionInventory
{
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _enchantLevel.
	 */
	private final int _enchantLevel;
	
	/**
	 * Constructor for ConditionSlotItemId.
	 * @param slot int
	 * @param itemId int
	 * @param enchantLevel int
	 */
	public ConditionSlotItemId(int slot, int itemId, int enchantLevel)
	{
		super(slot);
		_itemId = itemId;
		_enchantLevel = enchantLevel;
	}
	
	/**
	 * Method testImpl.
	 * @param env Env
	 * @return boolean
	 */
	@Override
	protected boolean testImpl(Env env)
	{
		if (!env.character.isPlayer())
		{
			return false;
		}
		Inventory inv = ((Player) env.character).getInventory();
		ItemInstance item = inv.getPaperdollItem(_slot);
		if (item == null)
		{
			return _itemId == 0;
		}
		return (item.getItemId() == _itemId) && (item.getEnchantLevel() >= _enchantLevel);
	}
}
