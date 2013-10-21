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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.funcs.FuncTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FafurionKindred extends Fighter
{
	/**
	 * Field DETRACTOR1. (value is 22270)
	 */
	private static final int DETRACTOR1 = 22270;
	/**
	 * Field DETRACTOR2. (value is 22271)
	 */
	private static final int DETRACTOR2 = 22271;
	/**
	 * Field Spirit_of_the_Lake. (value is 2368)
	 */
	private static final int Spirit_of_the_Lake = 2368;
	/**
	 * Field Water_Dragon_Scale. (value is 9691)
	 */
	private static final int Water_Dragon_Scale = 9691;
	/**
	 * Field Water_Dragon_Claw. (value is 9700)
	 */
	private static final int Water_Dragon_Claw = 9700;
	/**
	 * Field poisonTask.
	 */
	ScheduledFuture<?> poisonTask;
	/**
	 * Field despawnTask.
	 */
	ScheduledFuture<?> despawnTask;
	/**
	 * Field spawns.
	 */
	List<SimpleSpawner> spawns = new ArrayList<>();
	/**
	 * Field ft.
	 */
	private static final FuncTemplate ft = new FuncTemplate(null, "Mul", Stats.HEAL_EFFECTIVNESS, 0x90, 0);
	
	/**
	 * Constructor for FafurionKindred.
	 * @param actor NpcInstance
	 */
	public FafurionKindred(NpcInstance actor)
	{
		super(actor);
		actor.addStatFunc(ft.getFunc(this));
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		spawns.clear();
		ThreadPoolManager.getInstance().schedule(new SpawnTask(DETRACTOR1), 500);
		ThreadPoolManager.getInstance().schedule(new SpawnTask(DETRACTOR2), 500);
		ThreadPoolManager.getInstance().schedule(new SpawnTask(DETRACTOR1), 500);
		ThreadPoolManager.getInstance().schedule(new SpawnTask(DETRACTOR2), 500);
		poisonTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new PoisonTask(), 3000, 3000);
		despawnTask = ThreadPoolManager.getInstance().schedule(new DeSpawnTask(), 300000);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		cleanUp();
		super.onEvtDead(killer);
	}
	
	/**
	 * Method onEvtSeeSpell.
	 * @param skill Skill
	 * @param caster Creature
	 */
	@Override
	protected void onEvtSeeSpell(Skill skill, Creature caster)
	{
		final NpcInstance actor = getActor();
		if (actor.isDead() || (skill == null))
		{
			return;
		}
		if (skill.getId() == Spirit_of_the_Lake)
		{
			actor.setCurrentHp(actor.getCurrentHp() + 3000, false);
		}
		actor.getAggroList().remove(caster, true);
	}
	
	/**
	 * Method randomWalk.
	 * @return boolean
	 */
	@Override
	protected boolean randomWalk()
	{
		return false;
	}
	
	/**
	 * Method cleanUp.
	 */
	void cleanUp()
	{
		if (poisonTask != null)
		{
			poisonTask.cancel(false);
			poisonTask = null;
		}
		if (despawnTask != null)
		{
			despawnTask.cancel(false);
			despawnTask = null;
		}
		for (SimpleSpawner spawn : spawns)
		{
			spawn.deleteAll();
		}
		spawns.clear();
	}
	
	/**
	 * @author Mobius
	 */
	private class SpawnTask extends RunnableImpl
	{
		/**
		 * Field _id.
		 */
		private final int _id;
		
		/**
		 * Constructor for SpawnTask.
		 * @param id int
		 */
		SpawnTask(int id)
		{
			_id = id;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final NpcInstance actor = getActor();
			final SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(_id));
			sp.setLoc(Location.findPointToStay(actor, 100, 120));
			sp.setRespawnDelay(30, 40);
			sp.doSpawn(true);
			spawns.add(sp);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class PoisonTask extends RunnableImpl
	{
		/**
		 * Constructor for PoisonTask.
		 */
		PoisonTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final NpcInstance actor = getActor();
			actor.reduceCurrentHp(500, 0, actor, null, true, false, true, false, false, false, false);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class DeSpawnTask extends RunnableImpl
	{
		/**
		 * Constructor for DeSpawnTask.
		 */
		DeSpawnTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			final NpcInstance actor = getActor();
			dropItem(actor, Water_Dragon_Scale, Rnd.get(1, 2));
			if (Rnd.chance(36))
			{
				dropItem(actor, Water_Dragon_Claw, Rnd.get(1, 3));
			}
			cleanUp();
			actor.deleteMe();
		}
	}
	
	/**
	 * Method dropItem.
	 * @param actor NpcInstance
	 * @param id int
	 * @param count int
	 */
	void dropItem(NpcInstance actor, int id, int count)
	{
		final ItemInstance item = ItemFunctions.createItem(id);
		item.setCount(count);
		item.dropToTheGround(actor, Location.findPointToStay(actor, 100));
	}
}
