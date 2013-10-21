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
package lineage2.gameserver.model.entity.events.actions;

import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.EventAction;
import lineage2.gameserver.model.entity.events.GlobalEvent;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.Say2;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SysString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SayAction implements EventAction
{
	/**
	 * Field _range.
	 */
	private final int _range;
	/**
	 * Field _chatType.
	 */
	private final ChatType _chatType;
	/**
	 * Field _how.
	 */
	private String _how;
	/**
	 * Field _text.
	 */
	private NpcString _text;
	/**
	 * Field _sysString.
	 */
	private SysString _sysString;
	/**
	 * Field _systemMsg.
	 */
	private SystemMsg _systemMsg;
	
	/**
	 * Constructor for SayAction.
	 * @param range int
	 * @param type ChatType
	 */
	protected SayAction(int range, ChatType type)
	{
		_range = range;
		_chatType = type;
	}
	
	/**
	 * Constructor for SayAction.
	 * @param range int
	 * @param type ChatType
	 * @param sysString SysString
	 * @param systemMsg SystemMsg
	 */
	public SayAction(int range, ChatType type, SysString sysString, SystemMsg systemMsg)
	{
		this(range, type);
		_sysString = sysString;
		_systemMsg = systemMsg;
	}
	
	/**
	 * Constructor for SayAction.
	 * @param range int
	 * @param type ChatType
	 * @param how String
	 * @param string NpcString
	 */
	public SayAction(int range, ChatType type, String how, NpcString string)
	{
		this(range, type);
		_text = string;
		_how = how;
	}
	
	/**
	 * Method call.
	 * @param event GlobalEvent
	 * @see lineage2.gameserver.model.entity.events.EventAction#call(GlobalEvent)
	 */
	@Override
	public void call(GlobalEvent event)
	{
		List<Player> players = event.broadcastPlayers(_range);
		for (Player player : players)
		{
			packet(player);
		}
	}
	
	/**
	 * Method packet.
	 * @param player Player
	 */
	private void packet(Player player)
	{
		if (player == null)
		{
			return;
		}
		L2GameServerPacket packet = null;
		if (_sysString != null)
		{
			packet = new Say2(0, _chatType, _sysString, _systemMsg);
		}
		else
		{
			packet = new Say2(0, _chatType, _how, _text);
		}
		player.sendPacket(packet);
	}
}
