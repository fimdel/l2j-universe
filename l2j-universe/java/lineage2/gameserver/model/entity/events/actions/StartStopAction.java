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
package lineage2.gameserver.model.entity.events.actions;

import lineage2.gameserver.model.entity.events.EventAction;
import lineage2.gameserver.model.entity.events.GlobalEvent;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StartStopAction implements EventAction
{
	/**
	 * Field EVENT. (value is ""event"")
	 */
	public static final String EVENT = "event";
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _start.
	 */
	private final boolean _start;
	
	/**
	 * Constructor for StartStopAction.
	 * @param name String
	 * @param start boolean
	 */
	public StartStopAction(String name, boolean start)
	{
		_name = name;
		_start = start;
	}
	
	/**
	 * Method call.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.EventAction#call(GlobalEvent)
	 */
	@Override
	public void call(GlobalEvent event)
	{
		event.action(_name, _start);
	}
}
