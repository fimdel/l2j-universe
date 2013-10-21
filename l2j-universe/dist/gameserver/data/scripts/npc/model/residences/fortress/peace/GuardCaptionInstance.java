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

import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.events.impl.FortressSiegeEvent;
import lineage2.gameserver.model.entity.events.impl.SiegeEvent;
import lineage2.gameserver.model.entity.events.objects.DoorObject;
import lineage2.gameserver.model.entity.residence.Fortress;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.templates.npc.NpcTemplate;
import npc.model.residences.fortress.FacilityManagerInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class GuardCaptionInstance extends FacilityManagerInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor for GuardCaptionInstance.
	 * @param objectId int
	 * @param template NpcTemplate
	 */
	public GuardCaptionInstance(int objectId, NpcTemplate template)
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
		Fortress fortress = getFortress();
		if (command.equalsIgnoreCase("defenceInfo"))
		{
			if ((player.getClanPrivileges() & Clan.CP_CS_MANAGE_SIEGE) != Clan.CP_CS_MANAGE_SIEGE)
			{
				showChatWindow(player, "residence2/fortress/fortress_not_authorized.htm");
				return;
			}
			if (fortress.getContractState() != Fortress.CONTRACT_WITH_CASTLE)
			{
				showChatWindow(player, "residence2/fortress/fortress_supply_officer005.htm");
				return;
			}
			showChatWindow(player, "residence2/fortress/fortress_garrison002.htm", "%facility_0%", fortress.getFacilityLevel(Fortress.REINFORCE), "%facility_2%", fortress.getFacilityLevel(Fortress.DOOR_UPGRADE), "%facility_3%", fortress.getFacilityLevel(Fortress.DWARVENS), "%facility_4%", fortress.getFacilityLevel(Fortress.SCOUT));
		}
		else if (command.equalsIgnoreCase("defenceUp1") || command.equalsIgnoreCase("defenceUp2"))
		{
			buyFacility(player, Fortress.REINFORCE, Integer.parseInt(command.substring(9, 10)), 100000);
		}
		else if (command.equalsIgnoreCase("deployScouts"))
		{
			buyFacility(player, Fortress.SCOUT, 1, 150000);
		}
		else if (command.equalsIgnoreCase("doorUpgrade"))
		{
			boolean buy = buyFacility(player, Fortress.DOOR_UPGRADE, 1, 200000);
			if (buy)
			{
				List<DoorObject> doorObjects = fortress.getSiegeEvent().getObjects(FortressSiegeEvent.UPGRADEABLE_DOORS);
				for (DoorObject d : doorObjects)
				{
					d.setUpgradeValue(fortress.<SiegeEvent<?, ?>> getSiegeEvent(), d.getDoor().getMaxHp() * fortress.getFacilityLevel(Fortress.DOOR_UPGRADE));
				}
			}
		}
		else if (command.equalsIgnoreCase("hireDwarves"))
		{
			buyFacility(player, Fortress.DWARVENS, 1, 100000);
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
		showChatWindow(player, "residence2/fortress/fortress_garrison001.htm");
	}
}
