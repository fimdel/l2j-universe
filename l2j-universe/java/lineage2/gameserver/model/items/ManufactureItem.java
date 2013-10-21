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
public class ManufactureItem
{
	/**
	 * Field _recipeId.
	 */
	private final int _recipeId;
	/**
	 * Field _cost.
	 */
	private final long _cost;
	
	/**
	 * Constructor for ManufactureItem.
	 * @param recipeId int
	 * @param cost long
	 */
	public ManufactureItem(int recipeId, long cost)
	{
		_recipeId = recipeId;
		_cost = cost;
	}
	
	/**
	 * Method getRecipeId.
	 * @return int
	 */
	public int getRecipeId()
	{
		return _recipeId;
	}
	
	/**
	 * Method getCost.
	 * @return long
	 */
	public long getCost()
	{
		return _cost;
	}
	
	/**
	 * Method equals.
	 * @param o Object
	 * @return boolean
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		if (o == null)
		{
			return false;
		}
		if (o.getClass() != this.getClass())
		{
			return false;
		}
		return ((ManufactureItem) o).getRecipeId() == getRecipeId();
	}
}
