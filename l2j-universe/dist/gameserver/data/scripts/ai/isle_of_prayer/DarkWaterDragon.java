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
package ai.isle_of_prayer;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DarkWaterDragon extends Fighter
{
	/**
	 * Field _mobsSpawned.
	 */
	private int _mobsSpawned = 0;
	/**
	 * Field FAFURION. (value is 18482)
	 */
	private static final int FAFURION = 18482;
	/**
	 * Field SHADE1. (value is 22268)
	 */
	private static final int SHADE1 = 22268;
	/**
	 * Field SHADE2. (value is 22269)
	 */
	private static final int SHADE2 = 22269;
	/**
	 * Field MOBS.
	 */
	private static final int[] MOBS =
	{
		SHADE1,
		SHADE2
	};
	/**
	 * Field MOBS_COUNT. (value is 5)
	 */
	private static final int MOBS_COUNT = 5;
	/**
	 * Field RED_CRYSTAL. (value is 9596)
	 */
	private static final int RED_CRYSTAL = 9596;
	
	/**
	 * Constructor for DarkWaterDragon.
	 * @param actor NpcInstance
	 */
	public DarkWaterDragon(NpcInstance actor)
	{
		super(actor);
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
		if (!actor.isDead())
		{
			switch (_mobsSpawned)
			{
				case 0:
					_mobsSpawned = 1;
					spawnShades(attacker);
					break;
				case 1:
					if (actor.getCurrentHp() < (actor.getMaxHp() >> 1))
					{
						_mobsSpawned = 2;
						spawnShades(attacker);
					}
					break;
			}
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method spawnShades.
	 * @param attacker Creature
	 */
	private void spawnShades(Creature attacker)
	{
		final NpcInstance actor = getActor();
		for (int i = 0; i < MOBS_COUNT; i++)
		{
			try
			{
				SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(MOBS[Rnd.get(MOBS.length)]));
				sp.setLoc(Location.findPointToStay(actor, 100, 120));
				NpcInstance npc = sp.doSpawn(true);
				npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, Rnd.get(1, 100));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		_mobsSpawned = 0;
		final NpcInstance actor = getActor();
		try
		{
			final SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(FAFURION));
			sp.setLoc(Location.findPointToStay(actor, 100, 120));
			sp.doSpawn(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		if (killer != null)
		{
			final Player player = killer.getPlayer();
			if (player != null)
			{
				if (Rnd.chance(77))
				{
					actor.dropItem(player, RED_CRYSTAL, 1);
				}
			}
		}
		super.onEvtDead(killer);
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return _mobsSpawned == 0;
	}
}
