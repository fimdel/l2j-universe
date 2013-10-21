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
package lineage2.gameserver.templates.item.support;

import org.napile.primitive.sets.IntSet;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MerchantGuard
{
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _npcId.
	 */
	private final int _npcId;
	/**
	 * Field _max.
	 */
	private final int _max;
	
	/**
	 * Constructor for MerchantGuard.
	 * @param itemId int
	 * @param npcId int
	 * @param max int
	 * @param ssq IntSet
	 */
	public MerchantGuard(int itemId, int npcId, int max, IntSet ssq)
	{
		_itemId = itemId;
		_npcId = npcId;
		_max = max;
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * Method getNpcId.
	 * @return int
	 */
	public int getNpcId()
	{
		return _npcId;
	}
	
	/**
	 * Method getMax.
	 * @return int
	 */
	public int getMax()
	{
		return _max;
	}
}
