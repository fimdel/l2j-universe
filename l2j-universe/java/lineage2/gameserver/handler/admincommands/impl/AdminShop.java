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

import lineage2.gameserver.data.xml.holder.BuyListHolder;
import lineage2.gameserver.data.xml.holder.BuyListHolder.NpcTradeList;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExBuySellList;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.utils.GameStats;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminShop implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_buy.
		 */
		admin_buy,
		/**
		 * Field admin_gmshop.
		 */
		admin_gmshop,
		/**
		 * Field admin_tax.
		 */
		admin_tax,
		/**
		 * Field admin_taxclear.
		 */
		admin_taxclear
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
		if (!activeChar.getPlayerAccess().UseGMShop)
		{
			return false;
		}
		switch (command)
		{
			case admin_buy:
				try
				{
					handleBuyRequest(activeChar, fullString.substring(10));
				}
				catch (IndexOutOfBoundsException e)
				{
					activeChar.sendMessage("Please specify buylist.");
				}
				break;
			case admin_gmshop:
				activeChar.sendPacket(new NpcHtmlMessage(5).setFile("admin/gmshops.htm"));
				break;
			case admin_tax:
				activeChar.sendMessage("TaxSum: " + GameStats.getTaxSum());
				break;
			case admin_taxclear:
				GameStats.addTax(-GameStats.getTaxSum());
				activeChar.sendMessage("TaxSum: " + GameStats.getTaxSum());
				break;
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
	
	/**
	 * Method handleBuyRequest.
	 * @param activeChar Player
	 * @param command String
	 */
	private void handleBuyRequest(Player activeChar, String command)
	{
		int val = -1;
		try
		{
			val = Integer.parseInt(command);
		}
		catch (Exception e)
		{
		}
		NpcTradeList list = BuyListHolder.getInstance().getBuyList(val);
		if (list != null)
		{
			activeChar.sendPacket(new ExBuySellList.BuyList(list, activeChar, 0.), new ExBuySellList.SellRefundList(activeChar, false));
		}
		activeChar.sendActionFailed();
	}
}
