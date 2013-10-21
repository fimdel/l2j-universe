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

import lineage2.gameserver.data.xml.holder.StaticObjectHolder;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.StaticObjectInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StaticObjectObject implements SpawnableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _uid.
	 */
	private final int _uid;
	/**
	 * Field _instance.
	 */
	private StaticObjectInstance _instance;
	
	/**
	 * Constructor for StaticObjectObject.
	 * @param id int
	 */
	public StaticObjectObject(int id)
	{
		_uid = id;
	}
	
	/**
	 * Method spawnObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#spawnObject(GlobalEvent)
	 */
	@Override
	public void spawnObject(GlobalEvent event)
	{
		_instance = StaticObjectHolder.getInstance().getObject(_uid);
	}
	
	/**
	 * Method despawnObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#despawnObject(GlobalEvent)
	 */
	@Override
	public void despawnObject(GlobalEvent event)
	{
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
			_instance.removeEvent(event);
		}
		else
		{
			_instance.addEvent(event);
		}
	}
	
	/**
	 * Method setMeshIndex.
	 * @param id int
	 */
	public void setMeshIndex(int id)
	{
		_instance.setMeshIndex(id);
		_instance.broadcastInfo(false);
	}
	
	/**
	 * Method getUId.
	 * @return int
	 */
	public int getUId()
	{
		return _uid;
	}
}
