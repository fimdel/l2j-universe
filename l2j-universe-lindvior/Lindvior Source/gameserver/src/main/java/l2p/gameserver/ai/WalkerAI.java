package l2p.gameserver.ai;

import l2p.commons.util.Rnd;
import l2p.gameserver.geodata.GeoEngine;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.spawn.WalkerRouteTemplate;
import l2p.gameserver.templates.spawn.WalkerRouteTemplate.Route;
import l2p.gameserver.templates.spawn.WalkerRouteTemplate.RouteType;
import l2p.gameserver.utils.Location;

/**
 * АИ для системы хождения,брожения,дрочевания бешеных неписей.
 * Думаю пару типов для начала впилим, потом посмотрим на развитие событий :)
 * LINEAR - Линейнойе хождение (от первой к посл. и с посл. до первой)
 * CYCLE - Цикличность, само собой (от первой к посл. и с посл. поиск первой)
 * TELEPORT - ТП при роуте к посл. точке
 * RANDOM - Брожение в рандомные точки (аля RANDOM_WALK)
 * <p/>
 * Идея - Youri
 */
public class WalkerAI extends DefaultAI {
    private int _routeIndex = 0;
    private short _direction = 1;
    private long _lastMove = 0;

    public WalkerAI(NpcInstance actor) {
        super(actor);
        setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
    }

    @Override
    protected boolean thinkActive() {
        WalkerRouteTemplate routeTemplate = getActor().getWalkerRouteTemplate();

        if (routeTemplate == null)
            return false;

        boolean LINEAR = (routeTemplate.getRouteType() == RouteType.LINEAR);
        boolean CYCLE = (routeTemplate.getRouteType() == RouteType.CYCLE);
        boolean TELEPORT = (routeTemplate.getRouteType() == RouteType.TELEPORT);
        boolean RANDOM = (routeTemplate.getRouteType() == RouteType.RANDOM);

        if (routeTemplate.getIsRunning())
            getActor().setRunning();

        int pointsCount = routeTemplate.getPointsCount();
        if (pointsCount <= 0)
            return false;

        Route point = null;
        int oldIndex = _routeIndex;

        if ((_routeIndex + _direction) >= pointsCount || (_routeIndex + _direction) < 0) {
            if (LINEAR) {
                _direction *= -1;
                _routeIndex += _direction;
                point = routeTemplate.getPoints().get(_routeIndex);
            } else if (CYCLE) {
                _direction = 1;
                _routeIndex = 0;
                point = routeTemplate.getPoints().get(_routeIndex);
            } else if (TELEPORT) {
                _direction = 1;
                _routeIndex = 0;
                point = routeTemplate.getPoints().get(_routeIndex);
            } else if (RANDOM) {
                randomWalk();
            }
        } else {
            _routeIndex += _direction;
            point = routeTemplate.getPoints().get(_routeIndex);
        }

        Location nextLoc = point.getLoc();
        long delay = (point.getDelay() <= 0) ? routeTemplate.getDelay() : point.getDelay();

        if (_lastMove == 0) {
            _lastMove = System.currentTimeMillis() + delay;
            _routeIndex = oldIndex;
            return false;
        } else if (getActor().isMoving) {
            _routeIndex = oldIndex;
            return false;
        } else if (RANDOM)
            return false;

        else if (System.currentTimeMillis() - _lastMove > delay) {
            getActor().moveToLocation(nextLoc, 0, true);
            _lastMove = System.currentTimeMillis();
            if (TELEPORT & point.getLastPoint()) {
                getActor().teleToLocation(nextLoc);
                _lastMove = System.currentTimeMillis();
            }
        }

        return true;
    }

    @Override
    protected boolean randomWalk() {
        WalkerRouteTemplate routeTemplate = getActor().getWalkerRouteTemplate();

        int AI_WALK_RANGE = routeTemplate.getWalkRange();

        if (AI_WALK_RANGE == 0 | routeTemplate.getRouteType() == RouteType.RANDOM)
            return false;

        NpcInstance actor = getActor();
        if (actor == null)
            return false;

        Location sloc = actor.getSpawnedLoc();

        int x = sloc.x + Rnd.get(2 * AI_WALK_RANGE) - AI_WALK_RANGE;
        int y = sloc.y + Rnd.get(2 * AI_WALK_RANGE) - AI_WALK_RANGE;
        int z = GeoEngine.getHeight(x, y, sloc.z, actor.getGeoIndex());

        actor.setRunning();
        actor.moveToLocation(x, y, z, 0, true);

        return true;
    }
}