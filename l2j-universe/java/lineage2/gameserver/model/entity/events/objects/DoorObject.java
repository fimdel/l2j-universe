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

import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.DoorInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DoorObject implements SpawnableObject, InitableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _door.
	 */
	private DoorInstance _door;
	/**
	 * Field _weak.
	 */
	private boolean _weak;
	
	/**
	 * Constructor for DoorObject.
	 * @param id int
	 */
	public DoorObject(int id)
	{
		_id = id;
	}
	
	/**
	 * Method initObject.
	 * @param e GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.InitableObject#initObject(GlobalEvent)
	 */
	@Override
	public void initObject(GlobalEvent e)
	{
		_door = e.getReflection().getDoor(_id);
	}
	
	/**
	 * Method spawnObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#spawnObject(GlobalEvent)
	 */
	@Override
	public void spawnObject(GlobalEvent event)
	{
		refreshObject(event);
	}
	
	/**
	 * Method despawnObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#despawnObject(GlobalEvent)
	 */
	@Override
	public void despawnObject(GlobalEvent event)
	{
		Reflection ref = event.getReflection();
		if (ref == ReflectionManager.DEFAULT)
		{
			refreshObject(event);
		}
		else
		{
		}
	}
	
	/**
	 * Method refreshObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#refreshObject(GlobalEvent)
	 */
	@Override
	public void refreshObject(GlobalEvent event)
	{
		if (!event.isInProgress())
		{
			_door.removeEvent(event);
		}
		else
		{
			_door.addEvent(event);
		}
		if (_door.getCurrentHp() <= 0)
		{
			_door.decayMe();
			_door.spawnMe();
		}
		_door.setCurrentHp(_door.getMaxHp() * (isWeak() ? 0.5 : 1.), true);
		close(event);
	}
	
	/**
	 * Method getUId.
	 * @return int
	 */
	public int getUId()
	{
		return _door.getDoorId();
	}
	
	/**
	 * Method getUpgradeValue.
	 * @return int
	 */
	public int getUpgradeValue()
	{
		return _door.getUpgradeHp();
	}
	
	/**
	 * Method setUpgradeValue.
	 * @param event GlobalEvent
	 * @param val int
	 */
	public void setUpgradeValue(GlobalEvent event, int val)
	{
		_door.setUpgradeHp(val);
		refreshObject(event);
	}
	
	/**
	 * Method open.
	 * @param e GlobalEvent
	 */
	public void open(GlobalEvent e)
	{
		_door.openMe(null, !e.isInProgress());
	}
	
	/**
	 * Method close.
	 * @param e GlobalEvent
	 */
	public void close(GlobalEvent e)
	{
		_door.closeMe(null, !e.isInProgress());
	}
	
	/**
	 * Method getDoor.
	 * @return DoorInstance
	 */
	public DoorInstance getDoor()
	{
		return _door;
	}
	
	/**
	 * Method isWeak.
	 * @return boolean
	 */
	public boolean isWeak()
	{
		return _weak;
	}
	
	/**
	 * Method setWeak.
	 * @param weak boolean
	 */
	public void setWeak(boolean weak)
	{
		_weak = weak;
	}
}
