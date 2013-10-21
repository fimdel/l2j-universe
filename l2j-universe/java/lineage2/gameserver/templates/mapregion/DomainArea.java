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

import lineage2.gameserver.model.Territory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DomainArea implements RegionData
{
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _territory.
	 */
	private final Territory _territory;
	
	/**
	 * Constructor for DomainArea.
	 * @param id int
	 * @param territory Territory
	 */
	public DomainArea(int id, Territory territory)
	{
		_id = id;
		_territory = territory;
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
	 * Method getTerritory.
	 * @return Territory * @see lineage2.gameserver.templates.mapregion.RegionData#getTerritory()
	 */
	@Override
	public Territory getTerritory()
	{
		return _territory;
	}
}
