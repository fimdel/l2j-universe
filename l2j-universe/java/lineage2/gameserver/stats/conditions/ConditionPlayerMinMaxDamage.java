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
public class ConditionPlayerMinMaxDamage extends Condition
{
	/**
	 * Field _min.
	 */
	private final double _min;
	/**
	 * Field _max.
	 */
	private final double _max;
	
	/**
	 * Constructor for ConditionPlayerMinMaxDamage.
	 * @param min double
	 * @param max double
	 */
	public ConditionPlayerMinMaxDamage(double min, double max)
	{
		_min = min;
		_max = max;
	}
	
	/**
	 * Method testImpl.
	 * @param env Env
	 * @return boolean
	 */
	@Override
	protected boolean testImpl(Env env)
	{
		if ((_min > 0) && (env.value < _min))
		{
			return false;
		}
		if ((_max > 0) && (env.value > _max))
		{
			return false;
		}
		return true;
	}
}
