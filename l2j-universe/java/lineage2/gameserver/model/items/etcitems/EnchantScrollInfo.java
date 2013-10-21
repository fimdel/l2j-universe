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

import lineage2.gameserver.templates.item.ItemTemplate.Grade;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class EnchantScrollInfo
{
	/**
	 * Field itemId.
	 */
	private int itemId;
	/**
	 * Field type.
	 */
	private EnchantScrollType type;
	/**
	 * Field target.
	 */
	private EnchantScrollTarget target;
	/**
	 * Field grade.
	 */
	private Grade grade;
	/**
	 * Field max. Field safe. Field min. Field chance.
	 */
	private int chance, min, safe, max;
	
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
	 * Method getType.
	 * @return EnchantScrollType
	 */
	public EnchantScrollType getType()
	{
		return type;
	}
	
	/**
	 * Method setType.
	 * @param type EnchantScrollType
	 */
	public void setType(EnchantScrollType type)
	{
		this.type = type;
	}
	
	/**
	 * Method getGrade.
	 * @return Grade
	 */
	public Grade getGrade()
	{
		return grade;
	}
	
	/**
	 * Method setGrade.
	 * @param grade Grade
	 */
	public void setGrade(Grade grade)
	{
		this.grade = grade;
	}
	
	/**
	 * Method getMin.
	 * @return int
	 */
	public int getMin()
	{
		return min;
	}
	
	/**
	 * Method setMin.
	 * @param min int
	 */
	public void setMin(int min)
	{
		this.min = min;
	}
	
	/**
	 * Method getSafe.
	 * @return int
	 */
	public int getSafe()
	{
		return safe;
	}
	
	/**
	 * Method setSafe.
	 * @param safe int
	 */
	public void setSafe(int safe)
	{
		this.safe = safe;
	}
	
	/**
	 * Method getChance.
	 * @return int
	 */
	public int getChance()
	{
		return chance;
	}
	
	/**
	 * Method setChance.
	 * @param chance int
	 */
	public void setChance(int chance)
	{
		this.chance = chance;
	}
	
	/**
	 * Method getMax.
	 * @return int
	 */
	public int getMax()
	{
		return max;
	}
	
	/**
	 * Method setMax.
	 * @param max int
	 */
	public void setMax(int max)
	{
		this.max = max;
	}
	
	/**
	 * Method getTarget.
	 * @return EnchantScrollTarget
	 */
	public EnchantScrollTarget getTarget()
	{
		return target;
	}
	
	/**
	 * Method setTarget.
	 * @param target EnchantScrollTarget
	 */
	public void setTarget(EnchantScrollTarget target)
	{
		this.target = target;
	}
}
