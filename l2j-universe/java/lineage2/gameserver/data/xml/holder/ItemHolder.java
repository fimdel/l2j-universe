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
package lineage2.gameserver.data.xml.holder;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;
import lineage2.commons.data.xml.AbstractHolder;
import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ItemHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final ItemHolder _instance = new ItemHolder();
	/**
	 * Field _items.
	 */
	private final TIntObjectHashMap<ItemTemplate> _items = new TIntObjectHashMap<>();
	/**
	 * Field _allTemplates.
	 */
	private ItemTemplate[] _allTemplates;
	
	/**
	 * Method getInstance.
	 * @return ItemHolder
	 */
	public static ItemHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for ItemHolder.
	 */
	private ItemHolder()
	{
	}
	
	/**
	 * Method addItem.
	 * @param template ItemTemplate
	 */
	public void addItem(ItemTemplate template)
	{
		_items.put(template.getItemId(), template);
	}
	
	/**
	 * Method buildFastLookupTable.
	 */
	private void buildFastLookupTable()
	{
		int highestId = 0;
		for (int id : _items.keys())
		{
			if (id > highestId)
			{
				highestId = id;
			}
		}
		_allTemplates = new ItemTemplate[highestId + 1];
		for (TIntObjectIterator<ItemTemplate> iterator = _items.iterator(); iterator.hasNext();)
		{
			iterator.advance();
			_allTemplates[iterator.key()] = iterator.value();
		}
	}
	
	/**
	 * Method getTemplate.
	 * @param id int
	 * @return ItemTemplate
	 */
	public ItemTemplate getTemplate(int id)
	{
		ItemTemplate item = ArrayUtils.valid(_allTemplates, id);
		if (item == null)
		{
			warn("Not defined item id : " + id + ", or out of range!", new Exception());
			return null;
		}
		return _allTemplates[id];
	}
	
	/**
	 * Method getAllTemplates.
	 * @return ItemTemplate[]
	 */
	public ItemTemplate[] getAllTemplates()
	{
		return _allTemplates;
	}
	
	/**
	 * Method process.
	 */
	@Override
	protected void process()
	{
		buildFastLookupTable();
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _items.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_items.clear();
	}
}
