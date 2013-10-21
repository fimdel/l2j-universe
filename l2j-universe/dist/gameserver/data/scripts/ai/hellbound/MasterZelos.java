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
package ai.hellbound;

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MasterZelos extends Fighter
{
	/**
	 * Field _zone.
	 */
	private static Zone _zone;
	/**
	 * Field doors.
	 */
	private static final int[] doors =
	{
		19260054,
		19260053
	};
	
	/**
	 * Constructor for MasterZelos.
	 * @param actor NpcInstance
	 */
	public MasterZelos(NpcInstance actor)
	{
		super(actor);
		_zone = ReflectionUtils.getZone("[tully1]");
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		setZoneInactive();
		super.onEvtSpawn();
		for (int door2 : doors)
		{
			DoorInstance door = ReflectionUtils.getDoor(door2);
			door.closeMe();
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		for (int door2 : doors)
		{
			DoorInstance door = ReflectionUtils.getDoor(door2);
			door.openMe();
		}
		super.onEvtDead(killer);
		setZoneActive();
	}
	
	/**
	 * Method setZoneActive.
	 */
	private void setZoneActive()
	{
		_zone.setActive(true);
	}
	
	/**
	 * Method setZoneInactive.
	 */
	private void setZoneInactive()
	{
		_zone.setActive(false);
	}
}
