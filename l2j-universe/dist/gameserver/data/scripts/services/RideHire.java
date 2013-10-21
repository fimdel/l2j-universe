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
package services;

import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SetupGauge;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.tables.PetDataTable;
import lineage2.gameserver.utils.SiegeUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class RideHire extends Functions
{
	/**
	 * Method DialogAppend_30827.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30827(Integer val)
	{
		if (val == 0)
		{
			return "<br>[scripts_services.RideHire:ride_prices|Ride hire mountable pet.]";
		}
		return "";
	}
	
	/**
	 * Method ride_prices.
	 */
	public void ride_prices()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		show("scripts/services/ride-prices.htm", player, npc);
	}
	
	/**
	 * Method ride.
	 * @param args String[]
	 */
	public void ride(String[] args)
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (args.length != 3)
		{
			show("Incorrect input", player, npc);
			return;
		}
		if (!NpcInstance.canBypassCheck(player, npc))
		{
			return;
		}
		if (player.getActiveWeaponFlagAttachment() != null)
		{
			player.sendPacket(Msg.YOU_CANNOT_MOUNT_BECAUSE_YOU_DO_NOT_MEET_THE_REQUIREMENTS);
			return;
		}
		if (player.getTransformation() != 0)
		{
			show("Can't ride while in transformation mode.", player, npc);
			return;
		}
		if ((player.getSummonList() != null) || player.isMounted())
		{
			player.sendPacket(Msg.YOU_ALREADY_HAVE_A_PET);
			return;
		}
		int npc_id;
		switch (Integer.parseInt(args[0]))
		{
			case 1:
				npc_id = PetDataTable.WYVERN_ID;
				break;
			case 2:
				npc_id = PetDataTable.STRIDER_WIND_ID;
				break;
			case 3:
				npc_id = PetDataTable.WGREAT_WOLF_ID;
				break;
			case 4:
				npc_id = PetDataTable.WFENRIR_WOLF_ID;
				break;
			default:
				show("Unknown pet.", player, npc);
				return;
		}
		if (((npc_id == PetDataTable.WYVERN_ID) || (npc_id == PetDataTable.STRIDER_WIND_ID)) && !SiegeUtils.getCanRide())
		{
			show("Can't ride wyvern/strider while Siege in progress.", player, npc);
			return;
		}
		Integer time = Integer.parseInt(args[1]);
		Long price = Long.parseLong(args[2]);
		if (time > 1800)
		{
			show("Too long time to ride.", player, npc);
			return;
		}
		if (player.getAdena() < price)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		player.reduceAdena(price, true);
		doLimitedRide(player, npc_id, time);
	}
	
	/**
	 * Method doLimitedRide.
	 * @param player Player
	 * @param npc_id Integer
	 * @param time Integer
	 */
	public void doLimitedRide(Player player, Integer npc_id, Integer time)
	{
		if (!ride(player, npc_id))
		{
			return;
		}
		player.sendPacket(new SetupGauge(player, 3, time * 1000));
		executeTask(player, "services.RideHire", "rideOver", new Object[0], time * 1000);
	}
	
	/**
	 * Method rideOver.
	 */
	public void rideOver()
	{
		Player player = getSelf();
		if (player == null)
		{
			return;
		}
		unRide(player);
		show("Ride time is over.<br><br>Welcome back again!", player);
	}
}
