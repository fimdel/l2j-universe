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
package lineage2.gameserver.templates.jump;

import gnu.trove.map.hash.TIntObjectHashMap;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class JumpTrack
{
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _trackWays.
	 */
	private final TIntObjectHashMap<JumpWay> _trackWays;
	/**
	 * Field _startLoc.
	 */
	private final Location _startLoc;
	
	/**
	 * Constructor for JumpTrack.
	 * @param id int
	 * @param startLoc Location
	 */
	public JumpTrack(int id, Location startLoc)
	{
		_id = id;
		_trackWays = new TIntObjectHashMap<>();
		_startLoc = startLoc;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method getWay.
	 * @param id int
	 * @return JumpWay
	 */
	public JumpWay getWay(int id)
	{
		return _trackWays.get(id);
	}
	
	/**
	 * Method addWay.
	 * @param way JumpWay
	 */
	public void addWay(JumpWay way)
	{
		_trackWays.put(way.getId(), way);
	}
	
	/**
	 * Method getStartLocation.
	 * @return Location
	 */
	public Location getStartLocation()
	{
		return _startLoc;
	}
}
