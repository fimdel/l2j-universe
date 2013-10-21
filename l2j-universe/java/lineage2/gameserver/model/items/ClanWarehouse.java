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

import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import lineage2.gameserver.model.pledge.Clan;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ClanWarehouse extends Warehouse
{
	/**
	 * Constructor for ClanWarehouse.
	 * @param clan Clan
	 */
	public ClanWarehouse(Clan clan)
	{
		super(clan.getClanId());
	}
	
	/**
	 * Method getItemLocation.
	 * @return ItemLocation
	 */
	@Override
	public ItemLocation getItemLocation()
	{
		return ItemLocation.CLANWH;
	}
}
