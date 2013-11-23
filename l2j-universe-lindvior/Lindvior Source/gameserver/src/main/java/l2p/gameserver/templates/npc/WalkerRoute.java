/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.templates.npc;

import java.util.ArrayList;
import java.util.List;

public class WalkerRoute {
    private final int _id;
    private final WalkerRouteType _type;
    private final List<WalkerRoutePoint> _points = new ArrayList();

    public WalkerRoute(int id, WalkerRouteType type) {
        this._id = id;
        this._type = type;
    }

    public int getId() {
        return this._id;
    }

    public WalkerRouteType getType() {
        return this._type;
    }

    public void addPoint(WalkerRoutePoint route) {
        this._points.add(route);
    }

    public WalkerRoutePoint getPoint(int id) {
        return (WalkerRoutePoint) this._points.get(id);
    }

    public int size() {
        return this._points.size();
    }

    public boolean isValid() {
        if ((this._type == WalkerRouteType.DELETE) || (this._type == WalkerRouteType.FINISH)) {
            return size() > 0;
        }
        return size() > 1;
    }
}
