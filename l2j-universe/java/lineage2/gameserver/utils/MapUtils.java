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

import lineage2.gameserver.Config;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.World;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MapUtils
{
	/**
	 * Constructor for MapUtils.
	 */
	private MapUtils()
	{
	}
	
	/**
	 * Method regionX.
	 * @param o GameObject
	 * @return int
	 */
	public static int regionX(GameObject o)
	{
		return regionX(o.getX());
	}
	
	/**
	 * Method regionY.
	 * @param o GameObject
	 * @return int
	 */
	public static int regionY(GameObject o)
	{
		return regionY(o.getY());
	}
	
	/**
	 * Method regionX.
	 * @param x int
	 * @return int
	 */
	public static int regionX(int x)
	{
		return ((x - World.MAP_MIN_X) >> 15) + Config.GEO_X_FIRST;
	}
	
	/**
	 * Method regionY.
	 * @param y int
	 * @return int
	 */
	public static int regionY(int y)
	{
		return ((y - World.MAP_MIN_Y) >> 15) + Config.GEO_Y_FIRST;
	}
}
