package lineage2.gameserver.model.entity.museum;

/**
 * @author ALF
 * @date 21.08.2012
 */
public class WorldStatistic
{
	private static WorldStatistic _instance = new WorldStatistic();

	public static WorldStatistic getInstance()
	{
		return _instance;
	}
}
