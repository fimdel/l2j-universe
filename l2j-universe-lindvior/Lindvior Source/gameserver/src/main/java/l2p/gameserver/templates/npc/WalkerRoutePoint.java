/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.npc;

import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.utils.Location;

public class WalkerRoutePoint {
    private final Location _loc;
    private final NpcString _phrase;
    private final int _socialActionId;
    private final int _delay;
    private final boolean _running;

    public WalkerRoutePoint(Location loc, NpcString phrase, int socialActionId, int delay, boolean running) {
        this._loc = loc;
        this._phrase = phrase;
        this._socialActionId = socialActionId;
        this._delay = delay;
        this._running = running;
    }

    public Location getLocation() {
        return this._loc;
    }

    public NpcString getPhrase() {
        return this._phrase;
    }

    public int getSocialActionId() {
        return this._socialActionId;
    }

    public int getDelay() {
        return this._delay;
    }

    public boolean isRunning() {
        return this._running;
    }
}
