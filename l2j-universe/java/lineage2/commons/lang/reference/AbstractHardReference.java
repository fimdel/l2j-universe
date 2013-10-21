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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AbstractHardReference<T> implements HardReference<T>
{
	/**
	 * Field reference.
	 */
	private T reference;
	
	/**
	 * Constructor for AbstractHardReference.
	 * @param reference T
	 */
	public AbstractHardReference(T reference)
	{
		this.reference = reference;
	}
	
	/**
	 * Method get.
	 * @return T * @see lineage2.commons.lang.reference.HardReference#get()
	 */
	@Override
	public T get()
	{
		return reference;
	}
	
	/**
	 * Method clear.
	 * @see lineage2.commons.lang.reference.HardReference#clear()
	 */
	@Override
	public void clear()
	{
		reference = null;
	}
	
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (!(o instanceof AbstractHardReference))
		{
			return false;
		}
		if ((((AbstractHardReference) o)).get() == null)
		{
			return false;
		}
		return ((((AbstractHardReference) o)).get().equals(get()));
	}
}
