package npc.model;

import instances.Frintezza;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ItemFunctions;
import l2p.gameserver.utils.ReflectionUtils;

/**
 * @author pchayka
 */

public final class FrintezzaGatekeeperInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = 1937965538727707089L;
    private static final int frintezzaIzId = 136;

    public FrintezzaGatekeeperInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("request_frintezza")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(frintezzaIzId))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(frintezzaIzId)) {
                if (ItemFunctions.removeItem(player, 8073, 1, true) < 1) {
                    player.sendPacket(new SystemMessage(SystemMessage.C1S_ITEM_REQUIREMENT_IS_NOT_SUFFICIENT_AND_CANNOT_BE_ENTERED).addName(player));
                    return;
                }
                ReflectionUtils.enterReflection(player, new Frintezza(), frintezzaIzId);
            }
        } else
            super.onBypassFeedback(player, command);
    }
}