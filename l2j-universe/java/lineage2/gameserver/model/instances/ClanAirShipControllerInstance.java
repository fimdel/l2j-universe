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

import lineage2.commons.lang.reference.HardReference;
import lineage2.commons.lang.reference.HardReferences;
import lineage2.gameserver.data.xml.holder.AirshipDockHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.boat.ClanAirShip;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.templates.AirshipDock;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClanAirShipControllerInstance extends AirShipControllerInstance
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field ENERGY_STAR_STONE. (value is 13277)
	 */
	protected static final int ENERGY_STAR_STONE = 13277;
	/**
	 * Field AIRSHIP_SUMMON_LICENSE. (value is 13559)
	 */
	protected static final int AIRSHIP_SUMMON_LICENSE = 13559;
	/**
	 * Field _dockedShipRef.
	 */
	private HardReference<ClanAirShip> _dockedShipRef = HardReferences.emptyRef();
	/**
	 * Field _dock.
	 */
	private final AirshipDock _dock;
	/**
	 * Field _platform.
	 */
	private final AirshipDock.AirshipPlatform _platform;
	
	/**
	 * Constructor for ClanAirShipControllerInstance.
	 * @param objectID int
	 * @param template NpcTemplate
	 */
	public ClanAirShipControllerInstance(int objectID, NpcTemplate template)
	{
		super(objectID, template);
		int dockId = template.getAIParams().getInteger("dockId", 0);
		int platformId = template.getAIParams().getInteger("platformId", 0);
		_dock = AirshipDockHolder.getInstance().getDock(dockId);
		_platform = _dock.getPlatform(platformId);
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
		if (command.equalsIgnoreCase("summon"))
		{
			if ((player.getClan() == null) || (player.getClan().getLevel() < 5))
			{
				player.sendPacket(SystemMsg.IN_ORDER_TO_ACQUIRE_AN_AIRSHIP_THE_CLANS_LEVEL_MUST_BE_LEVEL_5_OR_HIGHER);
				return;
			}
			if ((player.getClanPrivileges() & Clan.CP_CL_SUMMON_AIRSHIP) != Clan.CP_CL_SUMMON_AIRSHIP)
			{
				player.sendPacket(SystemMsg.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			if (!player.getClan().isHaveAirshipLicense())
			{
				player.sendPacket(SystemMsg.AN_AIRSHIP_CANNOT_BE_SUMMONED_BECAUSE_EITHER_YOU_HAVE_NOT_REGISTERED_YOUR_AIRSHIP_LICENSE_OR_THE_AIRSHIP_HAS_NOT_YET_BEEN_SUMMONED);
				return;
			}
			ClanAirShip dockedAirShip = getDockedAirShip();
			ClanAirShip clanAirship = player.getClan().getAirship();
			if (clanAirship != null)
			{
				if (clanAirship == dockedAirShip)
				{
					player.sendPacket(SystemMsg.THE_CLAN_OWNED_AIRSHIP_ALREADY_EXISTS);
				}
				else
				{
					player.sendPacket(SystemMsg.YOUR_CLANS_AIRSHIP_IS_ALREADY_BEING_USED_BY_ANOTHER_CLAN_MEMBER);
				}
				return;
			}
			if (dockedAirShip != null)
			{
				Functions.npcSay(this, NpcString.IN_AIR_HARBOR_ALREADY_AIRSHIP_DOCKED_PLEASE_WAIT_AND_TRY_AGAIN, ChatType.SHOUT, 5000);
				return;
			}
			if (Functions.removeItem(player, ENERGY_STAR_STONE, 5) != 5)
			{
				player.sendPacket(new SystemMessage2(SystemMsg.AN_AIRSHIP_CANNOT_BE_SUMMONED_BECAUSE_YOU_DONT_HAVE_ENOUGH_S1).addItemName(ENERGY_STAR_STONE));
				return;
			}
			ClanAirShip dockedShip = new ClanAirShip(player.getClan());
			dockedShip.setDock(_dock);
			dockedShip.setPlatform(_platform);
			dockedShip.setHeading(0);
			dockedShip.spawnMe(_platform.getSpawnLoc());
			dockedShip.startDepartTask();
			Functions.npcSay(this, NpcString.AIRSHIP_IS_SUMMONED_IS_DEPART_IN_5_MINUTES, ChatType.SHOUT, 5000);
		}
		else if (command.equalsIgnoreCase("register"))
		{
			if ((player.getClan() == null) || !player.isClanLeader() || (player.getClan().getLevel() < 5))
			{
				player.sendPacket(SystemMsg.IN_ORDER_TO_ACQUIRE_AN_AIRSHIP_THE_CLANS_LEVEL_MUST_BE_LEVEL_5_OR_HIGHER);
				return;
			}
			if (player.getClan().isHaveAirshipLicense())
			{
				player.sendPacket(SystemMsg.THE_AIRSHIP_SUMMON_LICENSE_HAS_ALREADY_BEEN_ACQUIRED);
				return;
			}
			if (Functions.getItemCount(player, AIRSHIP_SUMMON_LICENSE) == 0)
			{
				player.sendPacket(SystemMsg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
				return;
			}
			Functions.removeItem(player, AIRSHIP_SUMMON_LICENSE, 1);
			player.getClan().setAirshipLicense(true);
			player.getClan().setAirshipFuel(ClanAirShip.MAX_FUEL);
			player.getClan().updateClanInDB();
			player.sendPacket(SystemMsg.THE_AIRSHIP_SUMMON_LICENSE_HAS_BEEN_ENTERED);
		}
		else
		{
			super.onBypassFeedback(player, command);
		}
	}
	
	/**
	 * Method getDockedAirShip.
	 * @return ClanAirShip
	 */
	@Override
	protected ClanAirShip getDockedAirShip()
	{
		ClanAirShip ship = _dockedShipRef.get();
		if ((ship != null) && ship.isDocked())
		{
			return ship;
		}
		return null;
	}
	
	/**
	 * Method setDockedShip.
	 * @param dockedShip ClanAirShip
	 */
	@SuppressWarnings("unchecked")
	public void setDockedShip(ClanAirShip dockedShip)
	{
		ClanAirShip old = _dockedShipRef.get();
		if (old != null)
		{
			old.setDock(null);
			old.setPlatform(null);
		}
		if (dockedShip != null)
		{
			boolean alreadyEnter = dockedShip.getDock() != null;
			dockedShip.setDock(_dock);
			dockedShip.setPlatform(_platform);
			if (!alreadyEnter)
			{
				dockedShip.startArrivalTask();
			}
		}
		if (dockedShip == null)
		{
			_dockedShipRef = HardReferences.emptyRef();
		}
		else
		{
			_dockedShipRef = (HardReference<ClanAirShip>) dockedShip.getRef();
		}
	}
}
