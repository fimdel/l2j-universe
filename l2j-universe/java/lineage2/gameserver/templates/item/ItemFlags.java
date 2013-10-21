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
public enum ItemFlags
{
	/**
	 * Field DESTROYABLE.
	 */
	DESTROYABLE(true),
	/**
	 * Field DROPABLE.
	 */
	DROPABLE(true),
	/**
	 * Field FREIGHTABLE.
	 */
	FREIGHTABLE(false),
	/**
	 * Field AUGMENTABLE.
	 */
	AUGMENTABLE(true),
	/**
	 * Field ENCHANTABLE.
	 */
	ENCHANTABLE(true),
	/**
	 * Field ATTRIBUTABLE.
	 */
	ATTRIBUTABLE(true),
	/**
	 * Field SELLABLE.
	 */
	SELLABLE(true),
	/**
	 * Field TRADEABLE.
	 */
	TRADEABLE(true),
	/**
	 * Field STOREABLE.
	 */
	STOREABLE(true);
	/**
	 * Field VALUES.
	 */
	public static final ItemFlags[] VALUES = values();
	/**
	 * Field _mask.
	 */
	private final int _mask;
	/**
	 * Field _defaultValue.
	 */
	private final boolean _defaultValue;
	
	/**
	 * Constructor for ItemFlags.
	 * @param defaultValue boolean
	 */
	ItemFlags(boolean defaultValue)
	{
		_defaultValue = defaultValue;
		_mask = 1 << ordinal();
	}
	
	/**
	 * Method mask.
	 * @return int
	 */
	public int mask()
	{
		return _mask;
	}
	
	/**
	 * Method getDefaultValue.
	 * @return boolean
	 */
	public boolean getDefaultValue()
	{
		return _defaultValue;
	}
}
