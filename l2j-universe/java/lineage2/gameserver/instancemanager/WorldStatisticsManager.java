package lineage2.gameserver.instancemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lineage2.gameserver.dao.WorldStatisticDAO;
import lineage2.gameserver.data.xml.holder.StatuesHolder;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.WinnerStatueInstance;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.model.worldstatistics.CharacterStatistic;
import lineage2.gameserver.model.worldstatistics.CharacterStatisticElement;
import lineage2.gameserver.templates.StatuesSpawnTemplate;
import lineage2.gameserver.utils.Location;

public class WorldStatisticsManager
{
	public static final int STATISTIC_TOP_PLAYER_LIMIT = 100;
	public static final int STATUES_TOP_PLAYER_LIMIT = 5;
	private static WorldStatisticsManager _instance;
	private final List<WinnerStatueInstance> spawnedStatues;

	private WorldStatisticsManager()
	{
		spawnedStatues = new ArrayList<>();
		spawnStatues();
	}

	public static WorldStatisticsManager getInstance()
	{
		if (_instance == null)
			_instance = new WorldStatisticsManager();
		return _instance;
	}

	private void spawnStatues()
	{
		Map<CategoryType, List<Location>> spawnLocations = StatuesHolder.getInstance().getSpawnLocations();
		List<StatuesSpawnTemplate> templates = WorldStatisticDAO.getInstance().getStatueTemplates(spawnLocations.keySet());
		for (StatuesSpawnTemplate template : templates)
		{
			List<Location> locations = spawnLocations.get(template.getCategoryType());
			for (Location loc : locations)
			{
				WinnerStatueInstance statue = new WinnerStatueInstance(IdFactory.getInstance().getNextId(), template);
				statue.setLoc(loc);
				statue.spawnMe();
				spawnedStatues.add(statue);
			}
		}
	}

	private void despawnStatues()
	{
		for (WinnerStatueInstance statue : spawnedStatues)
		{
			statue.deleteMe();
		}
		spawnedStatues.clear();
	}

	public final void updateStat(Player player, CategoryType categoryType, int subCategory, long valueAdd)
	{
		categoryType = CategoryType.getCategoryById(categoryType.getClientId(), subCategory);
		if (categoryType != null)
			WorldStatisticDAO.getInstance().updateStatisticFor(player, categoryType, valueAdd);
	}

	public void updateStat(Player player, CategoryType categoryType, long valueAdd)
	{
		updateStat(player, categoryType, 0, valueAdd);
	}

	public List<CharacterStatisticElement> getCurrentStatisticsForPlayer(int charId)
	{
		return WorldStatisticDAO.getInstance().getPersonalStatisticFor(charId);
	}

	public List<CharacterStatistic> getStatisticTop(CategoryType cat, boolean global, int limit)
	{
		return WorldStatisticDAO.getInstance().getStatisticForCategory(cat, global, limit);
	}

	public void resetMonthlyStatistic()
	{
		despawnStatues();
		WorldStatisticDAO.getInstance().recalculateWinners();
		spawnStatues();
	}

	public List<CharacterStatistic> getWinners(CategoryType categoryType, boolean global, int limit)
	{
		return WorldStatisticDAO.getInstance().getWinners(categoryType, global, limit);
	}
}
