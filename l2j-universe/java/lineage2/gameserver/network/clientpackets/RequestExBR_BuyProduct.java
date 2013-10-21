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

import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.ProductHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.ProductItem;
import lineage2.gameserver.model.ProductItemComponent;
import lineage2.gameserver.network.serverpackets.ExBR_BuyProduct;
import lineage2.gameserver.network.serverpackets.ExBR_GamePoint;
import lineage2.gameserver.templates.item.ItemTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExBR_BuyProduct extends L2GameClientPacket
{
	/**
	 * Field _productId.
	 */
	private int _productId;
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
		_productId = readD();
		_count = readD();
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
		if ((_count > 99) || (_count < 0))
		{
			return;
		}
		ProductItem product = ProductHolder.getInstance().getProduct(_productId);
		if (product == null)
		{
			activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_WRONG_PRODUCT));
			return;
		}
		if ((System.currentTimeMillis() < product.getStartTimeSale()) || (System.currentTimeMillis() > product.getEndTimeSale()))
		{
			activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_SALE_PERIOD_ENDED));
			return;
		}
		int totalPoints = product.getPoints() * _count;
		if (totalPoints < 0)
		{
			activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_WRONG_PRODUCT));
			return;
		}
		final long gamePointSize = activeChar.getPremiumPoints();
		if (totalPoints > gamePointSize)
		{
			activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_NOT_ENOUGH_POINTS));
			return;
		}
		int totalWeight = 0;
		for (ProductItemComponent com : product.getComponents())
		{
			totalWeight += com.getWeight();
		}
		totalWeight *= _count;
		int totalCount = 0;
		for (ProductItemComponent com : product.getComponents())
		{
			ItemTemplate item = ItemHolder.getInstance().getTemplate(com.getItemId());
			if (item == null)
			{
				activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_WRONG_PRODUCT));
				return;
			}
			totalCount += item.isStackable() ? 1 : com.getCount() * _count;
		}
		if (!activeChar.getInventory().validateCapacity(totalCount) || !activeChar.getInventory().validateWeight(totalWeight))
		{
			activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_INVENTORY_FULL));
			return;
		}
		activeChar.reducePremiumPoints(totalPoints);
		for (ProductItemComponent comp : product.getComponents())
		{
			activeChar.getInventory().addItem(comp.getItemId(), comp.getCount() * _count);
		}
		activeChar.sendPacket(new ExBR_GamePoint(activeChar));
		activeChar.sendPacket(new ExBR_BuyProduct(ExBR_BuyProduct.RESULT_OK));
		activeChar.sendChanges();
	}
}
