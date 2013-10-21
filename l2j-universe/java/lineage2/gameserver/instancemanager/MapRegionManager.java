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

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.World;
import lineage2.gameserver.templates.mapregion.RegionData;
import lineage2.gameserver.utils.Location;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MapRegionManager extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static MapRegionManager _instance;
	
	/**
	 * Method getInstance.
	 * @return MapRegionManager
	 */
	public static MapRegionManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new MapRegionManager();
		}
		return _instance;
	}
	
	/**
	 * Field map.
	 */
	private final RegionData[][][] map = new RegionData[World.WORLD_SIZE_X][World.WORLD_SIZE_Y][0];
	
	/**
	 * Constructor for MapRegionManager.
	 */
	private MapRegionManager()
	{
	}
	
	/**
	 * Method regionX.
	 * @param x int
	 * @return int
	 */
	private int regionX(int x)
	{
		return ((x - World.MAP_MIN_X) >> 15);
	}
	
	/**
	 * Method regionY.
	 * @param y int
	 * @return int
	 */
	private int regionY(int y)
	{
		return ((y - World.MAP_MIN_Y) >> 15);
	}
	
	/**
	 * Method addRegionData.
	 * @param rd RegionData
	 */
	public void addRegionData(RegionData rd)
	{
		for (int x = regionX(rd.getTerritory().getXmin()); x <= regionX(rd.getTerritory().getXmax()); x++)
		{
			for (int y = regionY(rd.getTerritory().getYmin()); y <= regionY(rd.getTerritory().getYmax()); y++)
			{
				map[x][y] = ArrayUtils.add(map[x][y], rd);
			}
		}
	}
	
	/**
	 * Method getRegionData.
	 * @param clazz Class<T>
	 * @param o GameObject
	 * @return T
	 */
	public <T extends RegionData> T getRegionData(Class<T> clazz, GameObject o)
	{
		return getRegionData(clazz, o.getX(), o.getY(), o.getZ());
	}
	
	/**
	 * Method getRegionData.
	 * @param clazz Class<T>
	 * @param loc Location
	 * @return T
	 */
	public <T extends RegionData> T getRegionData(Class<T> clazz, Location loc)
	{
		return getRegionData(clazz, loc.getX(), loc.getY(), loc.getZ());
	}
	
	/**
	 * Method getRegionData.
	 * @param clazz Class<T>
	 * @param x int
	 * @param y int
	 * @param z int
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public <T extends RegionData> T getRegionData(Class<T> clazz, int x, int y, int z)
	{
		for (RegionData rd : map[regionX(x)][regionY(y)])
		{
			if (rd.getClass() != clazz)
			{
				continue;
			}
			if (rd.getTerritory().isInside(x, y, z))
			{
				return (T) rd;
			}
		}
		return null;
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return World.WORLD_SIZE_X * World.WORLD_SIZE_Y;
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
	}
}
