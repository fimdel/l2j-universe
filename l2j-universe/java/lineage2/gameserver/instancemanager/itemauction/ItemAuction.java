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
package lineage2.gameserver.instancemanager.itemauction;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import lineage2.commons.dbutils.DbUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ItemAuction
{
	/**
	 * Field _log.
	 */
	private static Logger _log = LoggerFactory.getLogger(ItemAuction.class);
	/**
	 * Field ENDING_TIME_EXTEND_5.
	 */
	private static long ENDING_TIME_EXTEND_5 = TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES);
	/**
	 * Field ENDING_TIME_EXTEND_8.
	 */
	private static long ENDING_TIME_EXTEND_8 = TimeUnit.MILLISECONDS.convert(8, TimeUnit.MINUTES);
	/**
	 * Field _auctionId.
	 */
	private final int _auctionId;
	/**
	 * Field _instanceId.
	 */
	private final int _instanceId;
	/**
	 * Field _startingTime.
	 */
	private final long _startingTime;
	/**
	 * Field _endingTime.
	 */
	private final long _endingTime;
	/**
	 * Field _auctionItem.
	 */
	private final AuctionItem _auctionItem;
	/**
	 * Field _auctionBids.
	 */
	private final TIntObjectHashMap<ItemAuctionBid> _auctionBids;
	/**
	 * Field _auctionState.
	 */
	private ItemAuctionState _auctionState;
	/**
	 * Field _scheduledAuctionEndingExtendState.
	 */
	private int _scheduledAuctionEndingExtendState;
	/**
	 * Field _auctionEndingExtendState.
	 */
	private int _auctionEndingExtendState;
	/**
	 * Field _highestBid.
	 */
	private ItemAuctionBid _highestBid;
	/**
	 * Field _lastBidPlayerObjId.
	 */
	private int _lastBidPlayerObjId;
	
	/**
	 * Constructor for ItemAuction.
	 * @param auctionId int
	 * @param instanceId int
	 * @param startingTime long
	 * @param endingTime long
	 * @param auctionItem AuctionItem
	 * @param auctionState ItemAuctionState
	 */
	public ItemAuction(int auctionId, int instanceId, long startingTime, long endingTime, AuctionItem auctionItem, ItemAuctionState auctionState)
	{
		_auctionId = auctionId;
		_instanceId = instanceId;
		_startingTime = startingTime;
		_endingTime = endingTime;
		_auctionItem = auctionItem;
		_auctionBids = new TIntObjectHashMap<>();
		_auctionState = auctionState;
	}
	
	/**
	 * Method addBid.
	 * @param bid ItemAuctionBid
	 */
	void addBid(ItemAuctionBid bid)
	{
		_auctionBids.put(bid.getCharId(), bid);
		if ((_highestBid == null) || (_highestBid.getLastBid() < bid.getLastBid()))
		{
			_highestBid = bid;
		}
	}
	
	/**
	 * Method getAuctionState.
	 * @return ItemAuctionState
	 */
	public ItemAuctionState getAuctionState()
	{
		return _auctionState;
	}
	
	/**
	 * Method setAuctionState.
	 * @param expected ItemAuctionState
	 * @param wanted ItemAuctionState
	 * @return boolean
	 */
	public synchronized boolean setAuctionState(ItemAuctionState expected, ItemAuctionState wanted)
	{
		if (_auctionState != expected)
		{
			return false;
		}
		_auctionState = wanted;
		store();
		return true;
	}
	
	/**
	 * Method getAuctionId.
	 * @return int
	 */
	public int getAuctionId()
	{
		return _auctionId;
	}
	
	/**
	 * Method getInstanceId.
	 * @return int
	 */
	public int getInstanceId()
	{
		return _instanceId;
	}
	
	/**
	 * Method getAuctionItem.
	 * @return AuctionItem
	 */
	public AuctionItem getAuctionItem()
	{
		return _auctionItem;
	}
	
	/**
	 * Method createNewItemInstance.
	 * @return ItemInstance
	 */
	public ItemInstance createNewItemInstance()
	{
		return _auctionItem.createNewItemInstance();
	}
	
	/**
	 * Method getAuctionInitBid.
	 * @return long
	 */
	public long getAuctionInitBid()
	{
		return _auctionItem.getAuctionInitBid();
	}
	
	/**
	 * Method getHighestBid.
	 * @return ItemAuctionBid
	 */
	public ItemAuctionBid getHighestBid()
	{
		return _highestBid;
	}
	
	/**
	 * Method getAuctionEndingExtendState.
	 * @return int
	 */
	public int getAuctionEndingExtendState()
	{
		return _auctionEndingExtendState;
	}
	
	/**
	 * Method getScheduledAuctionEndingExtendState.
	 * @return int
	 */
	public int getScheduledAuctionEndingExtendState()
	{
		return _scheduledAuctionEndingExtendState;
	}
	
	/**
	 * Method setScheduledAuctionEndingExtendState.
	 * @param state int
	 */
	public void setScheduledAuctionEndingExtendState(int state)
	{
		_scheduledAuctionEndingExtendState = state;
	}
	
	/**
	 * Method getStartingTime.
	 * @return long
	 */
	public long getStartingTime()
	{
		return _startingTime;
	}
	
	/**
	 * Method getEndingTime.
	 * @return long
	 */
	public long getEndingTime()
	{
		if (_auctionEndingExtendState == 0)
		{
			return _endingTime;
		}
		else if (_auctionEndingExtendState == 1)
		{
			return _endingTime + ENDING_TIME_EXTEND_5;
		}
		else
		{
			return _endingTime + ENDING_TIME_EXTEND_8;
		}
	}
	
	/**
	 * Method getStartingTimeRemaining.
	 * @return long
	 */
	public long getStartingTimeRemaining()
	{
		return Math.max(getEndingTime() - System.currentTimeMillis(), 0L);
	}
	
	/**
	 * Method getFinishingTimeRemaining.
	 * @return long
	 */
	public long getFinishingTimeRemaining()
	{
		return Math.max(getEndingTime() - System.currentTimeMillis(), 0L);
	}
	
	/**
	 * Method store.
	 */
	public void store()
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("INSERT INTO item_auction (auctionId,instanceId,auctionItemId,startingTime,endingTime,auctionStateId) VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE auctionStateId=?");
			statement.setInt(1, _auctionId);
			statement.setInt(2, _instanceId);
			statement.setInt(3, _auctionItem.getAuctionItemId());
			statement.setLong(4, _startingTime);
			statement.setLong(5, _endingTime);
			statement.setInt(6, _auctionState.ordinal());
			statement.setInt(7, _auctionState.ordinal());
			statement.execute();
			statement.close();
		}
		catch (SQLException e)
		{
			_log.warn("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method getAndSetLastBidPlayerObjectId.
	 * @param playerObjId int
	 * @return int
	 */
	public int getAndSetLastBidPlayerObjectId(int playerObjId)
	{
		int lastBid = _lastBidPlayerObjId;
		_lastBidPlayerObjId = playerObjId;
		return lastBid;
	}
	
	/**
	 * Method updatePlayerBid.
	 * @param bid ItemAuctionBid
	 * @param delete boolean
	 */
	public void updatePlayerBid(ItemAuctionBid bid, boolean delete)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			if (delete)
			{
				statement = con.prepareStatement("DELETE FROM item_auction_bid WHERE auctionId=? AND playerObjId=?");
				statement.setInt(1, _auctionId);
				statement.setInt(2, bid.getCharId());
			}
			else
			{
				statement = con.prepareStatement("INSERT INTO item_auction_bid (auctionId,playerObjId,playerBid) VALUES (?,?,?) ON DUPLICATE KEY UPDATE playerBid=?");
				statement.setInt(1, _auctionId);
				statement.setInt(2, bid.getCharId());
				statement.setLong(3, bid.getLastBid());
				statement.setLong(4, bid.getLastBid());
			}
			statement.execute();
			statement.close();
		}
		catch (SQLException e)
		{
			_log.warn("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	/**
	 * Method registerBid.
	 * @param player Player
	 * @param newBid long
	 */
	public void registerBid(Player player, long newBid)
	{
		if (player == null)
		{
			throw new NullPointerException();
		}
		if (newBid < getAuctionInitBid())
		{
			player.sendPacket(Msg.YOUR_BID_PRICE_MUST_BE_HIGHER_THAN_THE_MINIMUM_PRICE_THAT_CAN_BE_BID);
			return;
		}
		if (newBid > Config.ALT_ITEM_AUCTION_MAX_BID)
		{
			if (Config.ALT_ITEM_AUCTION_MAX_BID == 100000000000L)
			{
				player.sendPacket(Msg.YOUR_BID_CANNOT_EXCEED_100_BILLION);
			}
			else
			{
				player.sendMessage("Your bid cannot exceed " + Config.ALT_ITEM_AUCTION_MAX_BID);
			}
			return;
		}
		if (getAuctionState() != ItemAuctionState.STARTED)
		{
			return;
		}
		int charId = player.getObjectId();
		synchronized (_auctionBids)
		{
			if ((_highestBid != null) && (newBid < _highestBid.getLastBid()))
			{
				player.sendPacket(Msg.YOUR_BID_MUST_BE_HIGHER_THAN_THE_CURRENT_HIGHEST_BID);
				return;
			}
			ItemAuctionBid bid = getBidFor(charId);
			if (bid == null)
			{
				if (!reduceItemCount(player, newBid))
				{
					return;
				}
				bid = new ItemAuctionBid(charId, newBid);
				_auctionBids.put(charId, bid);
				onPlayerBid(player, bid);
				updatePlayerBid(bid, false);
				player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_SUBMITTED_A_BID_IN_THE_AUCTION_OF_S1).addNumber(newBid));
				return;
			}
			if (!Config.ALT_ITEM_AUCTION_CAN_REBID)
			{
				player.sendPacket(Msg.SINCE_YOU_HAVE_ALREADY_SUBMITTED_A_BID_YOU_ARE_NOT_ALLOWED_TO_PARTICIPATE_IN_ANOTHER_AUCTION_AT_THIS_TIME);
				return;
			}
			if (bid.getLastBid() >= newBid)
			{
				player.sendPacket(Msg.THE_SECOND_BID_AMOUNT_MUST_BE_HIGHER_THAN_THE_ORIGINAL);
				return;
			}
			if (!reduceItemCount(player, newBid - bid.getLastBid()))
			{
				return;
			}
			bid.setLastBid(newBid);
			onPlayerBid(player, bid);
			updatePlayerBid(bid, false);
			player.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_SUBMITTED_A_BID_IN_THE_AUCTION_OF_S1).addNumber(newBid));
		}
	}
	
	/**
	 * Method onPlayerBid.
	 * @param player Player
	 * @param bid ItemAuctionBid
	 */
	private void onPlayerBid(Player player, ItemAuctionBid bid)
	{
		if (_highestBid == null)
		{
			_highestBid = bid;
		}
		else if (_highestBid.getLastBid() < bid.getLastBid())
		{
			Player old = _highestBid.getPlayer();
			if (old != null)
			{
				old.sendPacket(Msg.YOU_HAVE_BEEN_OUTBID);
			}
			_highestBid = bid;
		}
		if ((getEndingTime() - System.currentTimeMillis()) <= (1000 * 60 * 10))
		{
			if (_auctionEndingExtendState == 0)
			{
				_auctionEndingExtendState = 1;
				broadcastToAllBidders(Msg.BIDDER_EXISTS__THE_AUCTION_TIME_HAS_BEEN_EXTENDED_BY_5_MINUTES);
			}
			else if ((_auctionEndingExtendState == 1) && (getAndSetLastBidPlayerObjectId(player.getObjectId()) != player.getObjectId()))
			{
				_auctionEndingExtendState = 2;
				broadcastToAllBidders(Msg.BIDDER_EXISTS__AUCTION_TIME_HAS_BEEN_EXTENDED_BY_3_MINUTES);
			}
		}
	}
	
	/**
	 * Method broadcastToAllBidders.
	 * @param packet L2GameServerPacket
	 */
	public void broadcastToAllBidders(L2GameServerPacket packet)
	{
		TIntObjectIterator<ItemAuctionBid> itr = _auctionBids.iterator();
		ItemAuctionBid bid;
		while (itr.hasNext())
		{
			itr.advance();
			bid = itr.value();
			Player player = bid.getPlayer();
			if (player != null)
			{
				player.sendPacket(packet);
			}
		}
	}
	
	/**
	 * Method cancelBid.
	 * @param player Player
	 */
	public void cancelBid(Player player)
	{
		if (player == null)
		{
			throw new NullPointerException();
		}
		switch (getAuctionState())
		{
			case CREATED:
				player.sendPacket(Msg.THERE_ARE_NO_FUNDS_PRESENTLY_DUE_TO_YOU);
				return;
			case FINISHED:
				if (_startingTime < (System.currentTimeMillis() - Config.ALT_ITEM_AUCTION_MAX_CANCEL_TIME_IN_MILLIS))
				{
					player.sendPacket(Msg.THERE_ARE_NO_FUNDS_PRESENTLY_DUE_TO_YOU);
					return;
				}
				break;
			default:
				break;
		}
		int charId = player.getObjectId();
		synchronized (_auctionBids)
		{
			if (_highestBid == null)
			{
				player.sendPacket(Msg.THERE_ARE_NO_FUNDS_PRESENTLY_DUE_TO_YOU);
				return;
			}
			ItemAuctionBid bid = getBidFor(charId);
			if ((bid == null) || bid.isCanceled())
			{
				player.sendPacket(Msg.THERE_ARE_NO_FUNDS_PRESENTLY_DUE_TO_YOU);
				return;
			}
			if (bid.getCharId() == _highestBid.getCharId())
			{
				player.sendMessage("You cannot cancel you bid: You have the highest bid.");
				return;
			}
			increaseItemCount(player, bid.getLastBid());
			player.sendPacket(Msg.YOU_HAVE_CANCELED_YOUR_BID);
			if (Config.ALT_ITEM_AUCTION_CAN_REBID)
			{
				_auctionBids.remove(charId);
				updatePlayerBid(bid, true);
			}
			else
			{
				bid.cancelBid();
				updatePlayerBid(bid, false);
			}
		}
	}
	
	/**
	 * Method reduceItemCount.
	 * @param player Player
	 * @param count long
	 * @return boolean
	 */
	private boolean reduceItemCount(Player player, long count)
	{
		if (Config.ALT_ITEM_AUCTION_BID_ITEM_ID == 57)
		{
			if (!player.reduceAdena(count, true))
			{
				player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA_FOR_THIS_BID);
				return false;
			}
			return true;
		}
		return player.getInventory().destroyItemByItemId(Config.ALT_ITEM_AUCTION_BID_ITEM_ID, count);
	}
	
	/**
	 * Method increaseItemCount.
	 * @param player Player
	 * @param count long
	 */
	private void increaseItemCount(Player player, long count)
	{
		if (Config.ALT_ITEM_AUCTION_BID_ITEM_ID == 57)
		{
			player.addAdena(count);
		}
		else
		{
			player.getInventory().addItem(Config.ALT_ITEM_AUCTION_BID_ITEM_ID, count);
		}
		player.sendPacket(SystemMessage2.obtainItems(Config.ALT_ITEM_AUCTION_BID_ITEM_ID, count, 0));
	}
	
	/**
	 * Method getLastBid.
	 * @param player Player
	 * @return long
	 */
	public long getLastBid(Player player)
	{
		ItemAuctionBid bid = getBidFor(player.getObjectId());
		return bid != null ? bid.getLastBid() : -1L;
	}
	
	/**
	 * Method getBidFor.
	 * @param charId int
	 * @return ItemAuctionBid
	 */
	public ItemAuctionBid getBidFor(int charId)
	{
		return _auctionBids.get(charId);
	}
}
