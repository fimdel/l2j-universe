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
package lineage2.gameserver.tables;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.templates.item.ItemTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EnchantHPBonusTable
{
	/**
	 * Field _log.
	 */
	private static Logger _log = LoggerFactory.getLogger(EnchantHPBonusTable.class);
	/**
	 * Field _armorHPBonus.
	 */
	private final TIntObjectHashMap<Integer[]> _armorHPBonus = new TIntObjectHashMap<>();
	/**
	 * Field _onepieceFactor.
	 */
	private int _onepieceFactor = 100;
	/**
	 * Field _instance.
	 */
	private static EnchantHPBonusTable _instance = new EnchantHPBonusTable();
	
	/**
	 * Method getInstance.
	 * @return EnchantHPBonusTable
	 */
	public static EnchantHPBonusTable getInstance()
	{
		if (_instance == null)
		{
			_instance = new EnchantHPBonusTable();
		}
		return _instance;
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		_instance = new EnchantHPBonusTable();
	}
	
	/**
	 * Constructor for EnchantHPBonusTable.
	 */
	private EnchantHPBonusTable()
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			File file = new File(Config.DATAPACK_ROOT, "data/xml/other/enchant_bonus.xml");
			Document doc = factory.newDocumentBuilder().parse(file);
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n.getNodeName()))
				{
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
					{
						NamedNodeMap attrs = d.getAttributes();
						Node att;
						if ("options".equalsIgnoreCase(d.getNodeName()))
						{
							att = attrs.getNamedItem("onepiece_factor");
							if (att == null)
							{
								_log.info("EnchantHPBonusTable: Missing onepiece_factor, skipping");
								continue;
							}
							_onepieceFactor = Integer.parseInt(att.getNodeValue());
						}
						else if ("enchant_bonus".equalsIgnoreCase(d.getNodeName()))
						{
							Integer grade;
							att = attrs.getNamedItem("grade");
							if (att == null)
							{
								_log.info("EnchantHPBonusTable: Missing grade, skipping");
								continue;
							}
							grade = Integer.parseInt(att.getNodeValue());
							att = attrs.getNamedItem("values");
							if (att == null)
							{
								_log.info("EnchantHPBonusTable: Missing bonus id: " + grade + ", skipping");
								continue;
							}
							StringTokenizer st = new StringTokenizer(att.getNodeValue(), ",");
							int tokenCount = st.countTokens();
							Integer[] bonus = new Integer[tokenCount];
							for (int i = 0; i < tokenCount; i++)
							{
								Integer value = Integer.decode(st.nextToken().trim());
								if (value == null)
								{
									_log.info("EnchantHPBonusTable: Bad Hp value!! grade: " + grade + " token: " + i);
									value = 0;
								}
								bonus[i] = value;
							}
							_armorHPBonus.put(grade, bonus);
						}
					}
				}
			}
			_log.info("EnchantHPBonusTable: Loaded bonuses for " + _armorHPBonus.size() + " grades.");
		}
		catch (Exception e)
		{
			_log.warn("EnchantHPBonusTable: Lists could not be initialized.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Method getHPBonus.
	 * @param item ItemInstance
	 * @return int
	 */
	public final int getHPBonus(ItemInstance item)
	{
		final Integer[] values;
		if (item.getEnchantLevel() == 0)
		{
			return 0;
		}
		values = _armorHPBonus.get(item.getTemplate().getCrystalType().externalOrdinal);
		if ((values == null) || (values.length == 0))
		{
			return 0;
		}
		int bonus = values[Math.min(item.getEnchantLevel(), values.length) - 1];
		if (item.getTemplate().getBodyPart() == ItemTemplate.SLOT_FULL_ARMOR)
		{
			bonus = (int) ((bonus * _onepieceFactor) / 100.0D);
		}
		return bonus;
	}
}
