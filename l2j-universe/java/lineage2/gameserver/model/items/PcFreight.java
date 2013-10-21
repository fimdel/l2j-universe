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

import lineage2.gameserver.model.Player;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PcFreight extends Warehouse
{
	/**
	 * Constructor for PcFreight.
	 * @param player Player
	 */
	public PcFreight(Player player)
	{
		super(player.getObjectId());
	}
	
	/**
	 * Constructor for PcFreight.
	 * @param objectId int
	 */
	public PcFreight(int objectId)
	{
		super(objectId);
	}
	
	/**
	 * Method getItemLocation.
	 * @return ItemInstance.ItemLocation
	 */
	@Override
	public ItemInstance.ItemLocation getItemLocation()
	{
		return ItemInstance.ItemLocation.FREIGHT;
	}
}
