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
package lineage2.commons.lang.reference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HardReferences
{
	/**
	 * Constructor for HardReferences.
	 */
	private HardReferences()
	{
	}
	
	/**
	 * @author Mobius
	 */
	private static class EmptyReferencedHolder extends AbstractHardReference<Object>
	{
		/**
		 * Constructor for EmptyReferencedHolder.
		 * @param reference Object
		 */
		public EmptyReferencedHolder(Object reference)
		{
			super(reference);
		}
	}
	
	/**
	 * Field EMPTY_REF.
	 */
	private static HardReference<?> EMPTY_REF = new EmptyReferencedHolder(null);
	
	/**
	 * Method emptyRef.
	 * @return HardReference<T>
	 */
	@SuppressWarnings("unchecked")
	public static <T> HardReference<T> emptyRef()
	{
		return (HardReference<T>) EMPTY_REF;
	}
	
	/**
	 * Method unwrap.
	 * @param refs Collection<HardReference<T>>
	 * @return Collection<T>
	 */
	public static <T> Collection<T> unwrap(Collection<HardReference<T>> refs)
	{
		List<T> result = new ArrayList<>(refs.size());
		for (HardReference<T> ref : refs)
		{
			T obj = ref.get();
			if (obj != null)
			{
				result.add(obj);
			}
		}
		return result;
	}
	
	/**
	 * @author Mobius
	 */
	private static class WrappedIterable<T> implements Iterable<T>
	{
		/**
		 * Field refs.
		 */
		final Iterable<HardReference<T>> refs;
		
		/**
		 * Constructor for WrappedIterable.
		 * @param refs Iterable<HardReference<T>>
		 */
		WrappedIterable(Iterable<HardReference<T>> refs)
		{
			this.refs = refs;
		}
		
		/**
		 * @author Mobius
		 */
		private static class WrappedIterator<T> implements Iterator<T>
		{
			/**
			 * Field iterator.
			 */
			final Iterator<HardReference<T>> iterator;
			
			/**
			 * Constructor for WrappedIterator.
			 * @param iterator Iterator<HardReference<T>>
			 */
			WrappedIterator(Iterator<HardReference<T>> iterator)
			{
				this.iterator = iterator;
			}
			
			/**
			 * Method hasNext.
			 * @return boolean * @see java.util.Iterator#hasNext()
			 */
			@Override
			public boolean hasNext()
			{
				return iterator.hasNext();
			}
			
			/**
			 * Method next.
			 * @return T * @see java.util.Iterator#next()
			 */
			@Override
			public T next()
			{
				return iterator.next().get();
			}
			
			/**
			 * Method remove.
			 * @see java.util.Iterator#remove()
			 */
			@Override
			public void remove()
			{
				iterator.remove();
			}
		}
		
		/**
		 * Method iterator.
		 * @return Iterator<T> * @see java.lang.Iterable#iterator()
		 */
		@Override
		public Iterator<T> iterator()
		{
			return new WrappedIterator<>(refs.iterator());
		}
	}
	
	/**
	 * Method iterate.
	 * @param refs Iterable<HardReference<T>>
	 * @return Iterable<T>
	 */
	public static <T> Iterable<T> iterate(Iterable<HardReference<T>> refs)
	{
		return new WrappedIterable<>(refs);
	}
}
