package lineage2.gameserver.instancemanager;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author KilRoy
 */
public class SoHManager
{
	private static final Logger _log = LoggerFactory.getLogger(SoHManager.class);
	private static SoHManager _instance;
	private static final String SPAWN_GROUP = "soh_all1";
	private static final String SPAWN_GROUP2 = "soh_all3";
	private static final long SOH_OPEN_TIME = 24 * 60 * 60 * 1000L;
	private static Zone _zone;

	public static SoHManager getInstance()
	{
		if (_instance == null)
			_instance = new SoHManager();
		return _instance;
	}

	public SoHManager()
	{
		_log.info("Seed of Hellfire Manager: Loaded.");
		_zone = ReflectionUtils.getZone("[inner_hellfire01]");
		checkStageAndSpawn();
		if (!isSeedOpen())
			openSeed(getOpenedTime());
	}

	private static Zone getZone()
	{
		return _zone;
	}

	public static long getOpenedTime()
	{
		if (getCurrentStage() != 2)
			return 0;
		return ServerVariables.getLong("SoH_opened", 0) * 1000L - System.currentTimeMillis();
	}

	public static boolean isSeedOpen()
	{
		return getOpenedTime() > 0;
	}

	public static void setCurrentStage(int stage)
	{
		if (getCurrentStage() == stage)
			return;
		if (stage == 2)
			openSeed(SOH_OPEN_TIME);
		else if (isSeedOpen())
			closeSeed();
		ServerVariables.set("SoH_stage", stage);
		checkStageAndSpawn();
		_log.info("Seed of Hellfire Manager: Set to stage " + stage);
	}

	public static int getCurrentStage()
	{
		return ServerVariables.getInt("SoH_stage", 1);
	}

	public static void checkStageAndSpawn()
	{
		SpawnManager.getInstance().despawn(SPAWN_GROUP);
		SpawnManager.getInstance().despawn(SPAWN_GROUP2);
		switch (getCurrentStage())
		{
			case 1:
				SpawnManager.getInstance().spawn(SPAWN_GROUP);
				break;
			case 2:
				SpawnManager.getInstance().spawn(SPAWN_GROUP);
				break;
			default:
				SpawnManager.getInstance().spawn(SPAWN_GROUP);
				break;
		}
	}

	public static void openSeed(long timelimit)
	{
		if (timelimit <= 0)
			return;

		ServerVariables.unset("Tauti_kills");
		ServerVariables.set("SoH_opened", (System.currentTimeMillis() + timelimit) / 1000L);
		_log.info("Seed of Hellfire Manager: Opening the seed for " + Util.formatTime((int) timelimit / 1000));
		SpawnManager.getInstance().spawn(SPAWN_GROUP);

		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl() throws Exception
			{
				closeSeed();
				setCurrentStage(1);
			}
		}, timelimit);
	}

	public static void closeSeed()
	{
		_log.info("Seed of Hellfire Manager: Closing the seed.");
		ServerVariables.unset("SoH_opened");
		SpawnManager.getInstance().despawn(SPAWN_GROUP);

		for (Playable p : getZone().getInsidePlayables())
			p.teleToLocation(getZone().getRestartPoints().get(0));
	}
}