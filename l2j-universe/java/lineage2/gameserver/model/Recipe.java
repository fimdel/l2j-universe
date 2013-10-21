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

import lineage2.gameserver.templates.item.RecipeTemplate.RecipeComponent;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Recipe
{
	/**
	 * Field _recipes.
	 */
	private RecipeComponent[] _recipes;
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _level.
	 */
	private final int _level;
	/**
	 * Field _recipeId.
	 */
	private final int _recipeId;
	/**
	 * Field _recipeName.
	 */
	private final String _recipeName;
	/**
	 * Field _successRate.
	 */
	private final int _successRate;
	/**
	 * Field _mpCost.
	 */
	private final int _mpCost;
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _foundation.
	 */
	private final int _foundation;
	/**
	 * Field _count.
	 */
	private final int _count;
	/**
	 * Field _isdwarvencraft.
	 */
	private final boolean _isdwarvencraft;
	/**
	 * Field _sp. Field _exp.
	 */
	private final long _exp, _sp;
	
	/**
	 * Constructor for Recipe.
	 * @param id int
	 * @param level int
	 * @param recipeId int
	 * @param recipeName String
	 * @param successRate int
	 * @param mpCost int
	 * @param itemId int
	 * @param foundation int
	 * @param count int
	 * @param exp long
	 * @param sp long
	 * @param isdwarvencraft boolean
	 */
	public Recipe(int id, int level, int recipeId, String recipeName, int successRate, int mpCost, int itemId, int foundation, int count, long exp, long sp, boolean isdwarvencraft)
	{
		_id = id;
		_recipes = new RecipeComponent[0];
		_level = level;
		_recipeId = recipeId;
		_recipeName = recipeName;
		_successRate = successRate;
		_mpCost = mpCost;
		_itemId = itemId;
		_foundation = foundation;
		_count = count;
		_exp = exp;
		_sp = sp;
		_isdwarvencraft = isdwarvencraft;
	}
	
	/**
	 * Method addRecipe.
	 * @param recipe RecipeComponent
	 */
	public void addRecipe(RecipeComponent recipe)
	{
		int len = _recipes.length;
		RecipeComponent[] tmp = new RecipeComponent[len + 1];
		System.arraycopy(_recipes, 0, tmp, 0, len);
		tmp[len] = recipe;
		_recipes = tmp;
	}
	
	/**
	 * Method getId.
	 * @return int
	 */
	public int getId()
	{
		return _id;
	}
	
	/**
	 * Method getLevel.
	 * @return int
	 */
	public int getLevel()
	{
		return _level;
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
	 * Method getRecipeName.
	 * @return String
	 */
	public String getRecipeName()
	{
		return _recipeName;
	}
	
	/**
	 * Method getSuccessRate.
	 * @return int
	 */
	public int getSuccessRate()
	{
		return _successRate;
	}
	
	/**
	 * Method getMpCost.
	 * @return int
	 */
	public int getMpCost()
	{
		return _mpCost;
	}
	
	/**
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * Method getCount.
	 * @return int
	 */
	public int getCount()
	{
		return _count;
	}
	
	/**
	 * Method getRecipes.
	 * @return RecipeComponent[]
	 */
	public RecipeComponent[] getRecipes()
	{
		return _recipes;
	}
	
	/**
	 * Method isDwarvenRecipe.
	 * @return boolean
	 */
	public boolean isDwarvenRecipe()
	{
		return _isdwarvencraft;
	}
	
	/**
	 * Method getExp.
	 * @return long
	 */
	public long getExp()
	{
		return _exp;
	}
	
	/**
	 * Method getSp.
	 * @return long
	 */
	public long getSp()
	{
		return _sp;
	}
	
	/**
	 * Method getFoundation.
	 * @return int
	 */
	public int getFoundation()
	{
		return _foundation;
	}
}
