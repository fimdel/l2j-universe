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
package lineage2.gameserver.stats;

import lineage2.commons.lang.ArrayUtils;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.stats.funcs.Func;
import lineage2.gameserver.stats.funcs.FuncOwner;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class Calculator
{
	/**
	 * Field _functions.
	 */
	private Func[] _functions;
	/**
	 * Field _base.
	 */
	private double _base;
	/**
	 * Field _last.
	 */
	private double _last;
	/**
	 * Field _stat.
	 */
	public final Stats _stat;
	/**
	 * Field _character.
	 */
	public final Creature _character;
	
	/**
	 * Constructor for Calculator.
	 * @param stat Stats
	 * @param character Creature
	 */
	public Calculator(Stats stat, Creature character)
	{
		_stat = stat;
		_character = character;
		_functions = Func.EMPTY_FUNC_ARRAY;
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	public int size()
	{
		return _functions.length;
	}
	
	/**
	 * Method addFunc.
	 * @param f Func
	 */
	public void addFunc(Func f)
	{
		_functions = ArrayUtils.add(_functions, f);
		ArrayUtils.eqSort(_functions);
	}
	
	/**
	 * Method removeFunc.
	 * @param f Func
	 */
	public void removeFunc(Func f)
	{
		_functions = ArrayUtils.remove(_functions, f);
		if (_functions.length == 0)
		{
			_functions = Func.EMPTY_FUNC_ARRAY;
		}
		else
		{
			ArrayUtils.eqSort(_functions);
		}
	}
	
	/**
	 * Method removeOwner.
	 * @param owner Object
	 */
	public void removeOwner(Object owner)
	{
		Func[] tmp = _functions;
		for (Func element : tmp)
		{
			if (element.owner == owner)
			{
				removeFunc(element);
			}
		}
	}
	
	/**
	 * Method calc.
	 * @param env Env
	 */
	public void calc(Env env)
	{
		Func[] funcs = _functions;
		_base = env.value;
		boolean overrideLimits = false;
		for (Func func : funcs)
		{
			if (func == null)
			{
				continue;
			}
			if (func.owner instanceof FuncOwner)
			{
				if (!((FuncOwner) func.owner).isFuncEnabled())
				{
					continue;
				}
				if (((FuncOwner) func.owner).overrideLimits())
				{
					overrideLimits = true;
				}
			}
			if ((func.getCondition() == null) || func.getCondition().test(env))
			{
				func.calc(env);
			}
		}
		if (!overrideLimits)
		{
			env.value = _stat.validate(env.value);
		}
		if (env.value != _last)
		{
			_last = env.value;
		}
	}
	
	/**
	 * Method getFunctions.
	 * @return Func[]
	 */
	public Func[] getFunctions()
	{
		return _functions;
	}
	
	/**
	 * Method getBase.
	 * @return double
	 */
	public double getBase()
	{
		return _base;
	}
	
	/**
	 * Method getLast.
	 * @return double
	 */
	public double getLast()
	{
		return _last;
	}
}
