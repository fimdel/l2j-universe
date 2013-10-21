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
import lineage2.gameserver.data.xml.holder.StaticObjectHolder;
import lineage2.gameserver.templates.StaticObjectTemplate;
import lineage2.gameserver.templates.StatsSet;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class StaticObjectParser extends AbstractFileParser<StaticObjectHolder>
{
	/**
	 * Field _instance.
	 */
	private static StaticObjectParser _instance = new StaticObjectParser();
	
	/**
	 * Method getInstance.
	 * @return StaticObjectParser
	 */
	public static StaticObjectParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for StaticObjectParser.
	 */
	private StaticObjectParser()
	{
		super(StaticObjectHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/staticobjects.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "staticobjects.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 */
	@Override
	protected void readData(Element rootElement)
	{
		for (Iterator<?> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element staticObjectElement = (Element) iterator.next();
			StatsSet set = new StatsSet();
			set.set("uid", staticObjectElement.attributeValue("id"));
			set.set("stype", staticObjectElement.attributeValue("stype"));
			set.set("path", staticObjectElement.attributeValue("path"));
			set.set("map_x", staticObjectElement.attributeValue("map_x"));
			set.set("map_y", staticObjectElement.attributeValue("map_y"));
			set.set("name", staticObjectElement.attributeValue("name"));
			set.set("x", staticObjectElement.attributeValue("x"));
			set.set("y", staticObjectElement.attributeValue("y"));
			set.set("z", staticObjectElement.attributeValue("z"));
			set.set("spawn", staticObjectElement.attributeValue("spawn"));
			getHolder().addTemplate(new StaticObjectTemplate(set));
		}
	}
}
