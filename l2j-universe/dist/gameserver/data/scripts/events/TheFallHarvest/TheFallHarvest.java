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
package events.TheFallHarvest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.data.xml.holder.MultiSellHolder;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TheFallHarvest extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(TheFallHarvest.class);
	/**
	 * Field EVENT_MANAGER_ID.
	 */
	private static final int EVENT_MANAGER_ID = 31255;
	/**
	 * Field _spawns.
	 */
	private static final List<SimpleSpawner> _spawns = new ArrayList<>();
	/**
	 * Field _active.
	 */
	private static boolean _active = false;
	/**
	 * Field MultiSellLoaded.
	 */
	private static boolean MultiSellLoaded = false;
	/**
	 * Field multiSellFile.
	 */
	private static final File multiSellFile = new File(Config.DATAPACK_ROOT, "data/xml/other/event/TheFallHarvest/31255.xml");
	
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
			loadMultiSell();
			spawnEventManagers();
			_log.info("Loaded Event: The Fall Harvest [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: The Fall Harvest [state: deactivated]");
		}
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private static boolean isActive()
	{
		return IsActive("TheFallHarvest");
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
		if (SetActive("TheFallHarvest", true))
		{
			loadMultiSell();
			spawnEventManagers();
			System.out.println("Event 'The Fall Harvest' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.TheFallHarvest.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'The Fall Harvest' already started.");
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
		if (SetActive("TheFallHarvest", false))
		{
			unSpawnEventManagers();
			System.out.println("Event 'The Fall Harvest' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.TheFallHarvest.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event 'The Fall Harvest' not started.");
		}
		_active = false;
		show("admin/events.htm", player);
	}
	
	/**
	 * Method spawnEventManagers.
	 */
	private void spawnEventManagers()
	{
		final int[][] EVENT_MANAGERS =
		{
			{
				81921,
				148921,
				-3467,
				16384
			},
			{
				146405,
				28360,
				-2269,
				49648
			},
			{
				19319,
				144919,
				-3103,
				31135
			},
			{
				-82805,
				149890,
				-3129,
				33202
			},
			{
				-12347,
				122549,
				-3104,
				32603
			},
			{
				110642,
				220165,
				-3655,
				61898
			},
			{
				116619,
				75463,
				-2721,
				20881
			},
			{
				85513,
				16014,
				-3668,
				23681
			},
			{
				81999,
				53793,
				-1496,
				61621
			},
			{
				148159,
				-55484,
				-2734,
				44315
			},
			{
				44185,
				-48502,
				-797,
				27479
			},
			{
				86899,
				-143229,
				-1293,
				22021
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
	 * Method loadMultiSell.
	 */
	private static void loadMultiSell()
	{
		if (MultiSellLoaded)
		{
			return;
		}
		MultiSellHolder.getInstance().parseFile(multiSellFile);
		MultiSellLoaded = true;
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		unSpawnEventManagers();
		if (MultiSellLoaded)
		{
			MultiSellHolder.getInstance().remove(multiSellFile);
			MultiSellLoaded = false;
		}
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
		// empty method
	}
	
	/**
	 * Method onDeath.
	 * @param cha Creature
	 * @param killer Creature
	 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
	 */
	@Override
	public void onDeath(Creature cha, Creature killer)
	{
		if (_active && SimpleCheckDrop(cha, killer) && Rnd.chance(Config.EVENT_TFH_POLLEN_CHANCE * killer.getPlayer().getRateItems() * ((NpcInstance) cha).getTemplate().rateHp))
		{
			((NpcInstance) cha).dropItem(killer.getPlayer(), 6391, 1);
		}
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
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.TheFallHarvest.AnnounceEventStarted", null);
		}
	}
}
