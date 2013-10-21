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
package lineage2.gameserver.model.instances;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class ClanTraderInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for ClanTraderInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public ClanTraderInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
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
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		if (command.equalsIgnoreCase("crp"))
		{
			if ((player.getClan() != null) && (player.getClan().getLevel() > 4))
			{
				html.setFile("default/" + getNpcId() + "-2.htm");
			}
			else
			{
				html.setFile("default/" + getNpcId() + "-1.htm");
			}
			html.replace("%objectId%", String.valueOf(getObjectId()));
			player.sendPacket(html);
		}
		else if (command.startsWith("exchange"))
		{
			if (!player.isClanLeader())
			{
				html.setFile("default/" + getNpcId() + "-no.htm");
				html.replace("%objectId%", String.valueOf(getObjectId()));
				player.sendPacket(html);
				return;
			}
			int itemId = Integer.parseInt(command.substring(9).trim());
			int reputation = 0;
			long itemCount = 0;
			switch (itemId)
			{
				case 9911:
					reputation = 500;
					itemCount = 1;
					break;
				case 9910:
					reputation = 200;
					itemCount = 10;
					break;
				case 9912:
					reputation = 20;
					itemCount = 100;
					break;
			}
			if (player.getInventory().destroyItemByItemId(itemId, itemCount))
			{
				player.getClan().incReputation(reputation, false, "ClanTrader " + itemId + " from " + player.getName());
				player.getClan().broadcastToOnlineMembers(new PledgeShowInfoUpdate(player.getClan()));
				player.sendPacket(new SystemMessage(SystemMessage.YOUR_CLAN_HAS_ADDED_1S_POINTS_TO_ITS_CLAN_REPUTATION_SCORE).addNumber(reputation));
				html.setFile("default/" + getNpcId() + "-ExchangeSuccess.htm");
			}
			else
			{
				html.setFile("default/" + getNpcId() + "-ExchangeFailed.htm");
			}
			html.replace("%objectId%", String.valueOf(getObjectId()));
			player.sendPacket(html);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
}
