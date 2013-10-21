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
package lineage2.gameserver.instancemanager.itemauction;

import lineage2.gameserver.model.items.ItemInfo;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class AuctionItem extends ItemInfo
{
	/**
	 * Field _auctionItemId.
	 */
	private final int _auctionItemId;
	/**
	 * Field _auctionLength.
	 */
	private final int _auctionLength;
	/**
	 * Field _auctionInitBid.
	 */
	private final long _auctionInitBid;
	
	/**
	 * Constructor for AuctionItem.
	 * @param auctionItemId int
	 * @param auctionLength int
	 * @param auctionInitBid long
	 * @param itemId int
	 * @param itemCount long
	 * @param itemExtra StatsSet
	 */
	public AuctionItem(final int auctionItemId, final int auctionLength, final long auctionInitBid, final int itemId, final long itemCount, final StatsSet itemExtra)
	{
		_auctionItemId = auctionItemId;
		_auctionLength = auctionLength;
		_auctionInitBid = auctionInitBid;
		setObjectId(itemId);
		setItemId(itemId);
		setCount(itemCount);
		setEnchantLevel(itemExtra.getInteger("enchant_level", 0));
		setAugmentationId(itemExtra.getInteger("augmentation_id", 0));
	}
	
	/**
	 * Method getAuctionItemId.
	 * @return int
	 */
	public final int getAuctionItemId()
	{
		return _auctionItemId;
	}
	
	/**
	 * Method getAuctionLength.
	 * @return int
	 */
	public final int getAuctionLength()
	{
		return _auctionLength;
	}
	
	/**
	 * Method getAuctionInitBid.
	 * @return long
	 */
	public final long getAuctionInitBid()
	{
		return _auctionInitBid;
	}
	
	/**
	 * Method createNewItemInstance.
	 * @return ItemInstance
	 */
	public final ItemInstance createNewItemInstance()
	{
		final ItemInstance item = ItemFunctions.createItem(getItemId());
		item.setEnchantLevel(getEnchantLevel());
		if (getAugmentationId() != 0)
		{
			item.setAugmentationId(getAugmentationId());
		}
		return item;
	}
}
