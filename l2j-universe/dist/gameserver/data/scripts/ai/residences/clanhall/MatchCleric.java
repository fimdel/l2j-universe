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

import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.tables.SkillTable;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MatchCleric extends MatchFighter
{
	/**
	 * Field HEAL.
	 */
	public static final Skill HEAL = SkillTable.getInstance().getInfo(4056, 6);
	
	/**
	 * Constructor for MatchCleric.
	 * @param actor NpcInstance
	 */
	public MatchCleric(NpcInstance actor)
	{
		super(actor);
	}
	
	/**
	 * Method heal.
	 */
	public void heal()
	{
		final NpcInstance actor = getActor();
		addTaskCast(actor, HEAL);
		doTask();
	}
}
