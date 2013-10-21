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
import lineage2.commons.geometry.Polygon;
import lineage2.gameserver.Config;
import lineage2.gameserver.instancemanager.MapRegionManager;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.templates.mapregion.DomainArea;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DomainParser extends AbstractFileParser<MapRegionManager>
{
	/**
	 * Field _instance.
	 */
	private static final DomainParser _instance = new DomainParser();
	
	/**
	 * Method getInstance.
	 * @return DomainParser
	 */
	public static DomainParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for DomainParser.
	 */
	protected DomainParser()
	{
		super(MapRegionManager.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/mapregion/domains.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "domains.dtd";
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
			Element listElement = iterator.next();
			if ("domain".equals(listElement.getName()))
			{
				int id = Integer.parseInt(listElement.attributeValue("id"));
				Territory territory = null;
				for (Iterator<Element> i = listElement.elementIterator(); i.hasNext();)
				{
					Element n = i.next();
					if ("polygon".equalsIgnoreCase(n.getName()))
					{
						Polygon shape = ZoneParser.parsePolygon(n);
						if (!shape.validate())
						{
							error("DomainParser: invalid territory data : " + shape + "!");
						}
						if (territory == null)
						{
							territory = new Territory();
						}
						territory.add(shape);
					}
				}
				if (territory == null)
				{
					throw new RuntimeException("DomainParser: empty territory!");
				}
				getHolder().addRegionData(new DomainArea(id, territory));
			}
		}
	}
}
