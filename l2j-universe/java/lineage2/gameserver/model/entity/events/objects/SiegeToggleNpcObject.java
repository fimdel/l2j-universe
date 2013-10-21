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
package lineage2.gameserver.model.entity.events.objects;

import java.util.Set;

import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.residences.SiegeToggleNpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SiegeToggleNpcObject implements SpawnableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _toggleNpc.
	 */
	private final SiegeToggleNpcInstance _toggleNpc;
	/**
	 * Field _location.
	 */
	private final Location _location;
	
	/**
	 * Constructor for SiegeToggleNpcObject.
	 * @param id int
	 * @param fakeNpcId int
	 * @param loc Location
	 * @param hp int
	 * @param set Set<String>
	 */
	public SiegeToggleNpcObject(int id, int fakeNpcId, Location loc, int hp, Set<String> set)
	{
		_location = loc;
		_toggleNpc = (SiegeToggleNpcInstance) NpcHolder.getInstance().getTemplate(id).getNewInstance();
		_toggleNpc.initFake(fakeNpcId);
		_toggleNpc.setMaxHp(hp);
		_toggleNpc.setZoneList(set);
	}
	
	/**
	 * Method spawnObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#spawnObject(GlobalEvent)
	 */
	@Override
	public void spawnObject(GlobalEvent event)
	{
		_toggleNpc.decayFake();
		if (event.isInProgress())
		{
			_toggleNpc.addEvent(event);
		}
		else
		{
			_toggleNpc.removeEvent(event);
		}
		_toggleNpc.setCurrentHp(_toggleNpc.getMaxHp(), true);
		_toggleNpc.spawnMe(_location);
	}
	
	/**
	 * Method despawnObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#despawnObject(GlobalEvent)
	 */
	@Override
	public void despawnObject(GlobalEvent event)
	{
		_toggleNpc.removeEvent(event);
		_toggleNpc.decayFake();
		_toggleNpc.decayMe();
	}
	
	/**
	 * Method refreshObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#refreshObject(GlobalEvent)
	 */
	@Override
	public void refreshObject(GlobalEvent event)
	{
	}
	
	/**
	 * Method getToggleNpc.
	 * @return SiegeToggleNpcInstance
	 */
	public SiegeToggleNpcInstance getToggleNpc()
	{
		return _toggleNpc;
	}
	
	/**
	 * Method isAlive.
	 * @return boolean
	 */
	public boolean isAlive()
	{
		return _toggleNpc.isVisible();
	}
}
