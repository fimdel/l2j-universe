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
public class SeedProduction
{
	/**
	 * Field _seedId.
	 */
	int _seedId;
	/**
	 * Field _residual.
	 */
	long _residual;
	/**
	 * Field _price.
	 */
	long _price;
	/**
	 * Field _sales.
	 */
	long _sales;
	
	/**
	 * Constructor for SeedProduction.
	 * @param id int
	 */
	public SeedProduction(int id)
	{
		_seedId = id;
		_sales = 0;
		_price = 0;
		_sales = 0;
	}
	
	/**
	 * Constructor for SeedProduction.
	 * @param id int
	 * @param amount long
	 * @param price long
	 * @param sales long
	 */
	public SeedProduction(int id, long amount, long price, long sales)
	{
		_seedId = id;
		_residual = amount;
		_price = price;
		_sales = sales;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _seedId;
	}
	
	/**
	 * Method getCanProduce.
	 * @return long
	 */
	public long getCanProduce()
	{
		return _residual;
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
	 * Method getStartProduce.
	 * @return long
	 */
	public long getStartProduce()
	{
		return _sales;
	}
	
	/**
	 * Method setCanProduce.
	 * @param amount long
	 */
	public void setCanProduce(long amount)
	{
		_residual = amount;
	}
}
