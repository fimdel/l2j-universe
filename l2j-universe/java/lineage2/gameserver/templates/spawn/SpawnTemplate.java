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
package lineage2.gameserver.templates.spawn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class SpawnTemplate
{
	/**
	 * Field _periodOfDay.
	 */
	private final PeriodOfDay _periodOfDay;
	/**
	 * Field _count.
	 */
	private final int _count;
	/**
	 * Field _respawn.
	 */
	private final int _respawn;
	/**
	 * Field _respawnRandom.
	 */
	private final int _respawnRandom;
	/**
	 * Field _npcList.
	 */
	private final List<SpawnNpcInfo> _npcList = new ArrayList<>(1);
	/**
	 * Field _spawnRangeList.
	 */
	private final List<SpawnRange> _spawnRangeList = new ArrayList<>(1);
	
	/**
	 * Constructor for SpawnTemplate.
	 * @param periodOfDay PeriodOfDay
	 * @param count int
	 * @param respawn int
	 * @param respawnRandom int
	 */
	public SpawnTemplate(PeriodOfDay periodOfDay, int count, int respawn, int respawnRandom)
	{
		_periodOfDay = periodOfDay;
		_count = count;
		_respawn = respawn;
		_respawnRandom = respawnRandom;
	}
	
	/**
	 * Method addSpawnRange.
	 * @param range SpawnRange
	 */
	public void addSpawnRange(SpawnRange range)
	{
		_spawnRangeList.add(range);
	}
	
	/**
	 * Method getSpawnRange.
	 * @param index int
	 * @return SpawnRange
	 */
	public SpawnRange getSpawnRange(int index)
	{
		return _spawnRangeList.get(index);
	}
	
	/**
	 * Method addNpc.
	 * @param info SpawnNpcInfo
	 */
	public void addNpc(SpawnNpcInfo info)
	{
		_npcList.add(info);
	}
	
	/**
	 * Method getNpcId.
	 * @param index int
	 * @return SpawnNpcInfo
	 */
	public SpawnNpcInfo getNpcId(int index)
	{
		return _npcList.get(index);
	}
	
	/**
	 * Method getNpcSize.
	 * @return int
	 */
	public int getNpcSize()
	{
		return _npcList.size();
	}
	
	/**
	 * Method getSpawnRangeSize.
	 * @return int
	 */
	public int getSpawnRangeSize()
	{
		return _spawnRangeList.size();
	}
	
	/**
	 * Method getCount.
	 * @return int
	 */
	public int getCount()
	{
		return _count;
	}
	
	/**
	 * Method getRespawn.
	 * @return int
	 */
	public int getRespawn()
	{
		return _respawn;
	}
	
	/**
	 * Method getRespawnRandom.
	 * @return int
	 */
	public int getRespawnRandom()
	{
		return _respawnRandom;
	}
	
	/**
	 * Method getPeriodOfDay.
	 * @return PeriodOfDay
	 */
	public PeriodOfDay getPeriodOfDay()
	{
		return _periodOfDay;
	}
}
