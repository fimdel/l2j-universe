/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.ExStartScenePlayer;
import l2p.gameserver.templates.npc.NpcTemplate;

public final class BalrogStoryInstance extends NpcInstance {

    public BalrogStoryInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.startsWith("request_video")) {
            player.showQuestMovie(ExStartScenePlayer.SCENE_SI_BARLOG_STORY);
        } else
            super.onBypassFeedback(player, command);
    }
}
