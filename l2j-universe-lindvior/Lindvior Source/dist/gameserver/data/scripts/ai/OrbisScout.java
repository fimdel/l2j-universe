/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package ai;

import l2p.gameserver.ai.CtrlIntention;
import l2p.gameserver.ai.Fighter;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.spawn.WalkerRouteTemplate;
import l2p.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrbisScout extends Fighter {
    private static final Logger _log = LoggerFactory.getLogger(OrbisScout.class);
    private int _routeIndex = 0;
    private short _direction = 1;
    private long _lastMove = 0;
    private Creature player;
    private NpcInstance actor;
    //private Location _spawnedLoc = new Location();
    private int current_point = -1;
    //private Location loc = actor.getSpawnedLoc();
    private Player character;

    public OrbisScout(NpcInstance actor) {
        super(actor);
        setIntention(CtrlIntention.AI_INTENTION_ACTIVE);


    }

    @Override
    protected boolean thinkActive() {
        WalkerRouteTemplate routeTemplate = getActor().getWalkerRouteTemplate();

        if (routeTemplate == null)
            return false;

        if (character != null && player.isInRange(actor, 300)) {
            _log.info("ATTACK");
            setIntention(CtrlIntention.AI_INTENTION_ATTACK, player, 1);
            return false;
        }

        //if (player.isPlayer() && player.isInRange(loc, 300))
        //{

        //	_log.info("ATTACK");
        //	changeIntention(CtrlIntention.AI_INTENTION_ATTACK, player, null);
        //	//setIntention(CtrlIntention.AI_INTENTION_ATTACK);
        //	}

        boolean LINEAR = (routeTemplate.getRouteType() == WalkerRouteTemplate.RouteType.LINEAR);
        boolean CYCLE = (routeTemplate.getRouteType() == WalkerRouteTemplate.RouteType.CYCLE);
        boolean TELEPORT = (routeTemplate.getRouteType() == WalkerRouteTemplate.RouteType.TELEPORT);

        if (routeTemplate.getIsRunning())
            getActor().setRunning();

        int pointsCount = routeTemplate.getPointsCount();
        if (pointsCount <= 0)
            return false;

        WalkerRouteTemplate.Route point = null;
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
        } else if (System.currentTimeMillis() - _lastMove > delay) {
            if (TELEPORT & point.getLastPoint()) {
                getActor().teleToLocation(nextLoc);
                _lastMove = System.currentTimeMillis();
            }
            getActor().moveToLocation(nextLoc, 0, true);
            _lastMove = System.currentTimeMillis();
        }

        return true;
    }

    @Override
    protected boolean createNewTask() {
        return defaultFightTask();
    }

    @Override
    public boolean checkAggression(Creature target) {
        return super.checkAggression(target);
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    @Override
    protected void thinkAttack() {

        checkAggression(player);

        super.thinkAttack();
    }
}