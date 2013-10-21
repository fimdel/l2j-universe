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

import java.io.Serializable;

import lineage2.gameserver.model.base.Element;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ItemAttributes implements Serializable
{
	/**
	 * Field serialVersionUID. (value is 401594188363005415)
	 */
	private static final long serialVersionUID = 401594188363005415L;
	/**
	 * Field fire.
	 */
	private int fire;
	/**
	 * Field water.
	 */
	private int water;
	/**
	 * Field wind.
	 */
	private int wind;
	/**
	 * Field earth.
	 */
	private int earth;
	/**
	 * Field holy.
	 */
	private int holy;
	/**
	 * Field unholy.
	 */
	private int unholy;
	
	/**
	 * Constructor for ItemAttributes.
	 */
	public ItemAttributes()
	{
		this(0, 0, 0, 0, 0, 0);
	}
	
	/**
	 * Constructor for ItemAttributes.
	 * @param fire int
	 * @param water int
	 * @param wind int
	 * @param earth int
	 * @param holy int
	 * @param unholy int
	 */
	public ItemAttributes(int fire, int water, int wind, int earth, int holy, int unholy)
	{
		this.fire = fire;
		this.water = water;
		this.wind = wind;
		this.earth = earth;
		this.holy = holy;
		this.unholy = unholy;
	}
	
	/**
	 * Method getFire.
	 * @return int
	 */
	public int getFire()
	{
		return fire;
	}
	
	/**
	 * Method setFire.
	 * @param fire int
	 */
	public void setFire(int fire)
	{
		this.fire = fire;
	}
	
	/**
	 * Method getWater.
	 * @return int
	 */
	public int getWater()
	{
		return water;
	}
	
	/**
	 * Method setWater.
	 * @param water int
	 */
	public void setWater(int water)
	{
		this.water = water;
	}
	
	/**
	 * Method getWind.
	 * @return int
	 */
	public int getWind()
	{
		return wind;
	}
	
	/**
	 * Method setWind.
	 * @param wind int
	 */
	public void setWind(int wind)
	{
		this.wind = wind;
	}
	
	/**
	 * Method getEarth.
	 * @return int
	 */
	public int getEarth()
	{
		return earth;
	}
	
	/**
	 * Method setEarth.
	 * @param earth int
	 */
	public void setEarth(int earth)
	{
		this.earth = earth;
	}
	
	/**
	 * Method getHoly.
	 * @return int
	 */
	public int getHoly()
	{
		return holy;
	}
	
	/**
	 * Method setHoly.
	 * @param holy int
	 */
	public void setHoly(int holy)
	{
		this.holy = holy;
	}
	
	/**
	 * Method getUnholy.
	 * @return int
	 */
	public int getUnholy()
	{
		return unholy;
	}
	
	/**
	 * Method setUnholy.
	 * @param unholy int
	 */
	public void setUnholy(int unholy)
	{
		this.unholy = unholy;
	}
	
	/**
	 * Method getElement.
	 * @return Element
	 */
	public Element getElement()
	{
		if (fire > 0)
		{
			return Element.FIRE;
		}
		else if (water > 0)
		{
			return Element.WATER;
		}
		else if (wind > 0)
		{
			return Element.WIND;
		}
		else if (earth > 0)
		{
			return Element.EARTH;
		}
		else if (holy > 0)
		{
			return Element.HOLY;
		}
		else if (unholy > 0)
		{
			return Element.UNHOLY;
		}
		return Element.NONE;
	}
	
	/**
	 * Method getValue.
	 * @return int
	 */
	public int getValue()
	{
		if (fire > 0)
		{
			return fire;
		}
		else if (water > 0)
		{
			return water;
		}
		else if (wind > 0)
		{
			return wind;
		}
		else if (earth > 0)
		{
			return earth;
		}
		else if (holy > 0)
		{
			return holy;
		}
		else if (unholy > 0)
		{
			return unholy;
		}
		return 0;
	}
	
	/**
	 * Method setValue.
	 * @param element Element
	 * @param value int
	 */
	public void setValue(Element element, int value)
	{
		switch (element)
		{
			case FIRE:
				fire = value;
				break;
			case WATER:
				water = value;
				break;
			case WIND:
				wind = value;
				break;
			case EARTH:
				earth = value;
				break;
			case HOLY:
				holy = value;
				break;
			case UNHOLY:
				unholy = value;
				break;
			case NONE:
				break;
		}
	}
	
	/**
	 * Method getValue.
	 * @param element Element
	 * @return int
	 */
	public int getValue(Element element)
	{
		switch (element)
		{
			case FIRE:
				return fire;
			case WATER:
				return water;
			case WIND:
				return wind;
			case EARTH:
				return earth;
			case HOLY:
				return holy;
			case UNHOLY:
				return unholy;
			default:
				return 0;
		}
	}
	
	/**
	 * Method clone.
	 * @return ItemAttributes
	 */
	@Override
	public ItemAttributes clone()
	{
		return new ItemAttributes(fire, water, wind, earth, holy, unholy);
	}
}
