package lineage2.gameserver.data.xml.holder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lineage2.commons.data.xml.AbstractHolder;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.utils.Location;

public class StatuesHolder extends AbstractHolder
{
	private static StatuesHolder _instance;
	private Map<CategoryType, List<Location>> spawnLocations;

	private StatuesHolder()
	{
		spawnLocations = new HashMap<>();
	}

	public static StatuesHolder getInstance()
	{
		if (_instance == null)
		{
			_instance = new StatuesHolder();
		}
		return _instance;
	}

	@Override
	public int size()
	{
		return spawnLocations.size();
	}

	@Override
	public void clear()
	{
		spawnLocations.clear();
	}

	public void addSpawnInfo(CategoryType categoryType, List<Location> locations)
	{
		spawnLocations.put(categoryType, locations);
	}

	public Map<CategoryType, List<Location>> getSpawnLocations()
	{
		return Collections.unmodifiableMap(spawnLocations);
	}
}
