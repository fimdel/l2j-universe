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

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PavelRuins extends Fighter
{
	/**
	 * Field PAVEL_SAFETY_DEVICE. (value is 18917)
	 */
	private static final int PAVEL_SAFETY_DEVICE = 18917;
	/**
	 * Field CRUEL_PINCER_GOLEM_1. (value is 22801)
	 */
	private static final int CRUEL_PINCER_GOLEM_1 = 22801;
	/**
	 * Field CRUEL_PINCER_GOLEM_2. (value is 22802)
	 */
	private static final int CRUEL_PINCER_GOLEM_2 = 22802;
	/**
	 * Field CRUEL_PINCER_GOLEM_3. (value is 22803)
	 */
	private static final int CRUEL_PINCER_GOLEM_3 = 22803;
	/**
	 * Field DRILL_GOLEM_OF_TERROR_1. (value is 22804)
	 */
	private static final int DRILL_GOLEM_OF_TERROR_1 = 22804;
	/**
	 * Field DRILL_GOLEM_OF_TERROR_2. (value is 22805)
	 */
	private static final int DRILL_GOLEM_OF_TERROR_2 = 22805;
	/**
	 * Field DRILL_GOLEM_OF_TERROR_3. (value is 22806)
	 */
	private static final int DRILL_GOLEM_OF_TERROR_3 = 22806;
	
	/**
	 * Constructor for PavelRuins.
	 * @param actor NpcInstance
	 */
	public PavelRuins(NpcInstance actor)
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
		super.onEvtDead(killer);
		ThreadPoolManager.getInstance().schedule(new SpawnNext(actor, killer), 5000);
	}
	
	/**
	 * @author Mobius
	 */
	private static class SpawnNext extends RunnableImpl
	{
		/**
		 * Field _actor.
		 */
		private final NpcInstance _actor;
		/**
		 * Field _killer.
		 */
		private final Creature _killer;
		
		/**
		 * Constructor for SpawnNext.
		 * @param actor NpcInstance
		 * @param killer Creature
		 */
		SpawnNext(NpcInstance actor, Creature killer)
		{
			_actor = actor;
			_killer = killer;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (Rnd.chance(70))
			{
				Location loc = _actor.getLoc();
				switch (_actor.getNpcId())
				{
					case PAVEL_SAFETY_DEVICE:
						loc = new Location(loc.x + 30, loc.y + -30, loc.z);
						spawnNextMob(CRUEL_PINCER_GOLEM_3, _killer, loc);
						loc = new Location(loc.x + -30, loc.y + 30, loc.z);
						spawnNextMob(DRILL_GOLEM_OF_TERROR_3, _killer, loc);
						break;
					case CRUEL_PINCER_GOLEM_1:
						spawnNextMob(CRUEL_PINCER_GOLEM_2, _killer, loc);
						break;
					case CRUEL_PINCER_GOLEM_3:
						spawnNextMob(CRUEL_PINCER_GOLEM_1, _killer, loc);
						break;
					case DRILL_GOLEM_OF_TERROR_1:
						spawnNextMob(DRILL_GOLEM_OF_TERROR_2, _killer, loc);
						break;
					case DRILL_GOLEM_OF_TERROR_3:
						spawnNextMob(DRILL_GOLEM_OF_TERROR_1, _killer, loc);
						break;
				}
			}
		}
	}
	
	/**
	 * Method spawnNextMob.
	 * @param npcId int
	 * @param killer Creature
	 * @param loc Location
	 */
	static void spawnNextMob(int npcId, Creature killer, Location loc)
	{
		final SimpleSpawner sp = new SimpleSpawner(npcId);
		sp.setLocx(loc.x);
		sp.setLocy(loc.y);
		sp.setLocz(loc.z);
		final NpcInstance npc = sp.doSpawn(true);
		npc.setHeading(PositionUtils.calculateHeadingFrom(npc, killer));
		npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, killer, 1000);
	}
}
