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
package events.SavingSnowman;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.cache.Msg;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerEnterListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.reward.RewardList;
import lineage2.gameserver.network.serverpackets.CharMoveToLocation;
import lineage2.gameserver.network.serverpackets.MagicSkillUse;
import lineage2.gameserver.network.serverpackets.RadarControl;
import lineage2.gameserver.network.serverpackets.SystemMessage;
import lineage2.gameserver.network.serverpackets.SystemMessage2;
import lineage2.gameserver.network.serverpackets.components.ChatType;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.ItemFunctions;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;
import lineage2.gameserver.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SavingSnowman extends Functions implements ScriptFile, OnDeathListener, OnPlayerEnterListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(SavingSnowman.class);
	/**
	 * Field _spawns.
	 */
	static List<SimpleSpawner> _spawns = new ArrayList<>();
	/**
	 * Field _snowmanShoutTask.
	 */
	private static ScheduledFuture<?> _snowmanShoutTask;
	/**
	 * Field _saveTask.
	 */
	private static ScheduledFuture<?> _saveTask;
	/**
	 * Field _sayTask.
	 */
	private static ScheduledFuture<?> _sayTask;
	/**
	 * Field _eatTask.
	 */
	private static ScheduledFuture<?> _eatTask;
	/**
	 * Field _snowmanState.
	 */
	public static SnowmanState _snowmanState;
	/**
	 * Field _snowman.
	 */
	static NpcInstance _snowman;
	/**
	 * Field _thomas.
	 */
	private static Creature _thomas;
	
	/**
	 * @author Mobius
	 */
	public static enum SnowmanState
	{
		/**
		 * Field CAPTURED.
		 */
		CAPTURED,
		/**
		 * Field KILLED.
		 */
		KILLED,
		/**
		 * Field SAVED.
		 */
		SAVED;
	}
	
	/**
	 * Field INITIAL_SAVE_DELAY.
	 */
	private static final int INITIAL_SAVE_DELAY = 10 * 60 * 1000;
	/**
	 * Field SAVE_INTERVAL.
	 */
	private static final int SAVE_INTERVAL = 60 * 60 * 1000;
	/**
	 * Field SNOWMAN_SHOUT_INTERVAL.
	 */
	private static final int SNOWMAN_SHOUT_INTERVAL = 1 * 60 * 1000;
	/**
	 * Field THOMAS_EAT_DELAY.
	 */
	private static final int THOMAS_EAT_DELAY = 10 * 60 * 1000;
	/**
	 * Field SATNA_SAY_INTERVAL.
	 */
	private static final int SATNA_SAY_INTERVAL = 5 * 60 * 1000;
	/**
	 * Field EVENT_MANAGER_ID. (value is 13184)
	 */
	private static final int EVENT_MANAGER_ID = 13184;
	/**
	 * Field CTREE_ID. (value is 13006)
	 */
	private static final int CTREE_ID = 13006;
	/**
	 * Field EVENT_REWARDER_ID. (value is 13186)
	 */
	private static final int EVENT_REWARDER_ID = 13186;
	/**
	 * Field SNOWMAN_ID. (value is 13160)
	 */
	private static final int SNOWMAN_ID = 13160;
	/**
	 * Field THOMAS_ID. (value is 13183)
	 */
	private static final int THOMAS_ID = 13183;
	/**
	 * Field SANTA_BUFF_REUSE.
	 */
	private static final int SANTA_BUFF_REUSE = 12 * 3600 * 1000;
	/**
	 * Field SANTA_LOTTERY_REUSE.
	 */
	private static final int SANTA_LOTTERY_REUSE = 3 * 3600 * 1000;
	/**
	 * Field WEAPONS.
	 */
	private static final int[][] WEAPONS =
	{
		{
			20109,
			20110,
			20111,
			20112,
			20113,
			20114,
			20115,
			20116,
			20117,
			20118,
			20119,
			20120,
			20121,
			20122
		},
		{
			20123,
			20124,
			20125,
			20126,
			20127,
			20128,
			20129,
			20130,
			20131,
			20132,
			20133,
			20134,
			20135,
			20136
		},
		{
			20137,
			20138,
			20139,
			20140,
			20141,
			20142,
			20143,
			20144,
			20145,
			20146,
			20147,
			20148,
			20149,
			20150
		},
		{
			20151,
			20152,
			20153,
			20154,
			20155,
			20156,
			20157,
			20158,
			20159,
			20160,
			20161,
			20162,
			20163,
			20164
		},
		{
			20165,
			20166,
			20167,
			20168,
			20169,
			20170,
			20171,
			20172,
			20173,
			20174,
			20175,
			20176,
			20177,
			20178
		}
	};
	/**
	 * Field _active.
	 */
	static boolean _active = false;
	
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
			_log.info("Loaded Event: SavingSnowman [state: activated]");
			_saveTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SaveTask(), INITIAL_SAVE_DELAY, SAVE_INTERVAL);
			_sayTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SayTask(), SATNA_SAY_INTERVAL, SATNA_SAY_INTERVAL);
			_snowmanState = SnowmanState.SAVED;
		}
		else
		{
			_log.info("Loaded Event: SavingSnowman [state: deactivated]");
		}
	}
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private static boolean isActive()
	{
		return IsActive("SavingSnowman");
	}
	
	/**
	 * Method startEvent.
	 */
	public void startEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (Boolean.FALSE)
		{
			player.sendMessage("Event is currently disabled");
			return;
		}
		if (SetActive("SavingSnowman", true))
		{
			spawnEventManagers();
			System.out.println("Event 'SavingSnowman' started.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.SavingSnowman.AnnounceEventStarted", null);
			if (_saveTask == null)
			{
				_saveTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SaveTask(), INITIAL_SAVE_DELAY, SAVE_INTERVAL);
			}
			if (_sayTask == null)
			{
				_sayTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new SayTask(), SATNA_SAY_INTERVAL, SATNA_SAY_INTERVAL);
			}
			_snowmanState = SnowmanState.SAVED;
		}
		else
		{
			player.sendMessage("Event 'SavingSnowman' already started.");
		}
		_active = true;
		show("admin/events.htm", player);
	}
	
	/**
	 * Method stopEvent.
	 */
	public void stopEvent()
	{
		Player player = getSelf();
		if (!player.getPlayerAccess().IsEventGm)
		{
			return;
		}
		if (SetActive("SavingSnowman", false))
		{
			unSpawnEventManagers();
			if (_snowman != null)
			{
				_snowman.deleteMe();
			}
			if (_thomas != null)
			{
				_thomas.deleteMe();
			}
			System.out.println("Event 'SavingSnowman' stopped.");
			Announcements.getInstance().announceByCustomMessage("scripts.events.SavingSnowman.AnnounceEventStoped", null);
			if (_saveTask != null)
			{
				_saveTask.cancel(false);
				_saveTask = null;
			}
			if (_sayTask != null)
			{
				_sayTask.cancel(false);
				_sayTask = null;
			}
			if (_eatTask != null)
			{
				_eatTask.cancel(false);
				_eatTask = null;
			}
			_snowmanState = SnowmanState.SAVED;
		}
		else
		{
			player.sendMessage("Event 'SavingSnowman' not started.");
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
		if (_saveTask != null)
		{
			_saveTask.cancel(false);
		}
		_saveTask = null;
		if (_sayTask != null)
		{
			_sayTask.cancel(false);
		}
		_sayTask = null;
		_snowmanState = SnowmanState.SAVED;
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
		if (_active && (killer != null))
		{
			Player pKiller = killer.getPlayer();
			if ((pKiller != null) && SimpleCheckDrop(cha, killer) && (Rnd.get(1000) < Config.EVENT_SAVING_SNOWMAN_REWARDER_CHANCE))
			{
				List<Player> players = new ArrayList<>();
				if (pKiller.isInParty())
				{
					players = pKiller.getParty().getPartyMembers();
				}
				else
				{
					players.add(pKiller);
				}
				spawnRewarder(players.get(Rnd.get(players.size())));
			}
		}
	}
	
	/**
	 * Method spawnRewarder.
	 * @param rewarded Player
	 */
	public static void spawnRewarder(Player rewarded)
	{
		for (NpcInstance npc : rewarded.getAroundNpc(1500, 300))
		{
			if (npc.getNpcId() == EVENT_REWARDER_ID)
			{
				return;
			}
		}
		Location spawnLoc = Location.findPointToStay(rewarded, 300, 400);
		for (int i = 0; (i < 20) && !GeoEngine.canSeeCoord(rewarded, spawnLoc.x, spawnLoc.y, spawnLoc.z, false); i++)
		{
			spawnLoc = Location.findPointToStay(rewarded, 300, 400);
		}
		NpcTemplate template = NpcHolder.getInstance().getTemplate(EVENT_REWARDER_ID);
		if (template == null)
		{
			System.out.println("WARNING! events.SavingSnowman.spawnRewarder template is null for npc: " + EVENT_REWARDER_ID);
			Thread.dumpStack();
			return;
		}
		NpcInstance rewarder = new NpcInstance(IdFactory.getInstance().getNextId(), template);
		rewarder.setLoc(spawnLoc);
		rewarder.setHeading((int) (Math.atan2(spawnLoc.y - rewarded.getY(), spawnLoc.x - rewarded.getX()) * Creature.HEADINGS_IN_PI) + 32768);
		rewarder.spawnMe();
		Functions.npcSayCustomMessage(rewarder, "scripts.events.SavingSnowman.RewarderPhrase1");
		Location targetLoc = Location.findFrontPosition(rewarded, rewarded, 40, 50);
		rewarder.setSpawnedLoc(targetLoc);
		rewarder.broadcastPacket(new CharMoveToLocation(rewarder.getObjectId(), rewarder.getLoc(), targetLoc));
		executeTask("events.SavingSnowman.SavingSnowman", "reward", new Object[]
		{
			rewarder,
			rewarded
		}, 5000);
	}
	
	/**
	 * Method reward.
	 * @param rewarder NpcInstance
	 * @param rewarded Player
	 */
	public static void reward(NpcInstance rewarder, Player rewarded)
	{
		if (!_active || (rewarder == null) || (rewarded == null))
		{
			return;
		}
		Functions.npcSayCustomMessage(rewarder, "scripts.events.SavingSnowman.RewarderPhrase2", rewarded.getName());
		Functions.addItem(rewarded, 14616, 1);
		executeTask("events.SavingSnowman.SavingSnowman", "removeRewarder", new Object[]
		{
			rewarder
		}, 5000);
	}
	
	/**
	 * Method removeRewarder.
	 * @param rewarder NpcInstance
	 */
	public static void removeRewarder(NpcInstance rewarder)
	{
		if (!_active || (rewarder == null))
		{
			return;
		}
		Functions.npcSayCustomMessage(rewarder, "scripts.events.SavingSnowman.RewarderPhrase3");
		Location loc = rewarder.getSpawnedLoc();
		double radian = PositionUtils.convertHeadingToRadian(rewarder.getHeading());
		int x = loc.x - (int) (Math.sin(radian) * 300);
		int y = loc.y + (int) (Math.cos(radian) * 300);
		int z = loc.z;
		rewarder.broadcastPacket(new CharMoveToLocation(rewarder.getObjectId(), loc, new Location(x, y, z)));
		executeTask("events.SavingSnowman.SavingSnowman", "unspawnRewarder", new Object[]
		{
			rewarder
		}, 2000);
	}
	
	/**
	 * Method unspawnRewarder.
	 * @param rewarder NpcInstance
	 */
	public static void unspawnRewarder(NpcInstance rewarder)
	{
		if (!_active || (rewarder == null))
		{
			return;
		}
		rewarder.deleteMe();
	}
	
	/**
	 * Method buff.
	 */
	public void buff()
	{
		Player player = getSelf();
		if (!_active || player.isActionsDisabled() || player.isSitting() || (player.getLastNpc() == null) || (player.getLastNpc().getDistance(player) > 300))
		{
			return;
		}
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		String var = player.getVar("santaEventTime");
		if ((var != null) && (Long.parseLong(var) > System.currentTimeMillis()))
		{
			show("default/13184-4.htm", player);
			return;
		}
		if (_snowmanState != SnowmanState.SAVED)
		{
			show("default/13184-3.htm", player);
			return;
		}
		player.broadcastPacket(new MagicSkillUse(player, player, 23017, 1, 0, 0));
		player.altOnMagicUseTimer(player, SkillTable.getInstance().getInfo(23017, 1));
		player.setVar("santaEventTime", String.valueOf(System.currentTimeMillis() + SANTA_BUFF_REUSE), -1);
		for (Summon summon : player.getSummonList())
		{
			summon.broadcastPacket(new MagicSkillUse(summon, summon, 23017, 1, 0, 0));
			summon.altOnMagicUseTimer(summon, SkillTable.getInstance().getInfo(23017, 1));
		}
	}
	
	/**
	 * Method locateSnowman.
	 */
	public void locateSnowman()
	{
		Player player = getSelf();
		if (!_active || player.isActionsDisabled() || player.isSitting() || (player.getLastNpc() == null) || (player.getLastNpc().getDistance(player) > 300))
		{
			return;
		}
		if (_snowman != null)
		{
			player.sendPacket(new RadarControl(2, 2, _snowman.getLoc()), new RadarControl(0, 1, _snowman.getLoc()));
			player.sendPacket(new SystemMessage(SystemMessage.S2_S1).addZoneName(_snowman.getLoc()).addString("�?щите Снеговика в "));
		}
		else
		{
			player.sendPacket(Msg.YOUR_TARGET_CANNOT_BE_FOUND);
		}
	}
	
	/**
	 * Method coupon.
	 * @param var String[]
	 */
	public void coupon(String[] var)
	{
		Player player = getSelf();
		if (!_active || player.isActionsDisabled() || player.isSitting() || (player.getLastNpc() == null) || (player.getLastNpc().getDistance(player) > 300))
		{
			return;
		}
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		if (getItemCount(player, 20107) < 1)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_REQUIRED_ITEMS);
			return;
		}
		int num = Integer.parseInt(var[0]);
		if ((num < 0) || (num > 13))
		{
			return;
		}
		int expertise = Math.min(player.expertiseIndex, 5);
		expertise = Math.max(expertise, 1);
		expertise--;
		removeItem(player, 20107, 1);
		int item_id = WEAPONS[expertise][num];
		int enchant = Rnd.get(4, 16);
		ItemInstance item = ItemFunctions.createItem(item_id);
		item.setEnchantLevel(enchant);
		player.getInventory().addItem(item);
		player.sendPacket(SystemMessage2.obtainItems(item_id, 1, enchant));
	}
	
	/**
	 * Method lotery.
	 */
	public void lotery()
	{
		Player player = getSelf();
		if (!_active || player.isActionsDisabled() || player.isSitting() || (player.getLastNpc() == null) || (player.getLastNpc().getDistance(player) > 300))
		{
			return;
		}
		if (!player.isQuestContinuationPossible(true))
		{
			return;
		}
		if (getItemCount(player, 57) < Config.EVENT_SAVING_SNOWMAN_LOTERY_PRICE)
		{
			player.sendPacket(Msg.YOU_DO_NOT_HAVE_ENOUGH_ADENA);
			return;
		}
		String var = player.getVar("santaLotteryTime");
		if ((var != null) && (Long.parseLong(var) > System.currentTimeMillis()))
		{
			show("default/13184-5.htm", player);
			return;
		}
		removeItem(player, 57, Config.EVENT_SAVING_SNOWMAN_LOTERY_PRICE);
		player.setVar("santaLotteryTime", String.valueOf(System.currentTimeMillis() + SANTA_LOTTERY_REUSE), -1);
		int chance = Rnd.get(RewardList.MAX_CHANCE);
		if (chance < 300000)
		{
			addItem(player, 5561, 1);
		}
		else if (chance < 480000)
		{
			addItem(player, 14612, 1);
		}
		else if (chance < 630000)
		{
			addItem(player, 20107, 1);
		}
		else if (chance < 680000)
		{
			addItem(player, 14616, 1);
		}
		else if ((chance < 730000) && (getItemCount(player, 14611) == 0))
		{
			addItem(player, 14611, 1);
		}
		else if ((chance < 780000) && (getItemCount(player, 7836) == 0))
		{
			addItem(player, 7836, 1);
		}
		else if ((chance < 830000) && (getItemCount(player, 8936) == 0))
		{
			addItem(player, 8936, 1);
		}
		else if ((chance < 880000) && (getItemCount(player, 10606) == 0))
		{
			addItem(player, 10606, 1);
		}
		else if ((chance < 930000) && (getItemCount(player, 20094) == 0))
		{
			addItem(player, 20094, 1);
		}
		else if (chance < 960000)
		{
			addItem(player, 20575, 1);
		}
		else if (chance < 985000)
		{
			addItem(player, Rnd.get(9177, 9204), 1);
		}
		else if (chance < 997000)
		{
			addItem(player, Rnd.get(9156, 9157), 1);
		}
	}
	
	/**
	 * Method DialogAppend_13184.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_13184(Integer val)
	{
		if (val != 0)
		{
			return "";
		}
		return " (" + Util.formatAdena(Config.EVENT_SAVING_SNOWMAN_LOTERY_PRICE) + " adena)";
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
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.SavingSnowman.AnnounceEventStarted", null);
		}
	}
	
	/**
	 * Method getRandomSpawnPoint.
	 * @return Location
	 */
	private static Location getRandomSpawnPoint()
	{
		return new Location(0, 0, 0);
	}
	
	/**
	 * Method captureSnowman.
	 */
	public void captureSnowman()
	{
		Location spawnPoint = getRandomSpawnPoint();
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.SavingSnowman.AnnounceSnowmanCaptured", null, ChatType.CRITICAL_ANNOUNCE);
			player.sendPacket(new SystemMessage(SystemMessage.S2_S1).addZoneName(spawnPoint).addString("�?щите Снеговика в "));
			player.sendPacket(new RadarControl(2, 2, spawnPoint), new RadarControl(0, 1, spawnPoint));
		}
		NpcTemplate template = NpcHolder.getInstance().getTemplate(SNOWMAN_ID);
		if (template == null)
		{
			System.out.println("WARNING! events.SavingSnowman.captureSnowman template is null for npc: " + SNOWMAN_ID);
			Thread.dumpStack();
			return;
		}
		SimpleSpawner sp = new SimpleSpawner(template);
		sp.setLoc(spawnPoint);
		sp.setAmount(1);
		sp.setRespawnDelay(0);
		_snowman = sp.doSpawn(true);
		if (_snowman == null)
		{
			return;
		}
		template = NpcHolder.getInstance().getTemplate(THOMAS_ID);
		if (template == null)
		{
			System.out.println("WARNING! events.SavingSnowman.captureSnowman template is null for npc: " + THOMAS_ID);
			Thread.dumpStack();
			return;
		}
		Location pos = Location.findPointToStay(_snowman, 100, 120);
		sp = new SimpleSpawner(template);
		sp.setLoc(pos);
		sp.setAmount(1);
		sp.setRespawnDelay(0);
		_thomas = sp.doSpawn(true);
		if (_thomas == null)
		{
			return;
		}
		_snowmanState = SnowmanState.CAPTURED;
		if (_snowmanShoutTask != null)
		{
			_snowmanShoutTask.cancel(false);
			_snowmanShoutTask = null;
		}
		_snowmanShoutTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new ShoutTask(), 1, SNOWMAN_SHOUT_INTERVAL);
		if (_eatTask != null)
		{
			_eatTask.cancel(false);
			_eatTask = null;
		}
		_eatTask = executeTask("events.SavingSnowman.SavingSnowman", "eatSnowman", new Object[0], THOMAS_EAT_DELAY);
	}
	
	/**
	 * Method eatSnowman.
	 */
	public static void eatSnowman()
	{
		if ((_snowman == null) || (_thomas == null))
		{
			return;
		}
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.SavingSnowman.AnnounceSnowmanKilled", null, ChatType.CRITICAL_ANNOUNCE);
		}
		_snowmanState = SnowmanState.KILLED;
		if (_snowmanShoutTask != null)
		{
			_snowmanShoutTask.cancel(false);
			_snowmanShoutTask = null;
		}
		_snowman.deleteMe();
		_thomas.deleteMe();
	}
	
	/**
	 * Method freeSnowman.
	 * @param topDamager Creature
	 */
	public static void freeSnowman(Creature topDamager)
	{
		if ((_snowman == null) || (topDamager == null) || !topDamager.isPlayable())
		{
			return;
		}
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			Announcements.getInstance().announceToPlayerByCustomMessage(player, "scripts.events.SavingSnowman.AnnounceSnowmanSaved", null, ChatType.CRITICAL_ANNOUNCE);
		}
		_snowmanState = SnowmanState.SAVED;
		if (_snowmanShoutTask != null)
		{
			_snowmanShoutTask.cancel(false);
			_snowmanShoutTask = null;
		}
		if (_eatTask != null)
		{
			_eatTask.cancel(false);
			_eatTask = null;
		}
		Player player = topDamager.getPlayer();
		Functions.npcSayCustomMessage(_snowman, "scripts.events.SavingSnowman.SnowmanSayTnx", player.getName());
		addItem(player, 20034, 3);
		addItem(player, 20338, 1);
		addItem(player, 20344, 1);
		ThreadPoolManager.getInstance().execute(new RunnableImpl()
		{
			@Override
			public void runImpl()
			{
				_snowman.deleteMe();
			}
		});
	}
	
	/**
	 */
	public class SayTask extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!_active)
			{
				return;
			}
			for (SimpleSpawner s : _spawns)
			{
				if (s.getCurrentNpcId() == EVENT_MANAGER_ID)
				{
					Functions.npcSayCustomMessage(s.getLastSpawn(), "scripts.events.SavingSnowman.SantaSay");
				}
			}
		}
	}
	
	/**
	 */
	public class ShoutTask extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!_active || (_snowman == null) || (_snowmanState != SnowmanState.CAPTURED))
			{
				return;
			}
			Functions.npcShoutCustomMessage(_snowman, "scripts.events.SavingSnowman.SnowmanShout");
		}
	}
	
	/**
	 */
	public class SaveTask extends RunnableImpl
	{
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (!_active || (_snowmanState == SnowmanState.CAPTURED))
			{
				return;
			}
			captureSnowman();
		}
	}
}
