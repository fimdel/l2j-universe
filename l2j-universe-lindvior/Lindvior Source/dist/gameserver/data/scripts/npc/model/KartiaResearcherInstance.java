/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package npc.model;

import instances.Kartia.Party.KartiaLabyrinth85Party;
import instances.Kartia.Party.KartiaLabyrinth90Party;
import instances.Kartia.Party.KartiaLabyrinth95Party;
import instances.Kartia.Solo.KartiaLabyrinth85Solo;
import instances.Kartia.Solo.KartiaLabyrinth90Solo;
import instances.Kartia.Solo.KartiaLabyrinth95Solo;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.ReflectionUtils;

public class KartiaResearcherInstance extends NpcInstance {

    private static final Location TELEPORT_POSITION = new Location(-109032, -10440, -11949);

    private static final int Adolph88 = 33609;
    private static final int Barton88 = 33611;
    private static final int Hayuk88 = 33613;
    private static final int Eliyah88 = 33615;
    private static final int Elise88 = 33617;

    private static final int Adolph93 = 33620;
    private static final int Barton93 = 33622;
    private static final int Hayuk93 = 33624;
    private static final int Eliyah93 = 33626;
    private static final int Elise93 = 33628;

    private static final int Adolph98 = 33631;
    private static final int Barton98 = 33633;
    private static final int Hayuk98 = 33635;
    private static final int Eliyah98 = 33637;
    private static final int Elise98 = 33639;

    public KartiaResearcherInstance(int objectId, NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    public void onBypassFeedback(Player player, String command) {
        if (!canBypassCheck(player, this)) {
            return;
        }
        if (command.equalsIgnoreCase("request_zellaka_solo")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(205)) {
                    player.teleToLocation(r.getTeleportLoc(), r);
                }
            } else if (player.canEnterInstance(205)) {
                ReflectionUtils.enterReflection(player, new KartiaLabyrinth85Solo(), 205);
            }
        }
        if (command.equalsIgnoreCase("request_zellaka_party")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(208)) {
                    player.teleToLocation(r.getTeleportLoc(), r);
                }
            } else if (player.canEnterInstance(208)) {
                ReflectionUtils.enterReflection(player, new KartiaLabyrinth85Party(), 208);
            }
        }
        if (command.equalsIgnoreCase("request_pelline_solo")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(206)) {
                    player.teleToLocation(r.getTeleportLoc(), r);
                }
            } else if (player.canEnterInstance(206)) {
                ReflectionUtils.enterReflection(player, new KartiaLabyrinth90Solo(), 206);
            }
        }
        if (command.equalsIgnoreCase("request_pelline_party")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(209)) {
                    player.teleToLocation(r.getTeleportLoc(), r);
                }
            } else if (player.canEnterInstance(209)) {
                ReflectionUtils.enterReflection(player, new KartiaLabyrinth90Party(), 209);
            }
        }
        if (command.equalsIgnoreCase("request_kalios_solo")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(207)) {
                    player.teleToLocation(r.getTeleportLoc(), r);
                }
            } else if (player.canEnterInstance(207)) {
                ReflectionUtils.enterReflection(player, new KartiaLabyrinth95Solo(), 207);
            }
        }
        if (command.equalsIgnoreCase("request_kalios_party")) {
            Reflection r = player.getActiveReflection();
            if (r != null) {
                if (player.canReenterInstance(210)) {
                    player.teleToLocation(r.getTeleportLoc(), r);
                }
            } else if (player.canEnterInstance(210)) {
                ReflectionUtils.enterReflection(player, new KartiaLabyrinth95Party(), 210);
            }
        }
        if (command.startsWith("start")) {
            String[] splited = command.split("\\s");
            //w a s h
            String param = splited[1];
            Reflection r = player.getActiveReflection();
            if (r != null) {
                //PARTY INSTANCE
                if (r.getInstancedZoneId() > 207) {
                    player.getParty().Teleport(TELEPORT_POSITION);
                }
                //SOLO INSTANCE
                else {
                    player.teleToLocation(TELEPORT_POSITION);
                    if (r.getInstancedZoneId() == 205) {
                        NpcInstance adolph = r.addSpawnWithoutRespawn(Adolph88, TELEPORT_POSITION, 100);
                        adolph.setFollowTarget(player);
                        if (!param.equals("w")) {
                            NpcInstance barton = r.addSpawnWithoutRespawn(Barton88, TELEPORT_POSITION, 100);
                            barton.setFollowTarget(player);
                        }
                        if (!param.equals("a")) {
                            NpcInstance hayuk = r.addSpawnWithoutRespawn(Hayuk88, TELEPORT_POSITION, 100);
                            hayuk.setFollowTarget(player);
                        }
                        if (!param.equals("s")) {
                            NpcInstance eliyah = r.addSpawnWithoutRespawn(Eliyah88, TELEPORT_POSITION, 100);
                            eliyah.setFollowTarget(player);
                        }
                        if (!param.equals("h")) {
                            NpcInstance elise = r.addSpawnWithoutRespawn(Elise88, TELEPORT_POSITION, 100);
                            elise.setFollowTarget(player);
                        }
                    }
                    if (r.getInstancedZoneId() == 206) {
                        NpcInstance adolph = r.addSpawnWithoutRespawn(Adolph93, TELEPORT_POSITION, 100);
                        adolph.setFollowTarget(player);
                        if (!param.equals("w")) {
                            NpcInstance barton = r.addSpawnWithoutRespawn(Barton93, TELEPORT_POSITION, 100);
                            barton.setFollowTarget(player);
                        }
                        if (!param.equals("a")) {
                            NpcInstance hayuk = r.addSpawnWithoutRespawn(Hayuk93, TELEPORT_POSITION, 100);
                            hayuk.setFollowTarget(player);
                        }
                        if (!param.equals("s")) {
                            NpcInstance eliyah = r.addSpawnWithoutRespawn(Eliyah93, TELEPORT_POSITION, 100);
                            eliyah.setFollowTarget(player);
                        }
                        if (!param.equals("h")) {
                            NpcInstance elise = r.addSpawnWithoutRespawn(Elise93, TELEPORT_POSITION, 100);
                            elise.setFollowTarget(player);
                        }
                    }
                    if (r.getInstancedZoneId() == 207) {
                        NpcInstance adolph = r.addSpawnWithoutRespawn(Adolph98, TELEPORT_POSITION, 100);
                        adolph.setFollowTarget(player);
                        if (!param.equals("w")) {
                            NpcInstance barton = r.addSpawnWithoutRespawn(Barton98, TELEPORT_POSITION, 100);
                            barton.setFollowTarget(player);
                        }
                        if (!param.equals("a")) {
                            NpcInstance hayuk = r.addSpawnWithoutRespawn(Hayuk98, TELEPORT_POSITION, 100);
                            hayuk.setFollowTarget(player);
                        }
                        if (!param.equals("s")) {
                            NpcInstance eliyah = r.addSpawnWithoutRespawn(Eliyah98, TELEPORT_POSITION, 100);
                            eliyah.setFollowTarget(player);
                        }
                        if (!param.equals("h")) {
                            NpcInstance elise = r.addSpawnWithoutRespawn(Elise98, TELEPORT_POSITION, 100);
                            elise.setFollowTarget(player);
                        }
                    }
                }
            }
        }
        super.onBypassFeedback(player, command);
    }
}
