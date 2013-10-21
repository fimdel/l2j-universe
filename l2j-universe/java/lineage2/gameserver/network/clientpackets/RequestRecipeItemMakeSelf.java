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
package lineage2.gameserver.network.clientpackets;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.RecipeHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.ActionFail;
import lineage2.gameserver.network.serverpackets.RecipeItemMakeInfo;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.EtcItemTemplate.EtcItemType;
import lineage2.gameserver.templates.item.RecipeTemplate;
import lineage2.gameserver.templates.item.RecipeTemplate.RecipeComponent;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestRecipeItemMakeSelf extends L2GameClientPacket
{
	/**
	 * Field _recipeId.
	 */
	private int _recipeId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_recipeId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isInStoreMode())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isProcessingRequest())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(SystemMsg.YOU_CANNOT_DO_THAT_WHILE_FISHING_);
			return;
		}
		RecipeTemplate recipe = RecipeHolder.getInstance().getRecipeByRecipeId(_recipeId);
		if ((recipe == null) || (recipe.getMaterials().length == 0))
		{
			activeChar.sendPacket(SystemMsg.THE_RECIPE_IS_INCORRECT);
			return;
		}
		if (activeChar.getCurrentMp() < recipe.getMpConsume())
		{
			activeChar.sendPacket(SystemMsg.NOT_ENOUGH_MP, new RecipeItemMakeInfo(activeChar, recipe, 0));
			return;
		}
		if (!activeChar.findRecipe(_recipeId))
		{
			activeChar.sendPacket(SystemMsg.PLEASE_REGISTER_A_RECIPE, ActionFail.STATIC);
			return;
		}
		activeChar.getInventory().writeLock();
		try
		{
			RecipeComponent[] materials = recipe.getMaterials();
			for (RecipeComponent material : materials)
			{
				if (material.getCount() == 0)
				{
					continue;
				}
				if (Config.ALT_GAME_UNREGISTER_RECIPE && (ItemHolder.getInstance().getTemplate(material.getItemId()).getItemType() == EtcItemType.RECIPE))
				{
					RecipeTemplate rp = RecipeHolder.getInstance().getRecipeByRecipeItem(material.getItemId());
					if (activeChar.hasRecipe(rp))
					{
						continue;
					}
					activeChar.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_MATERIALS_TO_PERFORM_THAT_ACTION, new RecipeItemMakeInfo(activeChar, recipe, 0));
					return;
				}
				ItemInstance item = activeChar.getInventory().getItemByItemId(material.getItemId());
				if ((item == null) || (item.getCount() < material.getCount()))
				{
					activeChar.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_MATERIALS_TO_PERFORM_THAT_ACTION, new RecipeItemMakeInfo(activeChar, recipe, 0));
					return;
				}
			}
			for (RecipeComponent material : materials)
			{
				if (material.getCount() == 0)
				{
					continue;
				}
				if (Config.ALT_GAME_UNREGISTER_RECIPE && (ItemHolder.getInstance().getTemplate(material.getItemId()).getItemType() == EtcItemType.RECIPE))
				{
					activeChar.unregisterRecipe(RecipeHolder.getInstance().getRecipeByRecipeItem(material.getItemId()).getId());
				}
				else
				{
					if (!activeChar.getInventory().destroyItemByItemId(material.getItemId(), material.getCount()))
					{
						continue;
					}
					activeChar.sendPacket(SystemMessage2.removeItems(material.getItemId(), material.getCount()));
				}
			}
		}
		finally
		{
			activeChar.getInventory().writeUnlock();
		}
		activeChar.resetWaitSitTime();
		activeChar.reduceCurrentMp(recipe.getMpConsume(), null);
		RecipeComponent product = recipe.getRandomProduct();
		int itemId = product.getItemId();
		long itemsCount = product.getCount();
		int success = 0;
		if (Rnd.chance(recipe.getSuccessRate()))
		{
			ItemFunctions.addItem(activeChar, itemId, itemsCount, true);
			success = 1;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1_MANUFACTURING_FAILURE).addItemName(itemId));
		}
		activeChar.sendPacket(new RecipeItemMakeInfo(activeChar, recipe, success));
	}
}
