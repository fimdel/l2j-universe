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
import lineage2.gameserver.dao.CharacterDAO;
import lineage2.gameserver.dao.CharacterPostFriendDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExConfirmAddingPostFriend;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

import org.napile.primitive.maps.IntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExAddPostFriendForPostBox extends L2GameClientPacket
{
	/**
	 * Field _name.
	 */
	private String _name;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_name = readS(Config.CNAME_MAXLEN);
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
		int targetObjectId = CharacterDAO.getInstance().getObjectIdByName(_name);
		if (targetObjectId == 0)
		{
			player.sendPacket(new ExConfirmAddingPostFriend(_name, ExConfirmAddingPostFriend.NAME_IS_NOT_EXISTS));
			return;
		}
		if (_name.equalsIgnoreCase(player.getName()))
		{
			player.sendPacket(new ExConfirmAddingPostFriend(_name, ExConfirmAddingPostFriend.NAME_IS_NOT_REGISTERED));
			return;
		}
		IntObjectMap<String> postFriend = player.getPostFriends();
		if (postFriend.size() >= Player.MAX_POST_FRIEND_SIZE)
		{
			player.sendPacket(new ExConfirmAddingPostFriend(_name, ExConfirmAddingPostFriend.LIST_IS_FULL));
			return;
		}
		if (postFriend.containsKey(targetObjectId))
		{
			player.sendPacket(new ExConfirmAddingPostFriend(_name, ExConfirmAddingPostFriend.ALREADY_ADDED));
			return;
		}
		CharacterPostFriendDAO.getInstance().insert(player, targetObjectId);
		postFriend.put(targetObjectId, CharacterDAO.getInstance().getNameByObjectId(targetObjectId));
		player.sendPacket(new SystemMessage2(SystemMsg.S1_WAS_SUCCESSFULLY_ADDED_TO_YOUR_CONTACT_LIST).addString(_name), new ExConfirmAddingPostFriend(_name, ExConfirmAddingPostFriend.SUCCESS));
	}
}
