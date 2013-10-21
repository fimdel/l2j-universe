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

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExBR_ProductInfo;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExBR_ProductInfo extends L2GameClientPacket
{
	/**
	 * Field _productId.
	 */
	private int _productId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_productId = readD();
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
		activeChar.sendPacket(new ExBR_ProductInfo(_productId));
	}
}
