package npc.model;

import instances.Teredor;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ReflectionUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 16.10.12
 * Time: 23:00
 */
public final class TeredorGatekeeperInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final int teredorInstanceId = 160;

    public TeredorGatekeeperInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("teredor_enter")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(teredorInstanceId))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(teredorInstanceId)) {
                ReflectionUtils.enterReflection(player, new Teredor(), teredorInstanceId);
            }
        } else
            super.onBypassFeedback(player, command);
    }
}
