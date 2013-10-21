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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SoulCrystal
{
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _level.
	 */
	private final int _level;
	/**
	 * Field _nextItemId.
	 */
	private final int _nextItemId;
	/**
	 * Field _cursedNextItemId.
	 */
	private final int _cursedNextItemId;
	
	/**
	 * Constructor for SoulCrystal.
	 * @param itemId int
	 * @param level int
	 * @param nextItemId int
	 * @param cursedNextItemId int
	 */
	public SoulCrystal(int itemId, int level, int nextItemId, int cursedNextItemId)
	{
		_itemId = itemId;
		_level = level;
		_nextItemId = nextItemId;
		_cursedNextItemId = cursedNextItemId;
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
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return _level;
	}
	
	/**
	 * Method getNextItemId.
	 * @return int
	 */
	public int getNextItemId()
	{
		return _nextItemId;
	}
	
	/**
	 * Method getCursedNextItemId.
	 * @return int
	 */
	public int getCursedNextItemId()
	{
		return _cursedNextItemId;
	}
}
