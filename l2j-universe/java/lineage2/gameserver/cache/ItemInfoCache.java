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
package lineage2.gameserver.cache;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ItemInfoCache
{
	/**
	 * Field _instance.
	 */
	private final static ItemInfoCache _instance = new ItemInfoCache();
	
	/**
	 * Method getInstance.
	 * @return ItemInfoCache
	 */
	public static ItemInfoCache getInstance()
	{
		return _instance;
	}
	
	/**
	 * Field cache.
	 */
	private final Cache cache;
	
	/**
	 * Constructor for ItemInfoCache.
	 */
	private ItemInfoCache()
	{
		cache = CacheManager.getInstance().getCache(this.getClass().getName());
	}
	
	/**
	 * Method put.
	 * @param item ItemInstance
	 */
	public void put(ItemInstance item)
	{
		cache.put(new Element(item.getObjectId(), new ItemInfo(item)));
	}
	
	/**
	 * Method get.
	 * @param objectId int
	 * @return ItemInfo
	 */
	public ItemInfo get(int objectId)
	{
		Element element = cache.get(objectId);
		ItemInfo info = null;
		if (element != null)
		{
			info = (ItemInfo) element.getObjectValue();
		}
		Player player = null;
		if (info != null)
		{
			player = World.getPlayer(info.getOwnerId());
			ItemInstance item = null;
			if (player != null)
			{
				item = player.getInventory().getItemByObjectId(objectId);
			}
			if (item != null)
			{
				if (item.getItemId() == info.getItemId())
				{
					cache.put(new Element(item.getObjectId(), info = new ItemInfo(item)));
				}
			}
		}
		return info;
	}
}
