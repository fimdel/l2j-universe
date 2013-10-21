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
package events.Christmas;

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
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Christmas extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener
{
	/**
	 * Field EVENT_MANAGER_ID.
	 */
	private static final int EVENT_MANAGER_ID = 31863;
	/**
	 * Field CTREE_ID.
	 */
	private static final int CTREE_ID = 13006;
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(Christmas.class);
	/**
	 * Field _dropdata.
	 */
	private static final int[][] _dropdata =
	{
		{
			5556,
			20
		},
		{
			5557,
			20
		},
		{
			5558,
			50
		},
		{
			5559,
			5
		},
	};
	/**
	 * Field _spawns.
	 */
	private static final List<SimpleSpawner> _spawns = new ArrayList<>();
	/**
	 * Field _active.
	 */
	private static boolean _active = false;
	
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
			_log.info("Loaded Event: Christmas [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: Christmas [state: deactivated]");
		}
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private static boolean isActive()
	{
		return IsActive("Christmas");
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
		if (SetActive("Christmas", true))
		{
			spawnEventManagers();
			System.out.println("Event 'Christmas' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.Christmas.AnnounceEventStarted", null);
		}
		else
		{
			player.sendMessage("Event 'Christmas' already started.");
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
		if (SetActive("Christmas", false))
		{
			unSpawnEventManagers();
			System.out.println("Event 'Christmas' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.Christmas.AnnounceEventStoped", null);
		}
		else
		{
			player.sendMessage("Event 'Christmas' not started.");
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
				16384
			},
			{
				-12347,
				122549,
				-3104,
				16384
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
				8192
			}
		};
		final int[][] CTREES =
		{
			{
				81961,
				148921,
				-3467,
				0
			},
			{
				146445,
				28360,
				-2269,
				0
			},
			{
				19319,
				144959,
				-3103,
				0
			},
			{
				-82845,
				149890,
				-3129,
				0
			},
			{
				-12387,
				122549,
				-3104,
				0
			},
			{
				110602,
				220165,
				-3655,
				0
			},
			{
				116659,
				75463,
				-2721,
				0
			},
			{
				85553,
				16014,
				-3668,
				0
			},
			{
				81999,
				53743,
				-1496,
				0
			},
			{
				148199,
				-55484,
				-2734,
				0
			},
			{
				44185,
				-48542,
				-797,
				0
			},
			{
				86859,
				-143229,
				-1293,
				0
			}
		};
		SpawnNPCs(EVENT_MANAGER_ID, EVENT_MANAGERS, _spawns);
		SpawnNPCs(CTREE_ID, CTREES, _spawns);
	}
	
	/**
	 * Method unSpawnEventManagers.
	 */
	private void unSpawnEventManagers()
	{
		deSpawnNPCs(_spawns);
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
	 * Method onDeath.
	 * @param cha Creature
	 * @param killer Creature
	 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
	 */
	@Override
	public void onDeath(Creature cha, Creature killer)
	{
		if (_active && SimpleCheckDrop(cha, killer))
		{
			int dropCounter = 0;
			for (int[] drop : _dropdata)
			{
				if (Rnd.chance(drop[1] * killer.getPlayer().getRateItems() * Config.RATE_DROP_ITEMS * 0.1))
				{
					dropCounter++;
					((NpcInstance) cha).dropItem(killer.getPlayer(), drop[0], 1);
					if (dropCounter > 2)
					{
						break;
					}
				}
			}
		}
	}
	
	/**
	 * Method exchange.
	 * @param var String[]
	 */
	public void exchange(String[] var)
	{
		final Player player = getSelf();
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		if (player.isActionsDisabled() || player.isSitting() || (player.getLastNpc() == null) || (player.getLastNpc().getDistance(player) > 300))
		{
			return;
		}
		if (var[0].equalsIgnoreCase("0"))
		{
			if ((getItemCount(player, 5556) >= 4) && (getItemCount(player, 5557) >= 4) && (getItemCount(player, 5558) >= 10) && (getItemCount(player, 5559) >= 1))
			{
				removeItem(player, 5556, 4);
				removeItem(player, 5557, 4);
				removeItem(player, 5558, 10);
				removeItem(player, 5559, 1);
				addItem(player, 5560, 1);
				return;
			}
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
		}
		if (var[0].equalsIgnoreCase("1"))
		{
			if (getItemCount(player, 5560) >= 10)
			{
				removeItem(player, 5560, 10);
				addItem(player, 5561, 1);
				return;
			}
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
		}
		if (var[0].equalsIgnoreCase("2"))
		{
			if (getItemCount(player, 5560) >= 10)
			{
				removeItem(player, 5560, 10);
				addItem(player, 7836, 1);
				return;
			}
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
		}
		if (var[0].equalsIgnoreCase("3"))
		{
			if (getItemCount(player, 5560) >= 10)
			{
				removeItem(player, 5560, 10);
				addItem(player, 8936, 1);
				return;
			}
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
		}
		if (var[0].equalsIgnoreCase("4"))
		{
			if (getItemCount(player, 5560) >= 20)
			{
				removeItem(player, 5560, 20);
				addItem(player, 10606, 1);
				return;
			}
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
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
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.Christmas.AnnounceEventStarted", null);
		}
	}
}
