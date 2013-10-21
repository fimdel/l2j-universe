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

import lineage2.gameserver.data.xml.holder.ItemHolder;
import lineage2.gameserver.handler.admincommands.IAdminCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.utils.AdminFunctions;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Util;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AdminNochannel implements IAdminCommandHandler
{
	/**
	 * @author Mobius
	 */
	private static enum Commands
	{
		/**
		 * Field admin_nochannel.
		 */
		admin_nochannel,
		/**
		 * Field admin_nc.
		 */
		admin_nc
	}
	
	/**
	 * Method useAdminCommand.
	 * @param comm Enum<?>
	 * @param wordList String[]
	 * @param fullString String
	 * @param activeChar Player
	 * @return boolean
	 * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#useAdminCommand(Enum<?>, String[], String, Player)
	 */
	@Override
	public boolean useAdminCommand(Enum<?> comm, String[] wordList, String fullString, Player activeChar)
	{
		Commands command = (Commands) comm;
		if (!activeChar.getPlayerAccess().CanBanChat)
		{
			return false;
		}
		int banChatCount = 0;
		int penaltyCount = 0;
		int banChatCountPerDay = activeChar.getPlayerAccess().BanChatCountPerDay;
		if (banChatCountPerDay > 0)
		{
			String count = activeChar.getVar("banChatCount");
			if (count != null)
			{
				banChatCount = Integer.parseInt(count);
			}
			String penalty = activeChar.getVar("penaltyChatCount");
			if (penalty != null)
			{
				penaltyCount = Integer.parseInt(penalty);
			}
			long LastBanChatDayTime = 0;
			String time = activeChar.getVar("LastBanChatDayTime");
			if (time != null)
			{
				LastBanChatDayTime = Long.parseLong(time);
			}
			if (LastBanChatDayTime != 0)
			{
				if ((System.currentTimeMillis() - LastBanChatDayTime) < (1000 * 60 * 60 * 24))
				{
					if (banChatCount >= banChatCountPerDay)
					{
						activeChar.sendMessage("В �?утки, вы можете выдат�? не более " + banChatCount + " банов чата.");
						return false;
					}
				}
				else
				{
					int bonus_mod = banChatCount / 10;
					bonus_mod = Math.max(1, bonus_mod);
					bonus_mod = 1;
					if ((activeChar.getPlayerAccess().BanChatBonusId > 0) && (activeChar.getPlayerAccess().BanChatBonusCount > 0))
					{
						int add_count = activeChar.getPlayerAccess().BanChatBonusCount * bonus_mod;
						ItemTemplate item = ItemHolder.getInstance().getTemplate(activeChar.getPlayerAccess().BanChatBonusId);
						activeChar.sendMessage("Бону�? за модерирование: " + add_count + " " + item.getName());
						if (penaltyCount > 0)
						{
							activeChar.sendMessage("Штраф за нару�?ени�?: " + penaltyCount + " " + item.getName());
							activeChar.setVar("penaltyChatCount", "" + Math.max(0, penaltyCount - add_count), -1);
							add_count -= penaltyCount;
						}
						if (add_count > 0)
						{
							ItemFunctions.addItem(activeChar, activeChar.getPlayerAccess().BanChatBonusId, add_count, true);
						}
					}
					activeChar.setVar("LastBanChatDayTime", "" + System.currentTimeMillis(), -1);
					activeChar.setVar("banChatCount", "0", -1);
					banChatCount = 0;
				}
			}
			else
			{
				activeChar.setVar("LastBanChatDayTime", "" + System.currentTimeMillis(), -1);
			}
		}
		switch (command)
		{
			case admin_nochannel:
			case admin_nc:
			{
				if (wordList.length < 2)
				{
					activeChar.sendMessage("USAGE: //nochannel charName [period] [reason]");
					return false;
				}
				int timeval = 30;
				if (wordList.length > 2)
				{
					try
					{
						timeval = Integer.parseInt(wordList[2]);
					}
					catch (Exception E)
					{
						timeval = 30;
					}
				}
				String msg = AdminFunctions.banChat(activeChar, null, wordList[1], timeval, wordList.length > 3 ? Util.joinStrings(" ", wordList, 3) : null);
				activeChar.sendMessage(msg);
				if ((banChatCountPerDay > -1) && msg.startsWith("Вы забанили чат"))
				{
					banChatCount++;
					activeChar.setVar("banChatCount", "" + banChatCount, -1);
					activeChar.sendMessage("У ва�? о�?тало�?�? " + (banChatCountPerDay - banChatCount) + " банов чата.");
				}
			}
		}
		return true;
	}
	
	/**
	 * Method getAdminCommandEnum.
	 * @return Enum[]
	 * @see lineage2.gameserver.handler.admincommands.IAdminCommandHandler#getAdminCommandEnum()
	 */
	@Override
	public Enum<?>[] getAdminCommandEnum()
	{
		return Commands.values();
	}
}
