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
package lineage2.gameserver.templates.item.support;

import java.util.Collections;
import java.util.Set;

import lineage2.gameserver.templates.item.ItemTemplate;

import org.napile.primitive.Containers;
import org.napile.primitive.sets.IntSet;
import org.napile.primitive.sets.impl.HashIntSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EnchantItem
{
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _chance.
	 */
	private final int _chance;
	/**
	 * Field _maxEnchant.
	 */
	private final int _maxEnchant;
	/**
	 * Field _items.
	 */
	private IntSet _items = Containers.EMPTY_INT_SET;
	/**
	 * Field _grades.
	 */
	private final Set<ItemTemplate.Grade> _grades = Collections.emptySet();
	
	/**
	 * Constructor for EnchantItem.
	 * @param itemId int
	 * @param chance int
	 * @param maxEnchant int
	 */
	public EnchantItem(int itemId, int chance, int maxEnchant)
	{
		_itemId = itemId;
		_chance = chance;
		_maxEnchant = maxEnchant;
	}
	
	/**
	 * Method addItemId.
	 * @param id int
	 */
	public void addItemId(int id)
	{
		if (_items.isEmpty())
		{
			_items = new HashIntSet();
		}
		_items.add(id);
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * Method getChance.
	 * @return int
	 */
	public int getChance()
	{
		return _chance;
	}
	
	/**
	 * Method getMaxEnchant.
	 * @return int
	 */
	public int getMaxEnchant()
	{
		return _maxEnchant;
	}
	
	/**
	 * Method getGrades.
	 * @return Set<ItemTemplate.Grade>
	 */
	public Set<ItemTemplate.Grade> getGrades()
	{
		return _grades;
	}
	
	/**
	 * Method getItems.
	 * @return IntSet
	 */
	public IntSet getItems()
	{
		return _items;
	}
}
