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
package lineage2.gameserver.data.xml.holder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.templates.item.ItemTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BuyListHolder
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(BuyListHolder.class);
	/**
	 * Field _instance.
	 */
	private static BuyListHolder _instance;
	/**
	 * Field _lists.
	 */
	private final Map<Integer, NpcTradeList> _lists;
	
	/**
	 * Method getInstance.
	 * @return BuyListHolder
	 */
	public static BuyListHolder getInstance()
	{
		if (_instance == null)
		{
			_instance = new BuyListHolder();
		}
		return _instance;
	}
	
	/**
	 * Method reload.
	 */
	public static void reload()
	{
		_instance = new BuyListHolder();
	}
	
	/**
	 * Constructor for BuyListHolder.
	 */
	private BuyListHolder()
	{
		_lists = new HashMap<>();
		try
		{
			File filelists = new File(Config.DATAPACK_ROOT, "data/xml/other/merchant_filelists.xml");
			DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
			factory1.setValidating(false);
			factory1.setIgnoringComments(true);
			Document doc1 = factory1.newDocumentBuilder().parse(filelists);
			int counterFiles = 0;
			int counterItems = 0;
			for (Node n1 = doc1.getFirstChild(); n1 != null; n1 = n1.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n1.getNodeName()))
				{
					for (Node d1 = n1.getFirstChild(); d1 != null; d1 = d1.getNextSibling())
					{
						if ("file".equalsIgnoreCase(d1.getNodeName()))
						{
							final String filename = d1.getAttributes().getNamedItem("name").getNodeValue();
							File file = new File(Config.DATAPACK_ROOT, "data/xml/other/" + filename);
							DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();
							factory2.setValidating(false);
							factory2.setIgnoringComments(true);
							Document doc2 = factory2.newDocumentBuilder().parse(file);
							counterFiles++;
							for (Node n2 = doc2.getFirstChild(); n2 != null; n2 = n2.getNextSibling())
							{
								if ("list".equalsIgnoreCase(n2.getNodeName()))
								{
									for (Node d2 = n2.getFirstChild(); d2 != null; d2 = d2.getNextSibling())
									{
										if ("tradelist".equalsIgnoreCase(d2.getNodeName()))
										{
											String[] npcs = d2.getAttributes().getNamedItem("npc").getNodeValue().split(";");
											String[] shopIds = d2.getAttributes().getNamedItem("shop").getNodeValue().split(";");
											String[] markups = new String[0];
											boolean haveMarkups = false;
											if (d2.getAttributes().getNamedItem("markup") != null)
											{
												markups = d2.getAttributes().getNamedItem("markup").getNodeValue().split(";");
												haveMarkups = true;
											}
											int size = npcs.length;
											if (!haveMarkups)
											{
												markups = new String[size];
												for (int i = 0; i < size; i++)
												{
													markups[i] = "0";
												}
											}
											if ((shopIds.length != size) || (markups.length != size))
											{
												_log.warn("Do not correspond to the size of arrays");
												continue;
											}
											for (int n = 0; n < size; n++)
											{
												final int npc_id = Integer.parseInt(npcs[n]);
												final int shop_id = Integer.parseInt(shopIds[n]);
												final double markup = npc_id > 0 ? 1. + (Double.parseDouble(markups[n]) / 100.) : 0.;
												NpcTradeList tl = new NpcTradeList(shop_id);
												tl.setNpcId(npc_id);
												for (Node i = d2.getFirstChild(); i != null; i = i.getNextSibling())
												{
													if ("item".equalsIgnoreCase(i.getNodeName()))
													{
														final int itemId = Integer.parseInt(i.getAttributes().getNamedItem("id").getNodeValue());
														final ItemTemplate template = ItemHolder.getInstance().getTemplate(itemId);
														if (template == null)
														{
															_log.warn("Template not found for itemId: " + itemId + " for shop " + shop_id);
															continue;
														}
														if (!checkItem(template))
														{
															continue;
														}
														counterItems++;
														long price = i.getAttributes().getNamedItem("price") != null ? Long.parseLong(i.getAttributes().getNamedItem("price").getNodeValue()) : Math.round(template.getReferencePrice() * markup);
														TradeItem item = new TradeItem();
														item.setItemId(itemId);
														final int itemCount = i.getAttributes().getNamedItem("count") != null ? Integer.parseInt(i.getAttributes().getNamedItem("count").getNodeValue()) : 0;
														final int itemRechargeTime = i.getAttributes().getNamedItem("time") != null ? Integer.parseInt(i.getAttributes().getNamedItem("time").getNodeValue()) : 0;
														item.setOwnersPrice(price);
														item.setCount(itemCount);
														item.setCurrentValue(itemCount);
														item.setLastRechargeTime((int) (System.currentTimeMillis() / 60000));
														item.setRechargeTime(itemRechargeTime);
														tl.addItem(item);
													}
												}
												_lists.put(shop_id, tl);
											}
										}
									}
								}
							}
						}
					}
				}
			}
			_log.info("TradeController: Loaded " + counterFiles + " file(s).");
			_log.info("TradeController: Loaded " + counterItems + " Items.");
			_log.info("TradeController: Loaded " + _lists.size() + " Buylists.");
		}
		catch (Exception e)
		{
			_log.warn("TradeController: Buylists could not be initialized.");
			_log.error("", e);
		}
	}
	
	/**
	 * Method checkItem.
	 * @param template ItemTemplate
	 * @return boolean
	 */
	public boolean checkItem(ItemTemplate template)
	{
		if (template.isCommonItem() && !Config.ALT_ALLOW_SELL_COMMON)
		{
			return false;
		}
		if (template.isEquipment() && !template.isForPet() && (Config.ALT_SHOP_PRICE_LIMITS.length > 0))
		{
			for (int i = 0; i < Config.ALT_SHOP_PRICE_LIMITS.length; i += 2)
			{
				if (template.getBodyPart() == Config.ALT_SHOP_PRICE_LIMITS[i])
				{
					if (template.getReferencePrice() > Config.ALT_SHOP_PRICE_LIMITS[i + 1])
					{
						return false;
					}
					break;
				}
			}
		}
		if (Config.ALT_SHOP_UNALLOWED_ITEMS.length > 0)
		{
			for (int i : Config.ALT_SHOP_UNALLOWED_ITEMS)
			{
				if (template.getItemId() == i)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Method getBuyList.
	 * @param listId int
	 * @return NpcTradeList
	 */
	public NpcTradeList getBuyList(int listId)
	{
		return _lists.get(listId);
	}
	
	/**
	 * Method addToBuyList.
	 * @param listId int
	 * @param list NpcTradeList
	 */
	public void addToBuyList(int listId, NpcTradeList list)
	{
		_lists.put(listId, list);
	}
	
	/**
	 * @author Mobius
	 */
	public static class NpcTradeList
	{
		/**
		 * Field tradeList.
		 */
		private final List<TradeItem> tradeList = new ArrayList<>();
		/**
		 * Field _id.
		 */
		private final int _id;
		/**
		 * Field _npcId.
		 */
		private int _npcId;
		
		/**
		 * Constructor for NpcTradeList.
		 * @param id int
		 */
		public NpcTradeList(int id)
		{
			_id = id;
		}
		
		/**
		 * Method getListId.
		 * @return int
		 */
		public int getListId()
		{
			return _id;
		}
		
		/**
		 * Method setNpcId.
		 * @param id int
		 */
		public void setNpcId(int id)
		{
			_npcId = id;
		}
		
		/**
		 * Method getNpcId.
		 * @return int
		 */
		public int getNpcId()
		{
			return _npcId;
		}
		
		/**
		 * Method addItem.
		 * @param ti TradeItem
		 */
		public void addItem(TradeItem ti)
		{
			tradeList.add(ti);
		}
		
		/**
		 * Method getItems.
		 * @return List<TradeItem>
		 */
		public synchronized List<TradeItem> getItems()
		{
			List<TradeItem> result = new ArrayList<>();
			long currentTime = System.currentTimeMillis() / 60000L;
			for (TradeItem ti : tradeList)
			{
				if (ti.isCountLimited())
				{
					if ((ti.getCurrentValue() < ti.getCount()) && ((ti.getLastRechargeTime() + ti.getRechargeTime()) <= currentTime))
					{
						ti.setLastRechargeTime(ti.getLastRechargeTime() + ti.getRechargeTime());
						ti.setCurrentValue(ti.getCount());
					}
					if (ti.getCurrentValue() == 0)
					{
						continue;
					}
				}
				result.add(ti);
			}
			return result;
		}
		
		/**
		 * Method getItemByItemId.
		 * @param itemId int
		 * @return TradeItem
		 */
		public TradeItem getItemByItemId(int itemId)
		{
			for (TradeItem ti : tradeList)
			{
				if (ti.getItemId() == itemId)
				{
					return ti;
				}
			}
			return null;
		}
		
		/**
		 * Method updateItems.
		 * @param buyList List<TradeItem>
		 */
		public synchronized void updateItems(List<TradeItem> buyList)
		{
			for (TradeItem ti : buyList)
			{
				TradeItem ic = getItemByItemId(ti.getItemId());
				if (ic.isCountLimited())
				{
					ic.setCurrentValue(Math.max(ic.getCurrentValue() - ti.getCount(), 0));
				}
			}
		}
	}
}
