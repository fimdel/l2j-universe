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
package lineage2.commons.util.concurrent.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AtomicState
{
	/**
	 * Field stateUpdater.
	 */
	private static final AtomicIntegerFieldUpdater<AtomicState> stateUpdater = AtomicIntegerFieldUpdater.newUpdater(AtomicState.class, "value");
	/**
	 * Field value.
	 */
	private volatile int value;
	
	/**
	 * Constructor for AtomicState.
	 * @param initialValue boolean
	 */
	public AtomicState(boolean initialValue)
	{
		value = initialValue ? 1 : 0;
	}
	
	/**
	 * Constructor for AtomicState.
	 */
	public AtomicState()
	{
	}
	
	/**
	 * Method get.
	 * @return boolean
	 */
	public final boolean get()
	{
		return value != 0;
	}
	
	/**
	 * Method getBool.
	 * @param value int
	 * @return boolean
	 */
	private boolean getBool(int value)
	{
		if (value < 0)
		{
			throw new IllegalStateException();
		}
		return value > 0;
	}
	
	/**
	 * Method setAndGet.
	 * @param newValue boolean
	 * @return boolean
	 */
	public final boolean setAndGet(boolean newValue)
	{
		if (newValue)
		{
			return getBool(stateUpdater.incrementAndGet(this));
		}
		return getBool(stateUpdater.decrementAndGet(this));
	}
	
	/**
	 * Method getAndSet.
	 * @param newValue boolean
	 * @return boolean
	 */
	public final boolean getAndSet(boolean newValue)
	{
		if (newValue)
		{
			return getBool(stateUpdater.getAndIncrement(this));
		}
		return getBool(stateUpdater.getAndDecrement(this));
	}
}
