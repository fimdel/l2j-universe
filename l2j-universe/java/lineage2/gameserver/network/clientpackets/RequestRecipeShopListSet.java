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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ManufactureItem;
import lineage2.gameserver.network.serverpackets.RecipeShopMsg;
import lineage2.gameserver.utils.TradeHelper;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestRecipeShopListSet extends L2GameClientPacket
{
	/**
	 * Field _recipes.
	 */
	private int[] _recipes;
	/**
	 * Field _prices.
	 */
	private long[] _prices;
	/**
	 * Field _count.
	 */
	private int _count;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_count = readD();
		if (((_count * 12) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
		{
			_count = 0;
			return;
		}
		_recipes = new int[_count];
		_prices = new long[_count];
		for (int i = 0; i < _count; i++)
		{
			_recipes[i] = readD();
			_prices[i] = readQ();
			if (_prices[i] < 0)
			{
				_count = 0;
				return;
			}
		}
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player manufacturer = getClient().getActiveChar();
		if ((manufacturer == null) || (_count == 0))
		{
			return;
		}
		if (!TradeHelper.checksIfCanOpenStore(manufacturer, Player.STORE_PRIVATE_MANUFACTURE))
		{
			manufacturer.sendActionFailed();
			return;
		}
		if (_count > Config.MAX_PVTCRAFT_SLOTS)
		{
			sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			return;
		}
		List<ManufactureItem> createList = new CopyOnWriteArrayList<>();
		for (int i = 0; i < _count; i++)
		{
			int recipeId = _recipes[i];
			long price = _prices[i];
			if (!manufacturer.findRecipe(recipeId))
			{
				continue;
			}
			ManufactureItem mi = new ManufactureItem(recipeId, price);
			createList.add(mi);
		}
		if (!createList.isEmpty())
		{
			manufacturer.setCreateList(createList);
			manufacturer.saveTradeList();
			manufacturer.setPrivateStoreType(Player.STORE_PRIVATE_MANUFACTURE);
			manufacturer.broadcastPacket(new RecipeShopMsg(manufacturer));
			manufacturer.sitDown(null);
			manufacturer.broadcastCharInfo();
		}
		manufacturer.sendActionFailed();
	}
}
