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
package lineage2.gameserver.instancemanager.commission;

import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.templates.item.ExItemType;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CommissionItemInfo
{
	/**
	 * Field auctionId.
	 */
	private long auctionId;
	/**
	 * Field registeredPrice.
	 */
	private long registeredPrice;
	/**
	 * Field exItemType.
	 */
	private ExItemType exItemType;
	/**
	 * Field saleDays.
	 */
	private int saleDays;
	/**
	 * Field saleEndTime.
	 */
	private long saleEndTime;
	/**
	 * Field sellerName.
	 */
	private String sellerName;
	/**
	 * Field item.
	 */
	private final ItemInstance item;
	
	/**
	 * Constructor for CommissionItemInfo.
	 * @param item ItemInstance
	 */
	public CommissionItemInfo(ItemInstance item)
	{
		this.item = item;
	}
	
	/**
	 * Method getAuctionId.
	 * @return long
	 */
	public long getAuctionId()
	{
		return auctionId;
	}
	
	/**
	 * Method getRegisteredPrice.
	 * @return long
	 */
	public long getRegisteredPrice()
	{
		return registeredPrice;
	}
	
	/**
	 * Method getExItemType.
	 * @return ExItemType
	 */
	public ExItemType getExItemType()
	{
		return exItemType;
	}
	
	/**
	 * Method getSaleDays.
	 * @return int
	 */
	public int getSaleDays()
	{
		return saleDays;
	}
	
	/**
	 * Method getSaleEndTime.
	 * @return long
	 */
	public long getSaleEndTime()
	{
		return saleEndTime;
	}
	
	/**
	 * Method getSellerName.
	 * @return String
	 */
	public String getSellerName()
	{
		return sellerName;
	}
	
	/**
	 * Method getItem.
	 * @return ItemInstance
	 */
	public ItemInstance getItem()
	{
		return item;
	}
	
	/**
	 * Method setAuctionId.
	 * @param auctionId long
	 */
	public void setAuctionId(long auctionId)
	{
		this.auctionId = auctionId;
	}
	
	/**
	 * Method setRegisteredPrice.
	 * @param registeredPrice long
	 */
	public void setRegisteredPrice(long registeredPrice)
	{
		this.registeredPrice = registeredPrice;
	}
	
	/**
	 * Method setExItemType.
	 * @param exItemType ExItemType
	 */
	public void setExItemType(ExItemType exItemType)
	{
		this.exItemType = exItemType;
	}
	
	/**
	 * Method setSaleDays.
	 * @param saleDays int
	 */
	public void setSaleDays(int saleDays)
	{
		this.saleDays = saleDays;
	}
	
	/**
	 * Method setSaleEndTime.
	 * @param saleEndTime long
	 */
	public void setSaleEndTime(long saleEndTime)
	{
		this.saleEndTime = saleEndTime;
	}
	
	/**
	 * Method setSellerName.
	 * @param sellerName String
	 */
	public void setSellerName(String sellerName)
	{
		this.sellerName = sellerName;
	}
}
