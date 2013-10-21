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

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lineage2.commons.collections.LazyArrayList;
import lineage2.commons.util.Rnd;
import lineage2.gameserver.model.instances.NpcInstance;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class AggroList
{
	/**
	 * @author Mobius
	 */
	private abstract class DamageHate
	{
		/**
		 * Field hate.
		 */
		public int hate;
		/**
		 * Field damage.
		 */
		public int damage;
	}
	
	/**
	 * @author Mobius
	 */
	public class HateInfo extends DamageHate
	{
		/**
		 * Field attacker.
		 */
		public final Creature attacker;
		
		/**
		 * Constructor for HateInfo.
		 * @param attacker Creature
		 * @param ai AggroInfo
		 */
		@SuppressWarnings("synthetic-access")
		HateInfo(Creature attacker, AggroInfo ai)
		{
			this.attacker = attacker;
			hate = ai.hate;
			damage = ai.damage;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public class AggroInfo extends DamageHate
	{
		/**
		 * Field attackerId.
		 */
		public final int attackerId;
		
		/**
		 * Constructor for AggroInfo.
		 * @param attacker Creature
		 */
		@SuppressWarnings("synthetic-access")
		AggroInfo(Creature attacker)
		{
			attackerId = attacker.getObjectId();
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class DamageComparator implements Comparator<DamageHate>
	{
		/**
		 * Field instance.
		 */
		private static Comparator<DamageHate> instance = new DamageComparator();
		
		/**
		 * Method getInstance.
		 * @return Comparator<DamageHate>
		 */
		public static Comparator<DamageHate> getInstance()
		{
			return instance;
		}
		
		/**
		 * Constructor for DamageComparator.
		 */
		DamageComparator()
		{
		}
		
		/**
		 * Method compare.
		 * @param o1 DamageHate
		 * @param o2 DamageHate
		 * @return int
		 */
		@Override
		public int compare(DamageHate o1, DamageHate o2)
		{
			return o2.damage - o1.damage;
		}
	}
	
	/**
	 * @author Mobius
	 */
	public static class HateComparator implements Comparator<DamageHate>
	{
		/**
		 * Field instance.
		 */
		private static Comparator<DamageHate> instance = new HateComparator();
		
		/**
		 * Method getInstance.
		 * @return Comparator<DamageHate>
		 */
		public static Comparator<DamageHate> getInstance()
		{
			return instance;
		}
		
		/**
		 * Constructor for HateComparator.
		 */
		HateComparator()
		{
		}
		
		/**
		 * Method compare.
		 * @param o1 DamageHate
		 * @param o2 DamageHate
		 * @return int
		 */
		@Override
		public int compare(DamageHate o1, DamageHate o2)
		{
			int diff = o2.hate - o1.hate;
			return diff == 0 ? o2.damage - o1.damage : diff;
		}
	}
	
	/**
	 * Field npc.
	 */
	private final NpcInstance npc;
	/**
	 * Field hateList.
	 */
	private final TIntObjectHashMap<AggroInfo> hateList = new TIntObjectHashMap<>();
	/**
	 * Field lock.
	 */
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	/**
	 * Field readLock.
	 */
	private final Lock readLock = lock.readLock();
	/**
	 * Field writeLock.
	 */
	private final Lock writeLock = lock.writeLock();
	
	/**
	 * Constructor for AggroList.
	 * @param npc NpcInstance
	 */
	public AggroList(NpcInstance npc)
	{
		this.npc = npc;
	}
	
	/**
	 * Method addDamageHate.
	 * @param attacker Creature
	 * @param damage int
	 * @param aggro int
	 */
	public void addDamageHate(Creature attacker, int damage, int aggro)
	{
		damage = Math.max(damage, 0);
		if ((damage == 0) && (aggro == 0))
		{
			return;
		}
		writeLock.lock();
		try
		{
			AggroInfo ai;
			if ((ai = hateList.get(attacker.getObjectId())) == null)
			{
				hateList.put(attacker.getObjectId(), ai = new AggroInfo(attacker));
			}
			ai.damage += damage;
			ai.hate += aggro;
			ai.damage = Math.max(ai.damage, 0);
			ai.hate = Math.max(ai.hate, 0);
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	/**
	 * Method get.
	 * @param attacker Creature
	 * @return AggroInfo
	 */
	public AggroInfo get(Creature attacker)
	{
		readLock.lock();
		try
		{
			return hateList.get(attacker.getObjectId());
		}
		finally
		{
			readLock.unlock();
		}
	}
	
	/**
	 * Method remove.
	 * @param attacker Creature
	 * @param onlyHate boolean
	 */
	public void remove(Creature attacker, boolean onlyHate)
	{
		writeLock.lock();
		try
		{
			if (!onlyHate)
			{
				hateList.remove(attacker.getObjectId());
				return;
			}
			AggroInfo ai = hateList.get(attacker.getObjectId());
			if (ai != null)
			{
				ai.hate = 0;
			}
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	/**
	 * Method clear.
	 */
	public void clear()
	{
		clear(false);
	}
	
	/**
	 * Method clear.
	 * @param onlyHate boolean
	 */
	public void clear(boolean onlyHate)
	{
		writeLock.lock();
		try
		{
			if (hateList.isEmpty())
			{
				return;
			}
			if (!onlyHate)
			{
				hateList.clear();
				return;
			}
			AggroInfo ai;
			for (TIntObjectIterator<AggroInfo> itr = hateList.iterator(); itr.hasNext();)
			{
				itr.advance();
				ai = itr.value();
				ai.hate = 0;
				if (ai.damage == 0)
				{
					itr.remove();
				}
			}
		}
		finally
		{
			writeLock.unlock();
		}
	}
	
	/**
	 * Method isEmpty.
	 * @return boolean
	 */
	public boolean isEmpty()
	{
		readLock.lock();
		try
		{
			return hateList.isEmpty();
		}
		finally
		{
			readLock.unlock();
		}
	}
	
	/**
	 * Method getHateList.
	 * @return List<Creature>
	 */
	public List<Creature> getHateList()
	{
		AggroInfo[] hated;
		readLock.lock();
		try
		{
			if (hateList.isEmpty())
			{
				return Collections.emptyList();
			}
			hated = hateList.values(new AggroInfo[hateList.size()]);
		}
		finally
		{
			readLock.unlock();
		}
		Arrays.sort(hated, HateComparator.getInstance());
		if (hated[0].hate == 0)
		{
			return Collections.emptyList();
		}
		List<Creature> hateList = new LazyArrayList<>();
		List<Creature> chars = World.getAroundCharacters(npc);
		AggroInfo ai;
		for (AggroInfo element : hated)
		{
			ai = element;
			if (ai.hate == 0)
			{
				continue;
			}
			for (Creature cha : chars)
			{
				if (cha.getObjectId() == ai.attackerId)
				{
					hateList.add(cha);
					break;
				}
			}
		}
		return hateList;
	}
	
	/**
	 * Method getMostHated.
	 * @return Creature
	 */
	public Creature getMostHated()
	{
		AggroInfo[] hated;
		readLock.lock();
		try
		{
			if (hateList.isEmpty())
			{
				return null;
			}
			hated = hateList.values(new AggroInfo[hateList.size()]);
		}
		finally
		{
			readLock.unlock();
		}
		Arrays.sort(hated, HateComparator.getInstance());
		if (hated[0].hate == 0)
		{
			return null;
		}
		List<Creature> chars = World.getAroundCharacters(npc);
		AggroInfo ai;
		loop:
		for (int i = 0; i < hated.length; i++)
		{
			ai = hated[i];
			if (ai.hate == 0)
			{
				continue;
			}
			for (Creature cha : chars)
			{
				if (cha.getObjectId() == ai.attackerId)
				{
					if (cha.isDead())
					{
						continue loop;
					}
					return cha;
				}
			}
		}
		return null;
	}
	
	/**
	 * Method getRandomHated.
	 * @return Creature
	 */
	public Creature getRandomHated()
	{
		AggroInfo[] hated;
		readLock.lock();
		try
		{
			if (hateList.isEmpty())
			{
				return null;
			}
			hated = hateList.values(new AggroInfo[hateList.size()]);
		}
		finally
		{
			readLock.unlock();
		}
		Arrays.sort(hated, HateComparator.getInstance());
		if (hated[0].hate == 0)
		{
			return null;
		}
		List<Creature> chars = World.getAroundCharacters(npc);
		LazyArrayList<Creature> randomHated = LazyArrayList.newInstance();
		AggroInfo ai;
		Creature mostHated;
		loop:
		for (int i = 0; i < hated.length; i++)
		{
			ai = hated[i];
			if (ai.hate == 0)
			{
				continue;
			}
			for (Creature cha : chars)
			{
				if (cha.getObjectId() == ai.attackerId)
				{
					if (cha.isDead())
					{
						continue loop;
					}
					randomHated.add(cha);
					break;
				}
			}
		}
		if (randomHated.isEmpty())
		{
			mostHated = null;
		}
		else
		{
			mostHated = randomHated.get(Rnd.get(randomHated.size()));
		}
		LazyArrayList.recycle(randomHated);
		return mostHated;
	}
	
	/**
	 * Method getTopDamager.
	 * @return Creature
	 */
	public Creature getTopDamager()
	{
		AggroInfo[] hated;
		readLock.lock();
		try
		{
			if (hateList.isEmpty())
			{
				return null;
			}
			hated = hateList.values(new AggroInfo[hateList.size()]);
		}
		finally
		{
			readLock.unlock();
		}
		Creature topDamager = null;
		Arrays.sort(hated, DamageComparator.getInstance());
		if (hated[0].damage == 0)
		{
			return null;
		}
		List<Creature> chars = World.getAroundCharacters(npc);
		AggroInfo ai;
		for (AggroInfo element : hated)
		{
			ai = element;
			if (ai.damage == 0)
			{
				continue;
			}
			for (Creature cha : chars)
			{
				if (cha.getObjectId() == ai.attackerId)
				{
					topDamager = cha;
					return topDamager;
				}
			}
		}
		return null;
	}
	
	/**
	 * Method getCharMap.
	 * @return Map<Creature,HateInfo>
	 */
	public Map<Creature, HateInfo> getCharMap()
	{
		if (isEmpty())
		{
			return Collections.emptyMap();
		}
		Map<Creature, HateInfo> aggroMap = new HashMap<>();
		List<Creature> chars = World.getAroundCharacters(npc);
		readLock.lock();
		try
		{
			AggroInfo ai;
			for (TIntObjectIterator<AggroInfo> itr = hateList.iterator(); itr.hasNext();)
			{
				itr.advance();
				ai = itr.value();
				if ((ai.damage == 0) && (ai.hate == 0))
				{
					continue;
				}
				for (Creature attacker : chars)
				{
					if (attacker.getObjectId() == ai.attackerId)
					{
						aggroMap.put(attacker, new HateInfo(attacker, ai));
						break;
					}
				}
			}
		}
		finally
		{
			readLock.unlock();
		}
		return aggroMap;
	}
	
	/**
	 * Method getPlayableMap.
	 * @return Map<Playable,HateInfo>
	 */
	public Map<Playable, HateInfo> getPlayableMap()
	{
		if (isEmpty())
		{
			return Collections.emptyMap();
		}
		Map<Playable, HateInfo> aggroMap = new HashMap<>();
		List<Playable> chars = World.getAroundPlayables(npc);
		readLock.lock();
		try
		{
			AggroInfo ai;
			for (TIntObjectIterator<AggroInfo> itr = hateList.iterator(); itr.hasNext();)
			{
				itr.advance();
				ai = itr.value();
				if ((ai.damage == 0) && (ai.hate == 0))
				{
					continue;
				}
				for (Playable attacker : chars)
				{
					if (attacker.getObjectId() == ai.attackerId)
					{
						aggroMap.put(attacker, new HateInfo(attacker, ai));
						break;
					}
				}
			}
		}
		finally
		{
			readLock.unlock();
		}
		return aggroMap;
	}
}
