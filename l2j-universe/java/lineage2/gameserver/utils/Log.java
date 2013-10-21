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

import lineage2.commons.text.PrintfFormat;
import lineage2.gameserver.Config;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Log
{
	/**
	 * Field LOG_BOSS_KILLED.
	 */
	public static final PrintfFormat LOG_BOSS_KILLED = new PrintfFormat("%s: %s[%d] killed by %s at Loc(%d %d %d) in %s");
	/**
	 * Field LOG_BOSS_RESPAWN.
	 */
	public static final PrintfFormat LOG_BOSS_RESPAWN = new PrintfFormat("%s: %s[%d] scheduled for respawn in %s at %s");
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(Log.class);
	/**
	 * Field _logChat.
	 */
	private static final Logger _logChat = LoggerFactory.getLogger("chat");
	/**
	 * Field _logGm.
	 */
	private static final Logger _logGm = LoggerFactory.getLogger("gmactions");
	/**
	 * Field _logItems.
	 */
	private static final Logger _logItems = LoggerFactory.getLogger("item");
	/**
	 * Field _logGame.
	 */
	private static final Logger _logGame = LoggerFactory.getLogger("game");
	/**
	 * Field _logDebug.
	 */
	private static final Logger _logDebug = LoggerFactory.getLogger("debug");
	/**
	 * Field Create. (value is ""Create"")
	 */
	public static final String Create = "Create";
	/**
	 * Field Delete. (value is ""Delete"")
	 */
	public static final String Delete = "Delete";
	/**
	 * Field Drop. (value is ""Drop"")
	 */
	public static final String Drop = "Drop";
	/**
	 * Field PvPDrop. (value is ""PvPDrop"")
	 */
	public static final String PvPDrop = "PvPDrop";
	/**
	 * Field Crystalize. (value is ""Crystalize"")
	 */
	public static final String Crystalize = "Crystalize";
	/**
	 * Field EnchantFail. (value is ""EnchantFail"")
	 */
	public static final String EnchantFail = "EnchantFail";
	/**
	 * Field Pickup. (value is ""Pickup"")
	 */
	public static final String Pickup = "Pickup";
	/**
	 * Field PartyPickup. (value is ""PartyPickup"")
	 */
	public static final String PartyPickup = "PartyPickup";
	/**
	 * Field PrivateStoreBuy. (value is ""PrivateStoreBuy"")
	 */
	public static final String PrivateStoreBuy = "PrivateStoreBuy";
	/**
	 * Field PrivateStoreSell. (value is ""PrivateStoreSell"")
	 */
	public static final String PrivateStoreSell = "PrivateStoreSell";
	/**
	 * Field TradeBuy. (value is ""TradeBuy"")
	 */
	public static final String TradeBuy = "TradeBuy";
	/**
	 * Field TradeSell. (value is ""TradeSell"")
	 */
	public static final String TradeSell = "TradeSell";
	/**
	 * Field PostRecieve. (value is ""PostRecieve"")
	 */
	public static final String PostRecieve = "PostRecieve";
	/**
	 * Field PostSend. (value is ""PostSend"")
	 */
	public static final String PostSend = "PostSend";
	/**
	 * Field PostCancel. (value is ""PostCancel"")
	 */
	public static final String PostCancel = "PostCancel";
	/**
	 * Field PostExpire. (value is ""PostExpire"")
	 */
	public static final String PostExpire = "PostExpire";
	/**
	 * Field RefundSell. (value is ""RefundSell"")
	 */
	public static final String RefundSell = "RefundSell";
	/**
	 * Field RefundReturn. (value is ""RefundReturn"")
	 */
	public static final String RefundReturn = "RefundReturn";
	/**
	 * Field WarehouseDeposit. (value is ""WarehouseDeposit"")
	 */
	public static final String WarehouseDeposit = "WarehouseDeposit";
	/**
	 * Field WarehouseWithdraw. (value is ""WarehouseWithdraw"")
	 */
	public static final String WarehouseWithdraw = "WarehouseWithdraw";
	/**
	 * Field FreightWithdraw. (value is ""FreightWithdraw"")
	 */
	public static final String FreightWithdraw = "FreightWithdraw";
	/**
	 * Field FreightDeposit. (value is ""FreightDeposit"")
	 */
	public static final String FreightDeposit = "FreightDeposit";
	/**
	 * Field ClanWarehouseDeposit. (value is ""ClanWarehouseDeposit"")
	 */
	public static final String ClanWarehouseDeposit = "ClanWarehouseDeposit";
	/**
	 * Field ClanWarehouseWithdraw. (value is ""ClanWarehouseWithdraw"")
	 */
	public static final String ClanWarehouseWithdraw = "ClanWarehouseWithdraw";
	/**
	 * Field CommissionItemRegister. (value is ""CommissionShopRegister"")
	 */
	public static final String CommissionItemRegister = "CommissionShopRegister";
	/**
	 * Field CommissionItemSold. (value is ""CommissionShopItemSold"")
	 */
	public static final String CommissionItemSold = "CommissionShopItemSold";
	/**
	 * Field CommissionItemDelete. (value is ""CommissionShopItemDelete"")
	 */
	public static final String CommissionItemDelete = "CommissionShopItemDelete";
	
	/**
	 * Method add.
	 * @param fmt PrintfFormat
	 * @param o Object[]
	 * @param cat String
	 */
	public static void add(PrintfFormat fmt, Object[] o, String cat)
	{
		add(fmt.sprintf(o), cat);
	}
	
	/**
	 * Method add.
	 * @param fmt String
	 * @param o Object[]
	 * @param cat String
	 */
	public static void add(String fmt, Object[] o, String cat)
	{
		add(new PrintfFormat(fmt).sprintf(o), cat);
	}
	
	/**
	 * Method add.
	 * @param text String
	 * @param cat String
	 * @param player Player
	 */
	public static void add(String text, String cat, Player player)
	{
		StringBuilder output = new StringBuilder();
		output.append(cat);
		if (player != null)
		{
			output.append(' ');
			output.append(player);
		}
		output.append(' ');
		output.append(text);
		_logGame.info(output.toString());
	}
	
	/**
	 * Method add.
	 * @param text String
	 * @param cat String
	 */
	public static void add(String text, String cat)
	{
		add(text, cat, null);
	}
	
	/**
	 * Method debug.
	 * @param text String
	 */
	public static void debug(String text)
	{
		_logDebug.debug(text);
	}
	
	/**
	 * Method debug.
	 * @param text String
	 * @param t Throwable
	 */
	public static void debug(String text, Throwable t)
	{
		_logDebug.debug(text, t);
	}
	
	/**
	 * Method LogChat.
	 * @param type String
	 * @param player String
	 * @param target String
	 * @param text String
	 */
	public static void LogChat(String type, String player, String target, String text)
	{
		if (!Config.LOG_CHAT)
		{
			return;
		}
		StringBuilder output = new StringBuilder();
		output.append(type);
		output.append(' ');
		output.append('[');
		output.append(player);
		if (target != null)
		{
			output.append(" -> ");
			output.append(target);
		}
		output.append(']');
		output.append(' ');
		output.append(text);
		_logChat.info(output.toString());
	}
	
	/**
	 * Method LogCommand.
	 * @param player Player
	 * @param target GameObject
	 * @param command String
	 * @param success boolean
	 */
	public static void LogCommand(Player player, GameObject target, String command, boolean success)
	{
		StringBuilder output = new StringBuilder();
		if (success)
		{
			output.append("SUCCESS");
		}
		else
		{
			output.append("FAIL   ");
		}
		output.append(' ');
		output.append(player);
		if (target != null)
		{
			output.append(" -> ");
			output.append(target);
		}
		output.append(' ');
		output.append(command);
		_logGm.info(output.toString());
	}
	
	/**
	 * Method LogItem.
	 * @param activeChar Creature
	 * @param process String
	 * @param item ItemInstance
	 */
	public static void LogItem(Creature activeChar, String process, ItemInstance item)
	{
		LogItem(activeChar, process, item, item.getCount());
	}
	
	/**
	 * Method LogItem.
	 * @param activeChar Creature
	 * @param process String
	 * @param item ItemInstance
	 * @param count long
	 */
	public static void LogItem(Creature activeChar, String process, ItemInstance item, long count)
	{
		StringBuilder output = new StringBuilder();
		output.append(process);
		output.append(' ');
		output.append(item);
		output.append(' ');
		output.append(activeChar);
		output.append(' ');
		output.append(count);
		_logItems.info(output.toString());
	}
	
	/**
	 * Method LogPetition.
	 * @param fromChar Player
	 * @param Petition_type Integer
	 * @param Petition_text String
	 */
	public static void LogPetition(Player fromChar, Integer Petition_type, String Petition_text)
	{
	}
	
	/**
	 * Method LogAudit.
	 * @param player Player
	 * @param type String
	 * @param msg String
	 */
	public static void LogAudit(Player player, String type, String msg)
	{
	}
}
