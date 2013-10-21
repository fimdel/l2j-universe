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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class EventOwner implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _events.
	 */
	private final Set<GlobalEvent> _events = new HashSet<>(2);
	
	/**
	 * Method getEvent.
	 * @param eventClass Class<E>
	 * @return E
	 */
	@SuppressWarnings("unchecked")
	public <E extends GlobalEvent> E getEvent(Class<E> eventClass)
	{
		for (GlobalEvent e : _events)
		{
			if (e.getClass() == eventClass)
			{
				return (E) e;
			}
			if (eventClass.isAssignableFrom(e.getClass()))
			{
				return (E) e;
			}
		}
		return null;
	}
	
	/**
	 * Method addEvent.
	 * @param event GlobalEvent
	 */
	public void addEvent(GlobalEvent event)
	{
		_events.add(event);
	}
	
	/**
	 * Method removeEvent.
	 * @param event GlobalEvent
	 */
	public void removeEvent(GlobalEvent event)
	{
		_events.remove(event);
	}
	
	/**
	 * Method getEvents.
	 * @return Set<GlobalEvent>
	 */
	public Set<GlobalEvent> getEvents()
	{
		return _events;
	}
}
