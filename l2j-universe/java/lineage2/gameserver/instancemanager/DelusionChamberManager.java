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
package lineage2.gameserver.instancemanager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilderFactory;

import lineage2.commons.geometry.Rectangle;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.TeleportToLocation;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DelusionChamberManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(DelusionChamberManager.class);
	/**
	 * Field _instance.
	 */
	private static DelusionChamberManager _instance;
	/**
	 * Field _rooms.
	 */
	private final Map<Integer, Map<Integer, DelusionChamberRoom>> _rooms = new ConcurrentHashMap<>();
	
	/**
	 * Method getInstance.
	 * @return DelusionChamberManager
	 */
	public static DelusionChamberManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new DelusionChamberManager();
		}
		return _instance;
	}
	
	/**
	 * Constructor for DelusionChamberManager.
	 */
	public DelusionChamberManager()
	{
		load();
	}
	
	/**
	 * Method getRoom.
	 * @param type int
	 * @param room int
	 * @return DelusionChamberRoom
	 */
	public DelusionChamberRoom getRoom(int type, int room)
	{
		return _rooms.get(type).get(room);
	}
	
	/**
	 * Method getRooms.
	 * @param type int
	 * @return Map<Integer,DelusionChamberRoom>
	 */
	public Map<Integer, DelusionChamberRoom> getRooms(int type)
	{
		return _rooms.get(type);
	}
	
	/**
	 * Method load.
	 */
	public void load()
	{
		int countGood = 0, countBad = 0;
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			File file = new File(Config.DATAPACK_ROOT, "data/xml/other/delusion_chamber.xml");
			if (!file.exists())
			{
				throw new IOException();
			}
			Document doc = factory.newDocumentBuilder().parse(file);
			NamedNodeMap attrs;
			int type;
			int roomId;
			int mobId, delay, count;
			SimpleSpawner spawnDat;
			NpcTemplate template;
			Location tele = new Location();
			int xMin = 0, xMax = 0, yMin = 0, yMax = 0, zMin = 0, zMax = 0;
			boolean isBossRoom;
			for (Node rift = doc.getFirstChild(); rift != null; rift = rift.getNextSibling())
			{
				if ("rift".equalsIgnoreCase(rift.getNodeName()))
				{
					for (Node area = rift.getFirstChild(); area != null; area = area.getNextSibling())
					{
						if ("area".equalsIgnoreCase(area.getNodeName()))
						{
							attrs = area.getAttributes();
							type = Integer.parseInt(attrs.getNamedItem("type").getNodeValue());
							for (Node room = area.getFirstChild(); room != null; room = room.getNextSibling())
							{
								if ("room".equalsIgnoreCase(room.getNodeName()))
								{
									attrs = room.getAttributes();
									roomId = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
									Node boss = attrs.getNamedItem("isBossRoom");
									isBossRoom = (boss != null) && Boolean.parseBoolean(boss.getNodeValue());
									Territory territory = null;
									for (Node coord = room.getFirstChild(); coord != null; coord = coord.getNextSibling())
									{
										if ("teleport".equalsIgnoreCase(coord.getNodeName()))
										{
											attrs = coord.getAttributes();
											tele = Location.parseLoc(attrs.getNamedItem("loc").getNodeValue());
										}
										else if ("zone".equalsIgnoreCase(coord.getNodeName()))
										{
											attrs = coord.getAttributes();
											xMin = Integer.parseInt(attrs.getNamedItem("xMin").getNodeValue());
											xMax = Integer.parseInt(attrs.getNamedItem("xMax").getNodeValue());
											yMin = Integer.parseInt(attrs.getNamedItem("yMin").getNodeValue());
											yMax = Integer.parseInt(attrs.getNamedItem("yMax").getNodeValue());
											zMin = Integer.parseInt(attrs.getNamedItem("zMin").getNodeValue());
											zMax = Integer.parseInt(attrs.getNamedItem("zMax").getNodeValue());
											territory = new Territory().add(new Rectangle(xMin, yMin, xMax, yMax).setZmin(zMin).setZmax(zMax));
										}
									}
									if (territory == null)
									{
										_log.error("DimensionalRiftManager: invalid spawn data for room id " + roomId + "!");
									}
									if (!_rooms.containsKey(type))
									{
										_rooms.put(type, new ConcurrentHashMap<Integer, DelusionChamberRoom>());
									}
									_rooms.get(type).put(roomId, new DelusionChamberRoom(territory, tele, isBossRoom));
									for (Node spawn = room.getFirstChild(); spawn != null; spawn = spawn.getNextSibling())
									{
										if ("spawn".equalsIgnoreCase(spawn.getNodeName()))
										{
											attrs = spawn.getAttributes();
											mobId = Integer.parseInt(attrs.getNamedItem("mobId").getNodeValue());
											delay = Integer.parseInt(attrs.getNamedItem("delay").getNodeValue());
											count = Integer.parseInt(attrs.getNamedItem("count").getNodeValue());
											template = NpcHolder.getInstance().getTemplate(mobId);
											if (template == null)
											{
												_log.warn("Template " + mobId + " not found!");
											}
											if (!_rooms.containsKey(type))
											{
												_log.warn("Type " + type + " not found!");
											}
											else if (!_rooms.get(type).containsKey(roomId))
											{
												_log.warn("Room " + roomId + " in Type " + type + " not found!");
											}
											if ((template != null) && _rooms.containsKey(type) && _rooms.get(type).containsKey(roomId))
											{
												spawnDat = new SimpleSpawner(template);
												spawnDat.setTerritory(territory);
												spawnDat.setHeading(-1);
												spawnDat.setRespawnDelay(delay);
												spawnDat.setAmount(count);
												_rooms.get(type).get(roomId).getSpawns().add(spawnDat);
												countGood++;
											}
											else
											{
												countBad++;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.error("DelusionChamberManager: Error on loading delusion chamber spawns!", e);
		}
		int typeSize = _rooms.keySet().size();
		int roomSize = 0;
		for (int b : _rooms.keySet())
		{
			roomSize += _rooms.get(b).keySet().size();
		}
		_log.info("DelusionChamberManager: Loaded " + typeSize + " room types with " + roomSize + " rooms.");
		_log.info("DelusionChamberManager: Loaded " + countGood + " delusion chamber spawns, " + countBad + " errors.");
	}
	
	/**
	 * Method reload.
	 */
	public void reload()
	{
		for (int b : _rooms.keySet())
		{
			_rooms.get(b).clear();
		}
		_rooms.clear();
		load();
	}
	
	/**
	 * Method teleportToWaitingRoom.
	 * @param player Player
	 */
	public void teleportToWaitingRoom(Player player)
	{
		teleToLocation(player, Location.findPointToStay(getRoom(0, 0).getTeleportCoords(), 0, 250, ReflectionManager.DEFAULT.getGeoIndex()), null);
	}
	
	/**
	 * @author Mobius
	 */
	public class DelusionChamberRoom
	{
		/**
		 * Field _territory.
		 */
		private final Territory _territory;
		/**
		 * Field _teleportCoords.
		 */
		private final Location _teleportCoords;
		/**
		 * Field _isBossRoom.
		 */
		private final boolean _isBossRoom;
		/**
		 * Field _roomSpawns.
		 */
		private final List<SimpleSpawner> _roomSpawns;
		
		/**
		 * Constructor for DelusionChamberRoom.
		 * @param territory Territory
		 * @param tele Location
		 * @param isBossRoom boolean
		 */
		public DelusionChamberRoom(Territory territory, Location tele, boolean isBossRoom)
		{
			_territory = territory;
			_teleportCoords = tele;
			_isBossRoom = isBossRoom;
			_roomSpawns = new ArrayList<>();
		}
		
		/**
		 * Method getTeleportCoords.
		 * @return Location
		 */
		public Location getTeleportCoords()
		{
			return _teleportCoords;
		}
		
		/**
		 * Method checkIfInZone.
		 * @param loc Location
		 * @return boolean
		 */
		public boolean checkIfInZone(Location loc)
		{
			return checkIfInZone(loc.x, loc.y, loc.z);
		}
		
		/**
		 * Method checkIfInZone.
		 * @param x int
		 * @param y int
		 * @param z int
		 * @return boolean
		 */
		public boolean checkIfInZone(int x, int y, int z)
		{
			return _territory.isInside(x, y, z);
		}
		
		/**
		 * Method isBossRoom.
		 * @return boolean
		 */
		public boolean isBossRoom()
		{
			return _isBossRoom;
		}
		
		/**
		 * Method getSpawns.
		 * @return List<SimpleSpawner>
		 */
		public List<SimpleSpawner> getSpawns()
		{
			return _roomSpawns;
		}
	}
	
	/**
	 * Method showHtmlFile.
	 * @param player Player
	 * @param file String
	 * @param npc NpcInstance
	 */
	public void showHtmlFile(Player player, String file, NpcInstance npc)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(player, npc);
		html.setFile(file);
		html.replace("%t_name%", npc.getName());
		player.sendPacket(html);
	}
	
	/**
	 * Method teleToLocation.
	 * @param player Player
	 * @param loc Location
	 * @param ref Reflection
	 */
	public static void teleToLocation(Player player, Location loc, Reflection ref)
	{
		if (player.isTeleporting() || player.isDeleted())
		{
			return;
		}
		player.setIsTeleporting(true);
		player.setTarget(null);
		player.stopMove();
		if (player.isInBoat())
		{
			player.setBoat(null);
		}
		player.breakFakeDeath();
		player.decayMe();
		player.setLoc(loc);
		if (ref == null)
		{
			player.setReflection(ReflectionManager.DEFAULT);
		}
		player.setLastClientPosition(null);
		player.setLastServerPosition(null);
		player.sendPacket(new TeleportToLocation(player, loc));
	}
}
