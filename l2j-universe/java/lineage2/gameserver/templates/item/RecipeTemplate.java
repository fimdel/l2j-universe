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
package lineage2.gameserver.templates.item;

import java.util.ArrayList;
import java.util.Collection;

import lineage2.commons.lang.ArrayUtils;
import lineage2.commons.util.Rnd;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RecipeTemplate
{
	/**
	 * @author Mobius
	 */
	public static class RecipeComponent
	{
		/**
		 * Field _itemId.
		 */
		private final int _itemId;
		/**
		 * Field _count.
		 */
		private final long _count;
		/**
		 * Field _chance.
		 */
		private final int _chance;
		
		/**
		 * Constructor for RecipeComponent.
		 * @param itemId int
		 * @param count long
		 * @param chance int
		 */
		public RecipeComponent(int itemId, long count, int chance)
		{
			_itemId = itemId;
			_count = count;
			_chance = chance;
		}
		
		/**
		 * Constructor for RecipeComponent.
		 * @param itemId int
		 * @param count long
		 */
		public RecipeComponent(int itemId, long count)
		{
			this(itemId, count, 0);
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
		 * @return long
		 */
		public long getCount()
		{
			return _count;
		}
		
		/**
		 * Method getChance.
		 * @return int
		 */
		public int getChance()
		{
			return _chance;
		}
	}
	
	/**
	 * Field _id.
	 */
	private final int _id;
	/**
	 * Field _level.
	 */
	private final int _level;
	/**
	 * Field _mpConsume.
	 */
	private final int _mpConsume;
	/**
	 * Field _successRate.
	 */
	private final int _successRate;
	/**
	 * Field _itemId.
	 */
	private final int _itemId;
	/**
	 * Field _isDwarven.
	 */
	private final boolean _isDwarven;
	/**
	 * Field _materials.
	 */
	private final Collection<RecipeComponent> _materials;
	/**
	 * Field _products.
	 */
	private final Collection<RecipeComponent> _products;
	/**
	 * Field _npcFee.
	 */
	private final Collection<RecipeComponent> _npcFee;
	
	/**
	 * Constructor for RecipeTemplate.
	 * @param id int
	 * @param level int
	 * @param mpConsume int
	 * @param successRate int
	 * @param itemId int
	 * @param isDwarven boolean
	 */
	public RecipeTemplate(int id, int level, int mpConsume, int successRate, int itemId, boolean isDwarven)
	{
		_materials = new ArrayList<>();
		_products = new ArrayList<>();
		_npcFee = new ArrayList<>();
		_id = id;
		_level = level;
		_mpConsume = mpConsume;
		_successRate = successRate;
		_itemId = itemId;
		_isDwarven = isDwarven;
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
	 * Method getMpConsume.
	 * @return int
	 */
	public int getMpConsume()
	{
		return _mpConsume;
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
	 * Method getItemId.
	 * @return int
	 */
	public int getItemId()
	{
		return _itemId;
	}
	
	/**
	 * Method isDwarven.
	 * @return boolean
	 */
	public boolean isDwarven()
	{
		return _isDwarven;
	}
	
	/**
	 * Method addMaterial.
	 * @param material RecipeComponent
	 */
	public void addMaterial(RecipeComponent material)
	{
		_materials.add(material);
	}
	
	/**
	 * Method getMaterials.
	 * @return RecipeComponent[]
	 */
	public RecipeComponent[] getMaterials()
	{
		return _materials.toArray(new RecipeComponent[_materials.size()]);
	}
	
	/**
	 * Method addProduct.
	 * @param product RecipeComponent
	 */
	public void addProduct(RecipeComponent product)
	{
		_products.add(product);
	}
	
	/**
	 * Method getProducts.
	 * @return RecipeComponent[]
	 */
	public RecipeComponent[] getProducts()
	{
		return _products.toArray(new RecipeComponent[_products.size()]);
	}
	
	/**
	 * Method getRandomProduct.
	 * @return RecipeComponent
	 */
	public RecipeComponent getRandomProduct()
	{
		int chancesAmount = 0;
		for (RecipeComponent product : _products)
		{
			chancesAmount += product.getChance();
		}
		if (Rnd.chance(chancesAmount))
		{
			RecipeComponent[] successProducts = new RecipeComponent[0];
			while (successProducts.length == 0)
			{
				for (RecipeComponent product : _products)
				{
					if (Rnd.chance(product.getChance()))
					{
						successProducts = ArrayUtils.add(successProducts, product);
					}
				}
			}
			return successProducts[Rnd.get(successProducts.length)];
		}
		return null;
	}
	
	/**
	 * Method addNpcFee.
	 * @param fee RecipeComponent
	 */
	public void addNpcFee(RecipeComponent fee)
	{
		_npcFee.add(fee);
	}
	
	/**
	 * Method getNpcFee.
	 * @return RecipeComponent[]
	 */
	public RecipeComponent[] getNpcFee()
	{
		return _npcFee.toArray(new RecipeComponent[_npcFee.size()]);
	}
}
