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
package ai.Heine.FieldOfSelenceAndWhispers;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AwakenedMucrokian extends Fighter
{
	/**
	 * Field mob.
	 */
	private NpcInstance mob = null;
	
	/**
	 * Constructor for AwakenedMucrokian.
	 * @param actor NpcInstance
	 */
	public AwakenedMucrokian(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if ((actor == null) || actor.isDead())
		{
			return true;
		}
		if (mob == null)
		{
			final List<NpcInstance> around = getActor().getAroundNpc(300, 300);
			if ((around != null) && !around.isEmpty())
			{
				for (NpcInstance npc : around)
				{
					if ((npc.getNpcId() == 18805) || (npc.getNpcId() == 18806))
					{
						if ((mob == null) || (getActor().getDistance3D(npc) < getActor().getDistance3D(mob)))
						{
							mob = npc;
						}
					}
				}
			}
		}
		if (mob != null)
		{
			actor.stopMove();
			actor.setRunning();
			getActor().getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, mob, 1);
			return true;
		}
		return false;
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		if ((actor != null) && !actor.isDead())
		{
			if (attacker != null)
			{
				if ((attacker.getNpcId() >= 22656) && (attacker.getNpcId() <= 22659))
				{
					if (Rnd.chance(25))
					{
						final Location pos = Location.findPointToStay(actor, 200, 300);
						if (GeoEngine.canMoveToCoord(actor.getX(), actor.getY(), actor.getZ(), pos.x, pos.y, pos.z, actor.getGeoIndex()))
						{
							actor.setRunning();
						}
						addTaskMove(pos, false);
					}
				}
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
}
