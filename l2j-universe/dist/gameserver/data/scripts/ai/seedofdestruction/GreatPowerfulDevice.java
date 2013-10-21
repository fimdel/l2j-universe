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
package ai.seedofdestruction;

import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GreatPowerfulDevice extends DefaultAI
{
	/**
	 * Field MOBS.
	 */
	private static final int[] MOBS =
	{
		22540,
		22546,
		22542,
		22547,
		22538
	};
	/**
	 * Field OBELISK_LOC.
	 */
	private static final Location OBELISK_LOC = new Location(-245825, 217075, -12208);
	
	/**
	 * Constructor for GreatPowerfulDevice.
	 * @param actor NpcInstance
	 */
	public GreatPowerfulDevice(NpcInstance actor)
	{
		super(actor);
		actor.block();
		actor.startDamageBlocked();
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		if (checkAllDestroyed(actor.getNpcId()))
		{
			for (int i = 0; i < 6; i++)
			{
				for (int mobId : MOBS)
				{
					actor.getReflection().addSpawnWithoutRespawn(mobId, Location.findPointToStay(OBELISK_LOC.clone().setZ(-12224), 600, 1200, actor.getGeoIndex()), 0);
				}
			}
			actor.getReflection().openDoor(12240027);
			for (NpcInstance n : actor.getReflection().getNpcs())
			{
				if (n.getNpcId() == 18778)
				{
					n.stopDamageBlocked();
				}
			}
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * Method checkAllDestroyed.
	 * @param mobId int
	 * @return boolean
	 */
	private boolean checkAllDestroyed(int mobId)
	{
		for (NpcInstance n : getActor().getReflection().getNpcs())
		{
			if ((n.getNpcId() == mobId) && !n.isDead())
			{
				return false;
			}
		}
		return true;
	}
}
