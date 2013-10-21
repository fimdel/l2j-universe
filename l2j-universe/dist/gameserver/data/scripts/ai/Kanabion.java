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

import instances.KamalokaNightmare;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.PositionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Kanabion extends Fighter
{
	/**
	 * Constructor for Kanabion.
	 * @param actor NpcInstance
	 */
	public Kanabion(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		boolean isOverhit = false;
		if (actor instanceof MonsterInstance)
		{
			isOverhit = ((MonsterInstance) actor).getOverhitDamage() > 0;
		}
		final int npcId = actor.getNpcId();
		int nextId = 0;
		int type = 0;
		if ((npcId != getNextDoppler(npcId)) && (npcId != getNextVoid(npcId)))
		{
			type = 1;
			if (isOverhit)
			{
				if (Rnd.chance(70))
				{
					nextId = getNextDoppler(npcId);
				}
				else if (Rnd.chance(80))
				{
					nextId = getNextVoid(npcId);
				}
			}
			else if (Rnd.chance(65))
			{
				nextId = getNextDoppler(npcId);
			}
		}
		else if (npcId == getNextDoppler(npcId))
		{
			type = 2;
			if (isOverhit)
			{
				if (Rnd.chance(60))
				{
					nextId = getNextDoppler(npcId);
				}
				else if (Rnd.chance(90))
				{
					nextId = getNextVoid(npcId);
				}
			}
			else if (Rnd.chance(40))
			{
				nextId = getNextDoppler(npcId);
			}
			else if (Rnd.chance(50))
			{
				nextId = getNextVoid(npcId);
			}
		}
		else if (npcId == getNextVoid(npcId))
		{
			type = 3;
			if (isOverhit)
			{
				if (Rnd.chance(80))
				{
					nextId = getNextVoid(npcId);
				}
			}
			else if (Rnd.chance(50))
			{
				nextId = getNextVoid(npcId);
			}
		}
		final Reflection r = actor.getReflection();
		boolean spawnPossible = true;
		if (r instanceof KamalokaNightmare)
		{
			final KamalokaNightmare kama = (KamalokaNightmare) r;
			kama.addKilledKanabion(type);
			spawnPossible = kama.isSpawnPossible();
		}
		if (spawnPossible && (nextId > 0))
		{
			Creature player = null;
			if (!killer.isPlayer())
			{
				for (Player pl : World.getAroundPlayers(actor))
				{
					player = pl;
					break;
				}
			}
			if (player == null)
			{
				player = killer;
			}
			ThreadPoolManager.getInstance().schedule(new SpawnNext(actor, player, nextId), 5000);
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * @author Mobius
	 */
	public static class SpawnNext extends RunnableImpl
	{
		/**
		 * Field _actor.
		 */
		private final NpcInstance _actor;
		/**
		 * Field _player.
		 */
		private final Creature _player;
		/**
		 * Field _nextId.
		 */
		private final int _nextId;
		
		/**
		 * Constructor for SpawnNext.
		 * @param actor NpcInstance
		 * @param player Creature
		 * @param nextId int
		 */
		public SpawnNext(NpcInstance actor, Creature player, int nextId)
		{
			_actor = actor;
			_player = player;
			_nextId = nextId;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final SimpleSpawner sp = new SimpleSpawner(_nextId);
			sp.setLocx(_actor.getX());
			sp.setLocy(_actor.getY());
			sp.setLocz(_actor.getZ());
			sp.setReflection(_actor.getReflection());
			final NpcInstance npc = sp.doSpawn(true);
			npc.setHeading(PositionUtils.calculateHeadingFrom(npc, _player));
			npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, _player, 1000);
		}
	}
	
	/**
	 * Method getNextDoppler.
	 * @param npcId int
	 * @return int
	 */
	private int getNextDoppler(int npcId)
	{
		switch (npcId)
		{
			case 22452:
			case 22453:
			case 22454:
				return 22453;
			case 22455:
			case 22456:
			case 22457:
				return 22456;
			case 22458:
			case 22459:
			case 22460:
				return 22459;
			case 22461:
			case 22462:
			case 22463:
				return 22462;
			case 22464:
			case 22465:
			case 22466:
				return 22465;
			case 22467:
			case 22468:
			case 22469:
				return 22468;
			case 22470:
			case 22471:
			case 22472:
				return 22471;
			case 22473:
			case 22474:
			case 22475:
				return 22474;
			case 22476:
			case 22477:
			case 22478:
				return 22477;
			case 22479:
			case 22480:
			case 22481:
				return 22480;
			case 22482:
			case 22483:
			case 22484:
				return 22483;
			default:
				return 0;
		}
	}
	
	/**
	 * Method getNextVoid.
	 * @param npcId int
	 * @return int
	 */
	private int getNextVoid(int npcId)
	{
		switch (npcId)
		{
			case 22452:
			case 22453:
			case 22454:
				return 22454;
			case 22455:
			case 22456:
			case 22457:
				return 22457;
			case 22458:
			case 22459:
			case 22460:
				return 22460;
			case 22461:
			case 22462:
			case 22463:
				return 22463;
			case 22464:
			case 22465:
			case 22466:
				return 22466;
			case 22467:
			case 22468:
			case 22469:
				return 22469;
			case 22470:
			case 22471:
			case 22472:
				return 22472;
			case 22473:
			case 22474:
			case 22475:
				return 22475;
			case 22476:
			case 22477:
			case 22478:
				return 22478;
			case 22479:
			case 22480:
			case 22481:
				return 22481;
			case 22482:
			case 22483:
			case 22484:
				return 22484;
			default:
				return 0;
		}
	}
}
