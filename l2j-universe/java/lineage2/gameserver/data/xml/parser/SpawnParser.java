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
import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.commons.geometry.Polygon;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.SpawnHolder;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.spawn.PeriodOfDay;
import lineage2.gameserver.templates.spawn.SpawnNpcInfo;
import lineage2.gameserver.templates.spawn.SpawnTemplate;
import lineage2.gameserver.utils.Location;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class SpawnParser extends AbstractDirParser<SpawnHolder>
{
	/**
	 * Field _instance.
	 */
	private static final SpawnParser _instance = new SpawnParser();
	
	/**
	 * Method getInstance.
	 * @return SpawnParser
	 */
	public static SpawnParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for SpawnParser.
	 */
	protected SpawnParser()
	{
		super(SpawnHolder.getInstance());
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/spawn/");
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
		return "spawn.dtd";
	}
	
	/**
	 * Method readData.
	 * @param rootElement Element
	 * @throws Exception
	 */
	@Override
	protected void readData(Element rootElement) throws Exception
	{
		Map<String, Territory> territories = new HashMap<>();
		for (Iterator<Element> spawnIterator = rootElement.elementIterator(); spawnIterator.hasNext();)
		{
			Element spawnElement = spawnIterator.next();
			if (spawnElement.getName().equalsIgnoreCase("territory"))
			{
				String terName = spawnElement.attributeValue("name");
				Territory territory = parseTerritory(terName, spawnElement);
				territories.put(terName, territory);
			}
			else if (spawnElement.getName().equalsIgnoreCase("spawn"))
			{
				String group = spawnElement.attributeValue("group");
				int respawn = spawnElement.attributeValue("respawn") == null ? 60 : Integer.parseInt(spawnElement.attributeValue("respawn"));
				int respawnRandom = spawnElement.attributeValue("respawn_random") == null ? 0 : Integer.parseInt(spawnElement.attributeValue("respawn_random"));
				int count = spawnElement.attributeValue("count") == null ? 1 : Integer.parseInt(spawnElement.attributeValue("count"));
				PeriodOfDay periodOfDay = spawnElement.attributeValue("period_of_day") == null ? PeriodOfDay.NONE : PeriodOfDay.valueOf(spawnElement.attributeValue("period_of_day").toUpperCase());
				if (group == null)
				{
					group = periodOfDay.name();
				}
				SpawnTemplate template = new SpawnTemplate(periodOfDay, count, respawn, respawnRandom);
				for (Iterator<Element> subIterator = spawnElement.elementIterator(); subIterator.hasNext();)
				{
					Element subElement = subIterator.next();
					if (subElement.getName().equalsIgnoreCase("point"))
					{
						int x = Integer.parseInt(subElement.attributeValue("x"));
						int y = Integer.parseInt(subElement.attributeValue("y"));
						int z = Integer.parseInt(subElement.attributeValue("z"));
						int h = subElement.attributeValue("h") == null ? -1 : Integer.parseInt(subElement.attributeValue("h"));
						template.addSpawnRange(new Location(x, y, z, h));
					}
					else if (subElement.getName().equalsIgnoreCase("territory"))
					{
						String terName = subElement.attributeValue("name");
						if (terName != null)
						{
							Territory g = territories.get(terName);
							if (g == null)
							{
								error("Invalid territory name: " + terName + "; " + getCurrentFileName());
								continue;
							}
							template.addSpawnRange(g);
						}
						else
						{
							Territory temp = parseTerritory(null, subElement);
							template.addSpawnRange(temp);
						}
					}
					else if (subElement.getName().equalsIgnoreCase("npc"))
					{
						int npcId = Integer.parseInt(subElement.attributeValue("id"));
						int max = subElement.attributeValue("max") == null ? 0 : Integer.parseInt(subElement.attributeValue("max"));
						MultiValueSet<String> parameters = StatsSet.EMPTY;
						for (Element e : subElement.elements())
						{
							if (parameters.isEmpty())
							{
								parameters = new MultiValueSet<>();
							}
							parameters.set(e.attributeValue("name"), e.attributeValue("value"));
						}
						template.addNpc(new SpawnNpcInfo(npcId, max, parameters));
					}
				}
				if (template.getNpcSize() == 0)
				{
					warn("Npc id is zero! File: " + getCurrentFileName());
					continue;
				}
				if (template.getSpawnRangeSize() == 0)
				{
					warn("No points to spawn! File: " + getCurrentFileName());
					continue;
				}
				getHolder().addSpawn(group, template);
			}
		}
	}
	
	/**
	 * Method parseTerritory.
	 * @param name String
	 * @param e Element
	 * @return Territory
	 */
	private Territory parseTerritory(String name, Element e)
	{
		Territory t = new Territory();
		t.add(parsePolygon0(name, e));
		for (Iterator<Element> iterator = e.elementIterator("banned_territory"); iterator.hasNext();)
		{
			t.addBanned(parsePolygon0(name, iterator.next()));
		}
		return t;
	}
	
	/**
	 * Method parsePolygon0.
	 * @param name String
	 * @param e Element
	 * @return Polygon
	 */
	private Polygon parsePolygon0(String name, Element e)
	{
		Polygon temp = new Polygon();
		for (Iterator<Element> addIterator = e.elementIterator("add"); addIterator.hasNext();)
		{
			Element addElement = addIterator.next();
			int x = Integer.parseInt(addElement.attributeValue("x"));
			int y = Integer.parseInt(addElement.attributeValue("y"));
			int zmin = Integer.parseInt(addElement.attributeValue("zmin"));
			int zmax = Integer.parseInt(addElement.attributeValue("zmax"));
			temp.add(x, y).setZmin(zmin).setZmax(zmax);
		}
		if (!temp.validate())
		{
			error("Invalid polygon: " + name + "{" + temp + "}. File: " + getCurrentFileName());
		}
		return temp;
	}
}
