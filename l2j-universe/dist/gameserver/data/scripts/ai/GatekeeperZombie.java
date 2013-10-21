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
package ai;

import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GatekeeperZombie extends Mystic
{
	/**
	 * Constructor for GatekeeperZombie.
	 * @param actor NpcInstance
	 */
	public GatekeeperZombie(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return false;
		}
		if ((getIntention() != CtrlIntention.AI_INTENTION_ACTIVE) || !isGlobalAggro())
		{
			return false;
		}
		if (target.isAlikeDead() || !target.isPlayable())
		{
			return false;
		}
		if (!target.isInRangeZ(actor.getSpawnedLoc(), actor.getAggroRange()))
		{
			return false;
		}
		if ((Functions.getItemCount((Playable) target, 8067) != 0) || (Functions.getItemCount((Playable) target, 8064) != 0))
		{
			return false;
		}
		if (!GeoEngine.canSeeTarget(actor, target, false))
		{
			return false;
		}
		if (getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
		{
			actor.getAggroList().addDamageHate(target, 0, 1);
			setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		}
		return true;
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
}
