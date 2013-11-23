package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;

/**
 * Данный инстанс используется телепортерами из/в Pagan Temple
 *
 * @author SYS
 */
public class TriolsMirrorInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = 6242977611856110010L;

    public TriolsMirrorInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void showChatWindow(Player player, int val, Object... arg) {
        if (getNpcId() == 32040)
            player.teleToLocation(-12766, -35840, -10856); //to pagan
        else if (getNpcId() == 32039)
            player.teleToLocation(35079, -49758, -760); //from pagan
    }
}