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
package lineage2.gameserver.handler.items;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ItemHandler extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final ItemHandler _instance = new ItemHandler();
	
	/**
	 * Method getInstance.
	 * @return ItemHandler
	 */
	public static ItemHandler getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for ItemHandler.
	 */
	private ItemHandler()
	{
	}
	
	/**
	 * Method registerItemHandler.
	 * @param handler IItemHandler
	 */
	public void registerItemHandler(IItemHandler handler)
	{
		int[] ids = handler.getItemIds();
		for (int itemId : ids)
		{
			ItemTemplate template = ItemHolder.getInstance().getTemplate(itemId);
			if (template == null)
			{
				warn("Item not found: " + itemId + " handler: " + handler.getClass().getSimpleName());
			}
			else if (template.getHandler() != IItemHandler.NULL)
			{
				warn("Duplicate handler for item: " + itemId + "(" + template.getHandler().getClass().getSimpleName() + "," + handler.getClass().getSimpleName() + ")");
			}
			else
			{
				template.setHandler(handler);
			}
		}
	}
	
	/**
	 * Method unregisterItemHandler.
	 * @param handler IItemHandler
	 */
	public void unregisterItemHandler(IItemHandler handler)
	{
		for (int itemId : handler.getItemIds())
		{
			ItemTemplate template = ItemHolder.getInstance().getTemplate(itemId);
			template.setHandler(IItemHandler.NULL);
		}
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return 0;
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
	}
}
