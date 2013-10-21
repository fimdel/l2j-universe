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
package ai.residences.fortress.siege;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.entity.events.impl.FortressSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import npc.model.residences.SiegeGuardInstance;
import ai.residences.SiegeGuardRanger;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ArcherCaption extends SiegeGuardRanger
{
	/**
	 * Constructor for ArcherCaption.
	 * @param actor NpcInstance
	 */
	public ArcherCaption(NpcInstance actor)
	{
		super(actor);
		actor.addListener(FortressSiegeEvent.RESTORE_BARRACKS_LISTENER);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();
		final SiegeGuardInstance actor = getActor();
		final FortressSiegeEvent siegeEvent = actor.getEvent(FortressSiegeEvent.class);
		if (siegeEvent == null)
		{
			return;
		}
		if (siegeEvent.getResidence().getFacilityLevel(Fortress.GUARD_BUFF) > 0)
		{
			actor.doCast(SkillTable.getInstance().getInfo(5432, siegeEvent.getResidence().getFacilityLevel(Fortress.GUARD_BUFF)), actor, false);
		}
		siegeEvent.barrackAction(0, false);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	public void onEvtDead(Creature killer)
	{
		final SiegeGuardInstance actor = getActor();
		final FortressSiegeEvent siegeEvent = actor.getEvent(FortressSiegeEvent.class);
		if (siegeEvent == null)
		{
			return;
		}
		siegeEvent.barrackAction(0, true);
		siegeEvent.broadcastTo(SystemMsg.THE_BARRACKS_HAVE_BEEN_SEIZED, SiegeEvent.ATTACKERS, SiegeEvent.DEFENDERS);
		Functions.npcShout(actor, NpcString.YOU_MAY_HAVE_BROKEN_OUR_ARROWS_BUT_YOU_WILL_NEVER_BREAK_OUR_WILL_ARCHERS_RETREAT);
		super.onEvtDead(killer);
		siegeEvent.checkBarracks();
	}
}
