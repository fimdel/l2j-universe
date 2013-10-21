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
package lineage2.gameserver.model.actor.instances.player;

import java.util.Collections;
import java.util.Map;

import lineage2.gameserver.dao.CharacterFriendDAO;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.network.serverpackets.L2Friend;
import lineage2.gameserver.network.serverpackets.L2FriendStatus;
import lineage2.gameserver.network.serverpackets.SystemMessage;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FriendList
{
	/**
	 * Field _friendList.
	 */
	private Map<Integer, Friend> _friendList = Collections.emptyMap();
	/**
	 * Field _owner.
	 */
	private final Player _owner;
	
	/**
	 * Constructor for FriendList.
	 * @param owner Player
	 */
	public FriendList(Player owner)
	{
		_owner = owner;
	}
	
	/**
	 * Method restore.
	 */
	public void restore()
	{
		_friendList = CharacterFriendDAO.getInstance().select(_owner);
	}
	
	/**
	 * Method removeFriend.
	 * @param name String
	 */
	public void removeFriend(String name)
	{
		if (StringUtils.isEmpty(name))
		{
			return;
		}
		int objectId = removeFriend0(name);
		if (objectId > 0)
		{
			Player friendChar = World.getPlayer(objectId);
			_owner.sendPacket(new SystemMessage(SystemMessage.S1_HAS_BEEN_REMOVED_FROM_YOUR_FRIEND_LIST).addString(name), new L2Friend(name, false, friendChar != null, objectId));
			if (friendChar != null)
			{
				friendChar.sendPacket(new SystemMessage(SystemMessage.S1__HAS_BEEN_DELETED_FROM_YOUR_FRIENDS_LIST).addString(_owner.getName()), new L2Friend(_owner, false));
			}
		}
		else
		{
			_owner.sendPacket(new SystemMessage(SystemMessage.S1_IS_NOT_ON_YOUR_FRIEND_LIST).addString(name));
		}
	}
	
	/**
	 * Method notifyFriends.
	 * @param login boolean
	 */
	public void notifyFriends(boolean login)
	{
		for (Friend friend : _friendList.values())
		{
			Player friendPlayer = GameObjectsStorage.getPlayer(friend.getObjectId());
			if (friendPlayer != null)
			{
				Friend thisFriend = friendPlayer.getFriendList().getList().get(_owner.getObjectId());
				if (thisFriend == null)
				{
					continue;
				}
				thisFriend.update(_owner, login);
				if (login)
				{
					friendPlayer.sendPacket(new SystemMessage(SystemMessage.S1_FRIEND_HAS_LOGGED_IN).addString(_owner.getName()));
				}
				friendPlayer.sendPacket(new L2FriendStatus(_owner, login));
				friend.update(friendPlayer, login);
			}
		}
	}
	
	/**
	 * Method addFriend.
	 * @param friendPlayer Player
	 */
	public void addFriend(Player friendPlayer)
	{
		_friendList.put(friendPlayer.getObjectId(), new Friend(friendPlayer));
		CharacterFriendDAO.getInstance().insert(_owner, friendPlayer);
	}
	
	/**
	 * Method removeFriend0.
	 * @param name String
	 * @return int
	 */
	private int removeFriend0(String name)
	{
		if (name == null)
		{
			return 0;
		}
		Integer objectId = 0;
		for (Map.Entry<Integer, Friend> entry : _friendList.entrySet())
		{
			if (name.equalsIgnoreCase(entry.getValue().getName()))
			{
				objectId = entry.getKey();
				break;
			}
		}
		if (objectId > 0)
		{
			_friendList.remove(objectId);
			CharacterFriendDAO.getInstance().delete(_owner, objectId);
			return objectId;
		}
		return 0;
	}
	
	/**
	 * Method getList.
	 * @return Map<Integer,Friend>
	 */
	public Map<Integer, Friend> getList()
	{
		return _friendList;
	}
	
	/**
	 * Method toString.
	 * @return String
	 */
	@Override
	public String toString()
	{
		return "FriendList[owner=" + _owner.getName() + "]";
	}
}
