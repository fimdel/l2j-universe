package ai.TI;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.Location;


public class Running extends DefaultAI {

    // TODO: Для каждого нпц СВОИ точки!!
    // Этот класс - как супер класс для наследников...
    protected Location[] _points = {
            new Location(-115997, 260727, -2000),
            new Location(-110648, 260727, -2000),
            new Location(-110648, 252110, -2000),
            new Location(-115997, 252110, -2000),
    };

    protected int radius;

    private int _lastPoint = 0;
    private boolean _firstThought = true;

    public Running(NpcInstance actor) {
        super(actor);
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    @Override
    protected boolean thinkActive() {
        if (super.thinkActive())
            return true;

        if (!getActor().isMoving)
            startMoveTask();

        return true;
    }

    @Override
    protected void onEvtArrived() {
        startMoveTask();
        super.onEvtArrived();
    }

    private void startMoveTask() {
        NpcInstance npc = getActor();
        if (_firstThought) {
            _lastPoint = getIndex(Location.findNearest(npc, _points));
            _firstThought = false;
        } else
            _lastPoint++;
        if (_lastPoint >= _points.length) {
            _lastPoint = 0;
        }
        if (radius != 0)
            addTaskMove(Location.findPointToStay(_points[_lastPoint], radius, npc.getGeoIndex()), true);
        else
            addTaskMove(Location.findPointToStay(_points[_lastPoint], 500, npc.getGeoIndex()), true);
        doTask();
    }

    private int getIndex(Location loc) {
        for (int i = 0; i < _points.length; i++)
            if (_points[i] == loc)
                return i;
        return 0;
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }

    @Override
    protected boolean maybeMoveToHome() {
        return false;
    }

    @Override
    protected void teleportHome() {
    }

    @Override
    protected void returnHome(boolean clearAggro, boolean teleport) {
        super.returnHome(clearAggro, teleport);
        clearTasks();
        _firstThought = true;
        startMoveTask();
    }
}
