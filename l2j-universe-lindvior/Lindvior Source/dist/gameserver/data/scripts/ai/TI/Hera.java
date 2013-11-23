package ai.TI;

import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.Location;

public class Hera extends Running {

    public Hera(NpcInstance actor) {
        super(actor);
        _points = new Location[]{
                new Location(-114309, 257329, -1136),
                new Location(-114890, 256936, -1136),
                new Location(-114525, 254428, -1280),
                new Location(-114184, 255042, -1528),
                new Location(-114525, 254428, -1280),
                new Location(-114890, 256936, -1136),
                new Location(-114309, 257329, -1136)
        };
        radius = 1;
        this.AI_TASK_ACTIVE_DELAY = 2000; //2sec
    }

    @Override
    protected boolean randomWalk() {
        return false;
    }
}