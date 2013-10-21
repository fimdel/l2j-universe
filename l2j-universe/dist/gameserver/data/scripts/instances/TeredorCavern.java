package instances;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.utils.Location;

public class TeredorCavern extends Reflection
{
	private static final Logger _log = LoggerFactory.getLogger(TeredorCavern.class);
	private static int Teredor = 25785;
	private static Location TeredorSpawnCoords= new Location (176160, -185200, -3800);

	@Override
	protected void onCreate()
	{
		super.onCreate();
		_log.info("Teredor Spawned...");
		addSpawnWithoutRespawn(Teredor, TeredorSpawnCoords, 0);
	}
}