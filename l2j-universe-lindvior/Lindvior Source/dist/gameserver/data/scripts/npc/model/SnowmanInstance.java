package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

/**
 * Данный инстанс используется NPC Snowman в эвенте Saving Snowman
 *
 * @author SYS
 */
public class SnowmanInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = -7630540649773972082L;

    public SnowmanInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void showChatWindow(Player player, int val, Object... arg) {
        player.sendActionFailed();
    }
}