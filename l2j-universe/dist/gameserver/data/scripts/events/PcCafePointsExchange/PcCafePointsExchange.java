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
package events.PcCafePointsExchange;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class PcCafePointsExchange extends Functions implements ScriptFile
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(PcCafePointsExchange.class);
	/**
	 * Field EVENT_NAME. (value is ""PcCafePointsExchange"")
	 */
	private static final String EVENT_NAME = "PcCafePointsExchange";
	/**
	 * Field EVENT_MANAGER_ID. (value is 32130)
	 */
	private static final int EVENT_MANAGER_ID = 32130;
	/**
	 * Field _spawns.
	 */
	private static final List<SimpleSpawner> _spawns = new ArrayList<>();
	
	/**
	 * Method spawnEventManagers.
	 */
	private void spawnEventManagers()
	{
		final int[][] EVENT_MANAGERS =
		{
			{
				15880,
				143704,
				-2888,
				0
			},
			{
				83656,
				148440,
				-3430,
				32768
			},
			{
				147272,
				27416,
				-2228,
				16384
			},
			{
				42808,
				-47896,
				-822,
				49152
			},
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
			System.out.println("Event: 'PcCafePointsExchange' started.");
		}
		else
		{
			player.sendMessage("Event 'PcCafePointsExchange' already started.");
		}
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
			System.out.println("Event: 'PcCafePointsExchange' stopped.");
		}
		else
		{
			player.sendMessage("Event: 'PcCafePointsExchange' not started.");
		}
		show("admin/events.htm", player);
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (isActive())
		{
			spawnEventManagers();
			_log.info("Loaded Event: PcCafePointsExchange [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: PcCafePointsExchange [state: deactivated]");
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
}
