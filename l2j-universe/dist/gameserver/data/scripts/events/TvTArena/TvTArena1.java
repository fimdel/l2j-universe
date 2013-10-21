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
public class TvTArena1 extends Functions implements ScriptFile, OnDeathListener, OnTeleportListener, OnPlayerExitListener
{
	/**
	 * Field _log.
	 */
	private static final Logger _log = LoggerFactory.getLogger(TvTArena1.class);
	
	/**
	 * @author Mobius
	 */
	private static class TvTArena1Impl extends TvTTemplate
	{
		/**
		 * Constructor for TvTArena1Impl.
		 */
		TvTArena1Impl()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onLoad.
		 */
		@Override
		protected void onLoad()
		{
			_managerId = 31390;
			_className = "TvTArena1";
			_status = 0;
			_team1list = new CopyOnWriteArrayList<>();
			_team2list = new CopyOnWriteArrayList<>();
			_team1live = new CopyOnWriteArrayList<>();
			_team2live = new CopyOnWriteArrayList<>();
			_zoneListener = new ZoneListener();
			_zone = ReflectionUtils.getZone("[tvt_arena1]");
			_zone.addListener(_zoneListener);
			_team1points = new ArrayList<>();
			_team2points = new ArrayList<>();
			_team1points.add(new Location(-81806, -44865, -11418));
			_team1points.add(new Location(-81617, -44893, -11418));
			_team1points.add(new Location(-81440, -44945, -11418));
			_team1points.add(new Location(-81301, -48066, -11418));
			_team1points.add(new Location(-81168, -45208, -11418));
			_team1points.add(new Location(-81114, -46379, -11418));
			_team1points.add(new Location(-81068, -45570, -11418));
			_team1points.add(new Location(-81114, -45728, -11418));
			_team1points.add(new Location(-81162, -45934, -11418));
			_team1points.add(new Location(-81280, -46045, -11418));
			_team1points.add(new Location(-81424, -46196, -11418));
			_team1points.add(new Location(-81578, -46238, -11418));
			_team2points.add(new Location(-81792, -46299, -11418));
			_team2points.add(new Location(-81959, -46247, -11418));
			_team2points.add(new Location(-82147, -46206, -11418));
			_team2points.add(new Location(-82256, -46093, -11418));
			_team2points.add(new Location(-82418, -45940, -11418));
			_team2points.add(new Location(-82455, -45779, -11418));
			_team2points.add(new Location(-82513, -45573, -11418));
			_team2points.add(new Location(-82464, -45499, -11418));
			_team2points.add(new Location(-82421, -45215, -11418));
			_team2points.add(new Location(-82308, -45106, -11418));
			_team2points.add(new Location(-82160, -44948, -11418));
			_team2points.add(new Location(-81978, -44904, -11418));
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
			_instance = new TvTArena1Impl();
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
			_log.info("Loaded Event: TvT Arena 1 [state: activated]");
		}
		else
		{
			_log.info("Loaded Event: TvT Arena 1 [state: deactivated]");
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
	 * Method DialogAppend_31390.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31390(Integer val)
	{
		if (val == 0)
		{
			final Player player = getSelf();
			if (player.isGM())
			{
				return HtmCache.getInstance().getNotNull("scripts/events/TvTArena/31390.htm", player) + HtmCache.getInstance().getNotNull("scripts/events/TvTArena/31390-4.htm", player);
			}
			return HtmCache.getInstance().getNotNull("scripts/events/TvTArena/31390.htm", player);
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
		return IsActive("TvT Arena 1");
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
		if (SetActive("TvT Arena 1", true))
		{
			spawnEventManagers();
			System.out.println("Event: TvT Arena 1 started.");
			Announcements.getInstance().announceToAll("�?ачал�?�? TvT Arena 1 �?вент.");
		}
		else
		{
			player.sendMessage("TvT Arena 1 Event already started.");
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
		if (SetActive("TvT Arena 1", false))
		{
			ServerVariables.unset("TvT Arena 1");
			unSpawnEventManagers();
			stop();
			System.out.println("TvT Arena 1 Event stopped.");
			Announcements.getInstance().announceToAll("TvT Arena 1 �?вент окончен.");
		}
		else
		{
			player.sendMessage("TvT Arena 1 Event not started.");
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
				149167,
				-3495,
				0
			}
		};
		final NpcTemplate template = NpcHolder.getInstance().getTemplate(31390);
		for (int[] element : EVENT_MANAGERS)
		{
			SimpleSpawner sp = new SimpleSpawner(template);
			sp.setLocx(element[0]);
			sp.setLocy(element[1]);
			sp.setLocz(element[2]);
			sp.setHeading(element[3]);
			NpcInstance npc = sp.doSpawn(true);
			npc.setName("Arena 1");
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
