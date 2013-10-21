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
package lineage2.commons.util;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TroveUtils
{
	/**
	 * Field EMPTY_INT_OBJECT_MAP.
	 */
	@SuppressWarnings("rawtypes")
	private static final TIntObjectHashMap EMPTY_INT_OBJECT_MAP = new TIntObjectHashMapEmpty();
	/**
	 * Field EMPTY_INT_ARRAY_LIST.
	 */
	public static final TIntArrayList EMPTY_INT_ARRAY_LIST = new TIntArrayListEmpty();
	
	/**
	 * Method emptyIntObjectMap.
	 * @return TIntObjectHashMap<V>
	 */
	@SuppressWarnings("unchecked")
	public static <V> TIntObjectHashMap<V> emptyIntObjectMap()
	{
		return EMPTY_INT_OBJECT_MAP;
	}
	
	/**
	 * @author Mobius
	 */
	private static class TIntObjectHashMapEmpty<V> extends TIntObjectHashMap<V>
	{
		/**
		 * Constructor for TIntObjectHashMapEmpty.
		 */
		TIntObjectHashMapEmpty()
		{
			super(0);
		}
		
		/**
		 * Method put.
		 * @param key int
		 * @param value V
		 * @return V * @see gnu.trove.map.TIntObjectMap#put(int, V)
		 */
		@Override
		public V put(int key, V value)
		{
			throw new UnsupportedOperationException();
		}
		
		/**
		 * Method putIfAbsent.
		 * @param key int
		 * @param value V
		 * @return V * @see gnu.trove.map.TIntObjectMap#putIfAbsent(int, V)
		 */
		@Override
		public V putIfAbsent(int key, V value)
		{
			throw new UnsupportedOperationException();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class TIntArrayListEmpty extends TIntArrayList
	{
		/**
		 * Constructor for TIntArrayListEmpty.
		 */
		TIntArrayListEmpty()
		{
			super(0);
		}
		
		/**
		 * Method add.
		 * @param val int
		 * @return boolean * @see gnu.trove.list.TIntList#add(int)
		 */
		@Override
		public boolean add(int val)
		{
			throw new UnsupportedOperationException();
		}
	}
}
