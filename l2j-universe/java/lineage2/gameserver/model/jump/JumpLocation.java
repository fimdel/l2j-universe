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
package lineage2.gameserver.model.jump;

import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class JumpLocation
{
	/**
	 * Field zoneName.
	 */
	private final String zoneName;
	/**
	 * Field id.
	 */
	private final int id;
	/**
	 * Field is_last.
	 */
	private final boolean is_last;
	/**
	 * Field routes.
	 */
	private final int[] routes;
	/**
	 * Field location.
	 */
	private final Location location;
	
	/**
	 * Constructor for JumpLocation.
	 * @param zoneName String
	 * @param id int
	 * @param is_last boolean
	 * @param routes int[]
	 * @param location Location
	 */
	public JumpLocation(String zoneName, int id, boolean is_last, int[] routes, Location location)
	{
		this.zoneName = zoneName;
		this.id = id;
		this.is_last = is_last;
		this.routes = routes;
		this.location = location;
	}
	
	/**
	 * Method getZoneName.
	 * @return String
	 */
	public String getZoneName()
	{
		return zoneName;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Method isLast.
	 * @return boolean
	 */
	public boolean isLast()
	{
		return is_last;
	}
	
	/**
	 * Method getRoutes.
	 * @return int[]
	 */
	public int[] getRoutes()
	{
		return routes;
	}
	
	/**
	 * Method getLocation.
	 * @return Location
	 */
	public Location getLocation()
	{
		return location;
	}
	
	/**
	 * Method getX.
	 * @return int
	 */
	public int getX()
	{
		return location.getX();
	}
	
	/**
	 * Method getY.
	 * @return int
	 */
	public int getY()
	{
		return location.getY();
	}
	
	/**
	 * Method getZ.
	 * @return int
	 */
	public int getZ()
	{
		return location.getZ();
	}
}
