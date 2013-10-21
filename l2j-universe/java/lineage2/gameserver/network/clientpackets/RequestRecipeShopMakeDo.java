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
import lineage2.gameserver.data.xml.holder.RecipeHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.ManufactureItem;
import lineage2.gameserver.network.serverpackets.RecipeShopItemInfo;
import lineage2.gameserver.network.serverpackets.StatusUpdate.StatusUpdateField;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.RecipeTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.TradeHelper;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestRecipeShopMakeDo extends L2GameClientPacket
{
	/**
	 * Field _manufacturerId.
	 */
	private int _manufacturerId;
	/**
	 * Field _recipeId.
	 */
	private int _recipeId;
	/**
	 * Field _price.
	 */
	private long _price;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_manufacturerId = readD();
		_recipeId = readD();
		_price = readQ();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player buyer = getClient().getActiveChar();
		if (buyer == null)
		{
			return;
		}
		if (buyer.isActionsDisabled())
		{
			buyer.sendActionFailed();
			return;
		}
		if (buyer.isInStoreMode())
		{
			buyer.sendPacket(SystemMsg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		if (buyer.isInTrade())
		{
			buyer.sendActionFailed();
			return;
		}
		if (buyer.isFishing())
		{
			buyer.sendPacket(SystemMsg.YOU_CANNOT_DO_THAT_WHILE_FISHING_2);
			return;
		}
		if (!buyer.getPlayerAccess().UseTrade)
		{
			buyer.sendPacket(SystemMsg.SOME_LINEAGE_II_FEATURES_HAVE_BEEN_LIMITED_FOR_FREE_TRIALS_____);
			return;
		}
		Player manufacturer = (Player) buyer.getVisibleObject(_manufacturerId);
		if ((manufacturer == null) || (manufacturer.getPrivateStoreType() != Player.STORE_PRIVATE_MANUFACTURE) || !manufacturer.isInRangeZ(buyer, Creature.INTERACTION_DISTANCE))
		{
			buyer.sendActionFailed();
			return;
		}
		RecipeTemplate recipe = null;
		for (ManufactureItem mi : manufacturer.getCreateList())
		{
			if (mi.getRecipeId() == _recipeId)
			{
				if (_price == mi.getCost())
				{
					recipe = RecipeHolder.getInstance().getRecipeByRecipeId(_recipeId);
					break;
				}
			}
		}
		if (recipe == null)
		{
			buyer.sendActionFailed();
			return;
		}
		if (recipe.getMaterials().length == 0)
		{
			manufacturer.sendPacket(SystemMsg.THE_RECIPE_IS_INCORRECT);
			buyer.sendPacket(SystemMsg.THE_RECIPE_IS_INCORRECT);
			return;
		}
		if (!manufacturer.findRecipe(_recipeId))
		{
			buyer.sendActionFailed();
			return;
		}
		int success = 0;
		if (manufacturer.getCurrentMp() < recipe.getMpConsume())
		{
			manufacturer.sendPacket(SystemMsg.NOT_ENOUGH_MP);
			buyer.sendPacket(SystemMsg.NOT_ENOUGH_MP, new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
			return;
		}
		buyer.getInventory().writeLock();
		try
		{
			if (buyer.getAdena() < _price)
			{
				buyer.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA, new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
				return;
			}
			RecipeTemplate.RecipeComponent[] materials = recipe.getMaterials();
			for (RecipeTemplate.RecipeComponent material : materials)
			{
				if (material.getCount() == 0)
				{
					continue;
				}
				ItemInstance item = buyer.getInventory().getItemByItemId(material.getItemId());
				if ((item == null) || (material.getCount() > item.getCount()))
				{
					buyer.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_MATERIALS_TO_PERFORM_THAT_ACTION, new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
					return;
				}
			}
			if (!buyer.reduceAdena(_price, false))
			{
				buyer.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_ADENA, new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
				return;
			}
			for (RecipeTemplate.RecipeComponent material : materials)
			{
				if (material.getCount() == 0)
				{
					continue;
				}
				buyer.getInventory().destroyItemByItemId(material.getItemId(), material.getCount());
				buyer.sendPacket(SystemMessage2.removeItems(material.getItemId(), material.getCount()));
			}
			long tax = TradeHelper.getTax(manufacturer, _price);
			if (tax > 0)
			{
				_price -= tax;
			}
			manufacturer.addAdena(_price);
		}
		finally
		{
			buyer.getInventory().writeUnlock();
		}
		manufacturer.reduceCurrentMp(recipe.getMpConsume(), null);
		manufacturer.sendStatusUpdate(false, false, StatusUpdateField.CUR_MP);
		RecipeTemplate.RecipeComponent product = recipe.getRandomProduct();
		int itemId = product.getItemId();
		long itemsCount = product.getCount();
		if (Rnd.chance(recipe.getSuccessRate()))
		{
			ItemFunctions.addItem(buyer, itemId, itemsCount, true);
			if (itemsCount > 1)
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S1_CREATED_S2_S3_AT_THE_PRICE_OF_S4_ADENA);
				sm.addString(manufacturer.getName());
				sm.addItemName(itemId);
				sm.addNumber(itemsCount);
				sm.addNumber(_price);
				buyer.sendPacket(sm);
				sm = new SystemMessage(SystemMessage.S2_S3_HAVE_BEEN_SOLD_TO_S1_FOR_S4_ADENA);
				sm.addString(buyer.getName());
				sm.addItemName(itemId);
				sm.addNumber(itemsCount);
				sm.addNumber(_price);
				manufacturer.sendPacket(sm);
			}
			else
			{
				SystemMessage sm = new SystemMessage(SystemMessage.S1_CREATED_S2_AFTER_RECEIVING_S3_ADENA);
				sm.addString(manufacturer.getName());
				sm.addItemName(itemId);
				sm.addNumber(_price);
				buyer.sendPacket(sm);
				sm = new SystemMessage(SystemMessage.S2_IS_SOLD_TO_S1_AT_THE_PRICE_OF_S3_ADENA);
				sm.addString(buyer.getName());
				sm.addItemName(itemId);
				sm.addNumber(_price);
				manufacturer.sendPacket(sm);
			}
			success = 1;
		}
		else
		{
			SystemMessage sm = new SystemMessage(SystemMessage.S1_HAS_FAILED_TO_CREATE_S2_AT_THE_PRICE_OF_S3_ADENA);
			sm.addString(manufacturer.getName());
			sm.addItemName(itemId);
			sm.addNumber(_price);
			buyer.sendPacket(sm);
			sm = new SystemMessage(SystemMessage.THE_ATTEMPT_TO_CREATE_S2_FOR_S1_AT_THE_PRICE_OF_S3_ADENA_HAS_FAILED);
			sm.addString(buyer.getName());
			sm.addItemName(itemId);
			sm.addNumber(_price);
			manufacturer.sendPacket(sm);
		}
		buyer.sendChanges();
		buyer.sendPacket(new RecipeShopItemInfo(buyer, manufacturer, _recipeId, _price, success));
	}
}
