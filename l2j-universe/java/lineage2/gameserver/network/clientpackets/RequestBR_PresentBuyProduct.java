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
import java.util.List;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.data.xml.holder.ProductHolder;
import lineage2.gameserver.database.mysql;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.ProductItem;
import lineage2.gameserver.model.ProductItemComponent;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.serverpackets.ExBR_GamePoint;
import lineage2.gameserver.network.serverpackets.ExBR_PresentBuyProductPacket;
import lineage2.gameserver.network.serverpackets.ExNoticePostArrived;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.utils.ItemFunctions;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestBR_PresentBuyProduct extends L2GameClientPacket
{
	/**
	 * Field count. Field productId.
	 */
	private int productId, count;
	/**
	 * Field message. Field topic. Field receiverName.
	 */
	private String receiverName, topic, message;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		productId = readD();
		count = readD();
		receiverName = readS();
		topic = readS();
		message = readS();
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
		if ((count > 99) || (count < 0))
		{
			return;
		}
		ProductItem product = ProductHolder.getInstance().getProduct(productId);
		if (product == null)
		{
			activeChar.sendPacket(new ExBR_PresentBuyProductPacket(ExBR_PresentBuyProductPacket.RESULT_WRONG_PRODUCT));
			return;
		}
		if ((System.currentTimeMillis() < product.getStartTimeSale()) || (System.currentTimeMillis() > product.getEndTimeSale()))
		{
			activeChar.sendPacket(new ExBR_PresentBuyProductPacket(ExBR_PresentBuyProductPacket.RESULT_SALE_PERIOD_ENDED));
			return;
		}
		int totalPoints = product.getPoints() * count;
		if (totalPoints < 0)
		{
			activeChar.sendPacket(new ExBR_PresentBuyProductPacket(ExBR_PresentBuyProductPacket.RESULT_WRONG_PRODUCT));
			return;
		}
		final long gamePointSize = activeChar.getPremiumPoints();
		if (totalPoints > gamePointSize)
		{
			activeChar.sendPacket(new ExBR_PresentBuyProductPacket(ExBR_PresentBuyProductPacket.RESULT_NOT_ENOUGH_POINTS));
			return;
		}
		int recieverId;
		Player target = World.getPlayer(receiverName);
		if (target != null)
		{
			recieverId = target.getObjectId();
			receiverName = target.getName();
			if (target.isInBlockList(activeChar))
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BLOCKED_YOU_YOU_CANNOT_SEND_MAIL_TO_S1_).addString(receiverName));
				return;
			}
		}
		else
		{
			recieverId = CharacterDAO.getInstance().getObjectIdByName(receiverName);
			if (recieverId > 0)
			{
				if (mysql.simple_get_int("target_Id", "character_blocklist", "obj_Id=" + recieverId + " AND target_Id=" + activeChar.getObjectId()) > 0)
				{
					activeChar.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BLOCKED_YOU_YOU_CANNOT_SEND_MAIL_TO_S1_).addString(receiverName));
					return;
				}
			}
		}
		if (recieverId == 0)
		{
			activeChar.sendPacket(Msg.WHEN_THE_RECIPIENT_DOESN_T_EXIST_OR_THE_CHARACTER_HAS_BEEN_DELETED_SENDING_MAIL_IS_NOT_POSSIBLE);
			return;
		}
		activeChar.reducePremiumPoints(totalPoints);
		List<ItemInstance> attachments = new ArrayList<>();
		for (ProductItemComponent comp : product.getComponents())
		{
			ItemInstance item = ItemFunctions.createItem(comp.getItemId());
			item.setCount(comp.getCount());
			item.setOwnerId(activeChar.getObjectId());
			item.setLocation(ItemInstance.ItemLocation.MAIL);
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
		Mail mail = new Mail();
		mail.setSenderId(activeChar.getObjectId());
		mail.setSenderName(activeChar.getName());
		mail.setReceiverId(recieverId);
		mail.setReceiverName(receiverName);
		mail.setTopic(topic);
		mail.setBody(message);
		mail.setPrice(0);
		mail.setUnread(true);
		mail.setType(Mail.SenderType.PRESENT);
		mail.setExpireTime((720 * 3600) + (int) (System.currentTimeMillis() / 1000L));
		for (ItemInstance item : attachments)
		{
			mail.addAttachment(item);
		}
		mail.save();
		activeChar.sendPacket(new ExBR_GamePoint(activeChar));
		activeChar.sendPacket(new ExBR_PresentBuyProductPacket(ExBR_PresentBuyProductPacket.RESULT_OK));
		activeChar.sendChanges();
		if (target != null)
		{
			target.sendPacket(ExNoticePostArrived.STATIC_TRUE);
			target.sendPacket(Msg.THE_MAIL_HAS_ARRIVED);
		}
	}
}
