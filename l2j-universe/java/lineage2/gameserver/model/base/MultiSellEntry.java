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
package lineage2.gameserver.model.base;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MultiSellEntry
{
	/**
	 * Field _entryId.
	 */
	private int _entryId;
	/**
	 * Field _ingredients.
	 */
	private final List<MultiSellIngredient> _ingredients = new ArrayList<>();
	/**
	 * Field _production.
	 */
	private final List<MultiSellIngredient> _production = new ArrayList<>();
	/**
	 * Field _tax.
	 */
	private long _tax;
	
	/**
	 * Constructor for MultiSellEntry.
	 */
	public MultiSellEntry()
	{
	}
	
	/**
	 * Constructor for MultiSellEntry.
	 * @param id int
	 */
	public MultiSellEntry(int id)
	{
		_entryId = id;
	}
	
	/**
	 * Constructor for MultiSellEntry.
	 * @param id int
	 * @param product int
	 * @param prod_count int
	 * @param enchant int
	 */
	public MultiSellEntry(int id, int product, int prod_count, int enchant)
	{
		_entryId = id;
		addProduct(new MultiSellIngredient(product, prod_count, enchant));
	}
	
	/**
	 * Method setEntryId.
	 * @param entryId int
	 */
	public void setEntryId(int entryId)
	{
		_entryId = entryId;
	}
	
	/**
	 * Method getEntryId.
	 * @return int
	 */
	public int getEntryId()
	{
		return _entryId;
	}
	
	/**
	 * Method addIngredient.
	 * @param ingredient MultiSellIngredient
	 */
	public void addIngredient(MultiSellIngredient ingredient)
	{
		if (ingredient.getItemCount() > 0)
		{
			_ingredients.add(ingredient);
		}
	}
	
	/**
	 * Method getIngredients.
	 * @return List<MultiSellIngredient>
	 */
	public List<MultiSellIngredient> getIngredients()
	{
		return _ingredients;
	}
	
	/**
	 * Method addProduct.
	 * @param ingredient MultiSellIngredient
	 */
	public void addProduct(MultiSellIngredient ingredient)
	{
		_production.add(ingredient);
	}
	
	/**
	 * Method getProduction.
	 * @return List<MultiSellIngredient>
	 */
	public List<MultiSellIngredient> getProduction()
	{
		return _production;
	}
	
	/**
	 * Method getTax.
	 * @return long
	 */
	public long getTax()
	{
		return _tax;
	}
	
	/**
	 * Method setTax.
	 * @param tax long
	 */
	public void setTax(long tax)
	{
		_tax = tax;
	}
	
	/**
	 * Method hashCode.
	 * @return int
	 */
	@Override
	public int hashCode()
	{
		return _entryId;
	}
	
	/**
	 * Method clone.
	 * @return MultiSellEntry
	 */
	@Override
	public MultiSellEntry clone()
	{
		MultiSellEntry ret = new MultiSellEntry(_entryId);
		for (MultiSellIngredient i : _ingredients)
		{
			ret.addIngredient(i.clone());
		}
		for (MultiSellIngredient i : _production)
		{
			ret.addProduct(i.clone());
		}
		return ret;
	}
}
