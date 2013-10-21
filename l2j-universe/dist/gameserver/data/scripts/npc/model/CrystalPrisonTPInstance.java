package npc.model;

import instances.Vullock;
import instances.Baylor;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Awakeninger
 */

public final class CrystalPrisonTPInstance extends NpcInstance {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int VullockInstance = 167;
    private static final int BaylorInstance = 168;

    public CrystalPrisonTPInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.startsWith("request_vallock")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(VullockInstance))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(VullockInstance)) {
                ReflectionUtils.enterReflection(player, new Vullock(), VullockInstance);
            }
        } else if (command.equalsIgnoreCase("request_Baylor")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(BaylorInstance))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(BaylorInstance)) {
                ReflectionUtils.enterReflection(player, new Baylor(), BaylorInstance);
            }
        }else
            super.onBypassFeedback(player, command);
    }
}