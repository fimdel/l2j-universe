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
package lineage2.gameserver.model.items;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lineage2.commons.math.SafeMath;
import lineage2.gameserver.dao.ItemsDAO;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.utils.ItemFunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class ItemContainer
{
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(ItemContainer.class);
	/**
	 * Field _itemsDAO.
	 */
	protected static final ItemsDAO _itemsDAO = ItemsDAO.getInstance();
	/**
	 * Field _items.
	 */
	protected final List<ItemInstance> _items = new ArrayList<>();
	/**
	 * Field lock.
	 */
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();
	/**
	 * Field readLock.
	 */
	protected final Lock readLock = lock.readLock();
	/**
	 * Field writeLock.
	 */
	protected final Lock writeLock = lock.writeLock();
	
	/**
	 * Constructor for ItemContainer.
	 */
	protected ItemContainer()
	{
	}
	
	/**
	 * Method getSize.
	 * @return int
	 */
	public int getSize()
	{
		return _items.size();
	}
	
	/**
	 * Method getItems.
	 * @return ItemInstance[]
	 */
	public ItemInstance[] getItems()
	{
		readLock();
		try
		{
			return _items.toArray(new ItemInstance[_items.size()]);
		}
		finally
		{
			readUnlock();
		}
	}
	
	/**
	 * Method clear.
	 */
	public void clear()
	{
		writeLock();
		try
		{
			_items.clear();
		}
		finally
		{
			writeUnlock();
		}
	}
	
	/**
	 * Method writeLock.
	 */
	public final void writeLock()
	{
		writeLock.lock();
	}
	
	/**
	 * Method writeUnlock.
	 */
	public final void writeUnlock()
	{
		writeLock.unlock();
	}
	
	/**
	 * Method readLock.
	 */
	public final void readLock()
	{
		readLock.lock();
	}
	
	/**
	 * Method readUnlock.
	 */
	public final void readUnlock()
	{
		readLock.unlock();
	}
	
	/**
	 * Method getItemByObjectId.
	 * @param objectId int
	 * @return ItemInstance
	 */
	public ItemInstance getItemByObjectId(int objectId)
	{
		readLock();
		try
		{
			ItemInstance item;
			for (int i = 0; i < _items.size(); i++)
			{
				item = _items.get(i);
				if (item.getObjectId() == objectId)
				{
					return item;
				}
			}
		}
		finally
		{
			readUnlock();
		}
		return null;
	}
	
	/**
	 * Method getItemByItemId.
	 * @param itemId int
	 * @return ItemInstance
	 */
	public ItemInstance getItemByItemId(int itemId)
	{
		readLock();
		try
		{
			ItemInstance item;
			for (int i = 0; i < _items.size(); i++)
			{
				item = _items.get(i);
				if (item.getItemId() == itemId)
				{
					return item;
				}
			}
		}
		finally
		{
			readUnlock();
		}
		return null;
	}
	
	/**
	 * Method getItemsByItemId.
	 * @param itemId int
	 * @return List<ItemInstance>
	 */
	public List<ItemInstance> getItemsByItemId(int itemId)
	{
		List<ItemInstance> result = new ArrayList<>();
		readLock();
		try
		{
			ItemInstance item;
			for (int i = 0; i < _items.size(); i++)
			{
				item = _items.get(i);
				if (item.getItemId() == itemId)
				{
					result.add(item);
				}
			}
		}
		finally
		{
			readUnlock();
		}
		return result;
	}
	
	/**
	 * Method getCountOf.
	 * @param itemId int
	 * @return long
	 */
	public long getCountOf(int itemId)
	{
		long count = 0L;
		readLock();
		try
		{
			ItemInstance item;
			for (int i = 0; i < _items.size(); i++)
			{
				item = _items.get(i);
				if (item.getItemId() == itemId)
				{
					count = SafeMath.addAndLimit(count, item.getCount());
				}
			}
		}
		finally
		{
			readUnlock();
		}
		return count;
	}
	
	/**
	 * Method addItem.
	 * @param itemId int
	 * @param count long
	 * @return ItemInstance
	 */
	public ItemInstance addItem(int itemId, long count)
	{
		if (count < 1)
		{
			return null;
		}
		ItemInstance item;
		writeLock();
		try
		{
			item = getItemByItemId(itemId);
			if ((item != null) && item.isStackable())
			{
				synchronized (item)
				{
					item.setCount(SafeMath.addAndLimit(item.getCount(), count));
					onModifyItem(item);
				}
			}
			else
			{
				item = ItemFunctions.createItem(itemId);
				item.setCount(count);
				_items.add(item);
				onAddItem(item);
			}
		}
		finally
		{
			writeUnlock();
		}
		return item;
	}
	
	/**
	 * Method addItem.
	 * @param item ItemInstance
	 * @return ItemInstance
	 */
	public ItemInstance addItem(ItemInstance item)
	{
		if (item == null)
		{
			return null;
		}
		if (item.getCount() < 1)
		{
			return null;
		}
		ItemInstance result = null;
		writeLock();
		try
		{
			if (getItemByObjectId(item.getObjectId()) != null)
			{
				return null;
			}
			if (item.isStackable())
			{
				int itemId = item.getItemId();
				result = getItemByItemId(itemId);
				if (result != null)
				{
					synchronized (result)
					{
						result.setCount(SafeMath.addAndLimit(item.getCount(), result.getCount()));
						onModifyItem(result);
						onDestroyItem(item);
					}
				}
			}
			if (result == null)
			{
				_items.add(item);
				result = item;
				onAddItem(result);
			}
		}
		finally
		{
			writeUnlock();
		}
		return result;
	}
	
	/**
	 * Method removeItemByObjectId.
	 * @param objectId int
	 * @param count long
	 * @return ItemInstance
	 */
	public ItemInstance removeItemByObjectId(int objectId, long count)
	{
		if (count < 1)
		{
			return null;
		}
		ItemInstance result;
		writeLock();
		try
		{
			ItemInstance item;
			if ((item = getItemByObjectId(objectId)) == null)
			{
				return null;
			}
			synchronized (item)
			{
				result = removeItem(item, count);
			}
		}
		finally
		{
			writeUnlock();
		}
		return result;
	}
	
	/**
	 * Method removeItemByItemId.
	 * @param itemId int
	 * @param count long
	 * @return ItemInstance
	 */
	public ItemInstance removeItemByItemId(int itemId, long count)
	{
		if (count < 1)
		{
			return null;
		}
		ItemInstance result;
		writeLock();
		try
		{
			ItemInstance item;
			if ((item = getItemByItemId(itemId)) == null)
			{
				return null;
			}
			synchronized (item)
			{
				result = removeItem(item, count);
			}
		}
		finally
		{
			writeUnlock();
		}
		return result;
	}
	
	/**
	 * Method removeItem.
	 * @param item ItemInstance
	 * @param count long
	 * @return ItemInstance
	 */
	public ItemInstance removeItem(ItemInstance item, long count)
	{
		if (item == null)
		{
			return null;
		}
		if (count < 1)
		{
			return null;
		}
		if (item.getCount() < count)
		{
			return null;
		}
		writeLock();
		try
		{
			if (!_items.contains(item))
			{
				return null;
			}
			if (item.getCount() > count)
			{
				item.setCount(item.getCount() - count);
				onModifyItem(item);
				ItemInstance newItem = new ItemInstance(IdFactory.getInstance().getNextId(), item.getItemId());
				newItem.setCount(count);
				return newItem;
			}
			return removeItem(item);
		}
		finally
		{
			writeUnlock();
		}
	}
	
	/**
	 * Method removeItem.
	 * @param item ItemInstance
	 * @return ItemInstance
	 */
	public ItemInstance removeItem(ItemInstance item)
	{
		if (item == null)
		{
			return null;
		}
		writeLock();
		try
		{
			if (!_items.remove(item))
			{
				return null;
			}
			onRemoveItem(item);
			return item;
		}
		finally
		{
			writeUnlock();
		}
	}
	
	/**
	 * Method destroyItemByObjectId.
	 * @param objectId int
	 * @param count long
	 * @return boolean
	 */
	public boolean destroyItemByObjectId(int objectId, long count)
	{
		writeLock();
		try
		{
			ItemInstance item;
			if ((item = getItemByObjectId(objectId)) == null)
			{
				return false;
			}
			synchronized (item)
			{
				return destroyItem(item, count);
			}
		}
		finally
		{
			writeUnlock();
		}
	}
	
	/**
	 * Method destroyItemByItemId.
	 * @param itemId int
	 * @param count long
	 * @return boolean
	 */
	public boolean destroyItemByItemId(int itemId, long count)
	{
		writeLock();
		try
		{
			ItemInstance item;
			if ((item = getItemByItemId(itemId)) == null)
			{
				return false;
			}
			synchronized (item)
			{
				return destroyItem(item, count);
			}
		}
		finally
		{
			writeUnlock();
		}
	}
	
	/**
	 * Method destroyItem.
	 * @param item ItemInstance
	 * @param count long
	 * @return boolean
	 */
	public boolean destroyItem(ItemInstance item, long count)
	{
		if (item == null)
		{
			return false;
		}
		if (count < 1)
		{
			return false;
		}
		if (item.getCount() < count)
		{
			return false;
		}
		writeLock();
		try
		{
			if (!_items.contains(item))
			{
				return false;
			}
			if (item.getCount() > count)
			{
				item.setCount(item.getCount() - count);
				onModifyItem(item);
				return true;
			}
			return destroyItem(item);
		}
		finally
		{
			writeUnlock();
		}
	}
	
	/**
	 * Method destroyItem.
	 * @param item ItemInstance
	 * @return boolean
	 */
	public boolean destroyItem(ItemInstance item)
	{
		if (item == null)
		{
			return false;
		}
		writeLock();
		try
		{
			if (!_items.remove(item))
			{
				return false;
			}
			onRemoveItem(item);
			onDestroyItem(item);
			return true;
		}
		finally
		{
			writeUnlock();
		}
	}
	
	/**
	 * Method onAddItem.
	 * @param item ItemInstance
	 */
	protected abstract void onAddItem(ItemInstance item);
	
	/**
	 * Method onModifyItem.
	 * @param item ItemInstance
	 */
	protected abstract void onModifyItem(ItemInstance item);
	
	/**
	 * Method onRemoveItem.
	 * @param item ItemInstance
	 */
	protected abstract void onRemoveItem(ItemInstance item);
	
	/**
	 * Method onDestroyItem.
	 * @param item ItemInstance
	 */
	protected abstract void onDestroyItem(ItemInstance item);
}
