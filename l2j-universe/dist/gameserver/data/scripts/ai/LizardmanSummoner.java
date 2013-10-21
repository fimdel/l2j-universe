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

import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Mystic;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.PositionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LizardmanSummoner extends Mystic
{
	/**
	 * Field TANTA_LIZARDMAN_SCOUT.
	 */
	private static final int TANTA_LIZARDMAN_SCOUT = 22768;
	/**
	 * Field SPAWN_COUNT.
	 */
	private static final int SPAWN_COUNT = 2;
	/**
	 * Field spawnedMobs.
	 */
	private boolean spawnedMobs = false;
	
	/**
	 * Constructor for LizardmanSummoner.
	 * @param actor NpcInstance
	 */
	public LizardmanSummoner(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		spawnedMobs = false;
		super.onEvtSpawn();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		if (!spawnedMobs && attacker.isPlayable())
		{
			final NpcInstance actor = getActor();
			for (int i = 0; i < SPAWN_COUNT; i++)
			{
				try
				{
					SimpleSpawner sp = new SimpleSpawner(TANTA_LIZARDMAN_SCOUT);
					// @SuppressWarnings("unused")
					// int radius = (((i % 2) == 0) ? -1 : 1) * 16000;
					sp.setLoc(actor.getLoc());
					NpcInstance npc = sp.doSpawn(true);
					npc.setHeading(PositionUtils.calculateHeadingFrom(npc, attacker));
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 1000);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			spawnedMobs = true;
		}
		super.onEvtAttacked(attacker, damage);
	}
}
