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
package lineage2.gameserver.ai;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DoorAI extends CharacterAI
{
	/**
	 * Constructor for DoorAI.
	 * @param actor DoorInstance
	 */
	public DoorAI(DoorInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtTwiceClick.
	 * @param player Player
	 */
	public void onEvtTwiceClick(Player player)
	{
	}
	
	/**
	 * Method onEvtOpen.
	 * @param player Player
	 */
	public void onEvtOpen(Player player)
	{
	}
	
	/**
	 * Method onEvtClose.
	 * @param player Player
	 */
	public void onEvtClose(Player player)
	{
	}
	
	/**
	 * Method getActor.
	 * @return DoorInstance
	 */
	@Override
	public DoorInstance getActor()
	{
		return (DoorInstance) super.getActor();
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		Creature actor;
		if ((attacker == null) || ((actor = getActor()) == null))
		{
			return;
		}
		Player player = attacker.getPlayer();
		if (player == null)
		{
			return;
		}
		SiegeEvent<?, ?> siegeEvent1 = player.getEvent(SiegeEvent.class);
		SiegeEvent<?, ?> siegeEvent2 = actor.getEvent(SiegeEvent.class);
		if ((siegeEvent1 == null) || ((siegeEvent1 == siegeEvent2) && (siegeEvent1.getSiegeClan(SiegeEvent.ATTACKERS, player.getClan()) != null)))
		{
			for (NpcInstance npc : actor.getAroundNpc(900, 200))
			{
				if (!npc.isSiegeGuard())
				{
					continue;
				}
				if (Rnd.chance(20))
				{
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 10000);
				}
				else
				{
					npc.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, attacker, 2000);
				}
			}
		}
	}
}
