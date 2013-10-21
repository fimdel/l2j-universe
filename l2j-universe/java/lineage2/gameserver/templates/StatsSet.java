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
package lineage2.gameserver.templates;

import lineage2.commons.collections.MultiValueSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class StatsSet extends MultiValueSet<String>
{
	/**
	 * Field serialVersionUID. (value is -2209589233655930756)
	 */
	private static final long serialVersionUID = -2209589233655930756L;
	/**
	 * Field EMPTY.
	 */
	@SuppressWarnings("serial")
	public static final StatsSet EMPTY = new StatsSet()
	{
		@Override
		public Object put(String a, Object a2)
		{
			throw new UnsupportedOperationException();
		}
	};
	
	/**
	 * Constructor for StatsSet.
	 */
	public StatsSet()
	{
		super();
	}
	
	/**
	 * Constructor for StatsSet.
	 * @param set StatsSet
	 */
	public StatsSet(StatsSet set)
	{
		super(set);
	}
	
	/**
	 * Method clone.
	 * @return StatsSet
	 */
	@Override
	public StatsSet clone()
	{
		return new StatsSet(this);
	}
}
