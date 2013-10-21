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
package ai.hellbound;

import lineage2.gameserver.ai.Fighter;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DemonPrince extends Fighter
{
	/**
	 * Field ULTIMATE_DEFENSE_SKILL_ID. (value is 5044)
	 */
	private static final int ULTIMATE_DEFENSE_SKILL_ID = 5044;
	/**
	 * Field ULTIMATE_DEFENSE_SKILL.
	 */
	private static final Skill ULTIMATE_DEFENSE_SKILL = SkillTable.getInstance().getInfo(ULTIMATE_DEFENSE_SKILL_ID, 3);
	/**
	 * Field TELEPORTATION_CUBIC_ID. (value is 32375)
	 */
	private static final int TELEPORTATION_CUBIC_ID = 32375;
	/**
	 * Field CUBIC_POSITION.
	 */
	private static final Location CUBIC_POSITION = new Location(-22144, 278744, -8239, 0);
	/**
	 * Field _notUsedUltimateDefense.
	 */
	private boolean _notUsedUltimateDefense = true;
	
	/**
	 * Constructor for DemonPrince.
	 * @param actor NpcInstance
	 */
	public DemonPrince(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method onEvtAttacked.
	 * @param attacker Creature
	 * @param damage int
	 */
	@Override
	protected void onEvtAttacked(Creature attacker, int damage)
	{
		final NpcInstance actor = getActor();
		if (_notUsedUltimateDefense && (actor.getCurrentHpPercents() < 10))
		{
			_notUsedUltimateDefense = false;
			clearTasks();
			addTaskBuff(actor, ULTIMATE_DEFENSE_SKILL);
		}
		super.onEvtAttacked(attacker, damage);
	}
	
	/**
	 * Method onEvtDead.
	 * @param killer Creature
	 */
	@Override
	protected void onEvtDead(Creature killer)
	{
		final NpcInstance actor = getActor();
		_notUsedUltimateDefense = true;
		actor.getReflection().setReenterTime(System.currentTimeMillis());
		actor.getReflection().addSpawnWithoutRespawn(TELEPORTATION_CUBIC_ID, CUBIC_POSITION, 0);
		super.onEvtDead(killer);
	}
}
