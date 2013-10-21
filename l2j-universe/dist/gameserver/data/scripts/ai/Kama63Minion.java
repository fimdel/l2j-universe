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

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Kama63Minion extends Fighter
{
	/**
	 * Field BOSS_ID. (value is 18571)
	 */
	private static final int BOSS_ID = 18571;
	/**
	 * Field MINION_DIE_TIME. (value is 25000)
	 */
	private static final int MINION_DIE_TIME = 25000;
	/**
	 * Field _wait_timeout.
	 */
	private long _wait_timeout = 0;
	/**
	 * Field _boss.
	 */
	private NpcInstance _boss;
	/**
	 * Field _spawned.
	 */
	private boolean _spawned = false;
	/**
	 * Field _dieTask.
	 */
	ScheduledFuture<?> _dieTask = null;
	
	/**
	 * Constructor for Kama63Minion.
	 * @param actor NpcInstance
	 */
	public Kama63Minion(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		_boss = findBoss(BOSS_ID);
		super.onEvtSpawn();
	}
	
	/**
	 * Method thinkActive.
	 * @return boolean
	 */
	@Override
	protected boolean thinkActive()
	{
		if (_boss == null)
		{
			_boss = findBoss(BOSS_ID);
		}
		else if (!_spawned)
		{
			_spawned = true;
			Functions.npcSayCustomMessage(_boss, "Kama63Boss");
			final NpcInstance minion = getActor();
			minion.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, _boss.getAggroList().getRandomHated(), Rnd.get(1, 100));
			_dieTask = ThreadPoolManager.getInstance().schedule(new DieScheduleTimerTask(minion, _boss), MINION_DIE_TIME);
		}
		return super.thinkActive();
	}
	
	/**
	 * Method findBoss.
	 * @param npcId int
	 * @return NpcInstance
	 */
	private NpcInstance findBoss(int npcId)
	{
		if (System.currentTimeMillis() < _wait_timeout)
		{
			return null;
		}
		_wait_timeout = System.currentTimeMillis() + 15000;
		final NpcInstance minion = getActor();
		if (minion == null)
		{
			return null;
		}
		for (NpcInstance npc : World.getAroundNpc(minion))
		{
			if (npc.getNpcId() == npcId)
			{
				return npc;
			}
		}
		return null;
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_spawned = false;
		if (_dieTask != null)
		{
			_dieTask.cancel(false);
			_dieTask = null;
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * @author Mobius
	 */
	static public class DieScheduleTimerTask extends RunnableImpl
	{
		/**
		 * Field _minion.
		 */
		NpcInstance _minion = null;
		/**
		 * Field _master.
		 */
		NpcInstance _master = null;
		
		/**
		 * Constructor for DieScheduleTimerTask.
		 * @param minion NpcInstance
		 * @param master NpcInstance
		 */
		public DieScheduleTimerTask(NpcInstance minion, NpcInstance master)
		{
			_minion = minion;
			_master = master;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if ((_master != null) && (_minion != null) && !_master.isDead() && !_minion.isDead())
			{
				_master.setCurrentHp(_master.getCurrentHp() + (_minion.getCurrentHp() * 5), false);
			}
			Functions.npcSayCustomMessage(_minion, "Kama63Minion");
			_minion.doDie(_minion);
		}
	}
}
