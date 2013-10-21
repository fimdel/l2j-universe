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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum RestartType
{
	/**
	 * Field TO_VILLAGE.
	 */
	TO_VILLAGE,
	/**
	 * Field TO_CLANHALL.
	 */
	TO_CLANHALL,
	/**
	 * Field TO_CASTLE.
	 */
	TO_CASTLE,
	/**
	 * Field TO_FORTRESS.
	 */
	TO_FORTRESS,
	/**
	 * Field TO_FLAG.
	 */
	TO_FLAG,
	/**
	 * Field FIXED.
	 */
	FIXED,
	/**
	 * Field AGATHION.
	 */
	AGATHION;
	/**
	 * Field VALUES.
	 */
	public static final RestartType[] VALUES = values();
}
