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
package lineage2.gameserver.model;

import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TeleportLocation extends Location
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _price.
	 */
	private final long _price;
	/**
	 * Field _item.
	 */
	private final ItemTemplate _item;
	/**
	 * Field _name.
	 */
	private final int _name;
	/**
	 * Field _castleId.
	 */
	private final int _castleId;
	
	/**
	 * Constructor for TeleportLocation.
	 * @param item int
	 * @param price long
	 * @param name int
	 * @param castleId int
	 */
	public TeleportLocation(int item, long price, int name, int castleId)
	{
		_price = price;
		_name = name;
		_item = ItemHolder.getInstance().getTemplate(item);
		_castleId = castleId;
	}
	
	/**
	 * Method getPrice.
	 * @return long
	 */
	public long getPrice()
	{
		return _price;
	}
	
	/**
	 * Method getItem.
	 * @return ItemTemplate
	 */
	public ItemTemplate getItem()
	{
		return _item;
	}
	
	/**
	 * Method getName.
	 * @return int
	 */
	public int getName()
	{
		return _name;
	}
	
	/**
	 * Method getCastleId.
	 * @return int
	 */
	public int getCastleId()
	{
		return _castleId;
	}
}
