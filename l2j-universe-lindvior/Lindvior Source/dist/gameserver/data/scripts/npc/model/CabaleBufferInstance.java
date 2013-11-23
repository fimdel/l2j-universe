package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

public final class CabaleBufferInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = 4493683653737866004L;

    public CabaleBufferInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void showChatWindow(Player player, int val, Object... arg) {
    }

    @Override
    public void showChatWindow(Player player, String filename, Object... ar) {
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
    }
}