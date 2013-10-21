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

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.gameserver.Config;
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
public class EnchantScrollManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(EnchantScrollManager.class);
	/**
	 * Field _scrolls.
	 */
	private static TIntObjectHashMap<EnchantScrollInfo> _scrolls = new TIntObjectHashMap<>();
	
	/**
	 * Method load.
	 */
	public static void load()
	{
		_log.info("EnchantScrollManager: Loading stone data...");
		int _itemId = 0;
		EnchantScrollType _type = null;
		EnchantScrollTarget _target = null;
		Grade _grade = null;
		int _chance = 0;
		int _min = 0;
		int _safe = 0;
		int _max = 0;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setIgnoringComments(true);
		File file = new File(Config.DATAPACK_ROOT, "data/xml/asc/model/etcitems/EnchantScroll.xml");
		Document doc = null;
		if (file.exists())
		{
			try
			{
				doc = factory.newDocumentBuilder().parse(file);
			}
			catch (Exception e)
			{
				_log.warn("Could not parse EnchantScroll.xml file: " + e.getMessage(), e);
				return;
			}
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n.getNodeName()))
				{
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						if ("item".equalsIgnoreCase(d.getNodeName()))
						{
							NamedNodeMap attrs = d.getAttributes();
							Node att;
							att = attrs.getNamedItem("id");
							if (att != null)
							{
								_itemId = Integer.parseInt(att.getNodeValue());
							}
							att = attrs.getNamedItem("type");
							if (att != null)
							{
								_type = EnchantScrollType.valueOf(att.getNodeValue());
							}
							att = attrs.getNamedItem("target");
							if (att != null)
							{
								_target = EnchantScrollTarget.valueOf(att.getNodeValue());
							}
							att = attrs.getNamedItem("grade");
							if (att != null)
							{
								_grade = Grade.valueOf(att.getNodeValue());
							}
							att = attrs.getNamedItem("chance");
							if (att != null)
							{
								_chance = Integer.parseInt(att.getNodeValue());
							}
							att = attrs.getNamedItem("min");
							if (att != null)
							{
								_min = Integer.parseInt(att.getNodeValue());
							}
							att = attrs.getNamedItem("safe");
							if (att != null)
							{
								_safe = Integer.parseInt(att.getNodeValue());
							}
							att = attrs.getNamedItem("max");
							if (att != null)
							{
								_max = Integer.parseInt(att.getNodeValue());
							}
							EnchantScrollInfo esi = new EnchantScrollInfo();
							esi.setItemId(_itemId);
							esi.setType(_type);
							esi.setTarget(_target);
							esi.setGrade(_grade);
							esi.setChance(_chance);
							esi.setMin(_min);
							esi.setSafe(_safe);
							esi.setMax(_max);
							_scrolls.put(_itemId, esi);
						}
					}
				}
			}
		}
		_log.info("EnchantScrollManager: Loaded " + _scrolls.size() + " scrolls data...");
	}
	
	/**
	 * Method getScrollInfo.
	 * @param itemId int
	 * @return EnchantScrollInfo
	 */
	public static EnchantScrollInfo getScrollInfo(int itemId)
	{
		return _scrolls.get(itemId);
	}
	
	/**
	 * Method getEnchantScrollIds.
	 * @return int[]
	 */
	public static int[] getEnchantScrollIds()
	{
		return _scrolls.keys();
	}
}
