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
package lineage2.gameserver.model;

import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ProductItemComponent
{
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _count.
	 */
	private final int _count;
	/**
	 * Field _weight.
	 */
	private final int _weight;
	/**
	 * Field _dropable.
	 */
	private final boolean _dropable;
	
	/**
	 * Constructor for ProductItemComponent.
	 * @param item_id int
	 * @param count int
	 */
	public ProductItemComponent(int item_id, int count)
	{
		_itemId = item_id;
		_count = count;
		ItemTemplate item = ItemHolder.getInstance().getTemplate(item_id);
		if (item != null)
		{
			_weight = item.getWeight();
			_dropable = item.isDropable();
		}
		else
		{
			_weight = 0;
			_dropable = true;
		}
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
	 * Method getCount.
	 * @return int
	 */
	public int getCount()
	{
		return _count;
	}
	
	/**
	 * Method getWeight.
	 * @return int
	 */
	public int getWeight()
	{
		return _weight;
	}
	
	/**
	 * Method isDropable.
	 * @return boolean
	 */
	public boolean isDropable()
	{
		return _dropable;
	}
}
