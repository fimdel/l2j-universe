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

import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.ClanHallMiniGameEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.CMGSiegeClanObject;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.ClanHall;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.TimeUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RainbowMessengerInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field ITEM_ID. (value is 8034)
	 */
	public static final int ITEM_ID = 8034;
	
	/**
	 * Constructor for RainbowMessengerInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public RainbowMessengerInstance(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * Method onBypassFeedback.
	 * @param player Player
	 * @param command String
	 */
	@Override
	public void onBypassFeedback(final Player player, final String command)
	{
		if (!canBypassCheck(player, this))
		{
			return;
		}
		ClanHall clanHall = getClanHall();
		ClanHallMiniGameEvent miniGameEvent = clanHall.getSiegeEvent();
		if (command.equalsIgnoreCase("register"))
		{
			if (miniGameEvent.isRegistrationOver())
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti014.htm");
				return;
			}
			Clan clan = player.getClan();
			if ((clan == null) || (clan.getLevel() < 3) || (clan.getAllSize() <= 5))
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti011.htm");
				return;
			}
			if (clan.getLeaderId() != player.getObjectId())
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti010.htm");
				return;
			}
			if (clan.getHasHideout() > 0)
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti012.htm");
				return;
			}
			if (miniGameEvent.getSiegeClan(SiegeEvent.ATTACKERS, clan) != null)
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti013.htm");
				return;
			}
			long count = player.getInventory().getCountOf(ITEM_ID);
			if (count == 0)
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti008.htm");
			}
			else
			{
				if (!player.consumeItem(ITEM_ID, count))
				{
					return;
				}
				CMGSiegeClanObject siegeClanObject = new CMGSiegeClanObject(SiegeEvent.ATTACKERS, clan, count);
				miniGameEvent.addObject(SiegeEvent.ATTACKERS, siegeClanObject);
				SiegeClanDAO.getInstance().insert(clanHall, siegeClanObject);
				showChatWindow(player, "residence2/clanhall/messenger_yetti009.htm");
			}
		}
		else if (command.equalsIgnoreCase("cancel"))
		{
			if (miniGameEvent.isRegistrationOver())
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti017.htm");
				return;
			}
			Clan clan = player.getClan();
			if ((clan == null) || (clan.getLevel() < 3))
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti011.htm");
				return;
			}
			if (clan.getLeaderId() != player.getObjectId())
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti010.htm");
				return;
			}
			SiegeClanObject siegeClanObject = miniGameEvent.getSiegeClan(SiegeEvent.ATTACKERS, clan);
			if (siegeClanObject == null)
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti016.htm");
			}
			else
			{
				miniGameEvent.removeObject(SiegeEvent.ATTACKERS, siegeClanObject);
				SiegeClanDAO.getInstance().delete(clanHall, siegeClanObject);
				ItemFunctions.addItem(player, ITEM_ID, siegeClanObject.getParam() / 2L, true);
				showChatWindow(player, "residence2/clanhall/messenger_yetti005.htm");
			}
		}
		else if (command.equalsIgnoreCase("refund"))
		{
			if (miniGameEvent.isRegistrationOver())
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti010.htm");
				return;
			}
			Clan clan = player.getClan();
			if ((clan == null) || (clan.getLevel() < 3))
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti011.htm");
				return;
			}
			if (clan.getLeaderId() != player.getObjectId())
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti010.htm");
				return;
			}
			SiegeClanObject siegeClanObject = miniGameEvent.getSiegeClan(ClanHallMiniGameEvent.REFUND, clan);
			if (siegeClanObject == null)
			{
				showChatWindow(player, "residence2/clanhall/messenger_yetti020.htm");
			}
			else
			{
				miniGameEvent.removeObject(ClanHallMiniGameEvent.REFUND, siegeClanObject);
				SiegeClanDAO.getInstance().delete(clanHall, siegeClanObject);
				ItemFunctions.addItem(player, ITEM_ID, siegeClanObject.getParam(), true);
				showChatWindow(player, "residence2/clanhall/messenger_yetti019.htm");
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
		Clan clan = clanHall.getOwner();
		NpcHtmlMessage msg = new NpcHtmlMessage(player, this);
		if (clan != null)
		{
			msg.setFile("residence2/clanhall/messenger_yetti001.htm");
			msg.replace("%owner_name%", clan.getName());
		}
		else
		{
			msg.setFile("residence2/clanhall/messenger_yetti001a.htm");
		}
		msg.replace("%siege_date%", TimeUtils.toSimpleFormat(clanHall.getSiegeDate()));
		player.sendPacket(msg);
	}
}
