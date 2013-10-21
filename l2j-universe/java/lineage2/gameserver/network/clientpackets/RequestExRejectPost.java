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

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.dao.MailDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.mail.Mail;
import lineage2.gameserver.network.serverpackets.ExNoticePostArrived;
import lineage2.gameserver.network.serverpackets.ExShowReceivedPostList;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExRejectPost extends L2GameClientPacket
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
		Mail mail = MailDAO.getInstance().getReceivedMailByMailId(activeChar.getObjectId(), postId);
		if (mail != null)
		{
			if ((mail.getType() != Mail.SenderType.NORMAL) || mail.getAttachments().isEmpty())
			{
				activeChar.sendActionFailed();
				return;
			}
			int expireTime = (360 * 3600) + (int) (System.currentTimeMillis() / 1000L);
			Mail reject = mail.reject();
			mail.delete();
			reject.setExpireTime(expireTime);
			reject.save();
			Player sender = World.getPlayer(reject.getReceiverId());
			if (sender != null)
			{
				sender.sendPacket(ExNoticePostArrived.STATIC_TRUE);
			}
		}
		activeChar.sendPacket(new ExShowReceivedPostList(activeChar));
	}
}
