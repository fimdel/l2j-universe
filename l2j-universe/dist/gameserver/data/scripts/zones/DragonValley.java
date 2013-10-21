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
package zones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import lineage2.commons.threading.RunnableImpl;
import lineage2.gameserver.ThreadPoolManager;
import lineage2.gameserver.listener.zone.OnZoneEnterLeaveListener;
import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.Party;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Zone;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.scripts.ScriptFile;
import lineage2.gameserver.tables.SkillTable;
import lineage2.gameserver.utils.ReflectionUtils;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class DragonValley implements ScriptFile
{
	/**
	 * Field _zoneListener.
	 */
	private static ZoneListener _zoneListener;
	/**
	 * Field zone.
	 */
	private static Zone zone;
	/**
	 * Field weight.
	 */
	private static final Map<ClassId, Double> weight = new HashMap<>();
	/**
	 * Field inzone.
	 */
	static List<Player> inzone = new ArrayList<>();
	/**
	 * Field buffTask.
	 */
	private static ScheduledFuture<?> buffTask;
	static
	{
		weight.put(ClassId.DUELIST, 0.2);
		weight.put(ClassId.DREADNOUGHT, 0.7);
		weight.put(ClassId.PHOENIX_KNIGHT, 0.5);
		weight.put(ClassId.HELL_KNIGHT, 0.5);
		weight.put(ClassId.SAGITTARIUS, 0.3);
		weight.put(ClassId.ADVENTURER, 0.4);
		weight.put(ClassId.ARCHMAGE, 0.3);
		weight.put(ClassId.SOULTAKER, 0.3);
		weight.put(ClassId.ARCANA_LORD, 1.);
		weight.put(ClassId.CARDINAL, -0.6);
		weight.put(ClassId.HIEROPHANT, 0.);
		weight.put(ClassId.EVAS_TEMPLAR, 0.8);
		weight.put(ClassId.SWORD_MUSE, 0.5);
		weight.put(ClassId.WIND_RIDER, 0.4);
		weight.put(ClassId.MOONLIGHT_SENTINEL, 0.3);
		weight.put(ClassId.MYSTIC_MUSE, 0.3);
		weight.put(ClassId.ELEMENTAL_MASTER, 1.);
		weight.put(ClassId.EVAS_SAINT, -0.6);
		weight.put(ClassId.SHILLIEN_TEMPLAR, 0.8);
		weight.put(ClassId.SPECTRAL_DANCER, 0.5);
		weight.put(ClassId.GHOST_HUNTER, 0.4);
		weight.put(ClassId.GHOST_SENTINEL, 0.3);
		weight.put(ClassId.STORM_SCREAMER, 0.3);
		weight.put(ClassId.SPECTRAL_MASTER, 1.);
		weight.put(ClassId.SHILLIEN_SAINT, -0.6);
		weight.put(ClassId.TITAN, 0.3);
		weight.put(ClassId.GRAND_KHAVATARI, 0.2);
		weight.put(ClassId.DOMINATOR, 0.1);
		weight.put(ClassId.DOOMCRYER, 0.1);
		weight.put(ClassId.FORTUNE_SEEKER, 0.9);
		weight.put(ClassId.MAESTRO, 0.7);
		weight.put(ClassId.DOOMBRINGER, 0.2);
		weight.put(ClassId.TRICKSTER, 0.5);
		weight.put(ClassId.JUDICATOR, 0.1);
		weight.put(ClassId.M_SOUL_HOUND, 0.3);
		weight.put(ClassId.F_SOUL_HOUND, 0.3);
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
			if (cha.isPlayer())
			{
				inzone.add(cha.getPlayer());
			}
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
			if (cha.isPlayer() && inzone.contains(cha.getPlayer()))
			{
				inzone.remove(cha.getPlayer());
			}
		}
	}
	
	/**
	 * @author Mobius
	 */
	private class BuffTask extends RunnableImpl
	{
		/**
		 * Constructor for BuffTask.
		 */
		public BuffTask()
		{
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Method runImpl.
		 */
		@Override
		public void runImpl()
		{
			for (Player izp : inzone)
			{
				if (getBuffLevel(izp) > 0)
				{
					izp.altOnMagicUseTimer(izp, SkillTable.getInstance().getInfo(6885, getBuffLevel(izp)));
				}
			}
		}
	}
	
	/**
	 * Method getBuffLevel.
	 * @param pc Player
	 * @return int
	 */
	int getBuffLevel(Player pc)
	{
		if (pc.getParty() == null)
		{
			return 0;
		}
		Party party = pc.getParty();
		if (party.getMemberCount() < 5)
		{
			return 0;
		}
		for (Player p : party)
		{
			if ((p.getLevel() < 80) || !p.isInZone(zone))
			{
				return 0;
			}
		}
		double points = 0;
		int count = party.getMemberCount();
		for (Player p : party)
		{
			points += weight.get(p.getClassId());
		}
		return (int) Math.max(0, Math.min(3, Math.round(points * getCoefficient(count))));
	}
	
	/**
	 * Method getCoefficient.
	 * @param count int
	 * @return double
	 */
	private double getCoefficient(int count)
	{
		double cf;
		switch (count)
		{
			case 4:
				cf = 0.7;
				break;
			case 5:
				cf = 0.75;
				break;
			case 6:
				cf = 0.8;
				break;
			case 7:
				cf = 0.85;
				break;
			case 8:
				cf = 0.9;
				break;
			case 9:
				cf = 0.95;
				break;
			default:
				cf = 1;
		}
		return cf;
	}
	
	/**
	 * Method onLoad.
	 * @see lineage2.gameserver.scripts.ScriptFile#onLoad()
	 */
	@Override
	public void onLoad()
	{
		_zoneListener = new ZoneListener();
		zone = ReflectionUtils.getZone("[dragon_valley]");
		zone.addListener(_zoneListener);
		buffTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new BuffTask(), 1000L, 60000L);
	}
	
	/**
	 * Method onReload.
	 * @see lineage2.gameserver.scripts.ScriptFile#onReload()
	 */
	@Override
	public void onReload()
	{
		buffTask.cancel(false);
		zone.removeListener(_zoneListener);
	}
	
	/**
	 * Method onShutdown.
	 * @see lineage2.gameserver.scripts.ScriptFile#onShutdown()
	 */
	@Override
	public void onShutdown()
	{
	}
}
