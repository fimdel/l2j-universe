package lineage2.gameserver.data.xml.parser;

import org.dom4j.Element;
import lineage2.commons.data.xml.AbstractFileParser;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.StatuesHolder;
import lineage2.gameserver.model.worldstatistics.CategoryType;
import lineage2.gameserver.utils.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class StatuesSpawnParser extends AbstractFileParser<StatuesHolder>
{
	private static StatuesSpawnParser ourInstance = new StatuesSpawnParser();

	private StatuesSpawnParser()
	{
		super(StatuesHolder.getInstance());
	}	

	public static StatuesSpawnParser getInstance()
	{
		return ourInstance;
	}

	@Override
	public File getXMLFile()
	{
		return new File(Config.DATAPACK_ROOT, "data/xml/StatuesSpawn/StatuesSpawnData.xml");
	}

	@Override
	public String getDTDFileName()
	{
		return "StatuesSpawnData.dtd";
	}

	@Override
	protected void readData(Element rootElement) throws Exception
	{

		for (Element statuesElement : rootElement.elements())
		{
			int type = Integer.parseInt(statuesElement.attributeValue("type"));
			CategoryType categoryType = CategoryType.getCategoryById(type, 0);

			List<Location> locations = new ArrayList<>();
			for (Element spawnElement : statuesElement.elements())
			{
				String[] loc = spawnElement.attributeValue("loc").split(",");
				locations.add(new Location(Integer.parseInt(loc[0]), Integer.parseInt(loc[1]), Integer.parseInt(loc[2]), Integer.parseInt(loc[3])));
			}
			StatuesHolder.getInstance().addSpawnInfo(categoryType, locations);
		}
	}
}
