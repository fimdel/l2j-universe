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
package npc.model.residences.clanhall;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.ClanHallSiegeEvent;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.CastleSiegeInfo;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MessengerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _siegeDialog.
	 */
	private final String _siegeDialog;
	/**
	 * Field _ownerDialog.
	 */
	private final String _ownerDialog;
	
	/**
	 * Constructor for MessengerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public MessengerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		_siegeDialog = template.getAIParams().getString("siege_dialog");
		_ownerDialog = template.getAIParams().getString("owner_dialog");
	}
	
	/**
	 * Method showChatWindow.
	 * @param player Player
	 * @param val int
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		ClanHall clanHall = getClanHall();
		ClanHallSiegeEvent siegeEvent = clanHall.getSiegeEvent();
		if ((clanHall.getOwner() != null) && (clanHall.getOwner() == player.getClan()))
		{
			showChatWindow(player, _ownerDialog);
		}
		else if (siegeEvent.isInProgress())
		{
			showChatWindow(player, _siegeDialog);
		}
		else
		{
			player.sendPacket(new CastleSiegeInfo(clanHall, player));
		}
	}
}
