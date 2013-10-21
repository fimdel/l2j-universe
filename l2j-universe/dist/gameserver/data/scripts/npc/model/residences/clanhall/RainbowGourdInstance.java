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

import java.util.List;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Spawner;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.entity.events.impl.ClanHallMiniGameEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.CMGSiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.SpawnExObject;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.NpcUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RainbowGourdInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _winner.
	 */
	CMGSiegeClanObject _winner;
	
	/**
	 * Constructor for RainbowGourdInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public RainbowGourdInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		setHasChatWindow(false);
	}
	
	/**
	 * Method doDecrease.
	 * @param character Creature
	 */
	public void doDecrease(Creature character)
	{
		if (isDead())
		{
			return;
		}
		reduceCurrentHp(getMaxHp() * 0.2, 0, character, null, false, false, false, false, false, false, false);
	}
	
	/**
	 * Method doHeal.
	 */
	public void doHeal()
	{
		if (isDead())
		{
			return;
		}
		setCurrentHp(getCurrentHp() + (getMaxHp() * 0.2), false);
	}
	
	/**
	 * Method doSwitch.
	 * @param npc RainbowGourdInstance
	 */
	public void doSwitch(RainbowGourdInstance npc)
	{
		if (isDead() || npc.isDead())
		{
			return;
		}
		final double currentHp = getCurrentHp();
		setCurrentHp(npc.getCurrentHp(), false);
		npc.setCurrentHp(currentHp, false);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	public void onDeath(Creature killer)
	{
		super.onDeath(killer);
		ClanHallMiniGameEvent miniGameEvent = getEvent(ClanHallMiniGameEvent.class);
		if (miniGameEvent == null)
		{
			return;
		}
		Player player = killer.getPlayer();
		CMGSiegeClanObject siegeClanObject = miniGameEvent.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan());
		if (siegeClanObject == null)
		{
			return;
		}
		_winner = siegeClanObject;
		List<CMGSiegeClanObject> attackers = miniGameEvent.getObjects(SiegeEvent.ATTACKERS);
		for (int i = 0; i < attackers.size(); i++)
		{
			if (attackers.get(i) == siegeClanObject)
			{
				continue;
			}
			String arenaName = "arena_" + i;
			SpawnExObject spawnEx = miniGameEvent.getFirstObject(arenaName);
			RainbowYetiInstance yetiInstance = (RainbowYetiInstance) spawnEx.getSpawns().get(0).getFirstSpawned();
			yetiInstance.teleportFromArena();
			miniGameEvent.spawnAction(arenaName, false);
		}
	}
	
	/**
	 * Method onDecay.
	 */
	@Override
	public void onDecay()
	{
		super.onDecay();
		final ClanHallMiniGameEvent miniGameEvent = getEvent(ClanHallMiniGameEvent.class);
		if (miniGameEvent == null)
		{
			return;
		}
		if (_winner == null)
		{
			return;
		}
		List<CMGSiegeClanObject> attackers = miniGameEvent.getObjects(SiegeEvent.ATTACKERS);
		int index = attackers.indexOf(_winner);
		String arenaName = "arena_" + index;
		miniGameEvent.spawnAction(arenaName, false);
		SpawnExObject spawnEx = miniGameEvent.getFirstObject(arenaName);
		Spawner spawner = spawnEx.getSpawns().get(0);
		Location loc = (Location) spawner.getCurrentSpawnRange();
		miniGameEvent.removeBanishItems();
		final NpcInstance npc = NpcUtils.spawnSingle(35600, loc.x, loc.y, loc.z, 0);
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				List<Player> around = World.getAroundPlayers(npc, 750, 100);
				npc.deleteMe();
				for (Player player : around)
				{
					player.teleToLocation(miniGameEvent.getResidence().getOwnerRestartPoint());
				}
				miniGameEvent.processStep(_winner.getClan());
			}
		}, 10000L);
	}
	
	/**
	 * Method isAttackable.
	 * @param c Creature
	 * @return boolean
	 */
	@Override
	public boolean isAttackable(Creature c)
	{
		return false;
	}
	
	/**
	 * Method isAutoAttackable.
	 * @param c Creature
	 * @return boolean
	 */
	@Override
	public boolean isAutoAttackable(Creature c)
	{
		return false;
	}
	
	/**
	 * Method isInvul.
	 * @return boolean
	 */
	@Override
	public boolean isInvul()
	{
		return false;
	}
}
