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
import java.util.List;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class JoinedIterator<E> implements Iterator<E>
{
	/**
	 * Field _iterators.
	 */
	private Iterator<E>[] _iterators;
	/**
	 * Field _currentIteratorIndex.
	 */
	private int _currentIteratorIndex;
	/**
	 * Field _currentIterator.
	 */
	private Iterator<E> _currentIterator;
	/**
	 * Field _lastUsedIterator.
	 */
	private Iterator<E> _lastUsedIterator;
	
	/**
	 * Constructor for JoinedIterator.
	 * @param iterators List<Iterator<E>>
	 */
	public JoinedIterator(List<Iterator<E>> iterators)
	{
		this(iterators.toArray(new Iterator[iterators.size()]));
	}
	
	/**
	 * Constructor for JoinedIterator.
	 * @param iterators Iterator<?>[]
	 */
	@SuppressWarnings("unchecked")
	public JoinedIterator(Iterator<?>... iterators)
	{
		if (iterators == null)
		{
			throw new NullPointerException("Unexpected NULL iterators argument");
		}
		_iterators = (Iterator<E>[]) iterators;
	}
	
	/**
	 * Method hasNext.
	 * @return boolean * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext()
	{
		updateCurrentIterator();
		return _currentIterator.hasNext();
	}
	
	/**
	 * Method next.
	 * @return E * @see java.util.Iterator#next()
	 */
	@Override
	public E next()
	{
		updateCurrentIterator();
		return _currentIterator.next();
	}
	
	/**
	 * Method remove.
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove()
	{
		updateCurrentIterator();
		_lastUsedIterator.remove();
	}
	
	/**
	 * Method updateCurrentIterator.
	 */
	protected void updateCurrentIterator()
	{
		if (_currentIterator == null)
		{
			if (_iterators.length == 0)
			{
				_currentIterator = EmptyIterator.getInstance();
			}
			else
			{
				_currentIterator = _iterators[0];
			}
			_lastUsedIterator = _currentIterator;
		}
		while (!_currentIterator.hasNext() && (_currentIteratorIndex < (_iterators.length - 1)))
		{
			_currentIteratorIndex++;
			_currentIterator = _iterators[_currentIteratorIndex];
		}
	}
}
