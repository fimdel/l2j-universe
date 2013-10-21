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

import lineage2.commons.math.SafeMath;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.ManorManagerInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.manor.SeedProduction;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestBuySeed extends L2GameClientPacket
{
	/**
	 * Field _manorId. Field _count.
	 */
	private int _count, _manorId;
	/**
	 * Field _items.
	 */
	private int[] _items;
	/**
	 * Field _itemQ.
	 */
	private long[] _itemQ;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_manorId = readD();
		_count = readD();
		if (((_count * 12) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
		{
			_count = 0;
			return;
		}
		_items = new int[_count];
		_itemQ = new long[_count];
		for (int i = 0; i < _count; i++)
		{
			_items[i] = readD();
			_itemQ[i] = readQ();
			if (_itemQ[i] < 1)
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
		Player activeChar = getClient().getActiveChar();
		if ((activeChar == null) || (_count == 0))
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
			activeChar.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		if (activeChar.isInTrade())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (activeChar.isChaotic()) && !activeChar.isGM())
		{
			activeChar.sendActionFailed();
			return;
		}
		GameObject target = activeChar.getTarget();
		ManorManagerInstance manor = (target != null) && (target instanceof ManorManagerInstance) ? (ManorManagerInstance) target : null;
		if (!activeChar.isGM() && ((manor == null) || !activeChar.isInRange(manor, Creature.INTERACTION_DISTANCE)))
		{
			activeChar.sendActionFailed();
			return;
		}
		Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, _manorId);
		if (castle == null)
		{
			return;
		}
		long totalPrice = 0;
		int slots = 0;
		long weight = 0;
		try
		{
			for (int i = 0; i < _count; i++)
			{
				int seedId = _items[i];
				long count = _itemQ[i];
				long price = 0;
				long residual = 0;
				SeedProduction seed = castle.getSeed(seedId, CastleManorManager.PERIOD_CURRENT);
				price = seed.getPrice();
				residual = seed.getCanProduce();
				if (price < 1)
				{
					return;
				}
				if (residual < count)
				{
					return;
				}
				totalPrice = SafeMath.addAndCheck(totalPrice, SafeMath.mulAndCheck(count, price));
				ItemTemplate item = ItemHolder.getInstance().getTemplate(seedId);
				if (item == null)
				{
					return;
				}
				weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(count, item.getWeight()));
				if (!item.isStackable() || (activeChar.getInventory().getItemByItemId(seedId) == null))
				{
					slots++;
				}
			}
		}
		catch (ArithmeticException ae)
		{
			sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			return;
		}
		activeChar.getInventory().writeLock();
		try
		{
			if (!activeChar.getInventory().validateWeight(weight))
			{
				sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_WEIGHT_LIMIT);
				return;
			}
			if (!activeChar.getInventory().validateCapacity(slots))
			{
				sendPacket(Msg.YOUR_INVENTORY_IS_FULL);
				return;
			}
			if (!activeChar.reduceAdena(totalPrice, true))
			{
				sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			castle.addToTreasuryNoTax(totalPrice, false, true);
			for (int i = 0; i < _count; i++)
			{
				int seedId = _items[i];
				long count = _itemQ[i];
				SeedProduction seed = castle.getSeed(seedId, CastleManorManager.PERIOD_CURRENT);
				seed.setCanProduce(seed.getCanProduce() - count);
				castle.updateSeed(seed.getId(), seed.getCanProduce(), CastleManorManager.PERIOD_CURRENT);
				activeChar.getInventory().addItem(seedId, count);
				activeChar.sendPacket(SystemMessage2.obtainItems(seedId, count, 0));
			}
		}
		finally
		{
			activeChar.getInventory().writeUnlock();
		}
		activeChar.sendChanges();
	}
}
