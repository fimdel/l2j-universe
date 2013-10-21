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
package lineage2.gameserver.data.xml.parser;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.PlayerTemplateHolder;
import lineage2.gameserver.model.base.ClassType;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.base.Sex;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.StartItem;
import lineage2.gameserver.templates.player.BaseArmorDefence;
import lineage2.gameserver.templates.player.BaseJewelDefence;
import lineage2.gameserver.templates.player.LvlUpData;
import lineage2.gameserver.templates.player.PlayerTemplate;
import lineage2.gameserver.templates.player.StatAttributes;
import lineage2.gameserver.utils.Location;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class PlayerTemplateParser extends AbstractDirParser<PlayerTemplateHolder>
{
	/**
	 * Field _instance.
	 */
	private static final PlayerTemplateParser _instance = new PlayerTemplateParser();
	
	/**
	 * Method getInstance.
	 * @return PlayerTemplateParser
	 */
	public static PlayerTemplateParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for PlayerTemplateParser.
	 */
	private PlayerTemplateParser()
	{
		super(PlayerTemplateHolder.getInstance());
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/pc_parameters/template_data/");
	}
	
	/**
	 * Method isIgnored.
	 * @param f File
	 * @return boolean
	 */
	@Override
	public boolean isIgnored(File f)
	{
		return false;
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "template_data.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element element = iterator.next();
			Race race = Race.valueOf(element.attributeValue("race").toLowerCase());
			Sex sex = Sex.valueOf(element.attributeValue("sex").toUpperCase());
			ClassType classtype = ClassType.valueOf(element.attributeValue("type").toUpperCase());
			StatAttributes min_attr = null;
			StatAttributes max_attr = null;
			StatAttributes base_attr = null;
			BaseArmorDefence arm_defence = null;
			BaseJewelDefence jewl_defence = null;
			StatsSet stats_set = new StatsSet();
			List<StartItem> start_items = new ArrayList<>();
			List<Location> start_locations = new ArrayList<>();
			TIntObjectHashMap<LvlUpData> lvl_up_data = new TIntObjectHashMap<>();
			for (Iterator<Element> subIterator = element.elementIterator(); subIterator.hasNext();)
			{
				Element subElement = subIterator.next();
				if ("creation_data".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						if ("start_equipments".equalsIgnoreCase(e.getName()))
						{
							for (Element e2 : e.elements())
							{
								if ("equipment".equalsIgnoreCase(e2.getName()))
								{
									int item_id = Integer.parseInt(e2.attributeValue("item_id"));
									long count = Long.parseLong(e2.attributeValue("count"));
									boolean equiped = Boolean.parseBoolean(e2.attributeValue("equiped"));
									start_items.add(new StartItem(item_id, count, equiped));
								}
							}
						}
						else if ("start_points".equalsIgnoreCase(e.getName()))
						{
							for (Element e2 : e.elements())
							{
								if ("point".equalsIgnoreCase(e2.getName()))
								{
									start_locations.add(Location.parse(e2));
								}
							}
						}
					}
				}
				else if ("stats_data".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						if (("min_attributes".equalsIgnoreCase(e.getName())) || ("max_attributes".equalsIgnoreCase(e.getName())) || ("base_attributes".equalsIgnoreCase(e.getName())))
						{
							int _int = Integer.parseInt(e.attributeValue("int"));
							int str = Integer.parseInt(e.attributeValue("str"));
							int con = Integer.parseInt(e.attributeValue("con"));
							int men = Integer.parseInt(e.attributeValue("men"));
							int dex = Integer.parseInt(e.attributeValue("dex"));
							int wit = Integer.parseInt(e.attributeValue("wit"));
							StatAttributes attr = new StatAttributes(_int, str, con, men, dex, wit);
							if ("min_attributes".equalsIgnoreCase(e.getName()))
							{
								min_attr = attr;
							}
							else if ("max_attributes".equalsIgnoreCase(e.getName()))
							{
								max_attr = attr;
							}
							else if ("base_attributes".equalsIgnoreCase(e.getName()))
							{
								base_attr = attr;
							}
						}
						else if ("armor_defence".equalsIgnoreCase(e.getName()))
						{
							int chest = Integer.parseInt(e.attributeValue("chest"));
							int legs = Integer.parseInt(e.attributeValue("legs"));
							int helmet = Integer.parseInt(e.attributeValue("helmet"));
							int boots = Integer.parseInt(e.attributeValue("boots"));
							int gloves = Integer.parseInt(e.attributeValue("gloves"));
							int underwear = Integer.parseInt(e.attributeValue("underwear"));
							int cloak = Integer.parseInt(e.attributeValue("cloak"));
							arm_defence = new BaseArmorDefence(chest, legs, helmet, boots, gloves, underwear, cloak);
						}
						else if ("jewel_defence".equalsIgnoreCase(e.getName()))
						{
							int r_earring = Integer.parseInt(e.attributeValue("r_earring"));
							int l_earring = Integer.parseInt(e.attributeValue("l_earring"));
							int r_ring = Integer.parseInt(e.attributeValue("r_ring"));
							int l_ring = Integer.parseInt(e.attributeValue("l_ring"));
							int necklace = Integer.parseInt(e.attributeValue("necklace"));
							jewl_defence = new BaseJewelDefence(r_earring, l_earring, r_ring, l_ring, necklace);
						}
						else if ("base_stats".equalsIgnoreCase(e.getName()))
						{
							for (Element e2 : e.elements())
							{
								if ("stat_set".equalsIgnoreCase(e2.getName()))
								{
									stats_set.set(e2.attributeValue("name"), e2.attributeValue("value"));
								}
								else if ("regen_lvl_data".equalsIgnoreCase(e2.getName()))
								{
									for (Element e3 : e2.elements())
									{
										if ("lvl_data".equalsIgnoreCase(e3.getName()))
										{
											int lvl = Integer.parseInt(e3.attributeValue("lvl"));
											double hp = Double.parseDouble(e3.attributeValue("hp"));
											double mp = Double.parseDouble(e3.attributeValue("mp"));
											double cp = Double.parseDouble(e3.attributeValue("cp"));
											lvl_up_data.put(lvl, new LvlUpData(hp, mp, cp));
										}
									}
								}
							}
						}
					}
				}
			}
			PlayerTemplate template = new PlayerTemplate(stats_set, race, sex, min_attr, max_attr, base_attr, arm_defence, jewl_defence, start_locations, start_items, lvl_up_data);
			getHolder().addPlayerTemplate(race, classtype, sex, template);
		}
	}
}
