package l2p.gameserver.model.entity.boat;

import gnu.trove.TIntObjectHashMap;
import l2p.commons.threading.RunnableImpl;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.model.Playable;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.events.impl.ShuttleWayEvent;
import l2p.gameserver.network.serverpackets.*;
import l2p.gameserver.templates.ShuttleTemplate;
import l2p.gameserver.utils.Location;

/**
 * @author Bonux
 */
public class Shuttle extends Boat {
    private static class Docked extends RunnableImpl {
        private Shuttle _shuttle;

        public Docked(Shuttle shuttle) {
            _shuttle = shuttle;
        }

        public void runImpl() {
            if (_shuttle == null)
                return;

            _shuttle.getCurrentFloor().stopEvent();
            _shuttle.getNextFloor().reCalcNextTime(false);
        }
    }

    private static final long serialVersionUID = 1L;

    private final TIntObjectHashMap<ShuttleWayEvent> _floors = new TIntObjectHashMap<ShuttleWayEvent>();

    private boolean _moveBack;
    public int _currentWay;

    public Shuttle(int objectId, ShuttleTemplate template) {
        super(objectId, template);
        setFlying(true);
    }

    @Override
    public int getBoatId() {
        return getTemplate().getId();
    }

    @Override
    public final ShuttleTemplate getTemplate() {
        return (ShuttleTemplate) _template;
    }

    @Override
    public void onSpawn() {
        _moveBack = false;
        _currentWay = 0;

        getCurrentFloor().reCalcNextTime(false);
    }

    @Override
    public void onEvtArrived() {
        ThreadPoolManager.getInstance().schedule(new Docked(this), 1500L);
    }

    @Override
    public L2GameServerPacket infoPacket() {
        return new ExShuttleInfoPacket(this);
    }

    @Override
    public L2GameServerPacket movePacket() {
        return new ExShuttleMovePacket(this);
    }

    @Override
    public L2GameServerPacket inMovePacket(Player player, Location src, Location desc) {
        return new ExMTLInShuttlePacket(player, this, src, desc);
    }

    @Override
    public L2GameServerPacket stopMovePacket() {
        return null;
    }

    @Override
    public L2GameServerPacket inStopMovePacket(Player player) {
        return new ExStopMoveInShuttlePacket(player);
    }

    @Override
    public L2GameServerPacket startPacket() {
        return null;
    }

    @Override
    public L2GameServerPacket checkLocationPacket() {
        return null;
    }

    @Override
    public L2GameServerPacket validateLocationPacket(Player player) {
        return new ExValidateLocationInShuttlePacket(player);
    }

    @Override
    public L2GameServerPacket getOnPacket(Playable playable, Location location) {
        return new ExShuttleGetOnPacket(playable, this, location);
    }

    @Override
    public L2GameServerPacket getOffPacket(Playable playable, Location location) {
        return new ExShuttleGetOffPacket(playable, this, location);
    }

    @Override
    public boolean isShuttle() {
        return true;
    }

    @Override
    public void oustPlayers() {
        //
    }

    @Override
    public void trajetEnded(boolean oust) {
        //
    }

    @Override
    public void teleportShip(int x, int y, int z) {
        //
    }

    @Override
    public Location getReturnLoc() {
        return getCurrentFloor().getReturnLoc();
    }

    public void addFloor(ShuttleWayEvent floor) {
        _floors.put((floor.getId() % 100), floor);
    }

    public ShuttleWayEvent getCurrentFloor() {
        return _floors.get(_currentWay);
    }

    private ShuttleWayEvent getNextFloor() {
        int floors = _floors.size() - 1;
        if (!_moveBack) {
            _currentWay++;
            if (_currentWay > floors) {
                _currentWay = floors - 1;
                _moveBack = true;
            }
        } else {
            _currentWay--;
            if (_currentWay < 0) {
                _currentWay = 1;
                _moveBack = false;
            }
        }
        return _floors.get(_currentWay);
    }
}
