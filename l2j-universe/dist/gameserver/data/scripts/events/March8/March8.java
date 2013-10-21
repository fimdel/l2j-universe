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
package events.March8;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.components.CustomMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class March8 extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(March8.class);
	/**
	 * Field EVENT_NAME. (value is ""March8"")
	 */
	private static final String EVENT_NAME = "March8";
	/**
	 * Field RECIPE_PRICE. (value is 50000)
	 */
	private static final int RECIPE_PRICE = 50000;
	/**
	 * Field RECIPE_ID. (value is 20191)
	 */
	private static final int RECIPE_ID = 20191;
	/**
	 * Field EVENT_MANAGER_ID. (value is 4301)
	 */
	private static final int EVENT_MANAGER_ID = 4301;
	/**
	 * Field _spawns.
	 */
	private static final List<SimpleSpawner> _spawns = new ArrayList<>();
	/**
	 * Field DROP.
	 */
	private static final int[] DROP =
	{
		20192,
		20193,
		20194
	};
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
				-14823,
				123567,
				-3143,
				8192
			},
			{
				-83159,
				150914,
				-3155,
				49152
			},
			{
				18600,
				145971,
				-3095,
				40960
			},
			{
				82158,
				148609,
				-3493,
				60
			},
			{
				110992,
				218753,
				-3568,
				0
			},
			{
				116339,
				75424,
				-2738,
				0
			},
			{
				81140,
				55218,
				-1551,
				32768
			},
			{
				147148,
				27401,
				-2231,
				2300
			},
			{
				43532,
				-46807,
				-823,
				31471
			},
			{
				87765,
				-141947,
				-1367,
				6500
			},
			{
				147154,
				-55527,
				-2807,
				61300
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
			System.out.println("Event: March 8 started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.March8.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'March 8' already started.");
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
			System.out.println("Event: March 8 stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.March8.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event 'March 8' not started.");
		}
		_active = false;
		show("admin/events.htm", player);
	}
	
	/**
	 * Method buyrecipe.
	 */
	public void buyrecipe()
	{
		final Player player = getSelf();
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		if (!NpcInstance.canBypassCheck(player, player.getLastNpc()))
		{
			return;
		}
		final long need_adena = (long) (RECIPE_PRICE * Config.EVENT_MARCH8_PRICE_RATE);
		if (player.getAdena() < need_adena)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		player.reduceAdena(need_adena, true);
		Functions.addItem(player, RECIPE_ID, 1);
	}
	
	/**
	 * Method DialogAppend_4301.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_4301(Integer val)
	{
		if (val != 0)
		{
			return "";
		}
		final String price;
		String append = "";
		price = Util.formatAdena((long) (RECIPE_PRICE * Config.EVENT_MARCH8_PRICE_RATE));
		append += "<br><a action=\"bypass -h scripts_events.March8.March8:buyrecipe\">";
		append += new CustomMessage("scripts.events.March8.buyrecipe", getSelf()).addString(price);
		append += "</a><br>";
		return append;
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
			_log.info("Loaded Event: March 8 [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: March 8 [state: deactivated]");
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
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.March8.AnnounceEventStarted", null);
		}
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
		if (_active && SimpleCheckDrop(cha, killer) && Rnd.chance(Config.EVENT_MARCH8_DROP_CHANCE * killer.getPlayer().getRateItems() * ((NpcInstance) cha).getTemplate().rateHp))
		{
			((NpcInstance) cha).dropItem(killer.getPlayer(), DROP[Rnd.get(DROP.length)], 1);
		}
	}
}
