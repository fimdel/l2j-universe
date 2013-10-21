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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class CollectionUtils
{
	/**
	 * Constructor for CollectionUtils.
	 */
	private CollectionUtils()
	{
	}
	
	/**
	 * Method eqBrute.
	 * @param list List<T>
	 * @param lo int
	 * @param hi int
	 */
	private static <T extends Comparable<T>> void eqBrute(List<T> list, int lo, int hi)
	{
		if ((hi - lo) == 1)
		{
			if (list.get(hi).compareTo(list.get(lo)) < 0)
			{
				T e = list.get(lo);
				list.set(lo, list.get(hi));
				list.set(hi, e);
			}
		}
		else if ((hi - lo) == 2)
		{
			int pmin = list.get(lo).compareTo(list.get(lo + 1)) < 0 ? lo : lo + 1;
			pmin = list.get(pmin).compareTo(list.get(lo + 2)) < 0 ? pmin : lo + 2;
			if (pmin != lo)
			{
				T e = list.get(lo);
				list.set(lo, list.get(pmin));
				list.set(pmin, e);
			}
			eqBrute(list, lo + 1, hi);
		}
		else if ((hi - lo) == 3)
		{
			int pmin = list.get(lo).compareTo(list.get(lo + 1)) < 0 ? lo : lo + 1;
			pmin = list.get(pmin).compareTo(list.get(lo + 2)) < 0 ? pmin : lo + 2;
			pmin = list.get(pmin).compareTo(list.get(lo + 3)) < 0 ? pmin : lo + 3;
			if (pmin != lo)
			{
				T e = list.get(lo);
				list.set(lo, list.get(pmin));
				list.set(pmin, e);
			}
			int pmax = list.get(hi).compareTo(list.get(hi - 1)) > 0 ? hi : hi - 1;
			pmax = list.get(pmax).compareTo(list.get(hi - 2)) > 0 ? pmax : hi - 2;
			if (pmax != hi)
			{
				T e = list.get(hi);
				list.set(hi, list.get(pmax));
				list.set(pmax, e);
			}
			eqBrute(list, lo + 1, hi - 1);
		}
	}
	
	/**
	 * Method eqSort.
	 * @param list List<T>
	 * @param lo0 int
	 * @param hi0 int
	 */
	private static <T extends Comparable<T>> void eqSort(List<T> list, int lo0, int hi0)
	{
		int lo = lo0;
		int hi = hi0;
		if ((hi - lo) <= 3)
		{
			eqBrute(list, lo, hi);
			return;
		}
		T e, pivot = list.get((lo + hi) / 2);
		list.set((lo + hi) / 2, list.get(hi));
		list.set(hi, pivot);
		while (lo < hi)
		{
			while ((list.get(lo).compareTo(pivot) <= 0) && (lo < hi))
			{
				lo++;
			}
			while ((pivot.compareTo(list.get(hi)) <= 0) && (lo < hi))
			{
				hi--;
			}
			if (lo < hi)
			{
				e = list.get(lo);
				list.set(lo, list.get(hi));
				list.set(hi, e);
			}
		}
		list.set(hi0, list.get(hi));
		list.set(hi, pivot);
		eqSort(list, lo0, lo - 1);
		eqSort(list, hi + 1, hi0);
	}
	
	/**
	 * Method eqSort.
	 * @param list List<T>
	 */
	public static <T extends Comparable<T>> void eqSort(List<T> list)
	{
		eqSort(list, 0, list.size() - 1);
	}
	
	/**
	 * Method eqBrute.
	 * @param list List<T>
	 * @param lo int
	 * @param hi int
	 * @param c Comparator<? super T>
	 */
	private static <T> void eqBrute(List<T> list, int lo, int hi, Comparator<? super T> c)
	{
		if ((hi - lo) == 1)
		{
			if (c.compare(list.get(hi), list.get(lo)) < 0)
			{
				T e = list.get(lo);
				list.set(lo, list.get(hi));
				list.set(hi, e);
			}
		}
		else if ((hi - lo) == 2)
		{
			int pmin = c.compare(list.get(lo), list.get(lo + 1)) < 0 ? lo : lo + 1;
			pmin = c.compare(list.get(pmin), list.get(lo + 2)) < 0 ? pmin : lo + 2;
			if (pmin != lo)
			{
				T e = list.get(lo);
				list.set(lo, list.get(pmin));
				list.set(pmin, e);
			}
			eqBrute(list, lo + 1, hi, c);
		}
		else if ((hi - lo) == 3)
		{
			int pmin = c.compare(list.get(lo), list.get(lo + 1)) < 0 ? lo : lo + 1;
			pmin = c.compare(list.get(pmin), list.get(lo + 2)) < 0 ? pmin : lo + 2;
			pmin = c.compare(list.get(pmin), list.get(lo + 3)) < 0 ? pmin : lo + 3;
			if (pmin != lo)
			{
				T e = list.get(lo);
				list.set(lo, list.get(pmin));
				list.set(pmin, e);
			}
			int pmax = c.compare(list.get(hi), list.get(hi - 1)) > 0 ? hi : hi - 1;
			pmax = c.compare(list.get(pmax), list.get(hi - 2)) > 0 ? pmax : hi - 2;
			if (pmax != hi)
			{
				T e = list.get(hi);
				list.set(hi, list.get(pmax));
				list.set(pmax, e);
			}
			eqBrute(list, lo + 1, hi - 1, c);
		}
	}
	
	/**
	 * Method eqSort.
	 * @param list List<T>
	 * @param lo0 int
	 * @param hi0 int
	 * @param c Comparator<? super T>
	 */
	private static <T> void eqSort(List<T> list, int lo0, int hi0, Comparator<? super T> c)
	{
		int lo = lo0;
		int hi = hi0;
		if ((hi - lo) <= 3)
		{
			eqBrute(list, lo, hi, c);
			return;
		}
		T e, pivot = list.get((lo + hi) / 2);
		list.set((lo + hi) / 2, list.get(hi));
		list.set(hi, pivot);
		while (lo < hi)
		{
			while ((c.compare(list.get(lo), pivot) <= 0) && (lo < hi))
			{
				lo++;
			}
			while ((c.compare(pivot, list.get(hi)) <= 0) && (lo < hi))
			{
				hi--;
			}
			if (lo < hi)
			{
				e = list.get(lo);
				list.set(lo, list.get(hi));
				list.set(hi, e);
			}
		}
		list.set(hi0, list.get(hi));
		list.set(hi, pivot);
		eqSort(list, lo0, lo - 1, c);
		eqSort(list, hi + 1, hi0, c);
	}
	
	/**
	 * Method eqSort.
	 * @param list List<T>
	 * @param c Comparator<? super T>
	 */
	public static <T> void eqSort(List<T> list, Comparator<? super T> c)
	{
		eqSort(list, 0, list.size() - 1, c);
	}
	
	/**
	 * Method insertionSort.
	 * @param list List<T>
	 */
	public static <T extends Comparable<T>> void insertionSort(List<T> list)
	{
		for (int i = 1; i < list.size(); i++)
		{
			int j = i;
			T A;
			T B = list.get(i);
			while ((j > 0) && ((A = list.get(j - 1)).compareTo(B) > 0))
			{
				list.set(j, A);
				j--;
			}
			list.set(j, B);
		}
	}
	
	/**
	 * Method insertionSort.
	 * @param list List<T>
	 * @param c Comparator<? super T>
	 */
	public static <T> void insertionSort(List<T> list, Comparator<? super T> c)
	{
		for (int i = 1; i < list.size(); i++)
		{
			int j = i;
			T A;
			T B = list.get(i);
			while ((j > 0) && (c.compare(A = list.get(j - 1), B) > 0))
			{
				list.set(j, A);
				j--;
			}
			list.set(j, B);
		}
	}
	
	/**
	 * Method hashCode.
	 * @param collection Collection<E>
	 * @return int
	 */
	public static <E> int hashCode(Collection<E> collection)
	{
		int hashCode = 1;
		Iterator<E> i = collection.iterator();
		while (i.hasNext())
		{
			E obj = i.next();
			hashCode = (31 * hashCode) + (obj == null ? 0 : obj.hashCode());
		}
		return hashCode;
	}
	
	/**
	 * Method safeGet.
	 * @param list List<E>
	 * @param index int
	 * @return E
	 */
	public static <E> E safeGet(List<E> list, int index)
	{
		return list.size() > index ? list.get(index) : null;
	}
}
