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
package ai.groups;

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ForgeoftheGods extends Fighter
{
	/**
	 * Field RANDOM_SPAWN_MOBS.
	 */
	private static final int[] RANDOM_SPAWN_MOBS =
	{
		18799,
		18800,
		18801,
		18802,
		18803
	};
	/**
	 * Field FOG_MOBS.
	 */
	private static final int[] FOG_MOBS =
	{
		22634,
		22635,
		22636,
		22637,
		22638,
		22639,
		22640,
		22641,
		22642,
		22643,
		22644,
		22645,
		22646,
		22647,
		22648,
		22649
	};
	/**
	 * Field TAR_BEETLE. (value is 18804)
	 */
	private static final int TAR_BEETLE = 18804;
	/**
	 * Field TAR_BEETLE_ACTIVATE_SKILL_CHANGE.
	 */
	private static final int TAR_BEETLE_ACTIVATE_SKILL_CHANGE = 2;
	/**
	 * Field TAR_BEETLE_SEARCH_RADIUS.
	 */
	private static final int TAR_BEETLE_SEARCH_RADIUS = 500;
	
	/**
	 * Constructor for ForgeoftheGods.
	 * @param actor NpcInstance
	 */
	public ForgeoftheGods(NpcInstance actor)
	{
		super(actor);
		if (actor.getNpcId() == TAR_BEETLE)
		{
			AI_TASK_ATTACK_DELAY = 1250;
			actor.setIsInvul(true);
			actor.setHasChatWindow(false);
		}
		else if (ArrayUtils.contains(RANDOM_SPAWN_MOBS, actor.getNpcId()))
		{
			actor.startImmobilized();
		}
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		final NpcInstance actor = getActor();
		if (actor.getNpcId() != TAR_BEETLE)
		{
			return super.thinkActive();
		}
		if (actor.isDead() || !Rnd.chance(TAR_BEETLE_ACTIVATE_SKILL_CHANGE))
		{
			return false;
		}
		final List<Player> players = World.getAroundPlayers(actor, TAR_BEETLE_SEARCH_RADIUS, 200);
		if ((players == null) || (players.size() < 1))
		{
			return false;
		}
		actor.doCast(SkillTable.getInstance().getInfo(6142, Rnd.get(1, 3)), players.get(Rnd.get(players.size())), false);
		return true;
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		if (ArrayUtils.contains(FOG_MOBS, actor.getNpcId()))
		{
			try
			{
				final NpcInstance npc = NpcHolder.getInstance().getTemplate(RANDOM_SPAWN_MOBS[Rnd.get(RANDOM_SPAWN_MOBS.length)]).getNewInstance();
				npc.setSpawnedLoc(actor.getLoc());
				npc.setReflection(actor.getReflection());
				npc.setCurrentHpMp(npc.getMaxHp(), npc.getMaxMp(), true);
				npc.spawnMe(npc.getSpawnedLoc());
				npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, killer, Rnd.get(1, 100));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if (getActor().getNpcId() == TAR_BEETLE)
		{
			return;
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method onEvtAggression.
	 * @param target Creature
	 * @param aggro int
	 */
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		if (getActor().getNpcId() == TAR_BEETLE)
		{
			return;
		}
		super.onEvtAggression(target, aggro);
	}
	
	/**
	 * Method checkTarget.
	 * @param target Creature
	 * @param range int
	 * @return boolean
	 */
	@Override
	protected boolean checkTarget(Creature target, int range)
	{
		final NpcInstance actor = getActor();
		if (ArrayUtils.contains(RANDOM_SPAWN_MOBS, getActor().getNpcId()) && (target != null) && !actor.isInRange(target, actor.getAggroRange()))
		{
			actor.getAggroList().remove(target, true);
			return false;
		}
		return super.checkTarget(target, range);
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return (ArrayUtils.contains(RANDOM_SPAWN_MOBS, getActor().getNpcId()) || (getActor().getNpcId() == TAR_BEETLE)) ? false : true;
	}
}
