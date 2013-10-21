package npc.model;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.ExStartScenePlayer;
import lineage2.gameserver.templates.npc.NpcTemplate;

/**
 * @author Awakeninger
 */

public final class BalrogStoryInstance extends NpcInstance {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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