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
package lineage2.gameserver.network.loginservercon;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public enum ServerType
{
	/**
	 * Field NORMAL.
	 */
	NORMAL,
	/**
	 * Field RELAX.
	 */
	RELAX,
	/**
	 * Field TEST.
	 */
	TEST,
	/**
	 * Field NO_LABEL.
	 */
	NO_LABEL,
	/**
	 * Field RESTRICTED.
	 */
	RESTRICTED,
	/**
	 * Field EVENT.
	 */
	EVENT,
	/**
	 * Field FREE.
	 */
	FREE;
	/**
	 * Field _mask.
	 */
	private int _mask;
	
	/**
	 * Constructor for ServerType.
	 */
	ServerType()
	{
		_mask = 1 << ordinal();
	}
	
	/**
	 * Method getMask.
	 * @return int
	 */
	public int getMask()
	{
		return _mask;
	}
}
