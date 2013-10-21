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
import lineage2.gameserver.data.xml.holder.ShuttleTemplateHolder;
import lineage2.gameserver.templates.ShuttleTemplate;
import lineage2.gameserver.templates.ShuttleTemplate.ShuttleDoor;
import lineage2.gameserver.templates.StatsSet;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ShuttleTemplateParser extends AbstractFileParser<ShuttleTemplateHolder>
{
	/**
	 * Field _instance.
	 */
	private static final ShuttleTemplateParser _instance = new ShuttleTemplateParser();
	
	/**
	 * Method getInstance.
	 * @return ShuttleTemplateParser
	 */
	public static ShuttleTemplateParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for ShuttleTemplateParser.
	 */
	protected ShuttleTemplateParser()
	{
		super(ShuttleTemplateHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/shuttle_data.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "shuttle_data.dtd";
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
			Element shuttleElement = iterator.next();
			int shuttleId = Integer.parseInt(shuttleElement.attributeValue("id"));
			ShuttleTemplate template = new ShuttleTemplate(shuttleId);
			for (Iterator<?> doorsIterator = shuttleElement.elementIterator("doors"); doorsIterator.hasNext();)
			{
				Element doorsElement = (Element) doorsIterator.next();
				for (Iterator<?> doorIterator = doorsElement.elementIterator("door"); doorIterator.hasNext();)
				{
					Element doorElement = (Element) doorIterator.next();
					int doorId = Integer.parseInt(doorElement.attributeValue("id"));
					StatsSet set = new StatsSet();
					for (Iterator<?> setIterator = doorElement.elementIterator("set"); setIterator.hasNext();)
					{
						Element setElement = (Element) setIterator.next();
						set.set(setElement.attributeValue("name"), setElement.attributeValue("value"));
					}
					template.addDoor(new ShuttleDoor(doorId, set));
				}
			}
			getHolder().addTemplate(template);
		}
	}
}
