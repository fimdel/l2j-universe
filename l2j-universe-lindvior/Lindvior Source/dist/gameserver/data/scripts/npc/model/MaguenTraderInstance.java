package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.NpcUtils;

/**
 * @author pchayka
 */
public final class MaguenTraderInstance extends NpcInstance {
    /**
     *
     */
    private static final long serialVersionUID = 6340564651259358026L;

    public MaguenTraderInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("request_collector")) {
            if (Functions.getItemCount(player, 15487) > 0)
                showChatWindow(player, "default/32735-2.htm");
            else
                Functions.addItem(player, 15487, 1);
        } else if (command.equalsIgnoreCase("request_maguen")) {
            NpcUtils.spawnSingle(18839, Location.findPointToStay(getSpawnedLoc(), 40, 100, getGeoIndex()), getReflection()); // wild maguen
            showChatWindow(player, "default/32735-3.htm");
        } else
            super.onBypassFeedback(player, command);
    }
}