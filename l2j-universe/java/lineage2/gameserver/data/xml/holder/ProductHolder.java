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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.ProductItem;
import lineage2.gameserver.model.ProductItemComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ProductHolder
{
	/**
	 * Field _log.
	 */
	private static Logger _log = LoggerFactory.getLogger(ProductHolder.class.getName());
	/**
	 * Field _itemsList.
	 */
	TreeMap<Integer, ProductItem> _itemsList;
	/**
	 * Field _instance.
	 */
	private static ProductHolder _instance = new ProductHolder();
	
	/**
	 * Method getInstance.
	 * @return ProductHolder
	 */
	public static ProductHolder getInstance()
	{
		if (_instance == null)
		{
			_instance = new ProductHolder();
		}
		return _instance;
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		_instance = new ProductHolder();
	}
	
	/**
	 * Constructor for ProductHolder.
	 */
	private ProductHolder()
	{
		_itemsList = new TreeMap<>();
		try
		{
			File file = new File(Config.DATAPACK_ROOT, "data/xml/other/item-mall.xml");
			DocumentBuilderFactory factory1 = DocumentBuilderFactory.newInstance();
			factory1.setValidating(false);
			factory1.setIgnoringComments(true);
			Document doc1 = factory1.newDocumentBuilder().parse(file);
			for (Node n1 = doc1.getFirstChild(); n1 != null; n1 = n1.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n1.getNodeName()))
				{
					for (Node d1 = n1.getFirstChild(); d1 != null; d1 = d1.getNextSibling())
					{
						if ("product".equalsIgnoreCase(d1.getNodeName()))
						{
							Node onSaleNode = d1.getAttributes().getNamedItem("on_sale");
							Boolean onSale = (onSaleNode != null) && Boolean.parseBoolean(onSaleNode.getNodeValue());
							if (!onSale)
							{
								continue;
							}
							int productId = Integer.parseInt(d1.getAttributes().getNamedItem("id").getNodeValue());
							Node categoryNode = d1.getAttributes().getNamedItem("category");
							int category = categoryNode != null ? Integer.parseInt(categoryNode.getNodeValue()) : 5;
							Node priceNode = d1.getAttributes().getNamedItem("price");
							int price = priceNode != null ? Integer.parseInt(priceNode.getNodeValue()) : 0;
							Node isEventNode = d1.getAttributes().getNamedItem("is_event");
							Boolean isEvent = (isEventNode != null) && Boolean.parseBoolean(isEventNode.getNodeValue());
							Node isBestNode = d1.getAttributes().getNamedItem("is_best");
							Boolean isBest = (isBestNode != null) && Boolean.parseBoolean(isBestNode.getNodeValue());
							Node isNewNode = d1.getAttributes().getNamedItem("is_new");
							Boolean isNew = (isNewNode != null) && Boolean.parseBoolean(isNewNode.getNodeValue());
							int tabId = getProductTabId(isEvent, isBest, isNew);
							Node startTimeNode = d1.getAttributes().getNamedItem("sale_start_date");
							long startTimeSale = startTimeNode != null ? getMillisecondsFromString(startTimeNode.getNodeValue()) : 0;
							Node endTimeNode = d1.getAttributes().getNamedItem("sale_end_date");
							long endTimeSale = endTimeNode != null ? getMillisecondsFromString(endTimeNode.getNodeValue()) : 0;
							ArrayList<ProductItemComponent> components = new ArrayList<>();
							ProductItem pr = new ProductItem(productId, category, price, tabId, startTimeSale, endTimeSale);
							for (Node t1 = d1.getFirstChild(); t1 != null; t1 = t1.getNextSibling())
							{
								if ("component".equalsIgnoreCase(t1.getNodeName()))
								{
									int item_id = Integer.parseInt(t1.getAttributes().getNamedItem("item_id").getNodeValue());
									int count = Integer.parseInt(t1.getAttributes().getNamedItem("count").getNodeValue());
									ProductItemComponent component = new ProductItemComponent(item_id, count);
									components.add(component);
								}
							}
							pr.setComponents(components);
							_itemsList.put(productId, pr);
						}
					}
				}
			}
			_log.info(String.format("ProductItemTable: Loaded %d product item on sale.", _itemsList.size()));
		}
		catch (Exception e)
		{
			_log.warn("ProductItemTable: Lists could not be initialized.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Method getProductTabId.
	 * @param isEvent boolean
	 * @param isBest boolean
	 * @param isNew boolean
	 * @return int
	 */
	private static int getProductTabId(boolean isEvent, boolean isBest, boolean isNew)
	{
		if (isEvent && isBest)
		{
			return 3;
		}
		if (isEvent)
		{
			return 1;
		}
		if (isBest)
		{
			return 2;
		}
		return 4;
	}
	
	/**
	 * Method getMillisecondsFromString.
	 * @param datetime String
	 * @return long
	 */
	private static long getMillisecondsFromString(String datetime)
	{
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		try
		{
			Date time = df.parse(datetime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			return calendar.getTimeInMillis();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	/**
	 * Method getAllItems.
	 * @return Collection<ProductItem>
	 */
	public Collection<ProductItem> getAllItems()
	{
		return _itemsList.values();
	}
	
	/**
	 * Method getProduct.
	 * @param id int
	 * @return ProductItem
	 */
	public ProductItem getProduct(int id)
	{
		return _itemsList.get(id);
	}
}
