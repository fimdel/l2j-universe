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

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.math.SafeMath;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.dao.MailDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.ItemInstance.ItemLocation;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.serverpackets.ExShowReceivedPostList;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Log;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExReceivePost extends L2GameClientPacket
{
	/**
	 * Field postId.
	 */
	private int postId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		postId = readD();
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
			activeChar.sendPacket(Msg.YOU_CANNOT_RECEIVE_BECAUSE_THE_PRIVATE_SHOP_OR_WORKSHOP_IS_IN_PROGRESS);
			return;
		}
		if (activeChar.isInTrade())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_RECEIVE_DURING_AN_EXCHANGE);
			return;
		}
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}
		if (activeChar.getEnchantScroll() != null)
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_RECEIVE_DURING_AN_ITEM_ENHANCEMENT_OR_ATTRIBUTE_ENHANCEMENT);
			return;
		}
		Mail mail = MailDAO.getInstance().getReceivedMailByMailId(activeChar.getObjectId(), postId);
		if (mail != null)
		{
			activeChar.getInventory().writeLock();
			try
			{
				ItemInstance[] items;
				if ((mail.getAttachments().size() > 0) && !activeChar.isInPeaceZone())
				{
					activeChar.sendPacket(Msg.YOU_CANNOT_RECEIVE_IN_A_NON_PEACE_ZONE_LOCATION);
					return;
				}
				synchronized (mail.getAttachments())
				{
					if (mail.getAttachments().isEmpty())
					{
						return;
					}
					items = mail.getAttachments().toArray(new ItemInstance[mail.getAttachments().size()]);
					int slots = 0;
					long weight = 0;
					for (ItemInstance item : items)
					{
						weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(item.getCount(), item.getTemplate().getWeight()));
						if (!item.getTemplate().isStackable() || (activeChar.getInventory().getItemByItemId(item.getItemId()) == null))
						{
							slots++;
						}
					}
					if (!activeChar.getInventory().validateWeight(weight))
					{
						sendPacket(Msg.YOU_COULD_NOT_RECEIVE_BECAUSE_YOUR_INVENTORY_IS_FULL);
						return;
					}
					if (!activeChar.getInventory().validateCapacity(slots))
					{
						sendPacket(Msg.YOU_COULD_NOT_RECEIVE_BECAUSE_YOUR_INVENTORY_IS_FULL);
						return;
					}
					if (mail.getPrice() > 0)
					{
						if (!activeChar.reduceAdena(mail.getPrice(), true))
						{
							activeChar.sendPacket(Msg.YOU_CANNOT_RECEIVE_BECAUSE_YOU_DON_T_HAVE_ENOUGH_ADENA);
							return;
						}
						Player sender = World.getPlayer(mail.getSenderId());
						if (sender != null)
						{
							sender.addAdena(mail.getPrice(), true);
							sender.sendPacket(new SystemMessage(SystemMessage.S1_ACQUIRED_THE_ATTACHED_ITEM_TO_YOUR_MAIL).addName(activeChar));
						}
						else
						{
							int expireTime = (360 * 3600) + (int) (System.currentTimeMillis() / 1000L);
							Mail reply = mail.reply();
							reply.setExpireTime(expireTime);
							ItemInstance item = ItemFunctions.createItem(ItemTemplate.ITEM_ID_ADENA);
							item.setOwnerId(reply.getReceiverId());
							item.setCount(mail.getPrice());
							item.setLocation(ItemLocation.MAIL);
							item.save();
							Log.LogItem(activeChar, Log.PostSend, item);
							reply.addAttachment(item);
							reply.save();
						}
					}
					mail.getAttachments().clear();
				}
				mail.setJdbcState(JdbcEntityState.UPDATED);
				if (StringUtils.isEmpty(mail.getBody()))
				{
					mail.delete();
				}
				else
				{
					mail.update();
				}
				for (ItemInstance item : items)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_ACQUIRED_S2_S1).addItemName(item.getItemId()).addNumber(item.getCount()));
					Log.LogItem(activeChar, Log.PostRecieve, item);
					activeChar.getInventory().addItem(item);
				}
				activeChar.sendPacket(Msg.MAIL_SUCCESSFULLY_RECEIVED);
			}
			catch (ArithmeticException ae)
			{
			}
			finally
			{
				activeChar.getInventory().writeUnlock();
			}
		}
		activeChar.sendPacket(new ExShowReceivedPostList(activeChar));
	}
}
