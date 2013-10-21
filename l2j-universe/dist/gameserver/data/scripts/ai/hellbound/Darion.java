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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Darion extends Fighter
{
	/**
	 * Field doors.
	 */
	private static final int[] doors =
	{
		20250009,
		20250004,
		20250005,
		20250006,
		20250007
	};
	
	/**
	 * Constructor for Darion.
	 * @param actor NpcInstance
	 */
	public Darion(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		final NpcInstance actor = getActor();
		for (int i = 0; i < 5; i++)
		{
			try
			{
				SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(Rnd.get(25614, 25615)));
				sp.setLoc(Location.findPointToStay(actor, 400, 900));
				sp.doSpawn(true);
				sp.stopRespawn();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
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
		for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(25614, false))
		{
			npc.deleteMe();
		}
		for (NpcInstance npc : GameObjectsStorage.getAllByNpcId(25615, false))
		{
			npc.deleteMe();
		}
		super.onEvtDead(killer);
	}
}
