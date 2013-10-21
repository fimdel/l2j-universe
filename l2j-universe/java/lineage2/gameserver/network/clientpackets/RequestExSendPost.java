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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.serverpackets.ExNoticePostArrived;
import lineage2.gameserver.network.serverpackets.ExReplyWritePost;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.Util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExSendPost extends L2GameClientPacket
{
	/**
	 * Field _messageType.
	 */
	private int _messageType;
	/**
	 * Field _body. Field _topic. Field _recieverName.
	 */
	private String _recieverName, _topic, _body;
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
	 * Field _price.
	 */
	private long _price;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_recieverName = readS(35);
		_messageType = readD();
		_topic = readS(Byte.MAX_VALUE);
		_body = readS(Short.MAX_VALUE);
		_count = readD();
		if ((((_count * 12) + 4) > _buf.remaining()) || (_count > Short.MAX_VALUE) || (_count < 1))
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
			if ((_itemQ[i] < 1) || (ArrayUtils.indexOf(_items, _items[i]) < i))
			{
				_count = 0;
				return;
			}
		}
		_price = readQ();
		if (_price < 0)
		{
			_count = 0;
			_price = 0;
		}
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
		if (activeChar.isGM() && _recieverName.equalsIgnoreCase("ONLINE_ALL"))
		{
			Map<Integer, Long> map = new HashMap<Integer, Long>();
			if ((_items != null) && (_items.length > 0))
			{
				for (int i = 0; i < _items.length; i++)
				{
					ItemInstance item = activeChar.getInventory().getItemByObjectId(_items[i]);
					map.put(item.getItemId(), _itemQ[i]);
				}
			}
			for (Player p : GameObjectsStorage.getAllPlayersForIterate())
			{
				if ((p != null) && p.isOnline())
				{
					Functions.sendSystemMail(p, _topic, _body, map);
				}
			}
			activeChar.sendPacket(ExReplyWritePost.STATIC_TRUE);
			activeChar.sendPacket(Msg.MAIL_SUCCESSFULLY_SENT);
			return;
		}
		if (!Config.ALLOW_MAIL)
		{
			activeChar.sendMessage(new CustomMessage("mail.Disabled", activeChar));
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_FORWARD_BECAUSE_THE_PRIVATE_SHOP_OR_WORKSHOP_IS_IN_PROGRESS);
			return;
		}
		if (activeChar.isInTrade())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_FORWARD_DURING_AN_EXCHANGE);
			return;
		}
		if (activeChar.getEnchantScroll() != null)
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_FORWARD_DURING_AN_ITEM_ENHANCEMENT_OR_ATTRIBUTE_ENHANCEMENT);
			return;
		}
		if (_body.length() == 0)
		{
			activeChar.sendMessage("Enter the body message!");
			return;
		}
		if (activeChar.getName().equalsIgnoreCase(_recieverName))
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_SEND_A_MAIL_TO_YOURSELF);
			return;
		}
		if ((_count > 0) && !activeChar.isInPeaceZone())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_FORWARD_IN_A_NON_PEACE_ZONE_LOCATION);
			return;
		}
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}
		if (!activeChar.antiFlood.canMail())
		{
			activeChar.sendPacket(Msg.THE_PREVIOUS_MAIL_WAS_FORWARDED_LESS_THAN_1_MINUTE_AGO_AND_THIS_CANNOT_BE_FORWARDED);
			return;
		}
		if (_price > 0)
		{
			if (!activeChar.getPlayerAccess().UseTrade)
			{
				activeChar.sendPacket(Msg.THIS_ACCOUNT_CANOT_TRADE_ITEMS);
				activeChar.sendActionFailed();
				return;
			}
			String tradeBan = activeChar.getVar("tradeBan");
			if ((tradeBan != null) && (tradeBan.equals("-1") || (Long.parseLong(tradeBan) >= System.currentTimeMillis())))
			{
				if (tradeBan.equals("-1"))
				{
					activeChar.sendMessage(new CustomMessage("common.TradeBannedPermanently", activeChar));
				}
				else
				{
					activeChar.sendMessage(new CustomMessage("common.TradeBanned", activeChar).addString(Util.formatTime((int) ((Long.parseLong(tradeBan) / 1000L) - (System.currentTimeMillis() / 1000L)))));
				}
				return;
			}
		}
		if (activeChar.isInBlockList(_recieverName))
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_BLOCKED_C1).addString(_recieverName));
			return;
		}
		int recieverId;
		Player target = World.getPlayer(_recieverName);
		if (target != null)
		{
			recieverId = target.getObjectId();
			_recieverName = target.getName();
			if (target.isInBlockList(activeChar))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BLOCKED_YOU_YOU_CANNOT_SEND_MAIL_TO_S1_).addString(_recieverName));
				return;
			}
		}
		else
		{
			recieverId = CharacterDAO.getInstance().getObjectIdByName(_recieverName);
			if (recieverId > 0)
			{
				if (mysql.simple_get_int("target_Id", "character_blocklist", "obj_Id=" + recieverId + " AND target_Id=" + activeChar.getObjectId()) > 0)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BLOCKED_YOU_YOU_CANNOT_SEND_MAIL_TO_S1_).addString(_recieverName));
					return;
				}
			}
		}
		if (recieverId == 0)
		{
			activeChar.sendPacket(Msg.WHEN_THE_RECIPIENT_DOESN_T_EXIST_OR_THE_CHARACTER_HAS_BEEN_DELETED_SENDING_MAIL_IS_NOT_POSSIBLE);
			return;
		}
		int expireTime = ((_messageType == 1 ? 12 : 360) * 3600) + (int) (System.currentTimeMillis() / 1000L);
		if (_count > 8)
		{
			activeChar.sendPacket(SystemMsg.INCORRECT_ITEM_COUNT);
			return;
		}
		long serviceCost = 100 + (_count * 1000);
		List<ItemInstance> attachments = new ArrayList<>();
		activeChar.getInventory().writeLock();
		try
		{
			if (activeChar.getAdena() < serviceCost)
			{
				activeChar.sendPacket(Msg.YOU_CANNOT_FORWARD_BECAUSE_YOU_DON_T_HAVE_ENOUGH_ADENA);
				return;
			}
			if (_count > 0)
			{
				for (int i = 0; i < _count; i++)
				{
					ItemInstance item = activeChar.getInventory().getItemByObjectId(_items[i]);
					if ((item == null) || (item.getCount() < _itemQ[i]) || ((item.getItemId() == ItemTemplate.ITEM_ID_ADENA) && (item.getCount() < (_itemQ[i] + serviceCost))) || !item.canBeTraded(activeChar))
					{
						activeChar.sendPacket(Msg.THE_ITEM_THAT_YOU_RE_TRYING_TO_SEND_CANNOT_BE_FORWARDED_BECAUSE_IT_ISN_T_PROPER);
						return;
					}
				}
			}
			if (!activeChar.reduceAdena(serviceCost, true))
			{
				activeChar.sendPacket(Msg.YOU_CANNOT_FORWARD_BECAUSE_YOU_DON_T_HAVE_ENOUGH_ADENA);
				return;
			}
			if (_count > 0)
			{
				for (int i = 0; i < _count; i++)
				{
					ItemInstance item = activeChar.getInventory().removeItemByObjectId(_items[i], _itemQ[i]);
					Log.LogItem(activeChar, Log.PostSend, item);
					item.setOwnerId(activeChar.getObjectId());
					item.setLocation(ItemLocation.MAIL);
					if (item.getJdbcState().isSavable())
					{
						item.save();
					}
					else
					{
						item.setJdbcState(JdbcEntityState.UPDATED);
						item.update();
					}
					attachments.add(item);
				}
			}
		}
		finally
		{
			activeChar.getInventory().writeUnlock();
		}
		Mail mail = new Mail();
		mail.setSenderId(activeChar.getObjectId());
		mail.setSenderName(activeChar.getName());
		mail.setReceiverId(recieverId);
		mail.setReceiverName(_recieverName);
		mail.setTopic(_topic);
		mail.setBody(_body);
		mail.setPrice(_messageType > 0 ? _price : 0);
		mail.setUnread(true);
		mail.setType(Mail.SenderType.NORMAL);
		mail.setExpireTime(expireTime);
		for (ItemInstance item : attachments)
		{
			mail.addAttachment(item);
		}
		mail.save();
		activeChar.sendPacket(ExReplyWritePost.STATIC_TRUE);
		activeChar.sendPacket(Msg.MAIL_SUCCESSFULLY_SENT);
		if (target != null)
		{
			target.sendPacket(ExNoticePostArrived.STATIC_TRUE);
			target.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
		}
	}
}
