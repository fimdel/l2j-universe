package l2p.gameserver.templates.spawn;

import l2p.gameserver.utils.Location;

import java.util.ArrayList;
import java.util.List;


public class WalkerRouteTemplate {

    public enum RouteType {
        CYCLE(0),
        LINEAR(1),
        RANDOM(2),
        TELEPORT(3);

        int _id;

        RouteType(int id) {
            _id = id;
        }
    }

    ;

    public class Route {
        private int _x, _y, _z, _h;
        private long _delay;
        private Location _loc;
        private boolean _end = false;

        public Route(int x, int y, int z, int h, long delay, boolean end) {
            _x = x;
            _y = y;
            _z = z;
            _h = h;
            _delay = delay;
            _end = end;
        }

        public Route(int x, int y, int z) {
            this(x, y, z, 0, 0, false);
        }

        public Route(int x, int y, int z, int h) {
            this(x, y, z, h, 0, false);
        }

        public Route(int x, int y, int z, long delay) {
            this(x, y, z, 0, delay, false);
        }

        public Route(int x, int y, int z, long delay, boolean end) {
            this(x, y, z, 0, delay, end);
        }

        public Location getLoc() {
            if (_loc == null)
                _loc = new Location(_x, _y, _z, _h);
            return _loc;
        }

        public int getHeading() {
            return _h;
        }

        public long getDelay() {
            return _delay;
        }

        public boolean getLastPoint() {
            return _end;
        }
    }

    private int _npcId;
    private int _walkRange;
    private long _delay;
    private RouteType _type;
    private List<Route> _routes = new ArrayList<Route>();
    private boolean _isRunning = false;

    public WalkerRouteTemplate(int npcId, long delay, RouteType type, boolean isRunning, int walkRange) {
        _npcId = npcId;
        _delay = delay;
        _type = type;
        _isRunning = isRunning;
        _walkRange = walkRange;
    }

    public void setRoute(int x, int y, int z, int h, long delay, boolean end) {
        _routes.add(new Route(x, y, z, h, delay, end));
    }

    public void setRoute(int x, int y, int z) {
        setRoute(x, y, z, 0, 0, false);
    }

    public void setRoute(Route route) {
        setRoute(route);
    }

    public int getNpcId() {
        return _npcId;
    }

    public int getWalkRange() {
        return _walkRange;
    }

    public long getDelay() {
        return _delay;
    }

    public RouteType getRouteType() {
        return _type;
    }

    public List<Route> getPoints() {
        return _routes;
    }

    public int getPointsCount() {
        return _routes.size();
    }

    public boolean getIsRunning() {
        return _isRunning;
    }
}
