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

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.time.cron.SchedulingPattern;
import lineage2.gameserver.Config;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.templates.StatsSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ItemAuctionManager
{
	/**
	 * Field _log.
	 */
	private static Logger _log = LoggerFactory.getLogger(ItemAuctionManager.class);
	/**
	 * Field _instance.
	 */
	private static ItemAuctionManager _instance;
	
	/**
	 * Method getInstance.
	 * @return ItemAuctionManager
	 */
	public static ItemAuctionManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new ItemAuctionManager();
			if (Config.ALT_ITEM_AUCTION_ENABLED)
			{
				_instance.load();
			}
		}
		return _instance;
	}
	
	/**
	 * Field _managerInstances.
	 */
	private final TIntObjectHashMap<ItemAuctionInstance> _managerInstances = new TIntObjectHashMap<>();
	/**
	 * Field _nextId.
	 */
	private final AtomicInteger _nextId = new AtomicInteger();
	
	/**
	 * Constructor for ItemAuctionManager.
	 */
	private ItemAuctionManager()
	{
		_log.info("Initializing ItemAuctionManager");
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
			statement = con.prepareStatement("SELECT auctionId FROM item_auction ORDER BY auctionId DESC LIMIT 0, 1");
			rset = statement.executeQuery();
			if (rset.next())
			{
				_nextId.set(rset.getInt(1));
			}
		}
		catch (SQLException e)
		{
			_log.error("ItemAuctionManager: Failed loading auctions.", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement, rset);
		}
		File file = new File(Config.DATAPACK_ROOT, "data/xml/other/item_auctions.xml");
		if (!file.exists())
		{
			_log.warn("ItemAuctionManager: Missing item_auctions.xml!");
			return;
		}
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			for (Node na = doc.getFirstChild(); na != null; na = na.getNextSibling())
			{
				if ("list".equalsIgnoreCase(na.getNodeName()))
				{
					for (Node nb = na.getFirstChild(); nb != null; nb = nb.getNextSibling())
					{
						if ("instance".equalsIgnoreCase(nb.getNodeName()))
						{
							NamedNodeMap nab = nb.getAttributes();
							int instanceId = Integer.parseInt(nab.getNamedItem("id").getNodeValue());
							if (_managerInstances.containsKey(instanceId))
							{
								throw new Exception("Duplicate instanceId " + instanceId);
							}
							SchedulingPattern dateTime = new SchedulingPattern(nab.getNamedItem("schedule").getNodeValue());
							List<AuctionItem> items = new ArrayList<>();
							for (Node nc = nb.getFirstChild(); nc != null; nc = nc.getNextSibling())
							{
								if ("item".equalsIgnoreCase(nc.getNodeName()))
								{
									NamedNodeMap nac = nc.getAttributes();
									int auctionItemId = Integer.parseInt(nac.getNamedItem("auctionItemId").getNodeValue());
									int auctionLenght = Integer.parseInt(nac.getNamedItem("auctionLenght").getNodeValue());
									long auctionInitBid = Integer.parseInt(nac.getNamedItem("auctionInitBid").getNodeValue());
									int itemId = Integer.parseInt(nac.getNamedItem("itemId").getNodeValue());
									int itemCount = Integer.parseInt(nac.getNamedItem("itemCount").getNodeValue());
									if (auctionLenght < 1)
									{
										throw new IllegalArgumentException("auctionLenght < 1 for instanceId: " + instanceId + ", itemId " + itemId);
									}
									for (AuctionItem tmp : items)
									{
										if (tmp.getAuctionItemId() == auctionItemId)
										{
											throw new IllegalArgumentException("Dublicated auction item id " + auctionItemId + "for instanceId: " + instanceId);
										}
									}
									StatsSet itemExtra = new StatsSet();
									for (Node nd = nc.getFirstChild(); nd != null; nd = nd.getNextSibling())
									{
										if ("extra".equalsIgnoreCase(nd.getNodeName()))
										{
											NamedNodeMap nad = nd.getAttributes();
											for (int i = nad.getLength(); i-- > 0;)
											{
												Node n = nad.item(i);
												if (n != null)
												{
													itemExtra.set(n.getNodeName(), n.getNodeValue());
												}
											}
										}
									}
									AuctionItem item = new AuctionItem(auctionItemId, auctionLenght, auctionInitBid, itemId, itemCount, itemExtra);
									items.add(item);
								}
							}
							if (items.isEmpty())
							{
								throw new IllegalArgumentException("No items defined for instanceId: " + instanceId);
							}
							ItemAuctionInstance instance = new ItemAuctionInstance(instanceId, dateTime, items);
							_managerInstances.put(instanceId, instance);
						}
					}
				}
			}
			_log.info("ItemAuctionManager: Loaded " + _managerInstances.size() + " instance(s).");
		}
		catch (Exception e)
		{
			_log.error("ItemAuctionManager: Error while loading ItemAuctions.xml!", e);
		}
	}
	
	/**
	 * Method shutdown.
	 */
	public void shutdown()
	{
		ItemAuctionInstance[] instances = _managerInstances.values(new ItemAuctionInstance[_managerInstances.size()]);
		for (ItemAuctionInstance instance : instances)
		{
			instance.shutdown();
		}
	}
	
	/**
	 * Method getManagerInstance.
	 * @param instanceId int
	 * @return ItemAuctionInstance
	 */
	public ItemAuctionInstance getManagerInstance(int instanceId)
	{
		return _managerInstances.get(instanceId);
	}
	
	/**
	 * Method getNextId.
	 * @return int
	 */
	public int getNextId()
	{
		return _nextId.incrementAndGet();
	}
	
	/**
	 * Method deleteAuction.
	 * @param auctionId int
	 */
	public void deleteAuction(int auctionId)
	{
		Connection con = null;
		PreparedStatement statement = null;
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("DELETE FROM item_auction WHERE auctionId=?");
			statement.setInt(1, auctionId);
			statement.execute();
			statement.close();
			statement = con.prepareStatement("DELETE FROM item_auction_bid WHERE auctionId=?");
			statement.setInt(1, auctionId);
			statement.execute();
			statement.close();
		}
		catch (SQLException e)
		{
			_log.error("ItemAuctionManager: Failed deleting auction: " + auctionId, e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
}
