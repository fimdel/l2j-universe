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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class JumpWay
{
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _points.
	 */
	private final TIntObjectHashMap<JumpPoint> _points;
	
	/**
	 * Constructor for JumpWay.
	 * @param id int
	 */
	public JumpWay(int id)
	{
		_id = id;
		_points = new TIntObjectHashMap<>();
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
	 * Method getPoints.
	 * @return JumpPoint[]
	 */
	public JumpPoint[] getPoints()
	{
		return _points.values(new JumpPoint[_points.size()]);
	}
	
	/**
	 * Method getJumpPoint.
	 * @param nextWayId int
	 * @return JumpPoint
	 */
	public JumpPoint getJumpPoint(int nextWayId)
	{
		return _points.get(nextWayId);
	}
	
	/**
	 * Method addPoint.
	 * @param point JumpPoint
	 */
	public void addPoint(JumpPoint point)
	{
		_points.put(point.getNextWayId(), point);
	}
}
