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
package ai.residences.clanhall;

import java.util.List;

import lineage2.commons.collections.CollectionUtils;
import lineage2.gameserver.Config;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.residences.clanhall.CTBBossInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MatchFighter extends Fighter
{
	/**
	 * Constructor for MatchFighter.
	 * @param actor NpcInstance
	 */
	protected MatchFighter(NpcInstance actor)
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
		if (actor.isActionsDisabled())
		{
			return true;
		}
		if (_def_think)
		{
			if (doTask())
			{
				clearTasks();
			}
			return true;
		}
		final long now = System.currentTimeMillis();
		if ((now - _checkAggroTimestamp) > Config.AGGRO_CHECK_INTERVAL)
		{
			_checkAggroTimestamp = now;
			final List<Creature> chars = World.getAroundCharacters(actor);
			CollectionUtils.eqSort(chars, _nearestTargetComparator);
			for (Creature cha : chars)
			{
				if (checkAggression(cha))
				{
					return true;
				}
			}
		}
		if (randomWalk())
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	protected boolean checkAggression(Creature target)
	{
		final CTBBossInstance actor = getActor();
		if (getIntention() != CtrlIntention.AI_INTENTION_ACTIVE)
		{
			return false;
		}
		if (target.isAlikeDead() || target.isInvul())
		{
			return false;
		}
		if (!actor.isAttackable(target))
		{
			return false;
		}
		if (!GeoEngine.canSeeTarget(actor, target, false))
		{
			return false;
		}
		actor.getAggroList().addDamageHate(target, 0, 2);
		if ((target.isServitor() || target.isPet()))
		{
			actor.getAggroList().addDamageHate(target.getPlayer(), 0, 1);
		}
		startRunningTask(AI_TASK_ATTACK_DELAY);
		setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		return true;
	}
	
	/**
	 * Method canAttackCharacter.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	protected boolean canAttackCharacter(Creature target)
	{
		final NpcInstance actor = getActor();
		return actor.isAttackable(target);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();
		final CTBBossInstance actor = getActor();
		final int x = (int) (actor.getX() + (800 * Math.cos(actor.headingToRadians(actor.getHeading() - 32768))));
		final int y = (int) (actor.getY() + (800 * Math.sin(actor.headingToRadians(actor.getHeading() - 32768))));
		actor.setSpawnedLoc(new Location(x, y, actor.getZ()));
		addTaskMove(actor.getSpawnedLoc(), true);
		doTask();
	}
	
	/**
	 * Method isGlobalAI.
	 * @return boolean
	 */
	@Override
	public boolean isGlobalAI()
	{
		return true;
	}
	
	/**
	 * Method getActor.
	 * @return CTBBossInstance
	 */
	@Override
	public CTBBossInstance getActor()
	{
		return (CTBBossInstance) super.getActor();
	}
}
