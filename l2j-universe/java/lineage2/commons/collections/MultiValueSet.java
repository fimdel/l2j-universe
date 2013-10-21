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
package lineage2.commons.collections;

import java.util.HashMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MultiValueSet<T> extends HashMap<T, Object>
{
	/**
	 * Field serialVersionUID. (value is 8071544899414292397)
	 */
	private static final long serialVersionUID = 8071544899414292397L;
	
	/**
	 * Constructor for MultiValueSet.
	 */
	public MultiValueSet()
	{
		super();
	}
	
	/**
	 * Constructor for MultiValueSet.
	 * @param size int
	 */
	public MultiValueSet(int size)
	{
		super(size);
	}
	
	/**
	 * Constructor for MultiValueSet.
	 * @param set MultiValueSet<T>
	 */
	public MultiValueSet(MultiValueSet<T> set)
	{
		super(set);
	}
	
	/**
	 * Method set.
	 * @param key T
	 * @param value Object
	 */
	public void set(T key, Object value)
	{
		put(key, value);
	}
	
	/**
	 * Method set.
	 * @param key T
	 * @param value String
	 */
	public void set(T key, String value)
	{
		put(key, value);
	}
	
	/**
	 * Method set.
	 * @param key T
	 * @param value boolean
	 */
	public void set(T key, boolean value)
	{
		put(key, value ? Boolean.TRUE : Boolean.FALSE);
	}
	
	/**
	 * Method set.
	 * @param key T
	 * @param value int
	 */
	public void set(T key, int value)
	{
		put(key, Integer.valueOf(value));
	}
	
	/**
	 * Method set.
	 * @param key T
	 * @param value int[]
	 */
	public void set(T key, int[] value)
	{
		put(key, value);
	}
	
	/**
	 * Method set.
	 * @param key T
	 * @param value long
	 */
	public void set(T key, long value)
	{
		put(key, Long.valueOf(value));
	}
	
	/**
	 * Method set.
	 * @param key T
	 * @param value double
	 */
	public void set(T key, double value)
	{
		put(key, Double.valueOf(value));
	}
	
	/**
	 * Method set.
	 * @param key T
	 * @param value Enum<?>
	 */
	public void set(T key, Enum<?> value)
	{
		put(key, value);
	}
	
	/**
	 * Method unset.
	 * @param key T
	 */
	public void unset(T key)
	{
		remove(key);
	}
	
	/**
	 * Method isSet.
	 * @param key T
	 * @return boolean
	 */
	public boolean isSet(T key)
	{
		return get(key) != null;
	}
	
	/**
	 * Method clone.
	 * @return MultiValueSet<T>
	 */
	@Override
	public MultiValueSet<T> clone()
	{
		return new MultiValueSet<>(this);
	}
	
	/**
	 * Method getBool.
	 * @param key T
	 * @return boolean
	 */
	public boolean getBool(T key)
	{
		Object val = get(key);
		if (val instanceof Number)
		{
			return ((Number) val).intValue() != 0;
		}
		if (val instanceof String)
		{
			return Boolean.parseBoolean((String) val);
		}
		if (val instanceof Boolean)
		{
			return ((Boolean) val).booleanValue();
		}
		throw new IllegalArgumentException("Boolean value required, but found: " + val + "!");
	}
	
	/**
	 * Method getBool.
	 * @param key T
	 * @param defaultValue boolean
	 * @return boolean
	 */
	public boolean getBool(T key, boolean defaultValue)
	{
		Object val = get(key);
		if (val instanceof Number)
		{
			return ((Number) val).intValue() != 0;
		}
		if (val instanceof String)
		{
			return Boolean.parseBoolean((String) val);
		}
		if (val instanceof Boolean)
		{
			return ((Boolean) val).booleanValue();
		}
		return defaultValue;
	}
	
	/**
	 * Method getInteger.
	 * @param key T
	 * @return int
	 */
	public int getInteger(T key)
	{
		return getInteger(key, 0);
	}
	
	/**
	 * Method getInteger.
	 * @param key T
	 * @param defaultValue int
	 * @return int
	 */
	public int getInteger(T key, int defaultValue)
	{
		Object val = get(key);
		if (val instanceof Number)
		{
			return ((Number) val).intValue();
		}
		if (val instanceof String)
		{
			return Integer.parseInt((String) val);
		}
		if (val instanceof Boolean)
		{
			return ((Boolean) val).booleanValue() ? 1 : 0;
		}
		return defaultValue;
	}
	
	/**
	 * Method getIntegerArray.
	 * @param key T
	 * @return int[]
	 */
	public int[] getIntegerArray(T key)
	{
		Object val = get(key);
		if (val instanceof int[])
		{
			return (int[]) val;
		}
		if (val instanceof Number)
		{
			return new int[]
			{
				((Number) val).intValue()
			};
		}
		if (val instanceof String)
		{
			String[] vals = ((String) val).split(";");
			int[] result = new int[vals.length];
			int i = 0;
			for (String v : vals)
			{
				result[i++] = Integer.parseInt(v);
			}
			return result;
		}
		throw new IllegalArgumentException("Integer array required, but found: " + val + "!");
	}
	
	/**
	 * Method getIntegerArray.
	 * @param key T
	 * @param defaultArray int[]
	 * @return int[]
	 */
	public int[] getIntegerArray(T key, int[] defaultArray)
	{
		try
		{
			return getIntegerArray(key);
		}
		catch (IllegalArgumentException e)
		{
			return defaultArray;
		}
	}
	
	/**
	 * Method getLong.
	 * @param key T
	 * @return long
	 */
	public long getLong(T key)
	{
		Object val = get(key);
		if (val instanceof Number)
		{
			return ((Number) val).longValue();
		}
		if (val instanceof String)
		{
			return Long.parseLong((String) val);
		}
		if (val instanceof Boolean)
		{
			return ((Boolean) val).booleanValue() ? 1L : 0L;
		}
		throw new IllegalArgumentException("Long value required, but found: " + val + "!");
	}
	
	/**
	 * Method getLong.
	 * @param key T
	 * @param defaultValue long
	 * @return long
	 */
	public long getLong(T key, long defaultValue)
	{
		Object val = get(key);
		if (val instanceof Number)
		{
			return ((Number) val).longValue();
		}
		if (val instanceof String)
		{
			return Long.parseLong((String) val);
		}
		if (val instanceof Boolean)
		{
			return ((Boolean) val).booleanValue() ? 1L : 0L;
		}
		return defaultValue;
	}
	
	/**
	 * Method getDouble.
	 * @param key T
	 * @return double
	 */
	public double getDouble(T key)
	{
		Object val = get(key);
		if (val instanceof Number)
		{
			return ((Number) val).doubleValue();
		}
		if (val instanceof String)
		{
			return Double.parseDouble((String) val);
		}
		if (val instanceof Boolean)
		{
			return ((Boolean) val).booleanValue() ? 1. : 0.;
		}
		throw new IllegalArgumentException("Double value required, but found: " + val + "!");
	}
	
	/**
	 * Method getDouble.
	 * @param key T
	 * @param defaultValue double
	 * @return double
	 */
	public double getDouble(T key, double defaultValue)
	{
		Object val = get(key);
		if (val instanceof Number)
		{
			return ((Number) val).doubleValue();
		}
		if (val instanceof String)
		{
			return Double.parseDouble((String) val);
		}
		if (val instanceof Boolean)
		{
			return ((Boolean) val).booleanValue() ? 1. : 0.;
		}
		return defaultValue;
	}
	
	/**
	 * Method getString.
	 * @param key T
	 * @return String
	 */
	public String getString(T key)
	{
		Object val = get(key);
		if (val != null)
		{
			return String.valueOf(val);
		}
		throw new IllegalArgumentException("String value required, but not specified!");
	}
	
	/**
	 * Method getString.
	 * @param key T
	 * @param defaultValue String
	 * @return String
	 */
	public String getString(T key, String defaultValue)
	{
		Object val = get(key);
		if (val != null)
		{
			return String.valueOf(val);
		}
		return defaultValue;
	}
	
	/**
	 * Method getObject.
	 * @param key T
	 * @return Object
	 */
	public Object getObject(T key)
	{
		return get(key);
	}
	
	/**
	 * Method getObject.
	 * @param key T
	 * @param defaultValue Object
	 * @return Object
	 */
	public Object getObject(T key, Object defaultValue)
	{
		Object val = get(key);
		if (val != null)
		{
			return val;
		}
		return defaultValue;
	}
	
	/**
	 * Method getEnum.
	 * @param name T
	 * @param enumClass Class<E>
	 * @return E
	 */
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>> E getEnum(T name, Class<E> enumClass)
	{
		Object val = get(name);
		if ((val != null) && enumClass.isInstance(val))
		{
			return (E) val;
		}
		if (val instanceof String)
		{
			return Enum.valueOf(enumClass, (String) val);
		}
		throw new IllegalArgumentException("Enum value of type " + enumClass.getName() + "required, but found: " + val + "!");
	}
	
	/**
	 * Method getEnum.
	 * @param name T
	 * @param enumClass Class<E>
	 * @param defaultValue E
	 * @return E
	 */
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>> E getEnum(T name, Class<E> enumClass, E defaultValue)
	{
		Object val = get(name);
		if ((val != null) && enumClass.isInstance(val))
		{
			return (E) val;
		}
		if (val instanceof String)
		{
			return Enum.valueOf(enumClass, (String) val);
		}
		return defaultValue;
	}
}
