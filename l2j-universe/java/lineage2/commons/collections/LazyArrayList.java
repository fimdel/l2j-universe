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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unchecked")
public class LazyArrayList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
	/**
	 * Field serialVersionUID. (value is 8683452581122892189)
	 */
	private static final long serialVersionUID = 8683452581122892189L;
	
	/**
	 * @author Mobius
	 */
	@SuppressWarnings("rawtypes")
	private static class PoolableLazyArrayListFactory implements PoolableObjectFactory
	{
		/**
		 * Constructor for PoolableLazyArrayListFactory.
		 */
		public PoolableLazyArrayListFactory()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method makeObject.
		 * @return Object * @see org.apache.commons.pool.PoolableObjectFactory#makeObject()
		 */
		@Override
		public Object makeObject()
		{
			return new LazyArrayList();
		}
		
		/**
		 * Method destroyObject.
		 * @param obj Object
		 */
		@Override
		public void destroyObject(Object obj)
		{
			((LazyArrayList) obj).clear();
		}
		
		/**
		 * Method validateObject.
		 * @param obj Object
		 * @return boolean
		 */
		@Override
		public boolean validateObject(Object obj)
		{
			return true;
		}
		
		/**
		 * Method activateObject.
		 * @param obj Object
		 */
		@Override
		public void activateObject(Object obj)
		{
		}
		
		/**
		 * Method passivateObject.
		 * @param obj Object
		 */
		@Override
		public void passivateObject(Object obj)
		{
			((LazyArrayList) obj).clear();
		}
	}
	
	/**
	 * Field POOL_SIZE.
	 */
	private static final int POOL_SIZE = Integer.parseInt(System.getProperty("lazyarraylist.poolsize", "-1"));
	/**
	 * Field POOL.
	 */
	private static final ObjectPool<Object> POOL = new GenericObjectPool<>(new PoolableLazyArrayListFactory(), POOL_SIZE, GenericObjectPool.WHEN_EXHAUSTED_GROW, 0L, -1);
	
	/**
	 * Method newInstance.
	 * @return LazyArrayList<E>
	 */
	public static <E> LazyArrayList<E> newInstance()
	{
		try
		{
			return (LazyArrayList<E>) POOL.borrowObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return new LazyArrayList<>();
	}
	
	/**
	 * Method recycle.
	 * @param obj LazyArrayList<E>
	 */
	public static <E> void recycle(LazyArrayList<E> obj)
	{
		try
		{
			POOL.returnObject(obj);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Field L.
	 */
	private static final int L = 1 << 3;
	/**
	 * Field H.
	 */
	private static final int H = 1 << 10;
	/**
	 * Field elementData.
	 */
	protected transient Object[] elementData;
	/**
	 * Field size.
	 */
	protected transient int size = 0;
	/**
	 * Field capacity.
	 */
	protected transient int capacity = L;
	
	/**
	 * Constructor for LazyArrayList.
	 * @param initialCapacity int
	 */
	public LazyArrayList(int initialCapacity)
	{
		if (initialCapacity < H)
		{
			while (capacity < initialCapacity)
			{
				capacity <<= 1;
			}
		}
		else
		{
			capacity = initialCapacity;
		}
	}
	
	/**
	 * Constructor for LazyArrayList.
	 */
	public LazyArrayList()
	{
		this(8);
	}
	
	/**
	 * Method add.
	 * @param element E
	 * @return boolean * @see java.util.List#add(E)
	 */
	@Override
	public boolean add(E element)
	{
		ensureCapacity(size + 1);
		elementData[size++] = element;
		return true;
	}
	
	/**
	 * Method set.
	 * @param index int
	 * @param element E
	 * @return E * @see java.util.List#set(int, E)
	 */
	@Override
	public E set(int index, E element)
	{
		E e = null;
		if ((index >= 0) && (index < size))
		{
			e = (E) elementData[index];
			elementData[index] = element;
		}
		return e;
	}
	
	/**
	 * Method add.
	 * @param index int
	 * @param element E
	 * @see java.util.List#add(int, E)
	 */
	@Override
	public void add(int index, E element)
	{
		if ((index >= 0) && (index < size))
		{
			ensureCapacity(size + 1);
			System.arraycopy(elementData, index, elementData, index + 1, size - index);
			elementData[index] = element;
			size++;
		}
	}
	
	/**
	 * Method addAll.
	 * @param index int
	 * @param c Collection<? extends E>
	 * @return boolean * @see java.util.List#addAll(int, Collection<? extends E>)
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> c)
	{
		if ((c == null) || c.isEmpty())
		{
			return false;
		}
		if ((index >= 0) && (index < size))
		{
			Object[] a = c.toArray();
			int numNew = a.length;
			ensureCapacity(size + numNew);
			int numMoved = size - index;
			if (numMoved > 0)
			{
				System.arraycopy(elementData, index, elementData, index + numNew, numMoved);
			}
			System.arraycopy(a, 0, elementData, index, numNew);
			size += numNew;
			return true;
		}
		return false;
	}
	
	/**
	 * Method ensureCapacity.
	 * @param newSize int
	 */
	protected void ensureCapacity(int newSize)
	{
		if (newSize > capacity)
		{
			if (newSize < H)
			{
				while (capacity < newSize)
				{
					capacity <<= 1;
				}
			}
			else
			{
				while (capacity < newSize)
				{
					capacity = (capacity * 3) / 2;
				}
			}
			Object[] elementDataResized = new Object[capacity];
			if (elementData != null)
			{
				System.arraycopy(elementData, 0, elementDataResized, 0, size);
			}
			elementData = elementDataResized;
		}
		else if (elementData == null)
		{
			elementData = new Object[capacity];
		}
	}
	
	/**
	 * Method remove.
	 * @param index int
	 * @return E * @see java.util.List#remove(int)
	 */
	@Override
	public E remove(int index)
	{
		E e = null;
		if ((index >= 0) && (index < size))
		{
			size--;
			e = (E) elementData[index];
			elementData[index] = elementData[size];
			elementData[size] = null;
			trim();
		}
		return e;
	}
	
	/**
	 * Method remove.
	 * @param o Object
	 * @return boolean * @see java.util.List#remove(Object)
	 */
	@Override
	public boolean remove(Object o)
	{
		if (size == 0)
		{
			return false;
		}
		int index = -1;
		for (int i = 0; i < size; i++)
		{
			if (elementData[i] == o)
			{
				index = i;
				break;
			}
		}
		if (index == -1)
		{
			return false;
		}
		size--;
		elementData[index] = elementData[size];
		elementData[size] = null;
		trim();
		return true;
	}
	
	/**
	 * Method contains.
	 * @param o Object
	 * @return boolean * @see java.util.List#contains(Object)
	 */
	@Override
	public boolean contains(Object o)
	{
		if (size == 0)
		{
			return false;
		}
		for (int i = 0; i < size; i++)
		{
			if (elementData[i] == o)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method indexOf.
	 * @param o Object
	 * @return int * @see java.util.List#indexOf(Object)
	 */
	@Override
	public int indexOf(Object o)
	{
		if (size == 0)
		{
			return -1;
		}
		int index = -1;
		for (int i = 0; i < size; i++)
		{
			if (elementData[i] == o)
			{
				index = i;
				break;
			}
		}
		return index;
	}
	
	/**
	 * Method lastIndexOf.
	 * @param o Object
	 * @return int * @see java.util.List#lastIndexOf(Object)
	 */
	@Override
	public int lastIndexOf(Object o)
	{
		if (size == 0)
		{
			return -1;
		}
		int index = -1;
		for (int i = 0; i < size; i++)
		{
			if (elementData[i] == o)
			{
				index = i;
			}
		}
		return index;
	}
	
	/**
	 * Method trim.
	 */
	protected void trim()
	{
	}
	
	/**
	 * Method get.
	 * @param index int
	 * @return E * @see java.util.List#get(int)
	 */
	@Override
	public E get(int index)
	{
		if ((size > 0) && (index >= 0) && (index < size))
		{
			return (E) elementData[index];
		}
		return null;
	}
	
	/**
	 * Method clone.
	 * @return Object
	 */
	@Override
	public Object clone()
	{
		LazyArrayList<E> clone = new LazyArrayList<>();
		if (size > 0)
		{
			clone.capacity = capacity;
			clone.elementData = new Object[elementData.length];
			System.arraycopy(elementData, 0, clone.elementData, 0, size);
		}
		return clone;
	}
	
	/**
	 * Method clear.
	 * @see java.util.List#clear()
	 */
	@Override
	public void clear()
	{
		if (size == 0)
		{
			return;
		}
		for (int i = 0; i < size; i++)
		{
			elementData[i] = null;
		}
		size = 0;
		trim();
	}
	
	/**
	 * Method size.
	 * @return int * @see java.util.List#size()
	 */
	@Override
	public int size()
	{
		return size;
	}
	
	/**
	 * Method isEmpty.
	 * @return boolean * @see java.util.List#isEmpty()
	 */
	@Override
	public boolean isEmpty()
	{
		return size == 0;
	}
	
	/**
	 * Method capacity.
	 * @return int
	 */
	public int capacity()
	{
		return capacity;
	}
	
	/**
	 * Method addAll.
	 * @param c Collection<? extends E>
	 * @return boolean * @see java.util.List#addAll(Collection<? extends E>)
	 */
	@Override
	public boolean addAll(Collection<? extends E> c)
	{
		if ((c == null) || c.isEmpty())
		{
			return false;
		}
		Object[] a = c.toArray();
		int numNew = a.length;
		ensureCapacity(size + numNew);
		System.arraycopy(a, 0, elementData, size, numNew);
		size += numNew;
		return true;
	}
	
	/**
	 * Method containsAll.
	 * @param c Collection<?>
	 * @return boolean * @see java.util.List#containsAll(Collection<?>)
	 */
	@Override
	public boolean containsAll(Collection<?> c)
	{
		if (c == null)
		{
			return false;
		}
		if (c.isEmpty())
		{
			return true;
		}
		Iterator<?> e = c.iterator();
		while (e.hasNext())
		{
			if (!contains(e.next()))
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method retainAll.
	 * @param c Collection<?>
	 * @return boolean * @see java.util.List#retainAll(Collection<?>)
	 */
	@Override
	public boolean retainAll(Collection<?> c)
	{
		if (c == null)
		{
			return false;
		}
		boolean modified = false;
		Iterator<E> e = iterator();
		while (e.hasNext())
		{
			if (!c.contains(e.next()))
			{
				e.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	/**
	 * Method removeAll.
	 * @param c Collection<?>
	 * @return boolean * @see java.util.List#removeAll(Collection<?>)
	 */
	@Override
	public boolean removeAll(Collection<?> c)
	{
		if ((c == null) || c.isEmpty())
		{
			return false;
		}
		boolean modified = false;
		Iterator<?> e = iterator();
		while (e.hasNext())
		{
			if (c.contains(e.next()))
			{
				e.remove();
				modified = true;
			}
		}
		return modified;
	}
	
	/**
	 * Method toArray.
	 * @return Object[] * @see java.util.List#toArray()
	 */
	@Override
	public Object[] toArray()
	{
		Object[] r = new Object[size];
		if (size > 0)
		{
			System.arraycopy(elementData, 0, r, 0, size);
		}
		return r;
	}
	
	/**
	 * Method toArray.
	 * @param a T[]
	 * @return T[] * @see java.util.List#toArray(T[])
	 */
	@Override
	public <T> T[] toArray(T[] a)
	{
		T[] r = a.length >= size ? a : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
		if (size > 0)
		{
			System.arraycopy(elementData, 0, r, 0, size);
		}
		if (r.length > size)
		{
			r[size] = null;
		}
		return r;
	}
	
	/**
	 * Method iterator.
	 * @return Iterator<E> * @see java.util.List#iterator()
	 */
	@Override
	public Iterator<E> iterator()
	{
		return new LazyItr();
	}
	
	/**
	 * Method listIterator.
	 * @return ListIterator<E> * @see java.util.List#listIterator()
	 */
	@Override
	public ListIterator<E> listIterator()
	{
		return new LazyListItr(0);
	}
	
	/**
	 * Method listIterator.
	 * @param index int
	 * @return ListIterator<E> * @see java.util.List#listIterator(int)
	 */
	@Override
	public ListIterator<E> listIterator(int index)
	{
		return new LazyListItr(index);
	}
	
	/**
	 * @author Mobius
	 */
	private class LazyItr implements Iterator<E>
	{
		/**
		 * Field cursor.
		 */
		int cursor = 0;
		/**
		 * Field lastRet.
		 */
		int lastRet = -1;
		
		/**
		 * Constructor for LazyItr.
		 */
		public LazyItr()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method hasNext.
		 * @return boolean * @see java.util.Iterator#hasNext()
		 */
		@Override
		public boolean hasNext()
		{
			return cursor < size();
		}
		
		/**
		 * Method next.
		 * @return E * @see java.util.Iterator#next()
		 */
		@Override
		public E next()
		{
			E next = get(cursor);
			lastRet = cursor++;
			return next;
		}
		
		/**
		 * Method remove.
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove()
		{
			if (lastRet == -1)
			{
				throw new IllegalStateException();
			}
			LazyArrayList.this.remove(lastRet);
			if (lastRet < cursor)
			{
				cursor--;
			}
			lastRet = -1;
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class LazyListItr extends LazyItr implements ListIterator<E>
	{
		/**
		 * Constructor for LazyListItr.
		 * @param index int
		 */
		LazyListItr(int index)
		{
			cursor = index;
		}
		
		/**
		 * Method hasPrevious.
		 * @return boolean * @see java.util.ListIterator#hasPrevious()
		 */
		@Override
		public boolean hasPrevious()
		{
			return cursor > 0;
		}
		
		/**
		 * Method previous.
		 * @return E * @see java.util.ListIterator#previous()
		 */
		@Override
		public E previous()
		{
			int i = cursor - 1;
			E previous = get(i);
			lastRet = cursor = i;
			return previous;
		}
		
		/**
		 * Method nextIndex.
		 * @return int * @see java.util.ListIterator#nextIndex()
		 */
		@Override
		public int nextIndex()
		{
			return cursor;
		}
		
		/**
		 * Method previousIndex.
		 * @return int * @see java.util.ListIterator#previousIndex()
		 */
		@Override
		public int previousIndex()
		{
			return cursor - 1;
		}
		
		/**
		 * Method set.
		 * @param e E
		 * @see java.util.ListIterator#set(E)
		 */
		@Override
		public void set(E e)
		{
			if (lastRet == -1)
			{
				throw new IllegalStateException();
			}
			LazyArrayList.this.set(lastRet, e);
		}
		
		/**
		 * Method add.
		 * @param e E
		 * @see java.util.ListIterator#add(E)
		 */
		@Override
		public void add(E e)
		{
			LazyArrayList.this.add(cursor++, e);
			lastRet = -1;
		}
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		if (size == 0)
		{
			return "[]";
		}
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (int i = 0; i < size; i++)
		{
			Object e = elementData[i];
			sb.append(e == this ? "this" : e);
			if (i == (size - 1))
			{
				sb.append(']').toString();
			}
			else
			{
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	
	/**
	 * Method subList.
	 * @param fromIndex int
	 * @param toIndex int
	 * @return List<E> * @see java.util.List#subList(int, int)
	 */
	@Override
	public List<E> subList(int fromIndex, int toIndex)
	{
		throw new UnsupportedOperationException();
	}
}
