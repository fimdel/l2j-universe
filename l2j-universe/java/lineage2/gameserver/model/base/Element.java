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
package lineage2.gameserver.model.base;

import java.util.Arrays;

import lineage2.gameserver.stats.Stats;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum Element
{
	/**
	 * Field FIRE.
	 */
	FIRE(0, Stats.ATTACK_FIRE, Stats.DEFENCE_FIRE),
	/**
	 * Field WATER.
	 */
	WATER(1, Stats.ATTACK_WATER, Stats.DEFENCE_WATER),
	/**
	 * Field WIND.
	 */
	WIND(2, Stats.ATTACK_WIND, Stats.DEFENCE_WIND),
	/**
	 * Field EARTH.
	 */
	EARTH(3, Stats.ATTACK_EARTH, Stats.DEFENCE_EARTH),
	/**
	 * Field HOLY.
	 */
	HOLY(4, Stats.ATTACK_HOLY, Stats.DEFENCE_HOLY),
	/**
	 * Field UNHOLY.
	 */
	UNHOLY(5, Stats.ATTACK_UNHOLY, Stats.DEFENCE_UNHOLY),
	/**
	 * Field NONE.
	 */
	NONE(-2, null, null);
	/**
	 * Field VALUES.
	 */
	public final static Element[] VALUES = Arrays.copyOf(values(), 6);
	/**
	 * Field id.
	 */
	private final int id;
	/**
	 * Field attack.
	 */
	private final Stats attack;
	/**
	 * Field defence.
	 */
	private final Stats defence;
	
	/**
	 * Constructor for Element.
	 * @param id int
	 * @param attack Stats
	 * @param defence Stats
	 */
	private Element(int id, Stats attack, Stats defence)
	{
		this.id = id;
		this.attack = attack;
		this.defence = defence;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Method getAttack.
	 * @return Stats
	 */
	public Stats getAttack()
	{
		return attack;
	}
	
	/**
	 * Method getDefence.
	 * @return Stats
	 */
	public Stats getDefence()
	{
		return defence;
	}
	
	/**
	 * Method getElementById.
	 * @param id int
	 * @return Element
	 */
	public static Element getElementById(int id)
	{
		for (Element e : VALUES)
		{
			if (e.getId() == id)
			{
				return e;
			}
		}
		return NONE;
	}
	
	/**
	 * Method getReverseElement.
	 * @param element Element
	 * @return Element
	 */
	public static Element getReverseElement(Element element)
	{
		switch (element)
		{
			case WATER:
				return FIRE;
			case FIRE:
				return WATER;
			case WIND:
				return EARTH;
			case EARTH:
				return WIND;
			case HOLY:
				return UNHOLY;
			case UNHOLY:
				return HOLY;
			default:
				break;
		}
		return NONE;
	}
	
	/**
	 * Method getElementByName.
	 * @param name String
	 * @return Element
	 */
	public static Element getElementByName(String name)
	{
		for (Element e : VALUES)
		{
			if (e.name().equalsIgnoreCase(name))
			{
				return e;
			}
		}
		return NONE;
	}
}
