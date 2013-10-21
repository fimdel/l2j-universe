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
package lineage2.gameserver.instancemanager.commission;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import lineage2.commons.math.SafeMath;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.dao.CommissionShopDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.PcInventory;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.serverpackets.ExNoticePostArrived;
import lineage2.gameserver.network.serverpackets.ExResponseCommissionBuyInfo;
import lineage2.gameserver.network.serverpackets.ExResponseCommissionBuyItem;
import lineage2.gameserver.network.serverpackets.ExResponseCommissionInfo;
import lineage2.gameserver.network.serverpackets.ExResponseCommissionItemList;
import lineage2.gameserver.network.serverpackets.ExResponseCommissionList;
import lineage2.gameserver.network.serverpackets.ExShowCommission;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.ExItemType;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CommissionShopManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CommissionShopManager.class);
	/**
	 * Field MIN_FEE. (value is 10000)
	 */
	private static final long MIN_FEE = 10000;
	/**
	 * Field REGISTRATION_FEE. (value is 0.01)
	 */
	private static final double REGISTRATION_FEE = 0.01;
	/**
	 * Field SALE_FEE. (value is 0.5)
	 */
	private static final double SALE_FEE = 0.5;
	/**
	 * Field container.
	 */
	private static final CommissionItemContainer container = new CommissionItemContainer();
	/**
	 * Field ourInstance.
	 */
	private static CommissionShopManager ourInstance = new CommissionShopManager();
	
	/**
	 * Method getInstance.
	 * @return CommissionShopManager
	 */
	public static CommissionShopManager getInstance()
	{
		return ourInstance;
	}
	
	/**
	 * Constructor for CommissionShopManager.
	 */
	private CommissionShopManager()
	{
		restore();
	}
	
	/**
	 * Method getContainer.
	 * @return CommissionItemContainer
	 */
	public CommissionItemContainer getContainer()
	{
		return container;
	}
	
	/**
	 * Method restore.
	 */
	private void restore()
	{
		container.restore();
	}
	
	/**
	 * Method showRegistrableItems.
	 * @param player Player
	 */
	public void showRegistrableItems(Player player)
	{
		ItemInstance[] items = player.getInventory().getItems();
		List<TradeItem> registrableItems = new ArrayList<>(items.length);
		for (ItemInstance item : items)
		{
			if (item.canBeSold(player))
			{
				registrableItems.add(new TradeItem(item));
			}
		}
		player.sendPacket(new ExResponseCommissionItemList(registrableItems));
	}
	
	/**
	 * Method showCommission.
	 * @param player Player
	 */
	public void showCommission(Player player)
	{
		player.sendPacket(new ExShowCommission());
	}
	
	/**
	 * Method showCommissionInfo.
	 * @param player Player
	 * @param itemObjId int
	 */
	public void showCommissionInfo(Player player, int itemObjId)
	{
		ItemInstance item = player.getInventory().getItemByObjectId(itemObjId);
		if ((item != null) && item.canBeSold(player))
		{
			player.sendPacket(new ExResponseCommissionInfo(item));
		}
		else
		{
			player.sendPacket(new ExResponseCommissionInfo(item));
		}
	}
	
	/**
	 * Method showPlayerRegisteredItems.
	 * @param player Player
	 */
	public void showPlayerRegisteredItems(Player player)
	{
		List<CommissionItemInfo> items = CommissionShopDAO.getInstance().getRegisteredItemsFor(player);
		if (items.size() == 0)
		{
			player.sendPacket(new ExResponseCommissionList(ExResponseCommissionList.EMPTY_LIST));
		}
		else
		{
			player.sendPacket(new ExResponseCommissionList(ExResponseCommissionList.PLAYER_REGISTERED_ITEMS, 0, items));
		}
	}
	
	/**
	 * Method registerItem.
	 * @param player Player
	 * @param objectId int
	 * @param item_name String
	 * @param price long
	 * @param count long
	 * @param sale_days int
	 */
	public void registerItem(Player player, int objectId, String item_name, long price, long count, int sale_days)
	{
		PcInventory inventory = player.getInventory();
		ItemInstance item = inventory.getItemByObjectId(objectId);
		if ((item == null) || (item.getCount() < count) || !item.canBeSold(player))
		{
			return;
		}
		int days = (sale_days * 2) + 1;
		if ((days <= 0) || (days > 7))
		{
			return;
		}
		inventory.writeLock();
		container.writeLock();
		try
		{
			long total = SafeMath.mulAndCheck(price, count);
			long fee = Math.round(SafeMath.mulAndCheck(total, days) * REGISTRATION_FEE);
			fee = Math.max(fee, MIN_FEE);
			if ((fee > player.getAdena()) || !player.reduceAdena(fee, false))
			{
				player.sendPacket(new SystemMessage2(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA_TO_REGISTER_THE_ITEM));
				return;
			}
			ItemInstance cItem = inventory.removeItemByObjectId(objectId, count);
			container.addItem(cItem);
			String item_type = cItem.getTemplate().getExItemType().name();
			CommissionShopDAO.getInstance().saveNewItem(cItem.getObjectId(), player.getObjectId(), item_name, price, item_type, sale_days, System.currentTimeMillis() + (days * 86400000), player.getName());
			Log.LogItem(player, Log.CommissionItemRegister, cItem);
		}
		catch (ArithmeticException ae)
		{
			_log.info("CommissionShopManager.registerItem: " + ae, ae);
		}
		finally
		{
			inventory.writeUnlock();
			container.writeUnlock();
		}
		showRegistrableItems(player);
		showPlayerRegisteredItems(player);
	}
	
	/**
	 * Method showItems.
	 * @param listType int
	 * @param category int
	 * @param rareType int
	 * @param grade int
	 * @param searchName String
	 * @param player Player
	 */
	public void showItems(int listType, int category, int rareType, int grade, String searchName, Player player)
	{
		Queue<List<CommissionItemInfo>> list = new ArrayDeque<>();
		container.readLock();
		try
		{
			ExItemType[] types;
			if (listType == 1)
			{
				types = ExItemType.getTypesForMask(category);
			}
			else if (listType == 2)
			{
				types = new ExItemType[]
				{
					ExItemType.valueOf(category)
				};
			}
			else
			{
				return;
			}
			list = CommissionShopDAO.getInstance().getRegisteredItems(types, rareType, grade, searchName);
		}
		catch (Exception e)
		{
			_log.info("CommissionShopManager.showItems: " + e, e);
		}
		finally
		{
			container.readUnlock();
		}
		if ((list.size() == 1) && list.peek().isEmpty())
		{
			player.sendPacket(new ExResponseCommissionList(ExResponseCommissionList.EMPTY_LIST));
			return;
		}
		while (list.size() > 0)
		{
			List<CommissionItemInfo> part = list.poll();
			player.sendPacket(new ExResponseCommissionList(ExResponseCommissionList.ALL_ITEMS, list.size(), part));
		}
	}
	
	/**
	 * Method showCommissionBuyInfo.
	 * @param player Player
	 * @param auctionId long
	 * @param exItemType int
	 */
	public void showCommissionBuyInfo(Player player, long auctionId, int exItemType)
	{
		if ((exItemType < 0) || (exItemType > ExItemType.values().length))
		{
			return;
		}
		CommissionItemInfo itemInfo = CommissionShopDAO.getInstance().getCommissionItemInfo(auctionId, ExItemType.values()[exItemType]);
		if (itemInfo != null)
		{
			player.sendPacket(new ExResponseCommissionBuyInfo(itemInfo));
		}
	}
	
	/**
	 * Method returnBuyItem.
	 * @param player Player
	 * @param auctionId long
	 * @param exItemType int
	 */
	public void returnBuyItem(Player player, long auctionId, int exItemType)
	{
		CommissionItemInfo itemInfo = CommissionShopDAO.getInstance().getCommissionItemInfo(auctionId, ExItemType.values()[exItemType]);
		if (itemInfo == null)
		{
			return;
		}
		PcInventory inventory = player.getInventory();
		container.writeLock();
		inventory.writeLock();
		try
		{
			if (itemInfo.getItem().getOwnerId() != player.getObjectId())
			{
				player.sendPacket(new SystemMessage2(SystemMsg.ITEM_PURCHASE_HAS_FAILED));
				return;
			}
			if (!CommissionShopDAO.getInstance().removeItem(auctionId))
			{
				return;
			}
			container.removeItem(itemInfo.getItem());
			inventory.addItem(itemInfo.getItem());
			Log.LogItem(player, Log.CommissionItemDelete, itemInfo.getItem());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			container.writeUnlock();
			inventory.writeUnlock();
		}
		showRegistrableItems(player);
		showPlayerRegisteredItems(player);
	}
	
	/**
	 * Method requestBuyItem.
	 * @param player Player
	 * @param auctionId long
	 * @param exItemType int
	 */
	public void requestBuyItem(Player player, long auctionId, int exItemType)
	{
		CommissionItemInfo itemInfo = CommissionShopDAO.getInstance().getCommissionItemInfo(auctionId, ExItemType.values()[exItemType]);
		if (itemInfo == null)
		{
			return;
		}
		PcInventory inventory = player.getInventory();
		container.writeLock();
		inventory.writeLock();
		try
		{
			if (itemInfo.getItem().getOwnerId() == player.getObjectId())
			{
				player.sendPacket(new SystemMessage2(SystemMsg.ITEM_PURCHASE_HAS_FAILED));
				player.sendPacket(ExResponseCommissionBuyItem.FAILED);
				return;
			}
			if (!inventory.validateCapacity(itemInfo.getItem()) || !inventory.validateWeight(itemInfo.getItem()))
			{
				player.sendPacket(ExResponseCommissionBuyItem.FAILED);
				return;
			}
			long price = itemInfo.getRegisteredPrice() * itemInfo.getItem().getCount();
			if (price > player.getAdena())
			{
				player.sendPacket(ExResponseCommissionBuyItem.FAILED);
				return;
			}
			if (!CommissionShopDAO.getInstance().removeItem(auctionId))
			{
				player.sendPacket(ExResponseCommissionBuyItem.FAILED);
				return;
			}
			int receiverId = itemInfo.getItem().getOwnerId();
			inventory.reduceAdena(price);
			container.removeItem(itemInfo.getItem());
			inventory.addItem(itemInfo.getItem());
			player.sendPacket(new ExResponseCommissionBuyItem(1, itemInfo.getItem().getItemId(), itemInfo.getItem().getCount()));
			long fee = (long) Math.max(1000, price * SALE_FEE);
			Mail mail = new Mail();
			mail.setSenderId(receiverId);
			mail.setSenderName(itemInfo.getSellerName());
			mail.setReceiverId(receiverId);
			mail.setReceiverName(itemInfo.getSellerName());
			mail.setTopic("CommissionBuyTitle");
			mail.setBody(itemInfo.getItem().getName());
			mail.setSystemMsg1(3490);
			mail.setSystemMsg2(3491);
			ItemInstance item = ItemFunctions.createItem(ItemTemplate.ITEM_ID_ADENA);
			item.setLocation(ItemInstance.ItemLocation.MAIL);
			item.setCount(price - fee);
			if (item.getCount() > 0)
			{
				item.save();
				mail.addAttachment(item);
			}
			mail.setType(Mail.SenderType.SYSTEM);
			mail.setUnread(true);
			mail.setReturnable(false);
			mail.setExpireTime((360 * 3600) + (int) (System.currentTimeMillis() / 1000L));
			mail.save();
			Player receiver = World.getPlayer(receiverId);
			if (receiver != null)
			{
				receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
				receiver.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
			}
			Log.LogItem(player, Log.CommissionItemSold, itemInfo.getItem());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			container.writeUnlock();
			inventory.writeUnlock();
		}
	}
	
	/**
	 * Method returnExpiredItems.
	 */
	public void returnExpiredItems()
	{
		container.writeLock();
		try
		{
			List<CommissionItemInfo> expiredItems = CommissionShopDAO.getInstance().removeExpiredItems(System.currentTimeMillis());
			for (CommissionItemInfo itemInfo : expiredItems)
			{
				Mail mail = new Mail();
				mail.setSenderId(itemInfo.getItem().getOwnerId());
				mail.setSenderName("CommissionBuyTitle");
				mail.setReceiverId(itemInfo.getItem().getOwnerId());
				mail.setReceiverName(itemInfo.getSellerName());
				mail.setTopic("CommissionBuyTitle");
				mail.setSystemMsg1(3492);
				mail.setSystemMsg2(3493);
				container.removeItem(itemInfo.getItem());
				itemInfo.getItem().setLocation(ItemInstance.ItemLocation.MAIL);
				itemInfo.getItem().save();
				mail.addAttachment(itemInfo.getItem());
				mail.setType(Mail.SenderType.SYSTEM);
				mail.setUnread(true);
				mail.setReturnable(false);
				mail.setExpireTime((360 * 3600) + (int) (System.currentTimeMillis() / 1000L));
				mail.save();
				Player receiver = World.getPlayer(itemInfo.getItem().getOwnerId());
				if (receiver != null)
				{
					receiver.sendPacket(ExNoticePostArrived.STATIC_TRUE);
					receiver.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			container.writeUnlock();
		}
	}
}
