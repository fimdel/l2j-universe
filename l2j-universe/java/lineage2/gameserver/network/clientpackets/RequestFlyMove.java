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
package lineage2.gameserver.network.clientpackets;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.ExFlyMove;
import lineage2.gameserver.network.serverpackets.ExFlyMoveBroadcast;
import lineage2.gameserver.templates.jump.JumpPoint;
import lineage2.gameserver.templates.jump.JumpTrack;
import lineage2.gameserver.templates.jump.JumpWay;
import lineage2.gameserver.utils.Location;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class RequestFlyMove extends L2GameClientPacket
{
	/**
	 * Field _nextWayId.
	 */
	private int _nextWayId;
	
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
		_nextWayId = readD();
	}
	
	/**
	 * Method runImpl.
	 */
	@Override
	protected void runImpl()
	{
		Player activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		JumpWay way = activeChar.getCurrentJumpWay();
		if (way == null)
		{
			activeChar.onJumpingBreak();
			return;
		}
		JumpPoint point = way.getJumpPoint(_nextWayId);
		if (point == null)
		{
			activeChar.onJumpingBreak();
			return;
		}
		Location destLoc = point.getLocation();
		activeChar.broadcastPacketToOthers(new ExFlyMoveBroadcast(activeChar, 2, destLoc));
		activeChar.setLoc(destLoc);
		JumpTrack track = activeChar.getCurrentJumpTrack();
		if (track == null)
		{
			activeChar.onJumpingBreak();
			return;
		}
		JumpWay nextWay = track.getWay(_nextWayId);
		if (nextWay == null)
		{
			activeChar.onJumpingBreak();
			return;
		}
		activeChar.sendPacket(new ExFlyMove(activeChar.getObjectId(), nextWay.getPoints(), track.getId()));
		activeChar.setCurrentJumpWay(nextWay);
	}
}
