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

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.Priest;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.MinionInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.utils.Location;
import npc.model.QueenAntInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class QueenAntNurse extends Priest
{
	/**
	 * Constructor for QueenAntNurse.
	 * @param actor NpcInstance
	 */
	public QueenAntNurse(NpcInstance actor)
	{
		super(actor);
		MAX_PURSUE_RANGE = 10000;
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if (actor.isDead())
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
		final Creature top_desire_target = getTopDesireTarget();
		if (top_desire_target == null)
		{
			return false;
		}
		if ((actor.getDistance(top_desire_target) - top_desire_target.getColRadius() - actor.getColRadius()) > 200)
		{
			moveOrTeleportToLocation(Location.findFrontPosition(top_desire_target, actor, 100, 150));
			return false;
		}
		if (!top_desire_target.isCurrentHpFull() && doTask())
		{
			return createNewTask();
		}
		return false;
	}
	
	/**
	 * Method createNewTask.
	 * @return boolean
	 */
	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		final NpcInstance actor = getActor();
		final Creature top_desire_target = getTopDesireTarget();
		if (actor.isDead() || (top_desire_target == null))
		{
			return false;
		}
		if (!top_desire_target.isCurrentHpFull())
		{
			final Skill skill = _healSkills[Rnd.get(_healSkills.length)];
			if (skill.getAOECastRange() < actor.getDistance(top_desire_target))
			{
				moveOrTeleportToLocation(Location.findFrontPosition(top_desire_target, actor, skill.getAOECastRange() - 30, skill.getAOECastRange() - 10));
			}
			addTaskBuff(top_desire_target, skill);
			return true;
		}
		return false;
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
	 * Method moveOrTeleportToLocation.
	 * @param loc Location
	 */
	private void moveOrTeleportToLocation(Location loc)
	{
		final NpcInstance actor = getActor();
		actor.setRunning();
		if (actor.moveToLocation(loc, 0, true))
		{
			return;
		}
		clientStopMoving();
		_pathfindFails = 0;
		actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, 2036, 1, 500, 600000));
		ThreadPoolManager.getInstance().schedule(new Teleport(loc), 500);
	}
	
	/**
	 * Method getTopDesireTarget.
	 * @return Creature
	 */
	private Creature getTopDesireTarget()
	{
		final NpcInstance actor = getActor();
		final QueenAntInstance queen_ant = (QueenAntInstance) ((MinionInstance) actor).getLeader();
		if (queen_ant == null)
		{
			return null;
		}
		final Creature Larva = queen_ant.getLarva();
		if ((Larva != null) && (Larva.getCurrentHpPercents() < 5))
		{
			return Larva;
		}
		return queen_ant;
	}
	
	/**
	 * Method onIntentionAttack.
	 * @param target Creature
	 */
	@Override
	protected void onIntentionAttack(Creature target)
	{
		// empty method
	}
	
	/**
	 * Method onEvtClanAttacked.
	 * @param attacked_member Creature
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtClanAttacked(Creature attacked_member, Creature attacker, int damage)
	{
		if (doTask())
		{
			createNewTask();
		}
	}
}
