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
package lineage2.gameserver.utils;

import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.Warehouse;
import lineage2.gameserver.model.items.Warehouse.WarehouseType;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.WareHouseDepositList;
import lineage2.gameserver.network.serverpackets.WareHouseWithdrawList;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.templates.item.ItemTemplate.ItemClass;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class WarehouseFunctions
{
	/**
	 * Constructor for WarehouseFunctions.
	 */
	private WarehouseFunctions()
	{
	}
	
	/**
	 * Method showFreightWindow.
	 * @param player Player
	 */
	public static void showFreightWindow(Player player)
	{
		if (!WarehouseFunctions.canShowWarehouseWithdrawList(player, WarehouseType.FREIGHT))
		{
			player.sendActionFailed();
			return;
		}
		player.setUsingWarehouseType(WarehouseType.FREIGHT);
		player.sendPacket(new WareHouseWithdrawList(player, WarehouseType.FREIGHT, ItemClass.ALL));
	}
	
	/**
	 * Method showRetrieveWindow.
	 * @param player Player
	 * @param val int
	 */
	public static void showRetrieveWindow(Player player, int val)
	{
		if (!WarehouseFunctions.canShowWarehouseWithdrawList(player, WarehouseType.PRIVATE))
		{
			player.sendActionFailed();
			return;
		}
		player.setUsingWarehouseType(WarehouseType.PRIVATE);
		player.sendPacket(new WareHouseWithdrawList(player, WarehouseType.PRIVATE, ItemClass.values()[val]));
	}
	
	/**
	 * Method showDepositWindow.
	 * @param player Player
	 */
	public static void showDepositWindow(Player player)
	{
		if (!WarehouseFunctions.canShowWarehouseDepositList(player, WarehouseType.PRIVATE))
		{
			player.sendActionFailed();
			return;
		}
		player.setUsingWarehouseType(WarehouseType.PRIVATE);
		player.sendPacket(new WareHouseDepositList(player, WarehouseType.PRIVATE));
	}
	
	/**
	 * Method showDepositWindowClan.
	 * @param player Player
	 */
	public static void showDepositWindowClan(Player player)
	{
		if (!WarehouseFunctions.canShowWarehouseDepositList(player, WarehouseType.CLAN))
		{
			player.sendActionFailed();
			return;
		}
		if (!(player.isClanLeader() || ((Config.ALT_ALLOW_OTHERS_WITHDRAW_FROM_CLAN_WAREHOUSE || player.getVarB("canWhWithdraw")) && ((player.getClanPrivileges() & Clan.CP_CL_WAREHOUSE_SEARCH) == Clan.CP_CL_WAREHOUSE_SEARCH))))
		{
			player.sendPacket(Msg.ITEMS_LEFT_AT_THE_CLAN_HALL_WAREHOUSE_CAN_ONLY_BE_RETRIEVED_BY_THE_CLAN_LEADER_DO_YOU_WANT_TO_CONTINUE);
		}
		player.setUsingWarehouseType(WarehouseType.CLAN);
		player.sendPacket(new WareHouseDepositList(player, WarehouseType.CLAN));
	}
	
	/**
	 * Method showWithdrawWindowClan.
	 * @param player Player
	 * @param val int
	 */
	public static void showWithdrawWindowClan(Player player, int val)
	{
		if (!WarehouseFunctions.canShowWarehouseWithdrawList(player, WarehouseType.CLAN))
		{
			player.sendActionFailed();
			return;
		}
		player.setUsingWarehouseType(WarehouseType.CLAN);
		player.sendPacket(new WareHouseWithdrawList(player, WarehouseType.CLAN, ItemClass.values()[val]));
	}
	
	/**
	 * Method canShowWarehouseWithdrawList.
	 * @param player Player
	 * @param type WarehouseType
	 * @return boolean
	 */
	public static boolean canShowWarehouseWithdrawList(Player player, WarehouseType type)
	{
		if (!player.getPlayerAccess().UseWarehouse)
		{
			return false;
		}
		Warehouse warehouse = null;
		switch (type)
		{
			case PRIVATE:
				warehouse = player.getWarehouse();
				break;
			case FREIGHT:
				warehouse = player.getFreight();
				break;
			case CLAN:
			case CASTLE:
				if ((player.getClan() == null) || (player.getClan().getLevel() == 0))
				{
					player.sendPacket(Msg.ONLY_CLANS_OF_CLAN_LEVEL_1_OR_HIGHER_CAN_USE_A_CLAN_WAREHOUSE);
					return false;
				}
				boolean canWithdrawCWH = false;
				if (player.getClan() != null)
				{
					if ((player.getClanPrivileges() & Clan.CP_CL_WAREHOUSE_SEARCH) == Clan.CP_CL_WAREHOUSE_SEARCH)
					{
						canWithdrawCWH = true;
					}
				}
				if (!canWithdrawCWH)
				{
					player.sendPacket(Msg.YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_THE_CLAN_WAREHOUSE);
					return false;
				}
				warehouse = player.getClan().getWarehouse();
				break;
			default:
				return false;
		}
		if (warehouse.getSize() == 0)
		{
			player.sendPacket(type == WarehouseType.FREIGHT ? SystemMsg.NO_PACKAGES_HAVE_ARRIVED : Msg.YOU_HAVE_NOT_DEPOSITED_ANY_ITEMS_IN_YOUR_WAREHOUSE);
			return false;
		}
		return true;
	}
	
	/**
	 * Method canShowWarehouseDepositList.
	 * @param player Player
	 * @param type WarehouseType
	 * @return boolean
	 */
	public static boolean canShowWarehouseDepositList(Player player, WarehouseType type)
	{
		if (!player.getPlayerAccess().UseWarehouse)
		{
			return false;
		}
		switch (type)
		{
			case PRIVATE:
				return true;
			case CLAN:
			case CASTLE:
				if ((player.getClan() == null) || (player.getClan().getLevel() == 0))
				{
					player.sendPacket(Msg.ONLY_CLANS_OF_CLAN_LEVEL_1_OR_HIGHER_CAN_USE_A_CLAN_WAREHOUSE);
					return false;
				}
				boolean canWithdrawCWH = false;
				if (player.getClan() != null)
				{
					if ((player.getClanPrivileges() & Clan.CP_CL_WAREHOUSE_SEARCH) == Clan.CP_CL_WAREHOUSE_SEARCH)
					{
						canWithdrawCWH = true;
					}
				}
				if (!canWithdrawCWH)
				{
					player.sendPacket(Msg.YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_THE_CLAN_WAREHOUSE);
					return false;
				}
				return true;
			default:
				return false;
		}
	}
}
