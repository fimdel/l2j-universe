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
package events.FreyaEvent;

import java.util.ArrayList;
import java.util.List;

import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FreyaEvent extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener
{
	/**
	 * Field EVENT_MANAGER_ID. (value is 13296)
	 */
	private static final int EVENT_MANAGER_ID = 13296;
	/**
	 * Field ADENA. (value is 57)
	 */
	private static final int ADENA = 57;
	/**
	 * Field GIFT_RECEIVE_DELAY. (value is 20)
	 */
	private static final int GIFT_RECEIVE_DELAY = 20;
	/**
	 * Field GIFT_ID. (value is 15440)
	 */
	private static final int GIFT_ID = 15440;
	/**
	 * Field GIFT_PRICE. (value is 1)
	 */
	private static final int GIFT_PRICE = 1;
	/**
	 * Field DROP_CHANCE. (value is 55)
	 */
	private static final int DROP_CHANCE = 55;
	/**
	 * Field DROP_LIST.
	 */
	private static final int[] DROP_LIST =
	{
		17130,
		17131,
		17132,
		17133,
		17134,
		17135,
		17136,
		17137
	};
	/**
	 * Field EVENT_MANAGERS.
	 */
	private static final int[][] EVENT_MANAGERS =
	{
		{
			16111,
			142850,
			-2707,
			16000
		},
		{
			17275,
			145000,
			-3037,
			25000
		},
		{
			83037,
			149324,
			-3470,
			44000
		},
		{
			82145,
			148609,
			-3468,
			0
		},
		{
			81755,
			146487,
			-3534,
			32768
		},
		{
			-81031,
			150038,
			-3045,
			0
		},
		{
			-83156,
			150994,
			-3130,
			0
		},
		{
			-13727,
			122117,
			-2990,
			16384
		},
		{
			-14129,
			123869,
			-3118,
			40959
		},
		{
			-84411,
			244813,
			-3730,
			57343
		},
		{
			-84023,
			243051,
			-3730,
			4096
		},
		{
			46908,
			50856,
			-2997,
			8192
		},
		{
			45538,
			48357,
			-3061,
			18000
		},
		{
			9929,
			16324,
			-4576,
			62999
		},
		{
			11546,
			17599,
			-4586,
			46900
		},
		{
			81987,
			53723,
			-1497,
			0
		},
		{
			81083,
			56118,
			-1562,
			32768
		},
		{
			147200,
			25614,
			-2014,
			16384
		},
		{
			148557,
			26806,
			-2206,
			32768
		},
		{
			117356,
			76708,
			-2695,
			49151
		},
		{
			115887,
			76382,
			-2714,
			0
		},
		{
			-117239,
			46842,
			367,
			49151
		},
		{
			-119494,
			44882,
			367,
			24576
		},
		{
			111004,
			218928,
			-3544,
			16384
		},
		{
			108426,
			221876,
			-3600,
			49151
		},
		{
			-45278,
			-112766,
			-241,
			0
		},
		{
			-45372,
			-114104,
			-241,
			16384
		},
		{
			115096,
			-178370,
			-891,
			0
		},
		{
			116199,
			-182694,
			-1506,
			0
		},
		{
			86865,
			-142915,
			-1341,
			26000
		},
		{
			85584,
			-142490,
			-1343,
			0
		},
		{
			147421,
			-55435,
			-2736,
			49151
		},
		{
			148206,
			-55786,
			-2782,
			61439
		},
		{
			43165,
			-48461,
			-797,
			17000
		},
		{
			43966,
			-47709,
			-798,
			49999
		}
	};
	/**
	 * Field _name. (value is ""Freya Celebration"")
	 */
	private static final String _name = "Freya Celebration";
	/**
	 * Field _msgStarted. (value is ""scripts.events.FreyaEvent.AnnounceEventStarted"")
	 */
	private static final String _msgStarted = "scripts.events.FreyaEvent.AnnounceEventStarted";
	/**
	 * Field _msgEnded. (value is ""scripts.events.FreyaEvent.AnnounceEventStoped"")
	 */
	private static final String _msgEnded = "scripts.events.FreyaEvent.AnnounceEventStoped";
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(FreyaEvent.class);
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
			_log.info("Loaded Event: " + _name + " [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: " + _name + " [state: deactivated]");
		}
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	protected static boolean isActive()
	{
		return IsActive(_name);
	}
	
	/**
	 * Method spawnEventManagers.
	 */
	protected void spawnEventManagers()
	{
		SpawnNPCs(EVENT_MANAGER_ID, EVENT_MANAGERS, _spawns);
	}
	
	/**
	 * Method unSpawnEventManagers.
	 */
	protected void unSpawnEventManagers()
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
			final int itemId = DROP_LIST[Rnd.get(DROP_LIST.length)];
			if (Rnd.chance(DROP_CHANCE))
			{
				((NpcInstance) cha).dropItem(killer.getPlayer(), itemId, 1);
			}
		}
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
		if (SetActive(_name, true))
		{
			spawnEventManagers();
			System.out.println("Event '" + _name + "' started.");
			Announcements.getInstance().announceByCustomMessage(_msgStarted, null);
		}
		else
		{
			player.sendMessage("Event '" + _name + "' already started.");
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
		if (SetActive(_name, false))
		{
			unSpawnEventManagers();
			System.out.println("Event '" + _name + "' stopped.");
			Announcements.getInstance().announceByCustomMessage(_msgEnded, null);
		}
		else
		{
			player.sendMessage("Event '" + _name + "' not started.");
		}
		_active = false;
		show("admin/events.htm", player);
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
			Announcements.getInstance().announceToPlayerByCustomMessage(player, _msgStarted, null);
		}
	}
	
	/**
	 * Method receiveGift.
	 */
	public void receiveGift()
	{
		final Player player = getSelf();
		long _remaining_time;
		final long _reuse_time = GIFT_RECEIVE_DELAY * 60 * 60 * 1000;
		final long _curr_time = System.currentTimeMillis();
		final String _last_use_time = player.getVar("FreyaCelebration");
		if (_last_use_time != null)
		{
			_remaining_time = _curr_time - Long.parseLong(_last_use_time);
		}
		else
		{
			_remaining_time = _reuse_time;
		}
		if (_remaining_time >= _reuse_time)
		{
			if (getItemCount(player, ADENA) >= GIFT_PRICE)
			{
				removeItem(player, ADENA, GIFT_PRICE);
				addItem(player, GIFT_ID, 1);
				player.setVar("FreyaCelebration", String.valueOf(_curr_time), -1);
			}
			else
			{
				player.sendPacket(new SystemMessage(SystemMessage._2_UNITS_OF_THE_ITEM_S1_IS_REQUIRED).addNumber(GIFT_PRICE));
			}
		}
		else
		{
			final int hours = (int) (_reuse_time - _remaining_time) / 3600000;
			final int minutes = ((int) (_reuse_time - _remaining_time) % 3600000) / 60000;
			if (hours > 0)
			{
				player.sendPacket(new SystemMessage(SystemMessage.THERE_ARE_S1_HOURSS_AND_S2_MINUTES_REMAINING_UNTIL_THE_TIME_WHEN_THE_ITEM_CAN_BE_PURCHASED).addNumber(hours).addNumber(minutes));
			}
			else if (minutes > 0)
			{
				player.sendPacket(new SystemMessage(SystemMessage.THERE_ARE_S1_MINUTES_REMAINING_UNTIL_THE_TIME_WHEN_THE_ITEM_CAN_BE_PURCHASED).addNumber(minutes));
			}
			else if (getItemCount(player, ADENA) >= GIFT_PRICE)
			{
				removeItem(player, ADENA, GIFT_PRICE);
				addItem(player, GIFT_ID, 1);
				player.setVar("FreyaCelebration", String.valueOf(_curr_time), -1);
			}
			else
			{
				player.sendPacket(new SystemMessage(SystemMessage._2_UNITS_OF_THE_ITEM_S1_IS_REQUIRED).addNumber(GIFT_PRICE));
			}
		}
	}
}
