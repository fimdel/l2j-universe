package npc.model;

import l2p.gameserver.cache.Msg;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ItemFunctions;

/**
 * @author pchayka
 */

public final class KeplonInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = -257473056764724980L;

    public KeplonInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (checkForDominionWard(player))
            return;

        if (command.equalsIgnoreCase("buygreen")) {
            if (ItemFunctions.removeItem(player, 57, 10000, true) >= 10000) {
                ItemFunctions.addItem(player, 4401, 1, true);
                return;
            } else {
                player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
                return;
            }
        } else if (command.startsWith("buyblue")) {
            if (ItemFunctions.removeItem(player, 57, 10000, true) >= 10000) {
                ItemFunctions.addItem(player, 4402, 1, true);
                return;
            } else {
                player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
                return;
            }
        } else if (command.startsWith("buyred")) {
            if (ItemFunctions.removeItem(player, 57, 10000, true) >= 10000) {
                ItemFunctions.addItem(player, 4403, 1, true);
                return;
            } else {
                player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
                return;
            }
        } else
            super.onBypassFeedback(player, command);
    }
}