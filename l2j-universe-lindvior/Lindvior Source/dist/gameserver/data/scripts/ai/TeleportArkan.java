package ai;

import l2p.gameserver.ai.DefaultAI;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.World;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.utils.Location;

public class TeleportArkan extends DefaultAI {
    public TeleportArkan(NpcInstance actor) {
        super(actor);
        this.AI_TASK_ACTIVE_DELAY = 1000;
    }

    @Override
    protected boolean thinkActive() {
        NpcInstance actor = getActor();
        if (actor == null)
            return true;

        for (Player player : World.getAroundPlayers(actor, 200, 200)) {
            if (player != null) {
                player.teleToLocation(new Location(207559, 86429, -1000));
            }
        }
        return true;
    }

    @Override
    public boolean isGlobalAI() {
        return true;
    }
}
