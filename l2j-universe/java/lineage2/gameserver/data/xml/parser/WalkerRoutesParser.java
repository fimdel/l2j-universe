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
import lineage2.gameserver.data.xml.holder.WalkerRoutesHolder;
import lineage2.gameserver.templates.spawn.WalkerRouteTemplate;
import lineage2.gameserver.templates.spawn.WalkerRouteTemplate.RouteType;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class WalkerRoutesParser extends AbstractFileParser<WalkerRoutesHolder>
{
	/**
	 * Field _instance.
	 */
	private static final WalkerRoutesParser _instance = new WalkerRoutesParser();
	
	/**
	 * Method getInstance.
	 * @return WalkerRoutesParser
	 */
	public static WalkerRoutesParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for WalkerRoutesParser.
	 */
	protected WalkerRoutesParser()
	{
		super(WalkerRoutesHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/routes/walker_routes.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "walker_routes.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<Element> routeIterator = rootElement.elementIterator(); routeIterator.hasNext();)
		{
			Element routeElement = routeIterator.next();
			if (routeElement.getName().equalsIgnoreCase("route"))
			{
				int npcId = Integer.parseInt(routeElement.attributeValue("npcId"));
				RouteType type = RouteType.valueOf(routeElement.attributeValue("type"));
				long baseDelay = Long.parseLong(routeElement.attributeValue("delay"));
				boolean isRunning = Boolean.parseBoolean(routeElement.attributeValue("isRunning"));
				int walkRange = Integer.parseInt(routeElement.attributeValue("walkRange"));
				WalkerRouteTemplate template = new WalkerRouteTemplate(npcId, baseDelay, type, isRunning, walkRange);
				for (Iterator<Element> subIterator = routeElement.elementIterator(); subIterator.hasNext();)
				{
					Element subElement = subIterator.next();
					if (subElement.getName().equalsIgnoreCase("point"))
					{
						int x = Integer.parseInt(subElement.attributeValue("x"));
						int y = Integer.parseInt(subElement.attributeValue("y"));
						int z = Integer.parseInt(subElement.attributeValue("z"));
						int h = subElement.attributeValue("h") == null ? -1 : Integer.parseInt(subElement.attributeValue("h"));
						long delay = subElement.attributeValue("delay") == null ? 0 : Long.parseLong(subElement.attributeValue("delay"));
						boolean end = (subElement.attributeValue("endPoint") != null) && Boolean.parseBoolean(routeElement.attributeValue("endPoint"));
						template.setRoute(x, y, z, h, delay, end);
					}
				}
				getHolder().addSpawn(template);
			}
		}
	}
}
