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
package lineage2.gameserver.model.reference;

import lineage2.commons.lang.reference.AbstractHardReference;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class L2Reference<T> extends AbstractHardReference<T>
{
	/**
	 * Constructor for L2Reference.
	 * @param reference T
	 */
	public L2Reference(T reference)
	{
		super(reference);
	}
}
