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
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.World;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.items.TradeItem;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class TradeHelper
{
	/**
	 * Constructor for TradeHelper.
	 */
	private TradeHelper()
	{
	}
	
	/**
	 * Method checksIfCanOpenStore.
	 * @param player Player
	 * @param storeType int
	 * @return boolean
	 */
	public static boolean checksIfCanOpenStore(Player player, int storeType)
	{
		if (!player.getPlayerAccess().UseTrade)
		{
			player.sendPacket(Msg.THIS_ACCOUNT_CANOT_USE_PRIVATE_STORES);
			return false;
		}
		if (player.getLevel() < Config.SERVICES_TRADE_MIN_LEVEL)
		{
			player.sendMessage(new CustomMessage("trade.NotHavePermission", player).addNumber(Config.SERVICES_TRADE_MIN_LEVEL));
			return false;
		}
		String tradeBan = player.getVar("tradeBan");
		if ((tradeBan != null) && (tradeBan.equals("-1") || (Long.parseLong(tradeBan) >= System.currentTimeMillis())))
		{
			player.sendPacket(Msg.YOU_ARE_CURRENTLY_BANNED_FROM_ACTIVITIES_RELATED_TO_THE_PRIVATE_STORE_AND_PRIVATE_WORKSHOP);
			return false;
		}
		String BLOCK_ZONE = storeType == Player.STORE_PRIVATE_MANUFACTURE ? Zone.BLOCKED_ACTION_PRIVATE_WORKSHOP : Zone.BLOCKED_ACTION_PRIVATE_STORE;
		if (player.isActionBlocked(BLOCK_ZONE))
		{
			if (!Config.SERVICES_NO_TRADE_ONLY_OFFLINE || (Config.SERVICES_NO_TRADE_ONLY_OFFLINE && player.isInOfflineMode()))
			{
				player.sendPacket(storeType == Player.STORE_PRIVATE_MANUFACTURE ? new SystemMessage(SystemMessage.A_PRIVATE_WORKSHOP_MAY_NOT_BE_OPENED_IN_THIS_AREA) : Msg.A_PRIVATE_STORE_MAY_NOT_BE_OPENED_IN_THIS_AREA);
				return false;
			}
		}
		if (player.isCastingNow())
		{
			player.sendPacket(Msg.A_PRIVATE_STORE_MAY_NOT_BE_OPENED_WHILE_USING_A_SKILL);
			return false;
		}
		if (player.isInCombat())
		{
			player.sendPacket(Msg.WHILE_YOU_ARE_ENGAGED_IN_COMBAT_YOU_CANNOT_OPERATE_A_PRIVATE_STORE_OR_PRIVATE_WORKSHOP);
			return false;
		}
		if (player.isActionsDisabled() || player.isMounted() || player.isInOlympiadMode() || player.isInDuel() || player.isProcessingRequest())
		{
			return false;
		}
		if (Config.SERVICES_TRADE_ONLY_FAR)
		{
			boolean tradenear = false;
			for (Player p : World.getAroundPlayers(player, Config.SERVICES_TRADE_RADIUS, 200))
			{
				if (p.isInStoreMode())
				{
					tradenear = true;
					break;
				}
			}
			if (World.getAroundNpc(player, Config.SERVICES_TRADE_RADIUS + 100, 200).size() > 0)
			{
				tradenear = true;
			}
			if (tradenear)
			{
				player.sendMessage(new CustomMessage("trade.OtherTradersNear", player));
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method purchaseItem.
	 * @param buyer Player
	 * @param seller Player
	 * @param item TradeItem
	 */
	public final static void purchaseItem(Player buyer, Player seller, TradeItem item)
	{
		long price = item.getCount() * item.getOwnersPrice();
		if (!item.getItem().isStackable())
		{
			if (item.getEnchantLevel() > 0)
			{
				seller.sendPacket(new SystemMessage(SystemMessage._S2S3_HAS_BEEN_SOLD_TO_S1_AT_THE_PRICE_OF_S4_ADENA).addString(buyer.getName()).addNumber(item.getEnchantLevel()).addItemName(item.getItemId()).addNumber(price));
				buyer.sendPacket(new SystemMessage(SystemMessage._S2S3_HAS_BEEN_PURCHASED_FROM_S1_AT_THE_PRICE_OF_S4_ADENA).addString(seller.getName()).addNumber(item.getEnchantLevel()).addItemName(item.getItemId()).addNumber(price));
			}
			else
			{
				seller.sendPacket(new SystemMessage(SystemMessage.S2_IS_SOLD_TO_S1_AT_THE_PRICE_OF_S3_ADENA).addString(buyer.getName()).addItemName(item.getItemId()).addNumber(price));
				buyer.sendPacket(new SystemMessage(SystemMessage.S2_HAS_BEEN_PURCHASED_FROM_S1_AT_THE_PRICE_OF_S3_ADENA).addString(seller.getName()).addItemName(item.getItemId()).addNumber(price));
			}
		}
		else
		{
			seller.sendPacket(new SystemMessage(SystemMessage.S2_S3_HAVE_BEEN_SOLD_TO_S1_FOR_S4_ADENA).addString(buyer.getName()).addItemName(item.getItemId()).addNumber(item.getCount()).addNumber(price));
			buyer.sendPacket(new SystemMessage(SystemMessage.S3_S2_HAS_BEEN_PURCHASED_FROM_S1_FOR_S4_ADENA).addString(seller.getName()).addItemName(item.getItemId()).addNumber(item.getCount()).addNumber(price));
		}
	}
	
	/**
	 * Method getTax.
	 * @param seller Player
	 * @param price long
	 * @return long
	 */
	public final static long getTax(Player seller, long price)
	{
		long tax = (long) ((price * Config.SERVICES_TRADE_TAX) / 100);
		if (seller.isInZone(Zone.ZoneType.offshore))
		{
			tax = (long) ((price * Config.SERVICES_OFFSHORE_TRADE_TAX) / 100);
		}
		if (Config.SERVICES_TRADE_TAX_ONLY_OFFLINE && !seller.isInOfflineMode())
		{
			tax = 0;
		}
		if (Config.SERVICES_PARNASSUS_NOTAX && (seller.getReflection() == ReflectionManager.PARNASSUS))
		{
			tax = 0;
		}
		return tax;
	}
	
	/**
	 * Method cancelStore.
	 * @param activeChar Player
	 */
	public static void cancelStore(Player activeChar)
	{
		activeChar.setPrivateStoreType(Player.STORE_PRIVATE_NONE);
		if (activeChar.isInOfflineMode())
		{
			activeChar.setOfflineMode(false);
			activeChar.kick();
		}
		else
		{
			activeChar.broadcastCharInfo();
		}
	}
}
