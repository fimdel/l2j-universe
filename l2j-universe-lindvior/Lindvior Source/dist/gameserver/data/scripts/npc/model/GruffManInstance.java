package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ReflectionUtils;

/**
 * @author pchayka
 */

public final class GruffManInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = -3426830891517719757L;
    private static final int elcardiaIzId = 158;

    public GruffManInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("elcardia_enter")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(elcardiaIzId))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(elcardiaIzId))
                ReflectionUtils.enterReflection(player, elcardiaIzId);
        } else
            super.onBypassFeedback(player, command);
    }
}