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
package lineage2.gameserver.instancemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PlayerMessageStack
{
	/**
	 * Field _instance.
	 */
	private static PlayerMessageStack _instance;
	/**
	 * Field _stack.
	 */
	private final Map<Integer, List<L2GameServerPacket>> _stack = new HashMap<>();
	
	/**
	 * Method getInstance.
	 * @return PlayerMessageStack
	 */
	public static PlayerMessageStack getInstance()
	{
		if (_instance == null)
		{
			_instance = new PlayerMessageStack();
		}
		return _instance;
	}
	
	/**
	 * Constructor for PlayerMessageStack.
	 */
	public PlayerMessageStack()
	{
	}
	
	/**
	 * Method mailto.
	 * @param char_obj_id int
	 * @param message L2GameServerPacket
	 */
	public void mailto(int char_obj_id, L2GameServerPacket message)
	{
		Player cha = GameObjectsStorage.getPlayer(char_obj_id);
		if (cha != null)
		{
			cha.sendPacket(message);
			return;
		}
		synchronized (_stack)
		{
			List<L2GameServerPacket> messages;
			if (_stack.containsKey(char_obj_id))
			{
				messages = _stack.remove(char_obj_id);
			}
			else
			{
				messages = new ArrayList<>();
			}
			messages.add(message);
			_stack.put(char_obj_id, messages);
		}
	}
	
	/**
	 * Method CheckMessages.
	 * @param cha Player
	 */
	public void CheckMessages(Player cha)
	{
		List<L2GameServerPacket> messages = null;
		synchronized (_stack)
		{
			if (!_stack.containsKey(cha.getObjectId()))
			{
				return;
			}
			messages = _stack.remove(cha.getObjectId());
		}
		if ((messages == null) || (messages.size() == 0))
		{
			return;
		}
		for (L2GameServerPacket message : messages)
		{
			cha.sendPacket(message);
		}
	}
}
