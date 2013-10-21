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
package lineage2.gameserver.model.actor.instances.player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ShortCut
{
	/**
	 * Field TYPE_ITEM. (value is 1)
	 */
	public final static int TYPE_ITEM = 1;
	/**
	 * Field TYPE_SKILL. (value is 2)
	 */
	public final static int TYPE_SKILL = 2;
	/**
	 * Field TYPE_ACTION. (value is 3)
	 */
	public final static int TYPE_ACTION = 3;
	/**
	 * Field TYPE_MACRO. (value is 4)
	 */
	public final static int TYPE_MACRO = 4;
	/**
	 * Field TYPE_RECIPE. (value is 5)
	 */
	public final static int TYPE_RECIPE = 5;
	/**
	 * Field TYPE_TPBOOKMARK. (value is 6)
	 */
	public final static int TYPE_TPBOOKMARK = 6;
	/**
	 * Field PAGE_NORMAL_0. (value is 0)
	 */
	public final static int PAGE_NORMAL_0 = 0;
	/**
	 * Field PAGE_NORMAL_1. (value is 1)
	 */
	public final static int PAGE_NORMAL_1 = 1;
	/**
	 * Field PAGE_NORMAL_2. (value is 2)
	 */
	public final static int PAGE_NORMAL_2 = 2;
	/**
	 * Field PAGE_NORMAL_3. (value is 3)
	 */
	public final static int PAGE_NORMAL_3 = 3;
	/**
	 * Field PAGE_NORMAL_4. (value is 4)
	 */
	public final static int PAGE_NORMAL_4 = 4;
	/**
	 * Field PAGE_NORMAL_5. (value is 5)
	 */
	public final static int PAGE_NORMAL_5 = 5;
	/**
	 * Field PAGE_NORMAL_6. (value is 6)
	 */
	public final static int PAGE_NORMAL_6 = 6;
	/**
	 * Field PAGE_NORMAL_7. (value is 7)
	 */
	public final static int PAGE_NORMAL_7 = 7;
	/**
	 * Field PAGE_NORMAL_8. (value is 8)
	 */
	public final static int PAGE_NORMAL_8 = 8;
	/**
	 * Field PAGE_NORMAL_9. (value is 9)
	 */
	public final static int PAGE_NORMAL_9 = 9;
	/**
	 * Field PAGE_FLY_TRANSFORM. (value is 10)
	 */
	public final static int PAGE_FLY_TRANSFORM = 10;
	/**
	 * Field PAGE_AIRSHIP. (value is 11)
	 */
	public final static int PAGE_AIRSHIP = 11;
	/**
	 * Field PAGE_MAX.
	 */
	public final static int PAGE_MAX = PAGE_AIRSHIP;
	/**
	 * Field _slot.
	 */
	private final int _slot;
	/**
	 * Field _page.
	 */
	private final int _page;
	/**
	 * Field _type.
	 */
	private final int _type;
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _level.
	 */
	private final int _level;
	/**
	 * Field _characterType.
	 */
	private final int _characterType;
	
	/**
	 * Constructor for ShortCut.
	 * @param slot int
	 * @param page int
	 * @param type int
	 * @param id int
	 * @param level int
	 * @param characterType int
	 */
	public ShortCut(int slot, int page, int type, int id, int level, int characterType)
	{
		_slot = slot;
		_page = page;
		_type = type;
		_id = id;
		_level = level;
		_characterType = characterType;
	}
	
	/**
	 * Method getSlot.
	 * @return int
	 */
	public int getSlot()
	{
		return _slot;
	}
	
	/**
	 * Method getPage.
	 * @return int
	 */
	public int getPage()
	{
		return _page;
	}
	
	/**
	 * Method getType.
	 * @return int
	 */
	public int getType()
	{
		return _type;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
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
	 * Method getCharacterType.
	 * @return int
	 */
	public int getCharacterType()
	{
		return _characterType;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "ShortCut: " + _slot + "/" + _page + " ( " + _type + "," + _id + "," + _level + "," + _characterType + ")";
	}
}
