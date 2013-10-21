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

import gnu.trove.map.hash.TIntObjectHashMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.time.cron.SchedulingPattern;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import lineage2.gameserver.network.serverpackets.SystemMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ItemAuctionInstance
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(ItemAuctionInstance.class);
	/**
	 * Field DATE_FORMAT.
	 */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	/**
	 * Field START_TIME_SPACE.
	 */
	private static final long START_TIME_SPACE = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
	/**
	 * Field FINISH_TIME_SPACE.
	 */
	private static final long FINISH_TIME_SPACE = TimeUnit.MILLISECONDS.convert(10, TimeUnit.MINUTES);
	/**
	 * Field _instanceId.
	 */
	private final int _instanceId;
	/**
	 * Field _auctions.
	 */
	private final TIntObjectHashMap<ItemAuction> _auctions;
	/**
	 * Field _items.
	 */
	private final List<AuctionItem> _items;
	/**
	 * Field _dateTime.
	 */
	private final SchedulingPattern _dateTime;
	/**
	 * Field _currentAuction.
	 */
	private ItemAuction _currentAuction;
	/**
	 * Field _nextAuction.
	 */
	private ItemAuction _nextAuction;
	/**
	 * Field _stateTask.
	 */
	private ScheduledFuture<?> _stateTask;
	
	/**
	 * Constructor for ItemAuctionInstance.
	 * @param instanceId int
	 * @param dateTime SchedulingPattern
	 * @param items List<AuctionItem>
	 */
	ItemAuctionInstance(int instanceId, SchedulingPattern dateTime, List<AuctionItem> items)
	{
		_instanceId = instanceId;
		_auctions = new TIntObjectHashMap<>();
		_items = items;
		_dateTime = dateTime;
		load();
		_log.info("ItemAuction: Loaded " + _items.size() + " item(s) and registered " + _auctions.size() + " auction(s) for instance " + _instanceId + ".");
		checkAndSetCurrentAndNextAuction();
	}
	
	/**
	 * Method load.
	 */
	private void load()
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT auctionId FROM item_auction WHERE instanceId=?");
			statement.setInt(1, _instanceId);
			rset = statement.executeQuery();
			while (rset.next())
			{
				int auctionId = rset.getInt(1);
				try
				{
					ItemAuction auction = loadAuction(auctionId);
					if (auction != null)
					{
						_auctions.put(auctionId, auction);
					}
					else
					{
						ItemAuctionManager.getInstance().deleteAuction(auctionId);
					}
				}
				catch (SQLException e)
				{
					_log.warn("ItemAuction: Failed loading auction: " + auctionId, e);
				}
			}
		}
		catch (SQLException e)
		{
			_log.error("ItemAuction: Failed loading auctions.", e);
			return;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
	
	/**
	 * Method getCurrentAuction.
	 * @return ItemAuction
	 */
	public ItemAuction getCurrentAuction()
	{
		return _currentAuction;
	}
	
	/**
	 * Method getNextAuction.
	 * @return ItemAuction
	 */
	public ItemAuction getNextAuction()
	{
		return _nextAuction;
	}
	
	/**
	 * Method shutdown.
	 */
	public void shutdown()
	{
		ScheduledFuture<?> stateTask = _stateTask;
		if (stateTask != null)
		{
			stateTask.cancel(false);
		}
	}
	
	/**
	 * Method getAuctionItem.
	 * @param auctionItemId int
	 * @return AuctionItem
	 */
	private AuctionItem getAuctionItem(int auctionItemId)
	{
		for (int i = _items.size(); i-- > 0;)
		{
			try
			{
				AuctionItem item = _items.get(i);
				if (item.getAuctionItemId() == auctionItemId)
				{
					return item;
				}
			}
			catch (IndexOutOfBoundsException e)
			{
			}
		}
		return null;
	}
	
	/**
	 * Method checkAndSetCurrentAndNextAuction.
	 */
	void checkAndSetCurrentAndNextAuction()
	{
		ItemAuction[] auctions = _auctions.values(new ItemAuction[_auctions.size()]);
		ItemAuction currentAuction = null;
		ItemAuction nextAuction = null;
		switch (auctions.length)
		{
			case 0:
			{
				nextAuction = createAuction(System.currentTimeMillis() + START_TIME_SPACE);
				break;
			}
			case 1:
			{
				switch (auctions[0].getAuctionState())
				{
					case CREATED:
					{
						if (auctions[0].getStartingTime() < (System.currentTimeMillis() + START_TIME_SPACE))
						{
							currentAuction = auctions[0];
							nextAuction = createAuction(System.currentTimeMillis() + START_TIME_SPACE);
						}
						else
						{
							nextAuction = auctions[0];
						}
						break;
					}
					case STARTED:
					{
						currentAuction = auctions[0];
						nextAuction = createAuction(Math.max(currentAuction.getEndingTime() + FINISH_TIME_SPACE, System.currentTimeMillis() + START_TIME_SPACE));
						break;
					}
					case FINISHED:
					{
						currentAuction = auctions[0];
						nextAuction = createAuction(System.currentTimeMillis() + START_TIME_SPACE);
						break;
					}
					default:
						throw new IllegalArgumentException();
				}
				break;
			}
			default:
			{
				Arrays.sort(auctions, new Comparator<ItemAuction>()
				{
					@Override
					public int compare(ItemAuction o1, ItemAuction o2)
					{
						if (o2.getStartingTime() > o1.getStartingTime())
						{
							return 1;
						}
						if (o2.getStartingTime() < o1.getStartingTime())
						{
							return -1;
						}
						return 0;
					}
				});
				long currentTime = System.currentTimeMillis();
				for (ItemAuction auction : auctions)
				{
					if (auction.getAuctionState() == ItemAuctionState.STARTED)
					{
						currentAuction = auction;
						break;
					}
					else if (auction.getStartingTime() <= currentTime)
					{
						currentAuction = auction;
						break;
					}
				}
				for (ItemAuction auction : auctions)
				{
					if ((auction.getStartingTime() > currentTime) && (currentAuction != auction))
					{
						nextAuction = auction;
						break;
					}
				}
				if (nextAuction == null)
				{
					nextAuction = createAuction(System.currentTimeMillis() + START_TIME_SPACE);
				}
				break;
			}
		}
		_auctions.put(nextAuction.getAuctionId(), nextAuction);
		_currentAuction = currentAuction;
		_nextAuction = nextAuction;
		if ((currentAuction != null) && (currentAuction.getAuctionState() == ItemAuctionState.STARTED))
		{
			setStateTask(ThreadPoolManager.getInstance().schedule(new ScheduleAuctionTask(currentAuction), Math.max(currentAuction.getEndingTime() - System.currentTimeMillis(), 0L)));
			_log.info("ItemAuction: Schedule current auction " + currentAuction.getAuctionId() + " for instance " + _instanceId);
		}
		else
		{
			setStateTask(ThreadPoolManager.getInstance().schedule(new ScheduleAuctionTask(nextAuction), Math.max(nextAuction.getStartingTime() - System.currentTimeMillis(), 0L)));
			_log.info("ItemAuction: Schedule next auction " + nextAuction.getAuctionId() + " on " + DATE_FORMAT.format(new Date(nextAuction.getStartingTime())) + " for instance " + _instanceId);
		}
	}
	
	/**
	 * Method getAuction.
	 * @param auctionId int
	 * @return ItemAuction
	 */
	public ItemAuction getAuction(int auctionId)
	{
		return _auctions.get(auctionId);
	}
	
	/**
	 * Method getAuctionsByBidder.
	 * @param bidderObjId int
	 * @return ItemAuction[]
	 */
	public ItemAuction[] getAuctionsByBidder(int bidderObjId)
	{
		ItemAuction[] auctions = getAuctions();
		List<ItemAuction> stack = new ArrayList<>(auctions.length);
		for (ItemAuction auction : getAuctions())
		{
			if (auction.getAuctionState() != ItemAuctionState.CREATED)
			{
				ItemAuctionBid bid = auction.getBidFor(bidderObjId);
				if (bid != null)
				{
					stack.add(auction);
				}
			}
		}
		return stack.toArray(new ItemAuction[stack.size()]);
	}
	
	/**
	 * Method getAuctions.
	 * @return ItemAuction[]
	 */
	public ItemAuction[] getAuctions()
	{
		synchronized (_auctions)
		{
			return _auctions.values(new ItemAuction[_auctions.size()]);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class ScheduleAuctionTask extends RunnableImpl
	{
		/**
		 * Field _auction.
		 */
		private final ItemAuction _auction;
		
		/**
		 * Constructor for ScheduleAuctionTask.
		 * @param auction ItemAuction
		 */
		public ScheduleAuctionTask(ItemAuction auction)
		{
			_auction = auction;
		}
		
		/**
		 * Method runImpl.
		 * @throws Exception
		 */
		@Override
		public void runImpl() throws Exception
		{
			ItemAuctionState state = _auction.getAuctionState();
			switch (state)
			{
				case CREATED:
				{
					if (!_auction.setAuctionState(state, ItemAuctionState.STARTED))
					{
						throw new IllegalStateException("Could not set auction state: " + ItemAuctionState.STARTED.toString() + ", expected: " + state.toString());
					}
					_log.info("ItemAuction: Auction " + _auction.getAuctionId() + " has started for instance " + _auction.getInstanceId());
					if (Config.ALT_ITEM_AUCTION_START_ANNOUNCE)
					{
						String[] params = {};
						Announcements.getInstance().announceByCustomMessage("lineage2.gameserver.model.instances.L2ItemAuctionBrokerInstance.announce." + _auction.getInstanceId(), params);
					}
					checkAndSetCurrentAndNextAuction();
					break;
				}
				case STARTED:
				{
					switch (_auction.getAuctionEndingExtendState())
					{
						case 1:
						{
							if (_auction.getScheduledAuctionEndingExtendState() == 0)
							{
								_auction.setScheduledAuctionEndingExtendState(1);
								setStateTask(ThreadPoolManager.getInstance().schedule(this, Math.max(_auction.getEndingTime() - System.currentTimeMillis(), 0L)));
								return;
							}
							break;
						}
						case 2:
						{
							if (_auction.getScheduledAuctionEndingExtendState() != 2)
							{
								_auction.setScheduledAuctionEndingExtendState(2);
								setStateTask(ThreadPoolManager.getInstance().schedule(this, Math.max(_auction.getEndingTime() - System.currentTimeMillis(), 0L)));
								return;
							}
							break;
						}
					}
					if (!_auction.setAuctionState(state, ItemAuctionState.FINISHED))
					{
						throw new IllegalStateException("Could not set auction state: " + ItemAuctionState.FINISHED.toString() + ", expected: " + state.toString());
					}
					onAuctionFinished(_auction);
					checkAndSetCurrentAndNextAuction();
					break;
				}
				default:
					throw new IllegalStateException("Invalid state: " + state);
			}
		}
	}
	
	/**
	 * Method onAuctionFinished.
	 * @param auction ItemAuction
	 */
	void onAuctionFinished(ItemAuction auction)
	{
		auction.broadcastToAllBidders(new SystemMessage(SystemMessage.S1_S_AUCTION_HAS_ENDED).addNumber(auction.getAuctionId()));
		ItemAuctionBid bid = auction.getHighestBid();
		if (bid != null)
		{
			ItemInstance item = auction.createNewItemInstance();
			Player player = bid.getPlayer();
			if (player != null)
			{
				player.getWarehouse().addItem(item);
				player.sendPacket(Msg.YOU_HAVE_BID_THE_HIGHEST_PRICE_AND_HAVE_WON_THE_ITEM_THE_ITEM_CAN_BE_FOUND_IN_YOUR_PERSONAL);
				_log.info("ItemAuction: Auction " + auction.getAuctionId() + " has finished. Highest bid by (name) " + player.getName() + " for instance " + _instanceId);
			}
			else
			{
				item.setOwnerId(bid.getCharId());
				item.setLocation(ItemLocation.WAREHOUSE);
				item.setJdbcState(JdbcEntityState.UPDATED);
				item.update();
				_log.info("ItemAuction: Auction " + auction.getAuctionId() + " has finished. Highest bid by (id) " + bid.getCharId() + " for instance " + _instanceId);
			}
		}
		else
		{
			_log.info("ItemAuction: Auction " + auction.getAuctionId() + " has finished. There have not been any bid for instance " + _instanceId);
		}
	}
	
	/**
	 * Method setStateTask.
	 * @param future ScheduledFuture<?>
	 */
	void setStateTask(ScheduledFuture<?> future)
	{
		ScheduledFuture<?> stateTask = _stateTask;
		if (stateTask != null)
		{
			stateTask.cancel(false);
		}
		_stateTask = future;
	}
	
	/**
	 * Method createAuction.
	 * @param after long
	 * @return ItemAuction
	 */
	private ItemAuction createAuction(long after)
	{
		AuctionItem auctionItem = _items.get(Rnd.get(_items.size()));
		long startingTime = _dateTime.next(after);
		long endingTime = startingTime + TimeUnit.MILLISECONDS.convert(auctionItem.getAuctionLength(), TimeUnit.MINUTES);
		int auctionId = ItemAuctionManager.getInstance().getNextId();
		ItemAuction auction = new ItemAuction(auctionId, _instanceId, startingTime, endingTime, auctionItem, ItemAuctionState.CREATED);
		auction.store();
		return auction;
	}
	
	/**
	 * Method loadAuction.
	 * @param auctionId int
	 * @return ItemAuction * @throws SQLException
	 */
	private ItemAuction loadAuction(int auctionId) throws SQLException
	{
		Connection con = null;
		PreparedStatement statement = null;
		ResultSet rset = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("SELECT auctionItemId,startingTime,endingTime,auctionStateId FROM item_auction WHERE auctionId=?");
			statement.setInt(1, auctionId);
			rset = statement.executeQuery();
			if (!rset.next())
			{
				_log.warn("ItemAuction: Auction data not found for auction: " + auctionId);
				return null;
			}
			int auctionItemId = rset.getInt(1);
			long startingTime = rset.getLong(2);
			long endingTime = rset.getLong(3);
			int auctionStateId = rset.getInt(4);
			DbUtils.close(statement, rset);
			if (startingTime >= endingTime)
			{
				_log.warn("ItemAuction: Invalid starting/ending paramaters for auction: " + auctionId);
				return null;
			}
			AuctionItem auctionItem = getAuctionItem(auctionItemId);
			if (auctionItem == null)
			{
				_log.warn("ItemAuction: AuctionItem: " + auctionItemId + ", not found for auction: " + auctionId);
				return null;
			}
			ItemAuctionState auctionState = ItemAuctionState.stateForStateId(auctionStateId);
			if (auctionState == null)
			{
				_log.warn("ItemAuction: Invalid auctionStateId: " + auctionStateId + ", for auction: " + auctionId);
				return null;
			}
			ItemAuction auction = new ItemAuction(auctionId, _instanceId, startingTime, endingTime, auctionItem, auctionState);
			statement = con.prepareStatement("SELECT playerObjId,playerBid FROM item_auction_bid WHERE auctionId=?");
			statement.setInt(1, auctionId);
			rset = statement.executeQuery();
			while (rset.next())
			{
				int charId = rset.getInt(1);
				long playerBid = rset.getLong(2);
				ItemAuctionBid bid = new ItemAuctionBid(charId, playerBid);
				auction.addBid(bid);
			}
			return auction;
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
	}
}
