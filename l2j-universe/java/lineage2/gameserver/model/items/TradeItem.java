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

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class TradeItem extends ItemInfo
{
	/**
	 * Field _price.
	 */
	private long _price;
	/**
	 * Field _referencePrice.
	 */
	private long _referencePrice;
	/**
	 * Field _currentValue.
	 */
	private long _currentValue;
	/**
	 * Field _lastRechargeTime.
	 */
	private int _lastRechargeTime;
	/**
	 * Field _rechargeTime.
	 */
	private int _rechargeTime;
	
	/**
	 * Constructor for TradeItem.
	 */
	public TradeItem()
	{
		super();
	}
	
	/**
	 * Constructor for TradeItem.
	 * @param item ItemInstance
	 */
	public TradeItem(ItemInstance item)
	{
		super(item);
		setReferencePrice(item.getReferencePrice());
	}
	
	/**
	 * Method setOwnersPrice.
	 * @param price long
	 */
	public void setOwnersPrice(long price)
	{
		_price = price;
	}
	
	/**
	 * Method getOwnersPrice.
	 * @return long
	 */
	public long getOwnersPrice()
	{
		return _price;
	}
	
	/**
	 * Method setReferencePrice.
	 * @param price long
	 */
	public void setReferencePrice(long price)
	{
		_referencePrice = price;
	}
	
	/**
	 * Method getReferencePrice.
	 * @return long
	 */
	public long getReferencePrice()
	{
		return _referencePrice;
	}
	
	/**
	 * Method getStorePrice.
	 * @return long
	 */
	public long getStorePrice()
	{
		return getReferencePrice() / 2;
	}
	
	/**
	 * Method setCurrentValue.
	 * @param value long
	 */
	public void setCurrentValue(long value)
	{
		_currentValue = value;
	}
	
	/**
	 * Method getCurrentValue.
	 * @return long
	 */
	public long getCurrentValue()
	{
		return _currentValue;
	}
	
	/**
	 * Method setRechargeTime.
	 * @param rechargeTime int
	 */
	public void setRechargeTime(int rechargeTime)
	{
		_rechargeTime = rechargeTime;
	}
	
	/**
	 * Method getRechargeTime.
	 * @return int
	 */
	public int getRechargeTime()
	{
		return _rechargeTime;
	}
	
	/**
	 * Method isCountLimited.
	 * @return boolean
	 */
	public boolean isCountLimited()
	{
		return getCount() > 0;
	}
	
	/**
	 * Method setLastRechargeTime.
	 * @param lastRechargeTime int
	 */
	public void setLastRechargeTime(int lastRechargeTime)
	{
		_lastRechargeTime = lastRechargeTime;
	}
	
	/**
	 * Method getLastRechargeTime.
	 * @return int
	 */
	public int getLastRechargeTime()
	{
		return _lastRechargeTime;
	}
}
