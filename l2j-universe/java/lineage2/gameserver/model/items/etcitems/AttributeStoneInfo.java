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

import lineage2.gameserver.model.base.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AttributeStoneInfo
{
	/**
	 * Field itemId.
	 */
	private int itemId;
	/**
	 * Field minArmor.
	 */
	private int minArmor;
	/**
	 * Field maxArmor.
	 */
	private int maxArmor;
	/**
	 * Field minWeapon.
	 */
	private int minWeapon;
	/**
	 * Field maxWeapon.
	 */
	private int maxWeapon;
	/**
	 * Field incArmor.
	 */
	private int incArmor;
	/**
	 * Field incWeapon.
	 */
	private int incWeapon;
	/**
	 * Field incWeaponArmor.
	 */
	private int incWeaponArmor;
	/**
	 * Field element.
	 */
	private Element element;
	/**
	 * Field chance.
	 */
	private int chance;
	
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
	 * Method getMinArmor.
	 * @return int
	 */
	public int getMinArmor()
	{
		return minArmor;
	}
	
	/**
	 * Method setMinArmor.
	 * @param min int
	 */
	public void setMinArmor(int min)
	{
		minArmor = min;
	}
	
	/**
	 * Method getMaxArmor.
	 * @return int
	 */
	public int getMaxArmor()
	{
		return maxArmor;
	}
	
	/**
	 * Method setMaxArmor.
	 * @param max int
	 */
	public void setMaxArmor(int max)
	{
		maxArmor = max;
	}
	
	/**
	 * Method getMinWeapon.
	 * @return int
	 */
	public int getMinWeapon()
	{
		return minWeapon;
	}
	
	/**
	 * Method setMinWeapon.
	 * @param minWeapon int
	 */
	public void setMinWeapon(int minWeapon)
	{
		this.minWeapon = minWeapon;
	}
	
	/**
	 * Method getMaxWeapon.
	 * @return int
	 */
	public int getMaxWeapon()
	{
		return maxWeapon;
	}
	
	/**
	 * Method setMaxWeapon.
	 * @param maxWeapon int
	 */
	public void setMaxWeapon(int maxWeapon)
	{
		this.maxWeapon = maxWeapon;
	}
	
	/**
	 * Method getIncArmor.
	 * @return int
	 */
	public int getIncArmor()
	{
		return incArmor;
	}
	
	/**
	 * Method setIncArmor.
	 * @param incArmor int
	 */
	public void setIncArmor(int incArmor)
	{
		this.incArmor = incArmor;
	}
	
	/**
	 * Method getIncWeapon.
	 * @return int
	 */
	public int getIncWeapon()
	{
		return incWeapon;
	}
	
	/**
	 * Method setIncWeapon.
	 * @param incWeapon int
	 */
	public void setIncWeapon(int incWeapon)
	{
		this.incWeapon = incWeapon;
	}
	
	/**
	 * Method getincWeaponArmor.
	 * @return int
	 */
	public int getincWeaponArmor()
	{
		return incWeaponArmor;
	}
	
	/**
	 * Method setincWeaponArmor.
	 * @param intWeaponArmor int
	 */
	public void setincWeaponArmor(int intWeaponArmor)
	{
		incWeaponArmor = intWeaponArmor;
	}
	
	/**
	 * Method getElement.
	 * @return Element
	 */
	public Element getElement()
	{
		return element;
	}
	
	/**
	 * Method setElement.
	 * @param element Element
	 */
	public void setElement(Element element)
	{
		this.element = element;
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
	 * Method getStoneLevel.
	 * @return int
	 */
	public int getStoneLevel()
	{
		return maxWeapon / 50;
	}
}
