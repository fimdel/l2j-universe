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
package lineage2.gameserver.instancemanager;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.Playable;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import lineage2.gameserver.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SoIManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SoIManager.class);
	/**
	 * Field _instance.
	 */
	private static SoIManager _instance = null;
	/**
	 * Field SOI_OPEN_TIME.
	 */
	private static final long SOI_OPEN_TIME = 24 * 60 * 60 * 1000L;
	/**
	 * Field openSeedTeleportLocs.
	 */
	private static Location[] openSeedTeleportLocs =
	{
		new Location(-179537, 209551, -15504),
		new Location(-179779, 212540, -15520),
		new Location(-177028, 211135, -15520),
		new Location(-176355, 208043, -15520),
		new Location(-179284, 205990, -15520),
		new Location(-182268, 208218, -15520),
		new Location(-182069, 211140, -15520),
		new Location(-176036, 210002, -11948),
		new Location(-176039, 208203, -11949),
		new Location(-183288, 208205, -11939),
		new Location(-183290, 210004, -11939),
		new Location(-187776, 205696, -9536),
		new Location(-186327, 208286, -9536),
		new Location(-184429, 211155, -9536),
		new Location(-182811, 213871, -9504),
		new Location(-180921, 216789, -9536),
		new Location(-177264, 217760, -9536),
		new Location(-173727, 218169, -9536)
	};
	/**
	 * Field _zone.
	 */
	private static Zone _zone = null;
	
	/**
	 * Method getInstance.
	 * @return SoIManager
	 */
	public static SoIManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new SoIManager();
		}
		return _instance;
	}
	
	/**
	 * Constructor for SoIManager.
	 */
	public SoIManager()
	{
		_log.info("Seed of Infinity Manager: Loaded. Current stage is: " + getCurrentStage());
		_zone = ReflectionUtils.getZone("[inner_undying01]");
		checkStageAndSpawn();
		if (isSeedOpen())
		{
			openSeed(getOpenedTime());
		}
	}
	
	/**
	 * Method getCurrentStage.
	 * @return int
	 */
	public static int getCurrentStage()
	{
		return ServerVariables.getInt("SoI_stage", 1);
	}
	
	/**
	 * Method getOpenedTime.
	 * @return long
	 */
	public static long getOpenedTime()
	{
		if (getCurrentStage() != 3)
		{
			return 0;
		}
		return (ServerVariables.getLong("SoI_opened", 0) * 1000L) - System.currentTimeMillis();
	}
	
	/**
	 * Method setCurrentStage.
	 * @param stage int
	 */
	public static void setCurrentStage(int stage)
	{
		if (getCurrentStage() == stage)
		{
			return;
		}
		if (stage == 3)
		{
			openSeed(SOI_OPEN_TIME);
		}
		else if (isSeedOpen())
		{
			closeSeed();
		}
		ServerVariables.set("SoI_stage", stage);
		setCohemenesCount(0);
		setEkimusCount(0);
		setHoEDefCount(0);
		checkStageAndSpawn();
		_log.info("Seed of Infinity Manager: Set to stage " + stage);
	}
	
	/**
	 * Method isSeedOpen.
	 * @return boolean
	 */
	public static boolean isSeedOpen()
	{
		return getOpenedTime() > 0;
	}
	
	/**
	 * Method openSeed.
	 * @param time long
	 */
	public static void openSeed(long time)
	{
		if (time <= 0)
		{
			return;
		}
		ServerVariables.set("SoI_opened", (System.currentTimeMillis() + time) / 1000L);
		_log.info("Seed of Infinity Manager: Opening the seed for " + Util.formatTime((int) time / 1000));
		spawnOpenedSeed();
		ReflectionUtils.getDoor(14240102).openMe();
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				closeSeed();
				setCurrentStage(4);
			}
		}, time);
	}
	
	/**
	 * Method closeSeed.
	 */
	public static void closeSeed()
	{
		_log.info("Seed of Infinity Manager: Closing the seed.");
		ServerVariables.unset("SoI_opened");
		SpawnManager.getInstance().despawn("soi_hos_middle_seeds");
		SpawnManager.getInstance().despawn("soi_hoe_middle_seeds");
		SpawnManager.getInstance().despawn("soi_hoi_middle_seeds");
		SpawnManager.getInstance().despawn("soi_all_middle_stable_tumor");
		ReflectionUtils.getDoor(14240102).closeMe();
		for (Playable p : getZone().getInsidePlayables())
		{
			p.teleToLocation(getZone().getRestartPoints().get(0));
		}
	}
	
	/**
	 * Method checkStageAndSpawn.
	 */
	public static void checkStageAndSpawn()
	{
		SpawnManager.getInstance().despawn("soi_world_closedmouths");
		SpawnManager.getInstance().despawn("soi_world_mouths");
		SpawnManager.getInstance().despawn("soi_world_abyssgaze2");
		SpawnManager.getInstance().despawn("soi_world_abyssgaze1");
		switch (getCurrentStage())
		{
			case 1:
			case 4:
				SpawnManager.getInstance().spawn("soi_world_mouths");
				SpawnManager.getInstance().spawn("soi_world_abyssgaze2");
				break;
			case 5:
				SpawnManager.getInstance().spawn("soi_world_closedmouths");
				SpawnManager.getInstance().spawn("soi_world_abyssgaze2");
				break;
			default:
				SpawnManager.getInstance().spawn("soi_world_closedmouths");
				SpawnManager.getInstance().spawn("soi_world_abyssgaze1");
				break;
		}
	}
	
	/**
	 * Method getZone.
	 * @return Zone
	 */
	private static Zone getZone()
	{
		return _zone;
	}
	
	/**
	 * Method notifyCohemenesKill.
	 */
	public static void notifyCohemenesKill()
	{
		if (getCurrentStage() == 1)
		{
			if (getCohemenesCount() < 9)
			{
				setCohemenesCount(getCohemenesCount() + 1);
			}
			else
			{
				setCurrentStage(2);
			}
		}
	}
	
	/**
	 * Method notifyEkimusKill.
	 */
	public static void notifyEkimusKill()
	{
		if (getCurrentStage() == 2)
		{
			if (getEkimusCount() < 2)
			{
				setEkimusCount(getEkimusCount() + 1);
			}
			else
			{
				setCurrentStage(3);
			}
		}
	}
	
	/**
	 * Method notifyHoEDefSuccess.
	 */
	public static void notifyHoEDefSuccess()
	{
		if (getCurrentStage() == 4)
		{
			if (getHoEDefCount() < 9)
			{
				setHoEDefCount(getHoEDefCount() + 1);
			}
			else
			{
				setCurrentStage(5);
			}
		}
	}
	
	/**
	 * Method setCohemenesCount.
	 * @param i int
	 */
	public static void setCohemenesCount(int i)
	{
		ServerVariables.set("SoI_CohemenesCount", i);
	}
	
	/**
	 * Method setEkimusCount.
	 * @param i int
	 */
	public static void setEkimusCount(int i)
	{
		ServerVariables.set("SoI_EkimusCount", i);
	}
	
	/**
	 * Method setHoEDefCount.
	 * @param i int
	 */
	public static void setHoEDefCount(int i)
	{
		ServerVariables.set("SoI_hoedefkillcount", i);
	}
	
	/**
	 * Method getCohemenesCount.
	 * @return int
	 */
	public static int getCohemenesCount()
	{
		return ServerVariables.getInt("SoI_CohemenesCount", 0);
	}
	
	/**
	 * Method getEkimusCount.
	 * @return int
	 */
	public static int getEkimusCount()
	{
		return ServerVariables.getInt("SoI_EkimusCount", 0);
	}
	
	/**
	 * Method getHoEDefCount.
	 * @return int
	 */
	public static int getHoEDefCount()
	{
		return ServerVariables.getInt("SoI_hoedefkillcount", 0);
	}
	
	/**
	 * Method spawnOpenedSeed.
	 */
	private static void spawnOpenedSeed()
	{
		SpawnManager.getInstance().spawn("soi_hos_middle_seeds");
		SpawnManager.getInstance().spawn("soi_hoe_middle_seeds");
		SpawnManager.getInstance().spawn("soi_hoi_middle_seeds");
		SpawnManager.getInstance().spawn("soi_all_middle_stable_tumor");
	}
	
	/**
	 * Method teleportInSeed.
	 * @param p Player
	 */
	public static void teleportInSeed(Player p)
	{
		p.teleToLocation(openSeedTeleportLocs[Rnd.get(openSeedTeleportLocs.length)]);
	}
}
