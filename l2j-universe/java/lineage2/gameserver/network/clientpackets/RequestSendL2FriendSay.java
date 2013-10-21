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
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.L2FriendSay;
import lineage2.gameserver.utils.Log;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestSendL2FriendSay extends L2GameClientPacket
{
	/**
	 * Field _message.
	 */
	private String _message;
	/**
	 * Field _reciever.
	 */
	private String _reciever;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_message = readS(2048);
		_reciever = readS(16);
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
		if (activeChar.getNoChannel() != 0)
		{
			if ((activeChar.getNoChannelRemained() > 0) || (activeChar.getNoChannel() < 0))
			{
				activeChar.sendPacket(Msg.CHATTING_IS_CURRENTLY_PROHIBITED_IF_YOU_TRY_TO_CHAT_BEFORE_THE_PROHIBITION_IS_REMOVED_THE_PROHIBITION_TIME_WILL_BECOME_EVEN_LONGER);
				return;
			}
			activeChar.updateNoChannel(0);
		}
		Player targetPlayer = World.getPlayer(_reciever);
		if (targetPlayer == null)
		{
			activeChar.sendPacket(Msg.THAT_PLAYER_IS_NOT_ONLINE);
			return;
		}
		if (targetPlayer.isBlockAll())
		{
			activeChar.sendPacket(Msg.THE_PERSON_IS_IN_A_MESSAGE_REFUSAL_MODE);
			return;
		}
		if (!activeChar.getFriendList().getList().containsKey(targetPlayer.getObjectId()))
		{
			return;
		}
		Log.LogChat("FRIENDTELL", activeChar.getName(), _reciever, _message);
		L2FriendSay frm = new L2FriendSay(activeChar.getName(), _reciever, _message);
		targetPlayer.sendPacket(frm);
	}
}
