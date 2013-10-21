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

import lineage2.gameserver.handler.usercommands.IUserCommandHandler;
import lineage2.gameserver.instancemanager.MapRegionManager;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.templates.mapregion.RestartArea;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Loc implements IUserCommandHandler
{
	/**
	 * Field COMMAND_IDS.
	 */
	private static final int[] COMMAND_IDS =
	{
		0
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
		RestartArea ra = MapRegionManager.getInstance().getRegionData(RestartArea.class, activeChar);
		int msgId = ra != null ? ra.getRestartPoint().get(activeChar.getRace()).getMsgId() : 0;
		if (msgId > 0)
		{
			activeChar.sendPacket(new SystemMessage(msgId).addNumber(activeChar.getX()).addNumber(activeChar.getY()).addNumber(activeChar.getZ()));
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessage.S1).addString("Current location : " + activeChar.getX() + ", " + activeChar.getY() + ", " + activeChar.getZ()));
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
