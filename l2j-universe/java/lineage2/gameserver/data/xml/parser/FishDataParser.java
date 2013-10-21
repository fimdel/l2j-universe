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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.data.xml.AbstractFileParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.FishDataHolder;
import lineage2.gameserver.templates.item.support.FishGroup;
import lineage2.gameserver.templates.item.support.FishTemplate;
import lineage2.gameserver.templates.item.support.LureTemplate;
import lineage2.gameserver.templates.item.support.LureType;

import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FishDataParser extends AbstractFileParser<FishDataHolder>
{
	/**
	 * Field _instance.
	 */
	private static final FishDataParser _instance = new FishDataParser();
	
	/**
	 * Method getInstance.
	 * @return FishDataParser
	 */
	public static FishDataParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for FishDataParser.
	 */
	private FishDataParser()
	{
		super(FishDataHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/fishdata.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "fishdata.dtd";
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
			Element e = iterator.next();
			if ("fish".equals(e.getName()))
			{
				MultiValueSet<String> map = new MultiValueSet<>();
				for (Iterator<Attribute> attributeIterator = e.attributeIterator(); attributeIterator.hasNext();)
				{
					Attribute attribute = attributeIterator.next();
					map.put(attribute.getName(), attribute.getValue());
				}
				getHolder().addFish(new FishTemplate(map));
			}
			else if ("lure".equals(e.getName()))
			{
				MultiValueSet<String> map = new MultiValueSet<>();
				for (Iterator<Attribute> attributeIterator = e.attributeIterator(); attributeIterator.hasNext();)
				{
					Attribute attribute = attributeIterator.next();
					map.put(attribute.getName(), attribute.getValue());
				}
				Map<FishGroup, Integer> chances = new HashMap<>();
				for (Iterator<Element> elementIterator = e.elementIterator(); elementIterator.hasNext();)
				{
					Element chanceElement = elementIterator.next();
					chances.put(FishGroup.valueOf(chanceElement.attributeValue("type")), Integer.parseInt(chanceElement.attributeValue("value")));
				}
				map.put("chances", chances);
				getHolder().addLure(new LureTemplate(map));
			}
			else if ("distribution".equals(e.getName()))
			{
				int id = Integer.parseInt(e.attributeValue("id"));
				for (Iterator<Element> forLureIterator = e.elementIterator(); forLureIterator.hasNext();)
				{
					Element forLureElement = forLureIterator.next();
					LureType lureType = LureType.valueOf(forLureElement.attributeValue("type"));
					Map<FishGroup, Integer> chances = new HashMap<>();
					for (Iterator<Element> chanceIterator = forLureElement.elementIterator(); chanceIterator.hasNext();)
					{
						Element chanceElement = chanceIterator.next();
						chances.put(FishGroup.valueOf(chanceElement.attributeValue("type")), Integer.parseInt(chanceElement.attributeValue("value")));
					}
					getHolder().addDistribution(id, lureType, chances);
				}
			}
		}
	}
}
