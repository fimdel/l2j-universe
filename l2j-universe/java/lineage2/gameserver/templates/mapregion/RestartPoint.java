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
package lineage2.gameserver.templates.mapregion;

import java.util.List;

import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RestartPoint
{
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _bbs.
	 */
	private final int _bbs;
	/**
	 * Field _msgId.
	 */
	private final int _msgId;
	/**
	 * Field _restartPoints.
	 */
	private final List<Location> _restartPoints;
	/**
	 * Field _PKrestartPoints.
	 */
	private final List<Location> _PKrestartPoints;
	
	/**
	 * Constructor for RestartPoint.
	 * @param name String
	 * @param bbs int
	 * @param msgId int
	 * @param restartPoints List<Location>
	 * @param PKrestartPoints List<Location>
	 */
	public RestartPoint(String name, int bbs, int msgId, List<Location> restartPoints, List<Location> PKrestartPoints)
	{
		_name = name;
		_bbs = bbs;
		_msgId = msgId;
		_restartPoints = restartPoints;
		_PKrestartPoints = PKrestartPoints;
	}
	
	/**
	 * Method getName.
	 * @return String
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Method getBbs.
	 * @return int
	 */
	public int getBbs()
	{
		return _bbs;
	}
	
	/**
	 * Method getMsgId.
	 * @return int
	 */
	public int getMsgId()
	{
		return _msgId;
	}
	
	/**
	 * Method getRestartPoints.
	 * @return List<Location>
	 */
	public List<Location> getRestartPoints()
	{
		return _restartPoints;
	}
	
	/**
	 * Method getPKrestartPoints.
	 * @return List<Location>
	 */
	public List<Location> getPKrestartPoints()
	{
		return _PKrestartPoints;
	}
}
