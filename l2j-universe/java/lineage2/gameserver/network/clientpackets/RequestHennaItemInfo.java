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

import lineage2.gameserver.data.xml.holder.HennaHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.HennaItemInfo;
import lineage2.gameserver.templates.Henna;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestHennaItemInfo extends L2GameClientPacket
{
	/**
	 * Field _symbolId.
	 */
	private int _symbolId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_symbolId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		Henna henna = HennaHolder.getInstance().getHenna(_symbolId);
		if (henna != null)
		{
			player.sendPacket(new HennaItemInfo(henna, player));
		}
	}
}
