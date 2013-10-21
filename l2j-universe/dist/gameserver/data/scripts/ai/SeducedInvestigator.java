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

import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import lineage2.gameserver.tables.SkillTable;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SeducedInvestigator extends Fighter
{
	/**
	 * Field _allowedTargets.
	 */
	private final int[] _allowedTargets =
	{
		25659,
		25660,
		25661,
		25662,
		25663,
		25664
	};
	/**
	 * Field _reuse.
	 */
	private long _reuse = 0;
	
	/**
	 * Constructor for SeducedInvestigator.
	 * @param actor NpcInstance
	 */
	public SeducedInvestigator(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
		actor.startHealBlocked();
		AI_TASK_ACTIVE_DELAY = 5000;
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
			return false;
		}
		final List<NpcInstance> around = actor.getAroundNpc(1000, 300);
		if ((around != null) && !around.isEmpty())
		{
			for (NpcInstance npc : around)
			{
				if (ArrayUtils.contains(_allowedTargets, npc.getNpcId()))
				{
					actor.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, npc, 300);
				}
			}
		}
		if (Rnd.chance(0.1) && ((_reuse + 30000) < System.currentTimeMillis()))
		{
			final List<Player> players = World.getAroundPlayers(actor, 500, 200);
			if ((players == null) || (players.size() < 1))
			{
				return false;
			}
			final Player player = players.get(Rnd.get(players.size()));
			if (player.getReflectionId() == actor.getReflectionId())
			{
				_reuse = System.currentTimeMillis();
				final int[] buffs =
				{
					5970,
					5971,
					5972,
					5973
				};
				if (actor.getNpcId() == 36562)
				{
					actor.doCast(SkillTable.getInstance().getInfo(buffs[0], 1), player, true);
				}
				else if (actor.getNpcId() == 36563)
				{
					actor.doCast(SkillTable.getInstance().getInfo(buffs[1], 1), player, true);
				}
				else if (actor.getNpcId() == 36564)
				{
					actor.doCast(SkillTable.getInstance().getInfo(buffs[2], 1), player, true);
				}
				else
				{
					actor.doCast(SkillTable.getInstance().getInfo(buffs[3], 1), player, true);
				}
			}
		}
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
		final Reflection r = actor.getReflection();
		final List<Player> players = r.getPlayers();
		for (Player p : players)
		{
			p.sendPacket(new ExShowScreenMessage("The Investigator has been killed. The mission is failed.", 3000, ScreenMessageAlign.TOP_CENTER, true));
		}
		r.startCollapseTimer(5 * 1000L);
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
		final NpcInstance actor = getActor();
		if (attacker == null)
		{
			return;
		}
		if (attacker.isPlayable())
		{
			return;
		}
		if ((attacker.getNpcId() == 25659) || (attacker.getNpcId() == 25660) || (attacker.getNpcId() == 25661))
		{
			actor.getAggroList().addDamageHate(attacker, 0, 20);
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
		if (target.isPlayer() || target.isPet() || target.isServitor())
		{
			return;
		}
		super.onEvtAggression(target, aggro);
	}
	
	/**
	 * Method checkAggression.
	 * @param target Creature
	 * @return boolean
	 */
	@Override
	public boolean checkAggression(Creature target)
	{
		if (target.isPlayable())
		{
			return false;
		}
		return super.checkAggression(target);
	}
}
