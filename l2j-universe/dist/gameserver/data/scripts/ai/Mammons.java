/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package ai;

import java.util.concurrent.ScheduledFuture;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Mammons extends Functions implements ScriptFile
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Mammons.class);
	/**
	 * Field MAMMON_PRIEST_ID. (value is 33511)
	 */
	private static final int MAMMON_PRIEST_ID = 33511;
	/**
	 * Field MAMMON_MERCHANT_ID. (value is 31113)
	 */
	private static final int MAMMON_MERCHANT_ID = 31113;
	/**
	 * Field MAMMON_BLACKSMITH_ID. (value is 31126)
	 */
	private static final int MAMMON_BLACKSMITH_ID = 31126;
	/**
	 * Field PORT_TIME.
	 */
	private static final int PORT_TIME = 10 * 60 * 1000;
	/**
	 * Field PriestNpc.
	 */
	static NpcInstance PriestNpc;
	/**
	 * Field MerchantNpc.
	 */
	static NpcInstance MerchantNpc;
	/**
	 * Field BlacksmithNpc.
	 */
	static NpcInstance BlacksmithNpc;
	/**
	 * Field _mammonTeleportTask.
	 */
	private static ScheduledFuture<?> _mammonTeleportTask;
	/**
	 * Field mammonText.
	 */
	static final NpcString[] mammonText =
	{
		NpcString.RULERS_OF_THE_SEAL_I_BRING_YOU_WONDROUS_GIFTS,
		NpcString.RULERS_OF_THE_SEAL_I_HAVE_SOME_EXCELLENT_WEAPONS_TO_SHOW_YOU,
		NpcString.IVE_BEEN_SO_BUSY_LATELY_IN_ADDITION_TO_PLANNING_MY_TRIP
	};
	/**
	 * Field MAMMON_PRIEST_POINTS.
	 */
	static final Location[] MAMMON_PRIEST_POINTS =
	{
		new Location(16403, 144843, -3016, 27931),
		new Location(81284, 150155, -3528),
		new Location(114478, 217596, -3624, 0),
		new Location(79992, 56808, -1585),
		new Location(-84744, 151688, -3154, 0),
		new Location(-12344, 121736, -3014, 0),
		new Location(120392, 76488, -2167, 0),
		new Location(146984, 29624, -2294, 0),
		new Location(42856, -41432, -2212, 0),
		new Location(144632, -54136, -3006, 0),
		new Location(90024, -143672, -1565, 0),
	};
	/**
	 * Field MAMMON_MERCHANT_POINTS.
	 */
	static final Location[] MAMMON_MERCHANT_POINTS =
	{
		new Location(16380, 144784, -3016, 27931),
		new Location(81272, 150041, -3528),
		new Location(114482, 217538, -3624, 0),
		new Location(79992, 56856, -1585),
		new Location(-84744, 151656, -3154, 0),
		new Location(-12344, 121784, -3014, 0),
		new Location(120344, 76520, -2167, 0),
		new Location(146984, 29672, -2294, 0),
		new Location(42968, -41384, -2213, 0),
		new Location(144552, -54104, -3006, 0),
		new Location(89944, -143688, -1565, 0),
	};
	/**
	 * Field MAMMON_BLACKSMITH_POINTS.
	 */
	static final Location[] MAMMON_BLACKSMITH_POINTS =
	{
		new Location(16335, 144696, -3024, 27931),
		new Location(81266, 150091, -3528),
		new Location(114484, 217462, -3624, 0),
		new Location(79992, 56920, -1585),
		new Location(-84744, 151608, -3154, 0),
		new Location(-12344, 121640, -3014, 0),
		new Location(120296, 76536, -2167, 0),
		new Location(146984, 29736, -2294, 0),
		new Location(43032, -41336, -2214, 0),
		new Location(144472, -54088, -3006, 0),
		new Location(89912, -143752, -1566, 0),
	};
	
	/**
	 * Method SpawnMammons.
	 */
	public void SpawnMammons()
	{
		final int firstTown = Rnd.get(MAMMON_PRIEST_POINTS.length);
		NpcTemplate template = NpcHolder.getInstance().getTemplate(MAMMON_PRIEST_ID);
		SimpleSpawner sp = new SimpleSpawner(template);
		sp.setLoc(MAMMON_PRIEST_POINTS[firstTown]);
		sp.setAmount(1);
		sp.setRespawnDelay(0);
		PriestNpc = sp.doSpawn(true);
		template = NpcHolder.getInstance().getTemplate(MAMMON_MERCHANT_ID);
		sp = new SimpleSpawner(template);
		sp.setLoc(MAMMON_MERCHANT_POINTS[firstTown]);
		sp.setAmount(1);
		sp.setRespawnDelay(0);
		MerchantNpc = sp.doSpawn(true);
		template = NpcHolder.getInstance().getTemplate(MAMMON_BLACKSMITH_ID);
		sp = new SimpleSpawner(template);
		sp.setLoc(MAMMON_BLACKSMITH_POINTS[firstTown]);
		sp.setAmount(1);
		sp.setRespawnDelay(0);
		BlacksmithNpc = sp.doSpawn(true);
	}
	
	/**
	 * @author Mobius
	 */
	public static class TeleportMammons implements Runnable
	{
		/**
		 * Method run.
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			Functions.npcShout(BlacksmithNpc, mammonText[Rnd.get(mammonText.length)]);
			final int nextTown = Rnd.get(MAMMON_PRIEST_POINTS.length);
			PriestNpc.teleToLocation(MAMMON_PRIEST_POINTS[nextTown]);
			MerchantNpc.teleToLocation(MAMMON_MERCHANT_POINTS[nextTown]);
			BlacksmithNpc.teleToLocation(MAMMON_BLACKSMITH_POINTS[nextTown]);
		}
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		SpawnMammons();
		_mammonTeleportTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new TeleportMammons(), PORT_TIME, PORT_TIME);
		_log.info("Loaded AI: Mammons Teleporter");
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		// empty method
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		if (_mammonTeleportTask != null)
		{
			_mammonTeleportTask.cancel(true);
			_mammonTeleportTask = null;
		}
	}
}
