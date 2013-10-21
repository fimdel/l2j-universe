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
package lineage2.gameserver.model.items.etcitems;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import javolution.util.FastList;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.items.CrystallizationItem;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.templates.item.ItemTemplate.Grade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CrystallizationManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(CrystallizationManager.class);
	/**
	 * Field data.
	 */
	private static final Map<Grade, Map<Integer, FastList<CrystallizationItem>>> data = new HashMap<>();
	
	/**
	 * Method load.
	 */
	public static void load()
	{
		_log.info("CrystallizationManager: Loading stone data...");
		int _id = 0;
		int _count = 0;
		double _chance = 0;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringComments(true);
		File file = new File(Config.DATAPACK_ROOT, "data/xml/asc/model/player/CrystallizationData.xml");
		Document doc = null;
		if (file.exists())
		{
			try
			{
				doc = factory.newDocumentBuilder().parse(file);
			}
			catch (Exception e)
			{
				_log.warn("Could not parse CrystallizationData.xml file: " + e.getMessage(), e);
				return;
			}
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if (!"list".equalsIgnoreCase(n.getNodeName()))
				{
					continue;
				}
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if (!"grade".equalsIgnoreCase(d.getNodeName()))
					{
						continue;
					}
					NamedNodeMap attrs = d.getAttributes();
					Node att = attrs.getNamedItem("type");
					Grade crystalGrade = Grade.valueOf(att.getNodeValue());
					Map<Integer, FastList<CrystallizationItem>> crystallizationData = new HashMap<>();
					for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling())
					{
						if (!"crystal".equalsIgnoreCase(c.getNodeName()))
						{
							continue;
						}
						NamedNodeMap attrs1 = c.getAttributes();
						Node att1 = attrs1.getNamedItem("count");
						int crystalCount = Integer.parseInt(att1.getNodeValue());
						FastList<CrystallizationItem> itemsData = new FastList<>();
						for (Node b = c.getFirstChild(); b != null; b = b.getNextSibling())
						{
							if (!"item".equalsIgnoreCase(b.getNodeName()))
							{
								continue;
							}
							NamedNodeMap attrs2 = b.getAttributes();
							Node att2;
							att2 = attrs2.getNamedItem("id");
							if (att2 != null)
							{
								_id = Integer.parseInt(att2.getNodeValue());
							}
							att2 = attrs2.getNamedItem("count");
							if (att2 != null)
							{
								_count = Integer.parseInt(att2.getNodeValue());
							}
							att2 = attrs2.getNamedItem("chance");
							if (att2 != null)
							{
								_chance = Double.parseDouble(att2.getNodeValue());
							}
							CrystallizationItem crHolder = new CrystallizationItem(_id, _count, _chance);
							itemsData.add(crHolder);
						}
						crystallizationData.put(crystalCount, itemsData);
					}
					data.put(crystalGrade, crystallizationData);
				}
			}
		}
		_log.info("CrystallizationManager: Loaded " + data.size() + " variable...");
	}
	
	/**
	 * Method getProductsForItem.
	 * @param item ItemInstance
	 * @return FastList<CrystallizationItem>
	 */
	public static FastList<CrystallizationItem> getProductsForItem(ItemInstance item)
	{
		Map<Integer, FastList<CrystallizationItem>> temp = data.get(item.getTemplate().getCrystalType());
		if (temp.containsKey(Integer.valueOf(item.getTemplate().getCrystalCount())))
		{
			return temp.get(Integer.valueOf(item.getTemplate().getCrystalCount()));
		}
		return null;
	}
	
	/**
	 * Method isItemExistInTable.
	 * @param item ItemInstance
	 * @return boolean
	 */
	public static boolean isItemExistInTable(ItemInstance item)
	{
		return data.get(item.getTemplate().getCrystalType()).containsKey(Integer.valueOf(item.getTemplate().getCrystalCount()));
	}
}
