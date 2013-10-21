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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.instancemanager.commission.CommissionItemContainer;
import lineage2.gameserver.instancemanager.commission.CommissionItemInfo;
import lineage2.gameserver.instancemanager.commission.CommissionShopManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.templates.item.ExItemType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CommissionShopDAO
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CommissionShopDAO.class);
	/**
	 * Field INSERT_SQL_QUERY. (value is ""INSERT INTO commission_shop(obj_id, seller_id, item_name, price, item_type, sale_days, sale_end_time, seller_name) VALUES (?,?,?,?,?,?,?,?)"")
	 */
	private static final String INSERT_SQL_QUERY = "INSERT INTO commission_shop(obj_id, seller_id, item_name, price, item_type, sale_days, sale_end_time, seller_name) VALUES (?,?,?,?,?,?,?,?)";
	/**
	 * Field SELECT_PLAYER_REGISTERED_ITEMS_SQL_QUERY. (value is ""SELECT auction_id, price, item_type, sale_days, sale_end_time, seller_name, obj_id FROM commission_shop WHERE seller_id=?"")
	 */
	private static final String SELECT_PLAYER_REGISTERED_ITEMS_SQL_QUERY = "SELECT auction_id, price, item_type, sale_days, sale_end_time, seller_name, obj_id  FROM commission_shop WHERE seller_id=?";
	/**
	 * Field SELECT_REGISTERED_ITEMS. (value is ""SELECT auction_id, price, item_name, item_type, sale_days, sale_end_time, seller_name, obj_id FROM commission_shop WHERE item_type IN(?) ORDER BY auction_id"")
	 */
	private static final String SELECT_REGISTERED_ITEMS = "SELECT auction_id, price, item_name, item_type, sale_days, sale_end_time, seller_name, obj_id  FROM commission_shop WHERE item_type IN(?) ORDER BY auction_id";
	/**
	 * Field SELECT_COMMISSION_ITEM_INFO. (value is ""SELECT price, sale_days, sale_end_time, seller_name, obj_id FROM commission_shop WHERE auction_id=? AND item_type=?"")
	 */
	private static final String SELECT_COMMISSION_ITEM_INFO = "SELECT price, sale_days, sale_end_time, seller_name, obj_id  FROM commission_shop WHERE auction_id=? AND item_type=?";
	/**
	 * Field DELETE_COMMISSION_ITEM. (value is ""DELETE FROM commission_shop WHERE auction_id=?"")
	 */
	private static final String DELETE_COMMISSION_ITEM = "DELETE FROM commission_shop WHERE auction_id=?";
	/**
	 * Field SELECT_EXPIRED_ITEMS. (value is ""SELECT auction_id, price, sale_days, seller_name, item_type, obj_id, sale_end_time FROM commission_shop WHERE sale_end_time <= ?"")
	 */
	private static final String SELECT_EXPIRED_ITEMS = "SELECT auction_id, price, sale_days, seller_name, item_type, obj_id, sale_end_time  FROM commission_shop WHERE sale_end_time <= ?";
	/**
	 * Field ourInstance.
	 */
	private static final CommissionShopDAO ourInstance = new CommissionShopDAO();
	
	/**
	 * Method getInstance.
	 * @return CommissionShopDAO
	 */
	public static CommissionShopDAO getInstance()
	{
		return ourInstance;
	}
	
	/**
	 * Method saveNewItem.
	 * @param objectId int
	 * @param seller_id int
	 * @param item_name String
	 * @param price long
	 * @param exItemType String
	 * @param sale_days int
	 * @param sale_end_time long
	 * @param player_name String
	 */
	public void saveNewItem(int objectId, int seller_id, String item_name, long price, String exItemType, int sale_days, long sale_end_time, String player_name)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(INSERT_SQL_QUERY);
			statement.setInt(1, objectId);
			statement.setInt(2, seller_id);
			statement.setString(3, item_name);
			statement.setLong(4, price);
			statement.setString(5, exItemType);
			statement.setInt(6, sale_days);
			statement.setLong(7, sale_end_time);
			statement.setString(8, player_name);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.info("CommissionShopDAO.saveNewItem: " + e, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method getRegisteredItemsFor.
	 * @param player Player
	 * @return List<CommissionItemInfo>
	 */
	public List<CommissionItemInfo> getRegisteredItemsFor(Player player)
	{
		List<CommissionItemInfo> items = new ArrayList<>(10);
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		CommissionItemContainer container = CommissionShopManager.getInstance().getContainer();
		container.readLock();
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_PLAYER_REGISTERED_ITEMS_SQL_QUERY);
			statement.setInt(1, player.getObjectId());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int objectId = rset.getInt("obj_id");
				ItemInstance item;
				if ((item = container.getItemByObjectId(objectId)) == null)
				{
					continue;
				}
				CommissionItemInfo itemInfo = new CommissionItemInfo(item);
				itemInfo.setAuctionId(rset.getLong("auction_id"));
				itemInfo.setRegisteredPrice(rset.getLong("price"));
				itemInfo.setExItemType(ExItemType.valueOf(rset.getString("item_type")));
				itemInfo.setSaleDays(rset.getInt("sale_days"));
				itemInfo.setSaleEndTime(rset.getLong("sale_end_time"));
				itemInfo.setSellerName(rset.getString("seller_name"));
				items.add(itemInfo);
			}
		}
		catch (Exception e)
		{
			_log.info("CommissionShopDAO.getRegisteredItemsFor: " + e, e);
		}
		finally
		{
			container.readUnlock();
			DbUtils.closeQuietly(con, statement, rset);
		}
		return items;
	}
	
	/**
	 * Method getRegisteredItems.
	 * @param types ExItemType[]
	 * @param rareType int
	 * @param grade int
	 * @param searchName String
	 * @return Queue<List<CommissionItemInfo>>
	 */
	public Queue<List<CommissionItemInfo>> getRegisteredItems(ExItemType[] types, int rareType, int grade, String searchName)
	{
		Queue<List<CommissionItemInfo>> items = new ArrayDeque<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		String type = "";
		for (int i = 0; i < types.length; i++)
		{
			type += types[i].name();
			if ((i + 1) < types.length)
			{
				type += ",";
			}
		}
		CommissionItemContainer container = CommissionShopManager.getInstance().getContainer();
		container.readLock();
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_REGISTERED_ITEMS);
			statement.setString(1, type);
			rset = statement.executeQuery();
			int i = 0;
			List<CommissionItemInfo> list = new ArrayList<>(120);
			items.add(list);
			while (rset.next() && (i <= 999))
			{
				int objectId = rset.getInt("obj_id");
				ItemInstance item;
				if ((item = container.getItemByObjectId(objectId)) == null)
				{
					continue;
				}
				if (((rareType == 1) && !item.getTemplate().isBlessed()) || ((rareType == 0) && item.getTemplate().isBlessed()))
				{
					continue;
				}
				if ((grade > -1) && (item.getTemplate().getItemGrade().ordinal() != grade))
				{
					continue;
				}
				if (!searchName.isEmpty() && !rset.getString("item_name").toLowerCase().contains(searchName.toLowerCase()))
				{
					continue;
				}
				CommissionItemInfo itemInfo = new CommissionItemInfo(item);
				itemInfo.setAuctionId(rset.getLong("auction_id"));
				itemInfo.setRegisteredPrice(rset.getLong("price"));
				itemInfo.setExItemType(ExItemType.valueOf(rset.getString("item_type")));
				itemInfo.setSaleDays(rset.getInt("sale_days"));
				itemInfo.setSaleEndTime(rset.getLong("sale_end_time"));
				itemInfo.setSellerName(rset.getString("seller_name"));
				list.add(itemInfo);
				if ((i != 0) && ((i % 120) == 0))
				{
					list = new ArrayList<>(120);
					items.add(list);
				}
				i++;
			}
		}
		catch (Exception e)
		{
			_log.info("CommissionShopDAO.getRegisteredItems: " + e, e);
		}
		finally
		{
			container.readUnlock();
			DbUtils.closeQuietly(con, statement, rset);
		}
		return items;
	}
	
	/**
	 * Method getCommissionItemInfo.
	 * @param auctionId long
	 * @param exItemType ExItemType
	 * @return CommissionItemInfo
	 */
	public CommissionItemInfo getCommissionItemInfo(long auctionId, ExItemType exItemType)
	{
		CommissionItemInfo itemInfo = null;
		ResultSet rset = null;
		CommissionItemContainer container = CommissionShopManager.getInstance().getContainer();
		container.readLock();
		try (
				Connection con = DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(SELECT_COMMISSION_ITEM_INFO);
		)
		{
			statement.setLong(1, auctionId);
			statement.setString(2, exItemType.name());
			rset = statement.executeQuery();
			while (rset.next())
			{
				int objectId = rset.getInt("obj_id");
				ItemInstance item;
				if ((item = container.getItemByObjectId(objectId)) == null)
				{
					return null;
				}
				itemInfo = new CommissionItemInfo(item);
				itemInfo.setAuctionId(auctionId);
				itemInfo.setRegisteredPrice(rset.getLong("price"));
				itemInfo.setExItemType(exItemType);
				itemInfo.setSaleDays(rset.getInt("sale_days"));
				itemInfo.setSaleEndTime(rset.getLong("sale_end_time"));
				itemInfo.setSellerName(rset.getString("seller_name"));
			}
		}
		catch (Exception e)
		{
			_log.info("CommissionShopDAO.getCommissionItemInfo(long,int): " + e, e);
		}
		finally
		{
			container.readUnlock();
			DbUtils.closeQuietly(rset);
		}
		return itemInfo;
	}
	
	/**
	 * Method removeItem.
	 * @param auctionId long
	 * @return boolean
	 */
	public boolean removeItem(long auctionId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(DELETE_COMMISSION_ITEM);
			statement.setLong(1, auctionId);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.info("CommissionShopDAO.removeItem(long): " + e, e);
			return false;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
		return true;
	}
	
	/**
	 * Method removeExpiredItems.
	 * @param expireTime long
	 * @return List<CommissionItemInfo>
	 */
	public List<CommissionItemInfo> removeExpiredItems(long expireTime)
	{
		List<CommissionItemInfo> list = new ArrayList<>();
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		CommissionItemContainer container = CommissionShopManager.getInstance().getContainer();
		container.writeLock();
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(SELECT_EXPIRED_ITEMS);
			statement.setLong(1, expireTime);
			rset = statement.executeQuery();
			while (rset.next())
			{
				int objectId = rset.getInt("obj_id");
				ItemInstance item;
				if ((item = container.getItemByObjectId(objectId)) == null)
				{
					continue;
				}
				CommissionItemInfo itemInfo = new CommissionItemInfo(item);
				itemInfo.setAuctionId(rset.getLong("auction_id"));
				itemInfo.setRegisteredPrice(rset.getLong("price"));
				itemInfo.setExItemType(ExItemType.valueOf(rset.getString("item_type")));
				itemInfo.setSaleDays(rset.getInt("sale_days"));
				itemInfo.setSaleEndTime(rset.getLong("sale_end_time"));
				itemInfo.setSellerName(rset.getString("seller_name"));
				list.add(itemInfo);
			}
		}
		catch (Exception e)
		{
			_log.info("CommissionShopDAO.removeExpiredItems(long): " + e, e);
		}
		finally
		{
			container.writeUnlock();
			DbUtils.closeQuietly(con, statement, rset);
		}
		return list;
	}
}
