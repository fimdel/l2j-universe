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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BloodAltarManager
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(BloodAltarManager.class);
	/**
	 * Field _instance.
	 */
	private static BloodAltarManager _instance;
	/**
	 * Field delay.
	 */
	private static final long delay = 30 * 60 * 1000L;
	/**
	 * Field bossRespawnTimer.
	 */
	static long bossRespawnTimer = 0;
	/**
	 * Field bossesSpawned.
	 */
	static boolean bossesSpawned = false;
	/**
	 * Field bossGroups.
	 */
	private static final String[] bossGroups =
	{
		"bloodaltar_boss_aden",
		"bloodaltar_boss_darkelf",
		"bloodaltar_boss_dion",
		"bloodaltar_boss_dwarw",
		"bloodaltar_boss_giran",
		"bloodaltar_boss_gludin",
		"bloodaltar_boss_gludio",
		"bloodaltar_boss_goddart",
		"bloodaltar_boss_heine",
		"bloodaltar_boss_orc",
		"bloodaltar_boss_oren",
		"bloodaltar_boss_schutgart"
	};
	
	/**
	 * Method getInstance.
	 * @return BloodAltarManager
	 */
	public static BloodAltarManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new BloodAltarManager();
		}
		return _instance;
	}
	
	/**
	 * Constructor for BloodAltarManager.
	 */
	public BloodAltarManager()
	{
		_log.info("Blood Altar Manager: Initializing...");
		manageNpcs(true);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				if (Rnd.chance(30) && (bossRespawnTimer < System.currentTimeMillis()))
				{
					if (!bossesSpawned)
					{
						manageNpcs(false);
						manageBosses(true);
						bossesSpawned = true;
					}
					else
					{
						manageBosses(false);
						manageNpcs(true);
						bossesSpawned = false;
					}
				}
			}
		}, delay, delay);
	}
	
	/**
	 * Method manageNpcs.
	 * @param spawnAlive boolean
	 */
	static void manageNpcs(boolean spawnAlive)
	{
		if (spawnAlive)
		{
			SpawnManager.getInstance().despawn("bloodaltar_dead_npc");
			SpawnManager.getInstance().spawn("bloodaltar_alive_npc");
		}
		else
		{
			SpawnManager.getInstance().despawn("bloodaltar_alive_npc");
			SpawnManager.getInstance().spawn("bloodaltar_dead_npc");
		}
	}
	
	/**
	 * Method manageBosses.
	 * @param spawn boolean
	 */
	static void manageBosses(boolean spawn)
	{
		if (spawn)
		{
			for (String s : bossGroups)
			{
				SpawnManager.getInstance().spawn(s);
			}
		}
		else
		{
			bossRespawnTimer = System.currentTimeMillis() + (4 * 3600 * 1000L);
			for (String s : bossGroups)
			{
				SpawnManager.getInstance().despawn(s);
			}
		}
	}
}
