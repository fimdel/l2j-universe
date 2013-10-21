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

import java.util.Iterator;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EmptyIterator<E> implements Iterator<E>
{
	/**
	 * Field INSTANCE.
	 */
	@SuppressWarnings("rawtypes")
	private static final Iterator INSTANCE = new EmptyIterator();
	
	/**
	 * Method getInstance.
	 * @return Iterator<E>
	 */
	@SuppressWarnings("unchecked")
	public static <E> Iterator<E> getInstance()
	{
		return INSTANCE;
	}
	
	/**
	 * Constructor for EmptyIterator.
	 */
	private EmptyIterator()
	{
	}
	
	/**
	 * Method hasNext.
	 * @return boolean * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		return false;
	}
	
	/**
	 * Method next.
	 * @return E * @see java.util.Iterator#next()
	 */
	@Override
	public E next()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Method remove.
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}
}
