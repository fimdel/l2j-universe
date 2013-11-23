package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.templates.npc.NpcTemplate;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 18.09.12
 * Time: 7:14
 */
public class ZenyaInstance extends NpcInstance {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int val;

    public ZenyaInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("teleport")) {
            if (player.getLevel() < 80) {
                player.sendPacket(new NpcHtmlMessage(player, this, "default/32140-2.htm", val));
            } else {
                player.teleToLocation(183399, -81012, -5320);
            }
        } else
            super.onBypassFeedback(player, command);
    }
}
