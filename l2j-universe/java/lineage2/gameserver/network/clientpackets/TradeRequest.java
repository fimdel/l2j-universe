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
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Request;
import lineage2.gameserver.model.Request.L2RequestType;
import lineage2.gameserver.network.serverpackets.SendTradeRequest;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TradeRequest extends L2GameClientPacket
{
	/**
	 * Field _objectId.
	 */
	private int _objectId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
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
		if (activeChar.isOutOfControl())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (!activeChar.getPlayerAccess().UseTrade)
		{
			activeChar.sendPacket(Msg.THIS_ACCOUNT_CANOT_TRADE_ITEMS);
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		if (activeChar.isFishing())
		{
			activeChar.sendPacket(Msg.YOU_CANNOT_DO_THAT_WHILE_FISHING);
			return;
		}
		if (activeChar.isInTrade())
		{
			activeChar.sendPacket(Msg.YOU_ARE_ALREADY_TRADING_WITH_SOMEONE);
			return;
		}
		if (activeChar.isProcessingRequest())
		{
			activeChar.sendPacket(Msg.WAITING_FOR_ANOTHER_REPLY);
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
		GameObject target = activeChar.getVisibleObject(_objectId);
		if ((target == null) || !target.isPlayer() || (target == activeChar))
		{
			activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return;
		}
		if (!activeChar.isInRangeZ(target, Creature.INTERACTION_DISTANCE))
		{
			activeChar.sendPacket(Msg.YOUR_TARGET_IS_OUT_OF_RANGE);
			return;
		}
		Player reciever = (Player) target;
		if (!reciever.getPlayerAccess().UseTrade)
		{
			activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return;
		}
		tradeBan = reciever.getVar("tradeBan");
		if ((tradeBan != null) && (tradeBan.equals("-1") || (Long.parseLong(tradeBan) >= System.currentTimeMillis())))
		{
			activeChar.sendPacket(SystemMsg.THAT_IS_AN_INCORRECT_TARGET);
			return;
		}
		if (reciever.isInBlockList(activeChar))
		{
			activeChar.sendPacket(Msg.YOU_HAVE_BEEN_BLOCKED_FROM_THE_CONTACT_YOU_SELECTED);
			return;
		}
		if (reciever.getTradeRefusal() || reciever.isBusy())
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1_IS_BUSY_PLEASE_TRY_AGAIN_LATER).addString(reciever.getName()));
			return;
		}
		new Request(L2RequestType.TRADE_REQUEST, activeChar, reciever).setTimeout(10000L);
		reciever.sendPacket(new SendTradeRequest(activeChar.getObjectId()));
		activeChar.sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_REQUESTED_A_TRADE_WITH_C1).addString(reciever.getName()));
	}
}
