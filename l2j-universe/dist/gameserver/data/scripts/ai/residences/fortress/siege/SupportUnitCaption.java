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

import lineage2.commons.util.Rnd;
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
import ai.residences.SiegeGuardMystic;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SupportUnitCaption extends SiegeGuardMystic
{
	/**
	 * Constructor for SupportUnitCaption.
	 * @param actor NpcInstance
	 */
	public SupportUnitCaption(NpcInstance actor)
	{
		super(actor);
		actor.addListener(FortressSiegeEvent.RESTORE_BARRACKS_LISTENER);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param dam int
	 */
	@Override
	public void onEvtAttacked(Creature attacker, int dam)
	{
		super.onEvtAttacked(attacker, dam);
		final SiegeGuardInstance actor = getActor();
		if (Rnd.chance(1))
		{
			Functions.npcShout(actor, NpcString.SPIRIT_OF_FIRE_UNLEASH_YOUR_POWER_BURN_THE_ENEMY);
		}
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
		siegeEvent.barrackAction(2, false);
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
		siegeEvent.barrackAction(2, true);
		siegeEvent.broadcastTo(SystemMsg.THE_BARRACKS_HAVE_BEEN_SEIZED, SiegeEvent.ATTACKERS, SiegeEvent.DEFENDERS);
		Functions.npcShout(actor, NpcString.AT_LAST_THE_MAGIC_FIELD_THAT_PROTECTS_THE_FORTRESS_HAS_WEAKENED_VOLUNTEERS_STAND_BACK);
		super.onEvtDead(killer);
		siegeEvent.checkBarracks();
	}
}
