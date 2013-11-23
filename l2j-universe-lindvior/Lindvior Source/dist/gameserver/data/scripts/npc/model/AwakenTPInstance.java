/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 03.07.12
 * Time: 7:30
 * телепорт для переродившихся персоонажей
 */
@SuppressWarnings("serial")
public class AwakenTPInstance extends NpcInstance {

    private int val = 1;

    public AwakenTPInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.startsWith("awakingteleport")) {
            if (player.isAwaking()) {
                if ((player.getLevel() < 90) && (player.getLevel() >= 85))
                    showChatWindow(player, "teleporter/hermunkus-85.htm");
                if (player.getLevel() >= 90)
                    showChatWindow(player, "teleporter/hermunkus-90.htm");
            } else
                showChatWindow(player, "teleporter/hermunkus-noawakening.htm");
        } else
            super.onBypassFeedback(player, command);
    }
}