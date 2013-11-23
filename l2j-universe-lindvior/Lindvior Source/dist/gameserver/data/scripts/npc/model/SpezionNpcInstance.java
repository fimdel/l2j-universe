/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import instances.SpezionNormal;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ReflectionUtils;

public final class SpezionNpcInstance extends NpcInstance {
    public SpezionNpcInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("normal_spezion")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(159))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(159))
                ReflectionUtils.enterReflection(player, new SpezionNormal(), 159);
        } else
            super.onBypassFeedback(player, command);
    }
}
