/*
 * Copyright Mazaffaka Project (c) 2013.
 */

/*
 * Copyright Murzik Dev Team (c) 2013.
 */

package l2p.gameserver.instancemanager;

import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.GameObjectsStorage;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Zone;
import l2p.gameserver.network.serverpackets.EventTrigger;
import l2p.gameserver.network.serverpackets.L2GameServerPacket;
import l2p.gameserver.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class ParnassusManager {
    private static final String TELEPORT_ZONE_NAME = "[parnassus]";
    private static final Logger _log = LoggerFactory.getLogger(ParnassusManager.class);
    private static ParnassusManager _instance;
    private static final long _taskDelay = 24 * 60 * 60 * 1000L; // Пока что пущай будет так
    private static int _Stage = 0;
    private static int Prison1 = 24230010;
    private static int Prison2 = 24230012;
    private static int Prison3 = 24230014;
    private static int Vault1 = 24230016;
    private static int Vault2 = 24230018;
    private static final int Mon = 1;
    private static final int Wed = 2;
    private static final int Thi = 3;
    private static final int Tue = 4;
    private static final int Fri = 5;
    private static final int Sun = 6;
    private static final int Sat = 7;

    private static ZoneListener _zoneListener;

    public static ParnassusManager getInstance() {
        if (_instance == null)
            _instance = new ParnassusManager();
        return _instance;
    }

    public ParnassusManager() {
        _zoneListener = new ZoneListener();
        //ThreadPoolManager.getInstance().scheduleAtFixedRate(new ChangeStage(), _taskDelay, _taskDelay);
        Zone zone = ReflectionUtils.getZone(TELEPORT_ZONE_NAME);
        zone.addListener(_zoneListener);
        _log.info("Parnasus Manager: Loaded");
    }

    public class ZoneListener implements OnZoneEnterLeaveListener {
        @Override
        public void onZoneEnter(Zone zone, Creature cha) {
            if (zone == null)
                return;

            if (cha == null)
                return;

            if (!cha.isPlayer())
                return;

            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) <= 15) {
                broadcastPacket(Prison1, true);
                broadcastPacket(Prison2, false);
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 0 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 2 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 4) {
                    broadcastPacket(Prison3, true);
                    broadcastPacket(Vault1, false);
                    broadcastPacket(Vault2, false);
                }
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 3 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 5) {
                    broadcastPacket(Prison3, false);
                    broadcastPacket(Vault1, true);
                    broadcastPacket(Vault2, false);
                }
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 6) {
                    broadcastPacket(Prison3, false);
                    broadcastPacket(Vault1, false);
                    broadcastPacket(Vault2, true);
                }
                _log.info("Parnasus Manager: Currently: " + Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + " " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "< 15" + " Date Loaded");
            }
            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) >= 15) {
                broadcastPacket(Prison2, true);
                broadcastPacket(Prison1, false);
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 0 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 2 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 4) {
                    broadcastPacket(Prison3, true);
                    broadcastPacket(Vault1, false);
                    broadcastPacket(Vault2, false);
                }
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 3 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 5) {
                    broadcastPacket(Prison3, false);
                    broadcastPacket(Vault1, true);
                    broadcastPacket(Vault2, false);
                }
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 6) {
                    broadcastPacket(Prison3, false);
                    broadcastPacket(Vault1, false);
                    broadcastPacket(Vault2, true);
                }
            }
            _log.info("Parnasus Manager: Currently: " + Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + " " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "> 15" + " Date Loaded");

        }

        @Override
        public void onZoneLeave(Zone zone, Creature cha) {
        }
    }

    public void broadcastPacket(int value, boolean b) {
        L2GameServerPacket trigger = new EventTrigger(value, b);
        for (Player player : GameObjectsStorage.getAllPlayersForIterate())
            player.sendPacket(trigger);
    }
}
