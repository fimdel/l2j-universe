package npc.model;

import instances.AltarShilen;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Awakeninger
 */

public final class SnonePortalInstance extends NpcInstance {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int VullockInstance = 193;

    public SnonePortalInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("start")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(VullockInstance))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(VullockInstance)) {
                ReflectionUtils.enterReflection(player, new AltarShilen(), VullockInstance);
            }
        }else
            super.onBypassFeedback(player, command);
    }
}