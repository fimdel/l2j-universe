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
package lineage2.gameserver.stats.conditions;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ConditionTargetHasForbiddenSkill extends Condition
{
	/**
	 * Field _skillId.
	 */
	private final int _skillId;
	
	/**
	 * Constructor for ConditionTargetHasForbiddenSkill.
	 * @param skillId int
	 */
	public ConditionTargetHasForbiddenSkill(int skillId)
	{
		_skillId = skillId;
	}
	
	/**
	 * Method testImpl.
	 * @param env Env
	 * @return boolean
	 */
	@Override
	protected boolean testImpl(Env env)
	{
		Creature target = env.target;
		if (!target.isPlayable())
		{
			return false;
		}
		return !(target.getSkillLevel(_skillId) > 0);
	}
}
