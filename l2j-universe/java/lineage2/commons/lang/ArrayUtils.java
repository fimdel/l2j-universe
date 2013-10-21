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
package lineage2.commons.lang;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ArrayUtils
{
	/**
	 * Field INDEX_NOT_FOUND. (value is -1)
	 */
	public static final int INDEX_NOT_FOUND = -1;
	
	/**
	 * Method valid.
	 * @param array T[]
	 * @param index int
	 * @return T
	 */
	public static <T> T valid(T[] array, int index)
	{
		if (array == null)
		{
			return null;
		}
		if ((index < 0) || (array.length <= index))
		{
			return null;
		}
		return array[index];
	}
	
	/**
	 * Method add.
	 * @param array T[]
	 * @param element T
	 * @return T[]
	 */
	@SuppressWarnings(
	{
		"unchecked",
		"rawtypes"
	})
	public static <T> T[] add(T[] array, T element)
	{
		Class type = array != null ? array.getClass().getComponentType() : element != null ? element.getClass() : Object.class;
		T[] newArray = (T[]) copyArrayGrow(array, type);
		newArray[newArray.length - 1] = element;
		return newArray;
	}
	
	/**
	 * Method copyArrayGrow.
	 * @param array T[]
	 * @param type Class<? extends T>
	 * @return T[]
	 */
	@SuppressWarnings("unchecked")
	private static <T> T[] copyArrayGrow(T[] array, Class<? extends T> type)
	{
		if (array != null)
		{
			int arrayLength = Array.getLength(array);
			T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), arrayLength + 1);
			System.arraycopy(array, 0, newArray, 0, arrayLength);
			return newArray;
		}
		return (T[]) Array.newInstance(type, 1);
	}
	
	/**
	 * Method contains.
	 * @param array T[]
	 * @param value T
	 * @return boolean
	 */
	public static <T> boolean contains(T[] array, T value)
	{
		if (array == null)
		{
			return false;
		}
		for (T element : array)
		{
			if (value == element)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method indexOf.
	 * @param array T[]
	 * @param value T
	 * @param index int
	 * @return int
	 */
	public static <T> int indexOf(T[] array, T value, int index)
	{
		if ((index < 0) || (array.length <= index))
		{
			return INDEX_NOT_FOUND;
		}
		for (int i = index; i < array.length; i++)
		{
			if (value == array[i])
			{
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}
	
	/**
	 * Method remove.
	 * @param array T[]
	 * @param value T
	 * @return T[]
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] remove(T[] array, T value)
	{
		if (array == null)
		{
			return null;
		}
		int index = indexOf(array, value, 0);
		if (index == INDEX_NOT_FOUND)
		{
			return array;
		}
		int length = array.length;
		T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), length - 1);
		System.arraycopy(array, 0, newArray, 0, index);
		if (index < (length - 1))
		{
			System.arraycopy(array, index + 1, newArray, index, length - index - 1);
		}
		return newArray;
	}
	
	/**
	 * Method eqBrute.
	 * @param a T[]
	 * @param lo int
	 * @param hi int
	 */
	private static <T extends Comparable<T>> void eqBrute(T[] a, int lo, int hi)
	{
		if ((hi - lo) == 1)
		{
			if (a[hi].compareTo(a[lo]) < 0)
			{
				T e = a[lo];
				a[lo] = a[hi];
				a[hi] = e;
			}
		}
		else if ((hi - lo) == 2)
		{
			int pmin = a[lo].compareTo(a[lo + 1]) < 0 ? lo : lo + 1;
			pmin = a[pmin].compareTo(a[lo + 2]) < 0 ? pmin : lo + 2;
			if (pmin != lo)
			{
				T e = a[lo];
				a[lo] = a[pmin];
				a[pmin] = e;
			}
			eqBrute(a, lo + 1, hi);
		}
		else if ((hi - lo) == 3)
		{
			int pmin = a[lo].compareTo(a[lo + 1]) < 0 ? lo : lo + 1;
			pmin = a[pmin].compareTo(a[lo + 2]) < 0 ? pmin : lo + 2;
			pmin = a[pmin].compareTo(a[lo + 3]) < 0 ? pmin : lo + 3;
			if (pmin != lo)
			{
				T e = a[lo];
				a[lo] = a[pmin];
				a[pmin] = e;
			}
			int pmax = a[hi].compareTo(a[hi - 1]) > 0 ? hi : hi - 1;
			pmax = a[pmax].compareTo(a[hi - 2]) > 0 ? pmax : hi - 2;
			if (pmax != hi)
			{
				T e = a[hi];
				a[hi] = a[pmax];
				a[pmax] = e;
			}
			eqBrute(a, lo + 1, hi - 1);
		}
	}
	
	/**
	 * Method eqSort.
	 * @param a T[]
	 * @param lo0 int
	 * @param hi0 int
	 */
	private static <T extends Comparable<T>> void eqSort(T[] a, int lo0, int hi0)
	{
		int lo = lo0;
		int hi = hi0;
		if ((hi - lo) <= 3)
		{
			eqBrute(a, lo, hi);
			return;
		}
		T pivot = a[(lo + hi) / 2];
		a[(lo + hi) / 2] = a[hi];
		a[hi] = pivot;
		while (lo < hi)
		{
			while ((a[lo].compareTo(pivot) <= 0) && (lo < hi))
			{
				lo++;
			}
			while ((pivot.compareTo(a[hi]) <= 0) && (lo < hi))
			{
				hi--;
			}
			if (lo < hi)
			{
				T e = a[lo];
				a[lo] = a[hi];
				a[hi] = e;
			}
		}
		a[hi0] = a[hi];
		a[hi] = pivot;
		eqSort(a, lo0, lo - 1);
		eqSort(a, hi + 1, hi0);
	}
	
	/**
	 * Method eqSort.
	 * @param a T[]
	 */
	public static <T extends Comparable<T>> void eqSort(T[] a)
	{
		eqSort(a, 0, a.length - 1);
	}
	
	/**
	 * Method eqBrute.
	 * @param a T[]
	 * @param lo int
	 * @param hi int
	 * @param c Comparator<T>
	 */
	private static <T> void eqBrute(T[] a, int lo, int hi, Comparator<T> c)
	{
		if ((hi - lo) == 1)
		{
			if (c.compare(a[hi], a[lo]) < 0)
			{
				T e = a[lo];
				a[lo] = a[hi];
				a[hi] = e;
			}
		}
		else if ((hi - lo) == 2)
		{
			int pmin = c.compare(a[lo], a[lo + 1]) < 0 ? lo : lo + 1;
			pmin = c.compare(a[pmin], a[lo + 2]) < 0 ? pmin : lo + 2;
			if (pmin != lo)
			{
				T e = a[lo];
				a[lo] = a[pmin];
				a[pmin] = e;
			}
			eqBrute(a, lo + 1, hi, c);
		}
		else if ((hi - lo) == 3)
		{
			int pmin = c.compare(a[lo], a[lo + 1]) < 0 ? lo : lo + 1;
			pmin = c.compare(a[pmin], a[lo + 2]) < 0 ? pmin : lo + 2;
			pmin = c.compare(a[pmin], a[lo + 3]) < 0 ? pmin : lo + 3;
			if (pmin != lo)
			{
				T e = a[lo];
				a[lo] = a[pmin];
				a[pmin] = e;
			}
			int pmax = c.compare(a[hi], a[hi - 1]) > 0 ? hi : hi - 1;
			pmax = c.compare(a[pmax], a[hi - 2]) > 0 ? pmax : hi - 2;
			if (pmax != hi)
			{
				T e = a[hi];
				a[hi] = a[pmax];
				a[pmax] = e;
			}
			eqBrute(a, lo + 1, hi - 1, c);
		}
	}
	
	/**
	 * Method eqSort.
	 * @param a T[]
	 * @param lo0 int
	 * @param hi0 int
	 * @param c Comparator<T>
	 */
	private static <T> void eqSort(T[] a, int lo0, int hi0, Comparator<T> c)
	{
		int lo = lo0;
		int hi = hi0;
		if ((hi - lo) <= 3)
		{
			eqBrute(a, lo, hi, c);
			return;
		}
		T pivot = a[(lo + hi) / 2];
		a[(lo + hi) / 2] = a[hi];
		a[hi] = pivot;
		while (lo < hi)
		{
			while ((c.compare(a[lo], pivot) <= 0) && (lo < hi))
			{
				lo++;
			}
			while ((c.compare(pivot, a[hi]) <= 0) && (lo < hi))
			{
				hi--;
			}
			if (lo < hi)
			{
				T e = a[lo];
				a[lo] = a[hi];
				a[hi] = e;
			}
		}
		a[hi0] = a[hi];
		a[hi] = pivot;
		eqSort(a, lo0, lo - 1, c);
		eqSort(a, hi + 1, hi0, c);
	}
	
	/**
	 * Method eqSort.
	 * @param a T[]
	 * @param c Comparator<T>
	 */
	public static <T> void eqSort(T[] a, Comparator<T> c)
	{
		eqSort(a, 0, a.length - 1, c);
	}
	
	/**
	 * Method toArray.
	 * @param collection Collection<Integer>
	 * @return int[]
	 */
	public static int[] toArray(Collection<Integer> collection)
	{
		int[] ar = new int[collection.size()];
		int i = 0;
		for (Integer t : collection)
		{
			ar[i++] = t;
		}
		return ar;
	}
	
	/**
	 * Method createAscendingArray.
	 * @param min int
	 * @param max int
	 * @return int[]
	 */
	public static int[] createAscendingArray(int min, int max)
	{
		int length = max - min;
		int[] array = new int[length + 1];
		int x = 0;
		for (int i = min; i <= max; i++, x++)
		{
			array[x] = i;
		}
		return array;
	}
}
