package l2p.gameserver.instancemanager;

import l2p.gameserver.dao.WorldStatisticDAO;
import l2p.gameserver.data.xml.holder.StatuesHolder;
import l2p.gameserver.idfactory.IdFactory;
import l2p.gameserver.model.Player;
import l2p.gameserver.model.WinnerStatueInstance;
import l2p.gameserver.model.WorldStatistic.CategoryType;
import l2p.gameserver.model.WorldStatistic.CharacterStatistic;
import l2p.gameserver.model.WorldStatistic.CharacterStatisticElement;
import l2p.gameserver.templates.StatuesSpawnTemplate;
import l2p.gameserver.utils.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Darvin
 * Date: 29.10.12
 * Time: 16:39
 */
public class WorldStatisticsManager {
    public static final int STATISTIC_TOP_PLAYER_LIMIT = 100;
    public static final int STATUES_TOP_PLAYER_LIMIT = 5;
    private static WorldStatisticsManager _instance;
    private final List<WinnerStatueInstance> spawnedStatues;

    private WorldStatisticsManager() {
        spawnedStatues = new ArrayList<WinnerStatueInstance>();
        spawnStatues();
    }

    public static WorldStatisticsManager getInstance() {
        if (_instance == null)
            _instance = new WorldStatisticsManager();
        return _instance;
    }

    private void spawnStatues() {
        Map<CategoryType, List<Location>> spawnLocations = StatuesHolder.getInstance().getSpawnLocations();
        List<StatuesSpawnTemplate> templates = WorldStatisticDAO.getInstance().getStatueTemplates(spawnLocations.keySet());
        for (StatuesSpawnTemplate template : templates) {
            List<Location> locations = spawnLocations.get(template.getCategoryType());
            for (Location loc : locations) {
                WinnerStatueInstance statue = new WinnerStatueInstance(IdFactory.getInstance().getNextId(), template);
                statue.setLoc(loc);
                statue.spawnMe();
                spawnedStatues.add(statue);
            }
        }
    }

    private void despawnStatues() {
        for (WinnerStatueInstance statue : spawnedStatues) {
            statue.deleteMe();
        }
        spawnedStatues.clear();
    }

    public final void updateStat(Player player, CategoryType categoryType, int subCategory, long valueAdd) {
        categoryType = CategoryType.getCategoryById(categoryType.getClientId(), subCategory);
        if (categoryType != null)
            WorldStatisticDAO.getInstance().updateStatisticFor(player, categoryType, subCategory, valueAdd);
    }

    //  public void updateStat(Player player, CategoryType categoryType,  long valueAdd) {
//        updateStat(player, categoryType, 0, valueAdd);
//    }

    public List<CharacterStatisticElement> getCurrentStatisticsForPlayer(int charId) {
        return WorldStatisticDAO.getInstance().getPersonalStatisticFor(charId);
    }

    public List<CharacterStatistic> getStatisticTop(CategoryType cat, boolean global, int limit) {
        return WorldStatisticDAO.getInstance().getStatisticForCategory(cat, global, limit);
    }

    public void resetMonthlyStatistic() {
        despawnStatues();
        WorldStatisticDAO.getInstance().recalculateWinners();
        spawnStatues();
    }

    public List<CharacterStatistic> getWinners(CategoryType categoryType, boolean global, int limit) {
        return WorldStatisticDAO.getInstance().getWinners(categoryType, global, limit);
    }
}
