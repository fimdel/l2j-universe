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

import java.text.SimpleDateFormat;

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.handler.usercommands.IUserCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.utils.Strings;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class ClanPenalty implements IUserCommandHandler
{
	/**
	 * Field COMMAND_IDS.
	 */
	private static final int[] COMMAND_IDS =
	{
		100,
		114
	};
	
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
		long leaveClan = 0;
		if (activeChar.getLeaveClanTime() != 0)
		{
			leaveClan = activeChar.getLeaveClanTime() + (1 * 24 * 60 * 60 * 1000L);
		}
		long deleteClan = 0;
		if (activeChar.getDeleteClanTime() != 0)
		{
			deleteClan = activeChar.getDeleteClanTime() + (10 * 24 * 60 * 60 * 1000L);
		}
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String html = HtmCache.getInstance().getNotNull("command/penalty.htm", activeChar);
		if (activeChar.getClanId() == 0)
		{
			if ((leaveClan == 0) && (deleteClan == 0))
			{
				html = html.replaceFirst("%reason%", "No penalty is imposed.");
				html = html.replaceFirst("%expiration%", " ");
			}
			else if ((leaveClan > 0) && (deleteClan == 0))
			{
				html = html.replaceFirst("%reason%", "Penalty for leaving clan.");
				html = html.replaceFirst("%expiration%", format.format(leaveClan));
			}
			else if (deleteClan > 0)
			{
				html = html.replaceFirst("%reason%", "Penalty for dissolving clan.");
				html = html.replaceFirst("%expiration%", format.format(deleteClan));
			}
		}
		else if (activeChar.getClan().canInvite())
		{
			html = html.replaceFirst("%reason%", "No penalty is imposed.");
			html = html.replaceFirst("%expiration%", " ");
		}
		else
		{
			html = html.replaceFirst("%reason%", "Penalty for expelling clan member.");
			html = html.replaceFirst("%expiration%", format.format(activeChar.getClan().getExpelledMemberTime()));
		}
		NpcHtmlMessage msg = new NpcHtmlMessage(5);
		msg.setHtml(Strings.bbParse(html));
		activeChar.sendPacket(msg);
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
