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
package lineage2.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;

import lineage2.commons.dao.JdbcDAO;
import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dao.JdbcEntityStats;
import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemsDAO implements JdbcDAO<Integer, ItemInstance>
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ItemsDAO.class);
	private final static String RESTORE_ITEM = "SELECT object_id, owner_id, item_id, count, enchant_level, loc, loc_data, custom_type1, custom_type2, life_time, custom_flags, augmentation_id, attribute_fire, attribute_water, attribute_wind, attribute_earth, attribute_holy, attribute_unholy, agathion_energy, visual_id FROM items WHERE object_id = ?";
	private final static String RESTORE_OWNER_ITEMS = "SELECT object_id FROM items WHERE owner_id = ? AND loc = ?";
	private final static String RESTORE_ITEMS_BY_LOC = "SELECT object_id FROM items WHERE loc = ?";
	private final static String STORE_ITEM = "INSERT INTO items (object_id, owner_id, item_id, count, enchant_level, loc, loc_data, custom_type1, custom_type2, life_time, custom_flags, augmentation_id, attribute_fire, attribute_water, attribute_wind, attribute_earth, attribute_holy, attribute_unholy, agathion_energy, visual_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private final static String UPDATE_ITEM = "UPDATE items SET owner_id = ?, item_id = ?, count = ?, enchant_level = ?, loc = ?, loc_data = ?, custom_type1 = ?, custom_type2 = ?, life_time = ?, custom_flags = ?, augmentation_id = ?, attribute_fire = ?, attribute_water = ?, attribute_wind = ?, attribute_earth = ?, attribute_holy = ?, attribute_unholy = ?, agathion_energy=?, visual_id=? WHERE object_id = ?";
	private final static String REMOVE_ITEM = "DELETE FROM items WHERE object_id = ?";
	private final static ItemsDAO instance = new ItemsDAO();
	
	/**
	 * Method getInstance.
	 * @return ItemsDAO
	 */
	public final static ItemsDAO getInstance()
	{
		return instance;
	}
	
	/**
	 * Field load.
	 */
	final AtomicLong load = new AtomicLong();
	/**
	 * Field insert.
	 */
	final AtomicLong insert = new AtomicLong();
	/**
	 * Field update.
	 */
	final AtomicLong update = new AtomicLong();
	/**
	 * Field delete.
	 */
	final AtomicLong delete = new AtomicLong();
	/**
	 * Field cache.
	 */
	private final Cache cache;
	/**
	 * Field stats.
	 */
	private final JdbcEntityStats stats = new JdbcEntityStats()
	{
		@Override
		public long getLoadCount()
		{
			return load.get();
		}
		
		@Override
		public long getInsertCount()
		{
			return insert.get();
		}
		
		@Override
		public long getUpdateCount()
		{
			return update.get();
		}
		
		@Override
		public long getDeleteCount()
		{
			return delete.get();
		}
	};
	
	/**
	 * Constructor for ItemsDAO.
	 */
	private ItemsDAO()
	{
		cache = CacheManager.getInstance().getCache(ItemInstance.class.getName());
	}
	
	/**
	 * Method getCache.
	 * @return Cache
	 */
	public Cache getCache()
	{
		return cache;
	}
	
	/**
	 * Method getStats.
	 * @return JdbcEntityStats * @see lineage2.commons.dao.JdbcDAO#getStats()
	 */
	@Override
	public JdbcEntityStats getStats()
	{
		return stats;
	}
	
	/**
	 * Method load0.
	 * @param objectId int
	 * @return ItemInstance * @throws SQLException
	 */
	private ItemInstance load0(int objectId) throws SQLException
	{
		ItemInstance item = null;
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_ITEM);
			statement.setInt(1, objectId);
			rset = statement.executeQuery();
			item = load0(rset);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		load.incrementAndGet();
		return item;
	}
	
	/**
	 * Method load0.
	 * @param rset ResultSet
	 * @return ItemInstance * @throws SQLException
	 */
	private ItemInstance load0(ResultSet rset) throws SQLException
	{
		ItemInstance item = null;
		if (rset.next())
		{
			int objectId = rset.getInt(1);
			item = new ItemInstance(objectId);
			item.setOwnerId(rset.getInt(2));
			item.setItemId(rset.getInt(3));
			item.setCount(rset.getLong(4));
			item.setEnchantLevel(rset.getInt(5));
			item.setLocName(rset.getString(6));
			item.setLocData(rset.getInt(7));
			item.setCustomType1(rset.getInt(8));
			item.setCustomType2(rset.getInt(9));
			item.setLifeTime(rset.getInt(10));
			item.setCustomFlags(rset.getInt(11));
			item.setAugmentationId(rset.getInt(12));
			item.getAttributes().setFire(rset.getInt(13));
			item.getAttributes().setWater(rset.getInt(14));
			item.getAttributes().setWind(rset.getInt(15));
			item.getAttributes().setEarth(rset.getInt(16));
			item.getAttributes().setHoly(rset.getInt(17));
			item.getAttributes().setUnholy(rset.getInt(18));
			item.setAgathionEnergy(rset.getInt(19));
			item.setVisualId(rset.getInt(20));
		}
		return item;
	}
	
	/**
	 * Method save0.
	 * @param item ItemInstance
	 * @param statement PreparedStatement
	 * @throws SQLException
	 */
	private void save0(ItemInstance item, PreparedStatement statement) throws SQLException
	{
		statement.setInt(1, item.getObjectId());
		statement.setInt(2, item.getOwnerId());
		statement.setInt(3, item.getItemId());
		statement.setLong(4, item.getCount());
		statement.setInt(5, item.getEnchantLevel());
		statement.setString(6, item.getLocName());
		statement.setInt(7, item.getLocData());
		statement.setInt(8, item.getCustomType1());
		statement.setInt(9, item.getCustomType2());
		statement.setInt(10, item.getLifeTime());
		statement.setInt(11, item.getCustomFlags());
		statement.setInt(12, item.getAugmentationId());
		statement.setInt(13, item.getAttributes().getFire());
		statement.setInt(14, item.getAttributes().getWater());
		statement.setInt(15, item.getAttributes().getWind());
		statement.setInt(16, item.getAttributes().getEarth());
		statement.setInt(17, item.getAttributes().getHoly());
		statement.setInt(18, item.getAttributes().getUnholy());
		statement.setInt(19, item.getAgathionEnergy());
		statement.setInt(20, item.getVisualId());
	}
	
	/**
	 * Method save0.
	 * @param item ItemInstance
	 * @throws SQLException
	 */
	private void save0(ItemInstance item) throws SQLException
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(STORE_ITEM);
			save0(item, statement);
			statement.execute();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		insert.incrementAndGet();
	}
	
	/**
	 * Method delete0.
	 * @param item ItemInstance
	 * @param statement PreparedStatement
	 * @throws SQLException
	 */
	private void delete0(ItemInstance item, PreparedStatement statement) throws SQLException
	{
		statement.setInt(1, item.getObjectId());
	}
	
	/**
	 * Method delete0.
	 * @param item ItemInstance
	 * @throws SQLException
	 */
	private void delete0(ItemInstance item) throws SQLException
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(REMOVE_ITEM);
			delete0(item, statement);
			statement.execute();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		delete.incrementAndGet();
	}
	
	/**
	 * Method update0.
	 * @param item ItemInstance
	 * @param statement PreparedStatement
	 * @throws SQLException
	 */
	private void update0(ItemInstance item, PreparedStatement statement) throws SQLException
	{
		statement.setInt(20, item.getObjectId());
		statement.setInt(1, item.getOwnerId());
		statement.setInt(2, item.getItemId());
		statement.setLong(3, item.getCount());
		statement.setInt(4, item.getEnchantLevel());
		statement.setString(5, item.getLocName());
		statement.setInt(6, item.getLocData());
		statement.setInt(7, item.getCustomType1());
		statement.setInt(8, item.getCustomType2());
		statement.setInt(9, item.getLifeTime());
		statement.setInt(10, item.getCustomFlags());
		statement.setInt(11, item.getAugmentationId());
		statement.setInt(12, item.getAttributes().getFire());
		statement.setInt(13, item.getAttributes().getWater());
		statement.setInt(14, item.getAttributes().getWind());
		statement.setInt(15, item.getAttributes().getEarth());
		statement.setInt(16, item.getAttributes().getHoly());
		statement.setInt(17, item.getAttributes().getUnholy());
		statement.setInt(18, item.getAgathionEnergy());
		statement.setInt(19, item.getVisualId());
	}
	
	/**
	 * Method update0.
	 * @param item ItemInstance
	 * @throws SQLException
	 */
	private void update0(ItemInstance item) throws SQLException
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(UPDATE_ITEM);
			update0(item, statement);
			statement.execute();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		update.incrementAndGet();
	}
	
	/**
	 * Method load.
	 * @param objectId Integer
	 * @return ItemInstance
	 */
	@Override
	public ItemInstance load(Integer objectId)
	{
		ItemInstance item;
		Element ce = cache.get(objectId);
		if (ce != null)
		{
			item = (ItemInstance) ce.getObjectValue();
			return item;
		}
		try
		{
			item = load0(objectId);
			if (item == null)
			{
				return null;
			}
			item.setJdbcState(JdbcEntityState.STORED);
		}
		catch (SQLException e)
		{
			_log.error("Error while restoring item : " + objectId, e);
			return null;
		}
		cache.put(new Element(item.getObjectId(), item));
		return item;
	}
	
	/**
	 * Method load.
	 * @param objectIds Collection<Integer>
	 * @return Collection<ItemInstance>
	 */
	public Collection<ItemInstance> load(Collection<Integer> objectIds)
	{
		Collection<ItemInstance> list = Collections.emptyList();
		if (objectIds.isEmpty())
		{
			return list;
		}
		list = new ArrayList<>(objectIds.size());
		ItemInstance item;
		for (Integer objectId : objectIds)
		{
			item = load(objectId);
			if (item != null)
			{
				list.add(item);
			}
		}
		return list;
	}
	
	/**
	 * Method save.
	 * @param item ItemInstance
	 */
	@Override
	public void save(ItemInstance item)
	{
		if (!item.getJdbcState().isSavable())
		{
			return;
		}
		try
		{
			save0(item);
			item.setJdbcState(JdbcEntityState.STORED);
		}
		catch (SQLException e)
		{
			_log.error("Error while saving item : " + item, e);
			return;
		}
		cache.put(new Element(item.getObjectId(), item));
	}
	
	/**
	 * Method save.
	 * @param items Collection<ItemInstance>
	 */
	public void save(Collection<ItemInstance> items)
	{
		if (items.isEmpty())
		{
			return;
		}
		for (ItemInstance item : items)
		{
			save(item);
		}
	}
	
	/**
	 * Method update.
	 * @param item ItemInstance
	 */
	@Override
	public void update(ItemInstance item)
	{
		if (!item.getJdbcState().isUpdatable())
		{
			return;
		}
		try
		{
			update0(item);
			item.setJdbcState(JdbcEntityState.STORED);
		}
		catch (SQLException e)
		{
			_log.error("Error while updating item : " + item, e);
			return;
		}
		cache.putIfAbsent(new Element(item.getObjectId(), item));
	}
	
	/**
	 * Method update.
	 * @param items Collection<ItemInstance>
	 */
	public void update(Collection<ItemInstance> items)
	{
		if (items.isEmpty())
		{
			return;
		}
		for (ItemInstance item : items)
		{
			update(item);
		}
	}
	
	/**
	 * Method saveOrUpdate.
	 * @param item ItemInstance
	 */
	@Override
	public void saveOrUpdate(ItemInstance item)
	{
		if (item.getJdbcState().isSavable())
		{
			save(item);
		}
		else if (item.getJdbcState().isUpdatable())
		{
			update(item);
		}
	}
	
	/**
	 * Method saveOrUpdate.
	 * @param items Collection<ItemInstance>
	 */
	public void saveOrUpdate(Collection<ItemInstance> items)
	{
		if (items.isEmpty())
		{
			return;
		}
		for (ItemInstance item : items)
		{
			saveOrUpdate(item);
		}
	}
	
	/**
	 * Method delete.
	 * @param item ItemInstance
	 */
	@Override
	public void delete(ItemInstance item)
	{
		if (!item.getJdbcState().isDeletable())
		{
			return;
		}
		try
		{
			delete0(item);
			item.setJdbcState(JdbcEntityState.DELETED);
		}
		catch (SQLException e)
		{
			_log.error("Error while deleting item : " + item, e);
			return;
		}
		cache.remove(item.getObjectId());
	}
	
	/**
	 * Method delete.
	 * @param items Collection<ItemInstance>
	 */
	public void delete(Collection<ItemInstance> items)
	{
		if (items.isEmpty())
		{
			return;
		}
		for (ItemInstance item : items)
		{
			delete(item);
		}
	}
	
	/**
	 * Method getItemsByOwnerIdAndLoc.
	 * @param ownerId int
	 * @param loc ItemLocation
	 * @return Collection<ItemInstance>
	 */
	public Collection<ItemInstance> getItemsByOwnerIdAndLoc(int ownerId, ItemLocation loc)
	{
		Collection<Integer> objectIds = Collections.emptyList();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_OWNER_ITEMS);
			statement.setInt(1, ownerId);
			statement.setString(2, loc.name());
			rset = statement.executeQuery();
			objectIds = new ArrayList<>();
			while (rset.next())
			{
				objectIds.add(rset.getInt(1));
			}
		}
		catch (SQLException e)
		{
			_log.error("Error while restore items of owner : " + ownerId, e);
			objectIds.clear();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return load(objectIds);
	}
	
	/**
	 * Method getItemsByLoc.
	 * @param loc ItemLocation
	 * @return Collection<ItemInstance>
	 */
	public Collection<ItemInstance> getItemsByLoc(ItemLocation loc)
	{
		Collection<Integer> objectIds = Collections.emptyList();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(RESTORE_ITEMS_BY_LOC);
			statement.setString(1, loc.name());
			rset = statement.executeQuery();
			objectIds = new ArrayList<>();
			while (rset.next())
			{
				objectIds.add(rset.getInt(1));
			}
		}
		catch (SQLException e)
		{
			_log.error("Error while restore items for loc : " + loc.name(), e);
			objectIds.clear();
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		return load(objectIds);
	}
}
