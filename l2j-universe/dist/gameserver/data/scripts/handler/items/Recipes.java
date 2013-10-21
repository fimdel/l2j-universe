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
package handler.items;

import java.util.Collection;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.RecipeHolder;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.RecipeBookItemList;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.RecipeTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Recipes extends ScriptItemHandler
{
	/**
	 * Field _itemIds.
	 */
	private static int[] _itemIds = null;
	
	/**
	 * Constructor for Recipes.
	 */
	public Recipes()
	{
		final Collection<RecipeTemplate> rc = RecipeHolder.getInstance().getRecipes();
		_itemIds = new int[rc.size()];
		int i = 0;
		for (RecipeTemplate r : rc)
		{
			_itemIds[i++] = r.getItemId();
		}
	}
	
	/**
	 * Method useItem.
	 * @param playable Playable
	 * @param item ItemInstance
	 * @param ctrl boolean
	 * @return boolean * @see lineage2.gameserver.handler.items.IItemHandler#useItem(Playable, ItemInstance, boolean)
	 */
	@Override
	public boolean useItem(Playable playable, ItemInstance item, boolean ctrl)
	{
		if ((playable == null) || !playable.isPlayer())
		{
			return false;
		}
		final Player player = (Player) playable;
		final RecipeTemplate rp = RecipeHolder.getInstance().getRecipeByRecipeItem(item.getItemId());
		if (rp.isDwarven())
		{
			if (player.getDwarvenRecipeLimit() > 0)
			{
				if (player.getDwarvenRecipeBook().size() >= player.getDwarvenRecipeLimit())
				{
					player.sendPacket(Msg.NO_FURTHER_RECIPES_MAY_BE_REGISTERED);
					return false;
				}
				if (rp.getLevel() > player.getSkillLevel(Skill.SKILL_CRAFTING))
				{
					player.sendPacket(Msg.CREATE_ITEM_LEVEL_IS_TOO_LOW_TO_REGISTER_THIS_RECIPE);
					return false;
				}
				if (player.hasRecipe(rp))
				{
					player.sendPacket(Msg.THAT_RECIPE_IS_ALREADY_REGISTERED);
					return false;
				}
				if (!player.getInventory().destroyItem(item, 1L))
				{
					player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
					return false;
				}
				player.registerRecipe(rp, true);
				player.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BEEN_ADDED).addItemName(item.getItemId()));
				player.sendPacket(new RecipeBookItemList(player, true));
				return true;
			}
			player.sendPacket(Msg.YOU_ARE_NOT_AUTHORIZED_TO_REGISTER_A_RECIPE);
		}
		else if (player.getCommonRecipeLimit() > 0)
		{
			if (player.getCommonRecipeBook().size() >= player.getCommonRecipeLimit())
			{
				player.sendPacket(Msg.NO_FURTHER_RECIPES_MAY_BE_REGISTERED);
				return false;
			}
			if (player.hasRecipe(rp))
			{
				player.sendPacket(Msg.THAT_RECIPE_IS_ALREADY_REGISTERED);
				return false;
			}
			if (!player.getInventory().destroyItem(item, 1L))
			{
				player.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
				return false;
			}
			player.registerRecipe(rp, true);
			player.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BEEN_ADDED).addItemName(item.getItemId()));
			player.sendPacket(new RecipeBookItemList(player, false));
			return true;
		}
		else
		{
			player.sendPacket(Msg.YOU_ARE_NOT_AUTHORIZED_TO_REGISTER_A_RECIPE);
		}
		return false;
	}
	
	/**
	 * Method getItemIds.
	 * @return int[] * @see lineage2.gameserver.handler.items.IItemHandler#getItemIds()
	 */
	@Override
	public int[] getItemIds()
	{
		return _itemIds;
	}
}
