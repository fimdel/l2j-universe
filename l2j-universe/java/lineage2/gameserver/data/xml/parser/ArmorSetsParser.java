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

import lineage2.commons.data.xml.AbstractFileParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.ArmorSetsHolder;
import lineage2.gameserver.model.ArmorSet;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ArmorSetsParser extends AbstractFileParser<ArmorSetsHolder>
{
	/**
	 * Field _instance.
	 */
	private static final ArmorSetsParser _instance = new ArmorSetsParser();
	
	/**
	 * Method getInstance.
	 * @return ArmorSetsParser
	 */
	public static ArmorSetsParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for ArmorSetsParser.
	 */
	private ArmorSetsParser()
	{
		super(ArmorSetsHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/armor_sets.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "armor_sets.dtd";
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
			if ("set".equalsIgnoreCase(element.getName()))
			{
				String[] chests = null, legs = null, head = null, gloves = null, feet = null, shield = null, shield_skills = null, enchant6skills = null;
				if (element.attributeValue("chests") != null)
				{
					chests = element.attributeValue("chests").split(";");
				}
				if (element.attributeValue("legs") != null)
				{
					legs = element.attributeValue("legs").split(";");
				}
				if (element.attributeValue("head") != null)
				{
					head = element.attributeValue("head").split(";");
				}
				if (element.attributeValue("gloves") != null)
				{
					gloves = element.attributeValue("gloves").split(";");
				}
				if (element.attributeValue("feet") != null)
				{
					feet = element.attributeValue("feet").split(";");
				}
				if (element.attributeValue("shield") != null)
				{
					shield = element.attributeValue("shield").split(";");
				}
				if (element.attributeValue("shield_skills") != null)
				{
					shield_skills = element.attributeValue("shield_skills").split(";");
				}
				if (element.attributeValue("enchant6skills") != null)
				{
					enchant6skills = element.attributeValue("enchant6skills").split(";");
				}
				ArmorSet armorSet = new ArmorSet(chests, legs, head, gloves, feet, shield, shield_skills, enchant6skills);
				for (Iterator<Element> subIterator = element.elementIterator(); subIterator.hasNext();)
				{
					Element subElement = subIterator.next();
					if ("set_skills".equalsIgnoreCase(subElement.getName()))
					{
						int partsCount = Integer.parseInt(subElement.attributeValue("parts"));
						String[] skills = subElement.attributeValue("skills").split(";");
						armorSet.addSkills(partsCount, skills);
					}
				}
				getHolder().addArmorSet(armorSet);
			}
		}
	}
}
