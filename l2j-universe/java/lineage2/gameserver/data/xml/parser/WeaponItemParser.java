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

import java.io.File;
import java.util.Iterator;

import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.OptionDataHolder;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.stats.conditions.Condition;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.OptionDataTemplate;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.Bodypart;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class WeaponItemParser extends StatParser<ItemHolder>
{
	/**
	 * Field _instance.
	 */
	private static final WeaponItemParser _instance = new WeaponItemParser();
	
	/**
	 * Method getInstance.
	 * @return WeaponItemParser
	 */
	public static WeaponItemParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for WeaponItemParser.
	 */
	protected WeaponItemParser()
	{
		super(ItemHolder.getInstance());
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/stats/items/weapon/");
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
		return "item.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement org.dom4j.Element
	 * @throws Exception
	 */
	@Override
	protected void readData(org.dom4j.Element rootElement) throws Exception
	{
		for (Iterator<org.dom4j.Element> itemIterator = rootElement.elementIterator(); itemIterator.hasNext();)
		{
			org.dom4j.Element itemElement = itemIterator.next();
			StatsSet set = new StatsSet();
			set.set("item_id", itemElement.attributeValue("id"));
			set.set("name", itemElement.attributeValue("name"));
			set.set("add_name", itemElement.attributeValue("add_name", StringUtils.EMPTY));
			int slot = 0;
			for (Iterator<org.dom4j.Element> subIterator = itemElement.elementIterator(); subIterator.hasNext();)
			{
				org.dom4j.Element subElement = subIterator.next();
				String subName = subElement.getName();
				if (subName.equalsIgnoreCase("set"))
				{
					set.set(subElement.attributeValue("name"), subElement.attributeValue("value"));
				}
				else if (subName.equalsIgnoreCase("equip"))
				{
					for (Iterator<org.dom4j.Element> slotIterator = subElement.elementIterator(); slotIterator.hasNext();)
					{
						org.dom4j.Element slotElement = slotIterator.next();
						Bodypart bodypart = Bodypart.valueOf(slotElement.attributeValue("id"));
						if (bodypart.getReal() != null)
						{
							slot = bodypart.mask();
						}
						else
						{
							slot |= bodypart.mask();
						}
					}
				}
			}
			set.set("bodypart", slot);
			ItemTemplate template = null;
			try
			{
				if (itemElement.getName().equalsIgnoreCase("weapon"))
				{
					if (!set.containsKey("class"))
					{
						if ((slot & ItemTemplate.SLOT_L_HAND) > 0)
						{
							set.set("class", ItemTemplate.ItemClass.ARMOR);
						}
						else
						{
							set.set("class", ItemTemplate.ItemClass.WEAPON);
						}
					}
					template = new WeaponTemplate(set);
				}
			}
			catch (Exception e)
			{
				warn("Fail create item: " + set.get("item_id"), e);
				continue;
			}
			for (Iterator<org.dom4j.Element> subIterator = itemElement.elementIterator(); subIterator.hasNext();)
			{
				org.dom4j.Element subElement = subIterator.next();
				String subName = subElement.getName();
				if (subName.equalsIgnoreCase("for"))
				{
					parseFor(subElement, template);
				}
				else if (subName.equalsIgnoreCase("triggers"))
				{
					parseTriggers(subElement, template);
				}
				else if (subName.equalsIgnoreCase("skills"))
				{
					for (Iterator<org.dom4j.Element> nextIterator = subElement.elementIterator(); nextIterator.hasNext();)
					{
						org.dom4j.Element nextElement = nextIterator.next();
						int id = Integer.parseInt(nextElement.attributeValue("id"));
						int level = Integer.parseInt(nextElement.attributeValue("level"));
						Skill skill = SkillTable.getInstance().getInfo(id, level);
						if (skill != null)
						{
							template.attachSkill(skill);
						}
						else
						{
							info("Skill not found(" + id + "," + level + ") for item:" + set.getObject("item_id") + "; file:" + getCurrentFileName());
						}
					}
				}
				else if (subName.equalsIgnoreCase("enchant4_skill"))
				{
					int id = Integer.parseInt(subElement.attributeValue("id"));
					int level = Integer.parseInt(subElement.attributeValue("level"));
					Skill skill = SkillTable.getInstance().getInfo(id, level);
					if (skill != null)
					{
						template.setEnchant4Skill(skill);
					}
				}
				else if (subName.equalsIgnoreCase("unequip_skill"))
				{
					int id = Integer.parseInt(subElement.attributeValue("id"));
					int level = Integer.parseInt(subElement.attributeValue("level"));
					Skill skill = SkillTable.getInstance().getInfo(id, level);
					if (skill != null)
					{
						template.setUnequipeSkill(skill);
					}
				}
				else if (subName.equalsIgnoreCase("cond"))
				{
					Condition condition = parseFirstCond(subElement);
					if (condition != null)
					{
						int msgId = parseNumber(subElement.attributeValue("msgId")).intValue();
						condition.setSystemMsg(msgId);
						template.setCondition(condition);
					}
				}
				else if (subName.equalsIgnoreCase("attributes"))
				{
					int[] attributes = new int[6];
					for (Iterator<org.dom4j.Element> nextIterator = subElement.elementIterator(); nextIterator.hasNext();)
					{
						org.dom4j.Element nextElement = nextIterator.next();
						Element element;
						if (nextElement.getName().equalsIgnoreCase("attribute"))
						{
							element = Element.getElementByName(nextElement.attributeValue("element"));
							attributes[element.getId()] = Integer.parseInt(nextElement.attributeValue("value"));
						}
					}
					template.setBaseAtributeElements(attributes);
				}
				else if (subName.equalsIgnoreCase("enchant_options"))
				{
					for (Iterator<org.dom4j.Element> nextIterator = subElement.elementIterator(); nextIterator.hasNext();)
					{
						org.dom4j.Element nextElement = nextIterator.next();
						if (nextElement.getName().equalsIgnoreCase("level"))
						{
							int val = Integer.parseInt(nextElement.attributeValue("val"));
							int i = 0;
							int[] options = new int[3];
							for (org.dom4j.Element optionElement : nextElement.elements())
							{
								OptionDataTemplate optionData = OptionDataHolder.getInstance().getTemplate(Integer.parseInt(optionElement.attributeValue("id")));
								if (optionData == null)
								{
									error("Not found option_data for id: " + optionElement.attributeValue("id") + "; item_id: " + set.get("item_id"));
									continue;
								}
								options[i++] = optionData.getId();
							}
							template.addEnchantOptions(val, options);
						}
					}
				}
			}
			getHolder().addItem(template);
		}
	}
	
	/**
	 * Method getTableValue.
	 * @param name String
	 * @return Object
	 */
	@Override
	protected Object getTableValue(String name)
	{
		return null;
	}
}
