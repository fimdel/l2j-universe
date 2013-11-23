package ai;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.Location;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 10.10.12
 * Time: 21:33
 */
public class BloodySwampland extends DefaultAI {

    private NpcInstance npc;

    public BloodySwampland(NpcInstance actor) {
        super(actor);
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }

    @Override
    protected void onEvtSpawn() {
        npc.setSpawnedLoc((new Location(-23191, 53554, -3680, 5514)));
        npc.setSpawnedLoc((new Location(-20716, 51912, -3672, 29923)));
        npc.setSpawnedLoc((new Location(-14609, 44054, -3632, 34294)));
    }
}
