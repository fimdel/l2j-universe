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

import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.commons.geometry.Polygon;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.DoorHolder;
import lineage2.gameserver.templates.DoorTemplate;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.Location;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class DoorParser extends AbstractDirParser<DoorHolder>
{
	/**
	 * Field _instance.
	 */
	private static final DoorParser _instance = new DoorParser();
	
	/**
	 * Method getInstance.
	 * @return DoorParser
	 */
	public static DoorParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for DoorParser.
	 */
	protected DoorParser()
	{
		super(DoorHolder.getInstance());
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/doors/");
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
		return "doors.dtd";
	}
	
	/**
	 * Method initBaseStats.
	 * @return StatsSet
	 */
	private StatsSet initBaseStats()
	{
		StatsSet baseDat = new StatsSet();
		baseDat.set("level", 0);
		baseDat.set("baseSTR", 0);
		baseDat.set("baseCON", 0);
		baseDat.set("baseDEX", 0);
		baseDat.set("baseINT", 0);
		baseDat.set("baseWIT", 0);
		baseDat.set("baseMEN", 0);
		baseDat.set("baseShldDef", 0);
		baseDat.set("baseShldRate", 0);
		baseDat.set("baseAccCombat", 38);
		baseDat.set("baseEvasRate", 38);
		baseDat.set("baseCritRate", 38);
		baseDat.set("baseAtkRange", 0);
		baseDat.set("baseMpMax", 0);
		baseDat.set("baseCpMax", 0);
		baseDat.set("basePAtk", 0);
		baseDat.set("baseMAtk", 0);
		baseDat.set("basePAtkSpd", 0);
		baseDat.set("baseMAtkSpd", 0);
		baseDat.set("baseWalkSpd", 0);
		baseDat.set("baseRunSpd", 0);
		baseDat.set("baseHpReg", 0);
		baseDat.set("baseCpReg", 0);
		baseDat.set("baseMpReg", 0);
		return baseDat;
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
			Element doorElement = iterator.next();
			if ("door".equals(doorElement.getName()))
			{
				StatsSet doorSet = initBaseStats();
				StatsSet aiParams = null;
				doorSet.set("door_type", doorElement.attributeValue("type"));
				Element posElement = doorElement.element("pos");
				Location doorPos;
				int x = Integer.parseInt(posElement.attributeValue("x"));
				int y = Integer.parseInt(posElement.attributeValue("y"));
				int z = Integer.parseInt(posElement.attributeValue("z"));
				doorSet.set("pos", doorPos = new Location(x, y, z));
				Polygon shape = new Polygon();
				int minz = 0, maxz = 0;
				Element shapeElement = doorElement.element("shape");
				minz = Integer.parseInt(shapeElement.attributeValue("minz"));
				maxz = Integer.parseInt(shapeElement.attributeValue("maxz"));
				shape.add(Integer.parseInt(shapeElement.attributeValue("ax")), Integer.parseInt(shapeElement.attributeValue("ay")));
				shape.add(Integer.parseInt(shapeElement.attributeValue("bx")), Integer.parseInt(shapeElement.attributeValue("by")));
				shape.add(Integer.parseInt(shapeElement.attributeValue("cx")), Integer.parseInt(shapeElement.attributeValue("cy")));
				shape.add(Integer.parseInt(shapeElement.attributeValue("dx")), Integer.parseInt(shapeElement.attributeValue("dy")));
				shape.setZmin(minz);
				shape.setZmax(maxz);
				doorSet.set("shape", shape);
				doorPos.setZ(minz + 32);
				for (Iterator<Element> i = doorElement.elementIterator(); i.hasNext();)
				{
					Element n = i.next();
					if ("set".equals(n.getName()))
					{
						doorSet.set(n.attributeValue("name"), n.attributeValue("value"));
					}
					else if ("ai_params".equals(n.getName()))
					{
						if (aiParams == null)
						{
							aiParams = new StatsSet();
							doorSet.set("ai_params", aiParams);
						}
						for (Iterator<Element> aiParamsIterator = n.elementIterator(); aiParamsIterator.hasNext();)
						{
							Element aiParamElement = aiParamsIterator.next();
							aiParams.set(aiParamElement.attributeValue("name"), aiParamElement.attributeValue("value"));
						}
					}
				}
				doorSet.set("uid", doorElement.attributeValue("id"));
				doorSet.set("name", doorElement.attributeValue("name"));
				doorSet.set("baseHpMax", doorElement.attributeValue("hp"));
				doorSet.set("basePDef", doorElement.attributeValue("pdef"));
				doorSet.set("baseMDef", doorElement.attributeValue("mdef"));
				doorSet.set("collision_height", (maxz - minz) & 0xfff0);
				doorSet.set("collision_radius", Math.max(50, Math.min(doorPos.x - shape.getXmin(), doorPos.y - shape.getYmin())));
				DoorTemplate template = new DoorTemplate(doorSet);
				getHolder().addTemplate(template);
			}
		}
	}
}
