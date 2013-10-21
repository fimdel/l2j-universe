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
package lineage2.gameserver.geodata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lineage2.gameserver.Config;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExShowTrace;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GeoMove
{
	/**
	 * Method findPath.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param target Location
	 * @param obj GameObject
	 * @param showTrace boolean
	 * @param geoIndex int
	 * @return List<Location>
	 */
	private static List<Location> findPath(int x, int y, int z, Location target, GameObject obj, boolean showTrace, int geoIndex)
	{
		if (Math.abs(z - target.z) > 256)
		{
			return Collections.emptyList();
		}
		z = GeoEngine.getHeight(x, y, z, geoIndex);
		target.z = GeoEngine.getHeight(target, geoIndex);
		PathFind n = new PathFind(x, y, z, target.x, target.y, target.z, obj, geoIndex);
		if ((n.getPath() == null) || n.getPath().isEmpty())
		{
			return Collections.emptyList();
		}
		List<Location> targetRecorder = new ArrayList<>(n.getPath().size() + 2);
		targetRecorder.add(new Location(x, y, z));
		for (Location p : n.getPath())
		{
			targetRecorder.add(p.geo2world());
		}
		targetRecorder.add(target);
		if (Config.PATH_CLEAN)
		{
			pathClean(targetRecorder, geoIndex);
		}
		if (showTrace && obj.isPlayer() && ((Player) obj).getVarB("trace"))
		{
			Player player = (Player) obj;
			ExShowTrace trace = new ExShowTrace();
			int i = 0;
			for (Location loc : targetRecorder)
			{
				i++;
				if ((i == 1) || (i == targetRecorder.size()))
				{
					continue;
				}
				trace.addTrace(loc.x, loc.y, loc.z + 15, 30000);
			}
			player.sendPacket(trace);
		}
		return targetRecorder;
	}
	
	/**
	 * Method findMovePath.
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param target Location
	 * @param obj GameObject
	 * @param showTrace boolean
	 * @param geoIndex int
	 * @return List<List<Location>>
	 */
	public static List<List<Location>> findMovePath(int x, int y, int z, Location target, GameObject obj, boolean showTrace, int geoIndex)
	{
		return getNodePath(findPath(x, y, z, target, obj, showTrace, geoIndex), geoIndex);
	}
	
	/**
	 * Method getNodePath.
	 * @param path List<Location>
	 * @param geoIndex int
	 * @return List<List<Location>>
	 */
	private static List<List<Location>> getNodePath(List<Location> path, int geoIndex)
	{
		int size = path.size();
		if (size <= 1)
		{
			return Collections.emptyList();
		}
		List<List<Location>> result = new ArrayList<>(size);
		for (int i = 1; i < size; i++)
		{
			Location p2 = path.get(i);
			Location p1 = path.get(i - 1);
			List<Location> moveList = GeoEngine.MoveList(p1.x, p1.y, p1.z, p2.x, p2.y, geoIndex, true);
			if (moveList == null)
			{
				return Collections.emptyList();
			}
			if (!moveList.isEmpty())
			{
				result.add(moveList);
			}
		}
		return result;
	}
	
	/**
	 * Method constructMoveList.
	 * @param begin Location
	 * @param end Location
	 * @return List<Location>
	 */
	public static List<Location> constructMoveList(Location begin, Location end)
	{
		begin.world2geo();
		end.world2geo();
		int diff_x = end.x - begin.x, diff_y = end.y - begin.y, diff_z = end.z - begin.z;
		int dx = Math.abs(diff_x), dy = Math.abs(diff_y), dz = Math.abs(diff_z);
		float steps = Math.max(Math.max(dx, dy), dz);
		if (steps == 0)
		{
			return Collections.emptyList();
		}
		float step_x = diff_x / steps, step_y = diff_y / steps, step_z = diff_z / steps;
		float next_x = begin.x, next_y = begin.y, next_z = begin.z;
		List<Location> result = new ArrayList<>((int) steps + 1);
		result.add(new Location(begin.x, begin.y, begin.z));
		for (int i = 0; i < steps; i++)
		{
			next_x += step_x;
			next_y += step_y;
			next_z += step_z;
			result.add(new Location((int) (next_x + 0.5f), (int) (next_y + 0.5f), (int) (next_z + 0.5f)));
		}
		return result;
	}
	
	/**
	 * Method pathClean.
	 * @param path List<Location>
	 * @param geoIndex int
	 */
	private static void pathClean(List<Location> path, int geoIndex)
	{
		int size = path.size();
		if (size > 2)
		{
			for (int i = 2; i < size; i++)
			{
				Location p3 = path.get(i);
				Location p2 = path.get(i - 1);
				Location p1 = path.get(i - 2);
				if (p1.equals(p2) || p3.equals(p2) || IsPointInLine(p1, p2, p3))
				{
					path.remove(i - 1);
					size--;
					i = Math.max(2, i - 2);
				}
			}
		}
		int current = 0;
		int sub;
		while (current < (path.size() - 2))
		{
			Location one = path.get(current);
			sub = current + 2;
			while (sub < path.size())
			{
				Location two = path.get(sub);
				if (one.equals(two) || GeoEngine.canMoveWithCollision(one.x, one.y, one.z, two.x, two.y, two.z, geoIndex))
				{
					while ((current + 1) < sub)
					{
						path.remove(current + 1);
						sub--;
					}
				}
				sub++;
			}
			current++;
		}
	}
	
	/**
	 * Method IsPointInLine.
	 * @param p1 Location
	 * @param p2 Location
	 * @param p3 Location
	 * @return boolean
	 */
	private static boolean IsPointInLine(Location p1, Location p2, Location p3)
	{
		if (((p1.x == p3.x) && (p3.x == p2.x)) || ((p1.y == p3.y) && (p3.y == p2.y)))
		{
			return true;
		}
		if (((p1.x - p2.x) * (p1.y - p2.y)) == ((p2.x - p3.x) * (p2.y - p3.y)))
		{
			return true;
		}
		return false;
	}
}
