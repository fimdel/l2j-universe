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

import lineage2.gameserver.instancemanager.commission.CommissionShopManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestCommissionDelete extends L2GameClientPacket
{
	/**
	 * Field _bidId.
	 */
	public long _bidId;
	/**
	 * Field itemObjId.
	 */
	public int itemObjId;
	/**
	 * Field exItemType.
	 */
	public int exItemType;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_bidId = readQ();
		itemObjId = readD();
		exItemType = readD();
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
		CommissionShopManager.getInstance().returnBuyItem(activeChar, _bidId, itemObjId);
		activeChar.sendPacket(SystemMsg.CANCELLATION_OF_SALE_FOR_THE_ITEM_IS_SUCCESSFUL);
	}
}
