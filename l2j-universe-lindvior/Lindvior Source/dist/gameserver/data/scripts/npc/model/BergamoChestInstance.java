/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import l2p.commons.util.Rnd;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.templates.npc.NpcTemplate;

public class BergamoChestInstance extends NpcInstance {
    private static final long serialVersionUID = 1L;
    private static final int[] dropped = new int[]{6569, 6570, 6571, 6572, 6573, 6574, 6575, 6576, 6577, 6578, 19447, 19448, 9546, 9547, 9548, 9549, 9550, 9551, 9552, 9553, 9554, 9555, 9556, 9557};
    private static NpcInstance npc;

    public BergamoChestInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (command.equalsIgnoreCase("start")) {
            NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());
            htmlMessage.setFile("openBergamoChest.htm");
            this.doDie(null);
            dropItem(player, dropped[Rnd.get(dropped.length)], 1);
        } else
            super.onBypassFeedback(player, command);
    }

}
