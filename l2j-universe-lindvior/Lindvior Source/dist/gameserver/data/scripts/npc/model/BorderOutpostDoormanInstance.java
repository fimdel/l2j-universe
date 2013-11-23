package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.DoorInstance;
import l2p.gameserver.model.instances.GuardInstance;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ReflectionUtils;

/**
 * @author VISTALL
 * @date 10:26/24.06.2011
 */
public class BorderOutpostDoormanInstance extends GuardInstance {
    /**
     *
     */
    private static final long serialVersionUID = -4727531182878487757L;

    public BorderOutpostDoormanInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equals("openDoor")) {
            DoorInstance door = ReflectionUtils.getDoor(24170001);
            door.openMe();
        } else if (command.equals("closeDoor")) {
            DoorInstance door = ReflectionUtils.getDoor(24170001);
            door.closeMe();
        } else
            super.onBypassFeedback(player, command);
    }
}
