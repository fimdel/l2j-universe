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
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.dao.MailDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.serverpackets.ExShowSentPostList;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExCancelSentPost extends L2GameClientPacket
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
			activeChar.sendPacket(Msg.YOU_CANNOT_CANCEL_BECAUSE_THE_PRIVATE_SHOP_OR_WORKSHOP_IS_IN_PROGRESS);
			return;
		}
		if (activeChar.isInTrade())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_CANCEL_DURING_AN_EXCHANGE);
			return;
		}
		if (activeChar.getEnchantScroll() != null)
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_CANCEL_DURING_AN_ITEM_ENHANCEMENT_OR_ATTRIBUTE_ENHANCEMENT);
			return;
		}
		if (!activeChar.isInPeaceZone())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_CANCEL_IN_A_NON_PEACE_ZONE_LOCATION);
			return;
		}
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}
		Mail mail = MailDAO.getInstance().getSentMailByMailId(activeChar.getObjectId(), postId);
		if (mail != null)
		{
			if (mail.getAttachments().isEmpty())
			{
				activeChar.sendActionFailed();
				return;
			}
			activeChar.getInventory().writeLock();
			try
			{
				int slots = 0;
				long weight = 0;
				for (ItemInstance item : mail.getAttachments())
				{
					weight = SafeMath.addAndCheck(weight, SafeMath.mulAndCheck(item.getCount(), item.getTemplate().getWeight()));
					if (!item.getTemplate().isStackable() || (activeChar.getInventory().getItemByItemId(item.getItemId()) == null))
					{
						slots++;
					}
				}
				if (!activeChar.getInventory().validateWeight(weight))
				{
					sendPacket(Msg.YOU_COULD_NOT_CANCEL_RECEIPT_BECAUSE_YOUR_INVENTORY_IS_FULL);
					return;
				}
				if (!activeChar.getInventory().validateCapacity(slots))
				{
					sendPacket(Msg.YOU_COULD_NOT_CANCEL_RECEIPT_BECAUSE_YOUR_INVENTORY_IS_FULL);
					return;
				}
				ItemInstance[] items;
				synchronized (mail.getAttachments())
				{
					items = mail.getAttachments().toArray(new ItemInstance[mail.getAttachments().size()]);
					mail.getAttachments().clear();
				}
				for (ItemInstance item : items)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_ACQUIRED_S2_S1).addItemName(item.getItemId()).addNumber(item.getCount()));
					Log.LogItem(activeChar, Log.PostCancel, item);
					activeChar.getInventory().addItem(item);
				}
				mail.delete();
				activeChar.sendPacket(Msg.MAIL_SUCCESSFULLY_CANCELLED);
			}
			catch (ArithmeticException ae)
			{
			}
			finally
			{
				activeChar.getInventory().writeUnlock();
			}
		}
		activeChar.sendPacket(new ExShowSentPostList(activeChar));
	}
}
