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

import lineage2.gameserver.instancemanager.MatchingRoomManager;
import lineage2.gameserver.model.CommandChannel;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.matching.CCMatchingRoom;
import lineage2.gameserver.network.serverpackets.ListPartyWaiting;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RequestPartyMatchConfig extends L2GameClientPacket
{
	/**
	 * Field _page.
	 */
	private int _page;
	/**
	 * Field _region.
	 */
	private int _region;
	/**
	 * Field _allLevels.
	 */
	private int _allLevels;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_page = readD();
		_region = readD();
		_allLevels = readD();
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
		Party party = player.getParty();
		CommandChannel channel = party != null ? party.getCommandChannel() : null;
		if ((channel != null) && (channel.getChannelLeader() == player))
		{
			if (channel.getMatchingRoom() == null)
			{
				CCMatchingRoom room = new CCMatchingRoom(player, 1, player.getLevel(), 50, party.getLootDistribution(), player.getName());
				channel.setMatchingRoom(room);
			}
		}
		else if ((channel != null) && !channel.getParties().contains(party))
		{
			player.sendPacket(SystemMsg.THE_COMMAND_CHANNEL_AFFILIATED_PARTYS_PARTY_MEMBER_CANNOT_USE_THE_MATCHING_SCREEN);
		}
		else if ((party != null) && !party.isLeader(player))
		{
			player.sendPacket(SystemMsg.THE_LIST_OF_PARTY_ROOMS_CAN_ONLY_BE_VIEWED_BY_A_PERSON_WHO_IS_NOT_PART_OF_A_PARTY);
		}
		else
		{
			MatchingRoomManager.getInstance().addToWaitingList(player);
			player.sendPacket(new ListPartyWaiting(_region, _allLevels == 1, _page, player));
		}
	}
}
