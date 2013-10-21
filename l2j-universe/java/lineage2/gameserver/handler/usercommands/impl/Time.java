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
package lineage2.gameserver.handler.usercommands.impl;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lineage2.gameserver.Config;
import lineage2.gameserver.GameTimeController;
import lineage2.gameserver.handler.usercommands.IUserCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Time implements IUserCommandHandler
{
	/**
	 * Field COMMAND_IDS.
	 */
	private static final int[] COMMAND_IDS =
	{
		77
	};
	/**
	 * Field df.
	 */
	private static final NumberFormat df = NumberFormat.getInstance(Locale.ENGLISH);
	/**
	 * Field sf.
	 */
	private static final SimpleDateFormat sf = new SimpleDateFormat("H:mm");
	static
	{
		df.setMinimumIntegerDigits(2);
	}
	
	/**
	 * Method useUserCommand.
	 * @param id int
	 * @param activeChar Player
	 * @return boolean * @see lineage2.gameserver.handler.usercommands.IUserCommandHandler#useUserCommand(int, Player)
	 */
	@Override
	public boolean useUserCommand(int id, Player activeChar)
	{
		if (COMMAND_IDS[0] != id)
		{
			return false;
		}
		int h = GameTimeController.getInstance().getGameHour();
		int m = GameTimeController.getInstance().getGameMin();
		SystemMessage sm;
		if (GameTimeController.getInstance().isNowNight())
		{
			sm = new SystemMessage(SystemMessage.THE_CURRENT_TIME_IS_S1S2_IN_THE_NIGHT);
		}
		else
		{
			sm = new SystemMessage(SystemMessage.THE_CURRENT_TIME_IS_S1S2_IN_THE_DAY);
		}
		sm.addString(df.format(h)).addString(df.format(m));
		activeChar.sendPacket(sm);
		if (Config.ALT_SHOW_SERVER_TIME)
		{
			activeChar.sendMessage(new CustomMessage("usercommandhandlers.Time.ServerTime", activeChar, sf.format(new Date(System.currentTimeMillis()))));
		}
		return true;
	}
	
	/**
	 * Method getUserCommandList.
	 * @return int[] * @see lineage2.gameserver.handler.usercommands.IUserCommandHandler#getUserCommandList()
	 */
	@Override
	public final int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}
