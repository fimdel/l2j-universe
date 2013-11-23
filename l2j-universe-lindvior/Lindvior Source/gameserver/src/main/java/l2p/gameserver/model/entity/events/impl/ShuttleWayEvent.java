package l2p.gameserver.model.entity.events.impl;

import gnu.trove.TIntArrayList;
import l2p.commons.collections.MultiValueSet;
import l2p.gameserver.data.BoatHolder;
import l2p.gameserver.model.entity.boat.Shuttle;
import l2p.gameserver.model.entity.events.GlobalEvent;
import l2p.gameserver.utils.Location;

/**
 * @author Bonux
 */
public class ShuttleWayEvent extends GlobalEvent {
    private final Shuttle _shuttle;
    private final Location _nextFloorLoc;
    private final TIntArrayList _floorDoorsId = new TIntArrayList();
    private final int _speed;
    private final Location _returnLoc;

    public ShuttleWayEvent(MultiValueSet<String> set) {
        super(set);
        int shuttleId = set.getInteger("shuttle_id", -1);
        if (shuttleId > 0) {
            _shuttle = BoatHolder.getInstance().initShuttle(getName(), shuttleId);
            Location loc = Location.parseLoc(set.getString("spawn_point"));
            _shuttle.setLoc(loc, true);
            _shuttle.setHeading(loc.h);
        } else {
            //TODO: [Bonux] FIX IT!
            _shuttle = (Shuttle) BoatHolder.getInstance().getBoat(getName());
        }
        _nextFloorLoc = Location.parseLoc(set.getString("next_floor_loc"));
        _floorDoorsId.add(set.getIntegerArray("floor_doors_id"));
        _speed = set.getInteger("speed");
        _returnLoc = Location.parseLoc(set.getString("return_point"));
        _shuttle.addFloor(this);
    }

    @Override
    public void startEvent() {
        super.startEvent();
        _shuttle.setMoveSpeed(_speed);
        _shuttle.setRunState(1);
        _shuttle.broadcastCharInfo();
        _shuttle.moveToLocation(_nextFloorLoc.getX(), _nextFloorLoc.getY(), _nextFloorLoc.getZ(), 0, false);
    }

    @Override
    public void stopEvent() {
        super.stopEvent();
        _shuttle.setRunState(0);
        _shuttle.broadcastCharInfo();
    }

    @Override
    public void reCalcNextTime(boolean onInit) {
        if (onInit)
            return;

        clearActions();
        registerActions();
    }

    @Override
    protected long startTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    protected void printInfo() {
    }

    public boolean isThisFloorDoor(int doorId) {
        return _floorDoorsId.contains(doorId);
    }

    public Location getReturnLoc() {
        return _returnLoc;
    }
}
