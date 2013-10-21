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
package npc.model.residences.fortress.peace;

import lineage2.gameserver.dao.SiegeClanDAO;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.FortressSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.SiegeClanObject;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.model.pledge.Privilege;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SuspiciousMerchantInstance extends NpcInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for SuspiciousMerchantInstance.
	 * @param objectID int
	 * @param template NpcTemplate
	 */
	public SuspiciousMerchantInstance(int objectID, NpcTemplate template)
	{
		super(objectID, template);
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
		
		Fortress fortress = getFortress();
		FortressSiegeEvent siegeEvent = fortress.getSiegeEvent();
		
		if (command.equalsIgnoreCase("register"))
		{
			Clan clan = player.getClan();
			if (clan == null)
			{
				showChatWindow(player, "residence2/fortress/fortress_ordery002.htm");
				return;
			}
			
			if (clan.getHasFortress() == fortress.getId())
			{
				showChatWindow(player, "residence2/fortress/fortress_ordery014.htm", "%clan_name%", clan.getName());
				return;
			}
			
			if (!player.hasPrivilege(Privilege.CS_FS_SIEGE_WAR))
			{
				showChatWindow(player, "residence2/fortress/fortress_ordery012.htm");
				return;
			}
			
			if (clan.getCastle() > 0)
			{
				Castle relatedCastle = null;
				for (Castle castle : fortress.getRelatedCastles())
				{
					if (castle.getId() == clan.getCastle())
					{
						relatedCastle = castle;
					}
				}
				
				if (relatedCastle != null)
				{
					if (fortress.getContractState() == Fortress.CONTRACT_WITH_CASTLE)
					{
						showChatWindow(player, "residence2/fortress/fortress_ordery022.htm");
						return;
					}
					
					if (relatedCastle.getSiegeEvent().isRegistrationOver())
					{
						showChatWindow(player, "residence2/fortress/fortress_ordery022.htm");
						return;
					}
				}
				else
				{
					showChatWindow(player, "residence2/fortress/fortress_ordery021.htm");
					return;
				}
			}
			
			int attackersSize = siegeEvent.getObjects(SiegeEvent.ATTACKERS).size();
			
			if (attackersSize == 0)
			{
				if (!player.consumeItem(ItemTemplate.ITEM_ID_ADENA, 250000L))
				{
					showChatWindow(player, "residence2/fortress/fortress_ordery003.htm");
					return;
				}
			}
			
			SiegeClanObject siegeClan = siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, clan);
			if (siegeClan != null)
			{
				showChatWindow(player, "residence2/fortress/fortress_ordery007.htm");
				return;
			}
			
			for (Fortress $ : ResidenceHolder.getInstance().getResidenceList(Fortress.class))
			{
				if ($.getSiegeEvent().getSiegeClan(SiegeEvent.ATTACKERS, clan) != null)
				{
					showChatWindow(player, "residence2/fortress/fortress_ordery006.htm");
					return;
				}
			}
			
			if ((clan.getHasFortress() > 0) && (fortress.getSiegeDate().getTimeInMillis() > 0))
			{
				showChatWindow(player, "residence2/fortress/fortress_ordery006.htm");
				return;
			}
			
			siegeClan = new SiegeClanObject(SiegeEvent.ATTACKERS, clan, 0);
			siegeEvent.addObject(SiegeEvent.ATTACKERS, siegeClan);
			SiegeClanDAO.getInstance().insert(fortress, siegeClan);
			
			siegeEvent.reCalcNextTime(false);
			
			player.sendPacket(new SystemMessage2(SystemMsg.YOUR_CLAN_HAS_BEEN_REGISTERED_TO_S1S_FORTRESS_BATTLE).addResidenceName(fortress));
			showChatWindow(player, "residence2/fortress/fortress_ordery005.htm");
		}
		else if (command.equalsIgnoreCase("cancel"))
		{
			Clan clan = player.getClan();
			if ((clan == null) || !player.hasPrivilege(Privilege.CS_FS_SIEGE_WAR))
			{
				showChatWindow(player, "residence2/fortress/fortress_ordery010.htm");
				return;
			}
			
			SiegeClanObject siegeClan = siegeEvent.getSiegeClan(SiegeEvent.ATTACKERS, clan);
			if (siegeClan != null)
			{
				siegeEvent.removeObject(SiegeEvent.ATTACKERS, siegeClan);
				SiegeClanDAO.getInstance().delete(fortress, siegeClan);
				
				siegeEvent.reCalcNextTime(false);
				
				showChatWindow(player, "residence2/fortress/fortress_ordery009.htm");
			}
			else
			{
				showChatWindow(player, "residence2/fortress/fortress_ordery011.htm");
			}
		}
		else if (command.equalsIgnoreCase("state"))
		{
			int attackersSize = siegeEvent.getObjects(SiegeEvent.ATTACKERS).size();
			if (attackersSize == 0)
			{
				showChatWindow(player, "residence2/fortress/fortress_ordery019.htm");
			}
			else
			{
				showChatWindow(player, "residence2/fortress/fortress_ordery020.htm");
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
		NpcHtmlMessage html = new NpcHtmlMessage(player, this);
		Fortress fortress = getFortress();
		if (fortress.getOwner() != null)
		{
			html.setFile("residence2/fortress/fortress_ordery001a.htm");
			html.replace("%clan_name%", fortress.getOwner().getName());
		}
		else
		{
			html.setFile("residence2/fortress/fortress_ordery001.htm");
		}
		
		player.sendPacket(html);
	}
}