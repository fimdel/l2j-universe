package l2p.gameserver.network.clientpackets;

import l2p.gameserver.data.xml.holder.JumpTracksHolder;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.Zone.ZoneType;
import l2p.gameserver.network.serverpackets.ExFlyMove;
import l2p.gameserver.network.serverpackets.ExFlyMoveBroadcast;
import l2p.gameserver.network.serverpackets.FlyToLocation;
import l2p.gameserver.templates.jump.JumpTrack;
import l2p.gameserver.templates.jump.JumpWay;
import l2p.gameserver.utils.Location;

/**
 * @author K1mel
 * @twitter http://twitter.com/k1mel_developer
 */
public class RequestFlyMoveStart extends L2GameClientPacket {
    protected void readImpl() {
        //
    }

    protected void runImpl() {
        Player activeChar = getClient().getActiveChar();
        if (activeChar == null)
            return;

        Zone zone = activeChar.getZone(ZoneType.JUMPING);
        if (zone == null)
            return;

        JumpTrack track = JumpTracksHolder.getInstance().getTrack(zone.getTemplate().getJumpTrackId());
        if (track == null)
            return;

        Location destLoc = track.getStartLocation();
        activeChar.sendPacket(new FlyToLocation(activeChar, destLoc, FlyToLocation.FlyType.DUMMY, 0));

        JumpWay way = track.getWay(0);
        if (way == null)
            return;

        activeChar.sendPacket(new ExFlyMove(activeChar.getObjectId(), way.getPoints(), track.getId()));
        activeChar.broadcastPacketToOthers(new ExFlyMoveBroadcast(activeChar, 2, destLoc));
        activeChar.setVar("@safe_jump_loc", activeChar.getLoc().toXYZString(), -1);
        activeChar.setCurrentJumpTrack(track);
        activeChar.setCurrentJumpWay(way);
    }
}