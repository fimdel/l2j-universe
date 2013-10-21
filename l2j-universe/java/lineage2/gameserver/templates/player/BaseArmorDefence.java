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
package lineage2.gameserver.templates.player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class BaseArmorDefence
{
	/**
	 * Field _chest.
	 */
	private final int _chest;
	/**
	 * Field _legs.
	 */
	private final int _legs;
	/**
	 * Field _helmet.
	 */
	private final int _helmet;
	/**
	 * Field _boots.
	 */
	private final int _boots;
	/**
	 * Field _gloves.
	 */
	private final int _gloves;
	/**
	 * Field _underwear.
	 */
	private final int _underwear;
	/**
	 * Field _cloak.
	 */
	private final int _cloak;
	
	/**
	 * Constructor for BaseArmorDefence.
	 * @param chest int
	 * @param legs int
	 * @param helmet int
	 * @param boots int
	 * @param gloves int
	 * @param underwear int
	 * @param cloak int
	 */
	public BaseArmorDefence(int chest, int legs, int helmet, int boots, int gloves, int underwear, int cloak)
	{
		_chest = chest;
		_legs = legs;
		_helmet = helmet;
		_boots = boots;
		_gloves = gloves;
		_underwear = underwear;
		_cloak = cloak;
	}
	
	/**
	 * Method getChestDef.
	 * @return int
	 */
	public int getChestDef()
	{
		return _chest;
	}
	
	/**
	 * Method getLegsDef.
	 * @return int
	 */
	public int getLegsDef()
	{
		return _legs;
	}
	
	/**
	 * Method getHelmetDef.
	 * @return int
	 */
	public int getHelmetDef()
	{
		return _helmet;
	}
	
	/**
	 * Method getBootsDef.
	 * @return int
	 */
	public int getBootsDef()
	{
		return _boots;
	}
	
	/**
	 * Method getGlovesDef.
	 * @return int
	 */
	public int getGlovesDef()
	{
		return _gloves;
	}
	
	/**
	 * Method getUnderwearDef.
	 * @return int
	 */
	public int getUnderwearDef()
	{
		return _underwear;
	}
	
	/**
	 * Method getCloakDef.
	 * @return int
	 */
	public int getCloakDef()
	{
		return _cloak;
	}
}
