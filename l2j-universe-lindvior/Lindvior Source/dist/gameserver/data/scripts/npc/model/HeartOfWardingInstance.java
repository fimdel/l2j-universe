package npc.model;

import bosses.AntharasManager;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

/**
 * @author pchayka
 */

public final class HeartOfWardingInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = -5735055789302801878L;

    public HeartOfWardingInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("enter_lair")) {
            AntharasManager.enterTheLair(player);
            return;
        } else
            super.onBypassFeedback(player, command);
    }
}