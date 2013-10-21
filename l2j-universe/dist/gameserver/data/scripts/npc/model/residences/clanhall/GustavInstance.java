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
package npc.model.residences.clanhall;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.ai.CtrlEvent;
import lineage2.gameserver.model.AggroList;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectTasks;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.events.impl.ClanHallSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SpawnExObject;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import npc.model.residences.SiegeGuardInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GustavInstance extends SiegeGuardInstance implements _34SiegeGuard
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _canDead.
	 */
	private final AtomicBoolean _canDead = new AtomicBoolean();
	/**
	 * Field _teleportTask.
	 */
	private Future<?> _teleportTask;
	
	/**
	 * Constructor for GustavInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public GustavInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onSpawn.
	 */
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		_canDead.set(false);
		Functions.npcShout(this, NpcString.PREPARE_TO_DIE_FOREIGN_INVADERS_I_AM_GUSTAV_THE_ETERNAL_RULER_OF_THIS_FORTRESS_AND_I_HAVE_TAKEN_UP_MY_SWORD_TO_REPEL_THEE);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	public void onDeath(Creature killer)
	{
		if (!_canDead.get())
		{
			_canDead.set(true);
			setCurrentHp(1, true);
			for (Creature cha : World.getAroundCharacters(this))
			{
				ThreadPoolManager.getInstance().execute(new GameObjectTasks.NotifyAITask(cha, CtrlEvent.EVT_FORGET_OBJECT, this, null));
			}
			ClanHallSiegeEvent siegeEvent = getEvent(ClanHallSiegeEvent.class);
			if (siegeEvent == null)
			{
				return;
			}
			SpawnExObject obj = siegeEvent.getFirstObject(ClanHallSiegeEvent.BOSS);
			for (int i = 0; i < 3; i++)
			{
				final NpcInstance npc = obj.getSpawns().get(i).getFirstSpawned();
				Functions.npcSay(npc, ((_34SiegeGuard) npc).teleChatSay());
				npc.broadcastPacket(new MagicSkillUse(npc, npc, 4235, 1, 10000, 0));
				_teleportTask = ThreadPoolManager.getInstance().schedule(new RunnableImpl()
				{
					@Override
					public void runImpl()
					{
						Location loc = Location.findAroundPosition(177134, -18807, -2256, 50, 100, npc.getGeoIndex());
						npc.teleToLocation(loc);
						if (npc == GustavInstance.this)
						{
							npc.reduceCurrentHp(npc.getCurrentHp(), 0, npc, null, false, false, false, false, false, false, false);
						}
					}
				}, 10000L);
			}
		}
		else
		{
			if (_teleportTask != null)
			{
				_teleportTask.cancel(false);
				_teleportTask = null;
			}
			SiegeEvent<?, ?> siegeEvent = getEvent(SiegeEvent.class);
			if (siegeEvent == null)
			{
				return;
			}
			siegeEvent.processStep(getMostDamagedClan());
			super.onDeath(killer);
		}
	}
	
	/**
	 * Method getMostDamagedClan.
	 * @return Clan
	 */
	public Clan getMostDamagedClan()
	{
		ClanHallSiegeEvent siegeEvent = getEvent(ClanHallSiegeEvent.class);
		Player temp = null;
		Map<Player, Integer> damageMap = new HashMap<>();
		for (AggroList.HateInfo info : getAggroList().getPlayableMap().values())
		{
			Playable killer = (Playable) info.attacker;
			int damage = info.damage;
			if (killer.isPet() || killer.isServitor())
			{
				temp = killer.getPlayer();
			}
			else if (killer.isPlayer())
			{
				temp = (Player) killer;
			}
			if ((temp == null) || (siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, temp.getClan()) == null))
			{
				continue;
			}
			if (!damageMap.containsKey(temp))
			{
				damageMap.put(temp, damage);
			}
			else
			{
				int dmg = damageMap.get(temp) + damage;
				damageMap.put(temp, dmg);
			}
		}
		int mostDamage = 0;
		Player player = null;
		for (Map.Entry<Player, Integer> entry : damageMap.entrySet())
		{
			int damage = entry.getValue();
			Player t = entry.getKey();
			if (damage > mostDamage)
			{
				mostDamage = damage;
				player = t;
			}
		}
		return player == null ? null : player.getClan();
	}
	
	/**
	 * Method teleChatSay.
	 * @return NpcString * @see npc.model.residences.clanhall._34SiegeGuard#teleChatSay()
	 */
	@Override
	public NpcString teleChatSay()
	{
		return NpcString.THIS_IS_UNBELIEVABLE_HAVE_I_REALLY_BEEN_DEFEATED_I_SHALL_RETURN_AND_TAKE_YOUR_HEAD;
	}
	
	/**
	 * Method isEffectImmune.
	 * @return boolean
	 */
	@Override
	public boolean isEffectImmune()
	{
		return true;
	}
}
