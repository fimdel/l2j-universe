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

import lineage2.gameserver.dao.CharacterPostFriendDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

import org.apache.commons.lang3.StringUtils;
import org.napile.primitive.maps.IntObjectMap;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestExDeletePostFriendForPostBox extends L2GameClientPacket
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
		_name = readS();
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
		if (StringUtils.isEmpty(_name))
		{
			return;
		}
		int key = 0;
		IntObjectMap<String> postFriends = player.getPostFriends();
		for (IntObjectMap.Entry<String> entry : postFriends.entrySet())
		{
			if (entry.getValue().equalsIgnoreCase(_name))
			{
				key = entry.getKey();
			}
		}
		if (key == 0)
		{
			player.sendPacket(SystemMsg.THE_NAME_IS_NOT_CURRENTLY_REGISTERED);
			return;
		}
		player.getPostFriends().remove(key);
		CharacterPostFriendDAO.getInstance().delete(player, key);
		player.sendPacket(new SystemMessage2(SystemMsg.S1_WAS_SUCCESSFULLY_DELETED_FROM_YOUR_CONTACT_LIST).addString(_name));
	}
}
