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

import lineage2.gameserver.model.AggroList;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.ClanHallSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.templates.npc.NpcTemplate;
import npc.model.residences.SiegeGuardInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LidiaVonHellmannInstance extends SiegeGuardInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for LidiaVonHellmannInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public LidiaVonHellmannInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onDeath.
	 * @param killer Creature
	 */
	@Override
	public void onDeath(Creature killer)
	{
		SiegeEvent<?, ?> siegeEvent = getEvent(SiegeEvent.class);
		if (siegeEvent == null)
		{
			return;
		}
		siegeEvent.processStep(getMostDamagedClan());
		super.onDeath(killer);
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
	 * Method isEffectImmune.
	 * @return boolean
	 */
	@Override
	public boolean isEffectImmune()
	{
		return true;
	}
}
