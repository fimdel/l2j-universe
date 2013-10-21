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
package lineage2.gameserver.model.items.etcitems;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class LifeStoneInfo
{
	/**
	 * Field itemId.
	 */
	private int itemId;
	/**
	 * Field level.
	 */
	private int level;
	/**
	 * Field grade.
	 */
	private LifeStoneGrade grade;
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return itemId;
	}
	
	/**
	 * Method setItemId.
	 * @param itemId int
	 */
	public void setItemId(int itemId)
	{
		this.itemId = itemId;
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return level;
	}
	
	/**
	 * Method setLevel.
	 * @param level int
	 */
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	/**
	 * Method getGrade.
	 * @return LifeStoneGrade
	 */
	public LifeStoneGrade getGrade()
	{
		return grade;
	}
	
	/**
	 * Method setGrade.
	 * @param grade LifeStoneGrade
	 */
	public void setGrade(LifeStoneGrade grade)
	{
		this.grade = grade;
	}
}
