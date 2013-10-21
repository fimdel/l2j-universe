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
package lineage2.gameserver.model.base;

import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.items.ItemAttributes;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MultiSellIngredient implements Cloneable
{
	/**
	 * Field _itemId.
	 */
	private int _itemId;
	/**
	 * Field _itemCount.
	 */
	private long _itemCount;
	/**
	 * Field _itemEnchant.
	 */
	private int _itemEnchant;
	/**
	 * Field _chance.
	 */
	int _chance;
	/**
	 * Field _itemAttributes.
	 */
	private ItemAttributes _itemAttributes;
	/**
	 * Field _mantainIngredient.
	 */
	private boolean _mantainIngredient;
	
	/**
	 * Constructor for MultiSellIngredient.
	 * @param itemId int
	 * @param itemCount long
	 */
	public MultiSellIngredient(int itemId, long itemCount)
	{
		this(itemId, itemCount, 0, 100);
	}
	
	/**
	 * Constructor for MultiSellIngredient.
	 * @param itemId int
	 * @param itemCount long
	 * @param chance int
	 */
	public MultiSellIngredient(int itemId, long itemCount, int chance)
	{
		this(itemId, itemCount, 0, chance);
	}
	
	/**
	 * Constructor for MultiSellIngredient.
	 * @param itemId int
	 * @param itemCount long
	 * @param enchant int
	 * @param chance int
	 */
	public MultiSellIngredient(int itemId, long itemCount, int enchant, int chance)
	{
		_itemId = itemId;
		_itemCount = itemCount;
		_itemEnchant = enchant;
		_mantainIngredient = false;
		_chance = chance;
		_itemAttributes = new ItemAttributes();
	}
	
	/**
	 * Method clone.
	 * @return MultiSellIngredient
	 */
	@Override
	public MultiSellIngredient clone()
	{
		MultiSellIngredient mi = new MultiSellIngredient(_itemId, _itemCount, _itemEnchant, _chance);
		mi.setMantainIngredient(_mantainIngredient);
		mi.setItemAttributes(_itemAttributes.clone());
		return mi;
	}
	
	/**
	 * Method setChance.
	 * @param chance int
	 */
	public void setChance(int chance)
	{
		_chance = chance;
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
	 * Method setItemId.
	 * @param itemId int
	 */
	public void setItemId(int itemId)
	{
		_itemId = itemId;
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
	 * Method setItemCount.
	 * @param itemCount long
	 */
	public void setItemCount(long itemCount)
	{
		_itemCount = itemCount;
	}
	
	/**
	 * Method getItemCount.
	 * @return long
	 */
	public long getItemCount()
	{
		return _itemCount;
	}
	
	/**
	 * Method isStackable.
	 * @return boolean
	 */
	public boolean isStackable()
	{
		return (_itemId <= 0) || ItemHolder.getInstance().getTemplate(_itemId).isStackable();
	}
	
	/**
	 * Method setItemEnchant.
	 * @param itemEnchant int
	 */
	public void setItemEnchant(int itemEnchant)
	{
		_itemEnchant = itemEnchant;
	}
	
	/**
	 * Method getItemEnchant.
	 * @return int
	 */
	public int getItemEnchant()
	{
		return _itemEnchant;
	}
	
	/**
	 * Method getItemAttributes.
	 * @return ItemAttributes
	 */
	public ItemAttributes getItemAttributes()
	{
		return _itemAttributes;
	}
	
	/**
	 * Method setItemAttributes.
	 * @param attr ItemAttributes
	 */
	public void setItemAttributes(ItemAttributes attr)
	{
		_itemAttributes = attr;
	}
	
	/**
	 * Method hashCode.
	 * @return int
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) (_itemCount ^ (_itemCount >>> 32));
		for (Element e : Element.VALUES)
		{
			result = (prime * result) + _itemAttributes.getValue(e);
		}
		result = (prime * result) + _itemEnchant;
		result = (prime * result) + _itemId;
		return result;
	}
	
	/**
	 * Method equals.
	 * @param obj Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		MultiSellIngredient other = (MultiSellIngredient) obj;
		if (_itemId != other._itemId)
		{
			return false;
		}
		if (_itemCount != other._itemCount)
		{
			return false;
		}
		if (_itemEnchant != other._itemEnchant)
		{
			return false;
		}
		for (Element e : Element.VALUES)
		{
			if (_itemAttributes.getValue(e) != other._itemAttributes.getValue(e))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method getMantainIngredient.
	 * @return boolean
	 */
	public boolean getMantainIngredient()
	{
		return _mantainIngredient;
	}
	
	/**
	 * Method setMantainIngredient.
	 * @param mantainIngredient boolean
	 */
	public void setMantainIngredient(boolean mantainIngredient)
	{
		_mantainIngredient = mantainIngredient;
	}
}
