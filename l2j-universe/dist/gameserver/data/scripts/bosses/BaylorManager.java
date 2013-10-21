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
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.Config;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.geodata.GeoEngine;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.network.serverpackets.FlyToLocation;
import lineage2.gameserver.network.serverpackets.FlyToLocation.FlyType;
import lineage2.gameserver.network.serverpackets.SocialAction;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.Log;
import lineage2.gameserver.utils.PositionUtils;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class BaylorManager extends Functions implements ScriptFile
{
	/**
	 * Method baylor_spawn.
	 * @param loc Location
	 * @param npcId int
	 * @return NpcInstance
	 */
	public static NpcInstance baylor_spawn(Location loc, int npcId)
	{
		final NpcTemplate template = NpcHolder.getInstance().getTemplate(npcId);
		final NpcInstance npc = template.getNewInstance();
		npc.setSpawnedLoc(loc);
		npc.setHeading(loc.h);
		npc.setLoc(loc);
		npc.setReflection(currentReflection);
		npc.spawnMe();
		return npc;
	}
	
	/**
	 * @author Mobius
	 */
	private static class ActivityTimeEnd extends RunnableImpl
	{
		/**
		 * Constructor for ActivityTimeEnd.
		 */
		ActivityTimeEnd()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			setIntervalEndTask();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class BaylorSpawn extends RunnableImpl
	{
		/**
		 * Field _npcId.
		 */
		private final int _npcId;
		/**
		 * Field _pos.
		 */
		private final Location _pos = new Location(153569, 142075, -12711, 44732);
		
		/**
		 * Constructor for BaylorSpawn.
		 * @param npcId int
		 */
		BaylorSpawn(int npcId)
		{
			_npcId = npcId;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			switch (_npcId)
			{
				case CrystalPrisonGuard:
					final Reflection ref = ReflectionManager.getInstance().get(currentReflection);
					for (int doorId : doors)
					{
						ref.openDoor(doorId);
					}
					for (int i = 0; i < _crystalineLocation.length; i++)
					{
						_crystaline[i] = baylor_spawn(_crystalineLocation[i], CrystalPrisonGuard);
						_crystaline[i].setRunning();
						_crystaline[i].moveToLocation(_pos, 300, false);
						ThreadPoolManager.getInstance().schedule(new Social(_crystaline[i], 2), 15000);
					}
					break;
				case Baylor:
					Dying = false;
					_baylor = baylor_spawn(new Location(153569, 142075, -12732, 59864), Baylor);
					_baylor.addListener(BaylorDeathListener.getInstance());
					_state.setRespawnDate(getRespawnInterval() + FWBA_ACTIVITYTIMEOFMOBS);
					_state.setState(EpicBossState.State.ALIVE);
					_state.update();
					if (_socialTask != null)
					{
						_socialTask.cancel(false);
						_socialTask = null;
					}
					_socialTask = ThreadPoolManager.getInstance().schedule(new Social(_baylor, 1), 500);
					if (_endSceneTask != null)
					{
						_endSceneTask.cancel(false);
						_endSceneTask = null;
					}
					_endSceneTask = ThreadPoolManager.getInstance().schedule(new EndScene(), 23000);
					if (_activityTimeEndTask != null)
					{
						_activityTimeEndTask.cancel(false);
						_activityTimeEndTask = null;
					}
					_activityTimeEndTask = ThreadPoolManager.getInstance().schedule(new ActivityTimeEnd(), FWBA_ACTIVITYTIMEOFMOBS);
					break;
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class IntervalEnd extends RunnableImpl
	{
		/**
		 * Constructor for IntervalEnd.
		 */
		IntervalEnd()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_state.setState(EpicBossState.State.NOTSPAWN);
			_state.update();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class Social extends RunnableImpl
	{
		/**
		 * Field _action.
		 */
		private final int _action;
		/**
		 * Field _npc.
		 */
		private final NpcInstance _npc;
		
		/**
		 * Constructor for Social.
		 * @param npc NpcInstance
		 * @param actionId int
		 */
		Social(NpcInstance npc, int actionId)
		{
			_npc = npc;
			_action = actionId;
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			_npc.broadcastPacket(new SocialAction(_npc.getObjectId(), _action));
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class EndScene extends RunnableImpl
	{
		/**
		 * Constructor for EndScene.
		 */
		EndScene()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (Player player : getPlayersInside())
			{
				player.unblock();
				if (_baylor != null)
				{
					double angle = PositionUtils.convertHeadingToDegree(_baylor.getHeading());
					double radian = Math.toRadians(angle - 90);
					int x1 = -(int) (Math.sin(radian) * 600);
					int y1 = (int) (Math.cos(radian) * 600);
					Location flyLoc = GeoEngine.moveCheck(player.getX(), player.getY(), player.getZ(), player.getX() + x1, player.getY() + y1, player.getGeoIndex());
					player.setLoc(flyLoc);
					player.broadcastPacket(new FlyToLocation(player, flyLoc, FlyType.THROW_HORIZONTAL, 0));
				}
			}
			for (NpcInstance npc : _crystaline)
			{
				if (npc != null)
				{
					npc.reduceCurrentHp(npc.getMaxHp() + 1, 0, npc, null, true, true, false, false, false, false, false);
				}
			}
		}
	}
	
	/**
	 * Field Baylor. (value is 29099)
	 */
	private static final int Baylor = 29099;
	/**
	 * Field CrystalPrisonGuard. (value is 29100)
	 */
	private static final int CrystalPrisonGuard = 29100;
	/**
	 * Field Parme. (value is 32271)
	 */
	private static final int Parme = 32271;
	/**
	 * Field Oracle. (value is 32273)
	 */
	private static final int Oracle = 32273;
	/**
	 * Field _crystalineLocation.
	 */
	static final Location[] _crystalineLocation =
	{
		new Location(154404, 140596, -12711, 44732),
		new Location(153574, 140402, -12711, 44732),
		new Location(152105, 141230, -12711, 44732),
		new Location(151877, 142095, -12711, 44732),
		new Location(152109, 142920, -12711, 44732),
		new Location(152730, 143555, -12711, 44732),
		new Location(154439, 143538, -12711, 44732),
		new Location(155246, 142068, -12711, 44732)
	};
	/**
	 * Field _baylorChestLocation.
	 */
	private static final Location[] _baylorChestLocation =
	{
		new Location(153763, 142075, -12741, 64792),
		new Location(153701, 141942, -12741, 57739),
		new Location(153573, 141894, -12741, 49471),
		new Location(153445, 141945, -12741, 41113),
		new Location(153381, 142076, -12741, 32767),
		new Location(153441, 142211, -12741, 25730),
		new Location(153573, 142260, -12741, 16185),
		new Location(153706, 142212, -12741, 7579),
		new Location(153571, 142860, -12741, 16716),
		new Location(152783, 142077, -12741, 32176),
		new Location(153571, 141274, -12741, 49072),
		new Location(154365, 142073, -12741, 64149),
		new Location(154192, 142697, -12741, 7894),
		new Location(152924, 142677, -12741, 25072),
		new Location(152907, 141428, -12741, 39590),
		new Location(154243, 141411, -12741, 55500)
	};
	/**
	 * Field doors.
	 */
	static final int[] doors =
	{
		24220009,
		24220011,
		24220012,
		24220014,
		24220015,
		24220016,
		24220017,
		24220019
	};
	/**
	 * Field _crystaline.
	 */
	static NpcInstance[] _crystaline = new NpcInstance[8];
	/**
	 * Field _baylor.
	 */
	static NpcInstance _baylor;
	/**
	 * Field _intervalEndTask.
	 */
	private static ScheduledFuture<?> _intervalEndTask = null;
	/**
	 * Field _activityTimeEndTask.
	 */
	static ScheduledFuture<?> _activityTimeEndTask = null;
	/**
	 * Field _socialTask.
	 */
	static ScheduledFuture<?> _socialTask = null;
	/**
	 * Field _endSceneTask.
	 */
	static ScheduledFuture<?> _endSceneTask = null;
	/**
	 * Field _isAlreadyEnteredOtherParty.
	 */
	private static boolean _isAlreadyEnteredOtherParty = false;
	/**
	 * Field _state.
	 */
	static EpicBossState _state;
	/**
	 * Field _zone.
	 */
	private static Zone _zone;
	/**
	 * Field FWBA_ACTIVITYTIMEOFMOBS.
	 */
	private static final int FWBA_ACTIVITYTIMEOFMOBS = 120 * 60000;
	/**
	 * Field FWBA_FIXINTERVALOFBAYLORSPAWN.
	 */
	private static final int FWBA_FIXINTERVALOFBAYLORSPAWN = 1440 * 60000;
	/**
	 * Field FWBA_RANDOMINTERVALOFBAYLORSPAWN.
	 */
	private static final int FWBA_RANDOMINTERVALOFBAYLORSPAWN = 1440 * 60000;
	/**
	 * Field FWBA_ENABLESINGLEPLAYER. (value is false)
	 */
	private static final boolean FWBA_ENABLESINGLEPLAYER = false;
	/**
	 * Field Dying.
	 */
	static boolean Dying = false;
	/**
	 * Field currentReflection.
	 */
	static int currentReflection;
	
	/**
	 * Method canIntoBaylorLair.
	 * @param pc Player
	 * @return int
	 */
	public static int canIntoBaylorLair(Player pc)
	{
		if (pc.isGM())
		{
			return 0;
		}
		if (!FWBA_ENABLESINGLEPLAYER && !pc.isInParty())
		{
			return 4;
		}
		else if (_isAlreadyEnteredOtherParty)
		{
			return 2;
		}
		else if (_state.getState().equals(EpicBossState.State.NOTSPAWN))
		{
			return 0;
		}
		else if (_state.getState().equals(EpicBossState.State.ALIVE) || _state.getState().equals(EpicBossState.State.DEAD))
		{
			return 1;
		}
		else if (_state.getState().equals(EpicBossState.State.INTERVAL))
		{
			return 3;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Method checkAnnihilated.
	 */
	synchronized static void checkAnnihilated()
	{
		if (isPlayersAnnihilated())
		{
			setIntervalEndTask();
		}
	}
	
	/**
	 * Method entryToBaylorLair.
	 * @param pc Player
	 */
	public synchronized static void entryToBaylorLair(Player pc)
	{
		currentReflection = pc.getReflectionId();
		_zone.setReflection(pc.getReflection());
		ReflectionManager.getInstance().get(currentReflection).closeDoor(24220008);
		ThreadPoolManager.getInstance().schedule(new BaylorSpawn(CrystalPrisonGuard), 20000);
		ThreadPoolManager.getInstance().schedule(new BaylorSpawn(Baylor), 40000);
		if (pc.getParty() == null)
		{
			pc.teleToLocation(153569 + Rnd.get(-80, 80), 142075 + Rnd.get(-80, 80), -12732);
			pc.block();
		}
		else
		{
			final List<Player> members = new ArrayList<>();
			for (Player mem : pc.getParty().getPartyMembers())
			{
				if (!mem.isDead() && mem.isInRange(pc, 1500))
				{
					members.add(mem);
				}
			}
			for (Player mem : members)
			{
				mem.teleToLocation(153569 + Rnd.get(-80, 80), 142075 + Rnd.get(-80, 80), -12732);
				mem.block();
			}
		}
		_isAlreadyEnteredOtherParty = true;
	}
	
	/**
	 * Method getPlayersInside.
	 * @return List<Player>
	 */
	static List<Player> getPlayersInside()
	{
		final List<Player> result = new ArrayList<>();
		for (Player player : getZone().getInsidePlayers())
		{
			result.add(player);
		}
		return result;
	}
	
	/**
	 * Method getRespawnInterval.
	 * @return int
	 */
	static int getRespawnInterval()
	{
		return (int) (Config.ALT_RAID_RESPAWN_MULTIPLIER * (FWBA_FIXINTERVALOFBAYLORSPAWN + Rnd.get(0, FWBA_RANDOMINTERVALOFBAYLORSPAWN)));
	}
	
	/**
	 * Method getZone.
	 * @return Zone
	 */
	public static Zone getZone()
	{
		return _zone;
	}
	
	/**
	 * Method init.
	 */
	private static void init()
	{
		_state = new EpicBossState(Baylor);
		_zone = ReflectionUtils.getZone("[baylor_epic]");
		_zone.addListener(BaylorZoneListener.getInstance());
		_isAlreadyEnteredOtherParty = false;
		Log.add("BaylorManager : State of Baylor is " + _state.getState() + ".", "bosses");
		if (!_state.getState().equals(EpicBossState.State.NOTSPAWN))
		{
			setIntervalEndTask();
		}
		final Date dt = new Date(_state.getRespawnDate());
		Log.add("BaylorManager : Next spawn date of Baylor is " + dt + ".", "bosses");
		Log.add("BaylorManager : Init BaylorManager.", "bosses");
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
	 * Method onBaylorDie.
	 */
	static void onBaylorDie()
	{
		if (Dying)
		{
			return;
		}
		Dying = true;
		_state.setRespawnDate(getRespawnInterval());
		_state.setState(EpicBossState.State.INTERVAL);
		_state.update();
		Log.add("Baylor died", "bosses");
		baylor_spawn(_baylorChestLocation[Rnd.get(_baylorChestLocation.length)], 29116);
		baylor_spawn(new Location(153570, 142067, -9727, 55500), Parme);
		baylor_spawn(new Location(153569, 142075, -12732, 55500), Oracle);
		startCollapse();
	}
	
	/**
	 * @author Mobius
	 */
	private static class BaylorZoneListener implements OnZoneEnterLeaveListener
	{
		/**
		 * Field _instance.
		 */
		private static final OnZoneEnterLeaveListener _instance = new BaylorZoneListener();
		
		/**
		 * Method getInstance.
		 * @return OnZoneEnterLeaveListener
		 */
		public static OnZoneEnterLeaveListener getInstance()
		{
			return _instance;
		}
		
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param actor Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
		@Override
		public void onZoneEnter(Zone zone, Creature actor)
		{
			if (actor.isPlayer())
			{
				actor.addListener(PlayerDeathListener.getInstance());
			}
		}
		
		/**
		 * Method onZoneLeave.
		 * @param zone Zone
		 * @param actor Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
		 */
		@Override
		public void onZoneLeave(Zone zone, Creature actor)
		{
			if (actor.isPlayer())
			{
				actor.removeListener(PlayerDeathListener.getInstance());
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class PlayerDeathListener implements OnDeathListener
	{
		/**
		 * Field _instance.
		 */
		private static final OnDeathListener _instance = new PlayerDeathListener();
		
		/**
		 * Method getInstance.
		 * @return OnDeathListener
		 */
		public static OnDeathListener getInstance()
		{
			return _instance;
		}
		
		/**
		 * Method onDeath.
		 * @param actor Creature
		 * @param killer Creature
		 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
		 */
		@Override
		public void onDeath(Creature actor, Creature killer)
		{
			checkAnnihilated();
		}
	}
	
	/**
	 * @author Mobius
	 */
	private static class BaylorDeathListener implements OnDeathListener
	{
		/**
		 * Field _instance.
		 */
		private static final OnDeathListener _instance = new BaylorDeathListener();
		
		/**
		 * Method getInstance.
		 * @return OnDeathListener
		 */
		public static OnDeathListener getInstance()
		{
			return _instance;
		}
		
		/**
		 * Method onDeath.
		 * @param actor Creature
		 * @param killer Creature
		 * @see lineage2.gameserver.listener.actor.OnDeathListener#onDeath(Creature, Creature)
		 */
		@Override
		public void onDeath(Creature actor, Creature killer)
		{
			onBaylorDie();
		}
	}
	
	/**
	 * Method setIntervalEndTask.
	 */
	static void setIntervalEndTask()
	{
		setUnspawn();
		if (_state.getState().equals(EpicBossState.State.ALIVE))
		{
			_state.setState(EpicBossState.State.NOTSPAWN);
			_state.update();
			return;
		}
		if (!_state.getState().equals(EpicBossState.State.INTERVAL))
		{
			_state.setRespawnDate(getRespawnInterval());
			_state.setState(EpicBossState.State.INTERVAL);
			_state.update();
		}
		_intervalEndTask = ThreadPoolManager.getInstance().schedule(new IntervalEnd(), _state.getInterval());
	}
	
	/**
	 * Method setUnspawn.
	 */
	private static void setUnspawn()
	{
		if (!_isAlreadyEnteredOtherParty)
		{
			return;
		}
		_isAlreadyEnteredOtherParty = false;
		startCollapse();
		if (_baylor != null)
		{
			_baylor.deleteMe();
		}
		_baylor = null;
		for (NpcInstance npc : _crystaline)
		{
			if (npc != null)
			{
				npc.deleteMe();
			}
		}
		if (_intervalEndTask != null)
		{
			_intervalEndTask.cancel(false);
			_intervalEndTask = null;
		}
		if (_activityTimeEndTask != null)
		{
			_activityTimeEndTask.cancel(false);
			_activityTimeEndTask = null;
		}
	}
	
	/**
	 * Method startCollapse.
	 */
	private static void startCollapse()
	{
		if (currentReflection > 0)
		{
			final Reflection reflection = ReflectionManager.getInstance().get(currentReflection);
			if (reflection != null)
			{
				reflection.startCollapseTimer(300000);
			}
			currentReflection = 0;
		}
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
		setUnspawn();
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
