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
package events.TvTArena;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.gameserver.Announcements;
import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.instancemanager.ServerVariables;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerExitListener;
import lineage2.gameserver.listener.actor.player.OnTeleportListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TvTArena2 extends Functions implements ScriptFile, OnDeathListener, OnTeleportListener, OnPlayerExitListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(TvTArena2.class);
	
	/**
	 * @author Mobius
	 */
	private static class TvTArena2Impl extends TvTTemplate
	{
		/**
		 * Constructor for TvTArena2Impl.
		 */
		TvTArena2Impl()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onLoad.
		 */
		@Override
		protected void onLoad()
		{
			_managerId = 31391;
			_className = "TvTArena2";
			_status = 0;
			_team1list = new CopyOnWriteArrayList<>();
			_team2list = new CopyOnWriteArrayList<>();
			_team1live = new CopyOnWriteArrayList<>();
			_team2live = new CopyOnWriteArrayList<>();
			_zoneListener = new ZoneListener();
			_zone = ReflectionUtils.getZone("[tvt_arena2]");
			_zone.addListener(_zoneListener);
			_team1points = new ArrayList<>();
			_team2points = new ArrayList<>();
			_team1points.add(new Location(-77724, -47901, -11518, -11418));
			_team1points.add(new Location(-77718, -48080, -11518, -11418));
			_team1points.add(new Location(-77699, -48280, -11518, -11418));
			_team1points.add(new Location(-77777, -48442, -11518, -11418));
			_team1points.add(new Location(-77863, -48622, -11518, -11418));
			_team1points.add(new Location(-78002, -48714, -11518, -11418));
			_team1points.add(new Location(-78168, -48835, -11518, -11418));
			_team1points.add(new Location(-78353, -48851, -11518, -11418));
			_team1points.add(new Location(-78543, -48864, -11518, -11418));
			_team1points.add(new Location(-78709, -48784, -11518, -11418));
			_team1points.add(new Location(-78881, -48702, -11518, -11418));
			_team1points.add(new Location(-78981, -48555, -11518, -11418));
			_team2points.add(new Location(-79097, -48400, -11518, -11418));
			_team2points.add(new Location(-79107, -48214, -11518, -11418));
			_team2points.add(new Location(-79125, -48027, -11518, -11418));
			_team2points.add(new Location(-79047, -47861, -11518, -11418));
			_team2points.add(new Location(-78965, -47689, -11518, -11418));
			_team2points.add(new Location(-78824, -47594, -11518, -11418));
			_team2points.add(new Location(-78660, -47474, -11518, -11418));
			_team2points.add(new Location(-78483, -47456, -11518, -11418));
			_team2points.add(new Location(-78288, -47440, -11518, -11418));
			_team2points.add(new Location(-78125, -47515, -11518, -11418));
			_team2points.add(new Location(-77953, -47599, -11518, -11418));
			_team2points.add(new Location(-77844, -47747, -11518, -11418));
		}
		
		/**
		 * Method onReload.
		 */
		@Override
		protected void onReload()
		{
			if (_status > 0)
			{
				template_stop();
			}
			_zone.removeListener(_zoneListener);
		}
	}
	
	/**
	 * Field _instance.
	 */
	private static TvTTemplate _instance;
	
	/**
	 * Method getInstance.
	 * @return TvTTemplate
	 */
	public static TvTTemplate getInstance()
	{
		if (_instance == null)
		{
			_instance = new TvTArena2Impl();
		}
		return _instance;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		CharListenerList.addGlobal(this);
		getInstance().onLoad();
		if (isActive())
		{
			spawnEventManagers();
			_log.info("Loaded Event: TvT Arena 2 [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: TvT Arena 2 [state: deactivated]");
		}
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		getInstance().onReload();
		unSpawnEventManagers();
		_instance = null;
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
		getInstance().onDeath(cha, killer);
	}
	
	/**
	 * Method onPlayerExit.
	 * @param player Player
	 * @see lineage2.gameserver.listener.actor.player.OnPlayerExitListener#onPlayerExit(Player)
	 */
	@Override
	public void onPlayerExit(Player player)
	{
		getInstance().onPlayerExit(player);
	}
	
	/**
	 * Method onTeleport.
	 * @param player Player
	 * @param x int
	 * @param y int
	 * @param z int
	 * @param reflection Reflection
	 * @see lineage2.gameserver.listener.actor.player.OnTeleportListener#onTeleport(Player, int, int, int, Reflection)
	 */
	@Override
	public void onTeleport(Player player, int x, int y, int z, Reflection reflection)
	{
		getInstance().onTeleport(player);
	}
	
	/**
	 * Method DialogAppend_31391.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31391(Integer val)
	{
		if (val == 0)
		{
			final Player player = getSelf();
			if (player.isGM())
			{
				return HtmCache.getInstance().getNotNull("scripts/events/TvTArena/31391.htm", player) + HtmCache.getInstance().getNotNull("scripts/events/TvTArena/31391-4.htm", player);
			}
			return HtmCache.getInstance().getNotNull("scripts/events/TvTArena/31391.htm", player);
		}
		return "";
	}
	
	/**
	 * Method create1.
	 */
	public void create1()
	{
		getInstance().template_create1(getSelf());
	}
	
	/**
	 * Method register.
	 */
	public void register()
	{
		getInstance().template_register(getSelf());
	}
	
	/**
	 * Method check1.
	 * @param var String[]
	 */
	public void check1(String[] var)
	{
		getInstance().template_check1(getSelf(), getNpc(), var);
	}
	
	/**
	 * Method register_check.
	 */
	public void register_check()
	{
		getInstance().template_register_check(getSelf());
	}
	
	/**
	 * Method stop.
	 */
	public void stop()
	{
		getInstance().template_stop();
	}
	
	/**
	 * Method announce.
	 */
	public void announce()
	{
		getInstance().template_announce();
	}
	
	/**
	 * Method prepare.
	 */
	public void prepare()
	{
		getInstance().template_prepare();
	}
	
	/**
	 * Method start.
	 */
	public void start()
	{
		getInstance().template_start();
	}
	
	/**
	 * Method timeOut.
	 */
	public void timeOut()
	{
		getInstance().template_timeOut();
	}
	
	/**
	 * Field _spawns.
	 */
	private final List<NpcInstance> _spawns = new ArrayList<>();
	
	/**
	 * Method isActive.
	 * @return boolean
	 */
	private boolean isActive()
	{
		return IsActive("TvT Arena 2");
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
		if (SetActive("TvT Arena 2", true))
		{
			spawnEventManagers();
			System.out.println("Event: TvT Arena 2 started.");
			Announcements.getInstance().announceToAll("�?ачал�?�? TvT Arena 2 �?вент.");
		}
		else
		{
			player.sendMessage("TvT Arena 2 Event already started.");
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
		if (SetActive("TvT Arena 2", false))
		{
			ServerVariables.unset("TvT Arena 2");
			unSpawnEventManagers();
			stop();
			System.out.println("TvT Arena 2 Event stopped.");
			Announcements.getInstance().announceToAll("TvT Arena 2 �?вент окончен.");
		}
		else
		{
			player.sendMessage("TvT Arena 2 Event not started.");
		}
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
				82840,
				149048,
				-3472,
				0
			}
		};
		final NpcTemplate template = NpcHolder.getInstance().getTemplate(31391);
		for (int[] element : EVENT_MANAGERS)
		{
			SimpleSpawner sp = new SimpleSpawner(template);
			sp.setLocx(element[0]);
			sp.setLocy(element[1]);
			sp.setLocz(element[2]);
			sp.setHeading(element[3]);
			NpcInstance npc = sp.doSpawn(true);
			npc.setName("Arena 2");
			npc.setTitle("TvT Event");
			_spawns.add(npc);
		}
	}
	
	/**
	 * Method unSpawnEventManagers.
	 */
	private void unSpawnEventManagers()
	{
		for (NpcInstance npc : _spawns)
		{
			npc.deleteMe();
		}
		_spawns.clear();
	}
}
