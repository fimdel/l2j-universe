/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package zones;

import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Zone;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.Location;
import l2p.gameserver.utils.ReflectionUtils;

public class SOA_TP implements ScriptFile {
    private static final Location TELEPORT_LOC = new Location(-185839, 147909, -15312);
    private static final String[] zones = {"[Birthing_room_0]", "[Birthing_room_1]", "[Birthing_room_2]", "[Birthing_room_3]"};
    private static ZoneListener _zoneListener;

    private void init() {
        _zoneListener = new ZoneListener();

        for (String s : zones) {
            Zone zone = ReflectionUtils.getZone(s);
            zone.addListener(_zoneListener);
        }
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
            if (zone == null) {
                return;
            }

            if (cha == null) {
                return;
            }
            if (cha.getLevel() >= 85 || cha.getLevel() <= 89) {
                if ((zone.getName().equalsIgnoreCase("[Birthing_room_0]"))
                        || (zone.getName().equalsIgnoreCase("[Birthing_room_1]")) || (zone.getName().equalsIgnoreCase("[Birthing_room_2]")) || (zone.getName().equalsIgnoreCase("[Birthing_room_3]"))) {
                    cha.teleToLocation(TELEPORT_LOC);
                }
            } else
                cha.sendMessage("Вы не подходите по уровню");
        }

        @Override
        public void onZoneLeave(Zone zone, Creature cha) {
        }
    }
}