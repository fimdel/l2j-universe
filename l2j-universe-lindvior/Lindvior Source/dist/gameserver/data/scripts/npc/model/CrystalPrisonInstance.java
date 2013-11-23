/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import instances.CrystalHall;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.NpcHtmlMessage;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.ReflectionUtils;

import java.util.Calendar;

public class CrystalPrisonInstance extends NpcInstance {
    public CrystalPrisonInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this)) {
            return;
        }

        if (command.startsWith("request_CrystalHall")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(163))
                    player.teleToLocation(r.getTeleportLoc(), r);
            } else if (player.canEnterInstance(163)) {
                ReflectionUtils.enterReflection(player, new CrystalHall(), 163);
            }
        }

        if (command.startsWith("request_SteamCorridor")) {

        }

        if (command.startsWith("request_CoralGarden")) {

        }
    }

    @Override
    public void showChatWindow(Player player, int val, Object[] replace) {
        NpcHtmlMessage msg = new NpcHtmlMessage(player, this);
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 0 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 2 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 4) {
            msg.setFile("default/33522.htm");
            msg.replace("%instance%", "Огненный Корридор");
            msg.replace("%enter%", "request_SteamCorridor");
        }
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 3 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 5) {
            msg.setFile("default/33522.htm");
            msg.replace("%instance%", "Перламутровый Зал");
            msg.replace("%enter%", "request_CrystalHall");
        }
        if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 6) {
            msg.setFile("default/33522.htm");
            msg.replace("%instance%", "Корраловый Сад");
            msg.replace("%enter%", "request_CoralGarden");
        }
        player.sendPacket(msg);
    }
}

