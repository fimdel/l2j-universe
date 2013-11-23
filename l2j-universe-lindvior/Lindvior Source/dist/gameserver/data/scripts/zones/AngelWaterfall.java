/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package zones;

import instances.FreaksWorld;
import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Zone;
import l2p.gameserver.model.entity.Reflection;
import l2p.gameserver.model.quest.QuestState;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.ReflectionUtils;
import quests._10301_ShadowOfTerrorBlackishRedFog;

public class AngelWaterfall implements ScriptFile {
    private static final String TELEPORT_ZONE_NAME = "[25_20_telzone_to_magmeld]";
    private static final Location TELEPORT_LOC = new Location(207559, 86429, -1000);
    private static final int InstanceId = 192;
    private static ZoneListener _zoneListener;

    private void init() {
        _zoneListener = new ZoneListener();
        Zone zone = ReflectionUtils.getZone(TELEPORT_ZONE_NAME);
        zone.addListener(_zoneListener);
    }

    @Override
    public void onLoad() {
        init();
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

    public class ZoneListener implements OnZoneEnterLeaveListener {
        @Override
        public void onZoneEnter(Zone zone, Creature cha) {
            if (zone == null)
                return;

            if (cha == null)
                return;
            Player player = cha.getPlayer();
            if (player == null)
                return;

            QuestState qs = player.getQuestState(_10301_ShadowOfTerrorBlackishRedFog.class);
            if (qs != null) {
                //cha.teleToLocation(TELEPORT_LOC);
                Reflection r = player.getActiveReflection();
                if (r != null) {
                    if (player.canReenterInstance(InstanceId))
                        player.teleToLocation(r.getTeleportLoc(), r);
                } else if (player.canEnterInstance(InstanceId)) {
                    ReflectionUtils.enterReflection(player, new FreaksWorld(), InstanceId);
                }
            } else {
                player.teleToLocation(TELEPORT_LOC);
            }
        }

        @Override
        public void onZoneLeave(Zone zone, Creature cha) {
        }
    }
}