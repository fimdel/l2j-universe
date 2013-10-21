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

import java.util.Map;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.actor.instances.player.Friend;
import lineage2.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestFriendList extends L2GameClientPacket
{
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
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
		activeChar.sendPacket(Msg._FRIENDS_LIST_);
		Map<Integer, Friend> _list = activeChar.getFriendList().getList();
		for (Map.Entry<Integer, Friend> entry : _list.entrySet())
		{
			Player friend = World.getPlayer(entry.getKey());
			if (friend != null)
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CURRENTLY_ONLINE).addName(friend));
			}
			else
			{
				activeChar.sendPacket(new SystemMessage(SystemMessage.S1_CURRENTLY_OFFLINE).addString(entry.getValue().getName()));
			}
		}
		activeChar.sendPacket(Msg.__EQUALS__);
	}
}
