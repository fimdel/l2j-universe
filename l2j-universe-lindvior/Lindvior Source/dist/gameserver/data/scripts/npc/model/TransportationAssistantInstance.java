/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import l2p.gameserver.model.Player;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.SystemMessage;
import l2p.gameserver.templates.npc.NpcTemplate;

/**
 * @author KilRoy & Mangol
 */
public final class TransportationAssistantInstance extends NpcInstance {
    private static final long serialVersionUID = -6027309212963370840L;

    public TransportationAssistantInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;

        if (player.isCursedWeaponEquipped()) {
            player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_MOVE_WHILE_IN_A_CHAOTIC_STATE));
            return;
        }
        if (player.isDead()) {
            player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_MOVE_WHILE_DEAD));
            return;
        }
        if (player.isCastingNow() || player.isInCombat() || player.isAttackingNow()) {
            player.sendPacket(new SystemMessage(SystemMessage.YOU_CANNOT_MOVE_DURING_COMBAT));
            return;
        }

        if (command.equalsIgnoreCase("goto1ln"))
            player.teleToLocation(-147711, 152768, -14056);
        if (command.equalsIgnoreCase("goto1ls"))
            player.teleToLocation(-147867, 250710, -14024);
        if (command.equalsIgnoreCase("goto2ln"))
            player.teleToLocation(-150131, 143145, -11960);
        if (command.equalsIgnoreCase("goto2ls"))
            player.teleToLocation(-150169, 241022, -11928);

        else
            super.onBypassFeedback(player, command);
    }
}