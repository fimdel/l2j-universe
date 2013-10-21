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
package lineage2.gameserver.templates.item;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class StartItem
{
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _count.
	 */
	private final long _count;
	/**
	 * Field _equiped.
	 */
	private final boolean _equiped;
	
	/**
	 * Constructor for StartItem.
	 * @param id int
	 * @param count long
	 * @param equiped boolean
	 */
	public StartItem(int id, long count, boolean equiped)
	{
		_id = id;
		_count = count;
		_equiped = equiped;
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return _id;
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
	 * Method isEquiped.
	 * @return boolean
	 */
	public boolean isEquiped()
	{
		return _equiped;
	}
}
