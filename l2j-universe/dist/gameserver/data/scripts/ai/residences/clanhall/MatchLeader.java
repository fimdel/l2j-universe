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
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MatchLeader extends MatchFighter
{
	/**
	 * Field ATTACK_SKILL.
	 */
	public static final Skill ATTACK_SKILL = SkillTable.getInstance().getInfo(4077, 6);
	
	/**
	 * Constructor for MatchLeader.
	 * @param actor NpcInstance
	 */
	public MatchLeader(NpcInstance actor)
	{
		super(actor);
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
		if (Rnd.chance(10))
		{
			addTaskCast(attacker, ATTACK_SKILL);
		}
	}
}
