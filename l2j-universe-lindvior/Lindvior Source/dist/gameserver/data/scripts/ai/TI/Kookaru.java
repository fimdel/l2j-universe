package ai.TI;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.Location;

public class Kookaru extends DefaultAI {

    public Kookaru(NpcInstance actor) {
        super(actor);
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    static final Location[] _points = {
            new Location(-109752, 246920, -3011),
            new Location(-109496, 245656, -2955),
            new Location(-108712, 243896, -2912),
            new Location(-107240, 241160, -2230),
            new Location(-107112, 240376, -2120),
            new Location(-106904, 239464, -2150),
            new Location(-107080, 238296, -2384),
            new Location(-107720, 237400, -2568),
            new Location(-109144, 237448, -2967),};

    private int _lastPoint = 0;
    private boolean _firstThought = true;

    @Override
    protected boolean thinkActive() {
        if (super.thinkActive())
            return true;

        if (!getActor().isMoving)
            startMoveTask();

        return true;
    }

    private void startMoveTask() {
        NpcInstance actor = getActor();
        if (_firstThought) {
            _lastPoint = getIndex(Location.findNearest(actor, _points));
            _firstThought = false;
        } else
            _lastPoint++;
        if (_lastPoint >= _points.length) {
            _lastPoint = 0;
            //	actor.broadcastPacket(new ExShowScreenMessage(NpcString.TOO_BAD_TRY_AGAIN, 5000, ExShowScreenMessage.ScreenMessageAlign.TOP_CENTER, true, 1, 0, true));
            actor.deleteMe();
        }
        actor.setRunning();
        addTaskMove(Location.findPointToStay(_points[_lastPoint], 250, actor.getGeoIndex()), true);
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
}

