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
package lineage2.gameserver.model.quest;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Drop
{
	/**
	 * Field condition.
	 */
	public final int condition;
	/**
	 * Field maxcount.
	 */
	public final int maxcount;
	/**
	 * Field chance.
	 */
	public final int chance;
	/**
	 * Field itemList.
	 */
	public int[] itemList = ArrayUtils.EMPTY_INT_ARRAY;
	
	/**
	 * Constructor for Drop.
	 * @param condition int
	 * @param maxcount int
	 * @param chance int
	 */
	public Drop(int condition, int maxcount, int chance)
	{
		this.condition = condition;
		this.maxcount = maxcount;
		this.chance = chance;
	}
	
	/**
	 * Method addItem.
	 * @param item int
	 * @return Drop
	 */
	public Drop addItem(int item)
	{
		itemList = ArrayUtils.add(itemList, item);
		return this;
	}
}
