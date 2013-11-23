/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import instances.FortunaInstance;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ReflectionUtils;

public final class IzaelManagerInstance extends NpcInstance {
    private static final int fortunaId = 179;

    public IzaelManagerInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this)) {
            return;
        }
        if (command.equalsIgnoreCase("enter")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(fortunaId))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(fortunaId)) {
                ReflectionUtils.enterReflection(player, new FortunaInstance(), fortunaId);
            }
        }
        super.onBypassFeedback(player, command);
    }
}