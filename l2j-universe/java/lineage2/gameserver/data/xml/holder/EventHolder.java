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
package lineage2.gameserver.data.xml.holder;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.GlobalEvent;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.impl.TreeIntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class EventHolder extends AbstractHolder
{
	/**
	 * Field _instance.
	 */
	private static final EventHolder _instance = new EventHolder();
	/**
	 * Field _events.
	 */
	private final IntObjectMap<GlobalEvent> _events = new TreeIntObjectMap<>();
	
	/**
	 * Method getInstance.
	 * @return EventHolder
	 */
	public static EventHolder getInstance()
	{
		return _instance;
	}
	
	/**
	 * Method addEvent.
	 * @param type EventType
	 * @param event GlobalEvent
	 */
	public void addEvent(EventType type, GlobalEvent event)
	{
		_events.put(type.step() + event.getId(), event);
	}
	
	/**
	 * Method getEvent.
	 * @param type EventType
	 * @param id int
	 * @return E
	 */
	@SuppressWarnings("unchecked")
	public <E extends GlobalEvent> E getEvent(EventType type, int id)
	{
		return (E) _events.get(type.step() + id);
	}
	
	/**
	 * Method findEvent.
	 * @param player Player
	 */
	public void findEvent(Player player)
	{
		for (GlobalEvent event : _events.values())
		{
			if (event.isParticle(player))
			{
				player.addEvent(event);
			}
		}
	}
	
	/**
	 * Method callInit.
	 */
	public void callInit()
	{
		for (GlobalEvent event : _events.values())
		{
			event.initEvent();
		}
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	@Override
	public int size()
	{
		return _events.size();
	}
	
	/**
	 * Method clear.
	 */
	@Override
	public void clear()
	{
		_events.clear();
	}
}
