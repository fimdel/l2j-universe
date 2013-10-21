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
package lineage2.gameserver.instancemanager.naia;

import lineage2.commons.geometry.Polygon;
import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Territory;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public final class NaiaCoreManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(NaiaTowerManager.class);
	/**
	 * Field _instance.
	 */
	private static final NaiaCoreManager _instance = new NaiaCoreManager();
	/**
	 * Field _zone.
	 */
	static Zone _zone;
	/**
	 * Field _active.
	 */
	static boolean _active = false;
	/**
	 * Field _bossSpawned.
	 */
	private static boolean _bossSpawned = false;
	/**
	 * Field _coreTerritory.
	 */
	private static final Territory _coreTerritory = new Territory().add(new Polygon().add(-44789, 246305).add(-44130, 247452).add(-46092, 248606).add(-46790, 247414).add(-46139, 246304).setZmin(-14220).setZmax(-13800));
	/**
	 * Field fireSpore. (value is 25605)
	 */
	private static final int fireSpore = 25605;
	/**
	 * Field waterSpore. (value is 25606)
	 */
	private static final int waterSpore = 25606;
	/**
	 * Field windSpore. (value is 25607)
	 */
	private static final int windSpore = 25607;
	/**
	 * Field earthSpore. (value is 25608)
	 */
	private static final int earthSpore = 25608;
	/**
	 * Field fireEpidos. (value is 25609)
	 */
	private static final int fireEpidos = 25609;
	/**
	 * Field waterEpidos. (value is 25610)
	 */
	private static final int waterEpidos = 25610;
	/**
	 * Field windEpidos. (value is 25611)
	 */
	private static final int windEpidos = 25611;
	/**
	 * Field earthEpidos. (value is 25612)
	 */
	private static final int earthEpidos = 25612;
	/**
	 * Field teleCube. (value is 32376)
	 */
	private static final int teleCube = 32376;
	/**
	 * Field respawnDelay. (value is 120)
	 */
	private static final int respawnDelay = 120;
	/**
	 * Field coreClearTime.
	 */
	private static final long coreClearTime = 4 * 60 * 60 * 1000L;
	/**
	 * Field spawnLoc.
	 */
	private static final Location spawnLoc = new Location(-45496, 246744, -14209);
	
	/**
	 * Method getInstance.
	 * @return NaiaCoreManager
	 */
	public static final NaiaCoreManager getInstance()
	{
		return _instance;
	}
	
	/**
	 * Constructor for NaiaCoreManager.
	 */
	public NaiaCoreManager()
	{
		_zone = ReflectionUtils.getZone("[naia_core_poison]");
		_log.info("Naia Core Manager: Loaded");
	}
	
	/**
	 * Method launchNaiaCore.
	 */
	public static void launchNaiaCore()
	{
		if (isActive())
		{
			return;
		}
		_active = true;
		ReflectionUtils.getDoor(18250025).closeMe();
		_zone.setActive(true);
		spawnSpores();
		ThreadPoolManager.getInstance().schedule(new ClearCore(), coreClearTime);
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private static boolean isActive()
	{
		return _active;
	}
	
	/**
	 * Method setZoneActive.
	 * @param value boolean
	 */
	public static void setZoneActive(boolean value)
	{
		_zone.setActive(value);
	}
	
	/**
	 * Method spawnSpores.
	 */
	private static void spawnSpores()
	{
		spawnToRoom(fireSpore, 10, _coreTerritory);
		spawnToRoom(waterSpore, 10, _coreTerritory);
		spawnToRoom(windSpore, 10, _coreTerritory);
		spawnToRoom(earthSpore, 10, _coreTerritory);
	}
	
	/**
	 * Method spawnEpidos.
	 * @param index int
	 */
	public static void spawnEpidos(int index)
	{
		if (!isActive())
		{
			return;
		}
		int epidostospawn = 0;
		switch (index)
		{
			case 1:
			{
				epidostospawn = fireEpidos;
				break;
			}
			case 2:
			{
				epidostospawn = waterEpidos;
				break;
			}
			case 3:
			{
				epidostospawn = windEpidos;
				break;
			}
			case 4:
			{
				epidostospawn = earthEpidos;
				break;
			}
			default:
				break;
		}
		try
		{
			SimpleSpawner sp = new SimpleSpawner(epidostospawn);
			sp.setLoc(spawnLoc);
			sp.doSpawn(true);
			sp.stopRespawn();
			_bossSpawned = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method isBossSpawned.
	 * @return boolean
	 */
	public static boolean isBossSpawned()
	{
		return _bossSpawned;
	}
	
	/**
	 * Method removeSporesAndSpawnCube.
	 */
	public static void removeSporesAndSpawnCube()
	{
		int[] spores =
		{
			fireSpore,
			waterSpore,
			windSpore,
			earthSpore
		};
		for (NpcInstance spore : GameObjectsStorage.getAllByNpcId(spores, false))
		{
			spore.deleteMe();
		}
		try
		{
			SimpleSpawner sp = new SimpleSpawner(teleCube);
			sp.setLoc(spawnLoc);
			sp.doSpawn(true);
			sp.stopRespawn();
			Functions.npcShout(sp.getLastSpawn(), "Teleportation to Beleth Throne Room is available for 2 minutes");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class ClearCore extends RunnableImpl
	{
		/**
		 * Constructor for ClearCore.
		 */
		public ClearCore()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			int[] spores =
			{
				fireSpore,
				waterSpore,
				windSpore,
				earthSpore
			};
			int[] epidoses =
			{
				fireEpidos,
				waterEpidos,
				windEpidos,
				earthEpidos
			};
			for (NpcInstance spore : GameObjectsStorage.getAllByNpcId(spores, false))
			{
				spore.deleteMe();
			}
			for (NpcInstance epidos : GameObjectsStorage.getAllByNpcId(epidoses, false))
			{
				epidos.deleteMe();
			}
			_active = false;
			ReflectionUtils.getDoor(18250025).openMe();
			_zone.setActive(false);
		}
	}
	
	/**
	 * Method spawnToRoom.
	 * @param mobId int
	 * @param count int
	 * @param territory Territory
	 */
	private static void spawnToRoom(int mobId, int count, Territory territory)
	{
		for (int i = 0; i < count; i++)
		{
			try
			{
				SimpleSpawner sp = new SimpleSpawner(mobId);
				sp.setLoc(Territory.getRandomLoc(territory).setH(Rnd.get(65535)));
				sp.setRespawnDelay(respawnDelay, 30);
				sp.setAmount(1);
				sp.doSpawn(true);
				sp.startRespawn();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
