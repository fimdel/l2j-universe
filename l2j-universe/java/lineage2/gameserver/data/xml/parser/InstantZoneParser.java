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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lineage2.commons.data.xml.AbstractDirParser;
import lineage2.commons.geometry.Polygon;
import lineage2.commons.time.cron.SchedulingPattern;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.DoorHolder;
import lineage2.gameserver.data.xml.holder.InstantZoneHolder;
import lineage2.gameserver.data.xml.holder.SpawnHolder;
import lineage2.gameserver.data.xml.holder.ZoneHolder;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.templates.DoorTemplate;
import lineage2.gameserver.templates.InstantZone;
import lineage2.gameserver.templates.InstantZone.SpawnInfo;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.ZoneTemplate;
import lineage2.gameserver.templates.spawn.SpawnTemplate;
import lineage2.gameserver.utils.Location;

import org.dom4j.Element;
import org.napile.primitive.Containers;
import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class InstantZoneParser extends AbstractDirParser<InstantZoneHolder>
{
	/**
	 * Field _instance.
	 */
	private static InstantZoneParser _instance = new InstantZoneParser();
	
	/**
	 * Method getInstance.
	 * @return InstantZoneParser
	 */
	public static InstantZoneParser getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for InstantZoneParser.
	 */
	public InstantZoneParser()
	{
		super(InstantZoneHolder.getInstance());
	}
	
	/**
	 * Method getXMLDir.
	 * @return File
	 */
	@Override
	public File getXMLDir()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/instances/");
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
		return "instances.dtd";
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
			Element element = iterator.next();
			int instanceId;
			String name;
			SchedulingPattern resetReuse = new SchedulingPattern("30 6 * * *");
			int timelimit = -1;
			int timer = 60;
			int mapx = -1;
			int mapy = -1;
			boolean dispelBuffs = false;
			boolean onPartyDismiss = true;
			int mobId, respawn, respawnRnd, count, sharedReuseGroup = 0;
			int collapseIfEmpty = 0;
			int spawnType = 0;
			SpawnInfo spawnDat = null;
			int removedItemId = 0, removedItemCount = 0, giveItemId = 0, givedItemCount = 0, requiredQuestId = 0;
			int maxChannels = 20;
			boolean removedItemNecessity = false;
			boolean setReuseUponEntry = true;
			StatsSet params = new StatsSet();
			List<InstantZone.SpawnInfo> spawns = new ArrayList<>();
			IntObjectMap<InstantZone.DoorInfo> doors = Containers.emptyIntObjectMap();
			Map<String, InstantZone.ZoneInfo> zones = Collections.emptyMap();
			Map<String, InstantZone.SpawnInfo2> spawns2 = Collections.emptyMap();
			instanceId = Integer.parseInt(element.attributeValue("id"));
			name = element.attributeValue("name");
			String n = element.attributeValue("timelimit");
			if (n != null)
			{
				timelimit = Integer.parseInt(n);
			}
			n = element.attributeValue("collapseIfEmpty");
			collapseIfEmpty = Integer.parseInt(n);
			n = element.attributeValue("maxChannels");
			maxChannels = Integer.parseInt(n);
			n = element.attributeValue("dispelBuffs");
			dispelBuffs = (n != null) && Boolean.parseBoolean(n);
			int minLevel = 0, maxLevel = 0, minParty = 1, maxParty = 7;
			List<Location> teleportLocs = Collections.emptyList();
			Location ret = null;
			for (Iterator<Element> subIterator = element.elementIterator(); subIterator.hasNext();)
			{
				Element subElement = subIterator.next();
				if ("level".equalsIgnoreCase(subElement.getName()))
				{
					minLevel = Integer.parseInt(subElement.attributeValue("min"));
					maxLevel = Integer.parseInt(subElement.attributeValue("max"));
				}
				else if ("collapse".equalsIgnoreCase(subElement.getName()))
				{
					onPartyDismiss = Boolean.parseBoolean(subElement.attributeValue("on-party-dismiss"));
					timer = Integer.parseInt(subElement.attributeValue("timer"));
				}
				else if ("party".equalsIgnoreCase(subElement.getName()))
				{
					minParty = Integer.parseInt(subElement.attributeValue("min"));
					maxParty = Integer.parseInt(subElement.attributeValue("max"));
				}
				else if ("return".equalsIgnoreCase(subElement.getName()))
				{
					ret = Location.parseLoc(subElement.attributeValue("loc"));
				}
				else if ("teleport".equalsIgnoreCase(subElement.getName()))
				{
					if (teleportLocs.isEmpty())
					{
						teleportLocs = new ArrayList<>(1);
					}
					teleportLocs.add(Location.parseLoc(subElement.attributeValue("loc")));
				}
				else if ("remove".equalsIgnoreCase(subElement.getName()))
				{
					removedItemId = Integer.parseInt(subElement.attributeValue("itemId"));
					removedItemCount = Integer.parseInt(subElement.attributeValue("count"));
					removedItemNecessity = Boolean.parseBoolean(subElement.attributeValue("necessary"));
				}
				else if ("give".equalsIgnoreCase(subElement.getName()))
				{
					giveItemId = Integer.parseInt(subElement.attributeValue("itemId"));
					givedItemCount = Integer.parseInt(subElement.attributeValue("count"));
				}
				else if ("quest".equalsIgnoreCase(subElement.getName()))
				{
					requiredQuestId = Integer.parseInt(subElement.attributeValue("id"));
				}
				else if ("reuse".equalsIgnoreCase(subElement.getName()))
				{
					resetReuse = new SchedulingPattern(subElement.attributeValue("resetReuse"));
					sharedReuseGroup = Integer.parseInt(subElement.attributeValue("sharedReuseGroup"));
					setReuseUponEntry = Boolean.parseBoolean(subElement.attributeValue("setUponEntry"));
				}
				else if ("geodata".equalsIgnoreCase(subElement.getName()))
				{
					String[] rxy = subElement.attributeValue("map").split("_");
					mapx = Integer.parseInt(rxy[0]);
					mapy = Integer.parseInt(rxy[1]);
				}
				else if ("doors".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						if (doors.isEmpty())
						{
							doors = new HashIntObjectMap<>();
						}
						boolean opened = (e.attributeValue("opened") != null) && Boolean.parseBoolean(e.attributeValue("opened"));
						boolean invul = (e.attributeValue("invul") == null) || Boolean.parseBoolean(e.attributeValue("invul"));
						DoorTemplate template = DoorHolder.getInstance().getTemplate(Integer.parseInt(e.attributeValue("id")));
						doors.put(template.getNpcId(), new InstantZone.DoorInfo(template, opened, invul));
					}
				}
				else if ("zones".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						if (zones.isEmpty())
						{
							zones = new HashMap<>();
						}
						boolean active = (e.attributeValue("active") != null) && Boolean.parseBoolean(e.attributeValue("active"));
						ZoneTemplate template = ZoneHolder.getInstance().getTemplate(e.attributeValue("name"));
						if (template == null)
						{
							error("Zone: " + e.attributeValue("name") + " not found; file: " + getCurrentFileName());
							continue;
						}
						zones.put(template.getName(), new InstantZone.ZoneInfo(template, active));
					}
				}
				else if ("add_parameters".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						if ("param".equalsIgnoreCase(e.getName()))
						{
							params.set(e.attributeValue("name"), e.attributeValue("value"));
						}
					}
				}
				else if ("spawns".equalsIgnoreCase(subElement.getName()))
				{
					for (Element e : subElement.elements())
					{
						if ("group".equalsIgnoreCase(e.getName()))
						{
							String group = e.attributeValue("name");
							boolean spawned = (e.attributeValue("spawned") != null) && Boolean.parseBoolean(e.attributeValue("spawned"));
							List<SpawnTemplate> templates = SpawnHolder.getInstance().getSpawn(group);
							if (templates == null)
							{
								info("not find spawn group: " + group + " in file: " + getCurrentFileName());
							}
							else
							{
								if (spawns2.isEmpty())
								{
									spawns2 = new Hashtable<>();
								}
								spawns2.put(group, new InstantZone.SpawnInfo2(templates, spawned));
							}
						}
						else if ("spawn".equalsIgnoreCase(e.getName()))
						{
							String[] mobs = e.attributeValue("mobId").split(" ");
							String respawnNode = e.attributeValue("respawn");
							respawn = respawnNode != null ? Integer.parseInt(respawnNode) : 0;
							String respawnRndNode = e.attributeValue("respawnRnd");
							respawnRnd = respawnRndNode != null ? Integer.parseInt(respawnRndNode) : 0;
							String countNode = e.attributeValue("count");
							count = countNode != null ? Integer.parseInt(countNode) : 1;
							List<Location> coords = new ArrayList<>();
							spawnType = 0;
							String spawnTypeNode = e.attributeValue("type");
							if ((spawnTypeNode == null) || spawnTypeNode.equalsIgnoreCase("point"))
							{
								spawnType = 0;
							}
							else if (spawnTypeNode.equalsIgnoreCase("rnd"))
							{
								spawnType = 1;
							}
							else if (spawnTypeNode.equalsIgnoreCase("loc"))
							{
								spawnType = 2;
							}
							else
							{
								error("Spawn type  '" + spawnTypeNode + "' is unknown!");
							}
							for (Element e2 : e.elements())
							{
								if ("coords".equalsIgnoreCase(e2.getName()))
								{
									coords.add(Location.parseLoc(e2.attributeValue("loc")));
								}
							}
							Territory territory = null;
							if (spawnType == 2)
							{
								Polygon poly = new Polygon();
								for (Location loc : coords)
								{
									poly.add(loc.x, loc.y).setZmin(loc.z).setZmax(loc.z);
								}
								if (!poly.validate())
								{
									error("invalid spawn territory for instance id : " + instanceId + " - " + poly + "!");
								}
								territory = new Territory().add(poly);
							}
							for (String mob : mobs)
							{
								mobId = Integer.parseInt(mob);
								spawnDat = new InstantZone.SpawnInfo(spawnType, mobId, count, respawn, respawnRnd, coords, territory);
								spawns.add(spawnDat);
							}
						}
					}
				}
			}
			InstantZone instancedZone = new InstantZone(instanceId, name, resetReuse, sharedReuseGroup, timelimit, dispelBuffs, minLevel, maxLevel, minParty, maxParty, timer, onPartyDismiss, teleportLocs, ret, mapx, mapy, doors, zones, spawns2, spawns, collapseIfEmpty, maxChannels, removedItemId, removedItemCount, removedItemNecessity, giveItemId, givedItemCount, requiredQuestId, setReuseUponEntry, params);
			getHolder().addInstantZone(instancedZone);
		}
	}
}
