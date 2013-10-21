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

import java.util.Collections;
import java.util.List;

import lineage2.commons.math.SafeMath;
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
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.manor.CropProcure;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("unused")
public class RequestProcureCrop extends L2GameClientPacket
{
	/**
	 * Field _manorId.
	 */
	private int _manorId;
	/**
	 * Field _count.
	 */
	private int _count;
	/**
	 * Field _items.
	 */
	private int[] _items;
	/**
	 * Field _itemQ.
	 */
	private long[] _itemQ;
	/**
	 * Field _procureList.
	 */
	private List<CropProcure> _procureList = Collections.emptyList();
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_manorId = readD();
		_count = readD();
		if (((_count * 16) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
		{
			_count = 0;
			return;
		}
		_items = new int[_count];
		_itemQ = new long[_count];
		for (int i = 0; i < _count; i++)
		{
			readD();
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
		Castle castle = ResidenceHolder.getInstance().getResidence(Castle.class, _manorId);
		if (castle == null)
		{
			return;
		}
		int slots = 0;
		long weight = 0;
		try
		{
			for (int i = 0; i < _count; i++)
			{
				int itemId = _items[i];
				long count = _itemQ[i];
				CropProcure crop = castle.getCrop(itemId, CastleManorManager.PERIOD_CURRENT);
				if (crop == null)
				{
					return;
				}
				int rewradItemId = Manor.getInstance().getRewardItem(itemId, castle.getCrop(itemId, CastleManorManager.PERIOD_CURRENT).getReward());
				long rewradItemCount = Manor.getInstance().getRewardAmountPerCrop(castle.getId(), itemId, castle.getCropRewardType(itemId));
				rewradItemCount = SafeMath.mulAndCheck(count, rewradItemCount);
				ItemTemplate template = ItemHolder.getInstance().getTemplate(rewradItemId);
				if (template == null)
				{
					return;
				}
				weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(count, template.getWeight()));
				if (!template.isStackable() || (activeChar.getInventory().getItemByItemId(itemId) == null))
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
			_procureList = castle.getCropProcure(CastleManorManager.PERIOD_CURRENT);
			for (int i = 0; i < _count; i++)
			{
				int itemId = _items[i];
				long count = _itemQ[i];
				int rewradItemId = Manor.getInstance().getRewardItem(itemId, castle.getCrop(itemId, CastleManorManager.PERIOD_CURRENT).getReward());
				long rewradItemCount = Manor.getInstance().getRewardAmountPerCrop(castle.getId(), itemId, castle.getCropRewardType(itemId));
				rewradItemCount = SafeMath.mulAndCheck(count, rewradItemCount);
				if (!activeChar.getInventory().destroyItemByItemId(itemId, count))
				{
					continue;
				}
				ItemInstance item = activeChar.getInventory().addItem(rewradItemId, rewradItemCount);
				if (item == null)
				{
					continue;
				}
				activeChar.sendPacket(SystemMessage2.obtainItems(rewradItemId, rewradItemCount, 0));
			}
		}
		catch (ArithmeticException ae)
		{
			_count = 0;
		}
		finally
		{
			activeChar.getInventory().writeUnlock();
		}
		activeChar.sendChanges();
	}
}
