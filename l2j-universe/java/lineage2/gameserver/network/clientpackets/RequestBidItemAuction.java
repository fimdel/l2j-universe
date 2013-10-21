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

import lineage2.gameserver.instancemanager.itemauction.ItemAuction;
import lineage2.gameserver.instancemanager.itemauction.ItemAuctionInstance;
import lineage2.gameserver.instancemanager.itemauction.ItemAuctionManager;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestBidItemAuction extends L2GameClientPacket
{
	/**
	 * Field _instanceId.
	 */
	private int _instanceId;
	/**
	 * Field _bid.
	 */
	private long _bid;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected final void readImpl()
	{
		_instanceId = readD();
		_bid = readQ();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected final void runImpl()
	{
		final Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		ItemInstance adena = activeChar.getInventory().getItemByItemId(57);
		if ((_bid < 0) || (_bid > adena.getCount()))
		{
			return;
		}
		final ItemAuctionInstance instance = ItemAuctionManager.getInstance().getManagerInstance(_instanceId);
		NpcInstance broker = activeChar.getLastNpc();
		if ((broker == null) || (broker.getNpcId() != _instanceId) || (activeChar.getDistance(broker.getX(), broker.getY()) > Creature.INTERACTION_DISTANCE))
		{
			return;
		}
		if (instance != null)
		{
			final ItemAuction auction = instance.getCurrentAuction();
			if (auction != null)
			{
				auction.registerBid(activeChar, _bid);
			}
		}
	}
}
