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

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ProductItem
{
	/**
	 * Field NOT_LIMITED_START_TIME. (value is 315547200000)
	 */
	public static final long NOT_LIMITED_START_TIME = 315558000000L;
	/**
	 * Field NOT_LIMITED_END_TIME. (value is 2127445200000)
	 */
	public static final long NOT_LIMITED_END_TIME = 2127452400000L;
	/**
	 * Field NOT_LIMITED_START_HOUR. (value is 0)
	 */
	public static final int NOT_LIMITED_START_HOUR = 0;
	/**
	 * Field NOT_LIMITED_END_HOUR. (value is 23)
	 */
	public static final int NOT_LIMITED_END_HOUR = 23;
	/**
	 * Field NOT_LIMITED_START_MIN. (value is 0)
	 */
	public static final int NOT_LIMITED_START_MIN = 0;
	/**
	 * Field NOT_LIMITED_END_MIN. (value is 59)
	 */
	public static final int NOT_LIMITED_END_MIN = 59;
	/**
	 * Field _productId.
	 */
	private final int _productId;
	/**
	 * Field _category.
	 */
	private final int _category;
	/**
	 * Field _points.
	 */
	private final int _points;
	/**
	 * Field _tabId.
	 */
	private final int _tabId;
	/**
	 * Field _startTimeSale.
	 */
	private final long _startTimeSale;
	/**
	 * Field _endTimeSale.
	 */
	private final long _endTimeSale;
	/**
	 * Field _startHour.
	 */
	private final int _startHour;
	/**
	 * Field _endHour.
	 */
	private final int _endHour;
	/**
	 * Field _startMin.
	 */
	private final int _startMin;
	/**
	 * Field _endMin.
	 */
	private final int _endMin;
	/**
	 * Field _components.
	 */
	private ArrayList<ProductItemComponent> _components;
	
	/**
	 * Constructor for ProductItem.
	 * @param productId int
	 * @param category int
	 * @param points int
	 * @param tabId int
	 * @param startTimeSale long
	 * @param endTimeSale long
	 */
	public ProductItem(int productId, int category, int points, int tabId, long startTimeSale, long endTimeSale)
	{
		_productId = productId;
		_category = category;
		_points = points;
		_tabId = tabId;
		Calendar calendar;
		if (startTimeSale > 0)
		{
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(startTimeSale);
			_startTimeSale = startTimeSale;
			_startHour = calendar.get(Calendar.HOUR_OF_DAY);
			_startMin = calendar.get(Calendar.MINUTE);
		}
		else
		{
			_startTimeSale = NOT_LIMITED_START_TIME;
			_startHour = NOT_LIMITED_START_HOUR;
			_startMin = NOT_LIMITED_START_MIN;
		}
		if (endTimeSale > 0)
		{
			calendar = Calendar.getInstance();
			calendar.setTimeInMillis(endTimeSale);
			_endTimeSale = endTimeSale;
			_endHour = calendar.get(Calendar.HOUR_OF_DAY);
			_endMin = calendar.get(Calendar.MINUTE);
		}
		else
		{
			_endTimeSale = NOT_LIMITED_END_TIME;
			_endHour = NOT_LIMITED_END_HOUR;
			_endMin = NOT_LIMITED_END_MIN;
		}
	}
	
	/**
	 * Method setComponents.
	 * @param a ArrayList<ProductItemComponent>
	 */
	public void setComponents(ArrayList<ProductItemComponent> a)
	{
		_components = a;
	}
	
	/**
	 * Method getComponents.
	 * @return ArrayList<ProductItemComponent>
	 */
	public ArrayList<ProductItemComponent> getComponents()
	{
		if (_components == null)
		{
			_components = new ArrayList<>();
		}
		return _components;
	}
	
	/**
	 * Method getProductId.
	 * @return int
	 */
	public int getProductId()
	{
		return _productId;
	}
	
	/**
	 * Method getCategory.
	 * @return int
	 */
	public int getCategory()
	{
		return _category;
	}
	
	/**
	 * Method getPoints.
	 * @return int
	 */
	public int getPoints()
	{
		return _points;
	}
	
	/**
	 * Method getTabId.
	 * @return int
	 */
	public int getTabId()
	{
		return _tabId;
	}
	
	/**
	 * Method getStartTimeSale.
	 * @return long
	 */
	public long getStartTimeSale()
	{
		return _startTimeSale;
	}
	
	/**
	 * Method getStartHour.
	 * @return int
	 */
	public int getStartHour()
	{
		return _startHour;
	}
	
	/**
	 * Method getStartMin.
	 * @return int
	 */
	public int getStartMin()
	{
		return _startMin;
	}
	
	/**
	 * Method getEndTimeSale.
	 * @return long
	 */
	public long getEndTimeSale()
	{
		return _endTimeSale;
	}
	
	/**
	 * Method getEndHour.
	 * @return int
	 */
	public int getEndHour()
	{
		return _endHour;
	}
	
	/**
	 * Method getEndMin.
	 * @return int
	 */
	public int getEndMin()
	{
		return _endMin;
	}
}
