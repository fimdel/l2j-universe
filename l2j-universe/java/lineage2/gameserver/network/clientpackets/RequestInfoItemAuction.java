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
import lineage2.gameserver.network.serverpackets.ExItemAuctionInfo;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestInfoItemAuction extends L2GameClientPacket
{
	/**
	 * Field _instanceId.
	 */
	private int _instanceId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected final void readImpl()
	{
		_instanceId = readD();
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
		activeChar.getAndSetLastItemAuctionRequest();
		final ItemAuctionInstance instance = ItemAuctionManager.getInstance().getManagerInstance(_instanceId);
		if (instance == null)
		{
			return;
		}
		final ItemAuction auction = instance.getCurrentAuction();
		NpcInstance broker = activeChar.getLastNpc();
		if ((auction == null) || (broker == null) || (broker.getNpcId() != _instanceId) || (activeChar.getDistance(broker.getX(), broker.getY()) > Creature.INTERACTION_DISTANCE))
		{
			return;
		}
		activeChar.sendPacket(new ExItemAuctionInfo(true, auction, instance.getNextAuction()));
	}
}
