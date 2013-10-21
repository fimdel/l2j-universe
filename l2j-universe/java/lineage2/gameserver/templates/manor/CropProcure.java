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
package lineage2.gameserver.templates.manor;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class CropProcure
{
	/**
	 * Field _rewardType.
	 */
	int _rewardType;
	/**
	 * Field _cropId.
	 */
	int _cropId;
	/**
	 * Field _buyResidual.
	 */
	long _buyResidual;
	/**
	 * Field _buy.
	 */
	long _buy;
	/**
	 * Field _price.
	 */
	long _price;
	
	/**
	 * Constructor for CropProcure.
	 * @param id int
	 */
	public CropProcure(int id)
	{
		_cropId = id;
		_buyResidual = 0;
		_rewardType = 0;
		_buy = 0;
		_price = 0;
	}
	
	/**
	 * Constructor for CropProcure.
	 * @param id int
	 * @param amount long
	 * @param type int
	 * @param buy long
	 * @param price long
	 */
	public CropProcure(int id, long amount, int type, long buy, long price)
	{
		_cropId = id;
		_buyResidual = amount;
		_rewardType = type;
		_buy = buy;
		_price = price;
		if (_price < 0L)
		{
			_price = 0L;
		}
	}
	
	/**
	 * Method getReward.
	 * @return int
	 */
	public int getReward()
	{
		return _rewardType;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _cropId;
	}
	
	/**
	 * Method getAmount.
	 * @return long
	 */
	public long getAmount()
	{
		return _buyResidual;
	}
	
	/**
	 * Method getStartAmount.
	 * @return long
	 */
	public long getStartAmount()
	{
		return _buy;
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
	 * Method setAmount.
	 * @param amount long
	 */
	public void setAmount(long amount)
	{
		_buyResidual = amount;
	}
}
