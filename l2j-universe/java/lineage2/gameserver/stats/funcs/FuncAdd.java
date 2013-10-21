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
package lineage2.gameserver.stats.funcs;

import lineage2.gameserver.stats.Env;
import lineage2.gameserver.stats.Stats;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FuncAdd extends Func
{
	/**
	 * Constructor for FuncAdd.
	 * @param stat Stats
	 * @param order int
	 * @param owner Object
	 * @param value double
	 */
	public FuncAdd(Stats stat, int order, Object owner, double value)
	{
		super(stat, order, owner, value);
	}
	
	/**
	 * Method calc.
	 * @param env Env
	 */
	@Override
	public void calc(Env env)
	{
		env.value += value;
	}
}
