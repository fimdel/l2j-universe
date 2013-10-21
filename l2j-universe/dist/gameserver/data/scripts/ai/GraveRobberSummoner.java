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
import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.Func;
import lineage2.gameserver.templates.npc.MinionData;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GraveRobberSummoner extends Mystic
{
	/**
	 * Field Servitors.
	 */
	private static final int[] Servitors =
	{
		22683,
		22684,
		22685,
		22686
	};
	/**
	 * Field _lastMinionCount.
	 */
	int _lastMinionCount = 1;
	
	/**
	 * @author Mobius
	 */
	private class FuncMulMinionCount extends Func
	{
		/**
		 * Constructor for FuncMulMinionCount.
		 * @param stat Stats
		 * @param order int
		 * @param owner Object
		 */
		FuncMulMinionCount(Stats stat, int order, Object owner)
		{
			super(stat, order, owner);
		}
		
		/**
		 * Method calc.
		 * @param env Env
		 */
		@Override
		public void calc(Env env)
		{
			env.value *= _lastMinionCount;
		}
	}
	
	/**
	 * Constructor for GraveRobberSummoner.
	 * @param actor NpcInstance
	 */
	public GraveRobberSummoner(NpcInstance actor)
	{
		super(actor);
		actor.addStatFunc(new FuncMulMinionCount(Stats.MAGIC_DEFENCE, 0x30, actor));
		actor.addStatFunc(new FuncMulMinionCount(Stats.POWER_DEFENCE, 0x30, actor));
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		final NpcInstance actor = getActor();
		actor.getMinionList().addMinion(new MinionData(Servitors[Rnd.get(Servitors.length)], Rnd.get(2)));
		_lastMinionCount = Math.max(actor.getMinionList().getAliveMinions().size(), 1);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final MonsterInstance actor = (MonsterInstance) getActor();
		if (actor.isDead())
		{
			return;
		}
		_lastMinionCount = Math.max(actor.getMinionList().getAliveMinions().size(), 1);
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		actor.getMinionList().deleteMinions();
		super.onEvtDead(killer);
	}
}
