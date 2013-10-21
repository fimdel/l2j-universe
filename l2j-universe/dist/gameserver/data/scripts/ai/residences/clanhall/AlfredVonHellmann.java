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
package ai.residences.clanhall;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.events.impl.ClanHallSiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SpawnExObject;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.PositionUtils;
import lineage2.gameserver.utils.ReflectionUtils;
import ai.residences.SiegeGuardFighter;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AlfredVonHellmann extends SiegeGuardFighter
{
	/**
	 * Field DAMAGE_SKILL.
	 */
	public static final Skill DAMAGE_SKILL = SkillTable.getInstance().getInfo(5000, 1);
	/**
	 * Field DRAIN_SKILL.
	 */
	public static final Skill DRAIN_SKILL = SkillTable.getInstance().getInfo(5001, 1);
	/**
	 * Field ZONE_3.
	 */
	private static final Zone ZONE_3 = ReflectionUtils.getZone("lidia_zone3");
	
	/**
	 * Constructor for AlfredVonHellmann.
	 * @param actor NpcInstance
	 */
	public AlfredVonHellmann(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtSpawn.
	 */
	@Override
	public void onEvtSpawn()
	{
		super.onEvtSpawn();
		ZONE_3.setActive(true);
		Functions.npcShout(getActor(), NpcString.HEH_HEH_I_SEE_THAT_THE_FEAST_HAS_BEGAN_BE_WARY_THE_CURSE_OF_THE_HELLMANN_FAMILY_HAS_POISONED_THIS_LAND);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	public void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		super.onEvtDead(killer);
		ZONE_3.setActive(false);
		Functions.npcShout(actor, NpcString.AARGH_IF_I_DIE_THEN_THE_MAGIC_FORCE_FIELD_OF_BLOOD_WILL);
		final ClanHallSiegeEvent siegeEvent = actor.getEvent(ClanHallSiegeEvent.class);
		if (siegeEvent == null)
		{
			return;
		}
		final SpawnExObject spawnExObject = siegeEvent.getFirstObject(ClanHallSiegeEvent.BOSS);
		final NpcInstance lidiaNpc = spawnExObject.getFirstSpawned();
		if (lidiaNpc.getCurrentHpRatio() == 1.)
		{
			lidiaNpc.setCurrentHp(lidiaNpc.getMaxHp() >> 1, true);
		}
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	public void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		super.onEvtAttacked(attacker, damage);
		if ((PositionUtils.calculateDistance(attacker, actor, false) > 300.) && Rnd.chance(0.13))
		{
			addTaskCast(attacker, DRAIN_SKILL);
		}
		final Creature target = actor.getAggroList().getMostHated();
		if ((target.equals(attacker)) && Rnd.chance(0.3))
		{
			addTaskCast(attacker, DAMAGE_SKILL);
		}
	}
}
