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
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Manor;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.instances.ManorManagerInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.manor.CropProcure;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestProcureCropList extends L2GameClientPacket
{
	/**
	 * Field _count.
	 */
	private int _count;
	/**
	 * Field _items.
	 */
	private int[] _items;
	/**
	 * Field _crop.
	 */
	private int[] _crop;
	/**
	 * Field _manor.
	 */
	private int[] _manor;
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
		_count = readD();
		if (((_count * 20) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
		{
			_count = 0;
			return;
		}
		_items = new int[_count];
		_crop = new int[_count];
		_manor = new int[_count];
		_itemQ = new long[_count];
		for (int i = 0; i < _count; i++)
		{
			_items[i] = readD();
			_crop[i] = readD();
			_manor[i] = readD();
			_itemQ[i] = readQ();
			if ((_crop[i] < 1) || (_manor[i] < 1) || (_itemQ[i] < 1) || (ArrayUtils.indexOf(_items, _items[i]) < i))
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
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (activeChar.getKarma() < 0) && !activeChar.isGM())
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
		int currentManorId = manor == null ? 0 : manor.getCastle().getId();
		long totalFee = 0;
		int slots = 0;
		long weight = 0;
		try
		{
			for (int i = 0; i < _count; i++)
			{
				int objId = _items[i];
				int cropId = _crop[i];
				int manorId = _manor[i];
				long count = _itemQ[i];
				ItemInstance item = activeChar.getInventory().getItemByObjectId(objId);
				if ((item == null) || (item.getCount() < count) || (item.getItemId() != cropId))
				{
					return;
				}
				Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, manorId);
				if (castle == null)
				{
					return;
				}
				CropProcure crop = castle.getCrop(cropId, CastleManorManager.PERIOD_CURRENT);
				if ((crop == null) || (crop.getId() == 0) || (crop.getPrice() == 0))
				{
					return;
				}
				if (count > crop.getAmount())
				{
					return;
				}
				long price = SafeMath.mulAndCheck(count, crop.getPrice());
				long fee = 0;
				if ((currentManorId != 0) && (manorId != currentManorId))
				{
					fee = (price * 5) / 100;
				}
				totalFee = SafeMath.addAndCheck(totalFee, fee);
				int rewardItemId = Manor.getInstance().getRewardItem(cropId, crop.getReward());
				ItemTemplate template = ItemHolder.getInstance().getTemplate(rewardItemId);
				if (template == null)
				{
					return;
				}
				weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(count, template.getWeight()));
				if (!template.isStackable() || (activeChar.getInventory().getItemByItemId(cropId) == null))
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
			if (activeChar.getInventory().getAdena() < totalFee)
			{
				activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
			for (int i = 0; i < _count; i++)
			{
				int objId = _items[i];
				int cropId = _crop[i];
				int manorId = _manor[i];
				long count = _itemQ[i];
				ItemInstance item = activeChar.getInventory().getItemByObjectId(objId);
				if ((item == null) || (item.getCount() < count) || (item.getItemId() != cropId))
				{
					continue;
				}
				Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, manorId);
				if (castle == null)
				{
					continue;
				}
				CropProcure crop = castle.getCrop(cropId, CastleManorManager.PERIOD_CURRENT);
				if ((crop == null) || (crop.getId() == 0) || (crop.getPrice() == 0))
				{
					continue;
				}
				if (count > crop.getAmount())
				{
					continue;
				}
				int rewardItemId = Manor.getInstance().getRewardItem(cropId, crop.getReward());
				long sellPrice = count * crop.getPrice();
				long rewardPrice = ItemHolder.getInstance().getTemplate(rewardItemId).getReferencePrice();
				if (rewardPrice == 0)
				{
					continue;
				}
				double reward = (double) sellPrice / rewardPrice;
				long rewardItemCount = (long) reward + (Rnd.nextDouble() <= (reward % 1) ? 1 : 0);
				if (rewardItemCount < 1)
				{
					SystemMessage sm = new SystemMessage(SystemMessage.FAILED_IN_TRADING_S2_OF_CROP_S1);
					sm.addItemName(cropId);
					sm.addNumber(count);
					activeChar.sendPacket(sm);
					continue;
				}
				long fee = 0;
				if ((currentManorId != 0) && (manorId != currentManorId))
				{
					fee = (sellPrice * 5) / 100;
				}
				if (!activeChar.getInventory().destroyItemByObjectId(objId, count))
				{
					continue;
				}
				if (!activeChar.reduceAdena(fee, false))
				{
					SystemMessage sm = new SystemMessage(SystemMessage.FAILED_IN_TRADING_S2_OF_CROP_S1);
					sm.addItemName(cropId);
					sm.addNumber(count);
					activeChar.sendPacket(sm, Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
					continue;
				}
				crop.setAmount(crop.getAmount() - count);
				castle.updateCrop(crop.getId(), crop.getAmount(), CastleManorManager.PERIOD_CURRENT);
				castle.addToTreasuryNoTax(fee, false, false);
				if (activeChar.getInventory().addItem(rewardItemId, rewardItemCount) == null)
				{
					continue;
				}
				activeChar.sendPacket(new SystemMessage(SystemMessage.TRADED_S2_OF_CROP_S1).addItemName(cropId).addNumber(count), SystemMessage2.removeItems(cropId, count), SystemMessage2.obtainItems(rewardItemId, rewardItemCount, 0));
				if (fee > 0L)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.S1_ADENA_HAS_BEEN_PAID_FOR_PURCHASING_FEES).addNumber(fee));
				}
			}
		}
		finally
		{
			activeChar.getInventory().writeUnlock();
		}
		activeChar.sendChanges();
	}
}
