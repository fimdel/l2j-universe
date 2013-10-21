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

import lineage2.gameserver.data.xml.holder.InstantZoneHolder;
import lineage2.gameserver.handler.usercommands.IUserCommandHandler;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class InstanceZone implements IUserCommandHandler
{
	/**
	 * Field COMMAND_IDS.
	 */
	private static final int[] COMMAND_IDS =
	{
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
		if (activeChar.getActiveReflection() != null)
		{
			activeChar.sendPacket(new SystemMessage2(SystemMsg.INSTANT_ZONE_CURRENTLY_IN_USE_S1).addInstanceName(activeChar.getActiveReflection().getInstancedZoneId()));
		}
		int limit;
		boolean noLimit = true;
		boolean showMsg = false;
		for (int i : activeChar.getInstanceReuses().keySet())
		{
			limit = InstantZoneHolder.getInstance().getMinutesToNextEntrance(i, activeChar);
			if (limit > 0)
			{
				noLimit = false;
				if (!showMsg)
				{
					activeChar.sendPacket(SystemMsg.INSTANCE_ZONE_TIME_LIMIT);
					showMsg = true;
				}
				activeChar.sendPacket(new SystemMessage2(SystemMsg.S1_WILL_BE_AVAILABLE_FOR_REUSE_AFTER_S2_HOURS_S3_MINUTES).addInstanceName(i).addInteger(limit / 60).addInteger(limit % 60));
			}
		}
		if (noLimit)
		{
			activeChar.sendPacket(SystemMsg.THERE_IS_NO_INSTANCE_ZONE_UNDER_A_TIME_LIMIT);
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
