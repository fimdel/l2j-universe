/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import instances.Baylor;
import instances.CrystalHall;
import instances.Vullock;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.EventTrigger;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ReflectionUtils;

import java.util.Calendar;

public final class ParnaceTPInstance extends NpcInstance {
    private static final int CrystalHallInstance = 163;
    private static final int VullockInstance = 167;
    private static final int BaylorInstance = 168;
    private static final int CrystalPrison = 33523;
    private static final int CrystalHall = 33522;
    NpcHtmlMessage htmlMessage = new NpcHtmlMessage(getObjectId());

    public ParnaceTPInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    private static enum CrystalCavernsType {
        Perla,
        Steam,
        Coral
    }

    private CrystalCavernsType getType() {
        Calendar cal = Calendar.getInstance();

        if (cal.get(11) >= 18) {
            switch (cal.get(7)) {
                case 1:
                case 2:
                case 5:
                    return CrystalCavernsType.Steam;
                case 3:
                case 6:
                    return CrystalCavernsType.Perla;
                case 4:
                case 7:
                    return CrystalCavernsType.Coral;
            }
        } else {
            switch (cal.get(7)) {
                case 1:
                case 2:
                case 5:
                    return CrystalCavernsType.Perla;
                case 3:
                case 6:
                    return CrystalCavernsType.Coral;
                case 4:
                case 7:
                    return CrystalCavernsType.Steam;
            }
        }
        return CrystalCavernsType.Perla;
    }

    public String onEnterZone(Creature actor, Zone zone) {
        if ((actor instanceof Player)) {
            actor.sendPacket(new EventTrigger(24230010, true));
            actor.sendPacket(new EventTrigger(24230012, true));
            switch (getType().ordinal()) {
                case 1:
                    actor.sendPacket(new EventTrigger(24230018, true));
                    break;
                case 2:
                    actor.sendPacket(new EventTrigger(24230016, true));
                    break;
                case 3:
                    actor.sendPacket(new EventTrigger(24230014, true));
            }

        }

        return null;
    }


    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this))
            return;
        switch (getType().ordinal()) {
            case 1:
                htmlMessage.setFile("default/" + "gd_course_enter001a.htm");
                break;
            case 2:
                htmlMessage.setFile("default/" + "gd_course_enter000b.htm");
                break;
            case 3:
                htmlMessage.setFile("default/" + "gd_course_enter000c.htm");
                break;
        }

        if (command.startsWith("request_ch")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(CrystalHallInstance))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(CrystalHallInstance)) {
                ReflectionUtils.enterReflection(player, new CrystalHall(), CrystalHallInstance);
            }
        } else if (command.startsWith("request_vallock")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(VullockInstance))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(VullockInstance)) {
                ReflectionUtils.enterReflection(player, new Vullock(), VullockInstance);
            }
        } else if (command.startsWith("request_Baylor")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(BaylorInstance))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(BaylorInstance)) {
                ReflectionUtils.enterReflection(player, new Baylor(), BaylorInstance);
            }
        } else {
            super.onBypassFeedback(player, command);
        }
    }
}
