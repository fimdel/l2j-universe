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
package bosses;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.quest.QuestState;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;
import npc.model.SepulcherNpcInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bosses.FourSepulchersSpawn.GateKeeper;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class FourSepulchersManager extends Functions implements ScriptFile, OnDeathListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(FourSepulchersManager.class);
	/**
	 * Field QUEST_ID. (value is ""_620_FourGoblets"")
	 */
	public static final String QUEST_ID = "_620_FourGoblets";
	/**
	 * Field _zone.
	 */
	private static final Zone[] _zone = new Zone[4];
	/**
	 * Field ENTRANCE_PASS. (value is 7075)
	 */
	private static final int ENTRANCE_PASS = 7075;
	/**
	 * Field USED_PASS. (value is 7261)
	 */
	private static final int USED_PASS = 7261;
	/**
	 * Field CHAPEL_KEY. (value is 7260)
	 */
	private static final int CHAPEL_KEY = 7260;
	/**
	 * Field ANTIQUE_BROOCH. (value is 7262)
	 */
	private static final int ANTIQUE_BROOCH = 7262;
	/**
	 * Field _inEntryTime.
	 */
	static boolean _inEntryTime = false;
	/**
	 * Field _inAttackTime.
	 */
	static boolean _inAttackTime = false;
	/**
	 * Field _changeCoolDownTimeTask.
	 */
	static ScheduledFuture<?> _changeCoolDownTimeTask = null;
	/**
	 * Field _changeEntryTimeTask.
	 */
	static ScheduledFuture<?> _changeEntryTimeTask = null;
	/**
	 * Field _changeWarmUpTimeTask.
	 */
	static ScheduledFuture<?> _changeWarmUpTimeTask = null;
	/**
	 * Field _changeAttackTimeTask.
	 */
	static ScheduledFuture<?> _changeAttackTimeTask = null;
	/**
	 * Field _coolDownTimeEnd.
	 */
	private static long _coolDownTimeEnd = 0;
	/**
	 * Field _entryTimeEnd.
	 */
	static long _entryTimeEnd = 0;
	/**
	 * Field _warmUpTimeEnd.
	 */
	static long _warmUpTimeEnd = 0;
	/**
	 * Field _attackTimeEnd.
	 */
	static long _attackTimeEnd = 0;
	/**
	 * Field _newCycleMin.
	 */
	static int _newCycleMin = 55;
	/**
	 * Field _firstTimeRun.
	 */
	static boolean _firstTimeRun;
	
	/**
	 * Method init.
	 */
	public void init()
	{
		CharListenerList.addGlobal(this);
		_zone[0] = ReflectionUtils.getZone("[FourSepulchers1]");
		_zone[1] = ReflectionUtils.getZone("[FourSepulchers2]");
		_zone[2] = ReflectionUtils.getZone("[FourSepulchers3]");
		_zone[3] = ReflectionUtils.getZone("[FourSepulchers4]");
		if (_changeCoolDownTimeTask != null)
		{
			_changeCoolDownTimeTask.cancel(false);
		}
		if (_changeEntryTimeTask != null)
		{
			_changeEntryTimeTask.cancel(false);
		}
		if (_changeWarmUpTimeTask != null)
		{
			_changeWarmUpTimeTask.cancel(false);
		}
		if (_changeAttackTimeTask != null)
		{
			_changeAttackTimeTask.cancel(false);
		}
		_changeCoolDownTimeTask = null;
		_changeEntryTimeTask = null;
		_changeWarmUpTimeTask = null;
		_changeAttackTimeTask = null;
		_inEntryTime = false;
		_inAttackTime = false;
		_firstTimeRun = true;
		FourSepulchersSpawn.init();
		timeSelector();
	}
	
	/**
	 * Method timeSelector.
	 */
	private static void timeSelector()
	{
		timeCalculator();
		final long currentTime = System.currentTimeMillis();
		if ((currentTime >= _coolDownTimeEnd) && (currentTime < _entryTimeEnd))
		{
			cleanUp();
			_changeEntryTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeEntryTime(), 0);
			_log.info("FourSepulchersManager: Beginning in Entry time");
		}
		else if ((currentTime >= _entryTimeEnd) && (currentTime < _warmUpTimeEnd))
		{
			cleanUp();
			_changeWarmUpTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeWarmUpTime(), 0);
			_log.info("FourSepulchersManager: Beginning in WarmUp time");
		}
		else if ((currentTime >= _warmUpTimeEnd) && (currentTime < _attackTimeEnd))
		{
			cleanUp();
			_changeAttackTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeAttackTime(), 0);
			_log.info("FourSepulchersManager: Beginning in Attack time");
		}
		else
		{
			_changeCoolDownTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeCoolDownTime(), 0);
			_log.info("FourSepulchersManager: Beginning in Cooldown time");
		}
	}
	
	/**
	 * Method timeCalculator.
	 */
	private static void timeCalculator()
	{
		final Calendar tmp = Calendar.getInstance();
		if (tmp.get(Calendar.MINUTE) < _newCycleMin)
		{
			tmp.set(Calendar.HOUR, Calendar.getInstance().get(Calendar.HOUR) - 1);
		}
		tmp.set(Calendar.MINUTE, _newCycleMin);
		_coolDownTimeEnd = tmp.getTimeInMillis();
		_entryTimeEnd = _coolDownTimeEnd + (3 * 60000);
		_warmUpTimeEnd = _entryTimeEnd + (2 * 60000);
		_attackTimeEnd = _warmUpTimeEnd + (50 * 60000);
	}
	
	/**
	 * Method cleanUp.
	 */
	static void cleanUp()
	{
		for (Player player : getPlayersInside())
		{
			player.teleToClosestTown();
		}
		FourSepulchersSpawn.deleteAllMobs();
		FourSepulchersSpawn.closeAllDoors();
		FourSepulchersSpawn._hallInUse.clear();
		FourSepulchersSpawn._hallInUse.put(31921, false);
		FourSepulchersSpawn._hallInUse.put(31922, false);
		FourSepulchersSpawn._hallInUse.put(31923, false);
		FourSepulchersSpawn._hallInUse.put(31924, false);
		if (!FourSepulchersSpawn._archonSpawned.isEmpty())
		{
			final Set<Integer> npcIdSet = FourSepulchersSpawn._archonSpawned.keySet();
			for (int npcId : npcIdSet)
			{
				FourSepulchersSpawn._archonSpawned.put(npcId, false);
			}
		}
	}
	
	/**
	 * Method isEntryTime.
	 * @return boolean
	 */
	public static boolean isEntryTime()
	{
		return _inEntryTime;
	}
	
	/**
	 * Method isAttackTime.
	 * @return boolean
	 */
	public static boolean isAttackTime()
	{
		return _inAttackTime;
	}
	
	/**
	 * Method tryEntry.
	 * @param npc NpcInstance
	 * @param player Player
	 */
	public static synchronized void tryEntry(NpcInstance npc, Player player)
	{
		final int npcId = npc.getNpcId();
		switch (npcId)
		{
			case 31921:
			case 31922:
			case 31923:
			case 31924:
				break;
			default:
				return;
		}
		if (FourSepulchersSpawn._hallInUse.get(npcId))
		{
			showHtmlFile(player, npcId + "-FULL.htm", npc, null);
			return;
		}
		if (!player.isInParty() || (player.getParty().getMemberCount() < 4))
		{
			showHtmlFile(player, npcId + "-SP.htm", npc, null);
			return;
		}
		if (!player.getParty().isLeader(player))
		{
			showHtmlFile(player, npcId + "-NL.htm", npc, null);
			return;
		}
		for (Player mem : player.getParty().getPartyMembers())
		{
			QuestState qs = mem.getQuestState(QUEST_ID);
			if ((qs == null) || (!qs.isStarted() && !qs.isCompleted()))
			{
				showHtmlFile(player, npcId + "-NS.htm", npc, mem);
				return;
			}
			if (mem.getInventory().getItemByItemId(ENTRANCE_PASS) == null)
			{
				showHtmlFile(player, npcId + "-SE.htm", npc, mem);
				return;
			}
			if (!mem.isQuestContinuationPossible(true))
			{
				return;
			}
			if (mem.isDead() || !mem.isInRange(player, 700))
			{
				return;
			}
		}
		if (!isEntryTime())
		{
			showHtmlFile(player, npcId + "-NE.htm", npc, null);
			return;
		}
		showHtmlFile(player, npcId + "-OK.htm", npc, null);
		entry(npcId, player);
	}
	
	/**
	 * Method entry.
	 * @param npcId int
	 * @param player Player
	 */
	private static void entry(int npcId, Player player)
	{
		final Location loc = FourSepulchersSpawn._startHallSpawns.get(npcId);
		for (Player member : player.getParty().getPartyMembers())
		{
			member.teleToLocation(Location.findPointToStay(member, loc, 0, 80));
			Functions.removeItem(member, ENTRANCE_PASS, 1);
			if (member.getInventory().getItemByItemId(ANTIQUE_BROOCH) == null)
			{
				Functions.addItem(member, USED_PASS, 1);
			}
			Functions.removeItem(member, CHAPEL_KEY, 999999);
		}
		FourSepulchersSpawn._hallInUse.put(npcId, true);
	}
	
	/**
	 * Method onDeath.
	 * @param self Creature
	 * @param killer Creature
	 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
	 */
	@Override
	public void onDeath(Creature self, Creature killer)
	{
		if (self.isPlayer() && (self.getZ() >= -7250) && (self.getZ() <= -6841) && checkIfInZone(self))
		{
			checkAnnihilated((Player) self);
		}
	}
	
	/**
	 * Method checkAnnihilated.
	 * @param player Player
	 */
	public static void checkAnnihilated(final Player player)
	{
		if (isPlayersAnnihilated())
		{
			ThreadPoolManager.getInstance().schedule(new RunnableImpl()
			{
				@Override
				public void runImpl()
				{
					if (player.getParty() != null)
					{
						for (Player mem : player.getParty().getPartyMembers())
						{
							if (!mem.isDead())
							{
								break;
							}
							mem.teleToLocation(169589 + Rnd.get(-80, 80), -90493 + Rnd.get(-80, 80), -2914);
						}
					}
					else
					{
						player.teleToLocation(169589 + Rnd.get(-80, 80), -90493 + Rnd.get(-80, 80), -2914);
					}
				}
			}, 5000);
		}
	}
	
	/**
	 * Method minuteSelect.
	 * @param min int
	 * @return int
	 */
	private static int minuteSelect(int min)
	{
		switch (min % 5)
		{
			case 0:
				return min;
			case 1:
				return min - 1;
			case 2:
				return min - 2;
			case 3:
				return min + 2;
			default:
				return min + 1;
		}
	}
	
	/**
	 * Method managerSay.
	 * @param min int
	 */
	public static void managerSay(int min)
	{
		if (_inAttackTime)
		{
			if (min < 5)
			{
				return;
			}
			min = minuteSelect(min);
			String msg = min + " minute(s) have passed.";
			if (min == 90)
			{
				msg = "Game over. The teleport will appear momentarily";
			}
			for (SepulcherNpcInstance npc : FourSepulchersSpawn._managers)
			{
				if (!FourSepulchersSpawn._hallInUse.get(npc.getNpcId()))
				{
					continue;
				}
				npc.sayInShout(msg);
			}
		}
		else if (_inEntryTime)
		{
			final String msg1 = "You may now enter the Sepulcher";
			final String msg2 = "If you place your hand on the stone statue in front of each sepulcher," + " you will be able to enter";
			for (SepulcherNpcInstance npc : FourSepulchersSpawn._managers)
			{
				npc.sayInShout(msg1);
				npc.sayInShout(msg2);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class ManagerSay extends RunnableImpl
	{
		/**
		 * Constructor for ManagerSay.
		 */
		ManagerSay()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			if (_inAttackTime)
			{
				final Calendar tmp = Calendar.getInstance();
				tmp.setTimeInMillis(System.currentTimeMillis() - _warmUpTimeEnd);
				if ((tmp.get(Calendar.MINUTE) + 5) < 50)
				{
					managerSay(tmp.get(Calendar.MINUTE));
					ThreadPoolManager.getInstance().schedule(new ManagerSay(), 5 * 60000);
				}
				else if ((tmp.get(Calendar.MINUTE) + 5) >= 50)
				{
					managerSay(90);
				}
			}
			else if (_inEntryTime)
			{
				managerSay(0);
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class ChangeEntryTime extends RunnableImpl
	{
		/**
		 * Constructor for ChangeEntryTime.
		 */
		ChangeEntryTime()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_inEntryTime = true;
			_inAttackTime = false;
			long interval = 0;
			if (_firstTimeRun)
			{
				interval = _entryTimeEnd - System.currentTimeMillis();
			}
			else
			{
				interval = 3 * 60000;
			}
			ThreadPoolManager.getInstance().execute(new ManagerSay());
			_changeWarmUpTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeWarmUpTime(), interval);
			if (_changeEntryTimeTask != null)
			{
				_changeEntryTimeTask.cancel(false);
				_changeEntryTimeTask = null;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class ChangeWarmUpTime extends RunnableImpl
	{
		/**
		 * Constructor for ChangeWarmUpTime.
		 */
		ChangeWarmUpTime()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_inEntryTime = true;
			_inAttackTime = false;
			long interval = 0;
			if (_firstTimeRun)
			{
				interval = _warmUpTimeEnd - System.currentTimeMillis();
			}
			else
			{
				interval = 2 * 60000;
			}
			_changeAttackTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeAttackTime(), interval);
			if (_changeWarmUpTimeTask != null)
			{
				_changeWarmUpTimeTask.cancel(false);
				_changeWarmUpTimeTask = null;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class ChangeAttackTime extends RunnableImpl
	{
		/**
		 * Constructor for ChangeAttackTime.
		 */
		ChangeAttackTime()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_inEntryTime = false;
			_inAttackTime = true;
			for (GateKeeper gk : FourSepulchersSpawn._GateKeepers)
			{
				SepulcherNpcInstance npc = new SepulcherNpcInstance(IdFactory.getInstance().getNextId(), gk.template);
				npc.spawnMe(gk);
				FourSepulchersSpawn._allMobs.add(npc);
			}
			FourSepulchersSpawn.locationShadowSpawns();
			FourSepulchersSpawn.spawnMysteriousBox(31921);
			FourSepulchersSpawn.spawnMysteriousBox(31922);
			FourSepulchersSpawn.spawnMysteriousBox(31923);
			FourSepulchersSpawn.spawnMysteriousBox(31924);
			if (!_firstTimeRun)
			{
				_warmUpTimeEnd = System.currentTimeMillis();
			}
			long interval = 0;
			if (_firstTimeRun)
			{
				for (double min = Calendar.getInstance().get(Calendar.MINUTE); min < _newCycleMin; min++)
				{
					if ((min % 5) == 0)
					{
						_log.info(Calendar.getInstance().getTime() + " Atk announce scheduled to " + min + " minute of this hour.");
						Calendar inter = Calendar.getInstance();
						inter.set(Calendar.MINUTE, (int) min);
						ThreadPoolManager.getInstance().schedule(new ManagerSay(), inter.getTimeInMillis() - Calendar.getInstance().getTimeInMillis());
						break;
					}
				}
			}
			else
			{
				ThreadPoolManager.getInstance().schedule(new ManagerSay(), 5 * 60400);
			}
			if (_firstTimeRun)
			{
				interval = _attackTimeEnd - System.currentTimeMillis();
			}
			else
			{
				interval = 50 * 60000;
			}
			_changeCoolDownTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeCoolDownTime(), interval);
			if (_changeAttackTimeTask != null)
			{
				_changeAttackTimeTask.cancel(false);
				_changeAttackTimeTask = null;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class ChangeCoolDownTime extends RunnableImpl
	{
		/**
		 * Constructor for ChangeCoolDownTime.
		 */
		ChangeCoolDownTime()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_inEntryTime = false;
			_inAttackTime = false;
			cleanUp();
			final Calendar time = Calendar.getInstance();
			if ((Calendar.getInstance().get(Calendar.MINUTE) > _newCycleMin) && !_firstTimeRun)
			{
				time.set(Calendar.HOUR, Calendar.getInstance().get(Calendar.HOUR) + 1);
			}
			time.set(Calendar.MINUTE, _newCycleMin);
			_log.info("FourSepulchersManager: Entry time: " + time.getTime());
			if (_firstTimeRun)
			{
				_firstTimeRun = false;
			}
			final long interval = time.getTimeInMillis() - System.currentTimeMillis();
			_changeEntryTimeTask = ThreadPoolManager.getInstance().schedule(new ChangeEntryTime(), interval);
			if (_changeCoolDownTimeTask != null)
			{
				_changeCoolDownTimeTask.cancel(false);
				_changeCoolDownTimeTask = null;
			}
		}
	}
	
	/**
	 * Method getHallGateKeeper.
	 * @param npcId int
	 * @return GateKeeper
	 */
	public static GateKeeper getHallGateKeeper(int npcId)
	{
		for (GateKeeper gk : FourSepulchersSpawn._GateKeepers)
		{
			if (gk.template.npcId == npcId)
			{
				return gk;
			}
		}
		return null;
	}
	
	/**
	 * Method showHtmlFile.
	 * @param player Player
	 * @param file String
	 * @param npc NpcInstance
	 * @param member Player
	 */
	public static void showHtmlFile(Player player, String file, NpcInstance npc, Player member)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
		html.setFile("SepulcherNpc/" + file);
		if (member != null)
		{
			html.replace("%member%", member.getName());
		}
		player.sendPacket(html);
	}
	
	/**
	 * Method isPlayersAnnihilated.
	 * @return boolean
	 */
	private static boolean isPlayersAnnihilated()
	{
		for (Player pc : getPlayersInside())
		{
			if (!pc.isDead())
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method getPlayersInside.
	 * @return List<Player>
	 */
	private static List<Player> getPlayersInside()
	{
		final List<Player> result = new ArrayList<>();
		for (Zone zone : getZones())
		{
			result.addAll(zone.getInsidePlayers());
		}
		return result;
	}
	
	/**
	 * Method checkIfInZone.
	 * @param cha Creature
	 * @return boolean
	 */
	private static boolean checkIfInZone(Creature cha)
	{
		for (Zone zone : getZones())
		{
			if (zone.checkIfInZone(cha))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method getZones.
	 * @return Zone[]
	 */
	public static Zone[] getZones()
	{
		return _zone;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		init();
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		// empty method
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
}
