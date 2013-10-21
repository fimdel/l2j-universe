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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lineage2.commons.collections.MultiValueSet;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.spawn.SpawnNpcInfo;
import lineage2.gameserver.templates.spawn.SpawnRange;
import lineage2.gameserver.templates.spawn.SpawnTemplate;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class HardSpawner extends Spawner
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field _template.
	 */
	private final SpawnTemplate _template;
	/**
	 * Field _pointIndex.
	 */
	private int _pointIndex;
	/**
	 * Field _npcIndex.
	 */
	private int _npcIndex;
	/**
	 * Field _reSpawned.
	 */
	private final List<NpcInstance> _reSpawned = new CopyOnWriteArrayList<>();
	
	/**
	 * Constructor for HardSpawner.
	 * @param template SpawnTemplate
	 */
	public HardSpawner(SpawnTemplate template)
	{
		_template = template;
		_spawned = new CopyOnWriteArrayList<>();
	}
	
	/**
	 * Method decreaseCount.
	 * @param oldNpc NpcInstance
	 */
	@Override
	public void decreaseCount(NpcInstance oldNpc)
	{
		oldNpc.setSpawn(null);
		oldNpc.deleteMe();
		_spawned.remove(oldNpc);
		SpawnNpcInfo npcInfo = getNextNpcInfo();
		NpcInstance npc = npcInfo.getTemplate().getNewInstance();
		npc.setSpawn(this);
		_reSpawned.add(npc);
		decreaseCount0(npcInfo.getTemplate(), npc, oldNpc.getDeadTime());
	}
	
	/**
	 * Method doSpawn.
	 * @param spawn boolean
	 * @return NpcInstance
	 */
	@Override
	public NpcInstance doSpawn(boolean spawn)
	{
		SpawnNpcInfo npcInfo = getNextNpcInfo();
		return doSpawn0(npcInfo.getTemplate(), spawn, npcInfo.getParameters());
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
		_reSpawned.remove(mob);
		SpawnRange range = _template.getSpawnRange(getNextRangeId());
		mob.setSpawnRange(range);
		return initNpc0(mob, range.getRandomLoc(getReflection().getGeoIndex()), spawn, set);
	}
	
	/**
	 * Method getCurrentNpcId.
	 * @return int
	 */
	@Override
	public int getCurrentNpcId()
	{
		SpawnNpcInfo npcInfo = _template.getNpcId(_npcIndex);
		if (npcInfo.getTemplate() == null)
		{
			_log.warn("NpcIndex not found: " + _npcIndex);
			return 0;
		}
		return npcInfo.getTemplate().npcId;
	}
	
	/**
	 * Method getCurrentSpawnRange.
	 * @return SpawnRange
	 */
	@Override
	public SpawnRange getCurrentSpawnRange()
	{
		return _template.getSpawnRange(_pointIndex);
	}
	
	/**
	 * Method respawnNpc.
	 * @param oldNpc NpcInstance
	 */
	@Override
	public void respawnNpc(NpcInstance oldNpc)
	{
		initNpc(oldNpc, true, StatsSet.EMPTY);
	}
	
	/**
	 * Method deleteAll.
	 */
	@Override
	public void deleteAll()
	{
		super.deleteAll();
		for (NpcInstance npc : _reSpawned)
		{
			npc.setSpawn(null);
			npc.deleteMe();
		}
		_reSpawned.clear();
	}
	
	/**
	 * Method getNextNpcInfo.
	 * @return SpawnNpcInfo
	 */
	private synchronized SpawnNpcInfo getNextNpcInfo()
	{
		int old = _npcIndex++;
		if (_npcIndex >= _template.getNpcSize())
		{
			_npcIndex = 0;
		}
		SpawnNpcInfo npcInfo = _template.getNpcId(old);
		if (npcInfo.getMax() > 0)
		{
			int count = 0;
			for (NpcInstance npc : _spawned)
			{
				if (npc.getNpcId() == npcInfo.getTemplate().getNpcId())
				{
					count++;
				}
			}
			if (count >= npcInfo.getMax())
			{
				return getNextNpcInfo();
			}
		}
		return npcInfo;
	}
	
	/**
	 * Method getNextRangeId.
	 * @return int
	 */
	private synchronized int getNextRangeId()
	{
		int old = _pointIndex++;
		if (_pointIndex >= _template.getSpawnRangeSize())
		{
			_pointIndex = 0;
		}
		return old;
	}
	
	/**
	 * Method clone.
	 * @return HardSpawner
	 */
	@Override
	public HardSpawner clone()
	{
		HardSpawner spawnDat = new HardSpawner(_template);
		spawnDat.setAmount(_maximumCount);
		spawnDat.setRespawnDelay(_respawnDelay, _respawnDelayRandom);
		spawnDat.setRespawnTime(0);
		return spawnDat;
	}
}
