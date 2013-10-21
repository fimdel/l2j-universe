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
package lineage2.gameserver.model.entity.events;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum EventType
{
	/**
	 * Field MAIN_EVENT.
	 */
	MAIN_EVENT,
	/**
	 * Field SIEGE_EVENT.
	 */
	SIEGE_EVENT,
	/**
	 * Field PVP_EVENT.
	 */
	PVP_EVENT,
	/**
	 * Field BOAT_EVENT.
	 */
	BOAT_EVENT,
	/**
	 * Field FUN_EVENT.
	 */
	FUN_EVENT,
	/**
	 * Field SHUTTLE_EVENT.
	 */
	SHUTTLE_EVENT;
	/**
	 * Field _step.
	 */
	private int _step;
	
	/**
	 * Constructor for EventType.
	 */
	EventType()
	{
		_step = ordinal() * 1000;
	}
	
	/**
	 * Method step.
	 * @return int
	 */
	public int step()
	{
		return _step;
	}
}
