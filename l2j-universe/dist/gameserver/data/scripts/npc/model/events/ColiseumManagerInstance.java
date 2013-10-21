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
package npc.model.events;

import java.util.List;

import lineage2.commons.collections.CollectionUtils;
import lineage2.gameserver.data.xml.holder.EventHolder;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.EventType;
import lineage2.gameserver.model.entity.events.impl.UndergroundColiseumEvent;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ColiseumManagerInstance extends ColiseumHelperInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _startHtm.
	 */
	private final String _startHtm;
	/**
	 * Field _coliseumId.
	 */
	private final int _coliseumId;
	
	/**
	 * Constructor for ColiseumManagerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ColiseumManagerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		
		_startHtm = getParameter("start_htm", StringUtils.EMPTY);
		_coliseumId = getParameter("coliseum_id", 0);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		
		UndergroundColiseumEvent coliseumEvent = EventHolder.getInstance().getEvent(EventType.MAIN_EVENT, _coliseumId);
		
		if (command.equals("register"))
		{
			Party party = player.getParty();
			if (party == null)
			{
				showChatWindow(player, "events/kerthang_manager008.htm");
			}
			else if (party.getPartyLeader() != player)
			{
				showChatWindow(player, "events/kerthang_manager004.htm");
			}
			else
			{
				for (Player $player : party)
				{
					if (($player.getLevel() < coliseumEvent.getMinLevel()) || ($player.getLevel() > coliseumEvent.getMaxLevel()))
					{
						showChatWindow(player, "events/kerthang_manager011.htm", "%name%", $player.getName());
						return;
					}
				}
			}
		}
		else if (command.equals("viewTeams"))
		{
			
			List<Player> reg = coliseumEvent.getRegisteredPlayers();
			
			NpcHtmlMessage msg = new NpcHtmlMessage(player, this);
			msg.setFile("events/kerthang_manager003.htm");
			for (int i = 0; i < 5; i++)
			{
				Player $player = CollectionUtils.safeGet(reg, i);
				
				msg.replace("%team" + i + "%", $player == null ? StringUtils.EMPTY : $player.getName());
			}
			
			player.sendPacket(msg);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param ar Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... ar)
	{
		showChatWindow(player, _startHtm);
	}
}
