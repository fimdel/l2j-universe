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
import java.util.Iterator;
import java.util.List;

import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.commons.geometry.Circle;
import lineage2.commons.geometry.Polygon;
import lineage2.commons.geometry.Rectangle;
import lineage2.commons.geometry.Shape;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.ZoneHolder;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.World;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.ZoneTemplate;
import lineage2.gameserver.utils.Location;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ZoneParser extends AbstractDirParser<ZoneHolder>
{
	/**
	 * Field _instance.
	 */
	private static final ZoneParser _instance = new ZoneParser();
	
	/**
	 * Method getInstance.
	 * @return ZoneParser
	 */
	public static ZoneParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for ZoneParser.
	 */
	protected ZoneParser()
	{
		super(ZoneHolder.getInstance());
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/zone/");
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
		return "zone.dtd";
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
			StatsSet zoneDat = new StatsSet();
			Element zoneElement = iterator.next();
			if ("zone".equals(zoneElement.getName()))
			{
				zoneDat.set("name", zoneElement.attribute("name").getValue());
				zoneDat.set("type", zoneElement.attribute("type").getValue());
				Territory territory = null;
				boolean isShape;
				for (Iterator<Element> i = zoneElement.elementIterator(); i.hasNext();)
				{
					Element n = i.next();
					if ("set".equals(n.getName()))
					{
						zoneDat.set(n.attributeValue("name"), n.attributeValue("val"));
					}
					else if ("restart_point".equals(n.getName()))
					{
						List<Location> restartPoints = new ArrayList<>();
						for (Iterator<?> ii = n.elementIterator(); ii.hasNext();)
						{
							Element d = (Element) ii.next();
							if ("coords".equalsIgnoreCase(d.getName()))
							{
								Location loc = Location.parseLoc(d.attribute("loc").getValue());
								restartPoints.add(loc);
							}
						}
						zoneDat.set("restart_points", restartPoints);
					}
					else if ("PKrestart_point".equals(n.getName()))
					{
						List<Location> PKrestartPoints = new ArrayList<>();
						for (Iterator<?> ii = n.elementIterator(); ii.hasNext();)
						{
							Element d = (Element) ii.next();
							if ("coords".equalsIgnoreCase(d.getName()))
							{
								Location loc = Location.parseLoc(d.attribute("loc").getValue());
								PKrestartPoints.add(loc);
							}
						}
						zoneDat.set("PKrestart_points", PKrestartPoints);
					}
					else if ((isShape = "rectangle".equalsIgnoreCase(n.getName())) || "banned_rectangle".equalsIgnoreCase(n.getName()))
					{
						Shape shape = parseRectangle(n);
						if (territory == null)
						{
							territory = new Territory();
							zoneDat.set("territory", territory);
						}
						if (isShape)
						{
							territory.add(shape);
						}
						else
						{
							territory.addBanned(shape);
						}
					}
					else if ((isShape = "circle".equalsIgnoreCase(n.getName())) || "banned_cicrcle".equalsIgnoreCase(n.getName()))
					{
						Shape shape = parseCircle(n);
						if (territory == null)
						{
							territory = new Territory();
							zoneDat.set("territory", territory);
						}
						if (isShape)
						{
							territory.add(shape);
						}
						else
						{
							territory.addBanned(shape);
						}
					}
					else if ((isShape = "polygon".equalsIgnoreCase(n.getName())) || "banned_polygon".equalsIgnoreCase(n.getName()))
					{
						Polygon shape = parsePolygon(n);
						if (!shape.validate())
						{
							error("ZoneParser: invalid territory data : " + shape + ", zone: " + zoneDat.getString("name") + "!");
						}
						if (territory == null)
						{
							territory = new Territory();
							zoneDat.set("territory", territory);
						}
						if (isShape)
						{
							territory.add(shape);
						}
						else
						{
							territory.addBanned(shape);
						}
					}
				}
				if ((territory == null) || territory.getTerritories().isEmpty())
				{
					error("Empty territory for zone: " + zoneDat.get("name"));
				}
				ZoneTemplate template = new ZoneTemplate(zoneDat);
				getHolder().addTemplate(template);
			}
		}
	}
	
	/**
	 * Method parseRectangle.
	 * @param n Element
	 * @return Rectangle * @throws Exception
	 */
	public static Rectangle parseRectangle(Element n) throws Exception
	{
		int x1, y1, x2, y2, zmin = World.MAP_MIN_Z, zmax = World.MAP_MAX_Z;
		Iterator<Element> i = n.elementIterator();
		Element d = i.next();
		String[] coord = d.attributeValue("loc").split("[\\s,;]+");
		x1 = Integer.parseInt(coord[0]);
		y1 = Integer.parseInt(coord[1]);
		if (coord.length > 2)
		{
			zmin = Integer.parseInt(coord[2]);
			zmax = Integer.parseInt(coord[3]);
		}
		d = i.next();
		coord = d.attributeValue("loc").split("[\\s,;]+");
		x2 = Integer.parseInt(coord[0]);
		y2 = Integer.parseInt(coord[1]);
		if (coord.length > 2)
		{
			zmin = Integer.parseInt(coord[2]);
			zmax = Integer.parseInt(coord[3]);
		}
		Rectangle rectangle = new Rectangle(x1, y1, x2, y2);
		rectangle.setZmin(zmin);
		rectangle.setZmax(zmax);
		return rectangle;
	}
	
	/**
	 * Method parsePolygon.
	 * @param shape Element
	 * @return Polygon * @throws Exception
	 */
	public static Polygon parsePolygon(Element shape) throws Exception
	{
		Polygon poly = new Polygon();
		for (Iterator<Element> i = shape.elementIterator(); i.hasNext();)
		{
			Element d = i.next();
			if ("coords".equals(d.getName()))
			{
				String[] coord = d.attributeValue("loc").split("[\\s,;]+");
				if (coord.length < 4)
				{
					poly.add(Integer.parseInt(coord[0]), Integer.parseInt(coord[1])).setZmin(World.MAP_MIN_Z).setZmax(World.MAP_MAX_Z);
				}
				else
				{
					poly.add(Integer.parseInt(coord[0]), Integer.parseInt(coord[1])).setZmin(Integer.parseInt(coord[2])).setZmax(Integer.parseInt(coord[3]));
				}
			}
		}
		return poly;
	}
	
	/**
	 * Method parseCircle.
	 * @param shape Element
	 * @return Circle * @throws Exception
	 */
	public static Circle parseCircle(Element shape) throws Exception
	{
		Circle circle;
		String[] coord = shape.attribute("loc").getValue().split("[\\s,;]+");
		if (coord.length < 5)
		{
			circle = new Circle(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]), Integer.parseInt(coord[2])).setZmin(World.MAP_MIN_Z).setZmax(World.MAP_MAX_Z);
		}
		else
		{
			circle = new Circle(Integer.parseInt(coord[0]), Integer.parseInt(coord[1]), Integer.parseInt(coord[2])).setZmin(Integer.parseInt(coord[3])).setZmax(Integer.parseInt(coord[4]));
		}
		return circle;
	}
}
