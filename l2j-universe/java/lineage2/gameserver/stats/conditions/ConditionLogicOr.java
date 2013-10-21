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

import lineage2.gameserver.stats.Env;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ConditionLogicOr extends Condition
{
	/**
	 * Field emptyConditions.
	 */
	private final static Condition[] emptyConditions = new Condition[0];
	/**
	 * Field _conditions.
	 */
	public Condition[] _conditions = emptyConditions;
	
	/**
	 * Method add.
	 * @param condition Condition
	 */
	public void add(Condition condition)
	{
		if (condition == null)
		{
			return;
		}
		final int len = _conditions.length;
		final Condition[] tmp = new Condition[len + 1];
		System.arraycopy(_conditions, 0, tmp, 0, len);
		tmp[len] = condition;
		_conditions = tmp;
	}
	
	/**
	 * Method testImpl.
	 * @param env Env
	 * @return boolean
	 */
	@Override
	protected boolean testImpl(Env env)
	{
		for (Condition c : _conditions)
		{
			if (c.test(env))
			{
				return true;
			}
		}
		return false;
	}
}
