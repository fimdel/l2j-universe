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
import lineage2.gameserver.stats.conditions.Condition;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public abstract class Func implements Comparable<Func>
{
	/**
	 * Field EMPTY_FUNC_ARRAY.
	 */
	public static final Func[] EMPTY_FUNC_ARRAY = new Func[0];
	/**
	 * Field stat.
	 */
	public final Stats stat;
	/**
	 * Field order.
	 */
	public final int order;
	/**
	 * Field owner.
	 */
	public final Object owner;
	/**
	 * Field value.
	 */
	public final double value;
	/**
	 * Field cond.
	 */
	protected Condition cond;
	
	/**
	 * Constructor for Func.
	 * @param stat Stats
	 * @param order int
	 * @param owner Object
	 */
	public Func(Stats stat, int order, Object owner)
	{
		this(stat, order, owner, 0.);
	}
	
	/**
	 * Constructor for Func.
	 * @param stat Stats
	 * @param order int
	 * @param owner Object
	 * @param value double
	 */
	public Func(Stats stat, int order, Object owner, double value)
	{
		this.stat = stat;
		this.order = order;
		this.owner = owner;
		this.value = value;
	}
	
	/**
	 * Method setCondition.
	 * @param cond Condition
	 */
	public void setCondition(Condition cond)
	{
		this.cond = cond;
	}
	
	/**
	 * Method getCondition.
	 * @return Condition
	 */
	public Condition getCondition()
	{
		return cond;
	}
	
	/**
	 * Method calc.
	 * @param env Env
	 */
	public abstract void calc(Env env);
	
	/**
	 * Method compareTo.
	 * @param f Func
	 * @return int * @throws NullPointerException
	 */
	@Override
	public int compareTo(Func f) throws NullPointerException
	{
		return order - f.order;
	}
}
