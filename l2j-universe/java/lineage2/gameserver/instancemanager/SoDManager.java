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
public class SoDManager
{
	/**
	 * Field SPAWN_GROUP. (value is ""sod_free"")
	 */
	private static final String SPAWN_GROUP = "sod_free";
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SoDManager.class);
	/**
	 * Field _instance.
	 */
	private static SoDManager _instance;
	/**
	 * Field SOD_OPEN_TIME.
	 */
	private static long SOD_OPEN_TIME = 12 * 60 * 60 * 1000L;
	/**
	 * Field _zone.
	 */
	private static Zone _zone;
	/**
	 * Field _isOpened.
	 */
	private static boolean _isOpened = false;
	
	/**
	 * Method getInstance.
	 * @return SoDManager
	 */
	public static SoDManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new SoDManager();
		}
		return _instance;
	}
	
	/**
	 * Constructor for SoDManager.
	 */
	public SoDManager()
	{
		_log.info("Seed of Destruction Manager: Loaded");
		_zone = ReflectionUtils.getZone("[inner_destruction01]");
		if (!isAttackStage())
		{
			openSeed(getOpenedTimeLimit());
		}
	}
	
	/**
	 * Method isAttackStage.
	 * @return boolean
	 */
	public static boolean isAttackStage()
	{
		return getOpenedTimeLimit() <= 0;
	}
	
	/**
	 * Method addTiatKill.
	 */
	public static void addTiatKill()
	{
		if (!isAttackStage())
		{
			return;
		}
		if (getTiatKills() < 9)
		{
			ServerVariables.set("Tial_kills", getTiatKills() + 1);
		}
		else
		{
			openSeed(SOD_OPEN_TIME);
		}
	}
	
	/**
	 * Method getTiatKills.
	 * @return int
	 */
	public static int getTiatKills()
	{
		return ServerVariables.getInt("Tial_kills", 0);
	}
	
	/**
	 * Method getOpenedTimeLimit.
	 * @return long
	 */
	private static long getOpenedTimeLimit()
	{
		return (ServerVariables.getLong("SoD_opened", 0) * 1000L) - System.currentTimeMillis();
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
	 * Method teleportIntoSeed.
	 * @param p Player
	 */
	public static void teleportIntoSeed(Player p)
	{
		p.teleToLocation(new Location(-245800, 220488, -12112));
	}
	
	/**
	 * Method handleDoors.
	 * @param doOpen boolean
	 */
	public static void handleDoors(boolean doOpen)
	{
		for (int i = 12240003; i <= 12240031; i++)
		{
			if (doOpen)
			{
				ReflectionUtils.getDoor(i).openMe();
			}
			else
			{
				ReflectionUtils.getDoor(i).closeMe();
			}
		}
	}
	
	/**
	 * Method openSeed.
	 * @param timelimit long
	 */
	public static void openSeed(long timelimit)
	{
		if (_isOpened)
		{
			return;
		}
		_isOpened = true;
		ServerVariables.unset("Tial_kills");
		ServerVariables.set("SoD_opened", (System.currentTimeMillis() + timelimit) / 1000L);
		_log.info("Seed of Destruction Manager: Opening the seed for " + Util.formatTime((int) timelimit / 1000));
		SpawnManager.getInstance().spawn(SPAWN_GROUP);
		handleDoors(true);
		ThreadPoolManager.getInstance().schedule(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				closeSeed();
			}
		}, timelimit);
	}
	
	/**
	 * Method closeSeed.
	 */
	public static void closeSeed()
	{
		if (!_isOpened)
		{
			return;
		}
		_isOpened = false;
		_log.info("Seed of Destruction Manager: Closing the seed.");
		ServerVariables.unset("SoD_opened");
		SpawnManager.getInstance().despawn(SPAWN_GROUP);
		for (Playable p : getZone().getInsidePlayables())
		{
			p.teleToLocation(getZone().getRestartPoints().get(0));
		}
		handleDoors(false);
	}
}
