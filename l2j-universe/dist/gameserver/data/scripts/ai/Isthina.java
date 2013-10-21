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
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.ExShowScreenMessage;
import lineage2.gameserver.network.serverpackets.PlaySound;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Isthina extends Fighter
{
	/**
	 * Field ISTINA_LIGHT.
	 */
	static final int ISTINA_LIGHT = 29195;
	/**
	 * Field ISTINA_HARD.
	 */
	static final int ISTINA_HARD = 29196;
	/**
	 * Field BARRIER_OF_REFLECTION.
	 */
	static final Skill BARRIER_OF_REFLECTION = SkillTable.getInstance().getInfo(14215, 1);
	/**
	 * Field FLOOD.
	 */
	static final Skill FLOOD = SkillTable.getInstance().getInfo(14220, 1);
	/**
	 * Field MANIFESTATION_OF_AUTHORITY.
	 */
	static final Skill MANIFESTATION_OF_AUTHORITY = SkillTable.getInstance().getInfo(14289, 1);
	/**
	 * Field ACID_ERUPTION1.
	 */
	private static final Skill ACID_ERUPTION1 = SkillTable.getInstance().getInfo(14221, 1);
	/**
	 * Field ACID_ERUPTION2.
	 */
	private static final Skill ACID_ERUPTION2 = SkillTable.getInstance().getInfo(14222, 1);
	/**
	 * Field ACID_ERUPTION3.
	 */
	private static final Skill ACID_ERUPTION3 = SkillTable.getInstance().getInfo(14223, 1);
	/**
	 * Field ACID_ERUPTION1_TIMER.
	 */
	private long ACID_ERUPTION1_TIMER = 0;
	/**
	 * Field ACID_ERUPTION2_TIMER.
	 */
	private long ACID_ERUPTION2_TIMER = 0;
	/**
	 * Field ACID_ERUPTION3_TIMER.
	 */
	private long ACID_ERUPTION3_TIMER = 0;
	/**
	 * Field ACID_ERUPTION1_DELAY.
	 */
	private static final long ACID_ERUPTION1_DELAY = 60;
	/**
	 * Field ACID_ERUPTION2_DELAY.
	 */
	private static final long ACID_ERUPTION2_DELAY = 60;
	/**
	 * Field ACID_ERUPTION3_DELAY.
	 */
	private static final long ACID_ERUPTION3_DELAY = 60;
	/**
	 * Field DEATH_BLOW.
	 */
	static final int DEATH_BLOW = 14219;
	/**
	 * Field ISTINA_MARK.
	 */
	static final int ISTINA_MARK = 14218;
	/**
	 * Field RED_RING.
	 */
	static final int RED_RING = 14220101;
	/**
	 * Field BLUE_RING.
	 */
	static final int BLUE_RING = 14220102;
	/**
	 * Field GREEN_RING.
	 */
	static final int GREEN_RING = 14220103;
	/**
	 * Field RED_RING_LOC.
	 */
	final Zone RED_RING_LOC;
	/**
	 * Field BLUE_RING_LOC.
	 */
	final Zone BLUE_RING_LOC;
	/**
	 * Field GREEN_RING_LOC.
	 */
	final Zone GREEN_RING_LOC;
	/**
	 * Field _effectCheckTask.
	 */
	ScheduledFuture<?> _effectCheckTask = null;
	/**
	 * Field _authorityLock.
	 */
	private boolean _authorityLock = false;
	/**
	 * Field _hasFlood.
	 */
	private static final boolean _hasFlood = false;
	/**
	 * Field _hasBarrier.
	 */
	private static final boolean _hasBarrier = false;
	/**
	 * Field _ring.
	 */
	int _ring;
	/**
	 * Field _zone.
	 */
	static Zone _zone;
	
	/**
	 * Constructor for Isthina.
	 * @param actor NpcInstance
	 */
	public Isthina(NpcInstance actor)
	{
		super(actor);
		_zone = ReflectionManager.getInstance().get(getActor().getReflectionId()).getZone("[Isthina_epic]");
		RED_RING_LOC = ReflectionManager.getInstance().get(getActor().getReflectionId()).getZone("[Isthina_red_zone]");
		BLUE_RING_LOC = ReflectionManager.getInstance().get(getActor().getReflectionId()).getZone("[Isthina_blue_zone]");
		GREEN_RING_LOC = ReflectionManager.getInstance().get(getActor().getReflectionId()).getZone("[Isthina_green_zone]");
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
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		final long generalReuse = System.currentTimeMillis() + 60000L;
		ACID_ERUPTION1_TIMER += generalReuse + (Rnd.get(1, 20) * 1000L);
		ACID_ERUPTION2_TIMER += generalReuse + (Rnd.get(1, 20) * 1000L);
		ACID_ERUPTION3_TIMER += generalReuse + (Rnd.get(1, 20) * 1000L);
	}
	
	/**
	 * Method thinkAttack.
	 */
	@Override
	protected void thinkAttack()
	{
		final NpcInstance npc = getActor();
		if (_effectCheckTask == null)
		{
			_effectCheckTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new EffectCheckTask(npc), 0, 2000);
		}
		else
		{
			ThreadPoolManager.getInstance().scheduleAtFixedRate(new EffectCheckTask(npc), 0, 2000);
		}
		final double damage = (npc.getMaxHp() - npc.getCurrentHp());
		final double lastPercentHp = (npc.getCurrentHp() + damage) / npc.getMaxHp();
		final double currentPercentHp = npc.getCurrentHp() / npc.getMaxHp();
		if ((lastPercentHp > 0.9D) && (currentPercentHp <= 0.9D))
		{
			onPercentHpReached(npc, 90);
		}
		else if ((lastPercentHp > 0.8D) && (currentPercentHp <= 0.8D))
		{
			onPercentHpReached(npc, 80);
		}
		else if ((lastPercentHp > 0.7D) && (currentPercentHp <= 0.7D))
		{
			onPercentHpReached(npc, 70);
		}
		else if ((lastPercentHp > 0.6D) && (currentPercentHp <= 0.6D))
		{
			onPercentHpReached(npc, 60);
		}
		else if ((lastPercentHp > 0.5D) && (currentPercentHp <= 0.5D))
		{
			onPercentHpReached(npc, 50);
		}
		else if ((lastPercentHp > 0.45D) && (currentPercentHp <= 0.45D))
		{
			onPercentHpReached(npc, 45);
		}
		else if ((lastPercentHp > 0.4D) && (currentPercentHp <= 0.4D))
		{
			onPercentHpReached(npc, 40);
		}
		else if ((lastPercentHp > 0.35D) && (currentPercentHp <= 0.35D))
		{
			onPercentHpReached(npc, 35);
		}
		else if ((lastPercentHp > 0.3D) && (currentPercentHp <= 0.3D))
		{
			onPercentHpReached(npc, 30);
		}
		else if ((lastPercentHp > 0.25D) && (currentPercentHp <= 0.25D))
		{
			onPercentHpReached(npc, 25);
		}
		else if ((lastPercentHp > 0.2D) && (currentPercentHp <= 0.2D))
		{
			onPercentHpReached(npc, 20);
		}
		else if ((lastPercentHp > 0.15D) && (currentPercentHp <= 0.15D))
		{
			onPercentHpReached(npc, 15);
		}
		else if ((lastPercentHp > 0.1D) && (currentPercentHp <= 0.1D))
		{
			onPercentHpReached(npc, 10);
		}
		else if ((lastPercentHp > 0.05D) && (currentPercentHp <= 0.05D))
		{
			onPercentHpReached(npc, 5);
		}
		else if ((lastPercentHp > 0.02D) && (currentPercentHp <= 0.02D))
		{
			onPercentHpReached(npc, 2);
		}
		else if ((lastPercentHp > 0.01D) && (currentPercentHp <= 0.01D))
		{
			onPercentHpReached(npc, 1);
		}
		else
		{
			final double seed = Rnd.get();
			if ((seed < 0.005D) && (!_authorityLock))
			{
				authorityField(npc);
			}
		}
		super.thinkAttack();
	}
	
	/**
	 * Method onPercentHpReached.
	 * @param npc NpcInstance
	 * @param percent int
	 */
	public void onPercentHpReached(NpcInstance npc, int percent)
	{
		if (npc.isCastingNow())
		{
			return;
		}
		Skill skillToCast;
		final NpcInstance npcs = npc;
		if (!npc.isCastingNow())
		{
			if ((Rnd.get() <= 0.4D) && (ACID_ERUPTION1_TIMER < System.currentTimeMillis()))
			{
				skillToCast = ACID_ERUPTION1;
				ACID_ERUPTION1_TIMER = System.currentTimeMillis() + (ACID_ERUPTION1_DELAY * 1000);
				npc.doCast(skillToCast, npc, false);
			}
			else if ((Rnd.get() <= 0.5D) && (ACID_ERUPTION2_TIMER < System.currentTimeMillis()))
			{
				skillToCast = ACID_ERUPTION2;
				ACID_ERUPTION2_TIMER = System.currentTimeMillis() + (ACID_ERUPTION2_DELAY * 1000);
				npc.doCast(skillToCast, npc, false);
			}
			else if (ACID_ERUPTION3_TIMER < System.currentTimeMillis())
			{
				skillToCast = ACID_ERUPTION3;
				ACID_ERUPTION3_TIMER = System.currentTimeMillis() + (ACID_ERUPTION3_DELAY * 1000);
				npc.doCast(skillToCast, npc, false);
			}
		}
		if (((percent >= 50) && ((percent % 10) == 0) && !npc.isCastingNow()) || ((percent < 50) && ((percent % 5) == 0) && !npc.isCastingNow()))
		{
			if ((npcs.getEffectList().getEffectsBySkill(FLOOD) != null) && !npc.isCastingNow())
			{
				npc.doCast(FLOOD, npc, false);
			}
		}
		if ((npcs.getEffectList().getEffectsBySkill(BARRIER_OF_REFLECTION) == null) && !npc.isCastingNow())
		{
			npcs.doCast(BARRIER_OF_REFLECTION, npcs, false);
		}
	}
	
	/**
	 * Method authorityField.
	 * @param npc NpcInstance
	 */
	private void authorityField(final NpcInstance npc)
	{
		_authorityLock = true;
		final double seed = Rnd.get();
		final int ring = ((seed >= 0.33D) && (seed < 0.66D)) ? 1 : (seed < 0.33D) ? 0 : 2;
		_ring = ring;
		if (seed < 0.33D)
		{
			npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_GREEN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
		}
		else
		{
			if ((seed >= 0.33D) && (seed < 0.66D))
			{
				npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_BLUE, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
			}
			else
			{
				npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SOUL_STONE_STARTS_POWERFULLY_ILLUMINATING_IN_RED, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
			}
		}
		npc.broadcastPacket(new PlaySound("istina.istina_voice_01"));
		ThreadPoolManager.getInstance().schedule(new runAuthorityRing(npc), 10000L);
	}
	
	/**
	 * @author Mobius
	 */
	private class EffectCheckTask extends RunnableImpl
	{
		/**
		 * Field _npc.
		 */
		private final NpcInstance _npc;
		
		/**
		 * Constructor for EffectCheckTask.
		 * @param npc NpcInstance
		 */
		EffectCheckTask(NpcInstance npc)
		{
			_npc = npc;
		}
		
		/**
		 * Method runImpl.
		 */
		@SuppressWarnings("unused")
		@Override
		public void runImpl()
		{
			if (_npc == null)
			{
				if (_effectCheckTask != null)
				{
					_effectCheckTask.cancel(false);
				}
			}
			boolean hasBarrier = false;
			boolean hasFlood = false;
			if (_npc.getEffectList().getEffectsBySkillId(BARRIER_OF_REFLECTION.getId()) != null)
			{
				hasBarrier = true;
				if (hasFlood)
				{
					return;
				}
			}
			else
			{
				if (_npc.getEffectList().getEffectsBySkillId(FLOOD.getId()) != null)
				{
					hasFlood = true;
				}
				if (hasBarrier)
				{
					return;
				}
			}
			if ((_hasBarrier) && (!hasBarrier))
			{
				_npc.setNpcState(2);
				_npc.setNpcState(0);
				_npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_SPREADS_PROTECTIVE_SHEET, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
			}
			else if ((!_hasBarrier) && (hasBarrier))
			{
				_npc.setNpcState(1);
			}
			if ((_hasFlood) && (hasFlood))
			{
				_npc.broadcastPacket(new ExShowScreenMessage(NpcString.ISTINA_GETS_FURIOUS_AND_RECKLESSLY_CRAZY, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
			}
			else if ((_hasFlood) && (!hasFlood))
			{
				_npc.broadcastPacket(new ExShowScreenMessage(NpcString.BERSERKER_OF_ISTINA_HAS_BEEN_DISABLED, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class runAuthorityRing extends RunnableImpl
	{
		/**
		 * Field _npc.
		 */
		private final NpcInstance _npc;
		
		/**
		 * Constructor for runAuthorityRing.
		 * @param npc NpcInstance
		 */
		runAuthorityRing(NpcInstance npc)
		{
			_npc = npc;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final NpcInstance npc = _npc;
			Zone zones;
			if (_ring != 0)
			{
				npc.broadcastPacket(new EventTrigger(GREEN_RING, true));
				npc.broadcastPacket(new EventTrigger(RED_RING, true));
				zones = BLUE_RING_LOC;
			}
			else if (_ring != 1)
			{
				npc.broadcastPacket(new EventTrigger(BLUE_RING, true));
				npc.broadcastPacket(new EventTrigger(GREEN_RING, true));
				zones = RED_RING_LOC;
			}
			else
			{
				npc.broadcastPacket(new EventTrigger(RED_RING, true));
				npc.broadcastPacket(new EventTrigger(BLUE_RING, true));
				zones = GREEN_RING_LOC;
			}
			for (Player player : _zone.getInsidePlayers())
			{
				if (!player.isInZone(zones))
				{
					MANIFESTATION_OF_AUTHORITY.getEffects(npc, player, false, false);
				}
			}
		}
	}
}
