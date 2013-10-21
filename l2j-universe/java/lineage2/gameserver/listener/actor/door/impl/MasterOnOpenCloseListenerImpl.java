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
package lineage2.gameserver.listener.actor.door.impl;

import lineage2.gameserver.listener.actor.door.OnOpenCloseListener;
import lineage2.gameserver.model.instances.DoorInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MasterOnOpenCloseListenerImpl implements OnOpenCloseListener
{
	/**
	 * Field _door.
	 */
	private final DoorInstance _door;
	
	/**
	 * Constructor for MasterOnOpenCloseListenerImpl.
	 * @param door DoorInstance
	 */
	public MasterOnOpenCloseListenerImpl(DoorInstance door)
	{
		_door = door;
	}
	
	/**
	 * Method onOpen.
	 * @param doorInstance DoorInstance
	 * @see lineage2.gameserver.listener.actor.door.OnOpenCloseListener#onOpen(DoorInstance)
	 */
	@Override
	public void onOpen(DoorInstance doorInstance)
	{
		_door.openMe();
	}
	
	/**
	 * Method onClose.
	 * @param doorInstance DoorInstance
	 * @see lineage2.gameserver.listener.actor.door.OnOpenCloseListener#onClose(DoorInstance)
	 */
	@Override
	public void onClose(DoorInstance doorInstance)
	{
		_door.closeMe();
	}
}
