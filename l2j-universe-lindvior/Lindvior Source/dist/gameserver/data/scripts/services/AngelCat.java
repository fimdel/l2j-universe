/*
 * Copyright Mazaffaka Project (c) 2013.
 */

package services;

import l2p.gameserver.Config;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.model.SimpleSpawner;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.scripts.Functions;
import l2p.gameserver.scripts.ScriptFile;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.utils.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AngelCat extends Functions implements ScriptFile {
    private static final Logger _log = LoggerFactory.getLogger(AngelCat.class);
    private static final int ANGEL_CAT_ID = 4308; // Кот-Ангел Посланник Любви

    private static final Location[] SPAWN_LOC = {
            new Location(-114190, 254114, -1528, 27931), // Talkin
            new Location(16121, 142931, -2696, 27931), // Dion
            new Location(83680, 147996, -3400, 64087), // Giran
            new Location(111299, 219409, -3544, 61496), // Heine
            new Location(-14068, 123822, -3120, 27433), // Gludio
            new Location(-80999, 149996, -3040, 59718), // Gludin
            new Location(82921, 53097, -1488, 61318), // Oren
            new Location(147457, -55382, -2728, 40707), // Goddard
            new Location(86898, -142831, -1336, 41834), // Shuttgard
            new Location(43918, -47713, -792, 30607), // Rune
            new Location(147251, 25628, -2008, 28713), // Aden
            new Location(117281, 76733, -2688, 30749), // Hunters
    };

    public void Spawn() {
        for (int a = 0; a < SPAWN_LOC.length; a++) {
            NpcTemplate template = NpcHolder.getInstance().getTemplate(ANGEL_CAT_ID);
            SimpleSpawner sp = new SimpleSpawner(template);
            sp.setLoc(SPAWN_LOC[a]);
            sp.setAmount(1);
            sp.setRespawnDelay(0);
            NpcInstance AngelNpc = sp.doSpawn(true);
        }
    }

    @Override
    public void onLoad() {
        if (Config.ENABLE_ANGEL_CAT) {
            Spawn();
            _log.info("Loaded Services: Angel Cat");
        }
    }

    @Override
    public void onReload() {
    }

    @Override
    public void onShutdown() {
    }

}
