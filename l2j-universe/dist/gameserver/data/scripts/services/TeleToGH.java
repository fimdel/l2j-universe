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
package services;

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.Config;
import lineage2.gameserver.instancemanager.ReflectionManager;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.SimpleSpawner;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.scripts.Functions;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.utils.Location;
import lineage2.gameserver.utils.PositionUtils;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class TeleToGH extends Functions implements ScriptFile
{
	/**
	 * Field _spawns.
	 */
	private static List<SimpleSpawner> _spawns = new ArrayList<>();
	/**
	 * Field _zone.
	 */
	private static Zone _zone = ReflectionUtils.getZone("[giran_harbor_offshore]");
	/**
	 * Field _zoneListener.
	 */
	private static ZoneListener _zoneListener;
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		if (!Config.SERVICES_GIRAN_HARBOR_ENABLED)
		{
			return;
		}
		ReflectionManager.GIRAN_HARBOR.setCoreLoc(new Location(47416, 186568, -3480));
		SimpleSpawner spawn = new SimpleSpawner(30086);
		spawn.setLocx(48059);
		spawn.setLocy(186791);
		spawn.setLocz(-3512);
		spawn.setAmount(1);
		spawn.setHeading(42000);
		spawn.setRespawnDelay(5);
		spawn.setReflection(ReflectionManager.GIRAN_HARBOR);
		spawn.init();
		_spawns.add(spawn);
		spawn = new SimpleSpawner(32169);
		spawn.setLocx(48146);
		spawn.setLocy(186753);
		spawn.setLocz(-3512);
		spawn.setAmount(1);
		spawn.setHeading(42000);
		spawn.setRespawnDelay(5);
		spawn.setReflection(ReflectionManager.GIRAN_HARBOR);
		spawn.init();
		_spawns.add(spawn);
		spawn = new SimpleSpawner(13129);
		spawn.setLocx(47984);
		spawn.setLocy(186832);
		spawn.setLocz(-3445);
		spawn.setAmount(1);
		spawn.setHeading(42000);
		spawn.setRespawnDelay(5);
		spawn.setReflection(ReflectionManager.GIRAN_HARBOR);
		spawn.init();
		_spawns.add(spawn);
		spawn = new SimpleSpawner(31860);
		spawn.setLocx(48129);
		spawn.setLocy(186828);
		spawn.setLocz(-3512);
		spawn.setAmount(1);
		spawn.setHeading(45452);
		spawn.setRespawnDelay(5);
		spawn.setReflection(ReflectionManager.GIRAN_HARBOR);
		spawn.init();
		_spawns.add(spawn);
		spawn = new SimpleSpawner(30300);
		spawn.setLocx(48102);
		spawn.setLocy(186772);
		spawn.setLocz(-3512);
		spawn.setAmount(1);
		spawn.setHeading(42000);
		spawn.setRespawnDelay(5);
		spawn.setReflection(ReflectionManager.GIRAN_HARBOR);
		spawn.init();
		_spawns.add(spawn);
		spawn = new SimpleSpawner(32320);
		spawn.setLocx(47772);
		spawn.setLocy(186905);
		spawn.setLocz(-3480);
		spawn.setAmount(1);
		spawn.setHeading(42000);
		spawn.setRespawnDelay(5);
		spawn.setReflection(ReflectionManager.GIRAN_HARBOR);
		spawn.init();
		_spawns.add(spawn);
		spawn = new SimpleSpawner(32320);
		spawn.setLocx(46360);
		spawn.setLocy(187672);
		spawn.setLocz(-3480);
		spawn.setAmount(1);
		spawn.setHeading(42000);
		spawn.setRespawnDelay(5);
		spawn.setReflection(ReflectionManager.GIRAN_HARBOR);
		spawn.init();
		_spawns.add(spawn);
		spawn = new SimpleSpawner(32320);
		spawn.setLocx(49016);
		spawn.setLocy(185960);
		spawn.setLocz(-3480);
		spawn.setAmount(1);
		spawn.setHeading(42000);
		spawn.setRespawnDelay(5);
		spawn.setReflection(ReflectionManager.GIRAN_HARBOR);
		spawn.init();
		_spawns.add(spawn);
		_zoneListener = new ZoneListener();
		_zone.addListener(_zoneListener);
		_zone.setReflection(ReflectionManager.GIRAN_HARBOR);
		_zone.setActive(true);
		Zone zone = ReflectionUtils.getZone("[giran_harbor_peace_alt]");
		zone.setReflection(ReflectionManager.GIRAN_HARBOR);
		zone.setActive(true);
		zone = ReflectionUtils.getZone("[giran_harbor_no_trade]");
		zone.setReflection(ReflectionManager.GIRAN_HARBOR);
		zone.setActive(true);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		_zone.removeListener(_zoneListener);
		for (SimpleSpawner spawn : _spawns)
		{
			spawn.deleteAll();
		}
		_spawns.clear();
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
	
	/**
	 * Method toGH.
	 */
	public void toGH()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (!NpcInstance.canBypassCheck(player, npc))
		{
			return;
		}
		player.setVar("backCoords", player.getLoc().toXYZString(), -1);
		player.teleToLocation(Location.findPointToStay(_zone.getSpawn(), 30, 200, ReflectionManager.GIRAN_HARBOR.getGeoIndex()), ReflectionManager.GIRAN_HARBOR);
	}
	
	/**
	 * Method fromGH.
	 */
	public void fromGH()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		if (!NpcInstance.canBypassCheck(player, npc))
		{
			return;
		}
		String var = player.getVar("backCoords");
		if ((var == null) || var.equals(""))
		{
			teleOut();
			return;
		}
		player.teleToLocation(Location.parseLoc(var), 0);
	}
	
	/**
	 * Method teleOut.
	 */
	public void teleOut()
	{
		Player player = getSelf();
		NpcInstance npc = getNpc();
		if ((player == null) || (npc == null))
		{
			return;
		}
		player.teleToLocation(46776, 185784, -3528, 0);
		show("I don't know from where you came here, but I can teleport you the another border side.", player, npc);
	}
	
	/**
	 * Method DialogAppend_30059.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30059(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30080.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30080(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30177.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30177(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30233.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30233(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30256.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30256(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30320.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30320(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30848.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30848(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30878.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30878(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30899.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30899(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31210.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31210(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31275.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31275(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31320.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31320(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_31964.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_31964(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30006.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30006(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30134.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30134(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30146.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30146(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_32163.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_32163(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30576.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30576(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method DialogAppend_30540.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_30540(Integer val)
	{
		return getHtmlAppends(val);
	}
	
	/**
	 * Method getHtmlAppends.
	 * @param val Integer
	 * @return String
	 */
	public String getHtmlAppends(Integer val)
	{
		if ((val != 0) || !Config.SERVICES_GIRAN_HARBOR_ENABLED)
		{
			return "";
		}
		Player player = getSelf();
		if (player == null)
		{
			return "";
		}
		return "<br>[scripts_services.TeleToGH:toGH @811;Giran Harbor|\"I want free admission to Giran Harbor.\"]";
	}
	
	/**
	 * Method DialogAppend_13129.
	 * @param val Integer
	 * @return String
	 */
	public String DialogAppend_13129(Integer val)
	{
		return getHtmlAppends2(val);
	}
	
	/**
	 * Method getHtmlAppends2.
	 * @param val Integer
	 * @return String
	 */
	public String getHtmlAppends2(Integer val)
	{
		if ((val != 0) || !Config.SERVICES_GIRAN_HARBOR_ENABLED)
		{
			return "";
		}
		Player player = getSelf();
		if ((player == null) || (player.getReflectionId() != -2))
		{
			return "";
		}
		return "<br>[scripts_services.ManaRegen:DoManaRegen|Full MP Regeneration. (1 MP for 5 Adena)]<br>[scripts_services.TeleToGH:fromGH @811;From Giran Harbor|\"Exit the Giran Harbor.\"]<br>";
	}
	
	/**
	 * @author Mobius
	 */
	public class ZoneListener implements OnZoneEnterLeaveListener
	{
		/**
		 * Method onZoneEnter.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneEnter(Zone, Creature)
		 */
		@Override
		public void onZoneEnter(Zone zone, Creature cha)
		{
		}
		
		/**
		 * Method onZoneLeave.
		 * @param zone Zone
		 * @param cha Creature
		 * @see lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener#onZoneLeave(Zone, Creature)
		 */
		@Override
		public void onZoneLeave(Zone zone, Creature cha)
		{
			Player player = cha.getPlayer();
			if (player != null)
			{
				if (Config.SERVICES_GIRAN_HARBOR_ENABLED && (player.getReflection() == ReflectionManager.GIRAN_HARBOR) && player.isVisible())
				{
					double angle = PositionUtils.convertHeadingToDegree(cha.getHeading());
					double radian = Math.toRadians(angle - 90);
					cha.teleToLocation((int) (cha.getX() + (50 * Math.sin(radian))), (int) (cha.getY() - (50 * Math.cos(radian))), cha.getZ());
				}
			}
		}
	}
}
