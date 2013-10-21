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

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.Privilege;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.HtmlUtils;
import lineage2.gameserver.utils.ReflectionUtils;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AuctionedDoormanInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _doors.
	 */
	private final int[] _doors;
	/**
	 * Field _elite.
	 */
	private final boolean _elite;
	
	/**
	 * Constructor for AuctionedDoormanInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public AuctionedDoormanInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		_doors = template.getAIParams().getIntegerArray("doors", ArrayUtils.EMPTY_INT_ARRAY);
		_elite = template.getAIParams().getBool("elite", false);
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
		ClanHall clanHall = getClanHall();
		if (command.equalsIgnoreCase("openDoors"))
		{
			if (player.hasPrivilege(Privilege.CH_ENTER_EXIT) && (player.getClan().getHasHideout() == clanHall.getId()))
			{
				for (int d : _doors)
				{
					ReflectionUtils.getDoor(d).openMe();
				}
				showChatWindow(player, "residence2/clanhall/agitafterdooropen.htm");
			}
			else
			{
				showChatWindow(player, "residence2/clanhall/noAuthority.htm");
			}
		}
		else if (command.equalsIgnoreCase("closeDoors"))
		{
			if (player.hasPrivilege(Privilege.CH_ENTER_EXIT) && (player.getClan().getHasHideout() == clanHall.getId()))
			{
				for (int d : _doors)
				{
					ReflectionUtils.getDoor(d).closeMe(player, true);
				}
				showChatWindow(player, "residence2/clanhall/agitafterdoorclose.htm");
			}
			else
			{
				showChatWindow(player, "residence2/clanhall/noAuthority.htm");
			}
		}
		else if (command.equalsIgnoreCase("banish"))
		{
			if (player.hasPrivilege(Privilege.CH_DISMISS))
			{
				clanHall.banishForeigner();
				showChatWindow(player, "residence2/clanhall/agitafterbanish.htm");
			}
			else
			{
				showChatWindow(player, "residence2/clanhall/noAuthority.htm");
			}
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
	 * @param arg Object[]
	 */
	@Override
	public void showChatWindow(Player player, int val, Object... arg)
	{
		ClanHall clanHall = getClanHall();
		if (clanHall != null)
		{
			Clan playerClan = player.getClan();
			if ((playerClan != null) && (playerClan.getHasHideout() == clanHall.getId()))
			{
				showChatWindow(player, _elite ? "residence2/clanhall/WyvernAgitJanitorHi.htm" : "residence2/clanhall/AgitJanitorHi.htm", "%owner%", playerClan.getName());
			}
			else
			{
				if ((playerClan != null) && (playerClan.getCastle() > 0))
				{
					Castle castle = ResidenceHolder.getInstance().getResidence(playerClan.getCastle());
					NpcHtmlMessage html = new NpcHtmlMessage(player, this);
					html.setFile("merchant/territorystatus.htm");
					html.replace("%npcname%", getName());
					html.replace("%castlename%", HtmlUtils.htmlResidenceName(castle.getId()));
					html.replace("%taxpercent%", String.valueOf(castle.getTaxPercent()));
					html.replace("%clanname%", playerClan.getName());
					html.replace("%clanleadername%", playerClan.getLeaderName());
					player.sendPacket(html);
				}
				else
				{
					showChatWindow(player, "residence2/clanhall/noAgitInfo.htm");
				}
			}
		}
		else
		{
			showChatWindow(player, "residence2/clanhall/noAgitInfo.htm");
		}
	}
}
