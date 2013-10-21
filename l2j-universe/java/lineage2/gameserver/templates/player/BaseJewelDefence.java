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
public final class BaseJewelDefence
{
	/**
	 * Field _r_earring.
	 */
	private final int _r_earring;
	/**
	 * Field _l_earring.
	 */
	private final int _l_earring;
	/**
	 * Field _r_ring.
	 */
	private final int _r_ring;
	/**
	 * Field _l_ring.
	 */
	private final int _l_ring;
	/**
	 * Field _necklace.
	 */
	private final int _necklace;
	
	/**
	 * Constructor for BaseJewelDefence.
	 * @param r_earring int
	 * @param l_earring int
	 * @param r_ring int
	 * @param l_ring int
	 * @param necklace int
	 */
	public BaseJewelDefence(int r_earring, int l_earring, int r_ring, int l_ring, int necklace)
	{
		_r_earring = r_earring;
		_l_earring = l_earring;
		_r_ring = r_ring;
		_l_ring = l_ring;
		_necklace = necklace;
	}
	
	/**
	 * Method getREaaringDef.
	 * @return int
	 */
	public int getREaaringDef()
	{
		return _r_earring;
	}
	
	/**
	 * Method getLEaaringDef.
	 * @return int
	 */
	public int getLEaaringDef()
	{
		return _l_earring;
	}
	
	/**
	 * Method getRRingDef.
	 * @return int
	 */
	public int getRRingDef()
	{
		return _r_ring;
	}
	
	/**
	 * Method getLRingDef.
	 * @return int
	 */
	public int getLRingDef()
	{
		return _l_ring;
	}
	
	/**
	 * Method getNecklaceDef.
	 * @return int
	 */
	public int getNecklaceDef()
	{
		return _necklace;
	}
}
