package ai.octavis;


import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.Location;

public class OctavisRider extends Pointer {

    public OctavisRider(NpcInstance actor) {
        super(actor);
        AI_TASK_ACTIVE_DELAY = 250;

        _points = new Location[]{
                new Location(181911, 114835, -7678),
                new Location(182824, 114808, -7906),
                new Location(182536, 115224, -7836),
                new Location(182104, 115160, -7735),
                new Location(181480, 114936, -7702)};
    }

}