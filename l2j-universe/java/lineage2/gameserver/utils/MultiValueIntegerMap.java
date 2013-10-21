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
package lineage2.gameserver.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MultiValueIntegerMap
{
	/**
	 * Field map.
	 */
	private final Map<Integer, List<Integer>> map;
	
	/**
	 * Constructor for MultiValueIntegerMap.
	 */
	public MultiValueIntegerMap()
	{
		map = new ConcurrentHashMap<>();
	}
	
	/**
	 * Method keySet.
	 * @return Set<Integer>
	 */
	public Set<Integer> keySet()
	{
		return map.keySet();
	}
	
	/**
	 * Method values.
	 * @return Collection<List<Integer>>
	 */
	public Collection<List<Integer>> values()
	{
		return map.values();
	}
	
	/**
	 * Method allValues.
	 * @return List<Integer>
	 */
	public List<Integer> allValues()
	{
		List<Integer> result = new ArrayList<>();
		for (Map.Entry<Integer, List<Integer>> entry : map.entrySet())
		{
			result.addAll(entry.getValue());
		}
		return result;
	}
	
	/**
	 * Method entrySet.
	 * @return Set<Entry<Integer,List<Integer>>>
	 */
	public Set<Entry<Integer, List<Integer>>> entrySet()
	{
		return map.entrySet();
	}
	
	/**
	 * Method remove.
	 * @param key Integer
	 * @return List<Integer>
	 */
	public List<Integer> remove(Integer key)
	{
		return map.remove(key);
	}
	
	/**
	 * Method get.
	 * @param key Integer
	 * @return List<Integer>
	 */
	public List<Integer> get(Integer key)
	{
		return map.get(key);
	}
	
	/**
	 * Method containsKey.
	 * @param key Integer
	 * @return boolean
	 */
	public boolean containsKey(Integer key)
	{
		return map.containsKey(key);
	}
	
	/**
	 * Method clear.
	 */
	public void clear()
	{
		map.clear();
	}
	
	/**
	 * Method size.
	 * @return int
	 */
	public int size()
	{
		return map.size();
	}
	
	/**
	 * Method isEmpty.
	 * @return boolean
	 */
	public boolean isEmpty()
	{
		return map.isEmpty();
	}
	
	/**
	 * Method remove.
	 * @param key Integer
	 * @param value Integer
	 * @return Integer
	 */
	public Integer remove(Integer key, Integer value)
	{
		List<Integer> valuesForKey = map.get(key);
		if (valuesForKey == null)
		{
			return null;
		}
		boolean removed = valuesForKey.remove(value);
		if (!removed)
		{
			return null;
		}
		if (valuesForKey.isEmpty())
		{
			remove(key);
		}
		return value;
	}
	
	/**
	 * Method removeValue.
	 * @param value Integer
	 * @return Integer
	 */
	public Integer removeValue(Integer value)
	{
		List<Integer> toRemove = new ArrayList<>(1);
		for (Map.Entry<Integer, List<Integer>> entry : map.entrySet())
		{
			entry.getValue().remove(value);
			if (entry.getValue().isEmpty())
			{
				toRemove.add(entry.getKey());
			}
		}
		for (Integer key : toRemove)
		{
			remove(key);
		}
		return value;
	}
	
	/**
	 * Method put.
	 * @param key Integer
	 * @param value Integer
	 * @return Integer
	 */
	public Integer put(Integer key, Integer value)
	{
		List<Integer> coll = map.get(key);
		if (coll == null)
		{
			coll = new CopyOnWriteArrayList<>();
			map.put(key, coll);
		}
		coll.add(value);
		return value;
	}
	
	/**
	 * Method containsValue.
	 * @param value Integer
	 * @return boolean
	 */
	public boolean containsValue(Integer value)
	{
		for (Map.Entry<Integer, List<Integer>> entry : map.entrySet())
		{
			if (entry.getValue().contains(value))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method containsValue.
	 * @param key Integer
	 * @param value Integer
	 * @return boolean
	 */
	public boolean containsValue(Integer key, Integer value)
	{
		List<Integer> coll = map.get(key);
		if (coll == null)
		{
			return false;
		}
		return coll.contains(value);
	}
	
	/**
	 * Method size.
	 * @param key Integer
	 * @return int
	 */
	public int size(Integer key)
	{
		List<Integer> coll = map.get(key);
		if (coll == null)
		{
			return 0;
		}
		return coll.size();
	}
	
	/**
	 * Method putAll.
	 * @param key Integer
	 * @param values Collection<? extends Integer>
	 * @return boolean
	 */
	public boolean putAll(Integer key, Collection<? extends Integer> values)
	{
		if ((values == null) || (values.size() == 0))
		{
			return false;
		}
		boolean result = false;
		List<Integer> coll = map.get(key);
		if (coll == null)
		{
			coll = new CopyOnWriteArrayList<>();
			coll.addAll(values);
			if (coll.size() > 0)
			{
				map.put(key, coll);
				result = true;
			}
		}
		else
		{
			result = coll.addAll(values);
		}
		return result;
	}
	
	/**
	 * Method totalSize.
	 * @return int
	 */
	public int totalSize()
	{
		int total = 0;
		for (Map.Entry<Integer, List<Integer>> entry : map.entrySet())
		{
			total += entry.getValue().size();
		}
		return total;
	}
}
