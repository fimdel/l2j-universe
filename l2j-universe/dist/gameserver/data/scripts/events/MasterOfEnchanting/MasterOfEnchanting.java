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
package events.MasterOfEnchanting;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.Announcements;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class MasterOfEnchanting extends Functions implements ScriptFile, OnPlayerEnterListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(MasterOfEnchanting.class);
	/**
	 * Field EVENT_NAME. (value is ""MasterOfEnchanting"")
	 */
	private static final String EVENT_NAME = "MasterOfEnchanting";
	/**
	 * Field EVENT_MANAGER_ID.
	 */
	private static final int EVENT_MANAGER_ID = 32599;
	/**
	 * Field _spawns.
	 */
	private static final List<SimpleSpawner> _spawns = new ArrayList<>();
	/**
	 * Field _active.
	 */
	private static boolean _active = false;
	
	/**
	 * Method spawnEventManagers.
	 */
	private void spawnEventManagers()
	{
		final int[][] EVENT_MANAGERS =
		{
			{
				-119494,
				44882,
				360,
				24576
			},
			{
				-117239,
				46842,
				360,
				49151
			},
			{
				-84023,
				243051,
				-3728,
				4096
			},
			{
				-84411,
				244813,
				-3728,
				57343
			},
			{
				45538,
				48357,
				-3056,
				18000
			},
			{
				46908,
				50856,
				-2992,
				8192
			},
			{
				9929,
				16324,
				-4568,
				62999
			},
			{
				11546,
				17599,
				-4584,
				46900
			},
			{
				115096,
				-178370,
				-880,
				0
			},
			{
				116199,
				-182694,
				-1488,
				0
			},
			{
				-45372,
				-114104,
				-240,
				16384
			},
			{
				-45278,
				-112766,
				-240,
				0
			},
			{
				-83156,
				150994,
				-3120,
				0
			},
			{
				-81031,
				150038,
				-3040,
				0
			},
			{
				-13727,
				122117,
				-2984,
				16384
			},
			{
				-14129,
				123869,
				-3112,
				40959
			},
			{
				16111,
				142850,
				-2696,
				16000
			},
			{
				17275,
				145000,
				-3032,
				25000
			},
			{
				111004,
				218928,
				-3536,
				16384
			},
			{
				108426,
				221876,
				-3592,
				49151
			},
			{
				81755,
				146487,
				-3528,
				32768
			},
			{
				82145,
				148609,
				-3464,
				0
			},
			{
				83037,
				149324,
				-3464,
				44000
			},
			{
				81083,
				56118,
				-1552,
				32768
			},
			{
				81987,
				53723,
				-1488,
				0
			},
			{
				117356,
				76708,
				-2688,
				49151
			},
			{
				115887,
				76382,
				-2712,
				0
			},
			{
				147200,
				25614,
				-2008,
				16384
			},
			{
				148557,
				26806,
				-2200,
				32768
			},
			{
				43966,
				-47709,
				-792,
				49999
			},
			{
				43165,
				-48461,
				-792,
				17000
			},
			{
				147421,
				-55435,
				-2728,
				49151
			},
			{
				148206,
				-55786,
				-2776,
				904
			},
			{
				85584,
				-142490,
				-1336,
				0
			},
			{
				86865,
				-142915,
				-1336,
				26000
			}
		};
		SpawnNPCs(EVENT_MANAGER_ID, EVENT_MANAGERS, _spawns);
	}
	
	/**
	 * Method unSpawnEventManagers.
	 */
	private void unSpawnEventManagers()
	{
		deSpawnNPCs(_spawns);
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private static boolean isActive()
	{
		return IsActive(EVENT_NAME);
	}
	
	/**
	 * Method startEvent.
	 */
	public void startEvent()
	{
		final Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (SetActive(EVENT_NAME, true))
		{
			spawnEventManagers();
			System.out.println("Event: Master of Enchanting started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.MasOfEnch.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'Master of Enchanting' already started.");
		}
		_active = true;
		show("admin/events.htm", player);
	}
	
	/**
	 * Method stopEvent.
	 */
	public void stopEvent()
	{
		final Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (SetActive(EVENT_NAME, false))
		{
			unSpawnEventManagers();
			System.out.println("Event: Master of Enchanting stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.MasOfEnch.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event 'Master of Enchanting' not started.");
		}
		_active = false;
		show("html/admin/events.htm", player);
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
		if (isActive())
		{
			_active = true;
			spawnEventManagers();
			_log.info("Loaded Event: Master of Enchanting [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: Master of Enchanting [state: deactivated]");
		}
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		unSpawnEventManagers();
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		unSpawnEventManagers();
	}
	
	/**
	 * Method onPlayerEnter.
	 * @param player Player
	 * @see lineage2.gameserver.listener.actor.player.OnPlayerEnterListener#onPlayerEnter(Player)
	 */
	@Override
	public void onPlayerEnter(Player player)
	{
		if (_active)
		{
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.MasOfEnch.AnnounceEventStarted", null);
		}
	}
}
