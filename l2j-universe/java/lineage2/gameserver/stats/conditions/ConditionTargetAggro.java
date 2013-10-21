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
import lineage2.gameserver.model.instances.MonsterInstance;
import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ConditionTargetAggro extends Condition
{
	/**
	 * Field _isAggro.
	 */
	private final boolean _isAggro;
	
	/**
	 * Constructor for ConditionTargetAggro.
	 * @param isAggro boolean
	 */
	public ConditionTargetAggro(boolean isAggro)
	{
		_isAggro = isAggro;
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
		if (target == null)
		{
			return false;
		}
		if (target.isMonster())
		{
			return ((MonsterInstance) target).isAggressive() == _isAggro;
		}
		if (target.isPlayer())
		{
			return target.getKarma() < 0;
		}
		return false;
	}
}
