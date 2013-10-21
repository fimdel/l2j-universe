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
import ai.residences.SiegeGuardMystic;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GiselleVonHellmann extends SiegeGuardMystic
{
	/**
	 * Field DAMAGE_SKILL.
	 */
	private static final Skill DAMAGE_SKILL = SkillTable.getInstance().getInfo(5003, 1);
	/**
	 * Field ZONE_1.
	 */
	private static final Zone ZONE_1 = ReflectionUtils.getZone("lidia_zone1");
	/**
	 * Field ZONE_2.
	 */
	private static final Zone ZONE_2 = ReflectionUtils.getZone("lidia_zone2");
	
	/**
	 * Constructor for GiselleVonHellmann.
	 * @param actor NpcInstance
	 */
	public GiselleVonHellmann(NpcInstance actor)
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
		ZONE_1.setActive(true);
		ZONE_2.setActive(true);
		Functions.npcShout(getActor(), NpcString.ARISE_MY_FAITHFUL_SERVANTS_YOU_MY_PEOPLE_WHO_HAVE_INHERITED_THE_BLOOD);
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
		ZONE_1.setActive(false);
		ZONE_2.setActive(false);
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
			addTaskCast(attacker, DAMAGE_SKILL);
		}
	}
}
