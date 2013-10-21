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
package ai.teredor;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.CtrlIntention;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TeredorLairEggs extends Fighter
{
	/**
	 * Field awakenedMillipede.
	 */
	static int awakenedMillipede = 18995;
	/**
	 * Field teredorLarva.
	 */
	private static final int teredorLarva = 19016;
	/**
	 * Field timeToBlue.
	 */
	private static final int timeToBlue = 60;
	/**
	 * Field maxRandomTimeBlue.
	 */
	private static final int maxRandomTimeBlue = 80;
	/**
	 * Field monsterSpawnDelay.
	 */
	static int monsterSpawnDelay = 15;
	/**
	 * Field poisonId.
	 */
	private static final int poisonId = 14561;
	/**
	 * Field poisonLevel.
	 */
	private static final int poisonLevel = 1;
	/**
	 * Field distanceToDebuff.
	 */
	private static final int distanceToDebuff = 400;
	/**
	 * Field _poisoned.
	 */
	boolean _poisoned = false;
	/**
	 * Field _activated.
	 */
	boolean _activated = false;
	/**
	 * Field actor.
	 */
	final NpcInstance actor = getActor();
	
	/**
	 * Constructor for TeredorLairEggs.
	 * @param actor NpcInstance
	 */
	public TeredorLairEggs(NpcInstance actor)
	{
		super(actor);
		actor.startImmobilized();
	}
	
	/**
	 * Method thinkAttack.
	 */
	@Override
	protected void thinkAttack()
	{
		if (!_activated)
		{
			final Player player = (Player) actor.getAggroList().getMostHated();
			final Reflection ref = actor.getReflection();
			ThreadPoolManager.getInstance().schedule(new TaskSetBlue(actor, player, ref), (timeToBlue + Rnd.get(maxRandomTimeBlue)) * 1000);
			_activated = true;
		}
		if (!_poisoned)
		{
			final Player player = (Player) actor.getAggroList().getMostHated();
			if (player.getParty() != null)
			{
				for (Playable playable : player.getParty().getPartyMembersWithPets())
				{
					if ((playable != null) && (actor.getDistance(playable.getLoc()) <= distanceToDebuff))
					{
						actor.doCast(SkillTable.getInstance().getInfo(poisonId, poisonLevel), playable, true);
					}
				}
			}
			_poisoned = true;
		}
		super.thinkAttack();
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(teredorLarva));
		sp.setLoc(Location.findPointToStay(actor, 100, 120));
		sp.doSpawn(true);
		super.onEvtDead(killer);
	}
	
	/**
	 * @author Mobius
	 */
	public class TaskSetBlue implements Runnable
	{
		/**
		 * Field _npc.
		 */
		NpcInstance _npc;
		/**
		 * Field _player.
		 */
		Player _player;
		/**
		 * Field _ref.
		 */
		Reflection _ref;
		
		/**
		 * Constructor for TaskSetBlue.
		 * @param npc NpcInstance
		 * @param player Player
		 * @param ref Reflection
		 */
		public TaskSetBlue(NpcInstance npc, Player player, Reflection ref)
		{
			_npc = npc;
			_player = player;
			_ref = ref;
		}
		
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			if ((_npc != null) && (!_npc.isDead()))
			{
				ThreadPoolManager.getInstance().schedule(new SpawnMonster(_npc, _player, _ref), monsterSpawnDelay * 1000);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class SpawnMonster extends RunnableImpl
	{
		/**
		 * Field _npc.
		 */
		NpcInstance _npc;
		/**
		 * Field _player.
		 */
		Player _player;
		/**
		 * Field _ref.
		 */
		Reflection _ref;
		
		/**
		 * Constructor for SpawnMonster.
		 * @param npc NpcInstance
		 * @param player Player
		 * @param ref Reflection
		 */
		public SpawnMonster(NpcInstance npc, Player player, Reflection ref)
		{
			_npc = npc;
			_player = player;
			_ref = ref;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if ((_npc != null) && (!_npc.isDead()))
			{
				if (_player != null)
				{
					final Location coords = Location.findPointToStay(actor, 100, 120);
					final NpcInstance npc = _ref.addSpawnWithoutRespawn(awakenedMillipede, coords, 0);
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, _player, Rnd.get(1, 100));
				}
				else
				{
					_npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
				}
			}
		}
	}
}
