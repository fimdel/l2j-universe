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
package lineage2.gameserver.model;

import java.util.ArrayList;

import lineage2.commons.collections.MultiValueSet;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.data.xml.holder.NpcHolder;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.npc.NpcTemplate;
import lineage2.gameserver.templates.spawn.SpawnRange;
import lineage2.gameserver.utils.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SimpleSpawner extends Spawner
{
	/**
	 * Field serialVersionUID. (value is 1)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _log.
	 */
	@SuppressWarnings("unused")
	private static final Logger _log = LoggerFactory.getLogger(SimpleSpawner.class);
	/**
	 * Field _npcTemplate.
	 */
	private final NpcTemplate _npcTemplate;
	/**
	 * Field _heading. Field _locz. Field _locy. Field _locx.
	 */
	private int _locx, _locy, _locz, _heading;
	/**
	 * Field _territory.
	 */
	private Territory _territory;
	
	/**
	 * Constructor for SimpleSpawner.
	 * @param mobTemplate NpcTemplate
	 */
	public SimpleSpawner(NpcTemplate mobTemplate)
	{
		if (mobTemplate == null)
		{
			throw new NullPointerException();
		}
		_npcTemplate = mobTemplate;
		_spawned = new ArrayList<>(1);
	}
	
	/**
	 * Constructor for SimpleSpawner.
	 * @param npcId int
	 */
	public SimpleSpawner(int npcId)
	{
		NpcTemplate mobTemplate = NpcHolder.getInstance().getTemplate(npcId);
		if (mobTemplate == null)
		{
			throw new NullPointerException("Not find npc: " + npcId);
		}
		_npcTemplate = mobTemplate;
		_spawned = new ArrayList<>(1);
	}
	
	/**
	 * Method getAmount.
	 * @return int
	 */
	public int getAmount()
	{
		return _maximumCount;
	}
	
	/**
	 * Method getSpawnedCount.
	 * @return int
	 */
	public int getSpawnedCount()
	{
		return _currentCount;
	}
	
	/**
	 * Method getSheduledCount.
	 * @return int
	 */
	public int getSheduledCount()
	{
		return _scheduledCount;
	}
	
	/**
	 * Method getTerritory.
	 * @return Territory
	 */
	public Territory getTerritory()
	{
		return _territory;
	}
	
	/**
	 * Method getLoc.
	 * @return Location
	 */
	public Location getLoc()
	{
		return new Location(_locx, _locy, _locz);
	}
	
	/**
	 * Method getLocx.
	 * @return int
	 */
	public int getLocx()
	{
		return _locx;
	}
	
	/**
	 * Method getLocy.
	 * @return int
	 */
	public int getLocy()
	{
		return _locy;
	}
	
	/**
	 * Method getLocz.
	 * @return int
	 */
	public int getLocz()
	{
		return _locz;
	}
	
	/**
	 * Method getCurrentNpcId.
	 * @return int
	 */
	@Override
	public int getCurrentNpcId()
	{
		return _npcTemplate.getNpcId();
	}
	
	/**
	 * Method getCurrentSpawnRange.
	 * @return SpawnRange
	 */
	@Override
	public SpawnRange getCurrentSpawnRange()
	{
		if ((_locx == 0) && (_locz == 0))
		{
			return _territory;
		}
		return getLoc();
	}
	
	/**
	 * Method getHeading.
	 * @return int
	 */
	public int getHeading()
	{
		return _heading;
	}
	
	/**
	 * Method restoreAmount.
	 */
	public void restoreAmount()
	{
		_maximumCount = _referenceCount;
	}
	
	/**
	 * Method setTerritory.
	 * @param territory Territory
	 */
	public void setTerritory(Territory territory)
	{
		_territory = territory;
	}
	
	/**
	 * Method setLoc.
	 * @param loc Location
	 */
	public void setLoc(Location loc)
	{
		_locx = loc.x;
		_locy = loc.y;
		_locz = loc.z;
		_heading = loc.h;
	}
	
	/**
	 * Method setLocx.
	 * @param locx int
	 */
	public void setLocx(int locx)
	{
		_locx = locx;
	}
	
	/**
	 * Method setLocy.
	 * @param locy int
	 */
	public void setLocy(int locy)
	{
		_locy = locy;
	}
	
	/**
	 * Method setLocz.
	 * @param locz int
	 */
	public void setLocz(int locz)
	{
		_locz = locz;
	}
	
	/**
	 * Method setHeading.
	 * @param heading int
	 */
	public void setHeading(int heading)
	{
		_heading = heading;
	}
	
	/**
	 * Method decreaseCount.
	 * @param oldNpc NpcInstance
	 */
	@Override
	public void decreaseCount(NpcInstance oldNpc)
	{
		decreaseCount0(_npcTemplate, oldNpc, oldNpc.getDeadTime());
	}
	
	/**
	 * Method doSpawn.
	 * @param spawn boolean
	 * @return NpcInstance
	 */
	@Override
	public NpcInstance doSpawn(boolean spawn)
	{
		return doSpawn0(_npcTemplate, spawn, StatsSet.EMPTY);
	}
	
	/**
	 * Method initNpc.
	 * @param mob NpcInstance
	 * @param spawn boolean
	 * @param set MultiValueSet<String>
	 * @return NpcInstance
	 */
	@Override
	protected NpcInstance initNpc(NpcInstance mob, boolean spawn, MultiValueSet<String> set)
	{
		Location newLoc;
		if (_territory != null)
		{
			newLoc = _territory.getRandomLoc(_reflection.getGeoIndex());
			newLoc.setH(Rnd.get(0xFFFF));
		}
		else
		{
			newLoc = getLoc();
			newLoc.h = getHeading() == -1 ? Rnd.get(0xFFFF) : getHeading();
		}
		return initNpc0(mob, newLoc, spawn, set);
	}
	
	/**
	 * Method respawnNpc.
	 * @param oldNpc NpcInstance
	 */
	@Override
	public void respawnNpc(NpcInstance oldNpc)
	{
		oldNpc.refreshID();
		initNpc(oldNpc, true, StatsSet.EMPTY);
	}
	
	/**
	 * Method clone.
	 * @return SimpleSpawner
	 */
	@Override
	public SimpleSpawner clone()
	{
		SimpleSpawner spawnDat = new SimpleSpawner(_npcTemplate);
		spawnDat.setTerritory(_territory);
		spawnDat.setLocx(_locx);
		spawnDat.setLocy(_locy);
		spawnDat.setLocz(_locz);
		spawnDat.setHeading(_heading);
		spawnDat.setAmount(_maximumCount);
		spawnDat.setRespawnDelay(_respawnDelay, _respawnDelayRandom);
		return spawnDat;
	}
}
