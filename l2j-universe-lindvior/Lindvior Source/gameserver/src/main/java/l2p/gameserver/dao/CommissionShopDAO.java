/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.dao;

import l2p.commons.dbutils.DbUtils;
import l2p.gameserver.database.DatabaseFactory;
import l2p.gameserver.instancemanager.commission.CommissionItemContainer;
import l2p.gameserver.instancemanager.commission.CommissionItemInfo;
import l2p.gameserver.instancemanager.commission.CommissionShopManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.items.ItemInstance;
import l2p.gameserver.templates.item.type.ExItemType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA. User: Darvin Date: 09.06.12 Time: 21:17
 */
public class CommissionShopDAO {
    private static final Logger _log = LoggerFactory.getLogger(CommissionShopDAO.class);

    private static final String INSERT_SQL_QUERY = "INSERT INTO commission_shop(obj_id, seller_id, item_name, price, item_type, sale_days, sale_end_time, seller_name) VALUES (?,?,?,?,?,?,?,?)";
    private static final String SELECT_PLAYER_REGISTERED_ITEMS_SQL_QUERY = "SELECT auction_id, price, item_type, sale_days, sale_end_time, seller_name, obj_id  FROM commission_shop WHERE seller_id=?";
    private static final String SELECT_REGISTERED_ITEMS = "SELECT auction_id, price, item_name, item_type, sale_days, sale_end_time, seller_name, obj_id  FROM commission_shop WHERE item_type IN(?) ORDER BY auction_id";
    private static final String SELECT_COMMISSION_ITEM_INFO = "SELECT price, sale_days, sale_end_time, seller_name, obj_id  FROM commission_shop WHERE auction_id=? AND item_type=?";
    private static final String DELETE_COMMISSION_ITEM = "DELETE FROM commission_shop WHERE auction_id=?";
    private static final String SELECT_EXPIRED_ITEMS = "SELECT auction_id, price, sale_days, seller_name, item_type, obj_id  FROM commission_shop WHERE sale_end_time <= ?";

    private static final CommissionShopDAO ourInstance = new CommissionShopDAO();

    public static CommissionShopDAO getInstance() {
        return ourInstance;
    }

    public void saveNewItem(int objectId, int seller_id, String item_name, long price, ExItemType exItemType, int sale_days, long sale_end_time, String player_name) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(INSERT_SQL_QUERY);
            statement.setInt(1, objectId);
            statement.setInt(2, seller_id);
            statement.setString(3, item_name);
            statement.setLong(4, price);
            statement.setString(5, exItemType.name());
            statement.setInt(6, sale_days);
            statement.setLong(7, sale_end_time);
            statement.setString(8, player_name);
            statement.execute();
        } catch (Exception e) {
            _log.info("CommissionShopDAO.saveNewItem: " + e, e);
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
    }

    public List<CommissionItemInfo> getRegisteredItemsFor(Player player) {
        List<CommissionItemInfo> items = new ArrayList<CommissionItemInfo>(10);
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;

        CommissionItemContainer container = CommissionShopManager.getInstance().getContainer();
        container.readLock();
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(SELECT_PLAYER_REGISTERED_ITEMS_SQL_QUERY);
            statement.setInt(1, player.getObjectId());
            rset = statement.executeQuery();
            while (rset.next()) {
                int objectId = rset.getInt("obj_id");
                ItemInstance item;
                if ((item = container.getItemByObjectId(objectId)) == null) {
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
        } catch (Exception e) {
            _log.info("CommissionShopDAO.getRegisteredItemsFor: " + e, e);
        } finally {
            container.readUnlock();
            DbUtils.closeQuietly(con, statement, rset);
        }
        return items;
    }

    public Queue<List<CommissionItemInfo>> getRegisteredItems(ExItemType[] types, int rareType, int grade, String searchName) {
        Queue<List<CommissionItemInfo>> items = new ArrayDeque<List<CommissionItemInfo>>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;

        String type = "";
        for (int i = 0; i < types.length; i++) {
            type += types[i].name();
            if ((i + 1) < types.length) {
                type += ",";
            }
        }

        CommissionItemContainer container = CommissionShopManager.getInstance().getContainer();
        container.readLock();
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(SELECT_REGISTERED_ITEMS);
            statement.setString(1, type);
            rset = statement.executeQuery();
            int i = 0;
            List<CommissionItemInfo> list = new ArrayList<CommissionItemInfo>(120);
            items.add(list);
            while (rset.next() && (i <= 999)) {
                int objectId = rset.getInt("obj_id");
                ItemInstance item;
                if ((item = container.getItemByObjectId(objectId)) == null) {
                    continue;
                }
                if (((rareType == 1) && !item.getTemplate().isBlessed()) || ((rareType == 0) && item.getTemplate().isBlessed())) {
                    continue;
                }

                if ((grade > -1) && (item.getTemplate().getItemGrade().ordinal() != grade)) {
                    continue;
                }

                if (!searchName.isEmpty() && !rset.getString("item_name").toLowerCase().contains(searchName.toLowerCase())) {
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
                if ((i != 0) && ((i % 120) == 0)) {
                    list = new ArrayList<CommissionItemInfo>(120);
                    items.add(list);
                }
                i++;
            }
        } catch (Exception e) {
            _log.info("CommissionShopDAO.getRegisteredItems: " + e, e);
        } finally {
            container.readUnlock();
            DbUtils.closeQuietly(con, statement, rset);
        }
        return items;
    }

    @SuppressWarnings("resource")
    public CommissionItemInfo getCommissionItemInfo(long auctionId, ExItemType exItemType) {
        CommissionItemInfo itemInfo = null;
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;

        CommissionItemContainer container = CommissionShopManager.getInstance().getContainer();
        container.readLock();
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(SELECT_COMMISSION_ITEM_INFO);
            statement.setLong(1, auctionId);
            statement.setString(2, exItemType.name());
            rset = statement.executeQuery();
            while (rset.next()) {
                int objectId = rset.getInt("obj_id");
                ItemInstance item;
                if ((item = container.getItemByObjectId(objectId)) == null) {
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
        } catch (Exception e) {
            _log.info("CommissionShopDAO.getCommissionItemInfo(long,int): " + e, e);
        } finally {
            container.readUnlock();
            DbUtils.closeQuietly(con, statement, rset);
        }
        return itemInfo;
    }

    public boolean removeItem(long auctionId) {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(DELETE_COMMISSION_ITEM);
            statement.setLong(1, auctionId);
        } catch (Exception e) {
            _log.info("CommissionShopDAO.removeItem(long): " + e, e);
            return false;
        } finally {
            DbUtils.closeQuietly(con, statement);
        }
        return true;
    }

    @SuppressWarnings("resource")
    public List<CommissionItemInfo> removeExpiredItems(long expireTime) {
        List<CommissionItemInfo> list = new ArrayList<CommissionItemInfo>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rset = null;

        CommissionItemContainer container = CommissionShopManager.getInstance().getContainer();
        container.writeLock();
        try {
            con = DatabaseFactory.getInstance().getConnection();
            statement = con.prepareStatement(SELECT_EXPIRED_ITEMS);
            statement.setLong(1, expireTime);
            rset = statement.executeQuery();
            while (rset.next()) {
                int objectId = rset.getInt("obj_id");
                ItemInstance item;
                if ((item = container.getItemByObjectId(objectId)) == null) {
                    return null;
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
        } catch (Exception e) {
            _log.info("CommissionShopDAO.removeExpiredItems(long): " + e, e);
        } finally {
            container.writeUnlock();
            DbUtils.closeQuietly(con, statement, rset);
        }
        return list;
    }
}
