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
package lineage2.gameserver.handler.admincommands.impl;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.instancemanager.MapRegionManager;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.residence.Castle;
import lineage2.gameserver.templates.mapregion.DomainArea;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminZone implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_zone_check.
		 */
		admin_zone_check,
		/**
		 * Field admin_region.
		 */
		admin_region,
		/**
		 * Field admin_pos.
		 */
		admin_pos,
		/**
		 * Field admin_vis_count.
		 */
		admin_vis_count,
		/**
		 * Field admin_domain.
		 */
		admin_domain
	}
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;
		if ((activeChar == null) || !activeChar.getPlayerAccess().CanTeleport)
		{
			return false;
		}
		switch (command)
		{
			case admin_zone_check:
			{
				activeChar.sendMessage("Current region: " + activeChar.getCurrentRegion());
				activeChar.sendMessage("Zone list:");
				List<Zone> zones = new ArrayList<>();
				World.getZones(zones, activeChar.getLoc(), activeChar.getReflection());
				for (Zone zone : zones)
				{
					activeChar.sendMessage(zone.getType().toString() + ", name: " + zone.getName() + ", state: " + (zone.isActive() ? "active" : "not active") + ", inside: " + zone.checkIfInZone(activeChar) + "/" + zone.checkIfInZone(activeChar.getX(), activeChar.getY(), activeChar.getZ()));
				}
				break;
			}
			case admin_region:
			{
				activeChar.sendMessage("Current region: " + activeChar.getCurrentRegion());
				activeChar.sendMessage("Objects list:");
				for (GameObject o : activeChar.getCurrentRegion())
				{
					if (o != null)
					{
						activeChar.sendMessage(o.toString());
					}
				}
				break;
			}
			case admin_vis_count:
			{
				activeChar.sendMessage("Current region: " + activeChar.getCurrentRegion());
				activeChar.sendMessage("Players count: " + World.getAroundPlayers(activeChar).size());
				break;
			}
			case admin_pos:
			{
				String pos = activeChar.getX() + ", " + activeChar.getY() + ", " + activeChar.getZ() + ", " + activeChar.getHeading() + " Geo [" + ((activeChar.getX() - World.MAP_MIN_X) >> 4) + ", " + ((activeChar.getY() - World.MAP_MIN_Y) >> 4) + "] Ref " + activeChar.getReflectionId();
				activeChar.sendMessage("Pos: " + pos);
				break;
			}
			case admin_domain:
			{
				DomainArea domain = MapRegionManager.getInstance().getRegionData(DomainArea.class, activeChar);
				Castle castle = domain != null ? ResidenceHolder.getInstance().getResidence(Castle.class, domain.getId()) : null;
				if (castle != null)
				{
					activeChar.sendMessage("Domain: " + castle.getName());
				}
				else
				{
					activeChar.sendMessage("Domain: Unknown");
				}
			}
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[] * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}
