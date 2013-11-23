/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import instances.BaltusKnight;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ReflectionUtils;

public final class NewAntarasInstance extends NpcInstance {
    private static final int AntarInstance = 183;

    public NewAntarasInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("enter")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(AntarInstance))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(AntarInstance)) {
                ReflectionUtils.enterReflection(player, new BaltusKnight(), AntarInstance);
            }
        } else
            super.onBypassFeedback(player, command);
    }
}
