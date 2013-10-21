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
package lineage2.gameserver.taskmanager;

import java.util.concurrent.ConcurrentLinkedQueue;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ItemsAutoDestroy
{
	/**
	 * Field _instance.
	 */
	private static ItemsAutoDestroy _instance;
	/**
	 * Field _items.
	 */
	ConcurrentLinkedQueue<ItemInstance> _items = null;
	/**
	 * Field _herbs.
	 */
	ConcurrentLinkedQueue<ItemInstance> _herbs = null;
	
	/**
	 * Constructor for ItemsAutoDestroy.
	 */
	private ItemsAutoDestroy()
	{
		_herbs = new ConcurrentLinkedQueue<>();
		if (Config.AUTODESTROY_ITEM_AFTER > 0)
		{
			_items = new ConcurrentLinkedQueue<>();
			ThreadPoolManager.getInstance().scheduleAtFixedRate(new CheckItemsForDestroy(), 60000, 60000);
		}
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new CheckHerbsForDestroy(), 1000, 1000);
	}
	
	/**
	 * Method getInstance.
	 * @return ItemsAutoDestroy
	 */
	public static ItemsAutoDestroy getInstance()
	{
		if (_instance == null)
		{
			_instance = new ItemsAutoDestroy();
		}
		return _instance;
	}
	
	/**
	 * Method addItem.
	 * @param item ItemInstance
	 */
	public void addItem(ItemInstance item)
	{
		item.setDropTime(System.currentTimeMillis());
		_items.add(item);
	}
	
	/**
	 * Method addHerb.
	 * @param herb ItemInstance
	 */
	public void addHerb(ItemInstance herb)
	{
		herb.setDropTime(System.currentTimeMillis());
		_herbs.add(herb);
	}
	
	/**
	 * @author Mobius
	 */
	public class CheckItemsForDestroy extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			long _sleep = Config.AUTODESTROY_ITEM_AFTER * 1000L;
			long curtime = System.currentTimeMillis();
			for (ItemInstance item : _items)
			{
				if ((item == null) || (item.getLastDropTime() == 0) || (item.getLocation() != ItemInstance.ItemLocation.VOID))
				{
					_items.remove(item);
				}
				else if ((item.getLastDropTime() + _sleep) < curtime)
				{
					item.deleteMe();
					_items.remove(item);
				}
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class CheckHerbsForDestroy extends RunnableImpl
	{
		/**
		 * Field _sleep. (value is 60000)
		 */
		static final long _sleep = 60000;
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			long curtime = System.currentTimeMillis();
			for (ItemInstance item : _herbs)
			{
				if ((item == null) || (item.getLastDropTime() == 0) || (item.getLocation() != ItemInstance.ItemLocation.VOID))
				{
					_herbs.remove(item);
				}
				else if ((item.getLastDropTime() + _sleep) < curtime)
				{
					item.deleteMe();
					_herbs.remove(item);
				}
			}
		}
	}
}
