package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

public final class StarStoneInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = -467070721285172865L;

    public StarStoneInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void showChatWindow(Player player, int val, Object... arg) {
    }

    @Override
    public void showChatWindow(Player player, String filename, Object... replace) {
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
    }
}