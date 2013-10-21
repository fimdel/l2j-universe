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
public class SpawnDespawnAction implements EventAction
{
	/**
	 * Field _spawn.
	 */
	private final boolean _spawn;
	/**
	 * Field _name.
	 */
	private final String _name;
	
	/**
	 * Constructor for SpawnDespawnAction.
	 * @param name String
	 * @param spawn boolean
	 */
	public SpawnDespawnAction(String name, boolean spawn)
	{
		_spawn = spawn;
		_name = name;
	}
	
	/**
	 * Method call.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.EventAction#call(GlobalEvent)
	 */
	@Override
	public void call(GlobalEvent event)
	{
		event.spawnAction(_name, _spawn);
	}
}
