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

import java.io.Serializable;

import lineage2.gameserver.model.entity.events.GlobalEvent;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public interface SpawnableObject extends Serializable
{
	/**
	 * Method spawnObject.
	 * @param event GlobalEvent
	 */
	void spawnObject(GlobalEvent event);
	
	/**
	 * Method despawnObject.
	 * @param event GlobalEvent
	 */
	void despawnObject(GlobalEvent event);
	
	/**
	 * Method refreshObject.
	 * @param event GlobalEvent
	 */
	void refreshObject(GlobalEvent event);
}
