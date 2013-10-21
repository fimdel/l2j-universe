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
package lineage2.gameserver.model.items;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CrystallizationItem
{
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _count.
	 */
	private final long _count;
	/**
	 * Field _chance.
	 */
	private final double _chance;
	
	/**
	 * Constructor for CrystallizationItem.
	 * @param itemId int
	 * @param count long
	 * @param _chance2 double
	 */
	public CrystallizationItem(int itemId, long count, double _chance2)
	{
		_itemId = itemId;
		_count = count;
		_chance = _chance2;
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * Method getCount.
	 * @return long
	 */
	public long getCount()
	{
		return _count;
	}
	
	/**
	 * Method getChance.
	 * @return double
	 */
	public double getChance()
	{
		return _chance;
	}
}
