/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import instances.Bergamo;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ReflectionUtils;

public final class DimensionalInstance extends NpcInstance {

    public DimensionalInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.startsWith("start")) {
            this.decayMe();
            this.doDie(null);
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(212))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(212))
                ReflectionUtils.enterReflection(player, new Bergamo(), 212);
        } else
            super.onBypassFeedback(player, command);
    }
}
