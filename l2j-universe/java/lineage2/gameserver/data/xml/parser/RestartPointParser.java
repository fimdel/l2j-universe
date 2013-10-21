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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lineage2.commons.data.xml.AbstractFileParser;
import lineage2.commons.geometry.Polygon;
import lineage2.commons.geometry.Rectangle;
import lineage2.gameserver.Config;
import lineage2.gameserver.instancemanager.MapRegionManager;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.templates.mapregion.RestartArea;
import lineage2.gameserver.templates.mapregion.RestartPoint;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.dom4j.Attribute;
import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RestartPointParser extends AbstractFileParser<MapRegionManager>
{
	/**
	 * Field _instance.
	 */
	private static final RestartPointParser _instance = new RestartPointParser();
	
	/**
	 * Method getInstance.
	 * @return RestartPointParser
	 */
	public static RestartPointParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for RestartPointParser.
	 */
	private RestartPointParser()
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
		return new File(Config.DATAPACK_ROOT, "data/xml/mapregion/restart_points.xml");
	}
	
	/**
	 * Method getDTDFileName.
	 * @return String
	 */
	@Override
	public String getDTDFileName()
	{
		return "restart_points.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		List<Pair<Territory, Map<Race, String>>> restartArea = new ArrayList<>();
		Map<String, RestartPoint> restartPoint = new HashMap<>();
		for (Iterator<Element> iterator = rootElement.elementIterator(); iterator.hasNext();)
		{
			Element listElement = iterator.next();
			if ("restart_area".equals(listElement.getName()))
			{
				Territory territory = null;
				Map<Race, String> restarts = new HashMap<>();
				for (Iterator<Element> i = listElement.elementIterator(); i.hasNext();)
				{
					Element n = i.next();
					if ("region".equalsIgnoreCase(n.getName()))
					{
						Rectangle shape;
						Attribute map = n.attribute("map");
						String s = map.getValue();
						String val[] = s.split("_");
						int rx = Integer.parseInt(val[0]);
						int ry = Integer.parseInt(val[1]);
						int x1 = World.MAP_MIN_X + ((rx - Config.GEO_X_FIRST) << 15);
						int y1 = World.MAP_MIN_Y + ((ry - Config.GEO_Y_FIRST) << 15);
						int x2 = (x1 + (1 << 15)) - 1;
						int y2 = (y1 + (1 << 15)) - 1;
						shape = new Rectangle(x1, y1, x2, y2);
						shape.setZmin(World.MAP_MIN_Z);
						shape.setZmax(World.MAP_MAX_Z);
						if (territory == null)
						{
							territory = new Territory();
						}
						territory.add(shape);
					}
					else if ("polygon".equalsIgnoreCase(n.getName()))
					{
						Polygon shape = ZoneParser.parsePolygon(n);
						if (!shape.validate())
						{
							error("RestartPointParser: invalid territory data : " + shape + "!");
						}
						if (territory == null)
						{
							territory = new Territory();
						}
						territory.add(shape);
					}
					else if ("restart".equalsIgnoreCase(n.getName()))
					{
						Race race = Race.valueOf(n.attributeValue("race"));
						String locName = n.attributeValue("loc");
						restarts.put(race, locName);
					}
				}
				if (territory == null)
				{
					throw new RuntimeException("RestartPointParser: empty territory!");
				}
				if (restarts.isEmpty())
				{
					throw new RuntimeException("RestartPointParser: restarts not defined!");
				}
				restartArea.add(new ImmutablePair<>(territory, restarts));
			}
			else if ("restart_loc".equals(listElement.getName()))
			{
				String name = listElement.attributeValue("name");
				int bbs = Integer.parseInt(listElement.attributeValue("bbs", "0"));
				int msgId = Integer.parseInt(listElement.attributeValue("msg_id", "0"));
				List<Location> restartPoints = new ArrayList<>();
				List<Location> PKrestartPoints = new ArrayList<>();
				for (Iterator<Element> i = listElement.elementIterator(); i.hasNext();)
				{
					Element n = i.next();
					if ("restart_point".equals(n.getName()))
					{
						for (Iterator<Element> ii = n.elementIterator(); ii.hasNext();)
						{
							Element d = ii.next();
							if ("coords".equalsIgnoreCase(d.getName()))
							{
								Location loc = Location.parseLoc(d.attribute("loc").getValue());
								restartPoints.add(loc);
							}
						}
					}
					else if ("PKrestart_point".equals(n.getName()))
					{
						for (Iterator<Element> ii = n.elementIterator(); ii.hasNext();)
						{
							Element d = ii.next();
							if ("coords".equalsIgnoreCase(d.getName()))
							{
								Location loc = Location.parseLoc(d.attribute("loc").getValue());
								PKrestartPoints.add(loc);
							}
						}
					}
				}
				if (restartPoints.isEmpty())
				{
					throw new RuntimeException("RestartPointParser: restart_points not defined for restart_loc : " + name + "!");
				}
				if (PKrestartPoints.isEmpty())
				{
					PKrestartPoints = restartPoints;
				}
				RestartPoint rp = new RestartPoint(name, bbs, msgId, restartPoints, PKrestartPoints);
				restartPoint.put(name, rp);
			}
		}
		for (Pair<Territory, Map<Race, String>> ra : restartArea)
		{
			Map<Race, RestartPoint> restarts = new HashMap<>();
			for (Map.Entry<Race, String> e : ra.getValue().entrySet())
			{
				RestartPoint rp = restartPoint.get(e.getValue());
				if (rp == null)
				{
					throw new RuntimeException("RestartPointParser: restart_loc not found : " + e.getValue() + "!");
				}
				restarts.put(e.getKey(), rp);
				getHolder().addRegionData(new RestartArea(ra.getKey(), restarts));
			}
		}
	}
}
