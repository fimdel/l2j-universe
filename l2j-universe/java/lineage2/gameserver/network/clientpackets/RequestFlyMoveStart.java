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

import lineage2.gameserver.data.xml.holder.JumpTracksHolder;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.Zone.ZoneType;
import lineage2.gameserver.network.serverpackets.ExFlyMove;
import lineage2.gameserver.network.serverpackets.ExFlyMoveBroadcast;
import lineage2.gameserver.network.serverpackets.FlyToLocation;
import lineage2.gameserver.templates.jump.JumpTrack;
import lineage2.gameserver.templates.jump.JumpWay;
import lineage2.gameserver.utils.Location;

public class RequestFlyMoveStart extends L2GameClientPacket
{
	/**
	 * Method readImpl.
	 */
	@Override
	protected void readImpl()
	{
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
		Zone zone = activeChar.getZone(ZoneType.JUMPING);
		if (zone == null)
		{
			return;
		}
		JumpTrack track = JumpTracksHolder.getInstance().getTrack(zone.getTemplate().getJumpTrackId());
		if (track == null)
		{
			return;
		}
		Location destLoc = track.getStartLocation();
		activeChar.sendPacket(new FlyToLocation(activeChar, destLoc, FlyToLocation.FlyType.DUMMY, 0));
		JumpWay way = track.getWay(0);
		if (way == null)
		{
			return;
		}
		activeChar.sendPacket(new ExFlyMove(activeChar.getObjectId(), way.getPoints(), track.getId()));
		activeChar.broadcastPacketToOthers(new ExFlyMoveBroadcast(activeChar, 2, destLoc));
		activeChar.setVar("@safe_jump_loc", activeChar.getLoc().toXYZString(), -1);
		activeChar.setCurrentJumpTrack(track);
		activeChar.setCurrentJumpWay(way);
	}
}
