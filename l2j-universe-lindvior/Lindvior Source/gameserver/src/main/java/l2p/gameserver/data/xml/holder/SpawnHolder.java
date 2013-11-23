package l2p.gameserver.data.xml.holder;

import l2p.commons.data.xml.AbstractHolder;
import l2p.gameserver.templates.spawn.SpawnTemplate;

import java.util.*;

/**
 * @author VISTALL
 * @date 18:38/10.12.2010
 */
public final class SpawnHolder extends AbstractHolder {
    private static final SpawnHolder _instance = new SpawnHolder();

    private Map<String, List<SpawnTemplate>> _spawns = new HashMap<String, List<SpawnTemplate>>();

    public static SpawnHolder getInstance() {
        return _instance;
    }

    public void addSpawn(String group, SpawnTemplate spawn) {
        List<SpawnTemplate> spawns = _spawns.get(group);
        if (spawns == null)
            _spawns.put(group, spawns = new ArrayList<SpawnTemplate>());
        spawns.add(spawn);
    }

    public List<SpawnTemplate> getSpawn(String name) {
        List<SpawnTemplate> template = _spawns.get(name);
        return template == null ? Collections.<SpawnTemplate>emptyList() : template;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int size() {
        int i = 0;
        for (List l : _spawns.values())
            i += l.size();

        return i;
    }

    @Override
    public void clear() {
        _spawns.clear();
    }

    public Map<String, List<SpawnTemplate>> getSpawns() {
        return _spawns;
    }
}
