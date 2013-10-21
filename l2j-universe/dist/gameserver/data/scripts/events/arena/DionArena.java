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
package events.arena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.gameserver.data.htm.HtmCache;
import lineage2.gameserver.listener.actor.OnDeathListener;
import lineage2.gameserver.listener.actor.player.OnPlayerExitListener;
import lineage2.gameserver.listener.actor.player.OnTeleportListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.actor.listener.CharListenerList;
import lineage2.gameserver.model.entity.Reflection;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DionArena extends Functions implements ScriptFile, OnDeathListener, OnTeleportListener, OnPlayerExitListener
{
	/**
	 * @author Mobius
	 */
	private static class DionArenaImpl extends ArenaTemplate
	{
		/**
		 * Constructor for DionArenaImpl.
		 */
		DionArenaImpl()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method onLoad.
		 */
		@Override
		protected void onLoad()
		{
			_managerId = 20230001;
			_className = "DionArena";
			_status = 0;
			_team1list = new CopyOnWriteArrayList<>();
			_team2list = new CopyOnWriteArrayList<>();
			_team1live = new CopyOnWriteArrayList<>();
			_team2live = new CopyOnWriteArrayList<>();
			_expToReturn = new HashMap<>();
			_classToReturn = new HashMap<>();
			_zoneListener = new ZoneListener();
			_zone = ReflectionUtils.getZone("[dion_monster_pvp]");
			_zone.addListener(_zoneListener);
			_team1points = new ArrayList<>();
			_team2points = new ArrayList<>();
			_team1points.add(new Location(12053, 183101, -3563));
			_team1points.add(new Location(12253, 183101, -3563));
			_team1points.add(new Location(12459, 183101, -3563));
			_team1points.add(new Location(12659, 183101, -3563));
			_team1points.add(new Location(12851, 183101, -3563));
			_team2points.add(new Location(12851, 183941, -3563));
			_team2points.add(new Location(12659, 183941, -3563));
			_team2points.add(new Location(12459, 183941, -3563));
			_team2points.add(new Location(12253, 183941, -3563));
			_team2points.add(new Location(12053, 183941, -3563));
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
	private static ArenaTemplate _instance = new DionArenaImpl();
	
	/**
	 * Method getInstance.
	 * @return ArenaTemplate
	 */
	public static ArenaTemplate getInstance()
	{
		if (_instance == null)
		{
			_instance = new DionArenaImpl();
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
		getInstance().onLoad();
		CharListenerList.addGlobal(this);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		getInstance().onReload();
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
	 * Method DialogAppend_20230001.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_20230001(Integer val)
	{
		if (val == 0)
		{
			final Player player = getSelf();
			if (player.isGM())
			{
				return HtmCache.getInstance().getNotNull("scripts/events/arena/20230001.htm", player) + HtmCache.getInstance().getNotNull("scripts/events/arena/20230001-4.htm", player);
			}
			return HtmCache.getInstance().getNotNull("scripts/events/arena/20230001.htm", player);
		}
		return "";
	}
	
	/**
	 * Method DialogAppend_20230002.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_20230002(Integer val)
	{
		return DialogAppend_20230001(val);
	}
	
	/**
	 * Method create1.
	 */
	public void create1()
	{
		getInstance().template_create1(getSelf());
	}
	
	/**
	 * Method create2.
	 */
	public void create2()
	{
		getInstance().template_create2(getSelf());
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
		getInstance().template_check1(getSelf(), var);
	}
	
	/**
	 * Method check2.
	 * @param var String[]
	 */
	public void check2(String[] var)
	{
		getInstance().template_check2(getSelf(), var);
	}
	
	/**
	 * Method register_check.
	 * @param var String[]
	 */
	public void register_check(String[] var)
	{
		getInstance().template_register_check(getSelf(), var);
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
	public static void timeOut()
	{
		getInstance().template_timeOut();
	}
}
