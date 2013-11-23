package l2p.gameserver.model;

import l2p.gameserver.utils.Location;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 21.08.12
 * Time: 8:11
 */
@SuppressWarnings("serial")
public class RouteLocation extends Location {

    private final int _name;

    public RouteLocation(int name) {
        _name = name;
    }

    public int getName() {
        return _name;
    }
}
