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
package lineage2.gameserver.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lineage2.commons.collections.LazyArrayList;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class World
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(World.class);
	/**
	 * Field MAP_MIN_X.
	 */
	public static final int MAP_MIN_X = (Config.GEO_X_FIRST - 20) << 15;
	/**
	 * Field MAP_MAX_X.
	 */
	public static final int MAP_MAX_X = (((Config.GEO_X_LAST - 20) + 1) << 15) - 1;
	/**
	 * Field MAP_MIN_Y.
	 */
	public static final int MAP_MIN_Y = (Config.GEO_Y_FIRST - 18) << 15;
	/**
	 * Field MAP_MAX_Y.
	 */
	public static final int MAP_MAX_Y = (((Config.GEO_Y_LAST - 18) + 1) << 15) - 1;
	/**
	 * Field MAP_MIN_Z.
	 */
	public static final int MAP_MIN_Z = Config.MAP_MIN_Z;
	/**
	 * Field MAP_MAX_Z.
	 */
	public static final int MAP_MAX_Z = Config.MAP_MAX_Z;
	/**
	 * Field WORLD_SIZE_X.
	 */
	public static final int WORLD_SIZE_X = (Config.GEO_X_LAST - Config.GEO_X_FIRST) + 1;
	/**
	 * Field WORLD_SIZE_Y.
	 */
	public static final int WORLD_SIZE_Y = (Config.GEO_Y_LAST - Config.GEO_Y_FIRST) + 1;
	/**
	 * Field SHIFT_BY.
	 */
	public static final int SHIFT_BY = Config.SHIFT_BY;
	/**
	 * Field SHIFT_BY_Z.
	 */
	public static final int SHIFT_BY_Z = Config.SHIFT_BY_Z;
	/**
	 * Field OFFSET_X.
	 */
	public static final int OFFSET_X = Math.abs(MAP_MIN_X >> SHIFT_BY);
	/**
	 * Field OFFSET_Y.
	 */
	public static final int OFFSET_Y = Math.abs(MAP_MIN_Y >> SHIFT_BY);
	/**
	 * Field OFFSET_Z.
	 */
	public static final int OFFSET_Z = Math.abs(MAP_MIN_Z >> SHIFT_BY_Z);
	/**
	 * Field REGIONS_X.
	 */
	private static final int REGIONS_X = (MAP_MAX_X >> SHIFT_BY) + OFFSET_X;
	/**
	 * Field REGIONS_Y.
	 */
	private static final int REGIONS_Y = (MAP_MAX_Y >> SHIFT_BY) + OFFSET_Y;
	/**
	 * Field REGIONS_Z.
	 */
	private static final int REGIONS_Z = (MAP_MAX_Z >> SHIFT_BY_Z) + OFFSET_Z;
	/**
	 * Field _worldRegions.
	 */
	private static volatile WorldRegion[][][] _worldRegions = new WorldRegion[REGIONS_X + 1][REGIONS_Y + 1][REGIONS_Z + 1];
	
	/**
	 * Method init.
	 */
	public static void init()
	{
		_log.info("L2World: Creating regions: [" + (REGIONS_X + 1) + "][" + (REGIONS_Y + 1) + "][" + (REGIONS_Z + 1) + "].");
	}
	
	/**
	 * Method getRegions.
	 * @return WorldRegion[][][]
	 */
	private static WorldRegion[][][] getRegions()
	{
		return _worldRegions;
	}
	
	/**
	 * Method validX.
	 * @param x int
	 * @return int
	 */
	private static int validX(int x)
	{
		if (x < 0)
		{
			x = 0;
		}
		else if (x > REGIONS_X)
		{
			x = REGIONS_X;
		}
		return x;
	}
	
	/**
	 * Method validY.
	 * @param y int
	 * @return int
	 */
	private static int validY(int y)
	{
		if (y < 0)
		{
			y = 0;
		}
		else if (y > REGIONS_Y)
		{
			y = REGIONS_Y;
		}
		return y;
	}
	
	/**
	 * Method validZ.
	 * @param z int
	 * @return int
	 */
	private static int validZ(int z)
	{
		if (z < 0)
		{
			z = 0;
		}
		else if (z > REGIONS_Z)
		{
			z = REGIONS_Z;
		}
		return z;
	}
	
	/**
	 * Method validCoordX.
	 * @param x int
	 * @return int
	 */
	public static int validCoordX(int x)
	{
		if (x < MAP_MIN_X)
		{
			x = MAP_MIN_X + 1;
		}
		else if (x > MAP_MAX_X)
		{
			x = MAP_MAX_X - 1;
		}
		return x;
	}
	
	/**
	 * Method validCoordY.
	 * @param y int
	 * @return int
	 */
	public static int validCoordY(int y)
	{
		if (y < MAP_MIN_Y)
		{
			y = MAP_MIN_Y + 1;
		}
		else if (y > MAP_MAX_Y)
		{
			y = MAP_MAX_Y - 1;
		}
		return y;
	}
	
	/**
	 * Method validCoordZ.
	 * @param z int
	 * @return int
	 */
	public static int validCoordZ(int z)
	{
		if (z < MAP_MIN_Z)
		{
			z = MAP_MIN_Z + 1;
		}
		else if (z > MAP_MAX_Z)
		{
			z = MAP_MAX_Z - 1;
		}
		return z;
	}
	
	/**
	 * Method regionX.
	 * @param x int
	 * @return int
	 */
	private static int regionX(int x)
	{
		return (x >> SHIFT_BY) + OFFSET_X;
	}
	
	/**
	 * Method regionY.
	 * @param y int
	 * @return int
	 */
	private static int regionY(int y)
	{
		return (y >> SHIFT_BY) + OFFSET_Y;
	}
	
	/**
	 * Method regionZ.
	 * @param z int
	 * @return int
	 */
	private static int regionZ(int z)
	{
		return (z >> SHIFT_BY_Z) + OFFSET_Z;
	}
	
	/**
	 * Method isNeighbour.
	 * @param x1 int
	 * @param y1 int
	 * @param z1 int
	 * @param x2 int
	 * @param y2 int
	 * @param z2 int
	 * @return boolean
	 */
	static boolean isNeighbour(int x1, int y1, int z1, int x2, int y2, int z2)
	{
		return (x1 <= (x2 + 1)) && (x1 >= (x2 - 1)) && (y1 <= (y2 + 1)) && (y1 >= (y2 - 1)) && (z1 <= (z2 + 1)) && (z1 >= (z2 - 1));
	}
	
	/**
	 * Method getRegion.
	 * @param loc Location
	 * @return WorldRegion
	 */
	public static WorldRegion getRegion(Location loc)
	{
		return getRegion(validX(regionX(loc.x)), validY(regionY(loc.y)), validZ(regionZ(loc.z)));
	}
	
	/**
	 * Method getRegion.
	 * @param obj GameObject
	 * @return WorldRegion
	 */
	public static WorldRegion getRegion(GameObject obj)
	{
		return getRegion(validX(regionX(obj.getX())), validY(regionY(obj.getY())), validZ(regionZ(obj.getZ())));
	}
	
	/**
	 * Method getRegion.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return WorldRegion
	 */
	private static WorldRegion getRegion(int x, int y, int z)
	{
		WorldRegion[][][] regions = getRegions();
		WorldRegion region = null;
		region = regions[x][y][z];
		if (region == null)
		{
			synchronized (regions)
			{
				region = regions[x][y][z];
				if (region == null)
				{
					region = regions[x][y][z] = new WorldRegion(x, y, z);
				}
			}
		}
		return region;
	}
	
	/**
	 * Method getPlayer.
	 * @param name String
	 * @return Player
	 */
	public static Player getPlayer(String name)
	{
		return GameObjectsStorage.getPlayer(name);
	}
	
	/**
	 * Method getPlayer.
	 * @param objId int
	 * @return Player
	 */
	public static Player getPlayer(int objId)
	{
		return GameObjectsStorage.getPlayer(objId);
	}
	
	/**
	 * Method addVisibleObject.
	 * @param object GameObject
	 * @param dropper Creature
	 */
	public static void addVisibleObject(GameObject object, Creature dropper)
	{
		if ((object == null) || !object.isVisible() || object.isInObserverMode())
		{
			return;
		}
		WorldRegion region = getRegion(object);
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == region)
		{
			return;
		}
		if (currentRegion == null)
		{
			object.setCurrentRegion(region);
			region.addObject(object);
			for (int x = validX(region.getX() - 1); x <= validX(region.getX() + 1); x++)
			{
				for (int y = validY(region.getY() - 1); y <= validY(region.getY() + 1); y++)
				{
					for (int z = validZ(region.getZ() - 1); z <= validZ(region.getZ() + 1); z++)
					{
						getRegion(x, y, z).addToPlayers(object, dropper);
					}
				}
			}
		}
		else
		{
			currentRegion.removeObject(object);
			object.setCurrentRegion(region);
			region.addObject(object);
			for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			{
				for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				{
					for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					{
						if (!isNeighbour(region.getX(), region.getY(), region.getZ(), x, y, z))
						{
							getRegion(x, y, z).removeFromPlayers(object);
						}
					}
				}
			}
			for (int x = validX(region.getX() - 1); x <= validX(region.getX() + 1); x++)
			{
				for (int y = validY(region.getY() - 1); y <= validY(region.getY() + 1); y++)
				{
					for (int z = validZ(region.getZ() - 1); z <= validZ(region.getZ() + 1); z++)
					{
						if (!isNeighbour(currentRegion.getX(), currentRegion.getY(), currentRegion.getZ(), x, y, z))
						{
							getRegion(x, y, z).addToPlayers(object, dropper);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Method removeVisibleObject.
	 * @param object GameObject
	 */
	public static void removeVisibleObject(GameObject object)
	{
		if ((object == null) || object.isVisible() || object.isInObserverMode())
		{
			return;
		}
		WorldRegion currentRegion;
		if ((currentRegion = object.getCurrentRegion()) == null)
		{
			return;
		}
		object.setCurrentRegion(null);
		currentRegion.removeObject(object);
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					getRegion(x, y, z).removeFromPlayers(object);
				}
			}
		}
	}
	
	/**
	 * Method getAroundObjectById.
	 * @param object GameObject
	 * @param objId int
	 * @return GameObject
	 */
	public static GameObject getAroundObjectById(GameObject object, int objId)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
		{
			return null;
		}
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if (obj.getObjectId() == objId)
						{
							return obj;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Method getAroundObjects.
	 * @param object GameObject
	 * @return List<GameObject>
	 */
	public static List<GameObject> getAroundObjects(GameObject object)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
		{
			return Collections.emptyList();
		}
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		List<GameObject> result = new LazyArrayList<>(128);
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if ((obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						result.add(obj);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method getAroundObjects.
	 * @param object GameObject
	 * @param radius int
	 * @param height int
	 * @return List<GameObject>
	 */
	public static List<GameObject> getAroundObjects(GameObject object, int radius, int height)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
		{
			return Collections.emptyList();
		}
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int sqrad = radius * radius;
		List<GameObject> result = new LazyArrayList<>(128);
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if ((obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						if (Math.abs(obj.getZ() - oz) > height)
						{
							continue;
						}
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
						{
							continue;
						}
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
						{
							continue;
						}
						if (((dx * dx) + (dy * dy)) > sqrad)
						{
							continue;
						}
						result.add(obj);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method getAroundCharacters.
	 * @param object GameObject
	 * @return List<Creature>
	 */
	public static List<Creature> getAroundCharacters(GameObject object)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
		{
			return Collections.emptyList();
		}
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		List<Creature> result = new LazyArrayList<>(64);
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isCreature() || (obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						result.add((Creature) obj);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method getAroundCharacters.
	 * @param object GameObject
	 * @param radius int
	 * @param height int
	 * @return List<Creature>
	 */
	public static List<Creature> getAroundCharacters(GameObject object, int radius, int height)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
		{
			return Collections.emptyList();
		}
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int sqrad = radius * radius;
		List<Creature> result = new LazyArrayList<>(64);
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isCreature() || (obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						if (Math.abs(obj.getZ() - oz) > height)
						{
							continue;
						}
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
						{
							continue;
						}
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
						{
							continue;
						}
						if (((dx * dx) + (dy * dy)) > sqrad)
						{
							continue;
						}
						result.add((Creature) obj);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method getAroundNpc.
	 * @param object GameObject
	 * @return List<NpcInstance>
	 */
	public static List<NpcInstance> getAroundNpc(GameObject object)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
		{
			return Collections.emptyList();
		}
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		List<NpcInstance> result = new LazyArrayList<>(64);
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isNpc() || (obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						result.add((NpcInstance) obj);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method getAroundNpc.
	 * @param object GameObject
	 * @param radius int
	 * @param height int
	 * @return List<NpcInstance>
	 */
	public static List<NpcInstance> getAroundNpc(GameObject object, int radius, int height)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
		{
			return Collections.emptyList();
		}
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int sqrad = radius * radius;
		List<NpcInstance> result = new LazyArrayList<>(64);
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isNpc() || (obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						if (Math.abs(obj.getZ() - oz) > height)
						{
							continue;
						}
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
						{
							continue;
						}
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
						{
							continue;
						}
						if (((dx * dx) + (dy * dy)) > sqrad)
						{
							continue;
						}
						result.add((NpcInstance) obj);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method getAroundPlayables.
	 * @param object GameObject
	 * @return List<Playable>
	 */
	public static List<Playable> getAroundPlayables(GameObject object)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
		{
			return Collections.emptyList();
		}
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		List<Playable> result = new LazyArrayList<>(64);
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isPlayable() || (obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						result.add((Playable) obj);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method getAroundPlayables.
	 * @param object GameObject
	 * @param radius int
	 * @param height int
	 * @return List<Playable>
	 */
	public static List<Playable> getAroundPlayables(GameObject object, int radius, int height)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
		{
			return Collections.emptyList();
		}
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int sqrad = radius * radius;
		List<Playable> result = new LazyArrayList<>(64);
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isPlayable() || (obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						if (Math.abs(obj.getZ() - oz) > height)
						{
							continue;
						}
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
						{
							continue;
						}
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
						{
							continue;
						}
						if (((dx * dx) + (dy * dy)) > sqrad)
						{
							continue;
						}
						result.add((Playable) obj);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method getAroundPlayers.
	 * @param object GameObject
	 * @return List<Player>
	 */
	public static List<Player> getAroundPlayers(GameObject object)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
		{
			return Collections.emptyList();
		}
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		List<Player> result = new LazyArrayList<>(64);
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isPlayer() || (obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						result.add((Player) obj);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method getAroundPlayers.
	 * @param object GameObject
	 * @param radius int
	 * @param height int
	 * @return List<Player>
	 */
	public static List<Player> getAroundPlayers(GameObject object, int radius, int height)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
		{
			return Collections.emptyList();
		}
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int sqrad = radius * radius;
		List<Player> result = new LazyArrayList<>(64);
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isPlayer() || (obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						if (Math.abs(obj.getZ() - oz) > height)
						{
							continue;
						}
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
						{
							continue;
						}
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
						{
							continue;
						}
						if (((dx * dx) + (dy * dy)) > sqrad)
						{
							continue;
						}
						result.add((Player) obj);
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Method isNeighborsEmpty.
	 * @param region WorldRegion
	 * @return boolean
	 */
	public static boolean isNeighborsEmpty(WorldRegion region)
	{
		for (int x = validX(region.getX() - 1); x <= validX(region.getX() + 1); x++)
		{
			for (int y = validY(region.getY() - 1); y <= validY(region.getY() + 1); y++)
			{
				for (int z = validZ(region.getZ() - 1); z <= validZ(region.getZ() + 1); z++)
				{
					if (!getRegion(x, y, z).isEmpty())
					{
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Method activate.
	 * @param currentRegion WorldRegion
	 */
	public static void activate(WorldRegion currentRegion)
	{
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					getRegion(x, y, z).setActive(true);
				}
			}
		}
	}
	
	/**
	 * Method deactivate.
	 * @param currentRegion WorldRegion
	 */
	public static void deactivate(WorldRegion currentRegion)
	{
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					if (isNeighborsEmpty(getRegion(x, y, z)))
					{
						getRegion(x, y, z).setActive(false);
					}
				}
			}
		}
	}
	
	/**
	 * Method showObjectsToPlayer.
	 * @param player Player
	 */
	public static void showObjectsToPlayer(Player player)
	{
		WorldRegion currentRegion = player.isInObserverMode() ? player.getObserverRegion() : player.getCurrentRegion();
		if (currentRegion == null)
		{
			return;
		}
		int oid = player.getObjectId();
		int rid = player.getReflectionId();
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if ((obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						player.sendPacket(player.addVisibleObject(obj, null));
					}
				}
			}
		}
	}
	
	/**
	 * Method removeObjectsFromPlayer.
	 * @param player Player
	 */
	public static void removeObjectsFromPlayer(Player player)
	{
		WorldRegion currentRegion = player.isInObserverMode() ? player.getObserverRegion() : player.getCurrentRegion();
		if (currentRegion == null)
		{
			return;
		}
		int oid = player.getObjectId();
		int rid = player.getReflectionId();
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if ((obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						player.sendPacket(player.removeVisibleObject(obj, null));
					}
				}
			}
		}
	}
	
	/**
	 * Method removeObjectFromPlayers.
	 * @param object GameObject
	 */
	public static void removeObjectFromPlayers(GameObject object)
	{
		WorldRegion currentRegion = object.getCurrentRegion();
		if (currentRegion == null)
		{
			return;
		}
		int oid = object.getObjectId();
		int rid = object.getReflectionId();
		Player p;
		List<L2GameServerPacket> d = null;
		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
		{
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
			{
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
				{
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isPlayer() || (obj.getObjectId() == oid) || (obj.getReflectionId() != rid))
						{
							continue;
						}
						p = (Player) obj;
						p.sendPacket(p.removeVisibleObject(object, d == null ? d = object.deletePacketList() : d));
					}
				}
			}
		}
	}
	
	/**
	 * Method addZone.
	 * @param zone Zone
	 */
	static void addZone(Zone zone)
	{
		Reflection reflection = zone.getReflection();
		Territory territory = zone.getTerritory();
		if (territory == null)
		{
			_log.info("World: zone - " + zone.getName() + " not has territory.");
			return;
		}
		for (int x = validX(regionX(territory.getXmin())); x <= validX(regionX(territory.getXmax())); x++)
		{
			for (int y = validY(regionY(territory.getYmin())); y <= validY(regionY(territory.getYmax())); y++)
			{
				for (int z = validZ(regionZ(territory.getZmin())); z <= validZ(regionZ(territory.getZmax())); z++)
				{
					WorldRegion region = getRegion(x, y, z);
					region.addZone(zone);
					for (GameObject obj : region)
					{
						if (!obj.isCreature() || (obj.getReflection() != reflection))
						{
							continue;
						}
						((Creature) obj).updateZones();
					}
				}
			}
		}
	}
	
	/**
	 * Method removeZone.
	 * @param zone Zone
	 */
	static void removeZone(Zone zone)
	{
		Reflection reflection = zone.getReflection();
		Territory territory = zone.getTerritory();
		if (territory == null)
		{
			_log.info("World: zone - " + zone.getName() + " not has territory.");
			return;
		}
		for (int x = validX(regionX(territory.getXmin())); x <= validX(regionX(territory.getXmax())); x++)
		{
			for (int y = validY(regionY(territory.getYmin())); y <= validY(regionY(territory.getYmax())); y++)
			{
				for (int z = validZ(regionZ(territory.getZmin())); z <= validZ(regionZ(territory.getZmax())); z++)
				{
					WorldRegion region = getRegion(x, y, z);
					region.removeZone(zone);
					for (GameObject obj : region)
					{
						if (!obj.isCreature() || (obj.getReflection() != reflection))
						{
							continue;
						}
						((Creature) obj).updateZones();
					}
				}
			}
		}
	}
	
	/**
	 * Method getZones.
	 * @param inside List<Zone>
	 * @param loc Location
	 * @param reflection Reflection
	 */
	public static void getZones(List<Zone> inside, Location loc, Reflection reflection)
	{
		WorldRegion region = getRegion(loc);
		Zone[] zones = region.getZones();
		if (zones.length == 0)
		{
			return;
		}
		for (Zone zone : zones)
		{
			if (zone.checkIfInZone(loc.x, loc.y, loc.z, reflection))
			{
				inside.add(zone);
			}
		}
	}
	
	/**
	 * Method isWater.
	 * @param loc Location
	 * @param reflection Reflection
	 * @return boolean
	 */
	public static boolean isWater(Location loc, Reflection reflection)
	{
		return getWater(loc, reflection) != null;
	}
	
	/**
	 * Method getWater.
	 * @param loc Location
	 * @param reflection Reflection
	 * @return Zone
	 */
	public static Zone getWater(Location loc, Reflection reflection)
	{
		WorldRegion region = getRegion(loc);
		Zone[] zones = region.getZones();
		if (zones.length == 0)
		{
			return null;
		}
		for (Zone zone : zones)
		{
			if ((zone != null) && (zone.getType() == ZoneType.water) && zone.checkIfInZone(loc.x, loc.y, loc.z, reflection))
			{
				return zone;
			}
		}
		return null;
	}
	
	/**
	 * Method getStats.
	 * @return int[]
	 */
	public static int[] getStats()
	{
		WorldRegion region;
		int[] ret = new int[32];
		for (int x = 0; x <= REGIONS_X; x++)
		{
			for (int y = 0; y <= REGIONS_Y; y++)
			{
				for (int z = 0; z <= REGIONS_Z; z++)
				{
					ret[0]++;
					region = _worldRegions[x][y][z];
					if (region != null)
					{
						if (region.isActive())
						{
							ret[1]++;
						}
						else
						{
							ret[2]++;
						}
						for (GameObject obj : region)
						{
							ret[10]++;
							if (obj.isCreature())
							{
								ret[11]++;
								if (obj.isPlayer())
								{
									ret[12]++;
									Player p = (Player) obj;
									if (p.isInOfflineMode())
									{
										ret[13]++;
									}
								}
								else if (obj.isNpc())
								{
									ret[14]++;
									if (obj.isMonster())
									{
										ret[16]++;
										if (obj.isMinion())
										{
											ret[17]++;
										}
									}
									NpcInstance npc = (NpcInstance) obj;
									if (npc.hasAI())
									{
										if (npc.getAI().isActive())
										{
											ret[15]++;
										}
									}
								}
								else if (obj.isPlayable())
								{
									ret[18]++;
								}
								else if (obj.isDoor())
								{
									ret[19]++;
								}
							}
							else if (obj.isItem())
							{
								ret[20]++;
							}
						}
					}
					else
					{
						ret[3]++;
					}
				}
			}
		}
		return ret;
	}

	public static List<NpcInstance> getAroundNpcCor(Location loc, WorldRegion region, int reflect, int radius, int height)
	{
		WorldRegion currentRegion = region;
		if (currentRegion == null)
			return new ArrayList<NpcInstance>(0);

		int rid = reflect;
		int ox = loc.x;
		int oy = loc.y;
		int oz = loc.z;
		int sqrad = radius * radius;

		List<NpcInstance> result = new ArrayList<NpcInstance>(64);

		for (int x = validX(currentRegion.getX() - 1); x <= validX(currentRegion.getX() + 1); x++)
			for (int y = validY(currentRegion.getY() - 1); y <= validY(currentRegion.getY() + 1); y++)
				for (int z = validZ(currentRegion.getZ() - 1); z <= validZ(currentRegion.getZ() + 1); z++)
					for (GameObject obj : getRegion(x, y, z))
					{
						if (!obj.isNpc() || obj.getReflectionId() != rid)
							continue;
						if (Math.abs(obj.getZ() - oz) > height)
							continue;
						int dx = Math.abs(obj.getX() - ox);
						if (dx > radius)
							continue;
						int dy = Math.abs(obj.getY() - oy);
						if (dy > radius)
							continue;
						if (dx * dx + dy * dy > sqrad)
							continue;

						result.add((NpcInstance) obj);
					}
		return result;
	}
}
