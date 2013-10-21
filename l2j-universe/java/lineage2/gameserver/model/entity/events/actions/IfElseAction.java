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

import java.util.Collections;
import java.util.List;

import lineage2.gameserver.model.entity.events.EventAction;
import lineage2.gameserver.model.entity.events.GlobalEvent;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class IfElseAction implements EventAction
{
	/**
	 * Field _name.
	 */
	private final String _name;
	/**
	 * Field _reverse.
	 */
	private final boolean _reverse;
	/**
	 * Field _ifList.
	 */
	private List<EventAction> _ifList = Collections.emptyList();
	/**
	 * Field _elseList.
	 */
	private List<EventAction> _elseList = Collections.emptyList();
	
	/**
	 * Constructor for IfElseAction.
	 * @param name String
	 * @param reverse boolean
	 */
	public IfElseAction(String name, boolean reverse)
	{
		_name = name;
		_reverse = reverse;
	}
	
	/**
	 * Method call.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.EventAction#call(GlobalEvent)
	 */
	@Override
	public void call(GlobalEvent event)
	{
		List<EventAction> list = (_reverse ? !event.ifVar(_name) : event.ifVar(_name)) ? _ifList : _elseList;
		for (EventAction action : list)
		{
			action.call(event);
		}
	}
	
	/**
	 * Method setIfList.
	 * @param ifList List<EventAction>
	 */
	public void setIfList(List<EventAction> ifList)
	{
		_ifList = ifList;
	}
	
	/**
	 * Method setElseList.
	 * @param elseList List<EventAction>
	 */
	public void setElseList(List<EventAction> elseList)
	{
		_elseList = elseList;
	}
}
