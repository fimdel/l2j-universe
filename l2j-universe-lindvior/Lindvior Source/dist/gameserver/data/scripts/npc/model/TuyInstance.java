/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import instances.Nursery;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.ReflectionUtils;

public final class TuyInstance extends NpcInstance {
    private static final Location LOC_OUT = new Location(-178465, 153685, 2488);

    public TuyInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("start")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(171))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(171)) {
                ReflectionUtils.enterReflection(player, new Nursery(), 171);
            }
        } else if (command.equalsIgnoreCase("reward")) {  //TODO перенести в аи.
            if (Nursery.reward >= 1 || Nursery.reward <= 800) {
                ItemFunctions.addItem(player, 17602, 10, true);
                player.teleToLocation(LOC_OUT);
            } else if (Nursery.reward >= 801 || Nursery.reward <= 1600) {
                ItemFunctions.addItem(player, 17602, 60, true);
                player.teleToLocation(LOC_OUT);
            } else if (Nursery.reward >= 1601 || Nursery.reward <= 2000) {
                ItemFunctions.addItem(player, 17602, 160, true);
                player.teleToLocation(LOC_OUT);
            } else if (Nursery.reward >= 2001 || Nursery.reward <= 2400) {
                ItemFunctions.addItem(player, 17602, 200, true);
                player.teleToLocation(LOC_OUT);
            } else if (Nursery.reward >= 2401 || Nursery.reward <= 2800) {
                ItemFunctions.addItem(player, 17602, 240, true);
                player.teleToLocation(LOC_OUT);
            } else if (Nursery.reward >= 2801 || Nursery.reward <= 3200) {
                ItemFunctions.addItem(player, 17602, 280, true);
                player.teleToLocation(LOC_OUT);
            } else if (Nursery.reward >= 3201 || Nursery.reward <= 3600) {
                ItemFunctions.addItem(player, 17602, 320, true);
                player.teleToLocation(LOC_OUT);
            } else if (Nursery.reward >= 3601 || Nursery.reward <= 4000) {
                ItemFunctions.addItem(player, 17602, 360, true);
                player.teleToLocation(LOC_OUT);
            } else if (Nursery.reward >= 4001 || Nursery.reward <= 8000) {
                ItemFunctions.addItem(player, 17602, 400, true);
                player.teleToLocation(LOC_OUT);
            }
        } else
            super.onBypassFeedback(player, command);
    }
}
