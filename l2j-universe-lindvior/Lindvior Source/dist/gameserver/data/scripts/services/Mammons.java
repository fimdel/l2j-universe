/*
 * Copyright Mazaffaka Project (c) 2012.
 */

package services;

import l2p.commons.util.Rnd;
import l2p.gameserver.ThreadPoolManager;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.model.SimpleSpawner;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.network.serverpackets.components.NpcString;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 03.11.12
 * Time: 19:03
 */
public class Mammons extends Functions implements ScriptFile {
    private static final Logger _log = LoggerFactory.getLogger(Mammons.class);
    private static final int MAMMON_PRIEST_ID = 33511; // Жрец мамона
    private static final int MAMMON_MERCHANT_ID = 31113; // Торговец мамона
    private static final int MAMMON_BLACKSMITH_ID = 31126; // Кузнец мамона

    private static final int PORT_TIME = 10 * 60 * 1000; // 60 min
    private static NpcInstance PriestNpc;
    private static NpcInstance MerchantNpc;
    private static NpcInstance BlacksmithNpc;

    private static ScheduledFuture<?> _mammonTeleportTask;

    private static final NpcString[] mammonText = {
            NpcString.RULERS_OF_THE_SEAL_I_BRING_YOU_WONDROUS_GIFTS,
            NpcString.RULERS_OF_THE_SEAL_I_HAVE_SOME_EXCELLENT_WEAPONS_TO_SHOW_YOU,
            NpcString.IVE_BEEN_SO_BUSY_LATELY_IN_ADDITION_TO_PLANNING_MY_TRIP};

    // * - верные кординаты. Отстальные - примерные.
    private static final Location[] MAMMON_PRIEST_POINTS = {
            new Location(16403, 144843, -3016, 27931), // Dion
            new Location(81284, 150155, -3528), // Giran*
            new Location(114478, 217596, -3624, 0), // Heine*
            new Location(-12136, 121784, -3014), // Gludio
            new Location(-84808, 151416, -3154), // Gludin
            new Location(79976, 56968, -1585), // Oren
            new Location(144664, -54200, -3006), // Goddard
            new Location(90376, -143544, -1566), // Shuttgard
            new Location(42792, -41384, -2213), // Rune
            new Location(146968, -29656, -2294), // Aden
            new Location(120456, 76456, -2168), // Hunters
    };

    private static final Location[] MAMMON_MERCHANT_POINTS = {
            new Location(16380, 144784, -3016, 27931), // Dion
            new Location(81272, 150041, -3528), // Giran*
            new Location(114482, 217538, -3624, 0), // Heine*
            new Location(-12120, 121848, -3014), // Gludio
            new Location(-84808, 151352, -3154), // Gludin
            new Location(80056, 56952, -1585), // Oren
            new Location(144584, -54184, -3006), // Goddard
            new Location(90232, -143592, -1566), // Shuttgard
            new Location(42776, -41320, -2213), // Rune
            new Location(146968, -29704, -2294), // Aden
            new Location(120408, 76568, -2167), // Hunters
    };

    private static final Location[] MAMMON_BLACKSMITH_POINTS = {
            new Location(16335, 144696, -3024, 27931), // Dion
            new Location(81266, 150091, -3528), // Giran*
            new Location(114484, 217462, -3624, 0), // Heine*
            new Location(-12120, 121912, -3014), // Gludio
            new Location(-84808, 151320, -3154), // Gludin
            new Location(79992, 56792, -1585), // Oren
            new Location(144520, -54152, -3006), // Goddard
            new Location(89992, -143672, -1565), // Shuttgard
            new Location(42760, -41256, -2216), // Rune
            new Location(146968, -29784, -2294), // Aden
            new Location(120312, 76536, -2167), // Hunters
    };

    public void SpawnMammons() {
        int firstTown = Rnd.get(MAMMON_PRIEST_POINTS.length);

        NpcTemplate template = NpcHolder.getInstance().getTemplate(MAMMON_PRIEST_ID);
        SimpleSpawner sp = new SimpleSpawner(template);
        sp.setLoc(MAMMON_PRIEST_POINTS[firstTown]);
        sp.setAmount(1);
        sp.setRespawnDelay(0);
        PriestNpc = sp.doSpawn(true);

        template = NpcHolder.getInstance().getTemplate(MAMMON_MERCHANT_ID);
        sp = new SimpleSpawner(template);
        sp.setLoc(MAMMON_MERCHANT_POINTS[firstTown]);
        sp.setAmount(1);
        sp.setRespawnDelay(0);
        MerchantNpc = sp.doSpawn(true);

        template = NpcHolder.getInstance().getTemplate(MAMMON_BLACKSMITH_ID);
        sp = new SimpleSpawner(template);
        sp.setLoc(MAMMON_BLACKSMITH_POINTS[firstTown]);
        sp.setAmount(1);
        sp.setRespawnDelay(0);
        BlacksmithNpc = sp.doSpawn(true);
    }

    public static class TeleportMammons implements Runnable {
        @Override
        public void run() {
            Functions.npcShout(BlacksmithNpc, mammonText[Rnd.get(mammonText.length)]);
            int nextTown = Rnd.get(MAMMON_PRIEST_POINTS.length);
            PriestNpc.teleToLocation(MAMMON_PRIEST_POINTS[nextTown]);
            MerchantNpc.teleToLocation(MAMMON_MERCHANT_POINTS[nextTown]);
            BlacksmithNpc.teleToLocation(MAMMON_BLACKSMITH_POINTS[nextTown]);
        }
    }

    @Override
    public void onLoad() {
        SpawnMammons();
        _mammonTeleportTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new TeleportMammons(), PORT_TIME, PORT_TIME);
        _log.info("Loaded AI: Mammons Teleporter");
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onShutdown() {
        if (_mammonTeleportTask != null) {
            _mammonTeleportTask.cancel(true);
            _mammonTeleportTask = null;
        }
    }

}
