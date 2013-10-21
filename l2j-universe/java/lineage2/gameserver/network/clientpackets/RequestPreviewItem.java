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

import java.util.HashMap;
import java.util.Map;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.BuyListHolder;
import lineage2.gameserver.data.xml.holder.BuyListHolder.NpcTradeList;
import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.base.Race;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.Inventory;
import lineage2.gameserver.network.serverpackets.ShopPreviewInfo;
import lineage2.gameserver.network.serverpackets.ShopPreviewList;
import lineage2.gameserver.templates.item.ArmorTemplate.ArmorType;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.WeaponTemplate.WeaponType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestPreviewItem extends L2GameClientPacket
{
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(RequestPreviewItem.class);
	/**
	 * Field _unknow.
	 */
	@SuppressWarnings("unused")
	private int _unknow;
	/**
	 * Field _listId.
	 */
	private int _listId;
	/**
	 * Field _count.
	 */
	private int _count;
	/**
	 * Field _items.
	 */
	private int[] _items;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_unknow = readD();
		_listId = readD();
		_count = readD();
		if (((_count * 4) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
		{
			_count = 0;
			return;
		}
		_items = new int[_count];
		for (int i = 0; i < _count; i++)
		{
			_items[i] = readD();
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
		NpcInstance merchant = activeChar.getLastNpc();
		boolean isValidMerchant = (merchant != null) && merchant.isMerchantNpc();
		if (!activeChar.isGM() && ((merchant == null) || !isValidMerchant || !activeChar.isInRange(merchant, Creature.INTERACTION_DISTANCE)))
		{
			activeChar.sendActionFailed();
			return;
		}
		NpcTradeList list = BuyListHolder.getInstance().getBuyList(_listId);
		if (list == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		long totalPrice = 0;
		Map<Integer, Integer> itemList = new HashMap<>();
		try
		{
			for (int i = 0; i < _count; i++)
			{
				int itemId = _items[i];
				if (list.getItemByItemId(itemId) == null)
				{
					activeChar.sendActionFailed();
					return;
				}
				ItemTemplate template = ItemHolder.getInstance().getTemplate(itemId);
				if (template == null)
				{
					continue;
				}
				if (!template.isEquipable())
				{
					continue;
				}
				int paperdoll = Inventory.getPaperdollIndex(template.getBodyPart());
				if (paperdoll < 0)
				{
					continue;
				}
				if (activeChar.getRace() == Race.kamael)
				{
					if ((template.getItemType() == ArmorType.HEAVY) || (template.getItemType() == ArmorType.MAGIC) || (template.getItemType() == ArmorType.SIGIL) || (template.getItemType() == WeaponType.NONE))
					{
						continue;
					}
				}
				else
				{
					if ((template.getItemType() == WeaponType.CROSSBOW) || (template.getItemType() == WeaponType.RAPIER) || (template.getItemType() == WeaponType.ANCIENTSWORD))
					{
						continue;
					}
				}
				if (itemList.containsKey(paperdoll))
				{
					activeChar.sendPacket(Msg.THOSE_ITEMS_MAY_NOT_BE_TRIED_ON_SIMULTANEOUSLY);
					return;
				}
				itemList.put(paperdoll, itemId);
				totalPrice += ShopPreviewList.getWearPrice(template);
			}
			if (!activeChar.reduceAdena(totalPrice))
			{
				activeChar.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
				return;
			}
		}
		catch (ArithmeticException ae)
		{
			sendPacket(Msg.YOU_HAVE_EXCEEDED_THE_QUANTITY_THAT_CAN_BE_INPUTTED);
			return;
		}
		if (!itemList.isEmpty())
		{
			activeChar.sendPacket(new ShopPreviewInfo(itemList));
			ThreadPoolManager.getInstance().schedule(new RemoveWearItemsTask(activeChar), Config.WEAR_DELAY * 1000);
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class RemoveWearItemsTask extends RunnableImpl
	{
		/**
		 * Field _activeChar.
		 */
		private final Player _activeChar;
		
		/**
		 * Constructor for RemoveWearItemsTask.
		 * @param activeChar Player
		 */
		public RemoveWearItemsTask(Player activeChar)
		{
			_activeChar = activeChar;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_activeChar.sendPacket(Msg.TRYING_ON_MODE_HAS_ENDED);
			_activeChar.sendUserInfo();
		}
	}
}
