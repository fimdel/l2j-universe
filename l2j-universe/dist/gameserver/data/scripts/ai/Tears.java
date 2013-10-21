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

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.ai.DefaultAI;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Tears extends DefaultAI
{
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
			for (NpcInstance npc : spawns)
			{
				if (npc != null)
				{
					npc.deleteMe();
				}
			}
			spawns.clear();
			despawnTask = null;
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class SpawnMobsTask extends RunnableImpl
	{
		/**
		 * Constructor for SpawnMobsTask.
		 */
		SpawnMobsTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			spawnMobs();
			spawnTask = null;
		}
	}
	
	/**
	 * Field Invincible.
	 */
	final Skill Invincible;
	/**
	 * Field Freezing.
	 */
	final Skill Freezing;
	/**
	 * Field Water_Dragon_Scale. (value is 2369)
	 */
	private static final int Water_Dragon_Scale = 2369;
	/**
	 * Field Tears_Copy. (value is 25535)
	 */
	private static final int Tears_Copy = 25535;
	/**
	 * Field spawnTask.
	 */
	ScheduledFuture<?> spawnTask;
	/**
	 * Field despawnTask.
	 */
	ScheduledFuture<?> despawnTask;
	/**
	 * Field spawns.
	 */
	List<NpcInstance> spawns = new ArrayList<>();
	/**
	 * Field _isUsedInvincible.
	 */
	private boolean _isUsedInvincible = false;
	/**
	 * Field _scale_count.
	 */
	private int _scale_count = 0;
	/**
	 * Field _last_scale_time.
	 */
	private long _last_scale_time = 0;
	
	/**
	 * Constructor for Tears.
	 * @param actor NpcInstance
	 */
	public Tears(NpcInstance actor)
	{
		super(actor);
		final TIntObjectHashMap<Skill> skills = getActor().getTemplate().getSkills();
		Invincible = skills.get(5420);
		Freezing = skills.get(5238);
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
		if (actor.isDead() || (skill == null) || (caster == null))
		{
			return;
		}
		if ((System.currentTimeMillis() - _last_scale_time) > 5000)
		{
			_scale_count = 0;
		}
		if (skill.getId() == Water_Dragon_Scale)
		{
			_scale_count++;
			_last_scale_time = System.currentTimeMillis();
		}
		final Player player = caster.getPlayer();
		if (player == null)
		{
			return;
		}
		int count = 1;
		final Party party = player.getParty();
		if (party != null)
		{
			count = party.getMemberCount();
		}
		if (_scale_count >= count)
		{
			_scale_count = 0;
			actor.getEffectList().stopEffect(Invincible);
		}
	}
	
	/**
	 * Method createNewTask.
	 * @return boolean
	 */
	@Override
	protected boolean createNewTask()
	{
		clearTasks();
		final Creature target = prepareTarget();
		if (target == null)
		{
			return false;
		}
		final NpcInstance actor = getActor();
		if (actor.isDead())
		{
			return false;
		}
		final double distance = actor.getDistance(target);
		final double actor_hp_precent = actor.getCurrentHpPercents();
		final int rnd_per = Rnd.get(100);
		if ((actor_hp_precent < 15) && !_isUsedInvincible)
		{
			_isUsedInvincible = true;
			addTaskBuff(actor, Invincible);
			Functions.npcSay(actor, "Готов�?те�?�? к �?мерти!!!");
			return true;
		}
		if ((rnd_per < 5) && (spawnTask == null) && (despawnTask == null))
		{
			actor.broadcastPacketToOthers(new MagicSkillUse(actor, actor, 5441, 1, 3000, 0));
			spawnTask = ThreadPoolManager.getInstance().schedule(new SpawnMobsTask(), 3000);
			return true;
		}
		if (!actor.isAMuted() && (rnd_per < 75))
		{
			return chooseTaskAndTargets(null, target, distance);
		}
		return chooseTaskAndTargets(Freezing, target, distance);
	}
	
	/**
	 * Method spawnMobs.
	 */
	void spawnMobs()
	{
		final NpcInstance actor = getActor();
		Location pos;
		Creature hated;
		for (int i = 0; i < 9; i++)
		{
			try
			{
				pos = Location.findPointToStay(144298, 154420, -11854, 300, 320, actor.getGeoIndex());
				SimpleSpawner sp = new SimpleSpawner(NpcHolder.getInstance().getTemplate(Tears_Copy));
				sp.setLoc(pos);
				sp.setReflection(actor.getReflection());
				NpcInstance copy = sp.doSpawn(true);
				spawns.add(copy);
				hated = actor.getAggroList().getRandomHated();
				if (hated != null)
				{
					copy.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, hated, Rnd.get(1, 100));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		pos = Location.findPointToStay(144298, 154420, -11854, 300, 320, actor.getReflectionId());
		actor.teleToLocation(pos);
		hated = actor.getAggroList().getRandomHated();
		if (hated != null)
		{
			actor.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, hated, Rnd.get(1, 100));
		}
		if (despawnTask != null)
		{
			despawnTask.cancel(false);
		}
		despawnTask = ThreadPoolManager.getInstance().schedule(new DeSpawnTask(), 30000);
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
}
