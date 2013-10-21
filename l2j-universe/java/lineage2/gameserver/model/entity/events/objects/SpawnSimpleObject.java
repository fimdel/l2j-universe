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

import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SpawnSimpleObject implements SpawnableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _npcId.
	 */
	private final int _npcId;
	/**
	 * Field _loc.
	 */
	private final Location _loc;
	/**
	 * Field _npc.
	 */
	private NpcInstance _npc;
	
	/**
	 * Constructor for SpawnSimpleObject.
	 * @param npcId int
	 * @param loc Location
	 */
	public SpawnSimpleObject(int npcId, Location loc)
	{
		_npcId = npcId;
		_loc = loc;
	}
	
	/**
	 * Method spawnObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#spawnObject(GlobalEvent)
	 */
	@Override
	public void spawnObject(GlobalEvent event)
	{
		_npc = NpcUtils.spawnSingle(_npcId, _loc, event.getReflection());
		_npc.addEvent(event);
	}
	
	/**
	 * Method despawnObject.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.objects.SpawnableObject#despawnObject(GlobalEvent)
	 */
	@Override
	public void despawnObject(GlobalEvent event)
	{
		_npc.removeEvent(event);
		_npc.deleteMe();
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
}
