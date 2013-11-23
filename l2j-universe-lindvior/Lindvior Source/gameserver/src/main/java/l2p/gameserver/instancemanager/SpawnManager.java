package l2p.gameserver.instancemanager;

import l2p.gameserver.Config;
import l2p.gameserver.GameTimeController;
import l2p.gameserver.data.xml.holder.NpcHolder;
import l2p.gameserver.data.xml.holder.SpawnHolder;
import l2p.gameserver.data.xml.holder.WalkerRoutesHolder;
import l2p.gameserver.listener.game.OnDayNightChangeListener;
import l2p.gameserver.listener.game.OnSSPeriodListener;
import l2p.gameserver.model.HardSpawner;
import l2p.gameserver.model.Spawner;
import l2p.gameserver.model.instances.MonsterInstance;
import l2p.gameserver.model.instances.NpcInstance;
import l2p.gameserver.templates.npc.NpcTemplate;
import l2p.gameserver.templates.spawn.PeriodOfDay;
import l2p.gameserver.templates.spawn.SpawnTemplate;
import l2p.gameserver.templates.spawn.WalkerRouteTemplate;
import l2p.gameserver.utils.NpcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpawnManager {
    private class Listeners implements OnDayNightChangeListener, OnSSPeriodListener {
        @Override
        public void onDay() {
            despawn(PeriodOfDay.NIGHT.name());
            spawn(PeriodOfDay.DAY.name());
        }

        @Override
        public void onNight() {
            despawn(PeriodOfDay.DAY.name());
            spawn(PeriodOfDay.NIGHT.name());
        }

        @Override
        public void onPeriodChange(int mode) {
            switch (mode) {
                case 0: // all spawns
                    despawn(DAWN_GROUP);
                    despawn(DUSK_GROUP);
                    spawn(DAWN_GROUP);
                    spawn(DUSK_GROUP);
                    break;
                case 1: // dusk spawns
                    despawn(DAWN_GROUP);
                    despawn(DUSK_GROUP);
                    spawn(DUSK_GROUP);
                    break;
                case 2: // dawn spawns
                    despawn(DAWN_GROUP);
                    despawn(DUSK_GROUP);
                    spawn(DAWN_GROUP);
                    break;
            }
        }
    }

    private static final Logger _log = LoggerFactory.getLogger(SpawnManager.class);

    private static SpawnManager _instance = new SpawnManager();

    private static final String DAWN_GROUP = "dawn_spawn";
    private static final String DUSK_GROUP = "dusk_spawn";

    private Map<String, List<Spawner>> _spawns = new ConcurrentHashMap<String, List<Spawner>>();
    private Listeners _listeners = new Listeners();

    private List<NpcInstance> _walkers = new ArrayList<NpcInstance>();

    public static SpawnManager getInstance() {
        return _instance;
    }

    private SpawnManager() {
        for (Map.Entry<String, List<SpawnTemplate>> entry : SpawnHolder.getInstance().getSpawns().entrySet())
            fillSpawn(entry.getKey(), entry.getValue());

        GameTimeController.getInstance().addListener(_listeners);
    }

    public List<Spawner> fillSpawn(String group, List<SpawnTemplate> templateList) {
        if (Config.DONTLOADSPAWN)
            return Collections.emptyList();

        List<Spawner> spawnerList = _spawns.get(group);
        if (spawnerList == null)
            _spawns.put(group, spawnerList = new ArrayList<Spawner>(templateList.size()));

        for (SpawnTemplate template : templateList) {
            HardSpawner spawner = new HardSpawner(template);
            spawnerList.add(spawner);

            NpcTemplate npcTemplate = NpcHolder.getInstance().getTemplate(spawner.getCurrentNpcId());

            if (Config.RATE_MOB_SPAWN > 1 && npcTemplate.getInstanceClass() == MonsterInstance.class && npcTemplate.level >= Config.RATE_MOB_SPAWN_MIN_LEVEL && npcTemplate.level <= Config.RATE_MOB_SPAWN_MAX_LEVEL)
                spawner.setAmount(template.getCount() * Config.RATE_MOB_SPAWN);
            else
                spawner.setAmount(template.getCount());

            spawner.setRespawnDelay(template.getRespawn(), template.getRespawnRandom());
            spawner.setReflection(ReflectionManager.DEFAULT);
            spawner.setRespawnTime(0);

            if (npcTemplate.isRaid && group.equals(PeriodOfDay.NONE.name()))
                RaidBossSpawnManager.getInstance().addNewSpawn(npcTemplate.getNpcId(), spawner);
        }

        return spawnerList;
    }

    public void spawnAll() {
        spawn(PeriodOfDay.NONE.name());
        spawnWalkers();
        if (Config.ALLOW_EVENT_GATEKEEPER)
            spawn("event_gatekeeper");
        if (!Config.ALLOW_CLASS_MASTERS_LIST.isEmpty())
            spawn("class_master");
    }

    public void spawn(String group) {
        List<Spawner> spawnerList = _spawns.get(group);
        if (spawnerList == null)
            return;

        int npcSpawnCount = 0;

        for (Spawner spawner : spawnerList) {
            npcSpawnCount += spawner.init();

            if (npcSpawnCount % 1000 == 0 && npcSpawnCount != 0)
                _log.info("SpawnManager: spawned " + npcSpawnCount + " npc for group: " + group);
        }
        _log.info("SpawnManager: spawned " + npcSpawnCount + " npc; spawns: " + spawnerList.size() + "; group: " + group);
    }

    private void spawnWalkers() {
        despawnWalkers();
        long spawned = 0;
        for (WalkerRouteTemplate route : WalkerRoutesHolder.getInstance().getSpawns()) {
            if (route.getPointsCount() <= 0) {
                _log.warn("Cannot load walker without route points(Routes == 0). Skipped.");
                continue;
            }
            ++spawned;
            NpcInstance npc = NpcUtils.spawnSingle(route.getNpcId(), route.getPoints().get(0).getLoc());
            npc.setWalkerRouteTemplate(route);
            _walkers.add(npc);
        }
        _log.info("SpawnManager: Spawned " + spawned + " walker routes.");
    }

    public void despawn(String group) {
        List<Spawner> spawnerList = _spawns.get(group);
        if (spawnerList == null)
            return;

        for (Spawner spawner : spawnerList)
            spawner.deleteAll();
    }

    private void despawnWalkers() {
        for (NpcInstance npc : _walkers)
            npc.deleteMe();
        _walkers.clear();
    }

    public List<Spawner> getSpawners(String group) {
        List<Spawner> list = _spawns.get(group);
        return list == null ? Collections.<Spawner>emptyList() : list;
    }

    public void reloadAll() {
        RaidBossSpawnManager.getInstance().cleanUp();
        for (List<Spawner> spawnerList : _spawns.values())
            for (Spawner spawner : spawnerList)
                spawner.deleteAll();

        RaidBossSpawnManager.getInstance().reloadBosses();

        spawnAll();

        //FIXME [VISTALL] придумать другой способ
        int mode = 0;
        _listeners.onPeriodChange(mode);

        if (GameTimeController.getInstance().isNowNight())
            _listeners.onNight();
        else
            _listeners.onDay();
        despawnWalkers();
        spawnWalkers();
    }
}