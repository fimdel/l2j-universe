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

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestDropItem extends L2GameClientPacket
{
	/**
	 * Field _objectId.
	 */
	private int _objectId;
	/**
	 * Field _count.
	 */
	private long _count;
	/**
	 * Field _loc.
	 */
	private Location _loc;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		_count = readQ();
		_loc = new Location(readD(), readD(), readD());
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
		if ((_count < 1) || _loc.isNull())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (activeChar.isActionsDisabled())
		{
			activeChar.sendActionFailed();
			return;
		}
		if (!Config.ALLOW_DISCARDITEM)
		{
			activeChar.sendMessage(new CustomMessage("lineage2.gameserver.clientpackets.RequestDropItem.Disallowed", activeChar));
			return;
		}
		if (activeChar.isInStoreMode())
		{
			activeChar.sendPacket(Msg.WHILE_OPERATING_A_PRIVATE_STORE_OR_WORKSHOP_YOU_CANNOT_DISCARD_DESTROY_OR_TRADE_AN_ITEM);
			return;
		}
		if (activeChar.isSitting() || activeChar.isDropDisabled())
		{
			activeChar.sendActionFailed();
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
		if (!activeChar.isInRangeSq(_loc, 22500) || (Math.abs(_loc.z - activeChar.getZ()) > 50))
		{
			activeChar.sendPacket(Msg.THAT_IS_TOO_FAR_FROM_YOU_TO_DISCARD);
			return;
		}
		ItemInstance item = activeChar.getInventory().getItemByObjectId(_objectId);
		if (item == null)
		{
			activeChar.sendActionFailed();
			return;
		}
		if (!item.canBeDropped(activeChar, false))
		{
			activeChar.sendPacket(Msg.THAT_ITEM_CANNOT_BE_DISCARDED);
			return;
		}
		item.getTemplate().getHandler().dropItem(activeChar, item, _count, _loc);
	}
}
