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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import lineage2.commons.data.xml.AbstractFileParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.AirshipDockHolder;
import lineage2.gameserver.model.entity.events.objects.BoatPoint;
import lineage2.gameserver.network.serverpackets.components.SceneMovie;
import lineage2.gameserver.templates.AirshipDock;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class AirshipDockParser extends AbstractFileParser<AirshipDockHolder>
{
	/**
	 * Field _instance.
	 */
	private static final AirshipDockParser _instance = new AirshipDockParser();
	
	/**
	 * Method getInstance.
	 * @return AirshipDockParser
	 */
	public static AirshipDockParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for AirshipDockParser.
	 */
	protected AirshipDockParser()
	{
		super(AirshipDockHolder.getInstance());
	}
	
	/**
	 * Method getXMLFile.
	 * @return File
	 */
	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/other/airship_docks.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "airship_docks.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		for (Iterator<?> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element dockElement = (Element) iterator.next();
			int id = Integer.parseInt(dockElement.attributeValue("id"));
			List<BoatPoint> teleportList = parsePoints(dockElement.element("teleportlist"));
			for (BoatPoint point : teleportList)
			{
				point.setTeleport(true);
				point.setSpeed1(-1);
				point.setSpeed2(-1);
			}
			List<AirshipDock.AirshipPlatform> platformList = new ArrayList<>(2);
			for (Iterator<?> platformIterator = dockElement.elementIterator("platform"); platformIterator.hasNext();)
			{
				Element platformElement = (Element) platformIterator.next();
				SceneMovie movie = SceneMovie.valueOf(platformElement.attributeValue("movie"));
				BoatPoint oustLoc = BoatPoint.parse(platformElement.element("oust"));
				BoatPoint spawnLoc = BoatPoint.parse(platformElement.element("spawn"));
				List<BoatPoint> arrivalList = parsePoints(platformElement.element("arrival"));
				List<BoatPoint> departList = parsePoints(platformElement.element("depart"));
				AirshipDock.AirshipPlatform platform = new AirshipDock.AirshipPlatform(movie, oustLoc, spawnLoc, arrivalList, departList);
				platformList.add(platform);
			}
			getHolder().addDock(new AirshipDock(id, teleportList, platformList));
		}
	}
	
	/**
	 * Method parsePoints.
	 * @param listElement Element
	 * @return List<BoatPoint>
	 */
	private List<BoatPoint> parsePoints(Element listElement)
	{
		if (listElement == null)
		{
			return Collections.emptyList();
		}
		List<BoatPoint> list = new ArrayList<>(5);
		for (Iterator<?> iterator = listElement.elementIterator(); iterator.hasNext();)
		{
			list.add(BoatPoint.parse((Element) iterator.next()));
		}
		return list.isEmpty() ? Collections.<BoatPoint> emptyList() : list;
	}
}
