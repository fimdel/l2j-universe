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

import java.util.Map;

import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.base.Race;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RestartArea implements RegionData
{
	/**
	 * Field _territory.
	 */
	private final Territory _territory;
	/**
	 * Field _restarts.
	 */
	private final Map<Race, RestartPoint> _restarts;
	
	/**
	 * Constructor for RestartArea.
	 * @param territory Territory
	 * @param restarts Map<Race,RestartPoint>
	 */
	public RestartArea(Territory territory, Map<Race, RestartPoint> restarts)
	{
		_territory = territory;
		_restarts = restarts;
	}
	
	/**
	 * Method getTerritory.
	 * @return Territory * @see lineage2.gameserver.templates.mapregion.RegionData#getTerritory()
	 */
	@Override
	public Territory getTerritory()
	{
		return _territory;
	}
	
	/**
	 * Method getRestartPoint.
	 * @return Map<Race,RestartPoint>
	 */
	public Map<Race, RestartPoint> getRestartPoint()
	{
		return _restarts;
	}
}
