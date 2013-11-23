package zones;

import l2p.gameserver.listener.zone.OnZoneEnterLeaveListener;
import l2p.gameserver.model.Creature;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.Zone;
import l2p.gameserver.network.serverpackets.components.SceneMovie;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.utils.ReflectionUtils;

public class RuinsOfEsagiraMovie implements ScriptFile {
    private static final String ZONE_NAME = "[roe_presentation_video]";

    private static ZoneListener _zoneListener;

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

    private void init() {
        _zoneListener = new ZoneListener();
        Zone zone = ReflectionUtils.getZone(ZONE_NAME);
        if (zone != null)
            zone.addListener(_zoneListener);
    }

    public class ZoneListener implements OnZoneEnterLeaveListener {
        @Override
        public void onZoneEnter(Zone zone, Creature cha) {
            if (cha.isPlayer()) {
                Player player = cha.getPlayer();
                if (!player.getVarB("@roe_present_video")) {
                    player.showQuestMovie(SceneMovie.si_illusion_03_que);
                    player.setVar("@roe_present_video", "true", -1);
                }
            }
        }

        @Override
        public void onZoneLeave(Zone zone, Creature cha) {
        }
    }
}