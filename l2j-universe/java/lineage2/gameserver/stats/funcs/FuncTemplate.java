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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import lineage2.gameserver.stats.Stats;
import lineage2.gameserver.stats.conditions.Condition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class FuncTemplate
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(FuncTemplate.class);
	/**
	 * Field EMPTY_ARRAY.
	 */
	public static final FuncTemplate[] EMPTY_ARRAY = new FuncTemplate[0];
	/**
	 * Field _applyCond.
	 */
	public Condition _applyCond;
	/**
	 * Field _func.
	 */
	public Class<?> _func;
	/**
	 * Field _constructor.
	 */
	public Constructor<?> _constructor;
	/**
	 * Field _stat.
	 */
	public Stats _stat;
	/**
	 * Field _order.
	 */
	public int _order;
	/**
	 * Field _value.
	 */
	public double _value;
	
	/**
	 * Constructor for FuncTemplate.
	 * @param applyCond Condition
	 * @param func String
	 * @param stat Stats
	 * @param order int
	 * @param value double
	 */
	public FuncTemplate(Condition applyCond, String func, Stats stat, int order, double value)
	{
		_applyCond = applyCond;
		_stat = stat;
		_order = order;
		_value = value;
		try
		{
			_func = Class.forName("lineage2.gameserver.stats.funcs.Func" + func);
			_constructor = _func.getConstructor(new Class[]
			{
				Stats.class,
				Integer.TYPE,
				Object.class,
				Double.TYPE
			});
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
	}
	
	/**
	 * Method getFunc.
	 * @param owner Object
	 * @return Func
	 */
	public Func getFunc(Object owner)
	{
		try
		{
			Func f = (Func) _constructor.newInstance(_stat, _order, owner, _value);
			if (_applyCond != null)
			{
				f.setCondition(_applyCond);
			}
			return f;
		}
		catch (IllegalAccessException e)
		{
			_log.error("", e);
			return null;
		}
		catch (InstantiationException e)
		{
			_log.error("", e);
			return null;
		}
		catch (InvocationTargetException e)
		{
			_log.error("", e);
			return null;
		}
	}
}
