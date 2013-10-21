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
package lineage2.gameserver.utils;

import java.io.Serializable;

import lineage2.commons.geometry.Point3D;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.World;
import lineage2.gameserver.templates.spawn.SpawnRange;

import org.dom4j.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Location extends Point3D implements SpawnRange, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field h.
	 */
	public int h;
	
	/**
	 * Constructor for Location.
	 */
	public Location()
	{
	}
	
	/**
	 * Constructor for Location.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param heading int
	 */
	public Location(int x, int y, int z, int heading)
	{
		super(x, y, z);
		h = heading;
	}
	
	/**
	 * Constructor for Location.
	 * @param x int
	 * @param y int
	 * @param z int
	 */
	public Location(int x, int y, int z)
	{
		this(x, y, z, 0);
	}
	
	/**
	 * Constructor for Location.
	 * @param obj GameObject
	 */
	public Location(GameObject obj)
	{
		this(obj.getX(), obj.getY(), obj.getZ(), obj.getHeading());
	}
	
	/**
	 * Method changeZ.
	 * @param zDiff int
	 * @return Location
	 */
	public Location changeZ(int zDiff)
	{
		z += zDiff;
		return this;
	}
	
	/**
	 * Method correctGeoZ.
	 * @return Location
	 */
	public Location correctGeoZ()
	{
		z = GeoEngine.getHeight(x, y, z, 0);
		return this;
	}
	
	/**
	 * Method correctGeoZ.
	 * @param refIndex int
	 * @return Location
	 */
	public Location correctGeoZ(int refIndex)
	{
		z = GeoEngine.getHeight(x, y, z, refIndex);
		return this;
	}
	
	/**
	 * Method setX.
	 * @param x int
	 * @return Location
	 */
	public Location setX(int x)
	{
		this.x = x;
		return this;
	}
	
	/**
	 * Method setY.
	 * @param y int
	 * @return Location
	 */
	public Location setY(int y)
	{
		this.y = y;
		return this;
	}
	
	/**
	 * Method setZ.
	 * @param z int
	 * @return Location
	 */
	public Location setZ(int z)
	{
		this.z = z;
		return this;
	}
	
	/**
	 * Method setH.
	 * @param h int
	 * @return Location
	 */
	public Location setH(int h)
	{
		this.h = h;
		return this;
	}
	
	/**
	 * Method set.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return Location
	 */
	public Location set(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/**
	 * Method set.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param h int
	 * @return Location
	 */
	public Location set(int x, int y, int z, int h)
	{
		set(x, y, z);
		this.h = h;
		return this;
	}
	
	/**
	 * Method set.
	 * @param loc Location
	 * @return Location
	 */
	public Location set(Location loc)
	{
		x = loc.x;
		y = loc.y;
		z = loc.z;
		h = loc.h;
		return this;
	}
	
	/**
	 * Method world2geo.
	 * @return Location
	 */
	public Location world2geo()
	{
		x = (x - World.MAP_MIN_X) >> 4;
		y = (y - World.MAP_MIN_Y) >> 4;
		return this;
	}
	
	/**
	 * Method geo2world.
	 * @return Location
	 */
	public Location geo2world()
	{
		x = (x << 4) + World.MAP_MIN_X + 8;
		y = (y << 4) + World.MAP_MIN_Y + 8;
		return this;
	}
	
	/**
	 * Method distance.
	 * @param loc Location
	 * @return double
	 */
	public double distance(Location loc)
	{
		return distance(loc.x, loc.y);
	}
	
	/**
	 * Method distance.
	 * @param x int
	 * @param y int
	 * @return double
	 */
	public double distance(int x, int y)
	{
		long dx = this.x - x;
		long dy = this.y - y;
		return Math.sqrt((dx * dx) + (dy * dy));
	}
	
	/**
	 * Method distance3D.
	 * @param loc Location
	 * @return double
	 */
	public double distance3D(Location loc)
	{
		return distance3D(loc.x, loc.y, loc.z);
	}
	
	/**
	 * Method distance3D.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return double
	 */
	public double distance3D(int x, int y, int z)
	{
		long dx = this.x - x;
		long dy = this.y - y;
		long dz = this.z - z;
		return Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
	}
	
	/**
	 * Method clone.
	 * @return Location
	 */
	@Override
	public Location clone()
	{
		return new Location(x, y, z, h);
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public final String toString()
	{
		return x + "," + y + "," + z + "," + h;
	}
	
	/**
	 * Method isNull.
	 * @return boolean
	 */
	public boolean isNull()
	{
		return (x == 0) || (y == 0) || (z == 0);
	}
	
	/**
	 * Method toXYZString.
	 * @return String
	 */
	public final String toXYZString()
	{
		return x + " " + y + " " + z;
	}
	
	/**
	 * Method parseLoc.
	 * @param s String
	 * @return Location * @throws IllegalArgumentException
	 */
	public static Location parseLoc(String s) throws IllegalArgumentException
	{
		String[] xyzh = s.split("[\\s,;]+");
		if (xyzh.length < 3)
		{
			throw new IllegalArgumentException("Can't parse location from string: " + s);
		}
		int x = Integer.parseInt(xyzh[0]);
		int y = Integer.parseInt(xyzh[1]);
		int z = Integer.parseInt(xyzh[2]);
		int h = xyzh.length < 4 ? 0 : Integer.parseInt(xyzh[3]);
		return new Location(x, y, z, h);
	}
	
	/**
	 * Method parse.
	 * @param element Element
	 * @return Location
	 */
	public static Location parse(Element element)
	{
		int x = Integer.parseInt(element.attributeValue("x"));
		int y = Integer.parseInt(element.attributeValue("y"));
		int z = Integer.parseInt(element.attributeValue("z"));
		int h = element.attributeValue("h") == null ? 0 : Integer.parseInt(element.attributeValue("h"));
		return new Location(x, y, z, h);
	}
	
	/**
	 * Method findFrontPosition.
	 * @param obj GameObject
	 * @param obj2 GameObject
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	public static Location findFrontPosition(GameObject obj, GameObject obj2, int radiusmin, int radiusmax)
	{
		if ((radiusmax == 0) || (radiusmax < radiusmin))
		{
			return new Location(obj);
		}
		double collision = obj.getColRadius() + obj2.getColRadius();
		int randomRadius, randomAngle, tempz;
		int minangle = 0;
		int maxangle = 360;
		if (!obj.equals(obj2))
		{
			double angle = PositionUtils.calculateAngleFrom(obj, obj2);
			minangle = (int) angle - 45;
			maxangle = (int) angle + 45;
		}
		Location pos = new Location();
		for (int i = 0; i < 100; i++)
		{
			randomRadius = Rnd.get(radiusmin, radiusmax);
			randomAngle = Rnd.get(minangle, maxangle);
			pos.x = obj.getX() + (int) ((collision + randomRadius) * Math.cos(Math.toRadians(randomAngle)));
			pos.y = obj.getY() + (int) ((collision + randomRadius) * Math.sin(Math.toRadians(randomAngle)));
			pos.z = obj.getZ();
			tempz = GeoEngine.getHeight(pos.x, pos.y, pos.z, obj.getGeoIndex());
			if ((Math.abs(pos.z - tempz) < 200) && (GeoEngine.getNSWE(pos.x, pos.y, tempz, obj.getGeoIndex()) == GeoEngine.NSWE_ALL))
			{
				pos.z = tempz;
				if (!obj.equals(obj2))
				{
					pos.h = PositionUtils.getHeadingTo(pos, obj2.getLoc());
				}
				else
				{
					pos.h = obj.getHeading();
				}
				return pos;
			}
		}
		return new Location(obj);
	}
	
	/**
	 * Method findAroundPosition.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param radiusmin int
	 * @param radiusmax int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location findAroundPosition(int x, int y, int z, int radiusmin, int radiusmax, int geoIndex)
	{
		Location pos;
		int tempz;
		for (int i = 0; i < 100; i++)
		{
			pos = Location.coordsRandomize(x, y, z, 0, radiusmin, radiusmax);
			tempz = GeoEngine.getHeight(pos.x, pos.y, pos.z, geoIndex);
			if (GeoEngine.canMoveToCoord(x, y, z, pos.x, pos.y, tempz, geoIndex) && GeoEngine.canMoveToCoord(pos.x, pos.y, tempz, x, y, z, geoIndex))
			{
				pos.z = tempz;
				return pos;
			}
		}
		return new Location(x, y, z);
	}
	
	/**
	 * Method findAroundPosition.
	 * @param loc Location
	 * @param radius int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location findAroundPosition(Location loc, int radius, int geoIndex)
	{
		return findAroundPosition(loc.x, loc.y, loc.z, 0, radius, geoIndex);
	}
	
	/**
	 * Method findAroundPosition.
	 * @param loc Location
	 * @param radiusmin int
	 * @param radiusmax int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location findAroundPosition(Location loc, int radiusmin, int radiusmax, int geoIndex)
	{
		return findAroundPosition(loc.x, loc.y, loc.z, radiusmin, radiusmax, geoIndex);
	}
	
	/**
	 * Method findAroundPosition.
	 * @param obj GameObject
	 * @param loc Location
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	public static Location findAroundPosition(GameObject obj, Location loc, int radiusmin, int radiusmax)
	{
		return findAroundPosition(loc.x, loc.y, loc.z, radiusmin, radiusmax, obj.getGeoIndex());
	}
	
	/**
	 * Method findAroundPosition.
	 * @param obj GameObject
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	public static Location findAroundPosition(GameObject obj, int radiusmin, int radiusmax)
	{
		return findAroundPosition(obj, obj.getLoc(), radiusmin, radiusmax);
	}
	
	/**
	 * Method findAroundPosition.
	 * @param obj GameObject
	 * @param radius int
	 * @return Location
	 */
	public static Location findAroundPosition(GameObject obj, int radius)
	{
		return findAroundPosition(obj, 0, radius);
	}
	
	/**
	 * Method findPointToStay.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param radiusmin int
	 * @param radiusmax int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location findPointToStay(int x, int y, int z, int radiusmin, int radiusmax, int geoIndex)
	{
		Location pos;
		int tempz;
		for (int i = 0; i < 100; i++)
		{
			pos = Location.coordsRandomize(x, y, z, 0, radiusmin, radiusmax);
			tempz = GeoEngine.getHeight(pos.x, pos.y, pos.z, geoIndex);
			if ((Math.abs(pos.z - tempz) < 200) && (GeoEngine.getNSWE(pos.x, pos.y, tempz, geoIndex) == GeoEngine.NSWE_ALL))
			{
				pos.z = tempz;
				return pos;
			}
		}
		return new Location(x, y, z);
	}
	
	/**
	 * Method findPointToStay.
	 * @param loc Location
	 * @param radius int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location findPointToStay(Location loc, int radius, int geoIndex)
	{
		return findPointToStay(loc.x, loc.y, loc.z, 0, radius, geoIndex);
	}
	
	/**
	 * Method findPointToStay.
	 * @param loc Location
	 * @param radiusmin int
	 * @param radiusmax int
	 * @param geoIndex int
	 * @return Location
	 */
	public static Location findPointToStay(Location loc, int radiusmin, int radiusmax, int geoIndex)
	{
		return findPointToStay(loc.x, loc.y, loc.z, radiusmin, radiusmax, geoIndex);
	}
	
	/**
	 * Method findPointToStay.
	 * @param obj GameObject
	 * @param loc Location
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	public static Location findPointToStay(GameObject obj, Location loc, int radiusmin, int radiusmax)
	{
		return findPointToStay(loc.x, loc.y, loc.z, radiusmin, radiusmax, obj.getGeoIndex());
	}
	
	/**
	 * Method findPointToStay.
	 * @param obj GameObject
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	public static Location findPointToStay(GameObject obj, int radiusmin, int radiusmax)
	{
		return findPointToStay(obj, obj.getLoc(), radiusmin, radiusmax);
	}
	
	/**
	 * Method findPointToStay.
	 * @param obj GameObject
	 * @param radius int
	 * @return Location
	 */
	public static Location findPointToStay(GameObject obj, int radius)
	{
		return findPointToStay(obj, 0, radius);
	}
	
	/**
	 * Method coordsRandomize.
	 * @param loc Location
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	public static Location coordsRandomize(Location loc, int radiusmin, int radiusmax)
	{
		return coordsRandomize(loc.x, loc.y, loc.z, loc.h, radiusmin, radiusmax);
	}
	
	/**
	 * Method coordsRandomize.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param heading int
	 * @param radiusmin int
	 * @param radiusmax int
	 * @return Location
	 */
	public static Location coordsRandomize(int x, int y, int z, int heading, int radiusmin, int radiusmax)
	{
		if ((radiusmax == 0) || (radiusmax < radiusmin))
		{
			return new Location(x, y, z, heading);
		}
		int radius = Rnd.get(radiusmin, radiusmax);
		double angle = Rnd.nextDouble() * 2 * Math.PI;
		return new Location((int) (x + (radius * Math.cos(angle))), (int) (y + (radius * Math.sin(angle))), z, heading);
	}
	
	/**
	 * Method findNearest.
	 * @param creature Creature
	 * @param locs Location[]
	 * @return Location
	 */
	public static Location findNearest(Creature creature, Location[] locs)
	{
		Location defloc = null;
		for (Location loc : locs)
		{
			if (defloc == null)
			{
				defloc = loc;
			}
			else if (creature.getDistance(loc) < creature.getDistance(defloc))
			{
				defloc = loc;
			}
		}
		return defloc;
	}
	
	/**
	 * Method getRandomHeading.
	 * @return int
	 */
	public static int getRandomHeading()
	{
		return Rnd.get(65535);
	}
	
	/**
	 * Method getRandomLoc.
	 * @param ref int
	 * @return Location * @see lineage2.gameserver.templates.spawn.SpawnRange#getRandomLoc(int)
	 */
	@Override
	public Location getRandomLoc(int ref)
	{
		return this;
	}
}
