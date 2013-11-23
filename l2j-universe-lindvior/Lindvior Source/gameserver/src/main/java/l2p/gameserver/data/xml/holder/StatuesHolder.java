package l2p.gameserver.data.xml.holder;

import l2p.commons.data.xml.AbstractHolder;
import l2p.gameserver.model.WorldStatistic.CategoryType;
import l2p.gameserver.utils.Location;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 29.10.12
 * Time: 16:43
 */
public class StatuesHolder extends AbstractHolder {
    private static StatuesHolder _instance;
    private Map<CategoryType, List<Location>> spawnLocations;

    private StatuesHolder() {
        spawnLocations = new HashMap<CategoryType, List<Location>>();
    }

    public static StatuesHolder getInstance() {
        if (_instance == null) {
            _instance = new StatuesHolder();
        }
        return _instance;
    }

    @Override
    public int size() {
        return spawnLocations.size();
    }

    @Override
    public void clear() {
        spawnLocations.clear();
    }

    public void addSpawnInfo(CategoryType categoryType, List<Location> locations) {
        spawnLocations.put(categoryType, locations);
    }

    public Map<CategoryType, List<Location>> getSpawnLocations() {
        return Collections.unmodifiableMap(spawnLocations);
    }
}
