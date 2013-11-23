package l2p.gameserver.templates.jump;

import l2p.gameserver.utils.Location;

/**
 * @author K1mel
 * @twitter http://twitter.com/k1mel_developer
 */
public class JumpPoint {
    private final Location _loc;
    private final int _nextWayId;

    public JumpPoint(Location loc, int nextWayId) {
        _loc = loc;
        _nextWayId = nextWayId;
    }

    public Location getLocation() {
        return _loc;
    }

    public int getNextWayId() {
        return _nextWayId;
    }
}